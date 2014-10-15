package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class InternetGateway {
    def id
    def Name
    def Vpc

    def InternetGateway() {
    }

    def InternetGateway(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.Vpc = source.camelCase('Vpc')
    }

    def toResourceMap() {
        [
            (id): [
                'Type': 'AWS::EC2::InternetGateway',
                'Properties': [
                    'Tags': [
                        ['Key': 'Name', 'Value': Name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ],
            ("Attach$id" as String): [
                'Type': 'AWS::EC2::VPCGatewayAttachment',
                'Properties': [
                    'VpcId': Util.ref(this.Vpc),
                    'InternetGatewayId': Util.ref(this.id)
                ]
            ]
        ]
    }

    static def load(File file) {
        Util.load(file, {new InternetGateway(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, InternetGateway r -> o << r.toResourceMap() })
    }
}
