resources {
    waitCondition id: "WebServerWaitCondition", Handle: [Ref: "WebServerWaitHandle"], Timeout: "1000"
}
