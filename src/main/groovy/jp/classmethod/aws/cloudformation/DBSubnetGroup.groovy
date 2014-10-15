package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/09/10.
 */
@Canonical
class DBSubnetGroup {

    def id
    def Name
    def DBSubnetGroupDescription
    def SubnetNames = []

    def DBSubnetGroup() {
    }

    def DBSubnetGroup(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.DBSubnetGroupDescription = source.value('DBSubnetGroupDescription')
        this.SubnetNames = source.camelCaseList('SubnetNames')
    }


    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::RDS::DBSubnetGroup',
                'Properties': [
                    'DBSubnetGroupDescription': DBSubnetGroupDescription,
                    'SubnetIds': SubnetNames.collect { Util.ref(it) },
                    'Tags': [
                        ['Key': 'Name', 'Value': Name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
    }

    static def load(file) {
        Util.load(file, { new DBSubnetGroup(it) })
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, DBSubnetGroup r -> o << r.toResourceMap() })
    }

}
