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
                        name: 'db-dev',
                        multiAZ: false,
                        availabilityZone: 'ap-northeast-1a',
                        dBInstanceClass: 'db.m1.small',
                        allocatedStorage: '50',
                        iops: null,
                        engine: 'mysql',
                        engineVersion: '5.6.19',
                        port: '3306',
                        dBParameterGroupName: 'default.mysql5.6',
                        dBName: 'app',
                        masterUsername: 'root',
                        masterUserPassword: 'password1234',
                        vPCSecurityGroups: ['Internal']
                ),
                new DBInstance(
                        id: 'DbPrd',
                        name: 'db-prd',
                        multiAZ: true,
                        availabilityZone: null,
                        dBInstanceClass: 'db.m3.xlarge',
                        allocatedStorage: '200',
                        iops: 1000,
                        engine: 'mysql',
                        engineVersion: '5.6.19',
                        port: '3306',
                        dBParameterGroupName: 'default.mysql5.6',
                        dBName: 'app',
                        masterUsername: 'root',
                        masterUserPassword: 'password1234',
                        vPCSecurityGroups: ['Internal']
                )
        ]
    }

    @Test
    void "dev toResourceMap"() {
        def sut = new DBInstance(
                id: 'DbDev',
                name: 'db-dev',
                multiAZ: false,
                availabilityZone: 'ap-northeast-1a',
                dBInstanceClass: 'db.m1.small',
                allocatedStorage: '50',
                iops: null,
                engine: 'mysql',
                engineVersion: '5.6.19',
                port: '3306',
                dBParameterGroupName: 'default.mysql5.6',
                dBName: 'app',
                masterUsername: 'root',
                masterUserPassword: 'password1234',
                vPCSecurityGroups: ['Internal']
        )
        def expected = [
            "DbDev": [
                'Type': 'AWS::RDS::DBInstance',
                'Properties': [
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
                name: 'db-prd',
                multiAZ: true,
                availabilityZone: null,
                dBInstanceClass: 'db.m3.xlarge',
                allocatedStorage: '200',
                iops: 1000,
                engine: 'mysql',
                engineVersion: '5.6.19',
                port: '3306',
                dBParameterGroupName: 'default.mysql5.6',
                dBName: 'app',
                masterUsername: 'root',
                masterUserPassword: 'password1234',
                vPCSecurityGroups: ['Internal']
        )
        def expected = [
            "DbPrd": [
                'Type': 'AWS::RDS::DBInstance',
                'Properties': [
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
