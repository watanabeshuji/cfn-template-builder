resources {
    route id: "PublicRoute", DestinationCidrBlock: "0.0.0.0/0", RouteTableId: "Ref:RouteTable", GatewayId: [Ref: "IGW"]
    route id: "NatRoute", DestinationCidrBlock: "0.0.0.0/0", RouteTableId: "Ref:RouteTable", InstanceId: [Ref: "NAT"]
    route id: "DirectConnectRoute", DestinationCidrBlock: "10.226.0.0/16", RouteTableId: "Ref:RouteTable", VpcPeeringConnectionId: "pcx-xxxxxxxx"
}
