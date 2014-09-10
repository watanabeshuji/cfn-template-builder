package jp.classmethod.aws.cloudformation

import groovy.json.JsonBuilder

/**
 * Created by watanabeshuji on 2014/08/13.
 */
class Template {

    def json
    def resources

    def Template(Meta meta, Mappings mappings, resources) {
        this.resources = resources
        json = new JsonBuilder(['AWSTemplateFormatVersion': '2010-09-09'])
        json {
            'AWSTemplateFormatVersion' '2010-09-09'
            'Description' meta['Description']
            'Mappings' mappings
            'Resources' resources
        }
    }

    def toPrettyString() {
        json.toPrettyString()
    }

    static def build(String dirName) {
        def meta = Meta.load(new File("$dirName/Meta.txt"))
        def mappings = Mappings.load(new File("$dirName/Mappings"))
        def resources = [:]
        File dir = new File(dirName)
        dir.listFiles({d,f -> f ==~ /\d+_VPC\.csv/ } as FilenameFilter).each { VPC.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_InternetGateway\.csv/ } as FilenameFilter).each { InternetGateway.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_Subnet\.csv/ } as FilenameFilter).each { Subnet.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_RouteTable\.csv/ } as FilenameFilter).each { RouteTable.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_Route\.csv/ } as FilenameFilter).each { Route.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_SubnetRouteTableAssociation\.csv/ } as FilenameFilter).each { SubnetRouteTableAssociation.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_SecurityGroup\.csv/ } as FilenameFilter).each { SecurityGroup.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_Volume\.csv/ } as FilenameFilter).each { Volume.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_Instance\.csv/ } as FilenameFilter).each { Instance.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_DBSubnetGroup\.csv/ } as FilenameFilter).each { DBSubnetGroup.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_DBInstance\.csv/ } as FilenameFilter).each { DBInstance.inject(resources, it) }
        dir.listFiles({d,f -> f ==~ /\d+_CacheCluster\.csv/ } as FilenameFilter).each { CacheCluster.inject(resources, it) }
        new Template(meta, mappings, resources)
    }

}
