package jp.classmethod.aws.cloudformation.delegate
/**
 * Created by watanabeshuji on 2015/03/27.
 */
class MappingsDelegate {

    Map mappings

    MappingsDelegate(Map mappings) {
        this.mappings = mappings
    }

    def methodMissing(String name, args) {
        mappings[name] = args[0]
    }

}
