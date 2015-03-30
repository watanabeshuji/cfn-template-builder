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

    def RouteTable() {
    }

    def toResourceMap() {
        [
            'Type'      : Type,
            'Properties': [
                'VpcId': VpcId,
                'Tags' : [
                    ['Key': 'Name', 'Value': id],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
    }

}