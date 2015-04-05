resources {
    elasticLoadBalancing id: "ELB", LoadBalancerName: "ELB",
        Subnets: [[Ref: "FrontA"], [Ref: "FrontC"]], SecurityGroups: [[Ref: "PublicWeb"]],
        Instances: [[Ref: "WebA"], [Ref: "WebC"]], {
        listener Protocol: "HTTP", LoadBalancerPort: "80", InstancePort: "80"
        listener Protocol: "HTTPS", LoadBalancerPort: "443", InstanceProtocol: "HTTP", InstancePort: "80", SSLCertificateId: "sslid"
        healthCheck Target: "HTTP:80/", HealthyThreshold: "3", UnhealthyThreshold: "5", Interval: "30", Timeout: "5"
        accessLoggingPolicy Enabled: true, S3BucketName: [Ref: "ElbAccessLoggingBucket"], S3BucketPrefix: "elb/", EmitInterval: 60
    }
}

