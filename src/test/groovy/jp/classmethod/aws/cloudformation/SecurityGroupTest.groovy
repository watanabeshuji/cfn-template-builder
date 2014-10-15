package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/20.
 */
class SecurityGroupTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("SecurityGroupTest_default.csv").getFile())
        def actual = SecurityGroup.load(input)
        assert (actual as List) == [
            new SecurityGroup(id: 'Internal', Name: 'internal', Vpc: 'Vpc', Description: 'Allow all communications in VPC', ingress: [
                new SecurityGroup.Ingress('tcp',   '0', '65535', '10.0.0.0/16'),
                new SecurityGroup.Ingress('udp',   '0', '65535', '10.0.0.0/16'),
                new SecurityGroup.Ingress('icmp', '-1',    '-1', '10.0.0.0/16')
            ]),
            new SecurityGroup(id: 'CmMainte', Name: 'cm-mainte', Vpc: 'Vpc', Description: 'Allow ssh from Classmethod', ingress: [
                new SecurityGroup.Ingress('tcp',   '22', '22', '111.222.333.444/32'),
                new SecurityGroup.Ingress('tcp',   '22', '22', '222.333.444.555/32')
            ])
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new SecurityGroup(id: 'Internal', Name: 'internal', Vpc: 'Vpc', Description: 'Allow all communications in VPC', ingress: [
            new SecurityGroup.Ingress('tcp',   '0', '65535', '10.0.0.0/16'),
            new SecurityGroup.Ingress('udp',   '0', '65535', '10.0.0.0/16'),
            new SecurityGroup.Ingress('icmp', '-1',    '-1', '10.0.0.0/16')
        ])
        def expected = [
            "Internal": [
                'Type': 'AWS::EC2::SecurityGroup',
                'Properties': [
                    'VpcId': ['Ref': 'Vpc'],
                    'GroupDescription': 'Allow all communications in VPC',
                    'SecurityGroupIngress': [
                        ['IpProtocol': 'tcp', 'FromPort': '0', 'ToPort': '65535', 'CidrIp': '10.0.0.0/16'],
                        ['IpProtocol': 'udp', 'FromPort': '0', 'ToPort': '65535', 'CidrIp': '10.0.0.0/16'],
                        ['IpProtocol': 'icmp', 'FromPort': '-1', 'ToPort': '-1', 'CidrIp': '10.0.0.0/16']
                    ],
                    'Tags': [
                            ['Key': 'Name', 'Value': 'internal'],
                            ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


}
