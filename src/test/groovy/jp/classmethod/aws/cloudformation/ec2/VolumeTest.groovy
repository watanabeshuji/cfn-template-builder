package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.ec2.Volume
import org.junit.Test

/**
 * Test for Volume
 */
class VolumeTest {

    @Test
    void "toResourceMap"() {
        def sut = new Volume(id: 'TableauPrimaryVolume', Size: '100', VolumeType: 'gp2', AvailabilityZone: 'ap-northeast-1a')
        def expected = [
            'Type': 'AWS::EC2::Volume',
            'Properties': [
                'AvailabilityZone': 'ap-northeast-1a',
                'Size': '100',
                'VolumeType': 'gp2',
                'Tags': [
                    ['Key': 'Name', 'Value': 'TableauPrimaryVolume'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
