def userData = '''/
#!/bin/sh
yum -y update
'''

resources {
    instance id: "Web", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PublicSubnet"],
        ImageId: "FindInMap:AMI:AmazonLinux:201503", IamInstanceProfile: [Ref: "WebInstanceProfile"],
        SourceDestCheck: true, SecurityGroupIds: [[Ref: "Internal"], [Ref: "PublicWeb"]], UserData: userData
    instance id: "Batch", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PrivateSubnet"],
        ImageId: "FindInMap:AMI:AmazonLinux:201503", IamInstanceProfile: [Ref: "WebInstanceProfile"],
        SourceDestCheck: true, SecurityGroupIds: [[Ref: "Internal"]], {
        volume VolumeId: [Ref: "BatchVolume"], Device: "/dev/sdk"
    }
    instance id: "Server", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PrivateSubnet"], ImageId: "FindInMap:AMI:AmazonLinux:201503", {
        blockDeviceMapping DeviceName: "/dev/xvda", {
            ebs VolumeType: "gp2", VolumeSize: "200", DeleteOnTermination: true
        }
        blockDeviceMapping DeviceName: "/dev/xvdb", {
            ebs VolumeType: "gp2", VolumeSize: "100"
        }
    }
}
