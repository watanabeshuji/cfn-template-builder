
resources {
    recordSet id: "MyDnsRecord2", HostedZoneName: "example.com.", Comment: "A records for my frontends.",
        Name: "www.example.com.", Type: "A", TTL: "900", ResourceRecords: ['192.168.0.1', '192.168.0.2']
}
