cloudformation {
    outputs {
        "CodeDeployTrustRoleARN" Value: "GetAtt:CodeDeployTrustRole:Arn"
        "InstanceProfile" Value: "Ref:InstanceProfile", Description: "InstanceProfile Logical Id"
    }
}
