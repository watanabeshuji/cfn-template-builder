package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::EC2::Instance
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-instance.html
 *
 * Created by watanabeshuji on 2014/08/13.
 */
@Canonical
class Instance extends Resource {
    def id
    def InstanceType
    def KeyName
    def SubnetId
    def ImageId
    def IamInstanceProfile
    def SourceDestCheck
    def PrivateIpAddress
    def SecurityGroupIds
    List Volumes = []
    def BlockDeviceMappings = []
    def UserData

    def Instance() {
    }

    def toResourceMap() {
        def result = [
            'Type'      : 'AWS::EC2::Instance',
            'Properties': [:]
        ]
        if (InstanceType) result['Properties']['InstanceType'] = InstanceType
        if (KeyName) result['Properties']['KeyName'] = KeyName
        result['Properties']['SubnetId'] = SubnetId
        result['Properties']['ImageId'] = ImageId
        if (IamInstanceProfile) result['Properties']['IamInstanceProfile'] = IamInstanceProfile
        if (SourceDestCheck != null) result['Properties']['SourceDestCheck'] = SourceDestCheck
        result['Properties']['SecurityGroupIds'] = SecurityGroupIds
        if (PrivateIpAddress) result['Properties']['PrivateIpAddress'] = PrivateIpAddress
        result['Properties']['Tags'] = [
            ['Key': 'Name', 'Value': id],
            ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
        ]
        if (UserData) {
            result['Properties']['UserData'] = [
                'Fn::Base64': [
                    'Fn::Join': ['', UserData.split("\n").collect { "${it}\\n" }]
                ]
            ]
        }
        if (!BlockDeviceMappings.isEmpty()) {
            result['Properties']['BlockDeviceMappings'] = BlockDeviceMappings.collect { it.toResourceMap() }
        }
        if (!Volumes.isEmpty()) {
            result['Properties']['Volumes'] = Volumes.collect { it.toResourceMap() }
        }
        result
    }

    @Canonical
    static class Volume {

        def VolumeId
        def Device

        def toResourceMap() {
            [
                'VolumeId': VolumeId,
                'Device'  : Device
            ]
        }
    }


    @Canonical
    static class BlockDeviceMapping {

        def DeviceName
        def Ebs

        def toResourceMap() {
            def result = [
                'DeviceName': DeviceName,
            ]
            if (Ebs) result['Ebs'] = Ebs.toResourceMap()
            result
        }
    }

    @Canonical
    static class Ebs {
        def VolumeType
        def VolumeSize
        def DeleteOnTermination

        def toResourceMap() {
            def result = [
                'VolumeType': VolumeType,
                'VolumeSize': VolumeSize
            ]
            if (DeleteOnTermination) result['DeleteOnTermination'] = DeleteOnTermination
            result
        }

    }

}
