package jp.classmethod.aws.cloudformation.util
/**
 * Created by watanabeshuji on 2015/03/26.
 */
class Valid {

    static void checkKeys(String type, Map params, List supportedKeys) {
        def unsupportedKeys = new HashSet<>(params.keySet())
        unsupportedKeys.removeAll(supportedKeys)
        if (!unsupportedKeys.isEmpty()) {
            throw new ValidErrorException("$type not support parameters: ${unsupportedKeys}\n Supported keys: ${supportedKeys}")
        }
    }

    static void logicalId(String type, Map params) {
        if (params['id'] == null) throw new ValidErrorException("${type}[id] can't be null" + params)
        if (!(params['id'] ==~ /[a-zA-Z0-9]+/)) throw new ValidErrorException("${type}[id] must be alphanumeric (A-Za-z0-9): ${params['id']}")
    }

    static void require(String type, String name, Map params) {
        def value = params[name]
        if (value == null) throw new ValidErrorException("${type}[$name] can't be null.")
    }

    static void bool(String type, String name,  Map params) {
        def value = params[name]
        if (value == null) return
        if (!(value instanceof Boolean)) throw new ValidErrorException("${type}[$name] must be boolean value (or maybe null).")
    }
}

