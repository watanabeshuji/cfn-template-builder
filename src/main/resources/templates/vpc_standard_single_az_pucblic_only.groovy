package templates

cloudformation {
    description "a standard vpc template."
    resources {
        standardVpc(multiAZ: false, publicSubnetOnly: true)
    }
}

