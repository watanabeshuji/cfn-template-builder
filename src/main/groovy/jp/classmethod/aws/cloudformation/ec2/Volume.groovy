package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical

/**
 * AWS::EC2::Volume
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-ebs-volume.html
 *
 */
@Canonical
class Volume {
    def id
    def Size
    def VolumeType
    def AvailabilityZone

    def Volume() {
    }

    def toResourceMap() {
        [
            'Type': 'AWS::EC2::Volume',
            'Properties': [
                'AvailabilityZone': AvailabilityZone,
                'Size': Size,
                'VolumeType': VolumeType,
                'Tags': [
                    ['Key': 'Name', 'Value': Name],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                ]
            ]
        ]
    }
}
