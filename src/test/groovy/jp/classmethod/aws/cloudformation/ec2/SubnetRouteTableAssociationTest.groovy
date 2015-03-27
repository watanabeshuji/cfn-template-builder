package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.ec2.SubnetRouteTableAssociation
import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/20.
 */
class SubnetRouteTableAssociationTest {

    @Test
    void "toResourceMap"() {
        def sut = new SubnetRouteTableAssociation(
                id: 'PublicRouteAssociation',
                SubnetId: [Ref: 'PublicSubnet'],
                RouteTableId: [Ref: 'PublicRouteTable'])
        def expected = [
            'Type': 'AWS::EC2::SubnetRouteTableAssociation',
            'Properties': [
                'SubnetId': ['Ref': 'PublicSubnet'],
                'RouteTableId': ['Ref': 'PublicRouteTable']
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
