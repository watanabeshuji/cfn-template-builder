package templates

cloudformation {
    parameters {
        "InstanceTypeParameter" Type: "String", Default: "t1.micro", AllowedValues: ["t1.micro", "m1.small", "m1.large"], Description: "Enter t1.micro, m1.small, or m1.large. Default is t1.micro."
    }
}