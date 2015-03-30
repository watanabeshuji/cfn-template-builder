resources {
    securityGroup id: "MyMainte", VpcId: [Ref: "VPC"], Description: "Allow ssh access from specific ip address.", {
        securityGroupIngress IpProtocol: "tcp", FromPort: 22,  ToPort: 22, CidrIp: "1.2.3.4/32"
        securityGroupIngress IpProtocol: "tcp", FromPort: 22,  ToPort: 22, CidrIp: "1.2.3.5/32"
    }
}
