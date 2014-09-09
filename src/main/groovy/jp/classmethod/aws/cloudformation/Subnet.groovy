package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class Subnet {
    def id
    def name
    def vpcId
    def cidrBlock
    def az

    def Subnet() {
    }

    def Subnet(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.vpcId = source.camelCase('Vpc')
        this.cidrBlock = source.value('CidrBlock')
        this.az = source.value('AvailabilityZone')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::Subnet',
                'Properties': [
                    'VpcId': ["Ref": vpcId],
                    'CidrBlock': cidrBlock,
                    'AvailabilityZone': az,
                    'Tags': [
                        ['Key': 'Name', 'Value': name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
    }

    static def load(file) {
        if (!file.exists()) return []
        def result = []
        def meta
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                result << new Subnet(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Subnet r -> o << r.toResourceMap() })
    }
}
