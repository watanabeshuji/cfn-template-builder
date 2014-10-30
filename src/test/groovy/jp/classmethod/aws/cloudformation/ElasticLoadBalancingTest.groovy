package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/10/09.
 */
class ElasticLoadBalancingTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("ElasticLoadBalancing_default.csv").getFile())
        def actual = ElasticLoadBalancing.load(input)
        assert actual == [
            new ElasticLoadBalancing(
                id: 'Elb',
                name: 'elb',
                LoadBalancerName: 'elb-blue',
                Subnets: [['Fn::FindInMap': ['Resources', 'Subnet', 'FrontA']], ['Fn::FindInMap': ['Resources', 'Subnet', 'FrontC']]],
                Listeners: [
                    ['Protocol': 'HTTP', 'LoadBalancerPort': '80', 'InstanceProtocol': 'HTTP' ,'InstancePort':'80'],
                    ['Protocol': 'HTTPS','LoadBalancerPort': '443','InstanceProtocol': 'HTTP' ,'InstancePort':'80', 'SSLCertificateId': [
                        'Fn::FindInMap': ['Resources', 'SSLCertificateId', 'ExampleCom']
                    ]],
                ],
                SecurityGroups: [
                    ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'Internal']],
                    ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'FrontWeb']],
                ],
                Instances: [
                    ['Fn::FindInMap': ['Resources', 'Instance', 'Web1']],
                    ['Fn::FindInMap': ['Resources', 'Instance', 'Web2']],
                ],
                Target: 'HTTP:80/index.html',
                Timeout: '5',
                Interval: '30',
                UnhealthyThreshold: '2',
                HealthyThreshold: '10',
            )
        ]
    }

    @Test
    void "dev toResourceMap"() {
        def sut = new ElasticLoadBalancing(
            id: 'Elb',
            name: 'elb',
            LoadBalancerName: 'elb-blue',
            Subnets: [['Fn::FindInMap': ['Resources', 'Subnet', 'FrontA']], ['Fn::FindInMap': ['Resources', 'Subnet', 'FrontC']]],
            Listeners: [
                ['Protocol': 'HTTP', 'LoadBalancerPort': '80', 'InstanceProtocol': 'HTTP' ,'InstancePort':'80'],
                ['Protocol': 'HTTPS','LoadBalancerPort': '443','InstanceProtocol': 'HTTP' ,'InstancePort':'80', 'SSLCertificateId': [
                    ['Fn::FindInMap': ['Resources', 'SSLCertificateId', 'ExampleCom']]
                ]],
            ],
            SecurityGroups: [
                ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'Internal']],
                ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'FrontWeb']],
            ],
            Instances: [
                ['Fn::FindInMap': ['Resources', 'Instance', 'Web1']],
                ['Fn::FindInMap': ['Resources', 'Instance', 'Web2']],
            ],
            Target: 'HTTP:80/index.html',
            Timeout: '5',
            Interval: '30',
            UnhealthyThreshold: '2',
            HealthyThreshold: '10',
        )
        def expected = [
            "Elb": [
                'Type': 'AWS::ElasticLoadBalancing::LoadBalancer',
                'Properties': [
                    'LoadBalancerName': 'elb-blue',
                    'Subnets': [['Fn::FindInMap': ['Resources', 'Subnet', 'FrontA']], ['Fn::FindInMap': ['Resources', 'Subnet', 'FrontC']]],
                    'Listeners': [
                        ['Protocol': 'HTTP', 'LoadBalancerPort': '80', 'InstanceProtocol': 'HTTP' ,'InstancePort':'80'],
                        ['Protocol': 'HTTPS','LoadBalancerPort': '443','InstanceProtocol': 'HTTP' ,'InstancePort':'80', 'SSLCertificateId': [
                                ['Fn::FindInMap': ['Resources', 'SSLCertificateId', 'ExampleCom']]
                        ]],
                    ],
                    SecurityGroups: [
                        ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'Internal']],
                        ['Fn::FindInMap': ['Resources', 'SecurityGroup', 'FrontWeb']],
                    ],
                    Instances: [
                        ['Fn::FindInMap': ['Resources', 'Instance', 'Web1']],
                        ['Fn::FindInMap': ['Resources', 'Instance', 'Web2']],
                    ],
                    'HealthCheck': [
                        Target: 'HTTP:80/index.html',
                        Timeout: '5',
                        Interval: '30',
                        UnhealthyThreshold: '2',
                        HealthyThreshold: '10',
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }
}
