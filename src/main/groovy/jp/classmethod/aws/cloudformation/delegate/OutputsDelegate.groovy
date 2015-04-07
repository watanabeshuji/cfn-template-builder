package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.util.Params

/**
 * Created by watanabeshuji on 2015/03/27.
 */
class OutputsDelegate {

    Map outputs

    OutputsDelegate(Map outputs) {
        this.outputs = outputs
    }

    def methodMissing(String name, args) {
        outputs[name] = Params.convertValue(args[0])
    }

}
