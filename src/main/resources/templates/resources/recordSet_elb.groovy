package templates.resources

resources {
    recordSet id: "MyDnsRecord", HostedZoneName: "example.com.", Comment: "A records for elb.",
        Name: "www.example.com.", Type: "A", {
            aliasTarget HostedZoneId: "GetAtt:ELB:CanonicalHostedZoneNameID",
                        DNSName: "GetAtt:ELB:CanonicalHostedZoneName"
    }
}

