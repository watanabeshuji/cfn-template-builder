package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.util.ValidErrorException
import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class RouteTableTest {

    @Test
    void "load routeTable.groovy"() {
        Path input = getPath("/templates/resources/routeTable.groovy")
        def actual = RouteTable.load(input)
        assert actual == [
            new RouteTable(id: 'PublicRouteTable', VpcId: [Ref: "vpc"]),
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new RouteTable(id: 'PublicRouteTable', VpcId: [Ref: 'VPC'])
        def expected = [
            'Type'      : 'AWS::EC2::RouteTable',
            'Properties': [
                'VpcId': ['Ref': 'VPC'],
                'Tags' : [
                    ['Key': 'Name', 'Value': 'PublicRouteTable'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


    @Test
    void "refIds"() {
        def sut = RouteTable.newInstance(id: 'PublicRouteTable', VpcId: "Ref:VPC")
        assert sut.refIds == ['VPC']
    }

    @Test(expected = ValidErrorException)
    void "id必須"() {
        RouteTable.newInstance(VpcId: "Ref:VPC")
    }

    @Test(expected = ValidErrorException)
    void "VpcId必須"() {
        RouteTable.newInstance(id: 'PublicRouteTable')
    }

}
