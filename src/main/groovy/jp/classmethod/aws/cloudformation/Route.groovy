package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class Route {
    def id
    def name
    def routeTableId
    def cidrBlock
    def gatewayId
    def instanceId

    def Route() {}

    def Route(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.routeTableId = source.camelCase('RouteTable')
        this.cidrBlock = source.value('DestinationCidrBlock')
        this.gatewayId = source.camelCase('Gateway')
        this.instanceId = source.camelCase('Instance')
    }

    def toResourceMap() {
        def obj = [
            'Type': 'AWS::EC2::Route',
            'Properties': [
                'RouteTableId': ['Ref': routeTableId],
                'DestinationCidrBlock': cidrBlock
            ]
        ]
        if (gatewayId != null) {
            obj['DependsOn'] = "Attach$gatewayId"
            obj['Properties']['GatewayId'] = ['Ref': gatewayId]
        }
        if (instanceId != null) {
            obj['DependsOn'] = instanceId
            obj['Properties']['InstanceId'] = ["Ref": instanceId]
        }
        [ (this.id): obj]
    }

    static def load(file) {
        if (!file.exists()) return []
        def result = []
        def meta
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                result << new Route(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Route r -> o << r.toResourceMap() })
    }

}