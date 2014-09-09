package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/08/13.
 */
@Canonical
class SecurityGroup {

    def id
    def name
    def vpcId
    def desc
    def ingress = []

    def SecurityGroup() {
    }

    def SecurityGroup(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.vpcId = source.camelCase('Vpc')
        this.desc = source.value('Description')
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
                    'VpcId': ['Ref': vpcId],
                    'GroupDescription': desc,
                    'SecurityGroupIngress': ingress.collect {
                        [
                            'IpProtocol': it.protocol,
                            'FromPort': it.fromPort,
                            'ToPort': it.toPort,
                            'CidrIp': it.cidrIp
                        ]
                    },
                    'Tags': [
                        ['Key': 'Name', 'Value': name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
    }

    @Canonical
    static class Ingress {

        def protocol
        def fromPort
        def toPort
        def cidrIp

        Ingress(protocol, fromPort, toPort, cidrIp) {
            this.protocol = protocol
            this.fromPort = fromPort
            this.toPort = toPort
            this.cidrIp = cidrIp
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

