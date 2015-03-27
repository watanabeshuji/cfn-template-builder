package jp.classmethod.aws.cloudformation.dsl

import jp.classmethod.aws.cloudformation.CloudFormation
import jp.classmethod.aws.cloudformation.delegate.CloudFormationDelegate

import java.nio.file.Path

/**
 * Created by watanabeshuji on 2015/03/26.
 */
class CloudFormationDSL {

    static CloudFormation load(Path dsl) {
        CloudFormation cfn = new CloudFormation()
        Script dslScript = new GroovyShell().parse(dsl.toFile())
        dslScript.metaClass = createEMC(dslScript.class, { ExpandoMetaClass emc ->
            emc.cloudformation = { Closure cl ->
                cl.delegate = new CloudFormationDelegate(cfn)
                cl.resolveStrategy = Closure.DELEGATE_FIRST
                cl()
            }
        })
        dslScript.run()
        return cfn
    }

    static ExpandoMetaClass createEMC(Class clazz, Closure cls) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cls(emc)
        emc.initialize()
        return emc
    }

}
