package templates.resources

resources {
    instanceProfile id: "EC2InstanceProfile", Path: "/", Roles: [[Ref: "EC2Role"]]
}
