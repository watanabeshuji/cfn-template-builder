def ec2PolicyDocument = [
    "Version" : "2012-10-17",
    "Statement": [
        ["Effect": "Allow", "Action": "*", "Resource": "*" ]
    ]
]


cloudformation {
    description "a iam template."
    resources {
        role id: "EC2Role"
        policy id: "EC2Policy", PolicyName: "EC2", PolicyDocument: ec2PolicyDocument, Roles: [[Ref: "EC2Role"]]
        instanceProfile id: "EC2InstanceProfile", Path: "/", Roles: [[Ref: "EC2Role"]]
    }
}

