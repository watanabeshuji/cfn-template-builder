package jp.classmethod.aws.cloudformation.util
/**
 * Created by watanabeshuji on 2015/03/26.
 */
class Valid {

    static void checkKeys(Class type, Map params, List supportedKeys) {
        def unsupportedKeys = new HashSet<>(params.keySet())
        unsupportedKeys.removeAll(supportedKeys)
        if (!unsupportedKeys.isEmpty()) {
            throw new ValidErrorException("""\
${type.TYPE} not support parameters: ${unsupportedKeys}
--
$type.DESC"
"""
            )
        }
    }

    static void logicalId(Class type, Map params) {
        if (params['id'] == null) throw new ValidErrorException("""\
${type.TYPE}[id] can't be null")
--
$type.DESC"
""")
        if (!(params['id'] ==~ /[a-zA-Z0-9]+/)) throw new ValidErrorException("""\
${type.TYPE}[id] must be alphanumeric (A-Za-z0-9): ${params['id']}
--
$type.DESC"
""")
    }

    static void require(Class type, List names,  Map params) {
        for (String name: names) require(type, name, params)
    }

    static void require(Class type, String name, Map params) {
        def value = params[name]
        if (value == null) throw new ValidErrorException("""\
${type.TYPE}[$name] can't be null.
--
$type.DESC"
""")
    }

    static void bool(Class type, List names,  Map params) {
        for (String name: names) bool(type, name, params)
    }

    static void bool(Class type, String name,  Map params) {
        def value = params[name]
        if (value == null) return

        if (!(value instanceof Boolean)) throw new ValidErrorException("""\
${type.TYPE}[$name] must be boolean value (or maybe null).
--
$type.DESC"
""")
    }
}

