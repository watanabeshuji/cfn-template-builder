package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::EC2::RouteTable
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-route-table.html
 */
@Canonical
class RouteTable extends Resource {

    final def Type = 'AWS::EC2::RouteTable'
    String id
    def VpcId
    def Tags = [:]

    def RouteTable() {
    }

    def toResourceMap() {
        def map = [
            'Type'      : Type,
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