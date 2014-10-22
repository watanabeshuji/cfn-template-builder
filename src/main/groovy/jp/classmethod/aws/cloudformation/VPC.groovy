package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class VPC {
    def id
    def Name
    def CidrBlock
    def EnableDnsSupport
    def EnableDnsHostnames

    def VPC() {
    }

    def VPC(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.CidrBlock = source.value('CidrBlock')
        this.EnableDnsSupport = source.bool('EnableDnsSupport')
        this.EnableDnsHostnames = source.bool('EnableDnsHostnames')
    }

    def toResourceMap() {
        def map = [
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
        if (EnableDnsSupport != null) map[id]['Properties']['EnableDnsSupport'] = EnableDnsSupport
        if (EnableDnsHostnames != null) map[id]['Properties']['EnableDnsHostnames'] = EnableDnsHostnames
        map
    }

    static def load(file) {
        Util.load(file, { new VPC(it)} )
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, VPC r -> o << r.toResourceMap() })
    }
}
