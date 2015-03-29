package jp.classmethod.aws.cloudformation.ec2

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/20.
 */
class SecurityGroupTest {

    @Test
    void "load securityGroup.groovy"() {
        Path input = getPath("/templates/resources/securityGroup.groovy")
        def actual = SecurityGroup.load(input)
        assert actual == [
            new SecurityGroup(id: 'PublicWeb', VpcId: [Ref: "VPC"], Description: "Allow web access from internet.",
                SecurityGroupIngress: [
                    new SecurityGroupIngress(IpProtocol: "tcp", FromPort: 80, ToPort: 80, CidrIp: "0.0.0.0/0"),
                    new SecurityGroupIngress(IpProtocol: "tcp", FromPort: 443, ToPort: 443, CidrIp: "0.0.0.0/0")
                ]),
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new SecurityGroup(id: 'Internal', VpcId: [Ref: 'VPC'], Description: 'Allow all communications in VPC',
            SecurityGroupIngress: [
                new SecurityGroupIngress(IpProtocol: 'tcp', FromPort: '0', ToPort: '65535', CidrIp: '10.0.0.0/16'),
                new SecurityGroupIngress(IpProtocol: 'udp', FromPort: '0', ToPort: '65535', CidrIp: '10.0.0.0/16'),
                new SecurityGroupIngress(IpProtocol: 'icmp', FromPort: '-1', ToPort: '-1', CidrIp: '10.0.0.0/16')
            ])
        def expected = [
            'Type'      : 'AWS::EC2::SecurityGroup',
            'Properties': [
                'VpcId'               : ['Ref': 'VPC'],
                'GroupDescription'    : 'Allow all communications in VPC',
                'SecurityGroupIngress': [
                    ['IpProtocol': 'tcp', 'FromPort': '0', 'ToPort': '65535', 'CidrIp': '10.0.0.0/16'],
                    ['IpProtocol': 'udp', 'FromPort': '0', 'ToPort': '65535', 'CidrIp': '10.0.0.0/16'],
                    ['IpProtocol': 'icmp', 'FromPort': '-1', 'ToPort': '-1', 'CidrIp': '10.0.0.0/16']
                ],
                'Tags'                : [
                    ['Key': 'Name', 'Value': 'Internal'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


}
