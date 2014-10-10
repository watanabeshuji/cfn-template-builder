package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/09/11.
 */
class CacheSubnetGroupTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("CacheSubnetGroupTest_default.csv").getFile())
        def actual = CacheSubnetGroup.load(input)
        assert actual == [
                new CacheSubnetGroup(id: 'CacheSubnetGroupDev', name: 'cache-subnet-group-dev', Description:'Elastic Cache Subnet Group for Development', SubnetNames: ['SubnetA', 'SubnetC'])
        ]
    }

    @Test
    void "byids.csvのload"() {
        File input = new File(getClass().getResource("CacheSubnetGroupTest_byids.csv").getFile())
        def actual = CacheSubnetGroup.load(input)
        assert actual == [
                new CacheSubnetGroup(id: 'CacheSubnetGroup', name: 'cache-subnet-group', Description:'Elastic Cache Subnet Group', SubnetIds: [
                        ['Fn::FindInMap': ['Resources', 'Subnet', 'PrivateA']],
                        ['Fn::FindInMap': ['Resources', 'Subnet', 'PrivateC']],
                ])
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new CacheSubnetGroup(id: 'CacheSubnetGroup', name: 'cache-subnet-group', Description:'Elastic Cache Subnet Group', SubnetNames: ['SubnetA', 'SubnetC'])
        def expected = [
            "CacheSubnetGroup": [
                'Type': 'AWS::ElastiCache::SubnetGroup',
                'Properties': [
                    'Description': 'Elastic Cache Subnet Group',
                    'SubnetIds': [['Ref': 'SubnetA'], ['Ref': 'SubnetC']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }



    @Test
    void "idのtoResourceMap"() {
        def sut = new CacheSubnetGroup(id: 'CacheSubnetGroup', name: 'cache-subnet-group', Description:'Elastic Cache Subnet Group', SubnetIds: [
            ['Fn::FindInMap': ['Resources', 'Subnet', 'PrivateA']],
            ['Fn::FindInMap': ['Resources', 'Subnet', 'PrivateC']],
        ])
        def expected = [
                "CacheSubnetGroup": [
                        'Type': 'AWS::ElastiCache::SubnetGroup',
                        'Properties': [
                                'Description': 'Elastic Cache Subnet Group',
                                'SubnetIds': [['Fn::FindInMap': ['Resources', 'Subnet', 'PrivateA']], ['Fn::FindInMap': ['Resources', 'Subnet', 'PrivateC']]]
                        ]
                ]
        ]
        assert sut.toResourceMap() == expected
    }
}
