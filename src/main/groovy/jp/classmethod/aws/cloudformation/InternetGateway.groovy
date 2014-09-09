package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class InternetGateway {
    def id
    def name
    def vpcId

    def InternetGateway() {
    }

    def InternetGateway(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.vpcId = source.camelCase('Vpc')
    }

    def toResourceMap() {
        [
            (id): [
                'Type': 'AWS::EC2::InternetGateway',
                'Properties': [
                    'Tags': [
                        ['Key': 'Name', 'Value': name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ],
            ("Attach$id" as String): [
                'Type': 'AWS::EC2::VPCGatewayAttachment',
                'Properties': [
                    'VpcId': ["Ref": vpcId],
                    'InternetGatewayId': ["Ref": id]
                ]
            ]
        ]
    }

    static def load(File file) {
        if (!file.exists()) return []
        def result = []
        def meta
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                result << new InternetGateway(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, InternetGateway r -> o << r.toResourceMap() })
    }
}
