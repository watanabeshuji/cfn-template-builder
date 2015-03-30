package jp.classmethod.aws.cloudformation

import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient
import com.amazonaws.services.cloudformation.model.ValidateTemplateRequest

/**
 * Created by watanabeshuji on 2015/03/31.
 */
class CloudFormationClient {
    AmazonCloudFormation cfn

    def CloudFormationClient() {
        cfn = new AmazonCloudFormationClient()
    }

    def validateTemplate(String templateBody) {
        ValidateTemplateRequest req = new ValidateTemplateRequest()
        cfn.validateTemplate(req.withTemplateBody(templateBody))
    }
}
