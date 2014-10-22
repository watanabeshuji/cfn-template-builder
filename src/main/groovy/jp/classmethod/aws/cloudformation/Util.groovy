package jp.classmethod.aws.cloudformation

class Util {
    static def toCamelCase(name) {
        if (name == null) return null
        if (String.class.isInstance(name)) {
            name.split('-').collect { it.substring(0, 1).toUpperCase() + it.substring(1) }.join().trim()
        } else {
            name
        }
    }

    static def ref(value) {
        Map.class.isInstance(value) ? value : ['Ref': value]
    }

    static def load(File file, cls) {
        if (!file.exists()) return []
        def result = []
        def meta
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                result << cls(meta.newSource(line.split(',')))
            }
        }
        result
    }
}
