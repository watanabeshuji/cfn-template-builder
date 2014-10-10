package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/10/09.
 */
@Canonical
class ElasticLoadBalancing {

    def id
    def name
    def Subnets
    def Listeners = []
    def SecurityGroups
    def Instances
    def Target
    def Timeout
    def Interval
    def UnhealthyThreshold
    def HealthyThreshold

    def ElasticLoadBalancing() {
    }

    def ElasticLoadBalancing(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.Subnets = source.list('Subnets')
        this.SecurityGroups = source.list('SecurityGroups')
        this.Instances = source.list('Instances')
        this.Target = "${source.value('TargetProtocol')}:${source.value('TargetPort')}${source.value('TargetPath')}"
        this.Timeout = source.value('Timeout')
        this.Interval = source.value('Interval')
        this.UnhealthyThreshold = source.value('UnhealthyThreshold')
        this.HealthyThreshold = source.value('HealthyThreshold')
    }

    def toResourceMap() {
        def map = [
            (this.id): [
                'Type': 'AWS::ElasticLoadBalancing::LoadBalancer',
                'Properties': [
                    'LoadBalancerName': 'elb',
                    'Subnets': this.Subnets,
                    'Listeners': this.Listeners,
                    'SecurityGroups': this.SecurityGroups,
                    'Instances': this.Instances,
                    'HealthCheck': [
                        Target: this.Target,
                        Timeout: this.Timeout,
                        Interval: this.Interval,
                        UnhealthyThreshold: this.UnhealthyThreshold,
                        HealthyThreshold: this.HealthyThreshold,
                    ],
                ]
            ]
        ]
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
                def elb = new ElasticLoadBalancing(meta.newSource(line.split(',')))
                elb.Listeners += loadListeners(file, elb.name)
                result << elb
            }
        }
        result
    }

    static def loadListeners(File baseFile, name) {
        def path = baseFile.absolutePath
        path = path.substring(0, path.length() - 4) // .csv
        def meta
        def result = []
        new File("${path}_${name}_Listeners.csv").eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                result << ElasticLoadBalancing.listener(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, ElasticLoadBalancing r -> o << r.toResourceMap() })
    }

    static def listener(Source source) {
        def map = [
            'Protocol': source.value('Protocol'),
            'LoadBalancerPort': source.value('LoadBalancerPort'),
            'InstanceProtocol': source.value('InstanceProtocol'),
            'InstancePort': source.value('InstancePort')
        ]
        if (source.value('SSLCertificateId')) {
            map['SSLCertificateId'] = source.value('SSLCertificateId')
        }
        map
    }
}
