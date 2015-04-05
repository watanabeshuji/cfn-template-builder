package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * AWS::EC2::SubnetRouteTableAssociation
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-subnet-route-table-assoc.html
 */
@Canonical
class SubnetRouteTableAssociation extends Resource {

    static final def TYPE = 'AWS::EC2::SubnetRouteTableAssociation'
    static def DESC = '''\
AWS::EC2::SubnetRouteTableAssociation
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-subnet-route-table-assoc.html

[Required Params]
- id
- SubnetId
- RouteTableId

[Optional Params]

[Sample]
resources {
    subnetRouteTableAssociation id: "SubnetRouteTableAssociation", SubnetId: [Ref: 'Subnet'], RouteTableId: [Ref: 'RouteTable']
}
'''
    String id
    def SubnetId
    def RouteTableId

    static SubnetRouteTableAssociation newInstance(Map params) {
        convert(params)
        checkKeys(SubnetRouteTableAssociation, params, ['id', 'SubnetId', 'RouteTableId'])
        logicalId(SubnetRouteTableAssociation, params)
        require(SubnetRouteTableAssociation, ['SubnetId', 'RouteTableId'], params)
        new SubnetRouteTableAssociation(params).withRefIds(params)
    }

    def toResourceMap() {
        [
            'Type'      : TYPE,
            'Properties': [
                'SubnetId'    : SubnetId,
                'RouteTableId': RouteTableId
            ]
        ]
    }

}