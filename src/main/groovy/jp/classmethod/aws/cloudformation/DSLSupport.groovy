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
        _load(dsl.toFile(), { ExpandoMetaClass emc ->
            emc.cloudformation = { Closure cl ->
                cl.delegate = new CloudFormationDelegate(cfn)
                cl.resolveStrategy = Closure.DELEGATE_FIRST
                cl()
            }
            emc.properties = { Map params ->
                params
            }
            emc.properties = { Map params1, Map params2 ->
                Map params = [:]
                params << params1
                params << params2
                params
            }
        })
        cfn
    }

    static List loadResources(URI uri) {
        List resources = []
        _load(uri, { ExpandoMetaClass emc ->
            emc.resources = { Closure cl ->
                cl.delegate = new ResourcesDelegate(resources)
                cl.resolveStrategy = Closure.DELEGATE_FIRST
                cl()
            }
        })
        resources
    }

    static List loadResources(Path dsl) {
        List resources = []
        _load(dsl.toFile(), { ExpandoMetaClass emc ->
            emc.resources = { Closure cl ->
                cl.delegate = new ResourcesDelegate(resources)
                cl.resolveStrategy = Closure.DELEGATE_FIRST
                cl()
            }
        })
        resources
    }

    private static _load(URI uri, Closure cls) {
        Script dslScript = new GroovyShell().parse(uri)
        dslScript.metaClass = _createEMC(dslScript.class, cls)
        dslScript.run()
    }

    private static _load(File file, Closure cls) {
        Script dslScript = new GroovyShell().parse(file)
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
