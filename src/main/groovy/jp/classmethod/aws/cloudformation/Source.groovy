package jp.classmethod.aws.cloudformation

/**
 * Created by watanabeshuji on 2014/08/13.
 */
class Source {

    def source
    def meta

    def Source(source, meta) {
        this.source = source.collect { it.trim() }
        this.meta = meta
    }

    def containsKey(key) {
        (meta.indexOf(key) != -1)
    }

    def value(key) {
        if (!containsKey(key)) return null
        def v = source[meta.indexOf(key)]
        if (v == null || v == '-') return null
        if (v.indexOf(':') != -1) {
            v = toMap(v)
        }
        return v
    }

    def bool(key) {
        Boolean.valueOf(value(key))
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
//        value(key).split()
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

    def toMap(value) {
        ['Fn::FindInMap': value.split(':') ]
    }

}
