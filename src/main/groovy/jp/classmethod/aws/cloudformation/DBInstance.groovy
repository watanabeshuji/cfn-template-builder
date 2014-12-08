package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/09/10.
 */
@Canonical
class DBInstance {

    def id
    def Name
    def DBSubnetGroupName
    def MultiAZ
    def AvailabilityZone
    def DBInstanceClass
    def AllocatedStorage
    def Iops
    def Engine
    def EngineVersion
    def Port
    def DBParameterGroupName
    def DBName
    def DBSnapshotIdentifier
    def MasterUsername
    def MasterUserPassword
    def VPCSecurityGroups
    def DependsOn

    def DBInstance() {
    }

    def DBInstance(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.DBSubnetGroupName = source.camelCase('DBSubnetGroupName')
        this.MultiAZ = source.bool('MultiAZ')
        this.AvailabilityZone = source.value('AvailabilityZone')
        this.DBInstanceClass = source.value('DBInstanceClass')
        this.AllocatedStorage = source.value('AllocatedStorage')
        this.Iops = source.integer('Iops')
        this.Engine = source.value('Engine')
        this.EngineVersion = source.value('EngineVersion')
        this.Port = source.value('Port')
        this.DBParameterGroupName = source.value('DBParameterGroupName')
        this.DBName = source.value('DBName')
        this.DBSnapshotIdentifier = source.value('DBSnapshotIdentifier')
        this.MasterUsername = source.value('MasterUsername')
        this.MasterUserPassword = source.value('MasterUserPassword')
        this.VPCSecurityGroups = source.camelCaseList('VPCSecurityGroups')
        this.DependsOn = source.camelCaseList('DependsOn')
    }


    def toResourceMap() {
        def map = [
            (this.id): [
                'Type': 'AWS::RDS::DBInstance',
                'Properties': [
                    'DBSubnetGroupName': Util.ref(DBSubnetGroupName),
                    'MultiAZ': MultiAZ,
                    'DBInstanceClass': DBInstanceClass,
                    'AllocatedStorage': AllocatedStorage,
                    'Engine': Engine,
                    'EngineVersion': EngineVersion,
                    'Port': Port,
                    'DBParameterGroupName': DBParameterGroupName,
                    'DBName': DBName,
                    'MasterUsername': MasterUsername,
                    'MasterUserPassword': MasterUserPassword,
                    'VPCSecurityGroups': VPCSecurityGroups.collect { Util.ref(it) },
                    'Tags': [
                            ['Key': 'Name', 'Value': Name],
                            ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        if (!this.MultiAZ) map[this.id]['Properties']['AvailabilityZone'] = AvailabilityZone
        if (this.Iops) map[this.id]['Properties']['Ipos'] = Iops
        if (this.DBSnapshotIdentifier) map[this.id]['Properties']['DBSnapshotIdentifier'] = DBSnapshotIdentifier
        if (!DependsOn.isEmpty()) map[this.id]['DependsOn'] = DependsOn.collect { it }
        map
    }

    static def load(file) {
        if (!file.exists()) return []
        def result = []
        def meta
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                result << new DBInstance(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, DBInstance r -> o << r.toResourceMap() })
    }

}
