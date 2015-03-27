package jp.classmethod.aws.cloudformation.dsl

import jp.classmethod.aws.cloudformation.cloudformation.WaitCondition
import jp.classmethod.aws.cloudformation.cloudformation.WaitConditionHandle
import jp.classmethod.aws.cloudformation.ec2.EIP
import jp.classmethod.aws.cloudformation.ec2.Instance
import jp.classmethod.aws.cloudformation.ec2.InternetGateway
import jp.classmethod.aws.cloudformation.ec2.Route
import jp.classmethod.aws.cloudformation.ec2.RouteTable
import jp.classmethod.aws.cloudformation.ec2.SecurityGroup
import jp.classmethod.aws.cloudformation.ec2.SecurityGroupIngress
import jp.classmethod.aws.cloudformation.ec2.Subnet
import jp.classmethod.aws.cloudformation.ec2.SubnetRouteTableAssociation
import jp.classmethod.aws.cloudformation.ec2.VPC
import jp.classmethod.aws.cloudformation.ec2.VPCGatewayAttachment
import jp.classmethod.aws.cloudformation.ec2.Volume
import jp.classmethod.aws.cloudformation.elasticloadbalancing.ElasticLoadBalancing
import jp.classmethod.aws.cloudformation.iam.InstanceProfile
import jp.classmethod.aws.cloudformation.iam.Policy
import jp.classmethod.aws.cloudformation.iam.Role
import jp.classmethod.aws.cloudformation.rds.DBInstance
import jp.classmethod.aws.cloudformation.rds.DBSubnetGroup
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by watanabeshuji on 2015/03/26.
 */
class ResourcesDSLTest {

    @Test
    void "load vpc.groovy"() {
        Path input = Paths.get("cfn/resources/vpc.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
            new VPC(id: 'vpc1', CidrBlock: "10.0.0.0/16"),
            new VPC(id: 'vpc2', CidrBlock: "10.0.0.0/16", EnableDnsSupport: true, EnableDnsHostnames: true),
        ]
    }

    @Test
    void "load internetGateway.groovy"() {
        Path input = Paths.get("cfn/resources/internetGateway.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
            new InternetGateway(id: 'InternetGateway'),
        ]
    }

    @Test
    void "load vpcGatewayAttachment.groovy"() {
        Path input = Paths.get("cfn/resources/vpcGatewayAttachment.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
                new VPCGatewayAttachment(id: "InternetGatewayAttach", VpcId: [Ref: "VPC"], InternetGatewayId: [Ref: "InternetGateway"])
        ]
    }


    @Test
    void "load subnet.groovy"() {
        Path input = Paths.get("cfn/resources/subnet.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
            new Subnet(id: 'subnet1', CidrBlock: "10.0.0.0/24", VpcId: [Ref: "vpc"]),
            new Subnet(id: 'subnet2', CidrBlock: "10.0.1.0/24", VpcId: [Ref: "vpc"], AvailabilityZone: "ap-northeast-1a")
        ]
    }

    @Test
    void "load routeTable.groovy"() {
        Path input = Paths.get("cfn/resources/routeTable.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
                new RouteTable(id: 'PublicRouteTable', VpcId: [Ref: "vpc"]),
        ]
    }

    @Test
    void "load route.groovy"() {
        Path input = Paths.get("cfn/resources/route.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
            new Route(id: 'PublicRoute', DestinationCidrBlock: '0.0.0.0/0', GatewayId: [Ref: "IGW"]),
            new Route(id: 'NatRoute', DestinationCidrBlock: '0.0.0.0/0', InstanceId: [Ref: "NAT"]),
            new Route(id: 'DirectConnectRoute', DestinationCidrBlock: '10.226.0.0/16', VpcPeeringConnectionId: "pcx-xxxxxxxx"),
        ]
    }

    @Test
    void "load subnetRouteTableAssociation.groovy"() {
        Path input = Paths.get("cfn/resources/subnetRouteTableAssociation.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
                new SubnetRouteTableAssociation(
                        id: 'SubnetRouteTableAssociation',
                        SubnetId: [Ref: "Subnet"],
                        RouteTableId: [Ref: "RouteTable"]),
        ]
    }

    @Test
    void "load securityGroup.groovy"() {
        Path input = Paths.get("cfn/resources/securityGroup.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
                new SecurityGroup(id: 'PublicWeb', VpcId: [Ref: "VPC"], Description: "Allow web access from internet.",
                    SecurityGroupIngress: [
                            new SecurityGroupIngress(IpProtocol: "tcp", FromPort: 80,  ToPort:  80, CidrIp: "0.0.0.0/0"),
                            new SecurityGroupIngress(IpProtocol: "tcp", FromPort: 443,  ToPort:  443, CidrIp: "0.0.0.0/0")
                ]),
        ]
    }

    @Test
    void "load volume.groovy"() {
        Path input = Paths.get("cfn/resources/volume.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
            new Volume(id: "WebVolume", Size: "40", VolumeType: "gp2", AvailabilityZone: "ap-northeast-1a")
        ]
    }


    @Test
    void "load instance.groovy"() {
        Path input = Paths.get("cfn/resources/instance.groovy")
        def actual = ResourcesDSL.load(input)
        def userData = '''/
#!/bin/sh
yum -y update
'''
        def excepted = [
            new Instance(id: "Web", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PublicSubnet"],
                    ImageId: "FindInMap:AMI:AmazonLinux:201503", IamInstanceProfile: [Ref: "WebInstanceProfile"],
                    SourceDestCheck: true, SecurityGroupIds: [[Ref: "Internal"], [Ref: "PublicWeb"]],
                    UserData: userData),
            new Instance(id: "Batch", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PrivateSubnet"],
                    ImageId: "FindInMap:AMI:AmazonLinux:201503", IamInstanceProfile: [Ref: "WebInstanceProfile"],
                    SourceDestCheck: true, SecurityGroupIds: [[Ref: "Internal"]],
                    Volumes: [
                        new Instance.Volume(VolumeId: [Ref: "BatchVolume"], Device: "/dev/sdk")
                    ]
            ),
            new Instance(id: "Server", InstanceType: "t2.small", KeyName: "web-key", SubnetId: [Ref: "PrivateSubnet"],
                    ImageId: "FindInMap:AMI:AmazonLinux:201503",
                    BlockDeviceMappings: [
                            new Instance.BlockDeviceMapping(DeviceName: "/dev/xvda", Ebs: new Instance.Ebs(VolumeType: "gp2", VolumeSize: "200", DeleteOnTermination: true)),
                            new Instance.BlockDeviceMapping(DeviceName: "/dev/xvdb", Ebs: new Instance.Ebs(VolumeType: "gp2", VolumeSize: "100"))
                    ]),
        ]
        assert actual == excepted
    }

    @Test
    void "load eip.groovy"() {
        Path input = Paths.get("cfn/resources/eip.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
            new EIP(id: 'PublicIP'),
            new EIP(id: "WebIP", InstanceId: [Ref: "Web"])
        ]
    }

    @Test
    void "load role.groovy"() {
        Path input = Paths.get("cfn/resources/role.groovy")
        def actual = ResourcesDSL.load(input)
        assert actual == [
            new Role(id: 'Role')
        ]
    }

    @Test
    void "load policy.groovy"() {
        Path input = Paths.get("cfn/resources/policy.groovy")
        def actual = ResourcesDSL.load(input)
        def doc  = [
            "Version" : "2012-10-17",
            "Statement": [
                ["Effect": "Allow", "Action": "*", "Resource": "*" ]
            ]
        ]
        def expected = [
            new Policy(id: 'EC2Policy', PolicyName: "EC2", PolicyDocument: doc, Roles: [[Ref: "EC2Role"]])
        ]
        assert actual == expected
    }

    @Test
    void "load instanceProfile.groovy"() {
        Path input = Paths.get("cfn/resources/instanceProfile.groovy")
        def actual = ResourcesDSL.load(input)
        def expected = [
            new InstanceProfile(id: 'EC2InstanceProfile', Path: "/", Roles: [[Ref: "EC2Role"]])
        ]
        assert actual == expected
    }

    @Test
    void "load dbSubnetGroup.groovy"() {
        Path input = Paths.get("cfn/resources/dbSubnetGroup.groovy")
        def actual = ResourcesDSL.load(input)
        def expected = [
            new DBSubnetGroup(id: 'DBSubnetGroup', DBSubnetGroupDescription: "DB subnet group", SubnetIds: ["Ref:PrivateA", "Ref:PrivateA"])
        ]
        assert actual == expected
    }

    @Test
    void "load dbInstance.groovy"() {
        Path input = Paths.get("cfn/resources/dbInstance.groovy")
        def actual = ResourcesDSL.load(input)
        def expected = [
            new DBInstance(id: 'DbPrd', DBSubnetGroupName: [Ref: "DbSubnetGroup"], MultiAZ: true,
                           DBInstanceClass: "db.m3.xlarge", AllocatedStorage: "200", Iops: 1000, Engine: "mysql", EngineVersion: "5.6.19", Port: "3306",
                           DBParameterGroupName: "default.mysql5.6", DBName: "app", MasterUsername: "root", MasterUserPassword: "pass1234",
                           VPCSecurityGroups:  [[Ref: "Internal"]]),
            new DBInstance(id: "DbDev", DBSubnetGroupName: [Ref: "DbSubnetGroup"], MultiAZ: false ,AvailabilityZone: "ap-northeast-1a",
                           DBInstanceClass: "db.m1.small", AllocatedStorage: "50", Engine: "mysql", EngineVersion: "5.6.19", Port: "3306",
                           DBParameterGroupName: "default.mysql5.6", DBName: "app", MasterUsername: "root", MasterUserPassword: "pass1234",
                           VPCSecurityGroups: [[Ref: "Internal"]], DBSnapshotIdentifier: "snapshot01")
        ]
        assert actual == expected
    }

    @Test
    void "load elasticLoadBalancing.groovy"() {
        Path input = Paths.get("cfn/resources/elasticLoadBalancing.groovy")
        def actual = ResourcesDSL.load(input)
        def expected = [
            new ElasticLoadBalancing(id: 'ELB',  LoadBalancerName: "ELB",
                    Subnets: [[Ref: "FrontA"], [Ref: "FrontC"]], SecurityGroups: [[Ref: "PublicWeb"]],
                    Instances: [[Ref: "WebA"], [Ref: "WebC"]],
                    Listeners: [
                        new ElasticLoadBalancing.Listener(Protocol: "HTTP", LoadBalancerPort: "80", InstancePort: "80"),
                        new ElasticLoadBalancing.Listener(Protocol: "HTTPS", LoadBalancerPort: "443", InstanceProtocol: "HTTP",InstancePort: "80", SSLCertificateId: "sslid")
                    ],
                    HealthCheck: new ElasticLoadBalancing.HealthCheck(Target: "HTTP:80/", HealthyThreshold: "3", UnhealthyThreshold: "5", Interval: "30", Timeout: "5"),
                    AccessLoggingPolicy: new ElasticLoadBalancing.AccessLoggingPolicy(Enabled: true, S3BucketName: [Ref: "ElbAccessLoggingBucket"], S3BucketPrefix: "elb/", EmitInterval: 60)
            )
        ]
        assert actual == expected
    }


    @Test
    void "load waitConditionHandle.groovy"() {
        Path input = Paths.get("cfn/resources/waitConditionHandle.groovy")
        def actual = ResourcesDSL.load(input)
        def expected = [
            new WaitConditionHandle(id: 'WebServerWaitConditionHandle')
        ]
        assert actual == expected
    }

    @Test
    void "load waitCondition.groovy"() {
        Path input = Paths.get("cfn/resources/waitCondition.groovy")
        def actual = ResourcesDSL.load(input)
        def expected = [
            new WaitCondition(id: 'WebServerWaitCondition', Handle: [Ref: "WebServerWaitHandle"], Timeout: "1000")
        ]
        assert actual == expected
    }
}
