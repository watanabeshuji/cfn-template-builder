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

    final def Type = 'AWS::EC2::Instance'
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
    def Tags = [:]

    def Instance() {
    }

    def toResourceMap() {
        def map = [
            'Type'      : Type,
            'Properties': [
                'Tags': []
            ]
        ]
        if (InstanceType) map['Properties']['InstanceType'] = InstanceType
        if (KeyName) map['Properties']['KeyName'] = KeyName
        map['Properties']['SubnetId'] = SubnetId
        map['Properties']['ImageId'] = ImageId
        if (IamInstanceProfile) map['Properties']['IamInstanceProfile'] = IamInstanceProfile
        if (SourceDestCheck != null) map['Properties']['SourceDestCheck'] = SourceDestCheck
        map['Properties']['SecurityGroupIds'] = SecurityGroupIds
        if (PrivateIpAddress) map['Properties']['PrivateIpAddress'] = PrivateIpAddress
        if (UserData) {
            map['Properties']['UserData'] = [
                'Fn::Base64': [
                    'Fn::Join': ['', UserData.split("\n").collect { "${it}\\n" }]
                ]
            ]
        }
        if (!BlockDeviceMappings.isEmpty()) {
            map['Properties']['BlockDeviceMappings'] = BlockDeviceMappings.collect { it.toResourceMap() }
        }
        if (!Volumes.isEmpty()) {
            map['Properties']['Volumes'] = Volumes.collect { it.toResourceMap() }
        }
        Tags.each {key, value ->
            map['Properties']['Tags'] << ['Key': key, 'Value': value]
        }
        if (Tags['Name'] == null) map['Properties']['Tags'] << ['Key': 'Name', 'Value': id]
        if (Tags['Application'] == null) map['Properties']['Tags'] << ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
        map
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
