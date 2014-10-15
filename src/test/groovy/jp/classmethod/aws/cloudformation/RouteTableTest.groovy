package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class RouteTableTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("RouteTableTest_default.csv").getFile())
        def actual = RouteTable.load(input)
        assert actual == [
                new RouteTable(id: 'PublicRouteTable',  Name: 'public-route-table',  Vpc: 'Vpc'),
                new RouteTable(id: 'PrivateRouteTable', Name: 'private-route-table', Vpc: 'Vpc')
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new RouteTable(id: 'PublicRouteTable',  Name: 'public-route-table',  Vpc: 'Vpc')
        def expected = [
            'PublicRouteTable': [
                'Type': 'AWS::EC2::RouteTable',
                'Properties': [
                    'VpcId': ['Ref': 'Vpc'],
                    'Tags': [
                        ['Key': 'Name', 'Value': 'public-route-table'],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
