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
                new CacheSubnetGroup(id: 'CacheSubnetGroup', name: 'cache-subnet-group', description:'Elastic Cache Subnet Group', subnetIds: ['SubnetA', 'SubnetC'])
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new CacheSubnetGroup(id: 'CacheSubnetGroup', name: 'cache-subnet-group', description:'Elastic Cache Subnet Group', subnetIds: ['SubnetA', 'SubnetC'])
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

}
