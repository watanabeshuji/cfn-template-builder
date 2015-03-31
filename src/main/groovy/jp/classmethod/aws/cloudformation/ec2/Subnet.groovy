package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::EC2::Subnet
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-subnet.html
 */
@Canonical
class Subnet extends Resource {

    final def Type = 'AWS::EC2::Subnet'
    String id
    def VpcId
    def CidrBlock
    def AvailabilityZone
    def Tags = [:]

    def Subnet() {
    }

    def toResourceMap() {
        def map = [
            'Type'      : Type,
            'Properties': [
                'VpcId'           : VpcId,
                'CidrBlock'       : CidrBlock,
                'Tags'            : []
            ]
        ]
        if (AvailabilityZone) map['Properties']['AvailabilityZone'] = AvailabilityZone
        Tags.each {key, value ->
            map['Properties']['Tags'] << ['Key': key, 'Value': value]
        }
        if (Tags['Name'] == null) map['Properties']['Tags'] << ['Key': 'Name', 'Value': id]
        if (Tags['Application'] == null) map['Properties']['Tags'] << ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
        map
    }


}
