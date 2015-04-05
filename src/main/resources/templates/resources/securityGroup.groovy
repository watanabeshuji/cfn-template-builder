resources {
    securityGroup id: "PublicWeb", VpcId: [Ref: "VPC"], GroupDescription: "Allow web access from internet.", {
        securityGroupIngress IpProtocol: "tcp", FromPort: 80, ToPort: 80, CidrIp: "0.0.0.0/0"
        securityGroupIngress IpProtocol: "tcp", FromPort: 443, ToPort: 443, CidrIp: "0.0.0.0/0"
    }
}
