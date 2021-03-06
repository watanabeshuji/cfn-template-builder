package jp.classmethod.aws.cloudformation.rds

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::RDS::DBSubnetGroup
 * Created by watanabeshuji on 2014/09/10.
 */
@Canonical
class DBSubnetGroup extends Resource {

    def id
    static final def TYPE = 'AWS::RDS::DBSubnetGroup'
    def DBSubnetGroupDescription
    def SubnetIds

    def DBSubnetGroup() {
    }

    def toResourceMap() {
        [
            'Type'      : TYPE,
            'Properties': [
                'DBSubnetGroupDescription': DBSubnetGroupDescription,
                'SubnetIds'               : SubnetIds,
                'Tags'                    : [
                    ['Key': 'Name', 'Value': id],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
    }

}
