package jp.classmethod.aws.cloudformation

import jp.classmethod.aws.cloudformation.delegate.CloudFormationDelegate
import jp.classmethod.aws.cloudformation.delegate.ResourcesDelegate

import java.nio.file.Path

/**
 * Created by watanabeshuji on 2015/03/29.
 */
class DSLSupport {


    static CloudFormation load(Path dsl, Map binding) {
        CloudFormation cfn = new CloudFormation()
        cfn.binding = binding
        _load(dsl.toFile(), binding, { ExpandoMetaClass emc ->
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

    static List loadResources(URI uri, Map binding) {
        List resources = []
        _load(uri, binding, { ExpandoMetaClass emc ->
            emc.resources = { Closure cl ->
                cl.delegate = new ResourcesDelegate(resources)
                cl.resolveStrategy = Closure.DELEGATE_FIRST
                cl()
            }
        })
        resources
    }

    static List loadResources(Path dsl, Map binding) {
        List resources = []
        _load(dsl.toFile(), binding, { ExpandoMetaClass emc ->
            emc.resources = { Closure cl ->
                cl.delegate = new ResourcesDelegate(resources)
                cl.resolveStrategy = Closure.DELEGATE_FIRST
                cl()
            }
        })
        resources
    }

    private static _load(URI uri, Map binding, Closure cls) {
        Script dslScript = new GroovyShell().parse(uri)
        dslScript.setBinding(new Binding(binding))
        dslScript.metaClass = _createEMC(dslScript.class, cls)
        dslScript.run()
    }

    private static _load(File file, Map binding, Closure cls) {
        Script dslScript = new GroovyShell().parse(file)
        dslScript.setBinding(new Binding(binding))
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
