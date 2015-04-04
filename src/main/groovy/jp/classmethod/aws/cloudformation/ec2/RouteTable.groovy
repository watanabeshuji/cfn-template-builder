package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.checkKeys
import static jp.classmethod.aws.cloudformation.util.Valid.logicalId
import static jp.classmethod.aws.cloudformation.util.Valid.require

/**
 * AWS::EC2::RouteTable
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-route-table.html
 */
@Canonical
class RouteTable extends Resource {

    static final def TYPE = 'AWS::EC2::RouteTable'
    static def DESC = '''\
AWS::EC2::RouteTable
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-route-table.html

[Required Params]
- id
- VpcId

[Optional Params]
- Tags

[Sample]
resources {
    routeTable id: "PublicRouteTable", VpcId: [Ref: "vpc"]
}
'''
    String id
    def VpcId
    def Tags = [:]

    static RouteTable newInstance(Map params) {
        convert(params)
        checkKeys(RouteTable, params, ['id', 'VpcId', 'Tags'])
        logicalId(RouteTable, params)
        require(RouteTable, 'VpcId', params)
        new RouteTable(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'VpcId': VpcId,
                'Tags' : []
            ]
        ]
        Tags.each {key, value ->
            map['Properties']['Tags'] << ['Key': key, 'Value': value]
        }
        if (Tags['Name'] == null) map['Properties']['Tags'] << ['Key': 'Name', 'Value': id]
        if (Tags['Application'] == null) map['Properties']['Tags'] << ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
        map
    }

}