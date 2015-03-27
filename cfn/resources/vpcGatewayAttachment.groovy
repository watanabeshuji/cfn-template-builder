resources {
    vpcGatewayAttachment id: "InternetGatewayAttach", VpcId: [Ref: "VPC"], InternetGatewayId: [Ref: "InternetGateway"]
}