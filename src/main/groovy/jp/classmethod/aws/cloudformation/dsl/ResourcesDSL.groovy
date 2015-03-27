package jp.classmethod.aws.cloudformation.dsl

import jp.classmethod.aws.cloudformation.delegate.ResourcesDelegate

import java.nio.file.Path

/**
 * Created by watanabeshuji on 2015/03/26.
 */
class ResourcesDSL {
    static List load(Path dsl) {
        List resources = []
        Script dslScript = new GroovyShell().parse(dsl.toFile())
        dslScript.metaClass = createEMC(dslScript.class, { ExpandoMetaClass emc ->
            emc.resources = { Closure cl ->
                cl.delegate = new ResourcesDelegate(resources)
                cl.resolveStrategy = Closure.DELEGATE_FIRST
                cl()
            }
        })
        dslScript.run()
        return resources
    }

    static ExpandoMetaClass createEMC(Class clazz, Closure cls) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cls(emc)
        emc.initialize()
        return emc
    }
}
