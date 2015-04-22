package jp.classmethod.aws.cloudformation.route53

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * Created by watanabeshuji on 2015/04/22.
 */
@Canonical
class HostedZone extends Resource {

    static final def TYPE = 'AWS::Route53::HostedZone'
    static def DESC = '''\
AWS::Route53::HostedZone
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-route53-hostedzone.html

[Required Params]
- id
- Name

[Optional Params]

[Sample]
resources {
    hostedZone id: "ExampleComHostedZone", Name: "example.com"
}
'''
    def id
    def Name
    def HostedZoneConfig

    static HostedZone newInstance(Map params) {
        convert(params)
        checkKeys(HostedZone, params, ['id', 'Name'])
        logicalId(HostedZone, params)
        require(HostedZone, ['Name'], params)
        new HostedZone(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'Name': Name
            ]
        ]
        if (HostedZoneConfig) map['Properties']['HostedZoneConfig'] = HostedZoneConfig.toMap()
        map
    }

    @Canonical
    static class HostedZoneConfig {
        def Comment

        def toMap() {
            def map = [
                'Comment': Comment
            ]
            map
        }
    }
}
