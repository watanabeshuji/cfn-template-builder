package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class RouteTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("RouteTest_default.csv").getFile())
        def actual = Route.load(input)
        assert actual == [
            new Route(id: 'PublicRoute',  Name: 'public-route',  RouteTable:'PublicRouteTable',  DestinationCidrBlock: '0.0.0.0/0', Gateway:'Igw'),
            new Route(id: 'PrivateRoute', Name: 'private-route', RouteTable:'PrivateRouteTable', DestinationCidrBlock: '0.0.0.0/0', Instance:'Bastion')
        ]
    }

    @Test
    void "IGWでのtoResourceMap"() {
        def sut = new Route(id: 'PublicRoute',  Name: 'public-route',  RouteTable:'PublicRouteTable',  DestinationCidrBlock: '0.0.0.0/0', Gateway:'Igw')
        def expected = [
            "PublicRoute": [
                'Type': 'AWS::EC2::Route',
                'DependsOn': 'AttachIgw',
                'Properties': [
                    'RouteTableId': ['Ref': 'PublicRouteTable'],
                    'DestinationCidrBlock': '0.0.0.0/0',
                    'GatewayId': ['Ref': 'Igw']
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "NATでのtoResourceMap"() {
        def sut = new Route(id: 'PrivateRoute', Name: 'private-route', RouteTable:'PrivateRouteTable', DestinationCidrBlock: '0.0.0.0/0', Instance:'Bastion')
        def expected = [
                "PrivateRoute": [
                        'Type': 'AWS::EC2::Route',
                        'DependsOn': 'Bastion',
                        'Properties': [
                                'RouteTableId': ['Ref': 'PrivateRouteTable'],
                                'DestinationCidrBlock': '0.0.0.0/0',
                                'InstanceId': ['Ref': 'Bastion']
                        ]
                ]
        ]
        assert sut.toResourceMap() == expected
    }

}
