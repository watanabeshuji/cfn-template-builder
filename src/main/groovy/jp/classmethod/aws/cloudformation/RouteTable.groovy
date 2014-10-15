package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class RouteTable {
    def id
    def Name
    def Vpc

    def RouteTable() {
    }

    def RouteTable(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.Vpc = source.camelCase('Vpc')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::RouteTable',
                'Properties': [
                    'VpcId': Util.ref(Vpc),
                    'Tags': [
                        ['Key': 'Name', 'Value': Name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
    }

    static def load(File file) {
        Util.load(file, {new RouteTable(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, RouteTable r -> o << r.toResourceMap() })
    }

}