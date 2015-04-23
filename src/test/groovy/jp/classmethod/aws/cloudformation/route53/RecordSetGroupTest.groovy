package jp.classmethod.aws.cloudformation.route53

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2015/04/23.
 */
class RecordSetGroupTest {

    @Test
    void "load recordSet_SPF_ZoneId.groovy"() {
        Path input = getPath("/templates/resources/recordSetGroup.groovy")
        def actual = RecordSetGroup.load(input)
        def expected = [
            new RecordSetGroup(
                id: "MyDnsRecordSet", HostedZoneName: "example.com.", Comment: "A records for my frontends.",
                RecordSets: [
                    new RecordSet(Name: "mysite.example.com.", Type: "SPF", TTL: "900",
                        ResourceRecords: ['"v=spf1 ip4:192.168.0.1/16 -all"']
                    ),
                    new RecordSet(Name: "www2.example.com.", Type: "A", TTL: "900",
                        ResourceRecords: ['192.168.0.1', '192.168.0.2']
                    ),
                    new RecordSet(Name: "www.example.com.", Type: "A",
                        AliasTarget: new RecordSet.AliasTarget(
                            HostedZoneId: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneNameID"] ],
                            DNSName: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneName"] ]
                        )
                    )
                ]
            )
        ]
        assert actual == expected
    }


    @Test
    void "toResourceMap"() {
        def sut = RecordSetGroup.newInstance(id: "MyDnsRecordSet", HostedZoneName: "example.com.", Comment: "A records for my frontends.")
        sut.RecordSets << RecordSet.newInlineInstance(Name: "mysite.example.com.", Type: "SPF", TTL: "900", ResourceRecords: ['"v=spf1 ip4:192.168.0.1/16 -all"'])
        sut.RecordSets << RecordSet.newInlineInstance(Name: "www2.example.com.", Type: "A", TTL: "900", ResourceRecords: ['192.168.0.1', '192.168.0.2'])
        sut.RecordSets << RecordSet.newInlineInstance(Name: "www.example.com.", Type: "A")
        sut.RecordSets[2].AliasTarget = new RecordSet.AliasTarget(
            HostedZoneId: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneNameID"] ],
            DNSName: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneName"] ]
        )
        def expected = [
            'Type'      : 'AWS::Route53::RecordSetGroup',
            'Properties': [
                'HostedZoneName': 'example.com.',
                'Comment': 'A records for my frontends.',
                'RecordSets': [
                    [
                        'Name': 'mysite.example.com.',
                        'Type': 'SPF',
                        'TTL': '900',
                        'ResourceRecords': ['"v=spf1 ip4:192.168.0.1/16 -all"']
                    ],
                    [
                        'Name': 'www2.example.com.',
                        'Type': 'A',
                        'TTL': '900',
                        'ResourceRecords': ['192.168.0.1', '192.168.0.2']
                    ],
                    [
                        'Name': 'www.example.com.',
                        'Type': 'A',
                        'AliasTarget': [
                            HostedZoneId: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneNameID"] ],
                            DNSName: ["Fn::GetAtt": ["ELB", "CanonicalHostedZoneName"] ]
                        ]
                    ],
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
