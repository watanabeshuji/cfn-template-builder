package jp.classmethod.aws.cloudformation.elasticloadbalancing

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * Created by watanabeshuji on 2014/10/09.
 */
@Canonical
class ElasticLoadBalancing extends Resource {

    def id
    def LoadBalancerName
    def Subnets
    def Listeners = []
    def SecurityGroups
    def Instances
    def HealthCheck
    def AccessLoggingPolicy

    def ElasticLoadBalancing() {
    }

    def toResourceMap() {
        def map = [
            'Type'      : 'AWS::ElasticLoadBalancing::LoadBalancer',
            'Properties': [
                'LoadBalancerName': this.LoadBalancerName,
                'Subnets'         : this.Subnets,
                'SecurityGroups'  : this.SecurityGroups,
                'Instances'       : this.Instances,
                'Listeners'       : this.Listeners.collect { it.toResourceMap() }
            ]
        ]
        if (this.HealthCheck) map['Properties']['HealthCheck'] = HealthCheck.toResourceMap()
        if (this.AccessLoggingPolicy) map['Properties']['AccessLoggingPolicy'] = AccessLoggingPolicy.toResourceMap()
        map
    }

    @Canonical
    static class Listener {
        def Protocol
        def LoadBalancerPort
        def InstanceProtocol
        def InstancePort
        def SSLCertificateId

        def toResourceMap() {
            def map = [
                Protocol        : this.Protocol,
                LoadBalancerPort: this.LoadBalancerPort,
                InstanceProtocol: this.InstanceProtocol,
                InstancePort    : this.InstancePort,
            ]
            if (SSLCertificateId) map['SSLCertificateId'] = SSLCertificateId
            map
        }
    }

    @Canonical
    static class HealthCheck {
        def Target
        def Timeout
        def Interval
        def UnhealthyThreshold
        def HealthyThreshold

        def toResourceMap() {
            [
                Target            : this.Target,
                Timeout           : this.Timeout,
                Interval          : this.Interval,
                UnhealthyThreshold: this.UnhealthyThreshold,
                HealthyThreshold  : this.HealthyThreshold,
            ]
        }
    }

    @Canonical
    static class AccessLoggingPolicy {
        def Enabled
        def S3BucketName
        def S3BucketPrefix
        def EmitInterval

        def toResourceMap() {
            def map = [
                Enabled       : this.Enabled,
                S3BucketName  : this.S3BucketName,
                S3BucketPrefix: this.S3BucketPrefix,
                EmitInterval  : this.EmitInterval
            ]
            map
        }

    }

}
