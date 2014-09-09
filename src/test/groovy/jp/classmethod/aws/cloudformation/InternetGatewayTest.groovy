package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class InternetGatewayTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("InternetGatewayTest_default.csv").getFile())
        def actual = InternetGateway.load(input)
        assert actual == [
                new InternetGateway(id: 'Igw', name: 'igw', vpcId: 'Vpc')
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new InternetGateway(id: 'Igw', name: 'igw', vpcId: 'Vpc')
        def expected = [
                'Igw': [
                        'Type': 'AWS::EC2::InternetGateway',
                        'Properties': [
                                'Tags': [
                                        ['Key': 'Name', 'Value': 'igw'],
                                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                                ]
                        ]
                ],
                'AttachIgw': [
                        'Type': 'AWS::EC2::VPCGatewayAttachment',
                        'Properties': [
                                'VpcId': ["Ref": 'Vpc'],
                                'InternetGatewayId': ["Ref": 'Igw']
                        ]
                ]
        ]
        assert sut.toResourceMap() == expected
    }


}

