package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/20.
 */
class SubnetRouteTableAssociationTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("SubnetRouteTableAssociationTest_default.csv").getFile())
        def actual = SubnetRouteTableAssociation.load(input)
        assert actual == [
                new SubnetRouteTableAssociation(id: 'PublicRouteAssociation',  name: 'public-route-association',  subnetId: 'PublicSubnet',  routeTableId: 'PublicRouteTable'),
                new SubnetRouteTableAssociation(id: 'PrivateRouteAssociation', name: 'private-route-association', subnetId: 'PrivateSubnet', routeTableId: 'PrivateRouteTable')
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new SubnetRouteTableAssociation(
                id: 'PublicRouteAssociation',
                name: 'public-route-association',
                subnetId: 'PublicSubnet',
                routeTableId: 'PublicRouteTable')
        def expected = [
            "PublicRouteAssociation": [
                'Type': 'AWS::EC2::SubnetRouteTableAssociation',
                'Properties': [
                    'SubnetId': ['Ref': 'PublicSubnet'],
                    'RouteTableId': ['Ref': 'PublicRouteTable']
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
