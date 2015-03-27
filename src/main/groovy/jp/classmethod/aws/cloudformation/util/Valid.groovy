package jp.classmethod.aws.cloudformation.util
/**
 * Created by watanabeshuji on 2015/03/26.
 */
class Valid {
    static void logicalId(String value) {
        if (value == null) throw new ValidErrorException("Logical ID can't be null.")
        if (!(value ==~ /[a-zA-Z0-9]+/)) throw new ValidErrorException("The logical ID must be alphanumeric (A-Za-z0-9): " + value)
    }

    static void require(String name, Object value) {
        if (value == null) throw new ValidErrorException("$name can't be null.")
    }

    static void bool(String name, Object value) {
        if (value == null) return
        if (!(value instanceof Boolean)) throw new ValidErrorException("$name must be boolean value (or maybe null).")
    }
}

