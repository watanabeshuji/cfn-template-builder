package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class Volume {
    def id
    def Name
    def Size
    def VolumeType
    def AvailabilityZone

    def Volume() {
    }

    def Volume(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.Size = source.value('Size')
        this.VolumeType = source.value('VolumeType')
        this.AvailabilityZone = source.value('AvailabilityZone')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::Volume',
                'Properties': [
                    'AvailabilityZone': AvailabilityZone,
                    'Size': Size,
                    'VolumeType': VolumeType,
                    'Tags': [
                        ['Key': 'Name', 'Value': Name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
    }

    static def load(File file) {
        Util.load(file, { new Volume(it) })
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Volume r -> o << r.toResourceMap() })
    }
}
