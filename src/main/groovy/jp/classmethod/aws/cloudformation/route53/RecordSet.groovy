package jp.classmethod.aws.cloudformation.route53

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * Created by watanabeshuji on 2015/04/22.
 */
@Canonical
class RecordSet extends Resource {

    static final def TYPE = 'AWS::Route53::RecordSet'
    static def DESC = '''\
AWS::Route53::RecordSet
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-route53-recordset.html

[Required Params]
- id
- Name
- Type

[Optional Params]
- TTL

[Sample]
resources {
    recordSet id: "MyDnsRecord2", HostedZoneName: "example.com.", Comment: "A records for my frontends.",
        Name: "www.example.com.", Type: "A", TTL: "900", ResourceRecords: ['192.168.0.1', '192.168.0.2']
}

[Sample (ELB)]
resources {
    recordSet id: "MyDnsRecord", HostedZoneName: "example.com.", Comment: "A records for elb.",
        Name: "www.example.com.", Type: "A", {
            aliasTarget HostedZoneId: "GetAtt:ELB:CanonicalHostedZoneNameID",
                        DNSName: "GetAtt:ELB:CanonicalHostedZoneName"
    }
}


'''
    def id
    def Name
    def HostedZoneId
    def HostedZoneName
    def Type
    def TTL
    def ResourceRecords
    def SetIdentifier
    def Comment
    def AliasTarget

    static RecordSet newInstance(Map params) {
        convert(params)
        checkKeys(RecordSet, params, [
            'id', 'Name', 'Type', 'HostedZoneId', 'HostedZoneName', 'TTL', 'ResourceRecords', 'SetIdentifier', 'Comment'
        ])
        logicalId(RecordSet, params)
        require(RecordSet, ['Name', 'Type'], params)
        requireOneOf(RecordSet, ['HostedZoneId', 'HostedZoneName'], params)
        new RecordSet(params).withRefIds(params)
    }

    static RecordSet newInlineInstance(Map params) {
        convert(params)
        checkKeys(RecordSet, params, [
            'Name', 'Type', 'TTL', 'ResourceRecords', 'SetIdentifier', 'Comment'
        ])
        require(RecordSet, ['Name', 'Type'], params)
        new RecordSet(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'Name': Name,
                'Type': Type
            ]
        ]
        if (HostedZoneId) map['Properties']['HostedZoneId'] = HostedZoneId
        if (HostedZoneName) map['Properties']['HostedZoneName'] = HostedZoneName
        if (TTL) map['Properties']['TTL'] = TTL
        if (ResourceRecords && !ResourceRecords.isEmpty()) map['Properties']['ResourceRecords'] = ResourceRecords
        if (SetIdentifier) map['Properties']['SetIdentifier'] = SetIdentifier
        if (Comment) map['Properties']['Comment'] = Comment
        if (AliasTarget) map['Properties']['AliasTarget'] = AliasTarget.toMap()
        map
    }

    def toInlineMap() {
        def map = [
            'Name': Name,
            'Type': Type
        ]
        if (TTL) map['TTL'] = TTL
        if (ResourceRecords && !ResourceRecords.isEmpty()) map['ResourceRecords'] = ResourceRecords
        if (SetIdentifier) map['SetIdentifier'] = SetIdentifier
        if (Comment) map['Comment'] = Comment
        if (AliasTarget) map['AliasTarget'] = AliasTarget.toMap()
        map
    }

    @Canonical
    static class AliasTarget {
        def HostedZoneId
        def DNSName

        static AliasTarget newInstance(Map params) {
            convert(params)
            new AliasTarget(params)
        }

        def toMap() {
            [
                HostedZoneId: HostedZoneId,
                DNSName     : DNSName
            ]
        }

    }
}
