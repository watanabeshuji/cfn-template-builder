package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class SubnetRouteTableAssociation {
    def id
    def Name
    def Subnet
    def RouteTable

    def SubnetRouteTableAssociation() {
    }

    def SubnetRouteTableAssociation(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.Subnet = source.camelCase('Subnet')
        this.RouteTable = source.camelCase('RouteTable')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::SubnetRouteTableAssociation',
                'Properties': [
                    'SubnetId': Util.ref(Subnet),
                    'RouteTableId': Util.ref(RouteTable)
                ]
            ]
        ]
    }

    static def load(File file) {
        Util.load(file, {new SubnetRouteTableAssociation(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, SubnetRouteTableAssociation r -> o << r.toResourceMap() } )
    }

}