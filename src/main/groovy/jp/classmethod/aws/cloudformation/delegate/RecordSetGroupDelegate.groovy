package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.route53.RecordSet
import jp.classmethod.aws.cloudformation.route53.RecordSetGroup

/**
 * Created by watanabeshuji on 2015/04/23.
 */
class RecordSetGroupDelegate {

    RecordSetGroup recordSetGroup

    RecordSetGroupDelegate(RecordSetGroup recordSetGroup) {
        this.recordSetGroup = recordSetGroup
    }

    void recordSets(Map params) {
        this.recordSetGroup.RecordSets << RecordSet.newInlineInstance(params)
    }

    void recordSets(Map params, Closure cl) {
        def recordSet = RecordSet.newInlineInstance(params)
        cl.delegate = new RecordSetDelegate(recordSet)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        this.recordSetGroup.RecordSets << recordSet
    }

}
