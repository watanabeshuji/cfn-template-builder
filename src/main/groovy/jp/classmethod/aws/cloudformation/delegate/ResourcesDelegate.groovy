package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.cloudformation.WaitCondition
import jp.classmethod.aws.cloudformation.cloudformation.WaitConditionHandle
import jp.classmethod.aws.cloudformation.ec2.*
import jp.classmethod.aws.cloudformation.elasticloadbalancing.ElasticLoadBalancing
import jp.classmethod.aws.cloudformation.iam.InstanceProfile
import jp.classmethod.aws.cloudformation.iam.Policy
import jp.classmethod.aws.cloudformation.iam.Role
import jp.classmethod.aws.cloudformation.rds.DBInstance
import jp.classmethod.aws.cloudformation.rds.DBSubnetGroup
import jp.classmethod.aws.cloudformation.route53.HostedZone
import jp.classmethod.aws.cloudformation.route53.RecordSet

import static jp.classmethod.aws.cloudformation.util.Params.convert

/**
 * Created by watanabeshuji on 2015/03/26.
 */
class ResourcesDelegate {
    List resources

    ResourcesDelegate(List resources) {
        this.resources = resources
    }

    void standardVpc(Map params) {
        if (params == null) params = [:]
        def name = params.containsKey("name") ? params["name"] : ""
        boolean multiAZ = params.containsKey("multiAZ") ? params["multiAZ"] : true
        boolean publicSubnetOnly = params.containsKey("publicSubnetOnly") ? params["publicSubnetOnly"] : false
        String cidrBlock = params.containsKey("cidrBlock") ? params["cidrBlock"] : "10.0.0.0/16"
        def vpcId = [Ref: "${name}VPC"]
        if (multiAZ) {
            if (!publicSubnetOnly) { // multi AZ | public/private
                vpc id: "${name}VPC", CidrBlock: cidrBlock
                internetGateway id: "InternetGateway"
                vpcGatewayAttachment id: "InternetGatewayAttach", VpcId: vpcId, InternetGatewayId: [Ref: "InternetGateway"]
                subnet id: "${name}PublicSubnetA", VpcId: vpcId, CidrBlock: "10.0.10.0/24", AvailabilityZone: "ap-northeast-1a"
                subnet id: "${name}PublicSubnetC", VpcId: vpcId, CidrBlock: "10.0.20.0/24", AvailabilityZone: "ap-northeast-1c"
                subnet id: "${name}PrivateSubnetA", VpcId: vpcId, CidrBlock: "10.0.11.0/24", AvailabilityZone: "ap-northeast-1a"
                subnet id: "${name}PrivateSubnetC", VpcId: vpcId, CidrBlock: "10.0.21.0/24", AvailabilityZone: "ap-northeast-1c"
                routeTable id: "PublicRouteTable", VpcId: vpcId
                routeTable id: "PrivateRouteTable", VpcId: vpcId
                route id: "PublicRoute", RouteTableId: [Ref: "PublicRouteTable"], DestinationCidrBlock: "0.0.0.0/0", GatewayId: [Ref: "InternetGateway"]
                subnetRouteTableAssociation id: "SubnetRouteTableAssociationPublicSubnetA", SubnetId: [Ref: "${name}PublicSubnetA"], RouteTableId: [Ref: "PublicRouteTable"]
                subnetRouteTableAssociation id: "SubnetRouteTableAssociationPublicSubnetC", SubnetId: [Ref: "${name}PublicSubnetC"], RouteTableId: [Ref: "PublicRouteTable"]
                subnetRouteTableAssociation id: "SubnetRouteTableAssociationPrivateSubnetA", SubnetId: [Ref: "${name}PrivateSubnetA"], RouteTableId: [Ref: "PublicRouteTable"]
                subnetRouteTableAssociation id: "SubnetRouteTableAssociationPrivateSubnetC", SubnetId: [Ref: "${name}PrivateSubnetC"], RouteTableId: [Ref: "PublicRouteTable"]
            } else { // multi AZ | public only
                vpc id: "${name}VPC", CidrBlock: cidrBlock
                internetGateway id: "InternetGateway"
                vpcGatewayAttachment id: "InternetGatewayAttach", VpcId: vpcId, InternetGatewayId: [Ref: "InternetGateway"]
                subnet id: "${name}SubnetA", VpcId: vpcId, CidrBlock: "10.0.10.0/24", AvailabilityZone: "ap-northeast-1a"
                subnet id: "${name}SubnetC", VpcId: vpcId, CidrBlock: "10.0.20.0/24", AvailabilityZone: "ap-northeast-1c"
                routeTable id: "PublicRouteTable", VpcId: vpcId
                route id: "PublicRoute", RouteTableId: [Ref: "PublicRouteTable"], DestinationCidrBlock: "0.0.0.0/0", GatewayId: [Ref: "InternetGateway"]
                subnetRouteTableAssociation id: "SubnetRouteTableAssociationSubnetA", SubnetId: [Ref: "${name}SubnetA"], RouteTableId: [Ref: "PublicRouteTable"]
                subnetRouteTableAssociation id: "SubnetRouteTableAssociationSubnetC", SubnetId: [Ref: "${name}SubnetC"], RouteTableId: [Ref: "PublicRouteTable"]
            }
        } else {
            if (!publicSubnetOnly) {// single AZ | public/private
                vpc id: "${name}VPC", CidrBlock: cidrBlock
                internetGateway id: "InternetGateway"
                vpcGatewayAttachment id: "InternetGatewayAttach", VpcId: vpcId, InternetGatewayId: [Ref: "InternetGateway"]
                subnet id: "${name}PublicSubnet", VpcId: vpcId, CidrBlock: "10.0.10.0/24", AvailabilityZone: "ap-northeast-1a"
                subnet id: "${name}PrivateSubnet", VpcId: vpcId, CidrBlock: "10.0.11.0/24", AvailabilityZone: "ap-northeast-1a"
                routeTable id: "PublicRouteTable", VpcId: vpcId
                routeTable id: "PrivateRouteTable", VpcId: vpcId
                route id: "PublicRoute", RouteTableId: [Ref: "PublicRouteTable"], DestinationCidrBlock: "0.0.0.0/0", GatewayId: [Ref: "InternetGateway"]
                subnetRouteTableAssociation id: "SubnetRouteTableAssociationPublicSubnet", SubnetId: [Ref: "${name}PublicSubnet"], RouteTableId: [Ref: "PublicRouteTable"]
                subnetRouteTableAssociation id: "SubnetRouteTableAssociationPrivateSubnet", SubnetId: [Ref: "${name}PrivateSubnet"], RouteTableId: [Ref: "PublicRouteTable"]
            } else {// single AZ | public
                vpc id: "${name}VPC", CidrBlock: cidrBlock
                internetGateway id: "InternetGateway"
                vpcGatewayAttachment id: "InternetGatewayAttach", VpcId: vpcId, InternetGatewayId: [Ref: "InternetGateway"]
                subnet id: "${name}Subnet", VpcId: vpcId, CidrBlock: "10.0.0.0/24"
                routeTable id: "PublicRouteTable", VpcId: vpcId
                route id: "PublicRoute", RouteTableId: [Ref: "PublicRouteTable"], DestinationCidrBlock: "0.0.0.0/0", GatewayId: [Ref: "InternetGateway"]
                subnetRouteTableAssociation id: "SubnetRouteTableAssociation", SubnetId: [Ref: "${name}Subnet"], RouteTableId: [Ref: "PublicRouteTable"]
            }

        }
    }

    void vpc(Map params) {
        this.resources << VPC.newInstance(params)
    }

    void internetGateway(Map params) {
        this.resources << InternetGateway.newInstance(params)
    }

    void vpcGatewayAttachment(Map params) {
        this.resources << VPCGatewayAttachment.newInstance(params)
    }

    void subnet(Map params) {
        this.resources << Subnet.newInstance(params)
    }

    void routeTable(Map params) {
        this.resources << RouteTable.newInstance(params)
    }

    void route(Map params) {
        this.resources << Route.newInstance(params)
    }

    void subnetRouteTableAssociation(Map params) {
        this.resources << SubnetRouteTableAssociation.newInstance(params)
    }

    void securityGroup(Map params, Closure cl) {
        def securityGroup = SecurityGroup.newInstance(params)
        cl.delegate = new SecurityGroupDelegate(securityGroup)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        this.resources << securityGroup
    }

    void volume(Map params) {
        this.resources << new Volume(convert(params))
    }

    void instance(Map params) {
        this.resources << Instance.newInstance(params)
    }

    void instance(Map params, Closure cl) {
        def instance = new Instance(convert(params))
        cl.delegate = new InstanceDelegate(instance)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        this.resources << instance
    }

    void eip(Map params) {
        this.resources << EIP.newInstance(params)
    }

    void role(Map params) {
        this.resources << Role.newInstance(params)
    }

    void policy(Map params) {
        this.resources << Policy.newInstance(params)
    }

    void instanceProfile(Map params) {
        this.resources << InstanceProfile.newInstance(params)
    }

    void dbSubnetGroup(Map params) {
        this.resources << new DBSubnetGroup(convert(params))
    }

    void dbInstance(Map params) {
        this.resources << new DBInstance(convert(params))
    }

    void elasticLoadBalancing(Map params) {
        this.resources << new ElasticLoadBalancing(convert(params))
    }

    void elasticLoadBalancing(Map params, Closure cl) {
        def elasticLoadBalancing = new ElasticLoadBalancing(convert(params))
        cl.delegate = new ElasticLoadBalancingDelegate(elasticLoadBalancing)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        this.resources << elasticLoadBalancing
    }

    void waitConditionHandle(Map params) {
        this.resources << WaitConditionHandle.newInstance(params)
    }

    void waitCondition(Map params) {
        this.resources << WaitCondition.newInstance(params)
    }

    void hostedZone(Map params) {
        this.resources << HostedZone.newInstance(params)
    }

    void hostedZone(Map params, Closure cl) {
        def hostedZone = HostedZone.newInstance(params)
        cl.delegate = new HostedZoneDelegate(hostedZone)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        this.resources << hostedZone
    }

    void recordSet(Map params) {
        this.resources << RecordSet.newInstance(params)
    }
}
