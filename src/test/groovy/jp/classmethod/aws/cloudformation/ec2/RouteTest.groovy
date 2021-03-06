package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.util.ValidErrorException
import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class RouteTest {

    @Test
    void "load route.groovy"() {
        Path input = getPath("/templates/resources/route.groovy")
        def actual = Route.load(input)
        assert actual == [
            new Route(id: 'PublicRoute', DestinationCidrBlock: '0.0.0.0/0', RouteTableId: [Ref: "RouteTable"], GatewayId: [Ref: "IGW"]),
            new Route(id: 'NatRoute', DestinationCidrBlock: '0.0.0.0/0', RouteTableId: [Ref: "RouteTable"], InstanceId: [Ref: "NAT"]),
            new Route(id: 'DirectConnectRoute', DestinationCidrBlock: '10.226.0.0/16', RouteTableId: [Ref: "RouteTable"], VpcPeeringConnectionId: "pcx-xxxxxxxx"),
        ]
    }

    @Test
    void "IGWでのtoResourceMap"() {
        def sut = new Route(id: 'PublicRoute', RouteTableId: [Ref: 'PublicRouteTable'], DestinationCidrBlock: '0.0.0.0/0', GatewayId: [Ref: 'InternetGateway'])
        def expected = [
            'Type'      : 'AWS::EC2::Route',
            'Properties': [
                'RouteTableId'        : ['Ref': 'PublicRouteTable'],
                'DestinationCidrBlock': '0.0.0.0/0',
                'GatewayId'           : ['Ref': 'InternetGateway']
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "NATでのtoResourceMap"() {
        def sut = new Route(id: 'PrivateRoute', RouteTableId: [Ref: 'PrivateRouteTable'], DestinationCidrBlock: '0.0.0.0/0', InstanceId: [Ref: 'Bastion'])
        def expected = [
            'Type'      : 'AWS::EC2::Route',
            'Properties': [
                'RouteTableId'        : ['Ref': 'PrivateRouteTable'],
                'DestinationCidrBlock': '0.0.0.0/0',
                'InstanceId'          : ['Ref': 'Bastion']
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "VPCPeeringでのtoResourceMap"() {
        def sut = new Route(id: 'PeeringRoute', RouteTableId: [Ref: 'PrivateRouteTable'], DestinationCidrBlock: '10.24.0.0/16', VpcPeeringConnectionId: ['Fn::FindInMap': ['Resources', 'VpcPeering', 'VPC2']])
        def expected = [
            'Type'      : 'AWS::EC2::Route',
            'Properties': [
                'RouteTableId'          : ['Ref': 'PrivateRouteTable'],
                'DestinationCidrBlock'  : '10.24.0.0/16',
                'VpcPeeringConnectionId': ['Fn::FindInMap': ['Resources', 'VpcPeering', 'VPC2']]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "refIds"() {
        def sut = Route.newInstance(
            id: 'PrivateRoute', RouteTableId: "Ref:PrivateRouteTable",
            DestinationCidrBlock: '0.0.0.0/0', InstanceId: "Ref:Bastion")
        assert sut.refIds == ['PrivateRouteTable', 'Bastion']
    }

    @Test(expected = ValidErrorException)
    void "DestinationCidrBlock必須"() {
        Route.newInstance(id: 'PrivateRoute', RouteTableId: [Ref: 'PrivateRouteTable'], InstanceId: [Ref: 'Bastion'])
    }

    @Test(expected = ValidErrorException)
    void "RouteTableId必須"() {
        Route.newInstance(id: 'PrivateRoute', DestinationCidrBlock: '0.0.0.0/0', InstanceId: [Ref: 'Bastion'])
    }

    @Test(expected = ValidErrorException)
    void "GatewayId,InstanceId,VpcPeeringConnectionId どれかが必須"() {
        Route.newInstance(id: 'PrivateRoute', RouteTableId: "Ref:PrivateRouteTable", DestinationCidrBlock: '0.0.0.0/0')
    }

}
