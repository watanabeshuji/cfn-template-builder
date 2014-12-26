package jp.classmethod.aws.cloudformation

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.RegionUtils
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient
import com.amazonaws.services.cloudformation.model.CreateStackRequest

import java.nio.file.Files
import java.nio.file.Path


/**
 * Created by watanabeshuji on 2014/12/24.
 */
class CfnClient {

    void create(String stackName, Path templateFile) {
        def config = new Properties()
        config.load(new FileInputStream("aws.config"))
        def accessKey = config.getProperty("aws_access_key_id")
        def secretKey = config.getProperty("aws_secret_access_key")
        def credentials = new BasicAWSCredentials(accessKey, secretKey)
        def region = RegionUtils.getRegion(config.getProperty("region"))
        def client = new AmazonCloudFormationClient(credentials)
        client.setRegion(region)

        def request = new CreateStackRequest()
        request.setStackName(stackName)
        request.setTemplateBody(new String(Files.readAllBytes(templateFile)))
        def result = client.createStack(request)
        System.out.println(result)
    }
}
