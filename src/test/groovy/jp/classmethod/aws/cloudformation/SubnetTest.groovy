package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class SubnetTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("SubnetTest_default.csv").getFile())
        def actual = Subnet.load(input)
        assert actual == [
                new Subnet(id: 'PublicSubnet',  Name: 'public-subnet',  Vpc:'Vpc', CidrBlock: '10.0.0.0/24', AvailabilityZone: 'ap-northeast-1a'),
                new Subnet(id: 'PrivateSubnet', Name: 'private-subnet', Vpc:'Vpc', CidrBlock: '10.0.2.0/24', AvailabilityZone: 'ap-northeast-1a')
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new Subnet(id: 'PublicSubnet',  Name: 'public-subnet',  Vpc:'Vpc', CidrBlock: '10.0.0.0/24', AvailabilityZone: 'ap-northeast-1a')
        def expected = [
            "PublicSubnet": [
                'Type': 'AWS::EC2::Subnet',
                'Properties': [
                    'VpcId': ["Ref": 'Vpc'],
                    'CidrBlock': '10.0.0.0/24',
                    'AvailabilityZone': 'ap-northeast-1a',
                    'Tags': [
                        ['Key': 'Name', 'Value': 'public-subnet'],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
