cloudformation {
    outputs {
        "CodeDeployTrustRoleARN" "GetAtt:CodeDeployTrustRole:Arn"
        "InstanceProfile" "Ref:InstanceProfile"
    }
}
