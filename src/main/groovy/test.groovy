/**
 * Created by watanabeshuji on 2015/04/14.
 */


def shell = new GroovyShell()
def script = shell.parse("println env")
script.setBinding(new Binding([env: "dev"]))
script.run()


