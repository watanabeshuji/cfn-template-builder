package jp.classmethod.aws.cloudformation

/**
 * Created by watanabeshuji on 2015/03/30.
 */
class InvalidResourceException extends Exception {

    def InvalidResourceException(String msg) {
        super(msg)
    }
}
