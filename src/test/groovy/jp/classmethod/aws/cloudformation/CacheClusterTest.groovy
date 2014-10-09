package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/09/11.
 */
class CacheClusterTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("CacheClusterTest_default.csv").getFile())
        def actual = CacheCluster.load(input)
        assert actual == [
                new CacheCluster(
                        id: 'ElasticCacheDev',
                        name: 'elastic-cache-dev',
                        cacheNodeType: 'cache.m1.small',
                        numCacheNodes: '1',
                        engine: 'redis',
                        engineVersion: '2.8.6',
                        autoMinorVersionUpgrade: true,
                        cacheParameterGroupName: 'default.redis2.8',
                        preferredAvailabilityZone: 'ap-northeast-1a',
                        cacheSubnetGroupName: 'ElasticCacheGroupDev',
                        vpcSecurityGroupIds: ['Internal'],
                )
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new CacheCluster(
                id: 'ElasticCacheDev',
                name: 'elastic-cache-dev',
                cacheNodeType: 'cache.m1.small',
                numCacheNodes: '1',
                engine: 'redis',
                engineVersion: '2.8.6',
                autoMinorVersionUpgrade: true,
                cacheParameterGroupName: 'default.redis2.8',
                preferredAvailabilityZone: 'ap-northeast-1a',
                cacheSubnetGroupName: 'ElasticCacheGroupDev',
                vpcSecurityGroupIds: ['Internal']
        )
        def expected = [
            "ElasticCacheDev": [
                'Type': 'AWS::ElastiCache::CacheCluster',
                'Properties': [
                    'CacheNodeType': 'cache.m1.small',
                    'NumCacheNodes': '1',
                    'Engine': 'redis',
                    'EngineVersion': '2.8.6',
                    'AutoMinorVersionUpgrade': true,
                    'CacheParameterGroupName': 'default.redis2.8',
                    'PreferredAvailabilityZone': 'ap-northeast-1a',
                    'CacheSubnetGroupName': 'ElasticCacheGroupDev',
                    'VpcSecurityGroupIds': [['Ref': 'Internal']],
                    'Tags': [
                        ['Key': 'Name', 'Value': 'elastic-cache-dev'],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
