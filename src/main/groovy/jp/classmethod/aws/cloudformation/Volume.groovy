package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

@Canonical
class Volume {
    def id
    def name
    def size
    def volumeType
    def availabilityZone

    def Volume() {
    }

    def Volume(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.size = source.value('Size')
        this.volumeType = source.value('VolumeType')
        this.availabilityZone = source.value('AvailabilityZone')
    }

    def toResourceMap() {
        [
            (this.id): [
                'Type': 'AWS::EC2::Volume',
                'Properties': [
                    'AvailabilityZone': availabilityZone,
                    'Size': size,
                    'VolumeType': volumeType,
                    'Tags': [
                        ['Key': 'Name', 'Value': name],
                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                    ]
                ]
            ]
        ]
    }

    static def load(file) {
        if (!file.exists()) return []
        def result = []
        def meta
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                result << new Volume(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Volume r -> o << r.toResourceMap() })
    }
}
