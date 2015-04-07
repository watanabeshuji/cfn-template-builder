package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.CloudFormation
import jp.classmethod.aws.cloudformation.DSLSupport
import jp.classmethod.aws.cloudformation.InvalidResourceException

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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

    void resources(String resourcePath) {
        if (resourcePath.startsWith("/")) {
            URL resource = getClass().getResource(resourcePath)
            if (resource == null) throw new InvalidResourceException("not found: ${resourcePath}")
            List resources = DSLSupport.loadResources(resource.toURI())
            cfn.Resources.addAll(resources)
        } else {
            Path path = Paths.get(resourcePath)
            if (!Files.exists(path)) throw new InvalidResourceException("not found: ${resourcePath}")
            List resources = DSLSupport.loadResources(path)
            cfn.Resources.addAll(resources)
        }
    }

    void outputs(Closure cl) {
        cl.delegate = new OutputsDelegate(cfn.Outputs)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }

}
