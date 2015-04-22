package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.route53.RecordSet

/**
 * Created by watanabeshuji on 2015/04/22.
 */
class RecordSetDelegate {

    RecordSet recordSet

    RecordSetDelegate(RecordSet recordSet) {
        this.recordSet = recordSet
    }

    void aliasTarget(Map params) {
        this.recordSet.AliasTarget = RecordSet.AliasTarget.newInstance(params)
    }
}
