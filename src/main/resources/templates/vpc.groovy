def vpcId = [Ref: "VPC"]

cloudformation {
    description "a vpc template."
    mappings = [
        RegionMap: [
            "us-northeast-1": ["AvailabilityZoneA": "ap-northeast-1a", "AvailabilityZoneC": "ap-northeast-1c"]
        ]
    ]
    resources {
        vpc id: "VPC", CidrBlock: "10.0.0.0/16"
        internetGateway id: "InternetGateway"
        vpcGatewayAttachment id: "InternetGatewayAttach", VpcId: [Ref: "VPC"], InternetGatewayId: [Ref: "InternetGateway"]
        subnet id: "SubnetA", VpcId: vpcId, CidrBlock: "10.0.0.0/24", AvailabilityZone: "ap-northeast-1a"
        subnet id: "SubnetC", VpcId: vpcId, CidrBlock: "10.0.1.0/24", AvailabilityZone: "ap-northeast-1c"
        routeTable id: "PublicRouteTable",  VpcId: vpcId
        routeTable id: "PrivateRouteTable", VpcId: vpcId
        route id: "PublicRoute", RouteTableId: [Ref: "PublicRouteTable"], DestinationCidrBlock: "0.0.0.0/0", GatewayId: [Ref: "InternetGateway"]
        subnetRouteTableAssociation id: "SubnetRouteTableAssociationA", SubnetId: [Ref: "SubnetA"], RouteTableId: [Ref: "PublicRouteTable"]
        subnetRouteTableAssociation id: "SubnetRouteTableAssociationC", SubnetId: [Ref: "SubnetC"], RouteTableId: [Ref: "PublicRouteTable"]
        securityGroup id: "PublicWeb", VpcId: vpcId, GroupDescription: "Allow web access from internet.", {
            securityGroupIngress IpProtocol: "tcp", FromPort: 80,  ToPort:  80, CidrIp: "0.0.0.0/0"
            securityGroupIngress IpProtocol: "tcp", FromPort: 443, ToPort: 443, CidrIp: "0.0.0.0/0"
        }
    }
}

