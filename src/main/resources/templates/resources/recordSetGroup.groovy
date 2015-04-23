resources {
    recordSetGroup id: "MyDnsRecordSet", HostedZoneName: "example.com.", Comment: "A records for my frontends.", {
        recordSets Name: "mysite.example.com.", Type: "SPF", TTL: "900", ResourceRecords: ['"v=spf1 ip4:192.168.0.1/16 -all"']
        recordSets Name: "www2.example.com.", Type: "A", TTL: "900", ResourceRecords: ['192.168.0.1', '192.168.0.2']
        recordSets Name: "www.example.com.", Type: "A", {
            aliasTarget HostedZoneId: "GetAtt:ELB:CanonicalHostedZoneNameID", DNSName: "GetAtt:ELB:CanonicalHostedZoneName"
        }
    }
}
