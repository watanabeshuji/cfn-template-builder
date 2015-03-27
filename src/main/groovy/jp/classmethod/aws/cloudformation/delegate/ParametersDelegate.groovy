package jp.classmethod.aws.cloudformation.delegate
/**
 * Created by watanabeshuji on 2015/03/27.
 */
class ParametersDelegate {

    Map parameters

    ParametersDelegate(Map parameters) {
        this.parameters = parameters
    }

    def methodMissing(String name, args){
        parameters[name] = args[0]
    }

}
