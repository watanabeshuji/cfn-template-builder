cloudformation {
    description "a standard vpc template."
    resources {
        standardVpc(name: "Web", publicSubnetOnly: true)
    }
}

