package jp.classmethod.aws.cloudformation.testing

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by watanabeshuji on 2015/03/28.
 */
class TestSupport {

    static Path getPath(String resource) {
        Paths.get(getClass().getResource(resource).getPath())
    }

}
