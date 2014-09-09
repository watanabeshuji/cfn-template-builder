package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Test for VPC
 */
class VPCTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("VPCTest_default.csv").getFile())
        def actual = VPC.load(input)
        assert actual == [
            new VPC(id: 'Vpc', name: 'vpc', cidrBlock: '10.0.0.0/16')
        ]
    }

    @Test
    void "2vpcs.csvのload"() {
        File input = new File(getClass().getResource("VPCTest_2vpcs.csv").getFile())
        def actual = VPC.load(input)
        assert actual == [
                new VPC(id: 'VpcA', name: 'vpc-a', cidrBlock: '10.0.0.0/16'),
                new VPC(id: 'VpcB', name: 'vpc-b', cidrBlock: '10.1.0.0/16')
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new VPC(id: 'Vpc', name: 'vpc', cidrBlock: '10.0.0.0/16')
        def expected = [
            "Vpc": [
                'Type': 'AWS::EC2::VPC',
                'Properties': [
                    'CidrBlock': '10.0.0.0/16',
                    'Tags': [
                        ['Key': 'Name', 'Value': 'vpc'],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
