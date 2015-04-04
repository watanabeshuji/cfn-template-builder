package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.util.ValidErrorException
import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/20.
 */
class SubnetRouteTableAssociationTest {


    @Test
    void "load subnetRouteTableAssociation.groovy"() {
        Path input = getPath("/templates/resources/subnetRouteTableAssociation.groovy")
        def actual = SubnetRouteTableAssociation.load(input)
        assert actual == [
            new SubnetRouteTableAssociation(
                id: 'SubnetRouteTableAssociation',
                SubnetId: [Ref: "Subnet"],
                RouteTableId: [Ref: "RouteTable"]),
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new SubnetRouteTableAssociation(
            id: 'PublicRouteAssociation',
            SubnetId: [Ref: 'PublicSubnet'],
            RouteTableId: [Ref: 'PublicRouteTable'])
        def expected = [
            'Type'      : 'AWS::EC2::SubnetRouteTableAssociation',
            'Properties': [
                'SubnetId'    : ['Ref': 'PublicSubnet'],
                'RouteTableId': ['Ref': 'PublicRouteTable']
            ]
        ]
        assert sut.toResourceMap() == expected
    }


    @Test
    void "refIds"() {
        def sut = SubnetRouteTableAssociation.newInstance(
            id: 'PublicRouteAssociation',
            SubnetId: 'Ref:PublicSubnet',
            RouteTableId: 'Ref:PublicRouteTable')
        assert sut.refIds == ['PublicSubnet', 'PublicRouteTable']
    }

    @Test(expected = ValidErrorException)
    void "id 必須"() {
        def sut = SubnetRouteTableAssociation.newInstance(
            SubnetId: 'Ref:PublicSubnet',
            RouteTableId: 'Ref:PublicRouteTable')
    }

    @Test(expected = ValidErrorException)
    void "SubnetId 必須"() {
        def sut = SubnetRouteTableAssociation.newInstance(
            id: 'PublicRouteAssociation',
            RouteTableId: 'Ref:PublicRouteTable')
    }

    @Test(expected = ValidErrorException)
    void "RouteTableId 必須"() {
        def sut = SubnetRouteTableAssociation.newInstance(
            id: 'PublicRouteAssociation',
            SubnetId: 'Ref:PublicSubnet')
    }
}
