resources {
    route id: "PublicRoute",        DestinationCidrBlock: "0.0.0.0/0"     ,GatewayId: [Ref: "IGW"]
    route id: "NatRoute",           DestinationCidrBlock: "0.0.0.0/0"     ,InstanceId: [Ref: "NAT"]
    route id: "DirectConnectRoute", DestinationCidrBlock: "10.226.0.0/16" ,VpcPeeringConnectionId: "pcx-xxxxxxxx"
}
