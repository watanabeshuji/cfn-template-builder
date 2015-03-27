package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.CloudFormation

/**
 * Created by watanabeshuji on 2015/03/26.
 */
class CloudFormationDelegate {

    CloudFormation cfn

    CloudFormationDelegate(CloudFormation cfn) {
        this.cfn = cfn
    }

    void aWSTemplateFormatVersion(String version) {
        cfn.AWSTemplateFormatVersion = version
    }

    void description(String desc) {
        cfn.Description = desc
    }

    void setMappings(Map mappings) {
        cfn.Mappings.putAll(mappings)
    }

    void mappings(Closure cl) {
        cl.delegate = new MappingsDelegate(cfn.Mappings)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    void parameters(Closure cl) {
        cl.delegate = new ParametersDelegate(cfn.Parameters)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

    void resources(Closure cl) {
        cl.delegate = new ResourcesDelegate(cfn.Resources)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }
}
