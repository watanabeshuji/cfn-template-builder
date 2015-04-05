package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * AWS::EC2::SecurityGroup
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-security-group.html
 *
 * Created by watanabeshuji on 2014/08/13.
 */
@Canonical
class SecurityGroup extends Resource {

    static final def TYPE = 'AWS::EC2::SecurityGroup'
    static final def DESC = '''\
AWS::EC2::SecurityGroup
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-security-group.html

[Required Params]
- id
- VpcId
- GroupDescription

[Optional Params]
- AvailabilityZone

[Nested Resources]
- securityGroupIngress

[Sample]
securityGroup id: "PublicWeb", VpcId: [Ref: "VPC"], GroupDescription: "Allow web access from internet.", {
    securityGroupIngress IpProtocol: "tcp", FromPort: 80,  ToPort:  80, CidrIp: "0.0.0.0/0"
    securityGroupIngress IpProtocol: "tcp", FromPort: 443, ToPort: 443, CidrIp: "0.0.0.0/0"
}
'''
    def id
    def VpcId
    def GroupDescription
    List<SecurityGroupIngress> SecurityGroupIngress = []
    def Tags = [:]

    static SecurityGroup newInstance(Map params) {
        convert(params)
        checkKeys(SecurityGroup, params, ['id', 'VpcId', 'GroupDescription', 'SecurityGroupIngress', 'Tags'])
        logicalId(SecurityGroup, params)
        require(SecurityGroup, ['VpcId', 'GroupDescription'], params)
        new SecurityGroup(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'VpcId'               : VpcId,
                'GroupDescription'    : GroupDescription,
                'SecurityGroupIngress': SecurityGroupIngress.collect { it.toInlineMap() },
                'Tags'                : []
            ]
        ]
        Tags.each { key, value ->
            map['Properties']['Tags'] << ['Key': key, 'Value': value]
        }
        if (Tags['Name'] == null) map['Properties']['Tags'] << ['Key': 'Name', 'Value': id]
        if (Tags['Application'] == null) map['Properties']['Tags'] << ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
        map
    }

}

