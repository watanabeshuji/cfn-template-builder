package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.elasticloadbalancing.ElasticLoadBalancing

/**
 * Created by watanabeshuji on 2015/03/27.
 */
class ElasticLoadBalancingDelegate {

    ElasticLoadBalancing elasticLoadBalancing

    def ElasticLoadBalancingDelegate(ElasticLoadBalancing elasticLoadBalancing) {
        this.elasticLoadBalancing = elasticLoadBalancing
    }

    def listener(Map params) {
        this.elasticLoadBalancing.Listeners << new ElasticLoadBalancing.Listener(params)
    }

    def healthCheck(Map params) {
        this.elasticLoadBalancing.HealthCheck = new ElasticLoadBalancing.HealthCheck(params)
    }

    def accessLoggingPolicy(Map params) {
        this.elasticLoadBalancing.AccessLoggingPolicy = new ElasticLoadBalancing.AccessLoggingPolicy(params)
    }
}
