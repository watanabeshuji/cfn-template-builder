resources {
    subnet id: "subnet1", VpcId: [Ref: 'vpc'], CidrBlock: "10.0.0.0/24"
    subnet id: "subnet2", VpcId: [Ref: 'vpc'], CidrBlock: "10.0.1.0/24", "AvailabilityZone": "ap-northeast-1a"
}