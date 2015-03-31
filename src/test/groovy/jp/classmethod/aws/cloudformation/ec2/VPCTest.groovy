package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.util.ValidErrorException
import org.junit.Test

import java.nio.file.Paths

import static jp.classmethod.aws.cloudformation.testing.TestSupport.*

import java.nio.file.Path

/**
 * Test for VPC
 */
class VPCTest {

    @Test
    void "load vpc.groovy"() {
        Path input = Paths.get(getClass().getResource("/templates/resources/vpc.groovy").getPath())
        def actual = VPC.load(input)
        def expected = [
            new VPC(id: 'VPC', CidrBlock: "10.0.0.0/16")
        ]
        assert actual == expected
    }

    @Test
    void "load vpc_EnableDns.groovy"() {
        Path input = Paths.get(getClass().getResource("vpc_EnableDns.groovy").getPath())
        def actual = VPC.load(input)
        def expected = [
            new VPC(id: 'VPC', CidrBlock: "10.0.0.0/16", EnableDnsSupport: true, EnableDnsHostnames: true),
        ]
        assert actual == expected
    }


    @Test
    void "load vpc_withRef.groovy"() {
        Path input = Paths.get(getClass().getResource("vpc_withRef.groovy").getPath())
        def actual = VPC.load(input)
        def expected = [
            new VPC(id: 'DevVPC', CidrBlock: [Ref: "VPCRange"]),
        ]
        assert actual == expected
    }


    @Test
    void "load vpc_withTags.groovy"() {
        Path input = Paths.get(getClass().getResource("vpc_withTags.groovy").getPath())
        def actual = VPC.load(input)
        def expected = [
            new VPC(id: 'DevVPC', CidrBlock: "10.0.0.0/16", Tags: [Name: 'dev-vpc', Env: 'Development']),
        ]
        assert actual == expected
    }

    @Test
    void "toResourceMap"() {
        def sut = new VPC(id: 'VPC', CidrBlock: '10.0.0.0/16')
        def expected = [
            'Type'      : 'AWS::EC2::VPC',
            'Properties': [
                'CidrBlock': '10.0.0.0/16',
                'Tags'     : [
                    ['Key': 'Name', 'Value': 'VPC'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "toResourceMap_EnableDnsSupport"() {
        def sut = new VPC(id: 'VPC', CidrBlock: ['Ref': 'VpcCidrBlock'], EnableDnsSupport: true, EnableDnsHostnames: false)
        def expected = [
            'Type'      : 'AWS::EC2::VPC',
            'Properties': [
                'CidrBlock'         : ['Ref': 'VpcCidrBlock'],
                'EnableDnsSupport'  : true,
                'EnableDnsHostnames': false,
                'Tags'              : [
                    ['Key': 'Name', 'Value': 'VPC'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


    @Test
    void "toResourceMap Tags"() {
        def sut = new VPC(id: 'DevVPC', CidrBlock: "10.0.0.0/16", Tags: [Name: 'dev-vpc', Env: 'Development'])
        def expected = [
            'Type'      : 'AWS::EC2::VPC',
            'Properties': [
                'CidrBlock'         : "10.0.0.0/16",
                'Tags'              : [
                    ['Key': 'Name', 'Value': 'dev-vpc'],
                    ['Key': 'Env', 'Value': 'Development'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test(expected = ValidErrorException)
    void "doValidate: id必須"() {
        def sut = new VPC(CidrBlock: '10.0.0.0/16')
        sut.doValidate()
    }

    @Test(expected = ValidErrorException)
    void "doValidate: idにハイフン"() {
        def sut = new VPC(id: "vpc-dev", CidrBlock: '10.0.0.0/16')
        sut.doValidate()
    }

    @Test(expected = ValidErrorException)
    void "doValidate: CidrBlock必須"() {
        def sut = new VPC(id: "vpc")
        sut.doValidate()
    }

    @Test(expected = ValidErrorException)
    void "doValidate: EnableDnsSupport bool"() {
        def sut = new VPC(id: "vpc", CidrBlock: '10.0.0.0/16', EnableDnsSupport: "yes")
        sut.doValidate()
    }

    @Test(expected = ValidErrorException)
    void "doValidate: EnableDnsHostnames bool"() {
        def sut = new VPC(id: "vpc", CidrBlock: '10.0.0.0/16', EnableDnsHostnames: "yes")
        sut.doValidate()
    }
}
