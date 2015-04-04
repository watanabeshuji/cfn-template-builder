package jp.classmethod.aws.cloudformation.util

/**
 * Created by watanabeshuji on 2015/04/04.
 */
class Params {

    static Map convert(Map params) {
        params.each { k, v ->
            params[k] = convertValue(v)
        }
        params
    }

    private static def convertValue(value) {
        if (value instanceof List) return value.collect { convertValue(it) }
        if (needsConvertRef(value)) return toRef(value)
        if (needsConvertFindMap(value)) return toFindMap(value)
        value
    }

    private static boolean needsConvertRef(v) {
        String.class.isInstance(v) && v.startsWith("Ref:")
    }

    private static Map toRef(String v) {
        def keys = v.split(":")
        [Ref: keys[1]]
    }

    private static boolean needsConvertFindMap(v) {
        String.class.isInstance(v) && v.startsWith("FindInMap:")
    }

    private static Map toFindMap(String v) {
        def keys = v.split(":")
        ["Fn::FindInMap": [keys[1], keys[2], keys[3]]]
    }
}
