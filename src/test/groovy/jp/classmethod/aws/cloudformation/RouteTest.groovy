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
            new Route(id: 'PublicRoute',  name: 'public-route',  routeTableId:'PublicRouteTable',  cidrBlock: '0.0.0.0/0', gatewayId:'Igw'),
            new Route(id: 'PrivateRoute', name: 'private-route', routeTableId:'PrivateRouteTable', cidrBlock: '0.0.0.0/0', instanceId:'Bastion')
        ]
    }

    @Test
    void "IGWでのtoResourceMap"() {
        def sut = new Route(id: 'PublicRoute',  name: 'public-route',  routeTableId:'PublicRouteTable',  cidrBlock: '0.0.0.0/0', gatewayId:'Igw')
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
        def sut = new Route(id: 'PrivateRoute', name: 'private-route', routeTableId:'PrivateRouteTable', cidrBlock: '0.0.0.0/0', instanceId:'Bastion')
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
