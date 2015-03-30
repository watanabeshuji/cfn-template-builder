resources {
    subnet id: "Subnet1", VpcId: [Ref: 'VPC'], CidrBlock: "10.0.0.0/24"
    subnet id: "Subnet2", VpcId: [Ref: 'VPC'], CidrBlock: "10.0.1.0/24", "AvailabilityZone": "ap-northeast-1a"
}