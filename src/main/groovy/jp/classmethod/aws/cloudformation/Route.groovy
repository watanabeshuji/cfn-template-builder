package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class Route {
    def id
    def Name
    def RouteTable
    def DestinationCidrBlock
    def GatewayId
    def InstanceId
    def VpcPeeringConnectionId

    def Route() {}

    def Route(Source source) {
        if (source.camelCase('Gateway')) throw new UnsupportedFormatException("User 'GatewayId' column instead of 'Gateway' column")
        if (source.camelCase('Instance')) throw new UnsupportedFormatException("User 'InstanceId' column instead of 'Instance' column")
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.RouteTable = source.camelCase('RouteTable')
        this.DestinationCidrBlock = source.value('DestinationCidrBlock')
        this.GatewayId = source.camelCase('GatewayId')
        this.InstanceId = source.camelCase('InstanceId')
        this.VpcPeeringConnectionId = source.camelCase('VpcPeeringConnectionId')
        if (!GatewayId && !InstanceId && !VpcPeeringConnectionId) throw new UnsupportedFormatException("You must set column, one of 'GatewayId' or 'InstanceId' or 'VpcPeeringConnectionId'")
    }

    def toResourceMap() {
        def obj = [
            'Type': 'AWS::EC2::Route',
            'Properties': [
                'RouteTableId': Util.ref(RouteTable),
                'DestinationCidrBlock': DestinationCidrBlock
            ]
        ]
        if (GatewayId) {
            obj['DependsOn'] = "Attach$GatewayId"
            obj['Properties']['GatewayId'] = Util.ref(GatewayId)
        }
        if (InstanceId) {
            obj['DependsOn'] = InstanceId
            obj['Properties']['InstanceId'] = Util.ref(InstanceId)
        }
        if (VpcPeeringConnectionId) {
            obj['Properties']['VpcPeeringConnectionId'] = Util.ref(VpcPeeringConnectionId)
        }
        [ (this.id): obj]
    }

    static def load(File file) {
        Util.load(file, {new Route(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Route r -> o << r.toResourceMap() })
    }

}