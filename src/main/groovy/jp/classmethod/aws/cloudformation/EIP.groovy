package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class EIP {
    def id
    def Name
    def InstanceId

    def EIP() {
    }

    def EIP(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.InstanceId = source.value('InstanceId')
    }

    def toResourceMap() {
        def result = [
            (id): [
                'Type': 'AWS::EC2::EIP',
                'Properties': [
                    'Domain': 'vpc'
                ]
            ]
        ]
        if (InstanceId) result[id]['Properties']['InstanceId'] = InstanceId
        result
    }

    static def load(File file) {
        Util.load(file, {new EIP(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, EIP r -> o << r.toResourceMap() } )
    }

}
