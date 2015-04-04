package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

@Canonical
class Route extends Resource {

    static final def TYPE = 'AWS::EC2::Route'
    def id
    def RouteTableId
    def DestinationCidrBlock
    def GatewayId
    def InstanceId
    def VpcPeeringConnectionId

    static Route newInstance(Map params) {
        convert(params)
        checkKeys(TYPE, params, ['id', 'RouteTableId', 'DestinationCidrBlock', 'GatewayId', 'InstanceId', 'VpcPeeringConnectionId'])
        logicalId(TYPE, params)
        require(TYPE, 'RouteTableId', params)
        require(TYPE, 'DestinationCidrBlock', params)
        // TODO conditonal
        def instance = new Route(params)
        instance.addRefIds([params['RouteTableId'], params['GatewayId'], params['InstanceId'], params['VpcPeeringConnectionId']])
        instance
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