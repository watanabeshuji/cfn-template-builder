package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/10/09.
 */
@Canonical
class ElasticLoadBalancing {

    def id
    def name
    def LoadBalancerName
    def Subnets
    def Listeners = []
    def SecurityGroups
    def Instances
    def Target
    def Timeout
    def Interval
    def UnhealthyThreshold
    def HealthyThreshold
    def AccessLoggingPolicyEnabled
    def AccessLoggingPolicyS3BucketName
    def AccessLoggingPolicyS3BucketPrefix
    def AccessLoggingPolicyEmitInterval

    def ElasticLoadBalancing() {
    }

    def ElasticLoadBalancing(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.LoadBalancerName = source.value('LoadBalancerName')
        this.Subnets = source.list('Subnets')
        this.SecurityGroups = source.list('SecurityGroups')
        this.Instances = source.list('Instances')
        this.Target = "${source.value('TargetProtocol')}:${source.value('TargetPort')}${source.value('TargetPath')}"
        this.Timeout = source.value('Timeout')
        this.Interval = source.value('Interval')
        this.UnhealthyThreshold = source.value('UnhealthyThreshold')
        this.HealthyThreshold = source.value('HealthyThreshold')
        this.AccessLoggingPolicyEnabled = source.value('AccessLoggingPolicyEnabled')
        this.AccessLoggingPolicyS3BucketName = source.value('AccessLoggingPolicyS3BucketName')
        this.AccessLoggingPolicyS3BucketPrefix = source.value('AccessLoggingPolicyS3BucketPrefix')
        this.AccessLoggingPolicyEmitInterval = source.value('AccessLoggingPolicyEmitInterval')
    }

    def toResourceMap() {
        def map = [
            (this.id): [
                'Type': 'AWS::ElasticLoadBalancing::LoadBalancer',
                'Properties': [
                    'LoadBalancerName': this.LoadBalancerName,
                    'Subnets': this.Subnets.collect { Util.ref(it) },
                    'Listeners': this.Listeners,
                    'SecurityGroups': this.SecurityGroups.collect { Util.ref(it) },
                    'Instances': this.Instances.collect { Util.ref(it) },
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
        if (this.AccessLoggingPolicyEnabled) {
            map[this.id]['Properties']['AccessLoggingPolicy'] = [
                    'Enabled': this.AccessLoggingPolicyEnabled,
                    'S3BucketName': this.AccessLoggingPolicyS3BucketName,
                    'S3BucketPrefix': this.AccessLoggingPolicyS3BucketPrefix,
                    'EmitInterval': this.AccessLoggingPolicyEmitInterval,
            ]
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
                def elb = new ElasticLoadBalancing(meta.newSource(line.split(',')))
                elb.Listeners += loadListeners(file, elb.name)
                result << elb
            }
        }
        result
    }

    static def loadListeners(File baseFile, name) {
        def meta
        def result = []
        new File(Util.associatedFile(baseFile.absolutePath, "${name}_Listeners")).eachLine { line, num ->
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
