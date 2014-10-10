package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/08/14.
 */
@Canonical
class CacheCluster {
    def id
    def name
    def cacheNodeType
    def numCacheNodes
    def engine
    def engineVersion
    def autoMinorVersionUpgrade
    def cacheParameterGroupName
    def preferredAvailabilityZone
    def cacheSubnetGroupName
    def vpcSecurityGroupIds

    def CacheCluster() {
    }

    def CacheCluster(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.cacheNodeType = source.value('CacheNodeType')
        this.numCacheNodes = source.value('NumCacheNodes')
        this.engine = source.value('Engine')
        this.engineVersion = source.value('EngineVersion')
        this.autoMinorVersionUpgrade = source.bool('AutoMinorVersionUpgrade')
        this.preferredAvailabilityZone = source.value('PreferredAvailabilityZone')
        this.cacheParameterGroupName = source.value('CacheParameterGroupName')
        this.cacheSubnetGroupName = source.camelCase('CacheSubnetGroupName')
        this.vpcSecurityGroupIds = source.camelCaseList('VpcSecurityGroups')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::ElastiCache::CacheCluster',
                'Properties': [
                    'CacheNodeType': cacheNodeType,
                    'NumCacheNodes': numCacheNodes,
                    'Engine': engine,
                    'EngineVersion': engineVersion,
                    'AutoMinorVersionUpgrade': autoMinorVersionUpgrade,
                    'PreferredAvailabilityZone': preferredAvailabilityZone,
                    'CacheParameterGroupName': cacheParameterGroupName,
                    'CacheSubnetGroupName':  Util.ref(cacheSubnetGroupName),
                    'VpcSecurityGroupIds': vpcSecurityGroupIds.collect { Util.ref(it) },
                ]
            ]
        ]
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
        load(file).inject(resources, {o, CacheCluster r -> o << r.toResourceMap() })
    }

}
