def ec2PolicyDocument = [
    "Version"  : "2012-10-17",
    "Statement": [
        ["Effect": "Allow", "Action": "*", "Resource": "*"]
    ]
]
resources {
    policy id: "EC2Policy", PolicyName: "EC2", PolicyDocument: ec2PolicyDocument, Roles: ["Ref:EC2Role"]
}
