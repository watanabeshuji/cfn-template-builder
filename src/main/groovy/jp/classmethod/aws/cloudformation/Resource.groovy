package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

import java.nio.file.Path

/**
 * Created by watanabeshuji on 2015/03/28.
 */
@Canonical
class Resource {

    def refIds = []

    def withRefIds(Map params) {
        params.values().flatten().findAll {
            (it instanceof Map) && it.containsKey('Ref')
        }.forEach() {
            refIds << it['Ref']
        }
        this
    }

    static List load(Path dsl) {
        load(dsl, [:])
    }

    static List load(Path dsl, Map binding) {
        DSLSupport.loadResources(dsl, binding)
    }
}
