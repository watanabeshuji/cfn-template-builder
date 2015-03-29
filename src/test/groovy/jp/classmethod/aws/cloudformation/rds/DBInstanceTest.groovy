package jp.classmethod.aws.cloudformation.rds

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/09/11.
 */
class DBInstanceTest {


    @Test
    void "load dbInstance.groovy"() {
        Path input = getPath("/templates/resources/dbInstance.groovy")
        def actual = DBInstance.load(input)
        def expected = [
            new DBInstance(id: 'DbPrd', DBSubnetGroupName: [Ref: "DbSubnetGroup"], MultiAZ: true,
                DBInstanceClass: "db.m3.xlarge", AllocatedStorage: "200", Iops: 1000, Engine: "mysql", EngineVersion: "5.6.19", Port: "3306",
                DBParameterGroupName: "default.mysql5.6", DBName: "app", MasterUsername: "root", MasterUserPassword: "pass1234",
                VPCSecurityGroups: [[Ref: "Internal"]]),
            new DBInstance(id: "DbDev", DBSubnetGroupName: [Ref: "DbSubnetGroup"], MultiAZ: false, AvailabilityZone: "ap-northeast-1a",
                DBInstanceClass: "db.m1.small", AllocatedStorage: "50", Engine: "mysql", EngineVersion: "5.6.19", Port: "3306",
                DBParameterGroupName: "default.mysql5.6", DBName: "app", MasterUsername: "root", MasterUserPassword: "pass1234",
                VPCSecurityGroups: [[Ref: "Internal"]], DBSnapshotIdentifier: "snapshot01")
        ]
        assert actual == expected
    }

    @Test
    void "dev toResourceMap"() {
        def sut = new DBInstance(
            id: 'DbDev',
            DBSubnetGroupName: ['Ref': 'DbSubnetDev'],
            MultiAZ: false,
            AvailabilityZone: 'ap-northeast-1a',
            DBInstanceClass: 'db.m1.small',
            AllocatedStorage: '50',
            Engine: 'mysql',
            EngineVersion: '5.6.19',
            Port: '3306',
            DBParameterGroupName: 'default.mysql5.6',
            DBName: 'app',
            MasterUsername: 'root',
            MasterUserPassword: 'password1234',
            VPCSecurityGroups: [[Ref: 'Internal']]
        )
        def expected = [
            'Type'      : 'AWS::RDS::DBInstance',
            'Properties': [
                'DBSubnetGroupName'   : ['Ref': 'DbSubnetDev'],
                'MultiAZ'             : false,
                'AvailabilityZone'    : 'ap-northeast-1a',
                'DBInstanceClass'     : 'db.m1.small',
                'AllocatedStorage'    : '50',
                'Engine'              : 'mysql',
                'EngineVersion'       : '5.6.19',
                'Port'                : '3306',
                'DBParameterGroupName': 'default.mysql5.6',
                'DBName'              : 'app',
                'MasterUsername'      : 'root',
                'MasterUserPassword'  : 'password1234',
                'VPCSecurityGroups'   : [['Ref': 'Internal']],
                'Tags'                : [
                    ['Key': 'Name', 'Value': 'DbDev'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "prd toResourceMap"() {
        def sut = new DBInstance(
            id: 'DbPrd',
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
            VPCSecurityGroups: [[Ref: 'Internal']]
        )
        def expected = [
            'Type'      : 'AWS::RDS::DBInstance',
            'Properties': [
                'DBSubnetGroupName'   : ['Ref': 'DbSubnet'],
                'MultiAZ'             : true,
                'DBInstanceClass'     : 'db.m3.xlarge',
                'AllocatedStorage'    : '200',
                'Ipos'                : 1000,
                'Engine'              : 'mysql',
                'EngineVersion'       : '5.6.19',
                'Port'                : '3306',
                'DBParameterGroupName': 'default.mysql5.6',
                'DBName'              : 'app',
                'MasterUsername'      : 'root',
                'MasterUserPassword'  : 'password1234',
                'VPCSecurityGroups'   : [['Ref': 'Internal']],
                'Tags'                : [
                    ['Key': 'Name', 'Value': 'DbPrd'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "prd toResourceMap DBSnapshotIdentifier"() {
        def sut = new DBInstance(
            id: 'DbPrd',
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
            DBSnapshotIdentifier: 'snapshot01',
            MasterUsername: 'root',
            MasterUserPassword: 'password1234',
            VPCSecurityGroups: [[Ref: 'Internal']]
        )
        def expected = [
            'Type'      : 'AWS::RDS::DBInstance',
            'Properties': [
                'DBSubnetGroupName'   : ['Ref': 'DbSubnet'],
                'MultiAZ'             : true,
                'DBInstanceClass'     : 'db.m3.xlarge',
                'AllocatedStorage'    : '200',
                'Ipos'                : 1000,
                'Engine'              : 'mysql',
                'EngineVersion'       : '5.6.19',
                'Port'                : '3306',
                'DBParameterGroupName': 'default.mysql5.6',
                'DBName'              : 'app',
                'MasterUsername'      : 'root',
                'MasterUserPassword'  : 'password1234',
                'VPCSecurityGroups'   : [['Ref': 'Internal']],
                'Tags'                : [
                    ['Key': 'Name', 'Value': 'DbPrd'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ],
                'DBSnapshotIdentifier': 'snapshot01'
            ]
        ]
        assert sut.toResourceMap() == expected
    }
}
