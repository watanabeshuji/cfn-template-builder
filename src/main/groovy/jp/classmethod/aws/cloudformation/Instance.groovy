package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/08/13.
 */
@Canonical
class Instance {
    def id
    def Name
    def InstanceType
    def KeyName
    def SubnetId
    def ImageId
    def IamInstanceProfile
    def SourceDestCheck
    def PrivateIpAddress
    def EIP
    def SecurityGroupIds
    def volumes
    def userData = []

    def Instance() {
    }

    def Instance(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.InstanceType = source.value('InstanceType')
        this.KeyName = source.value('KeyName')
        this.SubnetId = source.camelCase('SubnetId')
        this.ImageId = source.value('ImageId')
        this.IamInstanceProfile = source.value('IamInstanceProfile')
        this.SourceDestCheck = source.bool('SourceDestCheck')
        this.PrivateIpAddress = source.value('PrivateIpAddress')
        this.EIP = source.bool('EIP')
        this.SecurityGroupIds = source.camelCaseList('SecurityGroupIds')
        def volumesInput = source.value('Volumes')
        if (volumesInput != null) {
            volumes = volumesInput.split(/\|/).collect {
                def a = it.split(/=/)
                ['VolumeId': a[0], 'Device': a[1]]
            }
        }
    }

    def toResourceMap() {
        def result = [
                (id): [
                        'Type': 'AWS::EC2::Instance',
                        'Properties': [
                                'InstanceType': InstanceType,
                                'KeyName': KeyName,
                                'SubnetId': Util.ref(SubnetId),
                                'ImageId': ImageId,
                                'IamInstanceProfile': IamInstanceProfile,
                                'SourceDestCheck': SourceDestCheck,
                                'SecurityGroupIds': SecurityGroupIds.collect { Util.ref(it) },
                                'Tags': [
                                        ['Key': 'Name', 'Value': Name],
                                        ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                                ],
                        ]
                ]
        ]
        if(!userData.isEmpty()) {
            result[id]['Properties']['UserData'] = [
                    'Fn::Base64': [
                            'Fn::Join': ['', userData]
                    ]
            ]
        }
        if (PrivateIpAddress) {
            result[id]['Properties']['PrivateIpAddress'] = PrivateIpAddress
        }
        // Elastic IP if needs
        if (EIP) {
            result << [
                    ("${id}EIP" as String): [
                            'Type': 'AWS::EC2::EIP',
                            'Properties': [
                                    'Domain': 'vpc',
                                    'InstanceId': ['Ref': id ]
                            ]
                    ]
            ]
        }
        if (volumes) {
            result[id]['Properties']['Volumes'] = volumes.collect {
                ['VolumeId': ['Ref': it['VolumeId']], 'Device': it['Device']]
            }
        }
        result
    }


    static def load(File file) {
        if (!file.exists()) return []
        def result = []
        def meta
        def userDataDir = new File(file.parentFile, 'userdata')
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                def instance = new Instance(meta.newSource(line.split(',')))
                def userData = new File(userDataDir, instance.name)
                if (userData.exists()) {
                    userData.eachLine {
                        instance.userData << it + '\\n'
                    }
                }
                result << instance
            }
        }
        result
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Instance r -> o << r.toResourceMap() } )
    }

}
