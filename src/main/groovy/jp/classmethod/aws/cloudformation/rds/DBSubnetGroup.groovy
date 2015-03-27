package jp.classmethod.aws.cloudformation.rds

import groovy.transform.Canonical

/**
 * AWS::RDS::DBSubnetGroup
 * Created by watanabeshuji on 2014/09/10.
 */
@Canonical
class DBSubnetGroup {

    def id
    def DBSubnetGroupDescription
    def SubnetIds

    def DBSubnetGroup() {
    }

    def toResourceMap() {
        [
            'Type': 'AWS::RDS::DBSubnetGroup',
            'Properties': [
                'DBSubnetGroupDescription': DBSubnetGroupDescription,
                'SubnetIds': SubnetIds,
                'Tags': [
                    ['Key': 'Name', 'Value': id],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                ]
            ]
        ]
    }

}
