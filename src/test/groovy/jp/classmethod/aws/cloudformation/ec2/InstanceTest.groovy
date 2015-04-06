package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.util.ValidErrorException
import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/20.
 */
class InstanceTest {


    @Test
    void "load instance.groovy"() {
        Path input = getPath("/templates/resources/instance.groovy")
        def actual = Instance.load(input)
        def userData = [
            "#!/bin/sh\\n",
            "\\n",
            "yum -y update\\n"
        ]
        def excepted = [
            new Instance(id: "Web", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PublicSubnet"],
                ImageId: ["Fn::FindInMap": ["AMI", "AmazonLinux", "201503"]], IamInstanceProfile: [Ref: "WebInstanceProfile"],
                SourceDestCheck: true, SecurityGroupIds: [[Ref: "Internal"], [Ref: "PublicWeb"]],
                UserData: userData),
            new Instance(id: "Batch", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PrivateSubnet"],
                ImageId: ["Fn::FindInMap": ["AMI", "AmazonLinux", "201503"]], IamInstanceProfile: [Ref: "WebInstanceProfile"],
                SourceDestCheck: true, SecurityGroupIds: [[Ref: "Internal"]],
                Volumes: [
                    new Instance.Volume(VolumeId: [Ref: "BatchVolume"], Device: "/dev/sdk")
                ]
            ),
            new Instance(id: "Server", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PrivateSubnet"],
                ImageId: ["Fn::FindInMap": ["AMI", "AmazonLinux", "201503"]],
                BlockDeviceMappings: [
                    new Instance.BlockDeviceMapping(DeviceName: "/dev/xvda", Ebs: new Instance.Ebs(VolumeType: "gp2", VolumeSize: "200", DeleteOnTermination: true)),
                    new Instance.BlockDeviceMapping(DeviceName: "/dev/xvdb", Ebs: new Instance.Ebs(VolumeType: "gp2", VolumeSize: "100"))
                ]),
        ]
        assert actual[0].UserData == excepted[0].UserData
        assert actual[1].UserData == excepted[1].UserData
        assert actual[2].UserData == excepted[2].UserData
        assert actual == excepted
    }

    @Test
    void "toResourceMap"() {
        def sut = Instance.newInstance(
            id: 'Bastion',
            InstanceType: 't1.micro',
            KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
            SubnetId: [Ref: 'PublicSubnet'],
            ImageId: ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
            IamInstanceProfile: ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
            SourceDestCheck: false,
            SecurityGroupIds: [[Ref: 'Internal'], [Ref: 'CmMainte']],
            UserData: '''\
#! /bin/bash -v
yum update -y
'''
        )
        def expected = [
            'Type'      : 'AWS::EC2::Instance',
            'Properties': [
                'ImageId'           : ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
                'Tags'              : [
                    ['Key': 'Name', 'Value': 'Bastion'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ],
                'InstanceType'      : 't1.micro',
                'KeyName'           : ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                'SubnetId'          : ['Ref': 'PublicSubnet'],
                'IamInstanceProfile': ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
                'SourceDestCheck'   : false,
                'SecurityGroupIds'  : [['Ref': 'Internal'], ['Ref': 'CmMainte']],
                'UserData'          : [
                    'Fn::Base64': [
                        'Fn::Join': ['',
                                     ['#! /bin/bash -v\\n', 'yum update -y\\n']
                        ]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


    @Test
    void "toResourceMap_Volume有"() {
        def sut = Instance.newInstance(
            id: 'NAT',
            InstanceType: 't1.micro',
            KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
            SubnetId: [Ref: 'PublicSubnet'],
            ImageId: ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
            IamInstanceProfile: ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
            SecurityGroupIds: [[Ref: 'Internal'], [Ref: 'CmMainte']],
            Volumes: [
                new Instance.Volume(VolumeId: [Ref: 'ExtVolume'], Device: '/dev/sdh')
            ]
        )
        def expected = [
            'Type'      : 'AWS::EC2::Instance',
            'Properties': [
                'InstanceType'      : 't1.micro',
                'KeyName'           : ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                'SubnetId'          : ['Ref': 'PublicSubnet'],
                'ImageId'           : ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
                'IamInstanceProfile': ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
                'SecurityGroupIds'  : [['Ref': 'Internal'], ['Ref': 'CmMainte']],
                'Tags'              : [
                    ['Key': 'Name', 'Value': 'NAT'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ],
                'Volumes'           : [['VolumeId': ['Ref': 'ExtVolume'], 'Device': '/dev/sdh']]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


    @Test
    void "toResourceMap_BlockDeviceMappings"() {
        def sut = Instance.newInstance(
            id: 'WebServer',
            InstanceType: 'm3.medium',
            KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
            SubnetId: [Ref: 'PublicSubnet'],
            ImageId: ['Fn::FindInMap': ['AMI', 'AmazonLinux', '20140901']],
            BlockDeviceMappings: [
                new Instance.BlockDeviceMapping(
                    DeviceName: '/dev/xvda',
                    Ebs: new Instance.Ebs(VolumeSize: '200', VolumeType: 'gp2', DeleteOnTermination: 'true')
                ),
                new Instance.BlockDeviceMapping(
                    DeviceName: '/dev/xvdb',
                    Ebs: new Instance.Ebs(VolumeSize: '400', VolumeType: 'gp2')
                )
            ]
        )
        def expected = [
            'Type'      : 'AWS::EC2::Instance',
            'Properties': [
                'ImageId'            : ['Fn::FindInMap': ['AMI', 'AmazonLinux', '20140901']],
                'Tags'               : [
                    ['Key': 'Name', 'Value': 'WebServer'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ],
                'InstanceType'       : 'm3.medium',
                'KeyName'            : ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                'SubnetId'           : ['Ref': 'PublicSubnet'],
                'BlockDeviceMappings': [
                    [
                        'DeviceName': '/dev/xvda',
                        'Ebs'       : ['VolumeSize': '200', 'VolumeType': 'gp2', 'DeleteOnTermination': 'true']
                    ],
                    [
                        'DeviceName': '/dev/xvdb',
                        'Ebs'       : ['VolumeSize': '400', 'VolumeType': 'gp2']
                    ]
                ],
            ]
        ]
        assert sut.toResourceMap() == expected
    }



    @Test
    void "refIds"() {
        def sut = Instance.newInstance(
            id: 'Bastion',
            InstanceType: 't1.micro',
            KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
            SubnetId: [Ref: 'PublicSubnet'],
            ImageId: ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
            IamInstanceProfile: ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
            SourceDestCheck: false,
            SecurityGroupIds: [[Ref: 'Internal'], [Ref: 'CmMainte']],
            UserData: '''\
#! /bin/bash -v
yum update -y
'''
        )
        assert sut.refIds == ['PublicSubnet','Internal', 'CmMainte']
    }

    @Test(expected = ValidErrorException)
    void "id 必須"() {
        Instance.newInstance(ImageId: ['Fn::FindInMap': ['AMI', 'NAT', '201309']])
    }

    @Test(expected = ValidErrorException)
    void "ImageId 必須"() {
        Instance.newInstance(id: 'Web')
    }
}

