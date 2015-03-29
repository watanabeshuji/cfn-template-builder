package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

@Canonical
class Route extends Resource {

    def id
    def RouteTableId
    def DestinationCidrBlock
    def GatewayId
    def InstanceId
    def VpcPeeringConnectionId

    def Route() {}

    def toResourceMap() {
        def obj = [
            'Type'      : 'AWS::EC2::Route',
            'Properties': [
                'RouteTableId'        : RouteTableId,
                'DestinationCidrBlock': DestinationCidrBlock
            ]
        ]
        if (GatewayId) obj['Properties']['GatewayId'] = GatewayId
        if (InstanceId) obj['Properties']['InstanceId'] = InstanceId
        if (VpcPeeringConnectionId) obj['Properties']['VpcPeeringConnectionId'] = VpcPeeringConnectionId
        obj
    }

}