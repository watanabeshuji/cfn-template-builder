package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical

/**
 * AWS::EC2::Subnet
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-subnet.html
 */
@Canonical
class Subnet {

    String id
    def VpcId
    def CidrBlock
    def AvailabilityZone

    def Subnet() {
    }

    def toResourceMap() {
        [
            'Type': 'AWS::EC2::Subnet',
            'Properties': [
                'VpcId': VpcId,
                'CidrBlock': CidrBlock,
                'AvailabilityZone': AvailabilityZone,
                'Tags': [
                    ['Key': 'Name', 'Value': id],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                ]
            ]
        ]
    }


}
