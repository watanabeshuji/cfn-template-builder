cloudformation {
    description "a separated template."
    resources {
        vpc id: "VPC", CidrBlock: "10.0.0.0/16"
    }
    resources "/templates/resources/subnet.groovy"
    resources "/templates/resources/securityGroup.groovy"
    resources "./templates/myMainte.groovy"
}