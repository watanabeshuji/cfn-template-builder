package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class Subnet {
    def id
    def Name
    def Vpc
    def CidrBlock
    def AvailabilityZone

    def Subnet() {
    }

    def Subnet(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.Vpc = source.camelCase('Vpc')
        this.CidrBlock = source.value('CidrBlock')
        this.AvailabilityZone = source.value('AvailabilityZone')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::Subnet',
                'Properties': [
                    'VpcId': Util.ref(this.Vpc),
                    'CidrBlock': CidrBlock,
                    'AvailabilityZone': AvailabilityZone,
                    'Tags': [
                        ['Key': 'Name', 'Value': Name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
    }

    static def load(file) {
        Util.load(file, {new Subnet(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Subnet r -> o << r.toResourceMap() })
    }
}
