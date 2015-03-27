package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.ec2.RouteTable
import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class RouteTableTest {

    @Test
    void "toResourceMap"() {
        def sut = new RouteTable(id: 'PublicRouteTable', VpcId: [Ref: 'VPC'])
        def expected = [
            'Type': 'AWS::EC2::RouteTable',
            'Properties': [
                'VpcId': ['Ref': 'VPC'],
                'Tags': [
                    ['Key': 'Name', 'Value': 'PublicRouteTable'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
