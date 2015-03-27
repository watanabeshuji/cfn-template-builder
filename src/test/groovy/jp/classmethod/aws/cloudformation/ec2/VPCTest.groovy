package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.ec2.VPC
import org.junit.Test

/**
 * Test for VPC
 */
class VPCTest {

    @Test
    void "toResourceMap"() {
        def sut = new VPC(id: 'VPC', CidrBlock: '10.0.0.0/16')
        def expected = [
            'Type': 'AWS::EC2::VPC',
            'Properties': [
                'CidrBlock': '10.0.0.0/16',
                'Tags': [
                    ['Key': 'Name', 'Value': 'VPC'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "toResourceMap_EnableDnsSupportのload"() {
        def sut = new VPC(id: 'VPC', CidrBlock: ['Ref': 'VpcCidrBlock'], EnableDnsSupport: true, EnableDnsHostnames: false)
        def expected = [
            'Type': 'AWS::EC2::VPC',
            'Properties': [
                'CidrBlock': ['Ref': 'VpcCidrBlock'],
                'EnableDnsSupport': true,
                'EnableDnsHostnames': false,
                'Tags': [
                    ['Key': 'Name', 'Value': 'VPC'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
