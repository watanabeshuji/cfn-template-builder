package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.ec2.Subnet
import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class SubnetTest {

    @Test
    void "toResourceMap"() {
        def sut = new Subnet(id: 'PublicSubnet', VpcId: [Ref: 'VPC'], CidrBlock: '10.0.0.0/24', AvailabilityZone: 'ap-northeast-1a')
        def expected = [
            'Type': 'AWS::EC2::Subnet',
            'Properties': [
                'VpcId': ["Ref": 'VPC'],
                'CidrBlock': '10.0.0.0/24',
                'AvailabilityZone': 'ap-northeast-1a',
                'Tags': [
                    ['Key': 'Name', 'Value': 'PublicSubnet'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
