package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/09/11.
 */
@Canonical
class CacheSubnetGroup {
    def id
    def name
    def Description
    def SubnetNames
    def SubnetIds

    def CacheSubnetGroup() {
    }

    def CacheSubnetGroup(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.Description = source.value('Description')
        if (source.containsKey('SubnetNames')) {
            SubnetNames = source.camelCaseList('SubnetNames')
        } else {
            SubnetIds = source.list('SubnetIds')
        }
    }

    def toResourceMap() {
        def map = [
            (this.id): [
                'Type': 'AWS::ElastiCache::SubnetGroup',
                'Properties': [
                    'Description': description,
                ]
            ]
        ]
        if (SubnetNames) {
            map[this.id]['Properties']['SubnetIds'] = SubnetNames.collect { ['Ref': it] }
        } else {
            map[this.id]['Properties']['SubnetIds'] = SubnetIds
        }
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
                result << new CacheSubnetGroup(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, CacheSubnetGroup r -> o << r.toResourceMap() })
    }

}
