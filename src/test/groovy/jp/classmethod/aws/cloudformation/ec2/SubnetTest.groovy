package jp.classmethod.aws.cloudformation.ec2

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class SubnetTest {


    @Test
    void "load subnet.groovy"() {
        Path input = getPath("/templates/resources/subnet.groovy")
        def actual = Subnet.load(input)
        assert actual == [
            new Subnet(id: 'Subnet1', CidrBlock: "10.0.0.0/24", VpcId: [Ref: "VPC"]),
            new Subnet(id: 'Subnet2', CidrBlock: "10.0.1.0/24", VpcId: [Ref: "VPC"], AvailabilityZone: "ap-northeast-1a")
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new Subnet(id: 'PublicSubnet', VpcId: [Ref: 'VPC'], CidrBlock: '10.0.0.0/24', AvailabilityZone: 'ap-northeast-1a')
        def expected = [
            'Type'      : 'AWS::EC2::Subnet',
            'Properties': [
                'VpcId'           : ["Ref": 'VPC'],
                'CidrBlock'       : '10.0.0.0/24',
                'AvailabilityZone': 'ap-northeast-1a',
                'Tags'            : [
                    ['Key': 'Name', 'Value': 'PublicSubnet'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
