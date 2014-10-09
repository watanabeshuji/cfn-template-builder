package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/09/10.
 */
@Canonical
class DBInstance {

    def id
    def name
    def dBSubnetGroupName
    def multiAZ
    def availabilityZone
    def dBInstanceClass
    def allocatedStorage
    def iops
    def engine
    def engineVersion
    def port
    def dBParameterGroupName
    def dBName
    def masterUsername
    def masterUserPassword
    def vPCSecurityGroups
    def dependsOn

    def DBInstance() {
    }

    def DBInstance(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.dBSubnetGroupName = source.value('DBSubnetGroupName')
        this.multiAZ = source.bool('MultiAZ')
        this.availabilityZone = source.value('AvailabilityZone')
        this.dBInstanceClass = source.value('DBInstanceClass')
        this.allocatedStorage = source.value('AllocatedStorage')
        this.iops = source.integer('Iops')
        this.engine = source.value('Engine')
        this.engineVersion = source.value('EngineVersion')
        this.port = source.value('Port')
        this.dBParameterGroupName = source.value('DBParameterGroupName')
        this.dBName = source.value('DBName')
        this.masterUsername = source.value('MasterUsername')
        this.masterUserPassword = source.value('MasterUserPassword')
        this.vPCSecurityGroups = source.camelCaseList('VPCSecurityGroupNames')
        this.dependsOn = source.camelCaseList('DependsOn')
    }


    def toResourceMap() {
        def map = [
            (this.id): [
                'Type': 'AWS::RDS::DBInstance',
                'Properties': [
                    'DBSubnetGroupName': ['Ref': dBSubnetGroupName],
                    'MultiAZ': multiAZ,
                    'DBInstanceClass': dBInstanceClass,
                    'AllocatedStorage': allocatedStorage,
                    'Engine': engine,
                    'EngineVersion': engineVersion,
                    'Port': port,
                    'DBParameterGroupName': dBParameterGroupName,
                    'DBName': dBName,
                    'MasterUsername': masterUsername,
                    'MasterUserPassword': masterUserPassword,
                    'VPCSecurityGroups': vPCSecurityGroups.collect { ['Ref': it] },
                    'Tags': [
                            ['Key': 'Name', 'Value': name],
                            ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        if (!this.multiAZ) map[this.id]['Properties']['AvailabilityZone'] = availabilityZone
        if (this.iops) map[this.id]['Properties']['Ipos'] = iops
        if (!dependsOn.isEmpty()) map[this.id]['DependsOn'] = dependsOn.collect { it }
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
