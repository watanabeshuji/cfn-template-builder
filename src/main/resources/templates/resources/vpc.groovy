package templates.resources

resources {
    vpc id: "vpc1", CidrBlock: "10.0.0.0/16"
    vpc id: "vpc2", CidrBlock: "10.0.0.0/16", EnableDnsSupport: true, EnableDnsHostnames: true
}