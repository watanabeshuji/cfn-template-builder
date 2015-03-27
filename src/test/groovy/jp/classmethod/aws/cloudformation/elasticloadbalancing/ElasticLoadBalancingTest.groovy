package jp.classmethod.aws.cloudformation.elasticloadbalancing

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/10/09.
 */
class ElasticLoadBalancingTest {

    @Test
    void "dev toResourceMap"() {
        def sut = new ElasticLoadBalancing(
            id: 'ELB',
            LoadBalancerName: 'elb-blue',
            Subnets: [['Fn::FindInMap': ['Resources', 'Subnet', 'FrontA']], ['Fn::FindInMap': ['Resources', 'Subnet', 'FrontC']]],
            Listeners: [
                new ElasticLoadBalancing.Listener('Protocol': 'HTTP', 'LoadBalancerPort': '80', 'InstanceProtocol': 'HTTP', 'InstancePort': '80'),
                new ElasticLoadBalancing.Listener('Protocol': 'HTTPS', 'LoadBalancerPort': '443', 'InstanceProtocol': 'HTTP', 'InstancePort': '80', 'SSLCertificateId': [
                    ['Fn::FindInMap': ['Resources', 'SSLCertificateId', 'ExampleCom']]
                ])
            ],
            SecurityGroups: [
                ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'Internal']],
                ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'FrontWeb']],
            ],
            Instances: [
                ['Fn::FindInMap': ['Resources', 'Instance', 'Web1']],
                ['Fn::FindInMap': ['Resources', 'Instance', 'Web2']],
            ],
            HealthCheck: new ElasticLoadBalancing.HealthCheck(
                Target: 'HTTP:80/index.html',
                Timeout: '5',
                Interval: '30',
                UnhealthyThreshold: '2',
                HealthyThreshold: '10'
            )
        )
        def expected = [
            'Type'      : 'AWS::ElasticLoadBalancing::LoadBalancer',
            'Properties': [
                'LoadBalancerName': 'elb-blue',
                'Subnets'         : [['Fn::FindInMap': ['Resources', 'Subnet', 'FrontA']], ['Fn::FindInMap': ['Resources', 'Subnet', 'FrontC']]],
                'Listeners'       : [
                    ['Protocol': 'HTTP', 'LoadBalancerPort': '80', 'InstanceProtocol': 'HTTP', 'InstancePort': '80'],
                    ['Protocol': 'HTTPS', 'LoadBalancerPort': '443', 'InstanceProtocol': 'HTTP', 'InstancePort': '80', 'SSLCertificateId': [
                        ['Fn::FindInMap': ['Resources', 'SSLCertificateId', 'ExampleCom']]
                    ]],
                ],
                SecurityGroups    : [
                    ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'Internal']],
                    ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'FrontWeb']],
                ],
                Instances         : [
                    ['Fn::FindInMap': ['Resources', 'Instance', 'Web1']],
                    ['Fn::FindInMap': ['Resources', 'Instance', 'Web2']],
                ],
                'HealthCheck'     : [
                    Target            : 'HTTP:80/index.html',
                    Timeout           : '5',
                    Interval          : '30',
                    UnhealthyThreshold: '2',
                    HealthyThreshold  : '10',
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


    @Test
    void "AccessLoggingPolicy toResourceMap"() {
        def sut = new ElasticLoadBalancing(
            id: 'ELB',
            LoadBalancerName: 'elb',
            Subnets: [['Fn::FindInMap': ['Resources', 'Subnet', 'FrontA']]],
            Listeners: [],
            SecurityGroups: [
                ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'Internal']],
                ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'FrontWeb']],
            ],
            Instances: [
                ['Fn::FindInMap': ['Resources', 'Instance', 'Web1']],
                ['Fn::FindInMap': ['Resources', 'Instance', 'Web2']],
            ],
            HealthCheck: new ElasticLoadBalancing.HealthCheck(
                Target            : 'HTTP:80/index.html',
                Timeout           : '5',
                Interval          : '30',
                UnhealthyThreshold: '2',
                HealthyThreshold  : '10',
            ),
            AccessLoggingPolicy: new ElasticLoadBalancing.AccessLoggingPolicy(
                Enabled: 'true',
                S3BucketName: 'logs.classmethod.jp',
                S3BucketPrefix: 'elb/prd',
                EmitInterval: '60'
            )
        )
        def expected = [
            'Type'      : 'AWS::ElasticLoadBalancing::LoadBalancer',
            'Properties': [
                'LoadBalancerName'   : 'elb',
                'Subnets'            : [['Fn::FindInMap': ['Resources', 'Subnet', 'FrontA']]],
                'Listeners'          : [
                ],
                SecurityGroups       : [
                    ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'Internal']],
                    ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'FrontWeb']],
                ],
                Instances            : [
                    ['Fn::FindInMap': ['Resources', 'Instance', 'Web1']],
                    ['Fn::FindInMap': ['Resources', 'Instance', 'Web2']],
                ],
                'HealthCheck'        : [
                    Target            : 'HTTP:80/index.html',
                    Timeout           : '5',
                    Interval          : '30',
                    UnhealthyThreshold: '2',
                    HealthyThreshold  : '10',
                ],
                'AccessLoggingPolicy': [
                    'Enabled'       : 'true',
                    'S3BucketName'  : 'logs.classmethod.jp',
                    'S3BucketPrefix': 'elb/prd',
                    'EmitInterval'  : '60',
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
