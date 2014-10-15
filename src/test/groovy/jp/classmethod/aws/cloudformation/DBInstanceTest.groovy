package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/09/11.
 */
class DBInstanceTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("DBInstanceTest_default.csv").getFile())
        def actual = DBInstance.load(input)
        assert actual == [
                new DBInstance(
                        id: 'DbDev',
                        Name: 'db-dev',
                        DBSubnetGroupName: 'DbSubnetDev',
                        MultiAZ: false,
                        AvailabilityZone: 'ap-northeast-1a',
                        DBInstanceClass: 'db.m1.small',
                        AllocatedStorage: '50',
                        Iops: null,
                        Engine: 'mysql',
                        EngineVersion: '5.6.19',
                        Port: '3306',
                        DBParameterGroupName: 'default.mysql5.6',
                        DBName: 'app',
                        MasterUsername: 'root',
                        MasterUserPassword: 'password1234',
                        VPCSecurityGroups: ['Internal'],
                        DependsOn: []
                ),
                new DBInstance(
                        id: 'DbPrd',
                        Name: 'db-prd',
                        DBSubnetGroupName: 'DbSubnet',
                        MultiAZ: true,
                        AvailabilityZone: null,
                        DBInstanceClass: 'db.m3.xlarge',
                        AllocatedStorage: '200',
                        Iops: 1000,
                        Engine: 'mysql',
                        EngineVersion: '5.6.19',
                        Port: '3306',
                        DBParameterGroupName: 'default.mysql5.6',
                        DBName: 'app',
                        MasterUsername: 'root',
                        MasterUserPassword: 'password1234',
                        VPCSecurityGroups: ['Internal'],
                        DependsOn: []
                )
        ]
    }

    @Test
    void "dev toResourceMap"() {
        def sut = new DBInstance(
                id: 'DbDev',
                Name: 'db-dev',
                DBSubnetGroupName: ['Ref': 'DbSubnetDev'],
                MultiAZ: false,
                AvailabilityZone: 'ap-northeast-1a',
                DBInstanceClass: 'db.m1.small',
                AllocatedStorage: '50',
                Iops: null,
                Engine: 'mysql',
                EngineVersion: '5.6.19',
                Port: '3306',
                DBParameterGroupName: 'default.mysql5.6',
                DBName: 'app',
                MasterUsername: 'root',
                MasterUserPassword: 'password1234',
                VPCSecurityGroups: ['Internal'],
                DependsOn: []
        )
        def expected = [
            "DbDev": [
                'Type': 'AWS::RDS::DBInstance',
                'Properties': [
                    'DBSubnetGroupName': ['Ref': 'DbSubnetDev'],
                    'MultiAZ': false,
                    'AvailabilityZone': 'ap-northeast-1a',
                    'DBInstanceClass': 'db.m1.small',
                    'AllocatedStorage': '50',
                    'Engine': 'mysql',
                    'EngineVersion': '5.6.19',
                    'Port': '3306',
                    'DBParameterGroupName': 'default.mysql5.6',
                    'DBName': 'app',
                    'MasterUsername': 'root',
                    'MasterUserPassword': 'password1234',
                    'VPCSecurityGroups': [['Ref': 'Internal']],
                    'Tags': [
                        ['Key': 'Name', 'Value': 'db-dev'],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "prd toResourceMap"() {
        def sut = new DBInstance(
                id: 'DbPrd',
                Name: 'db-prd',
                DBSubnetGroupName: ['Ref': 'DbSubnet'],
                MultiAZ: true,
                AvailabilityZone: null,
                DBInstanceClass: 'db.m3.xlarge',
                AllocatedStorage: '200',
                Iops: 1000,
                Engine: 'mysql',
                EngineVersion: '5.6.19',
                Port: '3306',
                DBParameterGroupName: 'default.mysql5.6',
                DBName: 'app',
                MasterUsername: 'root',
                MasterUserPassword: 'password1234',
                VPCSecurityGroups: ['Internal'],
                DependsOn: []
        )
        def expected = [
            "DbPrd": [
                'Type': 'AWS::RDS::DBInstance',
                'Properties': [
                    'DBSubnetGroupName': ['Ref': 'DbSubnet'],
                    'MultiAZ': true,
                    'DBInstanceClass': 'db.m3.xlarge',
                    'AllocatedStorage': '200',
                    'Ipos': 1000,
                    'Engine': 'mysql',
                    'EngineVersion': '5.6.19',
                    'Port': '3306',
                    'DBParameterGroupName': 'default.mysql5.6',
                    'DBName': 'app',
                    'MasterUsername': 'root',
                    'MasterUserPassword': 'password1234',
                    'VPCSecurityGroups': [['Ref': 'Internal']],
                    'Tags': [
                        ['Key': 'Name', 'Value': 'db-prd'],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
