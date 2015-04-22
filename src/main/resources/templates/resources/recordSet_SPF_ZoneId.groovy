
resources {
    recordSet id: "MyDnsRecord", HostedZoneId: "/hostedzone/Z3DG6IL3SJCGPX",
        Name: "mysite.example.com.", Type: "SPF", TTL: "900", ResourceRecords: ['"v=spf1 ip4:192.168.0.1/16 -all"']
}
