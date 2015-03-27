package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.ec2.Instance

/**
 * Created by watanabeshuji on 2015/03/27.
 */
class BlockDeviceMappingDelegate {
    Instance.BlockDeviceMapping blockDeviceMapping

    BlockDeviceMappingDelegate(Instance.BlockDeviceMapping blockDeviceMapping) {
        this.blockDeviceMapping = blockDeviceMapping
    }

    void ebs(Map params) {
        this.blockDeviceMapping.Ebs = new Instance.Ebs(params)
    }

}
