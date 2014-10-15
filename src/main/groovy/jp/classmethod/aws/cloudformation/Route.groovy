package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class Route {
    def id
    def Name
    def RouteTable
    def DestinationCidrBlock
    def Gateway
    def Instance

    def Route() {}

    def Route(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.RouteTable = source.camelCase('RouteTable')
        this.DestinationCidrBlock = source.value('DestinationCidrBlock')
        this.Gateway = source.camelCase('Gateway')
        this.Instance = source.camelCase('Instance')
    }

    def toResourceMap() {
        def obj = [
            'Type': 'AWS::EC2::Route',
            'Properties': [
                'RouteTableId': Util.ref(RouteTable),
                'DestinationCidrBlock': DestinationCidrBlock
            ]
        ]
        if (Gateway) {
            obj['DependsOn'] = "Attach$Gateway"
            obj['Properties']['GatewayId'] = Util.ref(Gateway)
        }
        if (Instance) {
            obj['DependsOn'] = Instance
            obj['Properties']['InstanceId'] = Util.ref(Instance)
        }
        [ (this.id): obj]
    }

    static def load(File file) {
        Util.load(file, {new Route(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Route r -> o << r.toResourceMap() })
    }

}