package jp.classmethod.aws.cloudformation.util

/**
 * Created by watanabeshuji on 2015/04/04.
 */
class Params {

    static def convertUserData(String userData) {
        def result = []
        userData.split("\n").each {
            result.addAll(splitLine(it))
        }
        result
    }

    private static List<String> splitLine(String line) {
        if (line.indexOf("[Ref:") == -1) return ["${line}\\n"]
        String buf = line
        List result = []
        while (true) {
            def m = (buf =~ /(.*?)\[Ref:(.*?)\](.*)/)
            if (m.matches()) {
                result << m[0][1]
                result << [Ref: m[0][2]]
                buf = m[0][3]
            } else {
                result << "${buf}\\n"
                break
            }
        }
        result
    }


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
