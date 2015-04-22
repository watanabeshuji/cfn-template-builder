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
    hostedZone id: "ExampleComHostedZone", Name: "example.com"
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

    static RecordSet newInstance(Map params) {
        convert(params)
        checkKeys(HostedZone, params, [
            'id', 'Name', 'Type', 'HostedZoneId', 'HostedZoneName', 'TTL', 'ResourceRecords', 'SetIdentifier', 'Comment'
        ])
        logicalId(HostedZone, params)
        require(HostedZone, ['Name', 'Type'], params)
        requireOneOf(HostedZone, ['HostedZoneId', 'HostedZoneName'], params)
        new RecordSet(params).withRefIds(params)
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
        map
    }

}
