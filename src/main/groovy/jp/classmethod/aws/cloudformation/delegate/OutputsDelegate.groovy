package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.Output

/**
 * Created by watanabeshuji on 2015/03/27.
 */
class OutputsDelegate {

    List outputs

    OutputsDelegate(List outputs) {
        this.outputs = outputs
    }

    def methodMissing(String name, args) {
        outputs << Output.newInstance(name, args[0])
    }

}
