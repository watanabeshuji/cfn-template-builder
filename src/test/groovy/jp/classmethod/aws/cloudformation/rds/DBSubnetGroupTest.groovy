package jp.classmethod.aws.cloudformation.rds

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/09/11.
 */
class DBSubnetGroupTest {


    @Test
    void "toResourceMap"() {
        def sut = new DBSubnetGroup(id: 'DbSubnet', DBSubnetGroupDescription: 'DB Subnet', SubnetIds: [['Ref': 'SubnetA'], ['Ref': 'SubnetC']])
        def expected = [
            'Type'      : 'AWS::RDS::DBSubnetGroup',
            'Properties': [
                'DBSubnetGroupDescription': 'DB Subnet',
                'SubnetIds'               : [['Ref': 'SubnetA'], ['Ref': 'SubnetC']],
                'Tags'                    : [
                    ['Key': 'Name', 'Value': 'DbSubnet'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
