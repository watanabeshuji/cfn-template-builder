package jp.classmethod.aws.cloudformation

class Util {
    static def toCamelCase(name) {
        if (name == null) return null
        name.split('-').collect { it.substring(0, 1).toUpperCase() + it.substring(1) }.join().trim()
    }
}
