package jp.classmethod.aws.cloudformation.util

/**
 * Created by watanabeshuji on 2015/03/26.
 */
class Values {


    def value(v) {
        if (v == null || v == '-') return null
        if (convertToRef(v)) return toRef(v)
        if (convertToGetAttr(v)) return toGetAttr(v)
        if (convertToMap(v)) return toMap(v)
        return v
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
        ['Fn::FindInMap': value.split(':').collect({ convertToRef(it) ? toRef(it) : it })]
    }

    private def convertToGetAttr(value) {
        value ==~ /A\[[a-zA-Z0-9_\\-|]+\]/
    }

    private def toGetAttr(value) {
        ['Fn::GetAtt': value.substring(2, value.length() - 1).split('\\|')]
    }
}
