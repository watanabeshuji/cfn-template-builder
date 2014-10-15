package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/09/11.
 */
class DBSubnetGroupTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("DBSubnetGroupTest_default.csv").getFile())
        def actual = DBSubnetGroup.load(input)
        assert actual == [
                new DBSubnetGroup(id: 'DbSubnet',    Name: 'db-subnet',     DBSubnetGroupDescription:'DB Subnet',                 SubnetNames: ['SubnetA', 'SubnetC']),
                new DBSubnetGroup(id: 'DbSubnetDev', Name: 'db-subnet-dev', DBSubnetGroupDescription:'DB Subnet for development', SubnetNames: ['SubnetDevA', 'SubnetDevC'])
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new DBSubnetGroup(id: 'DbSubnet',    Name: 'db-subnet',  DBSubnetGroupDescription:'DB Subnet', SubnetNames: ['SubnetA', 'SubnetC'])
        def expected = [
            "DbSubnet": [
                'Type': 'AWS::RDS::DBSubnetGroup',
                'Properties': [
                    'DBSubnetGroupDescription': 'DB Subnet',
                    'SubnetIds': [['Ref': 'SubnetA'], ['Ref': 'SubnetC']],
                    'Tags': [
                        ['Key': 'Name', 'Value': 'db-subnet'],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
