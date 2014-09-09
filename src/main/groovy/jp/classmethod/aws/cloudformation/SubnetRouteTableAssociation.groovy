package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class SubnetRouteTableAssociation {
    def id
    def name
    def subnetId
    def routeTableId

    def SubnetRouteTableAssociation() {
    }

    def SubnetRouteTableAssociation(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.subnetId = source.camelCase('Subnet')
        this.routeTableId = source.camelCase('RouteTable')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::SubnetRouteTableAssociation',
                'Properties': [
                    'SubnetId': ['Ref': subnetId],
                    'RouteTableId': ['Ref': routeTableId]
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
                result << new SubnetRouteTableAssociation(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, SubnetRouteTableAssociation r -> o << r.toResourceMap() } )
    }

}