package jp.classmethod.aws.cloudformation

import groovy.json.JsonSlurper
import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class InstanceProfile {
    def id
    def Name
    def Path
    def Roles

    def InstanceProfile() {
    }

    def InstanceProfile(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.Path = source.value('Path')
        this.Roles = source.refList('Roles')
    }

    def toResourceMap() {
        def result = [
            (id): [
                'Type': 'AWS::IAM::InstanceProfile',
                'Properties': [
                    'Path': Path,
                    'Roles': Roles
                ]
            ]
        ]
        result
    }

    static def load(File file) {
        Util.load(file, {new InstanceProfile(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, InstanceProfile r -> o << r.toResourceMap() } )
    }

}
