package jp.classmethod.aws.cloudformation

import jp.classmethod.aws.cloudformation.cloudformation.WaitCondition
import jp.classmethod.aws.cloudformation.cloudformation.WaitConditionHandle
import jp.classmethod.aws.cloudformation.ec2.*
import jp.classmethod.aws.config.ConfigClient
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by watanabeshuji on 2014/09/09.
 */
class CfnTemplateBuilderPlugin implements Plugin<Project> {

    static final String TASK_NAME = 'AWS tasks'
    static final String BASE_MESSAGE = '''\
CloudFormation Template Builder
'''

    void apply(Project project) {
        initProject(project)
        project.task('cfnValidate') << {
            println BASE_MESSAGE
            println 'Validate templates....'
            doValidate(project)
        }
        project.task('cfnBuild') << {
            println BASE_MESSAGE
            println 'Build templates....'
            doBuild(project)
        }
        project.task('cfnRelationships') << {
            println BASE_MESSAGE
            def vpcId = project.getProperty('vpcId')
            relationships(vpcId)
        }
        project.task('cfnHelp') << {
            println BASE_MESSAGE
            doHelp(project)
        }
        project.tasks.cfnValidate.group = TASK_NAME
        project.tasks.cfnBuild.group = TASK_NAME
        project.tasks.cfnRelationships.group = TASK_NAME
        project.tasks.cfnHelp.group = TASK_NAME
        project.tasks.cfnValidate.description = "Validate CloudFormation templates."
        project.tasks.cfnBuild.description = "Build CloudFormation templates."
        project.tasks.cfnRelationships.description = "TODO "
        project.tasks.cfnHelp.description = "Show documents and samples. Option: -PcfnType=[Type]."
    }

    def initProject(Project project) {
        project.extensions.create("cfn", CfnTemplateBuilderPluginExtention)
    }

    def getCfnDir(Project project) {
        if (project.hasProperty('cfnDir')) {
            project.cfnDir
        } else {
            project.extensions.cfn.cfnDir
        }
    }

    def List<Path[]> getCfnTemplates(Project project) {
        def cfnDir = getCfnDir(project)
        def templates = project.hasProperty('cfnTemplate') ? [project.cfnTemplate] : project.extensions.cfn.cfnTemplates
        templates.collect {
            [Paths.get(cfnDir, "${it}.groovy"), Paths.get(cfnDir, "${it}.template")]
        }
    }

    def List<String> getEnvironments(Project project) {
        project.hasProperty('cfnEnvironment') ? [project.cfnEnvironment] : project.extensions.cfn.cfnEnvironments
    }

    def doValidate(Project project) {
        def environments = getEnvironments(project)
        getCfnTemplates(project).each { template ->
            if (environments != null && !environments.isEmpty()) {
                environments.each { env ->
                    CloudFormation cfn = load(template[0], [env: env])
                    validateTemplate(cfn)
                    println()
                }
            } else {
                CloudFormation cfn = load(template[0], [:])
                validateTemplate(cfn)
                println()
            }
        }
        println "Success!"
    }

    def doBuild(Project project) {
        def environments = getEnvironments(project)
        getCfnTemplates(project).each { template ->
            if (environments != null && !environments.isEmpty()) {
                mkEnvDirs(getCfnDir(project), environments)
                environments.each { env ->
                    CloudFormation cfn = load(template[0], [env: env])
                    build(cfn, template[1].parent.resolve(env).resolve(template[1].fileName))
                    println()
                }
            } else {
                CloudFormation cfn = load(template[0], [:])
                build(cfn, template[1])
                println()
            }
        }
        println "Success!"
    }

    def mkEnvDirs(String cfnDir, List<String> environments) {
        environments.each { env ->
            Path p = Paths.get(cfnDir, env)
            if (Files.notExists(p)) Files.createDirectory(p)
        }
    }

    def CloudFormation load(Path path, Map binding) {
        println "Load from $path"
        CloudFormation.load(path, binding)
    }

    def validateTemplate(CloudFormation cfn) {
        CloudFormationClient client = new CloudFormationClient()
        client.validateTemplate(cfn.toString())
    }

    def build(CloudFormation cfn, Path out) {
        cfn.doValidate()
        def json = cfn.toPrettyString()
        println json
        out.write(json)
        println "File generated:  ${out}"
        println "Name\tType"
        cfn.resourcesSummary.each { println "${it.Name}\t${it.Type}" }
    }

    def relationships(String vpcId) {
        def client = new ConfigClient()
        def relationships = client.getRelationshipsToVPC(vpcId)
        def resources = [:]
        println "mappings = ["
        println "    Resources: ["
        println "        SecurityGroup: ["
        relationships['SecurityGroup'].each { k, v ->
            print "\"$k\": \"$v\", "
        }
        println "],"
        println "        Subnet: ["
        relationships['Subnet'].each { k, v ->
            print "\"$k\": \"$v\", "
        }
        println "]"
        println "    ]"
        println "]"
    }

    def doHelp(Project project) {
        def type = project.hasProperty('cfnType') ? project.cfnType : ""
        println "------"
        switch (type) {
            case "VPC":
                println VPC.DESC
                break
            case "InternetGateway":
                println InternetGateway.DESC
                break
            case "VPCGatewayAttachment":
                println VPCGatewayAttachment.DESC
                break
            case "Subnet":
                println Subnet.DESC
                break
            case "RouteTable":
                println RouteTable.DESC
                break
            case "Route":
                println Route.DESC
                break
            case "SubnetRouteTableAssociation":
                println SubnetRouteTableAssociation.DESC
                break
            case "EIP":
                println EIP.DESC
                break
            case "Instance":
                println Instance.DESC
                break
            case "WaitCondition":
                println WaitCondition.DESC
                break
            case "WaitConditionHandle":
                println WaitConditionHandle.DESC
                break
            default:
                println '''\
[Usage]
gradle -PcfnType=[Type] cfnHelp
Type list
- VPC
- InternetGateway
- VPCGatewayAttachment
- Subnet
- RouteTable
- Route
- SubnetRouteTableAssociation
- EIP
- Instance
- WaitCondition
- WaitConditionHandle
'''
                break
        }
    }
}

