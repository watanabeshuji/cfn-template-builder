package jp.classmethod.aws.cloudformation

import groovy.transform.ToString

/**
 * Created by watanabeshuji on 2014/08/14.
 */
@ToString
class CacheCluster {
    def id
    def name
    def cacheNodeType
    def numCacheNodes
    def engine
    def engineVersion
    def autoMinorVersionUpgrade
    def preferredAvailabilityZone
    def cacheSubnetGroupName
    def vpcSecurityGroupIds

    def CacheCluster(source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.cacheNodeType = source.value('CacheNodeType')
        this.numCacheNodes = source.value('NumCacheNodes')
        this.engine = source.value('Engine')
        this.engineVersion = source.value('EngineVersion')
        this.autoMinorVersionUpgrade = source.bool('AutoMinorVersionUpgrade')
        this.preferredAvailabilityZone = source.value('PreferredAvailabilityZone')
        this.cacheSubnetGroupName = source.value('CacheSubnetGroupName')
        this.vpcSecurityGroupIds = source.camelCaseList('VpcSecurityGroupIds')
    }

    static def load(File file) {
        def result = []
        def meta
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                result << new CacheCluster(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, CacheCluster r ->
            o << [ "$r.id": [
                    'Type': 'AWS::ElastiCache::CacheCluster',
                    'Properties': [
                            'CacheNodeType': r.cacheNodeType,
                            'NumCacheNodes': r.numCacheNodes,
                            'Engine': r.engine,
                            'EngineVersion': r.engineVersion,
                            'AutoMinorVersionUpgrade': r.autoMinorVersionUpgrade,
                            'PreferredAvailabilityZone': r.preferredAvailabilityZone,
                            'CacheSubnetGroupName': r.cacheSubnetGroupName,
                            'VpcSecurityGroupIds': r.vpcSecurityGroupIds
                    ]
            ]]
        })
    }

}
