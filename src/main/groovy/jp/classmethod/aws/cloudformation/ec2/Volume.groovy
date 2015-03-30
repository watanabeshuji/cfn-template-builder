package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::EC2::Volume
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-ebs-volume.html
 *
 */
@Canonical
class Volume extends Resource {

    final def Type = 'AWS::EC2::Volume'
    def id
    def Size
    def VolumeType
    def AvailabilityZone

    def toResourceMap() {
        [
            'Type'      : Type,
            'Properties': [
                'AvailabilityZone': AvailabilityZone,
                'Size'            : Size,
                'VolumeType'      : VolumeType,
                'Tags'            : [
                    ['Key': 'Name', 'Value': id],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
    }
}
