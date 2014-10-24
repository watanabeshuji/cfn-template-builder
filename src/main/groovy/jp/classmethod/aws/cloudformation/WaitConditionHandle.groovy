package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/10/24.
 */
@Canonical
class WaitConditionHandle {

    def id
    def Name

    def WaitConditionHandle() {}

    def WaitConditionHandle(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::CloudFormation::WaitConditionHandle'
            ]
        ]
    }

    static def load(File file) {
        Util.load(file, {new WaitConditionHandle(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, WaitConditionHandle r -> o << r.toResourceMap() })
    }

}
