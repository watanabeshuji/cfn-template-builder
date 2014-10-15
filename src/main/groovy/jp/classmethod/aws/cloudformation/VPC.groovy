package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class VPC {
    def id
    def Name
    def CidrBlock

    def VPC() {
    }

    def VPC(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.CidrBlock = source.value('CidrBlock')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::VPC',
                'Properties': [
                    'CidrBlock': CidrBlock,
                    'Tags': [
                        ['Key': 'Name', 'Value': Name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
    }

    static def load(file) {
        Util.load(file, { new VPC(it)} )
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, VPC r -> o << r.toResourceMap() })
    }
}
