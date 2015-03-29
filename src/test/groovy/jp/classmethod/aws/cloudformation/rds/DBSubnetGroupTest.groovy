package jp.classmethod.aws.cloudformation.rds

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/09/11.
 */
class DBSubnetGroupTest {


    @Test
    void "load dbSubnetGroup.groovy"() {
        Path input = getPath("/templates/resources/dbSubnetGroup.groovy")
        def actual = DBSubnetGroup.load(input)
        def expected = [
            new DBSubnetGroup(id: 'DBSubnetGroup', DBSubnetGroupDescription: "DB subnet group", SubnetIds: ["Ref:PrivateA", "Ref:PrivateA"])
        ]
        assert actual == expected
    }

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
