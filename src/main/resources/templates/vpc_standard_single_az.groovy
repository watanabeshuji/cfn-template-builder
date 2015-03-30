package templates

cloudformation {
    description "a standard vpc template."
    resources {
        standardVpc(name: "Dev", multiAZ: false)
    }
}

