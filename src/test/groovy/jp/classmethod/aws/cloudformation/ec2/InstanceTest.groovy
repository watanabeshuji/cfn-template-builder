package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.ec2.Instance
import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/20.
 */
class InstanceTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("InstanceTest_default.csv").getFile())
        def actual = Instance.load(input)
        assert actual[0].userData == ['#! /bin/bash -v\n', 'yum update -y\n']
        assert actual == [
                new Instance(
                        id: 'Bastion',
                        Name: 'bastion',
                        InstanceType: 't1.micro',
                        KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                        SubnetId: 'PublicSubnet',
                        ImageId: ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
                        IamInstanceProfile: ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
                        SourceDestCheck: false,
                        EIP: true,
                        SecurityGroupIds: ['Internal', 'CmMainte'],
                        volumes: null,
                        userData: ['#! /bin/bash -v\n', 'yum update -y\n']
                )
        ]
    }

    @Test
    void "tomcat.csvのload"() {
        File input = new File(getClass().getResource("InstanceTest_tomcat.csv").getFile())
        def actual = Instance.load(input)
        assert actual == [
                new Instance(
                        id: 'Tomcat',
                        Name: 'tomcat',
                        InstanceType: 't1.small',
                        KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                        SubnetId: 'PublicSubnet',
                        ImageId: ['Fn::FindInMap': ['AMI', 'AmazonLinux', '20140901']],
                        IamInstanceProfile: ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
                        SourceDestCheck: true,
                        EIP: false,
                        SecurityGroupIds: ['Internal'],
                        volumes: null,
                        userData: [
                                '#! /bin/bash -v\n',
                                'yum -y update\n',
                                '\n',
                                '# Helper function\n',
                                'function error_exit\n',
                                '{\n',
                                '  /opt/aws/bin/cfn-signal -e 1 -r "$1" \'', ["Ref": "WebServerWaitHandle"], '\'\n',
                                '  exit 1\n',
                                '}\n',
                                'yum -y install java-1.8.0-openjdk-devel tomcat8\n',
                                'yum -y remove  java-1.7.0-openjdk\n',
                                'chkconfig tomcat8 on\n',
                                'service tomat8 start\n',
                                '\n',
                                '# All is well so signal success\n',
                                '/opt/aws/bin/cfn-signal -e $? -r "Server setup complete" \'', ["Ref" : "WebServerWaitHandle"], '\'\n',
                        ]
                )
        ]
    }

    @Test
    void "withVolume.csvのload"() {
        File input = new File(getClass().getResource("InstanceTest_withVolume.csv").getFile())
        def actual = Instance.load(input)
        assert actual[0].userData == ['#! /bin/bash -v\n', 'yum update -y\n']
        assert actual == [
                new Instance(
                        id: 'Bastion',
                        Name: 'bastion',
                        InstanceType: 't1.micro',
                        KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                        SubnetId: 'PublicSubnet',
                        ImageId: ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
                        IamInstanceProfile: ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
                        SourceDestCheck: false,
                        EIP: true,
                        SecurityGroupIds: ['Internal', 'CmMainte'],
                        volumes: [['VolumeId': 'ExtVolume', 'Device': '/dev/sdh']],
                        userData: ['#! /bin/bash -v\n', 'yum update -y\n']
                )
        ]
    }

    @Test
    void "BlockDeviceMappingのload"() {
        File input = new File(getClass().getResource("InstanceTest_BlockDeviceMapping.csv").getFile())
        def actual = Instance.load(input)
        assert actual[0].userData == []
        assert actual == [
                new Instance(
                        id: 'WebServer',
                        Name: 'WebServer',
                        InstanceType: 'm3.medium',
                        KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                        SubnetId: 'PublicSubnet',
                        ImageId: ['Fn::FindInMap': ['AMI', 'AmazonLinux', '20140901']],
                        SecurityGroupIds: [],
                        BlockDeviceMappings: [
                            [
                                'DeviceName': '/dev/xvda',
                                'Ebs': ['VolumeSize': '200', 'VolumeType': 'gp2', 'DeleteOnTermination': 'true']
                            ],
                            [
                                'DeviceName': '/dev/xvdb',
                                'Ebs': ['VolumeSize': '400', 'VolumeType': 'gp2']
                            ]
                        ]
                )
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new Instance(
                id: 'Bastion',
                Name: 'bastion',
                InstanceType: 't1.micro',
                KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                SubnetId: 'PublicSubnet',
                ImageId: ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
                IamInstanceProfile: ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
                SourceDestCheck: false,
                EIP: true,
                SecurityGroupIds: ['Internal', 'CmMainte'],
                volumes: null,
                userData: ['#! /bin/bash -v\\n', 'yum update -y\\n']
        )
        def expected = [
            'Bastion': [
                'Type': 'AWS::EC2::Instance',
                'Properties': [
                    'InstanceType': 't1.micro',
                    'KeyName': ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                    'SubnetId': ['Ref': 'PublicSubnet'],
                    'ImageId': ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
                    'IamInstanceProfile': ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
                    'SourceDestCheck': false,
                    'SecurityGroupIds': [ ['Ref': 'Internal'], ['Ref': 'CmMainte'] ],
                    'Tags': [
                        ['Key': 'Name', 'Value': 'bastion'],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ],
                    'UserData': [
                        'Fn::Base64': [
                            'Fn::Join': ['',
                                 ['#! /bin/bash -v\\n', 'yum update -y\\n']
                            ]
                        ]
                    ]
                ]
            ],
            'BastionEIP': [
                'Type': 'AWS::EC2::EIP',
                'Properties': [
                    'Domain': 'vpc',
                    'InstanceId': ["Ref": 'Bastion']
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


    @Test
    void "toResourceMap_Volume有"() {
        def sut = new Instance(
                id: 'Bastion',
                Name: 'bastion',
                InstanceType: 't1.micro',
                KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                SubnetId: 'PublicSubnet',
                ImageId: ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
                IamInstanceProfile: ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
                SourceDestCheck: false,
                EIP: false,
                SecurityGroupIds: ['Internal', 'CmMainte'],
                volumes: [['VolumeId': 'ExtVolume', 'Device': '/dev/sdh']],
                userData: []
        )
        def expected = [
                'Bastion': [
                        'Type': 'AWS::EC2::Instance',
                        'Properties': [
                                'InstanceType': 't1.micro',
                                'KeyName': ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                                'SubnetId': ['Ref': 'PublicSubnet'],
                                'ImageId': ['Fn::FindInMap': ['AMI', 'NAT', '201309']],
                                'IamInstanceProfile': ['Fn::FindInMap': ['Common', 'Role', 'Ec2Role']],
                                'SourceDestCheck': false,
                                'SecurityGroupIds': [ ['Ref': 'Internal'], ['Ref': 'CmMainte'] ],
                                'Tags': [
                                        ['Key': 'Name', 'Value': 'bastion'],
                                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                                ],
                                'Volumes': [['VolumeId': ['Ref':'ExtVolume'], 'Device': '/dev/sdh']]
                        ]
                ]
        ]
        assert sut.toResourceMap() == expected
    }


    @Test
    void "toResourceMap_BlockDeviceMappings"() {
        def sut = new Instance(
            id: 'WebServer',
            Name: 'WebServer',
            InstanceType: 'm3.medium',
            KeyName: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
            SubnetId: 'PublicSubnet',
            ImageId: ['Fn::FindInMap': ['AMI', 'AmazonLinux', '20140901']],
            SecurityGroupIds: [],
            BlockDeviceMappings: [
                [
                    'DeviceName': '/dev/xvda',
                    'Ebs': ['VolumeSize': '200', 'VolumeType': 'gp2', 'DeleteOnTermination': 'true']
                ],
                [
                    'DeviceName': '/dev/xvdb',
                    'Ebs': ['VolumeSize': '400', 'VolumeType': 'gp2']
                ]
            ]
        )
        def expected = [
            'WebServer': [
                'Type': 'AWS::EC2::Instance',
                'Properties': [
                    'InstanceType': 'm3.medium',
                    'KeyName': ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']],
                    'SubnetId': ['Ref': 'PublicSubnet'],
                    'ImageId': ['Fn::FindInMap': ['AMI', 'AmazonLinux', '20140901']],
                    'SecurityGroupIds': [],
                    'BlockDeviceMappings': [
                        [
                            'DeviceName': '/dev/xvda',
                            'Ebs': ['VolumeSize': '200', 'VolumeType': 'gp2', 'DeleteOnTermination': 'true']
                        ],
                        [
                            'DeviceName': '/dev/xvdb',
                            'Ebs': ['VolumeSize': '400', 'VolumeType': 'gp2']
                        ]
                    ],
                    'Tags': [
                            ['Key': 'Name', 'Value': 'WebServer'],
                            ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ],
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


}

