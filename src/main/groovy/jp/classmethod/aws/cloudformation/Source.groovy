package jp.classmethod.aws.cloudformation

/**
 * Created by watanabeshuji on 2014/08/13.
 */
class Source {

    def source
    SourceMeta meta

    def Source(source, SourceMeta meta) {
        this.source = source.collect { it.trim() }
        this.meta = meta
    }

    def containsKey(key) {
        (meta.indexOf(key) != -1)
    }

    def value(key) {
        if (!containsKey(key)) return null
        def idx = meta.indexOf(key)
        if (idx == -1) return null
        def v = source[idx]
        if (v == null || v == '-') return null
        if (convertToRef(v)) return toRef(v)
        if (convertToMap(v)) return toMap(v)
        return v
    }


    def bool(key) {
        def b = value(key)
        b != null ? Boolean.valueOf(b) : null
    }

    def integer(key) {
        def v = value(key)
        v != null ? Integer.valueOf(v) : null
    }

    def list(key) {
        def v = source[meta.indexOf(key)]
        if (v == null || v == '-') return []
        v.split(/\|/).collect {
            it.contains(':')  ? toMap(it) : it
        }
    }

    def camelCase(key) {
        Util.toCamelCase(value(key))
    }

    def camelCaseList(key) {
        def idx = meta.indexOf(key)
        if (idx < 0) return []
        def v = source[idx]
        if (v == null || v == '-') return null
        v.split(/\|/).collect {
            it.contains(':')  ? toMap(it) : Util.toCamelCase(it)
        }
    }

    private def convertToRef(value) {
        value ==~ /P\[[a-zA-Z0-9_\\-]+\]/
    }

    private def toRef(value) {
        ['Ref': value.substring(2, value.length() - 1)]
    }

    private def convertToMap(value) {
        value.indexOf(':') != -1 && !value.startsWith('arn')
    }

    private def toMap(value) {
        ['Fn::FindInMap': value.split(':').collect({ convertToRef(it) ? toRef(it) : it }) ]
    }


}
