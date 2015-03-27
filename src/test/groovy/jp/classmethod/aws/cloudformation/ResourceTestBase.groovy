package jp.classmethod.aws.cloudformation

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by watanabeshuji on 2015/03/28.
 */
class ResourceTestBase {

    Path getPath(String resource) {
        Paths.get(getClass().getResource(resource).getPath())
    }

}
