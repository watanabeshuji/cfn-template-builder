package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::EC2::SubnetRouteTableAssociation
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-subnet-route-table-assoc.html
 */
@Canonical
class SubnetRouteTableAssociation extends Resource {

    String id
    def SubnetId
    def RouteTableId

    def SubnetRouteTableAssociation() {
    }

    def toResourceMap() {
        [
            'Type'      : 'AWS::EC2::SubnetRouteTableAssociation',
            'Properties': [
                'SubnetId'    : SubnetId,
                'RouteTableId': RouteTableId
            ]
        ]
    }

}