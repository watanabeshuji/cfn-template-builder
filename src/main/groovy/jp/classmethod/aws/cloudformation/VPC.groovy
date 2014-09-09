package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class VPC {
    def id
    def name
    def cidrBlock

    def VPC() {
    }

    def VPC(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.cidrBlock = source.value('CidrBlock')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::VPC',
                'Properties': [
                    'CidrBlock': cidrBlock,
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
                result << new VPC(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, VPC r -> o << r.toResourceMap() })
    }
}
