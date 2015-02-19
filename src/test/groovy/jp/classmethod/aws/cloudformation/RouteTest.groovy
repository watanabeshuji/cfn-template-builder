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
            new Route(id: 'PublicRoute',  Name: 'public-route',  RouteTable:'PublicRouteTable',  DestinationCidrBlock: '0.0.0.0/0',    GatewayId:'Igw'),
            new Route(id: 'PrivateRoute', Name: 'private-route', RouteTable:'PrivateRouteTable', DestinationCidrBlock: '0.0.0.0/0',    InstanceId:'Bastion'),
            new Route(id: 'PeeringRoute', Name: 'peering-route', RouteTable:'PrivateRouteTable', DestinationCidrBlock: '10.24.0.0/16', VpcPeeringConnectionId:['Fn::FindInMap':['Resources', 'VpcPeering', 'VPC2']])
        ]
    }

    @Test
    void "IGWでのtoResourceMap"() {
        def sut = new Route(id: 'PublicRoute',  Name: 'public-route',  RouteTable:'PublicRouteTable',  DestinationCidrBlock: '0.0.0.0/0', GatewayId:'Igw')
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
        def sut = new Route(id: 'PrivateRoute', Name: 'private-route', RouteTable:'PrivateRouteTable', DestinationCidrBlock: '0.0.0.0/0', InstanceId:'Bastion')
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

    @Test
    void "VPCPeeringでのtoResourceMap"() {
        def sut = new Route(id: 'PeeringRoute', Name: 'peering-route', RouteTable:'PrivateRouteTable', DestinationCidrBlock: '10.24.0.0/16', VpcPeeringConnectionId:['Fn::FindInMap':['Resources', 'VpcPeering', 'VPC2']])
        def expected = [
                "PeeringRoute": [
                        'Type': 'AWS::EC2::Route',
                        'Properties': [
                                'RouteTableId': ['Ref': 'PrivateRouteTable'],
                                'DestinationCidrBlock': '10.24.0.0/16',
                                'VpcPeeringConnectionId': ['Fn::FindInMap':['Resources', 'VpcPeering', 'VPC2']]
                        ]
                ]
        ]
        assert sut.toResourceMap() == expected
    }

}
