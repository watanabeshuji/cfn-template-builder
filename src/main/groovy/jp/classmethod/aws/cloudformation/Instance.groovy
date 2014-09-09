package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/08/13.
 */
@Canonical
class Instance {
    def id
    def name
    def instanceType
    def keyName
    def subnetId
    def imageId
    def iamInstanceProfile
    def sourceDestCheck
    def eip
    def securityGroups
    def volumes
    def userData = []

    def Instance() {
    }

    def Instance(Source source) {
        this.id = source.camelCase('Name')
        this.name = source.value('Name')
        this.instanceType = source.value('InstanceType')
        this.keyName = source.value('KeyName')
        if (source.containsKey('SubnetId')) {
            this.subnetId = source.value('SubnetId')
        } else {
            this.subnetId = ["Ref": source.camelCase('Subnet')]
        }
        this.imageId = source.value('ImageId')
        this.iamInstanceProfile = source.value('IamInstanceProfile')
        this.sourceDestCheck = source.bool('SourceDestCheck')
        this.eip = source.bool('EIP')
        if (source.containsKey('SecurityGroupIds')) {
            this.securityGroups = source.list('SecurityGroupIds')
        } else {
            this.securityGroups = source.camelCaseList('SecurityGroups').collect { ['Ref': it] }
        }
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
                                'InstanceType': instanceType,
                                'KeyName': keyName,
                                'SubnetId': subnetId,
                                'ImageId': imageId,
                                'IamInstanceProfile': iamInstanceProfile,
                                'SourceDestCheck': sourceDestCheck,
                                'SecurityGroupIds': securityGroups,
                                'Tags': [
                                        ['Key': 'Name', 'Value': name],
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
        // Elastic IP if needs
        if (eip) {
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
                        instance.userData << it + '\n'
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
