package jp.classmethod.aws.cloudformation.route53

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2015/04/22.
 */
class HostedZoneTest {

    @Test
    void "load hostedZone.groovy"() {
        Path input = getPath("/templates/resources/hostedZone.groovy")
        def actual = HostedZone.load(input)
        def expected = [
            new HostedZone(id: 'ExampleComHostedZone', Name: "example.com")
        ]
        assert actual == expected
    }

    @Test
    void "load hostedZone_with_comment.groovy"() {
        Path input = getPath("/templates/resources/hostedZone_with_comment.groovy")
        def actual = HostedZone.load(input)
        def expected = [
            new HostedZone(id: 'ExampleComHostedZone', Name: "example.com", HostedZoneConfig: new HostedZone.HostedZoneConfig(
                Comment: 'for example.com'
            ))
        ]
        assert actual == expected
    }

    @Test
    void "toResourceMap"() {
        def sut = HostedZone.newInstance(id: 'ExampleComHostedZone', Name: "example.com")
        def expected = [
            'Type'      : 'AWS::Route53::HostedZone',
            'Properties': [
                'Name': 'example.com'
            ]
        ]
        assert sut.toResourceMap() == expected
    }


    @Test
    void "toResourceMap with comment"() {
        def sut = HostedZone.newInstance(id: 'ExampleComHostedZone', Name: "example.com")
        sut.HostedZoneConfig = new HostedZone.HostedZoneConfig(
            Comment: 'My hosted zone for example.com'
        )
        def expected = [
            'Type'      : 'AWS::Route53::HostedZone',
            'Properties': [
                'Name'            : 'example.com',
                "HostedZoneConfig": [
                    "Comment": "My hosted zone for example.com"
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }
}
