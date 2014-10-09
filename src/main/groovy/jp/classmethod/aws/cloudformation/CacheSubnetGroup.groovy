package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/09/11.
 */
@Canonical
class CacheSubnetGroup {
    def id
    def name
    def description
    def subnetIds = []

    def CacheSubnetGroup() {
    }

    def CacheSubnetGroup(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.description = source.value('Description')
        this.subnetIds = source.camelCaseList('SubnetNames')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::ElastiCache::SubnetGroup',
                'Properties': [
                    'Description': description,
                    'SubnetIds': subnetIds.collect { ['Ref': it] }
                ]
            ]
        ]
    }

    static def load(file) {
        if (!file.exists()) return []
        def result = []
        def meta
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                result << new CacheSubnetGroup(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, CacheSubnetGroup r -> o << r.toResourceMap() })
    }

}
