package jp.classmethod.aws.cloudformation

import jp.classmethod.aws.cloudformation.delegate.CloudFormationDelegate
import jp.classmethod.aws.cloudformation.delegate.ResourcesDelegate

import java.nio.file.Path

/**
 * Created by watanabeshuji on 2015/03/29.
 */
class DSLSupport {


    static CloudFormation load(Path dsl) {
        CloudFormation cfn = new CloudFormation()
        _load(dsl, { ExpandoMetaClass emc ->
            emc.cloudformation = { Closure cl ->
                cl.delegate = new CloudFormationDelegate(cfn)
                cl.resolveStrategy = Closure.DELEGATE_FIRST
                cl()
            }
        })
        cfn
    }

    static List loadResources(Path dsl) {
        List resources = []
        _load(dsl, { ExpandoMetaClass emc ->
            emc.resources = { Closure cl ->
                cl.delegate = new ResourcesDelegate(resources)
                cl.resolveStrategy = Closure.DELEGATE_FIRST
                cl()
            }
        })
        resources
    }

    private static _load(Path dsl, Closure cls) {
        Script dslScript = new GroovyShell().parse(dsl.toFile())
        dslScript.metaClass = _createEMC(dslScript.class, cls)
        dslScript.run()
    }

    private static ExpandoMetaClass _createEMC(Class clazz, Closure cls) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cls(emc)
        emc.initialize()
        return emc
    }
}
