package jp.classmethod.aws.cloudformation.ec2

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
        def userData = '''/
#!/bin/sh
yum -y update
'''
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
        assert actual == excepted
    }

    @Test
    void "toResourceMap"() {
        def sut = new Instance(
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
                'InstanceType'      : 't1.micro',
                'KeyName'           : ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                'SubnetId'          : ['Ref': 'PublicSubnet'],
                'ImageId'           : ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
                'IamInstanceProfile': ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
                'SourceDestCheck'   : false,
                'SecurityGroupIds'  : [['Ref': 'Internal'], ['Ref': 'CmMainte']],
                'Tags'              : [
                    ['Key': 'Name', 'Value': 'Bastion'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ],
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
        def sut = new Instance(
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
        def sut = new Instance(
            id: 'WebServer',
            InstanceType: 'm3.medium',
            KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
            SubnetId: [Ref: 'PublicSubnet'],
            ImageId: ['Fn::FindInMap': ['AMI', 'AmazonLinux', '20140901']],
            SecurityGroupIds: [],
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
                'InstanceType'       : 'm3.medium',
                'KeyName'            : ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                'SubnetId'           : ['Ref': 'PublicSubnet'],
                'ImageId'            : ['Fn::FindInMap': ['AMI', 'AmazonLinux', '20140901']],
                'SecurityGroupIds'   : [],
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
                'Tags'               : [
                    ['Key': 'Name', 'Value': 'WebServer'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ],
            ]
        ]
        assert sut.toResourceMap() == expected
    }


}

