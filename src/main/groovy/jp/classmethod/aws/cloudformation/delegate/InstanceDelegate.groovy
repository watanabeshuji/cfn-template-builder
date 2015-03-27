package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.ec2.Instance

/**
 * Created by watanabeshuji on 2015/03/27.
 */
class InstanceDelegate {

    Instance instance

    InstanceDelegate(Instance instance) {
        this.instance = instance
    }

    void volume(Map params) {
        this.instance.Volumes << new Instance.Volume(params)
    }

    void blockDeviceMapping(Map params) {
        this.instance.BlockDeviceMappings << new Instance.BlockDeviceMapping(params)
    }

    void blockDeviceMapping(Map params, Closure cl) {
        def blockDeviceMapping = new Instance.BlockDeviceMapping(params)
        cl.delegate = new BlockDeviceMappingDelegate(blockDeviceMapping)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        this.instance.BlockDeviceMappings << blockDeviceMapping
    }
}
