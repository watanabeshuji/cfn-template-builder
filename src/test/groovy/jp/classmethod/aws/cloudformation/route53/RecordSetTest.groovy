package jp.classmethod.aws.cloudformation.route53

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2015/04/22.
 */
class RecordSetTest {

    @Test
    void "load recordSet_SPF_ZoneId.groovy"() {
        Path input = getPath("/templates/resources/recordSet_SPF_ZoneId.groovy")
        def actual = RecordSet.load(input)
        def expected = [
            new RecordSet(id: 'MyDnsRecord', HostedZoneId: "/hostedzone/Z3DG6IL3SJCGPX", Name: "mysite.example.com.",
                Type: "SPF", TTL: "900", ResourceRecords: ['"v=spf1 ip4:192.168.0.1/16 -all"']
            )
        ]
        assert actual == expected
    }

    @Test
    void "load recordSet_A_ZoneName.groovy"() {
        Path input = getPath("/templates/resources/recordSet_A_ZoneName.groovy")
        def actual = RecordSet.load(input)
        def expected = [
            new RecordSet(id: 'MyDnsRecord2', HostedZoneName: "example.com.", Comment: "A records for my frontends.",
                Name: "www.example.com.", Type: "A", TTL: "900",
                ResourceRecords: ['192.168.0.1', '192.168.0.2']
            )
        ]
        assert actual == expected
    }


    @Test
    void "load recordSet_elb.groovy"() {
        Path input = getPath("/templates/resources/recordSet_elb.groovy")
        def actual = RecordSet.load(input)
        def expected = [
            new RecordSet(id: 'MyDnsRecord', HostedZoneName: "example.com.", Comment: "A records for elb.",
                Name: "www.example.com.", Type: "A",
                AliasTarget: new RecordSet.AliasTarget(
                    HostedZoneId: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneNameID"] ],
                    DNSName: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneName"] ]
                )
            )
        ]
        assert actual == expected
    }

    @Test
    void "toResourceMap SPF Record"() {
        def sut = RecordSet.newInstance(id: 'MyDnsRecord', HostedZoneId: "/hostedzone/Z3DG6IL3SJCGPX", Name: "mysite.example.com.",
            Type: "SPF", TTL: "900", ResourceRecords: ['"v=spf1 ip4:192.168.0.1/16 -all"']
        )
        def expected = [
            'Type'      : 'AWS::Route53::RecordSet',
            'Properties': [
                'HostedZoneId': '/hostedzone/Z3DG6IL3SJCGPX',
                'Name': 'mysite.example.com.',
                'Type': 'SPF',
                'TTL': '900',
                'ResourceRecords': ['"v=spf1 ip4:192.168.0.1/16 -all"']
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "toResourceMap A Record"() {
        def sut = RecordSet.newInstance(
            id: 'MyDnsRecord2', HostedZoneName: "example.com.", Comment: "A records for my frontends.",
            Name: "www.example.com.", Type: "A", TTL: "900",
            ResourceRecords: ['192.168.0.1', '192.168.0.2']
        )
        def expected = [
            'Type'      : 'AWS::Route53::RecordSet',
            'Properties': [
                'HostedZoneName': 'example.com.',
                'Name': 'www.example.com.',
                'Type': 'A',
                'TTL': '900',
                'ResourceRecords': ['192.168.0.1', '192.168.0.2'],
                'Comment': "A records for my frontends."
            ]
        ]
        assert sut.toResourceMap() == expected
    }


    @Test
    void "toResourceMap ELB A Record"() {
        def sut = new RecordSet(id: 'MyDnsRecord', HostedZoneName: "example.com.", Comment: "A records for elb.",
            Name: "www.example.com.", Type: "A",
            AliasTarget: new RecordSet.AliasTarget(
                HostedZoneId: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneNameID"] ],
                DNSName: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneName"] ]
            )
        )
        def expected = [
            'Type'      : 'AWS::Route53::RecordSet',
            'Properties': [
                'HostedZoneName': 'example.com.',
                'Name': 'www.example.com.',
                'Type': 'A',
                'Comment': "A records for elb.",
                'AliasTarget': [
                    HostedZoneId: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneNameID"] ],
                    DNSName: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneName"] ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }
}
