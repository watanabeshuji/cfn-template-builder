package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * AWS::EC2::Subnet
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-subnet.html
 */
@Canonical
class Subnet extends Resource {

    static final def TYPE = 'AWS::EC2::Subnet'
    static final def DESC = '''\
AWS::EC2::Subnet
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-subnet.html

[Required Params]
- id
- VpcId
- CidrBlock

[Optional Params]
- AvailabilityZone
- Tags

[Sample]
resources {
    subnet id: "SubnetA", VpcId: [Ref: 'VPC'], CidrBlock: "10.0.10.0/24", "AvailabilityZone": "ap-northeast-1a"
    subnet id: "SubnetC", VpcId: [Ref: 'VPC'], CidrBlock: "10.0.20.0/24", "AvailabilityZone": "ap-northeast-1c"
}
'''
    String id
    def VpcId
    def CidrBlock
    def AvailabilityZone
    def Tags = [:]

    static Subnet newInstance(Map params) {
        convert(params)
        checkKeys(Subnet, params, ['id', 'VpcId', 'CidrBlock', 'AvailabilityZone', 'Tags'])
        logicalId(Subnet, params)
        require(Subnet, ['VpcId', 'CidrBlock'], params)
        def instance = new Subnet(params)
        instance.addRefIds([params['VpcId']])
        instance
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'VpcId'    : VpcId,
                'CidrBlock': CidrBlock,
                'Tags'     : []
            ]
        ]
        if (AvailabilityZone) map['Properties']['AvailabilityZone'] = AvailabilityZone
        Tags.each { key, value ->
            map['Properties']['Tags'] << ['Key': key, 'Value': value]
        }
        if (Tags['Name'] == null) map['Properties']['Tags'] << ['Key': 'Name', 'Value': id]
        if (Tags['Application'] == null) map['Properties']['Tags'] << ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
        map
    }


}
