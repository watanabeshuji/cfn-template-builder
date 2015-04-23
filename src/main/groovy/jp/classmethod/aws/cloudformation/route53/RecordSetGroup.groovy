package jp.classmethod.aws.cloudformation.route53

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * Created by watanabeshuji on 2015/04/22.
 */
@Canonical
class RecordSetGroup extends Resource {

    static final def TYPE = 'AWS::Route53::RecordSetGroup'
    static def DESC = '''\
AWS::Route53::RecordSetGroup
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-route53-recordsetgroup.html

[Required Params]
- id
- RecordSets
- HostedZoneId or HostedZoneName

[Optional Params]
- Comment

[Sample]
resources {
    recordSetGroup id: "MyDnsRecordSet", HostedZoneName: "example.com.", Comment: "A records for my frontends.", {
        recordSets Name: "mysite.example.com.", Type: "SPF", TTL: "900", ResourceRecords: ['"v=spf1 ip4:192.168.0.1/16 -all"']
        recordSets Name: "www2.example.com.", Type: "A", TTL: "900", ResourceRecords: ['192.168.0.1', '192.168.0.2']
        recordSets Name: "www.example.com.", Type: "A", {
            aliasTarget HostedZoneId: "GetAtt:ELB:CanonicalHostedZoneNameID", DNSName: "GetAtt:ELB:CanonicalHostedZoneName"
        }
    }
}
'''
    def id
    def HostedZoneId
    def HostedZoneName
    def Comment
    def RecordSets = []

    static RecordSetGroup newInstance(Map params) {
        convert(params)
        checkKeys(HostedZone, params, [
            'id', 'HostedZoneId', 'HostedZoneName', 'Comment'
        ])
        logicalId(RecordSetGroup, params)
        requireOneOf(RecordSetGroup, ['HostedZoneId', 'HostedZoneName'], params)
        new RecordSetGroup(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'RecordSets': RecordSets.collect { it.toInlineMap() }
            ]
        ]
        if (HostedZoneId) map['Properties']['HostedZoneId'] = HostedZoneId
        if (HostedZoneName) map['Properties']['HostedZoneName'] = HostedZoneName
        if (Comment) map['Properties']['Comment'] = Comment
        map
    }

}
