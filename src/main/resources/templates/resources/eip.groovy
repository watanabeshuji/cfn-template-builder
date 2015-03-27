package templates.resources

resources {
    eip id: "PublicIP"
    eip id: "WebIP", InstanceId: [Ref: "Web"]
}
