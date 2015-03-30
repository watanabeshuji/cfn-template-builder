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
        def vpcName = params.containsKey("name") ? params["name"] : "VPC"
        boolean multiAZ = params.containsKey("multiAZ") ? params["multiAZ"] : true
        boolean publicSubnetOnly = params.containsKey("publicSubnetOnly") ? params["multiAZ"] : false
        String cidrBlock = params.containsKey("cidrBlock") ? params["cidrBlock"] : "10.0.0.0/16"
        def vpcId = [Ref: vpcName]

        vpc id: vpcName, CidrBlock: cidrBlock
        internetGateway id: "InternetGateway"
        vpcGatewayAttachment id: "InternetGatewayAttach", VpcId: vpcId, InternetGatewayId: [Ref: "InternetGateway"]
        subnet id: "SubnetA", VpcId: vpcId, CidrBlock: "10.0.0.0/24", AvailabilityZone: "ap-northeast-1a"
        subnet id: "SubnetC", VpcId: vpcId, CidrBlock: "10.0.1.0/24", AvailabilityZone: "ap-northeast-1c"
        routeTable id: "PublicRouteTable",  VpcId: vpcId
        routeTable id: "PrivateRouteTable", VpcId: vpcId
        route id: "PublicRoute", RouteTableId: [Ref: "PublicRouteTable"], DestinationCidrBlock: "0.0.0.0/0", GatewayId: [Ref: "InternetGateway"]
        subnetRouteTableAssociation id: "SubnetRouteTableAssociationA", SubnetId: [Ref: "SubnetA"], RouteTableId: [Ref: "PublicRouteTable"]
        subnetRouteTableAssociation id: "SubnetRouteTableAssociationC", SubnetId: [Ref: "SubnetC"], RouteTableId: [Ref: "PublicRouteTable"]
    }

    void vpc(Map params) {
        this.resources << new VPC(convert(params))
    }

    void internetGateway(Map params) {
        this.resources << new InternetGateway(params)
    }

    void vpcGatewayAttachment(Map params) {
        this.resources << new VPCGatewayAttachment(params)
    }

    void subnet(Map params) {
        this.resources << new Subnet(params)
    }

    void routeTable(Map params) {
        this.resources << new RouteTable(params)
    }

    void route(Map params) {
        this.resources << new Route(params)
    }

    void subnetRouteTableAssociation(Map params) {
        this.resources << new SubnetRouteTableAssociation(params)
    }

    void securityGroup(Map params, Closure cl) {
        def securityGroup = new SecurityGroup(params)
        cl.delegate = new SecurityGroupDelegate(securityGroup)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        this.resources << securityGroup
    }

    void volume(Map params) {
        this.resources << new Volume(params)
    }

    void instance(Map params) {
        this.resources << new Instance(params)
    }

    void instance(Map params, Closure cl) {
        def instance = new Instance(params)
        cl.delegate = new InstanceDelegate(instance)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        this.resources << instance
    }

    void eip(Map params) {
        this.resources << new EIP(params)
    }

    void role(Map params) {
        this.resources << new Role(params)
    }

    void policy(Map params) {
        this.resources << new Policy(params)
    }

    void instanceProfile(Map params) {
        this.resources << new InstanceProfile(params)
    }

    void dbSubnetGroup(Map params) {
        this.resources << new DBSubnetGroup(params)
    }

    void dbInstance(Map params) {
        this.resources << new DBInstance(params)
    }

    void elasticLoadBalancing(Map params) {
        this.resources << new ElasticLoadBalancing(params)
    }

    void elasticLoadBalancing(Map params, Closure cl) {
        def elasticLoadBalancing = new ElasticLoadBalancing(params)
        cl.delegate = new ElasticLoadBalancingDelegate(elasticLoadBalancing)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        this.resources << elasticLoadBalancing
    }

    void waitConditionHandle(Map params) {
        this.resources << new WaitConditionHandle(params)
    }

    void waitCondition(Map params) {
        this.resources << new WaitCondition(params)
    }

    private Map convert(Map params) {
        params.each {k, v ->
            if (needsConvertFindMap(v)) params[k] = toFindMap(v)
        }
        params
    }

    private boolean needsConvertFindMap(v) {
        String.class.isInstance(v) && v.startsWith("FindInMap:")
    }

    private Map toFindMap(String v) {
        def keys = v.split(":")
        ["Fn::FindInMap": [keys[1], keys[2], keys[3]]]
    }
}
