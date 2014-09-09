package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Test for Volume
 */
class VolumeTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("VolumeTest_default.csv").getFile())
        def actual = Volume.load(input)
        assert actual == [
            new Volume(id: 'TableauPrimaryVolume', name: 'tableau-primary-volume', size: '100', volumeType: 'gp2', availabilityZone: 'ap-northeast-1a'),
            new Volume(id: 'TableauWorker1Volume', name: 'tableau-worker1-volume', size: '150', volumeType: 'gp2', availabilityZone: 'ap-northeast-1c')
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new Volume(id: 'TableauPrimaryVolume', name: 'tableau-primary-volume', size: '100', volumeType: 'gp2', availabilityZone: 'ap-northeast-1a')
        def expected = [
            "TableauPrimaryVolume": [
                'Type': 'AWS::EC2::Volume',
                'Properties': [
                    'AvailabilityZone': 'ap-northeast-1a',
                    'Size': '100',
                    'VolumeType': 'gp2',
                    'Tags': [
                        ['Key': 'Name', 'Value': 'tableau-primary-volume'],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
