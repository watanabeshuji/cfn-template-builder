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
    def BlockDeviceMappings = []
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
                'Properties': [:]
            ]
        ]
        if (InstanceType) result[id]['Properties']['InstanceType'] = InstanceType
        if (KeyName) result[id]['Properties']['KeyName'] = KeyName
        result[id]['Properties']['SubnetId'] = Util.ref(SubnetId)
        result[id]['Properties']['ImageId'] = ImageId
        if (IamInstanceProfile) result[id]['Properties']['IamInstanceProfile'] = IamInstanceProfile
        if (SourceDestCheck != null) result[id]['Properties']['SourceDestCheck'] = SourceDestCheck
        result[id]['Properties']['SecurityGroupIds'] = SecurityGroupIds.collect { Util.ref(it) }
        if (PrivateIpAddress) result[id]['Properties']['PrivateIpAddress'] = PrivateIpAddress
        result[id]['Properties']['Tags'] = [
            ['Key': 'Name', 'Value': Name],
            ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
        ]
        if(!userData.isEmpty()) {
            result[id]['Properties']['UserData'] = [
                    'Fn::Base64': [
                            'Fn::Join': ['', userData]
                    ]
            ]
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
        if (!BlockDeviceMappings.isEmpty()) {
            result[id]['Properties']['BlockDeviceMappings'] = BlockDeviceMappings
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
                instance.BlockDeviceMappings += loadBlockDeviceMappings(file, instance.name)
                result << instance
            }
        }
        result
    }

    static def loadBlockDeviceMappings(File baseFile, name) {
        def result = []
        def file = new File(Util.associatedFile(baseFile.absolutePath, "${name}_BlockDeviceMappings"))
        if (!file.exists()) return []
        def meta
        file.eachLine { line, num ->
            if (num == 1) {
                meta = new SourceMeta(line.split(','))
            } else {
                result << Instance.blockDeviceMapping(meta.newSource(line.split(',')))
            }
        }
        result
    }

    static def blockDeviceMapping(Source source) {
        def map = [
            'DeviceName': source.value('DeviceName'),
            'Ebs': [
                'VolumeSize': source.value('EbsVolumeSize'),
            ]
        ]
        if (source.value('EbsVolumeType')) map['Ebs']['VolumeType'] = source.value('EbsVolumeType')
        if (source.value('EbsDeleteOnTermination')) map['Ebs']['DeleteOnTermination'] = source.value('EbsDeleteOnTermination')
        map
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Instance r -> o << r.toResourceMap() } )
    }

}
