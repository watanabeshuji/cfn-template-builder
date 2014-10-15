package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/08/13.
 */
@Canonical
class SecurityGroup {

    def id
    def Name
    def Vpc
    def Description
    def ingress = []

    def SecurityGroup() {
    }

    def SecurityGroup(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.Vpc = source.camelCase('Vpc')
        this.Description = source.value('Description')
        this.addIngress(source)
    }

    def addIngress(source) {
        this.ingress << new Ingress(
                source.value('Protocol'),
                source.value('FromPort'),
                source.value('ToPort'),
                source.value('CidrIp'))
    }
    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::SecurityGroup',
                'Properties': [
                    'VpcId': Util.ref(this.Vpc),
                    'GroupDescription': Description,
                    'SecurityGroupIngress': ingress.collect {
                        [
                            'IpProtocol': it.Protocol,
                            'FromPort': it.FromPort,
                            'ToPort': it.ToPort,
                            'CidrIp': it.CidrIp
                        ]
                    },
                    'Tags': [
                        ['Key': 'Name', 'Value': Name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
    }

    @Canonical
    static class Ingress {

        def Protocol
        def FromPort
        def ToPort
        def CidrIp

        Ingress(protocol, fromPort, toPort, cidrIp) {
            this.Protocol = protocol
            this.FromPort = fromPort
            this.ToPort = toPort
            this.CidrIp = cidrIp
        }
    }

    static def load(file) {
        if (!file.exists()) return []
        def groups = [:]
        def meta
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                def source = meta.newSource(line.split(','))
                def name = source.value('Name')
                if (groups[name] == null) {
                    groups[name] = new SecurityGroup(source)
                } else {
                    groups[name].addIngress(source)
                }
            }
        }
        groups.values()
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, SecurityGroup r -> o << r.toResourceMap() } )
    }

}

