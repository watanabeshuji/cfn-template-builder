package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

import java.nio.file.Path

/**
 * Created by watanabeshuji on 2015/03/28.
 */
@Canonical
class Resource {

    static List load(Path dsl) {
        DSLSupport.loadResources(dsl)
    }
}
