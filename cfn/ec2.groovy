cloudformation {
    description "a ec2 template."
    resources {
        waitConditionHandle id: "WebWaitConditionHandle"
        waitCondition id: "WebWaitCondition", Handle: [Ref: "WebWaitConditionHandle"], Timeout: "1000"
        eip id: "WebIP", InstanceId: [Ref: "Web"]
    }
}

