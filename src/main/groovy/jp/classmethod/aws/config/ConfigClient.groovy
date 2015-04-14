package jp.classmethod.aws.config

import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.config.AmazonConfig
import com.amazonaws.services.config.AmazonConfigClient
import com.amazonaws.services.config.model.GetResourceConfigHistoryRequest
import com.amazonaws.services.config.model.ResourceType
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest
import com.amazonaws.services.ec2.model.DescribeSubnetsRequest

/**
 * Created by watanabeshuji on 2015/04/08.
 */
class ConfigClient {
    AmazonConfig client
    AmazonEC2Client ec2Client

    def ConfigClient() {
        client = Region.getRegion(Regions.fromName("ap-northeast-1"))
            .createClient(AmazonConfigClient, null, null)
        ec2Client = Region.getRegion(Regions.fromName("ap-northeast-1"))
            .createClient(AmazonEC2Client, null, null)
    }

    def getRelationshipsToVPC(String vpcId) {
        def req = new GetResourceConfigHistoryRequest()
            .withResourceType(ResourceType.AWSEC2VPC)
            .withResourceId(vpcId)
        def res = client.getResourceConfigHistory(req)
        def result = [
            Subnet: [:],
            SecurityGroup: [:],
        ]
        res.configurationItems.each {
            it.relationships.each {
                switch (it.resourceType) {
                    case ResourceType.AWSEC2Subnet.toString():
                        def r = ec2Client.describeSubnets(new DescribeSubnetsRequest().withSubnetIds(it.resourceId))
                        def name = r.subnets[0].tags.find { it.key == "Name"}.value
                        result['Subnet'][name] = it.resourceId
                        break
                    case ResourceType.AWSEC2SecurityGroup.toString():
                        def r = ec2Client.describeSecurityGroups(new DescribeSecurityGroupsRequest().withGroupIds(it.resourceId))
                        def name = r.securityGroups[0].groupName
                        result['SecurityGroup'][name] = it.resourceId
                        break
                    default:
                        break
                }
            }
        }
        result
    }


    public static void main(String[] args) {
        def client = new ConfigClient()
        def relationships = client.getRelationshipsToVPC("vpc-aa61aecf")
        println relationships
    }
}
