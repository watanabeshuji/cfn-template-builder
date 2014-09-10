package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/09/10.
 */
@Canonical
class DBSubnetGroup {

    def id
    def name
    def dBSubnetGroupDescription
    def subnetIds = []

    def DBSubnetGroup() {
    }

    def DBSubnetGroup(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.dBSubnetGroupDescription = source.value('DBSubnetGroupDescription')
        this.subnetIds = source.camelCaseList('SubnetNames')
    }


    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::RDS::DBSubnetGroup',
                'Properties': [
                    'DBSubnetGroupDescription': dBSubnetGroupDescription,
                    'SubnetIds': subnetIds.collect { ['Ref': it] },
                    'Tags': [
                        ['Key': 'Name', 'Value': name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
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
                result << new DBSubnetGroup(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, DBSubnetGroup r -> o << r.toResourceMap() })
    }

}
