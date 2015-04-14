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

    static def convertValue(value) {
        if (value instanceof List) return value.collect { convertValue(it) }
        if (needsConvertRef(value)) return toRef(value)
        if (needsConvertGetAtt(value)) return toGetAtt(value)
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
        def keys = []
        def buf = v.substring("FindInMap:".length())
        while (0 < buf.length()) {
            if (buf.startsWith("[Ref:")) {
                def ref = toRef(buf.substring(1, buf.indexOf("]")))
                keys << ref
                buf = buf.substring(buf.indexOf("]") + 1)
                if (buf.indexOf(':') == 0) buf = buf.substring(1)
            } else {
                def idx = buf.indexOf(':')
                if (idx == -1) {
                    keys << buf
                    break
                } else {
                    keys << buf.substring(0, idx)
                    buf = buf.substring(idx + 1)
                }
            }
        }
        ["Fn::FindInMap": keys]
    }

    private static boolean needsConvertGetAtt(v) {
        String.class.isInstance(v) && v.startsWith("GetAtt:")
    }

    private static Map toGetAtt(String v) {
        def keys = v.split(":")
        ["Fn::GetAtt": [keys[1], keys[2]]]
    }

}
