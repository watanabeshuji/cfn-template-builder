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
                new DBSubnetGroup(id: 'DbSubnet',    name: 'db-subnet',     dBSubnetGroupDescription:'DB Subnet',                 subnetIds: ['SubnetA', 'SubnetC']),
                new DBSubnetGroup(id: 'DbSubnetDev', name: 'db-subnet-dev', dBSubnetGroupDescription:'DB Subnet for development', subnetIds: ['SubnetDevA', 'SubnetDevC'])
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new DBSubnetGroup(id: 'DbSubnet',    name: 'db-subnet',     dBSubnetGroupDescription:'DB Subnet', subnetIds: ['SubnetA', 'SubnetC'])
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
