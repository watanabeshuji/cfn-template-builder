package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource
import jp.classmethod.aws.cloudformation.util.Params

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.checkKeys
import static jp.classmethod.aws.cloudformation.util.Valid.logicalId
import static jp.classmethod.aws.cloudformation.util.Valid.require

/**
 * AWS::EC2::Instance
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-instance.html
 *
 * Created by watanabeshuji on 2014/08/13.
 */
@Canonical
class Instance extends Resource {

    static final def TYPE = 'AWS::EC2::Instance'
    static def DESC = """\
AWS::EC2::EIP
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-instance.html

[Required Params]
- id
- ImageId

[Optional Params]
- InstanceType
- KeyName
- SubnetId
- IamInstanceProfile
- SourceDestCheck
- PrivateIpAddress
- SecurityGroupIds
- Volumes
- BlockDeviceMappings
- UserData
- Tags

[Sample]
def userData = '''\
#!/bin/sh

yum -y update
'''

    resources {
        instance id: "Web", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PublicSubnet"],
            ImageId: "FindInMap:AMI:AmazonLinux:201503", IamInstanceProfile: [Ref: "WebInstanceProfile"],
            SourceDestCheck: true, SecurityGroupIds: [[Ref: "Internal"], [Ref: "PublicWeb"]], UserData: userData
        instance id: "Batch", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PrivateSubnet"],
            ImageId: "FindInMap:AMI:AmazonLinux:201503", IamInstanceProfile: [Ref: "WebInstanceProfile"],
            SourceDestCheck: true, SecurityGroupIds: [[Ref: "Internal"]], {
            volume VolumeId: [Ref: "BatchVolume"], Device: "/dev/sdk"
        }
        instance id: "Server", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PrivateSubnet"], ImageId: "FindInMap:AMI:AmazonLinux:201503", {
            blockDeviceMapping DeviceName: "/dev/xvda", {
                ebs VolumeType: "gp2", VolumeSize: "200", DeleteOnTermination: true
            }
            blockDeviceMapping DeviceName: "/dev/xvdb", {
                ebs VolumeType: "gp2", VolumeSize: "100"
            }
        }
    }

"""

    def id
    def ImageId
    def InstanceType
    def KeyName
    def SubnetId
    def IamInstanceProfile
    def SourceDestCheck
    def PrivateIpAddress
    def SecurityGroupIds
    List Volumes = []
    def BlockDeviceMappings = []
    def UserData
    def Tags = [:]

    static Instance newInstance(Map params) {
        convert(params)
        checkKeys(Instance, params, [
            'id', 'ImageId', 'InstanceType', 'KeyName', 'SubnetId', 'IamInstanceProfile', 'SourceDestCheck',
            'PrivateIpAddress', 'SecurityGroupIds', 'Volumes', 'BlockDeviceMappings', 'UserData', 'Tags'
            ])
        logicalId(Instance, params)
        require(RouteTable, 'ImageId', params)
        if (params['UserData']) params['UserData'] = Params.convertUserData(params['UserData'])
        new Instance(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'ImageId': ImageId,
                'Tags': []
            ]
        ]
        if (InstanceType) map['Properties']['InstanceType'] = InstanceType
        if (KeyName) map['Properties']['KeyName'] = KeyName
        if (SubnetId) map['Properties']['SubnetId'] = SubnetId
        if (IamInstanceProfile) map['Properties']['IamInstanceProfile'] = IamInstanceProfile
        if (SourceDestCheck != null) map['Properties']['SourceDestCheck'] = SourceDestCheck
        if (SecurityGroupIds) map['Properties']['SecurityGroupIds'] = SecurityGroupIds
        if (PrivateIpAddress) map['Properties']['PrivateIpAddress'] = PrivateIpAddress
        if (UserData) {
            map['Properties']['UserData'] = [
                'Fn::Base64': [
                    'Fn::Join': ['', UserData]
                ]
            ]
        }
        if (!BlockDeviceMappings.isEmpty()) {
            map['Properties']['BlockDeviceMappings'] = BlockDeviceMappings.collect { it.toResourceMap() }
        }
        if (!Volumes.isEmpty()) {
            map['Properties']['Volumes'] = Volumes.collect { it.toResourceMap() }
        }
        Tags.each { key, value ->
            map['Properties']['Tags'] << ['Key': key, 'Value': value]
        }
        if (Tags['Name'] == null) map['Properties']['Tags'] << ['Key': 'Name', 'Value': id]
        if (Tags['Application'] == null) map['Properties']['Tags'] << ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
        map
    }

    @Canonical
    static class Volume {

        static final def TYPE = 'AWS::EC2::Instance'
        static def DESC = '''\
AWS::EC2::EIP
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-instance.html

[Required Params]
- id
- ImageId

[Optional Params]
- InstanceType
- KeyName
- SubnetId
- IamInstanceProfile
- SourceDestCheck
- PrivateIpAddress
- SecurityGroupIds
- Volumes
- BlockDeviceMappings
- UserData
- Tags

[Sample]
'''

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
