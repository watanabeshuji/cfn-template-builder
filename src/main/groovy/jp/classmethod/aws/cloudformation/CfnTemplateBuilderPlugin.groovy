package jp.classmethod.aws.cloudformation

import jp.classmethod.aws.cloudformation.ec2.InternetGateway
import jp.classmethod.aws.cloudformation.ec2.Route
import jp.classmethod.aws.cloudformation.ec2.RouteTable
import jp.classmethod.aws.cloudformation.ec2.Subnet
import jp.classmethod.aws.cloudformation.ec2.SubnetRouteTableAssociation
import jp.classmethod.aws.cloudformation.ec2.VPC
import jp.classmethod.aws.cloudformation.ec2.VPCGatewayAttachment
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by watanabeshuji on 2014/09/09.
 */
class CfnTemplateBuilderPlugin implements Plugin<Project> {

    static final String TASK_NAME = 'AWS tasks'

    void apply(Project project) {
        project.ext.cfnDir = (project.hasProperty('cfnDir')) ? project.getProperty('cfnDir') : "./cfn"
        project.ext.cfnType = (project.hasProperty('cfnType')) ? project.getProperty('cfnType') : ""
        def dir = project.ext.cfnDir
        def type = project.ext.cfnType
        def output = (project.hasProperty('output')) ? project.getProperty('output') as Boolean : true
        def dryRun = (project.hasProperty('dryRun')) ? project.getProperty('dryRun') as Boolean : false
        project.task('cfnInit') << {
            println 'CloudFormation Builder'
            def cfnDir = project.ext.cfnDir
            if (Paths.get(cfnDir).toFile().exists()) {
                println ""
                println "Sorry!!"
                println "Already exist cfn directory: " + cfnDir
                return
            }
            def out = Paths.get(cfnDir, 'cfn.groovy')
            Files.createDirectory(Paths.get(cfnDir))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/templates/cfn.groovy'), out)
            println "create CloudFormation DSL file: $out"
        }
        project.task('cfnValidate') << {
            println 'CloudFormation Builder'
            println 'Validate DSL as template....'
            CloudFormation cfn = load(dir)
            validateTemplate(cfn)
            println "Success!"
        }
        project.task('cfnBuild') << {
            println 'CloudFormation Builder'
            CloudFormation cfn = load(dir)
            build(cfn, output, dir)
        }
        project.task('cfnClean') << {
            println 'CloudFormation Builder'
            println 'Cleanup cfn directory....'
            def cfnDir = project.ext.cfnDir
            Paths.get(cfnDir).toFile().deleteDir()
        }
        project.task('cfnHelp') << {
            println 'CloudFormation Builder'
            help(type)
        }
        project.tasks.cfnInit.group = TASK_NAME
        project.tasks.cfnClean.group = TASK_NAME
        project.tasks.cfnValidate.group = TASK_NAME
        project.tasks.cfnBuild.group = TASK_NAME
        project.tasks.cfnHelp.group = TASK_NAME
        project.tasks.cfnInit.description = "Initialize cfn-template-builder. Create cfn directory. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnValidate.description = "Validate CloudFormation DSL. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnBuild.description = "Build CloudFormation DSL. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnClean.description = "Cleanup cfn directory. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnHelp.description = "Show documents and samples. Option: -PcfnType=[Type]."
    }

    def CloudFormation load(String dir) {
        println "Load from $dir"
        CloudFormation.load(Paths.get(dir, "cfn.groovy"))
    }

    def validateTemplate(CloudFormation cfn) {
        CloudFormationClient client = new CloudFormationClient()
        client.validateTemplate(cfn.toString())
    }

    def build(CloudFormation cfn, output, dir) {
        cfn.doValidate()
        def json = cfn.toPrettyString()
        if (output) println json
        def out = new File(dir, "cfn.template")
        out.write(json)
        println "File generated:  ${out.absolutePath}"
        println "Name\tType"
        cfn.resourcesSummary.each { println "${it.Name}\t${it.Type}" }
    }

    def help(String type) {
        println "------"
        switch (type) {
            case "EC2::VPC":
                println VPC.DESC
                break
            case "EC2::InternetGateway":
                println InternetGateway.DESC
                break
            case "EC2::VPCGatewayAttachment":
                println VPCGatewayAttachment.DESC
                break
            case "EC2::Subnet":
                println Subnet.DESC
                break
            case "EC2::RouteTable":
                println RouteTable.DESC
                break
            case "EC2::Route":
                println Route.DESC
                break
            case "EC2::SubnetRouteTableAssociation":
                println SubnetRouteTableAssociation.DESC
                break
            default:
                println '''\
[Usage]
gradle -PcfnType=[Type] cfnHelp
Type list
- EC2::VPC
- EC2::InternetGateway
- EC2::VPCGatewayAttachment
- EC2::Subnet
- EC2::RouteTable
- EC2::Route
- EC2::SubnetRouteTableAssociation
'''
                break
        }
    }
}

