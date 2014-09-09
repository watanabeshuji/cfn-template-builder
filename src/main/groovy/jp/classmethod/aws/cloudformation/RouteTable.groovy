package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class RouteTable {
    def id
    def name
    def vpcId

    def RouteTable() {
    }

    def RouteTable(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.vpcId = source.camelCase('Vpc')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::RouteTable',
                'Properties': [
                    'VpcId': ['Ref': vpcId],
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
                result << new RouteTable(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, RouteTable r -> o << r.toResourceMap() })
    }

}