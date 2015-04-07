package jp.classmethod.aws.cloudformation

import jp.classmethod.aws.cloudformation.cloudformation.WaitCondition
import jp.classmethod.aws.cloudformation.cloudformation.WaitConditionHandle
import jp.classmethod.aws.cloudformation.ec2.*
import jp.classmethod.aws.cloudformation.iam.InstanceProfile
import jp.classmethod.aws.cloudformation.iam.Policy
import jp.classmethod.aws.cloudformation.iam.Role
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by watanabeshuji on 2015/03/26.
 */
class CloudFormationTest {

    private Path getPath(String resource) {
        Paths.get(getClass().getResource(resource).getPath())
    }

    @Test
    void "load empty.groovy"() {
        Path input = getPath("/templates/empty.groovy")
        def actual = CloudFormation.load(input)
        assert actual == new CloudFormation(Description: "a cloudformation template.")
    }


    @Test
    void "load mappings.groovy"() {
        Path input = getPath("/templates/mappings.groovy")
        def actual = CloudFormation.load(input)
        assert actual == new CloudFormation(
            Mappings: [
                RegionMap : [
                    "us-northeast-1": ["AvailabilityZoneA": "ap-northeast-1a", "AvailabilityZoneC": "ap-northeast-1c"]
                ],
                AddressMap: [
                    "IpRange": ["VPC": "10.0.0.0/24"]
                ]
            ],
            Resources: [
                new VPC(id: 'VPC', CidrBlock: ["Fn::FindInMap": ["AddressMap", "IpRange", "VPC"]]),
            ]
        )
    }

    @Test
    void "load parameters.groovy"() {
        Path input = getPath("/templates/parameters.groovy")
        def actual = CloudFormation.load(input)
        assert actual == new CloudFormation(
            Parameters: [
                InstanceTypeParameter: [Type: "String", Default: "t1.micro", AllowedValues: ["t1.micro", "m1.small", "m1.large"], Description: "Enter t1.micro, m1.small, or m1.large. Default is t1.micro."]
            ]
        )
    }

    @Test
    void "load vpc.groovy"() {
        Path input = getPath("/templates/vpc.groovy")
        def actual = CloudFormation.load(input)
        def expected = new CloudFormation(
            Description: "a vpc template.",
            Mappings: [
                RegionMap: [
                    "us-northeast-1": ["AvailabilityZoneA": "ap-northeast-1a", "AvailabilityZoneC": "ap-northeast-1c"]
                ]
            ],
            Resources: [
                new VPC(id: 'VPC', CidrBlock: "10.0.0.0/16"),
                new InternetGateway(id: 'InternetGateway'),
                new VPCGatewayAttachment(id: 'InternetGatewayAttach', VpcId: [Ref: "VPC"], InternetGatewayId: [Ref: "InternetGateway"]),
                new Subnet(id: 'SubnetA', CidrBlock: "10.0.0.0/24", VpcId: [Ref: "VPC"], AvailabilityZone: "ap-northeast-1a"),
                new Subnet(id: 'SubnetC', CidrBlock: "10.0.1.0/24", VpcId: [Ref: "VPC"], AvailabilityZone: "ap-northeast-1c"),
                new RouteTable(id: 'PublicRouteTable', VpcId: [Ref: "VPC"]),
                new RouteTable(id: 'PrivateRouteTable', VpcId: [Ref: "VPC"]),
                new Route(id: 'PublicRoute', RouteTableId: [Ref: "PublicRouteTable"], DestinationCidrBlock: '0.0.0.0/0', GatewayId: [Ref: "InternetGateway"]),
                new SubnetRouteTableAssociation(id: 'SubnetRouteTableAssociationA', SubnetId: [Ref: "SubnetA"], RouteTableId: [Ref: "PublicRouteTable"]),
                new SubnetRouteTableAssociation(id: 'SubnetRouteTableAssociationC', SubnetId: [Ref: "SubnetC"], RouteTableId: [Ref: "PublicRouteTable"]),
                new SecurityGroup(id: 'PublicWeb', VpcId: [Ref: "VPC"], GroupDescription: "Allow web access from internet.",
                    SecurityGroupIngress: [
                        new SecurityGroupIngress(IpProtocol: "tcp", FromPort: 80, ToPort: 80, CidrIp: "0.0.0.0/0"),
                        new SecurityGroupIngress(IpProtocol: "tcp", FromPort: 443, ToPort: 443, CidrIp: "0.0.0.0/0")
                    ]),
            ]
        )
        assert actual == expected
    }

    @Test
    void "load ec2.groovy"() {
        Path input = getPath("/templates/ec2.groovy")
        def actual = CloudFormation.load(input)
        def expected = new CloudFormation(
            Description: "a ec2 template.",
            Resources: [
                new WaitConditionHandle(id: 'WebWaitConditionHandle'),
                new WaitCondition(id: 'WebWaitCondition', Handle: [Ref: "WebWaitConditionHandle"], Timeout: "1000"),
                new EIP(id: "WebIP", InstanceId: [Ref: "Web"]),
            ]
        )
        assert actual == expected
    }

    @Test
    void "load iam.groovy"() {
        Path input = getPath("/templates/iam.groovy")
        def actual = CloudFormation.load(input)
        def doc = [
            "Version"  : "2012-10-17",
            "Statement": [
                ["Effect": "Allow", "Action": "*", "Resource": "*"]
            ]
        ]
        def expected = new CloudFormation(
            Description: "a iam template.",
            Resources: [
                new Role(id: 'EC2Role'),
                new Policy(id: 'EC2Policy', PolicyName: "EC2", PolicyDocument: doc, Roles: [[Ref: "EC2Role"]]),
                new InstanceProfile(id: 'EC2InstanceProfile', Path: "/", Roles: [[Ref: "EC2Role"]])
            ]
        )
        assert actual == expected
    }


    @Test
    void "load separated.groovy"() {
        Path input = getPath("/templates/separated.groovy")
        def actual = CloudFormation.load(input)
        def expected = new CloudFormation(
            Description: "a separated template.",
            Resources: [
                new VPC(id: 'VPC', CidrBlock: "10.0.0.0/16"),
                new Subnet(id: 'Subnet1', CidrBlock: "10.0.0.0/24", VpcId: [Ref: "VPC"]),
                new Subnet(id: 'Subnet2', CidrBlock: "10.0.1.0/24", VpcId: [Ref: "VPC"], AvailabilityZone: "ap-northeast-1a"),
                new SecurityGroup(id: 'PublicWeb', VpcId: [Ref: "VPC"], GroupDescription: "Allow web access from internet.",
                    SecurityGroupIngress: [
                        new SecurityGroupIngress(IpProtocol: "tcp", FromPort: 80, ToPort: 80, CidrIp: "0.0.0.0/0"),
                        new SecurityGroupIngress(IpProtocol: "tcp", FromPort: 443, ToPort: 443, CidrIp: "0.0.0.0/0")
                    ]),
                new SecurityGroup(id: 'MyMainte', VpcId: [Ref: "VPC"], GroupDescription: "Allow ssh access from specific ip address.",
                    SecurityGroupIngress: [
                        new SecurityGroupIngress(IpProtocol: "tcp", FromPort: 22, ToPort: 22, CidrIp: "1.2.3.4/32"),
                        new SecurityGroupIngress(IpProtocol: "tcp", FromPort: 22, ToPort: 22, CidrIp: "1.2.3.5/32")
                    ]),
            ]
        )
        assert actual == expected
    }


    @Test
    void "load outputs.groovy"() {
        Path input = getPath("/templates/outputs.groovy")
        def actual = CloudFormation.load(input)
        def expected = new CloudFormation(
            Outputs: [
                CodeDeployTrustRoleARN: ["Fn::GetAtt": ["CodeDeployTrustRole", "Arn"]],
                InstanceProfile: [Ref: "InstanceProfile"]
            ]
        )
        assert actual == expected
    }


    @Test(expected = InvalidResourceException)
    void "doValidteで重複idはエラー"() {
        CloudFormation sut = new CloudFormation()
        sut.Resources << new VPC(id: "VPC")
        sut.Resources << new VPC(id: "VPC")
        sut.doValidate()
    }
}
