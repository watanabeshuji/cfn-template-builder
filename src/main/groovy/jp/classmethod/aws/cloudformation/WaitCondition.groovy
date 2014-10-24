package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/10/24.
 */
@Canonical
class WaitCondition {


    def id
    def Name
    def Handle
    def Timeout
    def DependsOn

    def WaitCondition() {}

    def WaitCondition(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.Handle = source.value('Handle')
        this.Timeout = source.value('Timeout')
        this.DependsOn = source.list('DependsOn')
    }

    def toResourceMap() {
        def map =[
            (this.id): [
                'Type': 'AWS::CloudFormation::WaitCondition',
                'Properties': [
                    'Handle': Util.ref(this.Handle),
                    'Timeout': this.Timeout
                ]
            ]
        ]
        if (!DependsOn.isEmpty()) map[this.id]['DependsOn'] = this.DependsOn
        map
    }

    static def load(File file) {
        Util.load(file, {new WaitCondition(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, WaitCondition r -> o << r.toResourceMap() })
    }
}
