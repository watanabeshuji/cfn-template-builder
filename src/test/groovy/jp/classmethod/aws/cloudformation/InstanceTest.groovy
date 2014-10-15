package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/20.
 */
class InstanceTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("InstanceTest_default.csv").getFile())
        def actual = Instance.load(input)
        assert actual[0].userData == ['#! /bin/bash -v\\n', 'yum update -y\\n']
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
                        userData: ['#! /bin/bash -v\\n', 'yum update -y\\n']
                )
        ]
    }

    @Test
    void "withVolume.csvのload"() {
        File input = new File(getClass().getResource("InstanceTest_withVolume.csv").getFile())
        def actual = Instance.load(input)
        assert actual[0].userData == ['#! /bin/bash -v\\n', 'yum update -y\\n']
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
                        userData: ['#! /bin/bash -v\\n', 'yum update -y\\n']
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

}

