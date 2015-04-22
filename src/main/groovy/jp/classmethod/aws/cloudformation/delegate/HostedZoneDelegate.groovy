package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.route53.HostedZone

/**
 * Created by watanabeshuji on 2015/03/27.
 */
class HostedZoneDelegate {

    HostedZone hostedZone

    HostedZoneDelegate(HostedZone hostedZone) {
        this.hostedZone = hostedZone
    }

    void hostedZoneConfig(Map params) {
        this.hostedZone.HostedZoneConfig = new HostedZone.HostedZoneConfig(params)
    }

}
