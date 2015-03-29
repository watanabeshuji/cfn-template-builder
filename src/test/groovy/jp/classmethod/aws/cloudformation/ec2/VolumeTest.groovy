package jp.classmethod.aws.cloudformation.ec2

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Test for Volume
 */
class VolumeTest {

    @Test
    void "load volume.groovy"() {
        Path input = getPath("/templates/resources/volume.groovy")
        def actual = Volume.load(input)
        assert actual == [
            new Volume(id: "WebVolume", Size: "40", VolumeType: "gp2", AvailabilityZone: "ap-northeast-1a")
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new Volume(id: 'TableauPrimaryVolume', Size: '100', VolumeType: 'gp2', AvailabilityZone: 'ap-northeast-1a')
        def expected = [
            'Type'      : 'AWS::EC2::Volume',
            'Properties': [
                'AvailabilityZone': 'ap-northeast-1a',
                'Size'            : '100',
                'VolumeType'      : 'gp2',
                'Tags'            : [
                    ['Key': 'Name', 'Value': 'TableauPrimaryVolume'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
