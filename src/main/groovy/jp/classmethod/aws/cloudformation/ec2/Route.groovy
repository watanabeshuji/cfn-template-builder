package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

@Canonical
class Route extends Resource {

    static final def TYPE = 'AWS::EC2::Route'
    static def DESC = '''\
AWS::EC2::Route
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-route.html

[Required Params]
- id
- RouteTableId
- DestinationCidrBlock
- GatewayId or InstanceId or VpcPeeringConnectionId

[Optional Params]

[Sample]
resources {
    route id: "PublicRoute",        DestinationCidrBlock: "0.0.0.0/0"     ,RouteTableId: "Ref:RouteTable", GatewayId: [Ref: "IGW"]
    route id: "NatRoute",           DestinationCidrBlock: "0.0.0.0/0"     ,RouteTableId: "Ref:RouteTable", InstanceId: [Ref: "NAT"]
    route id: "DirectConnectRoute", DestinationCidrBlock: "10.226.0.0/16" ,RouteTableId: "Ref:RouteTable", VpcPeeringConnectionId: "pcx-xxxxxxxx"
}
'''

    def id
    def RouteTableId
    def DestinationCidrBlock
    def GatewayId
    def InstanceId
    def VpcPeeringConnectionId

    static Route newInstance(Map params) {
        convert(params)
        checkKeys(Route, params, ['id', 'RouteTableId', 'DestinationCidrBlock', 'GatewayId', 'InstanceId', 'VpcPeeringConnectionId'])
        logicalId(Route, params)
        require(Route, ['RouteTableId', 'DestinationCidrBlock'], params)
        requireOneOf(Route, ['GatewayId', 'InstanceId', 'VpcPeeringConnectionId'], params)
        new Route(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'RouteTableId'        : RouteTableId,
                'DestinationCidrBlock': DestinationCidrBlock
            ]
        ]
        if (GatewayId) map['Properties']['GatewayId'] = GatewayId
        if (InstanceId) map['Properties']['InstanceId'] = InstanceId
        if (VpcPeeringConnectionId) map['Properties']['VpcPeeringConnectionId'] = VpcPeeringConnectionId
        map
    }

}