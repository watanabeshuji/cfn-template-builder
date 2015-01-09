package jp.classmethod.aws.cloudformation

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.internal.FileUtils

import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

/**
 * Created by watanabeshuji on 2014/09/09.
 */
class CfnTemplateBuilderPlugin implements Plugin<Project> {

    static final String TASK_NAME = 'AWS tasks'

    void apply(Project project) {
        project.ext.cfnDir = (project.hasProperty('cfnDir')) ? project.getProperty('cfnDir') : "./cfn"
        project.task('cfnInit') << {
            println 'CloudFormation Builder'
            def cfnDir = project.ext.cfnDir
            if (Paths.get(cfnDir).toFile().exists()) {
                println ""
                println "Sorry!!"
                println "Already exist cfn directory: " + cfnDir
                return
            }
            Files.createDirectory(Paths.get(cfnDir))
            Files.createDirectory(Paths.get(cfnDir, 'Mappings'))
            Files.createDirectory(Paths.get(cfnDir, 'userdata'))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/templates/Meta.txt'), Paths.get(cfnDir, 'Meta.txt'))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/templates/Mappings/AMI'), Paths.get(cfnDir, 'Mappings', 'AMI'))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/templates/Mappings/Common'), Paths.get(cfnDir, 'Mappings', 'Common'))
        }
        project.task('cfnNew') << {
            println 'CloudFormation Builder'
            println 'Create new CSV files....'
            def cfnDir = project.ext.cfnDir
            def cfnType = getProjectProperty(project, 'cfnType', 'ALL')
            createTemplateFile(cfnDir, cfnType)
        }
        project.task('cfnBuild') << {
            println 'CloudFormation Builder'
            def dir = project.ext.cfnDir
            def output = (project.hasProperty('output')) ? project.getProperty('output') as Boolean : true
            def dryRun = (project.hasProperty('dryRun')) ? project.getProperty('dryRun') as Boolean : false
            println "Load from $dir"
            def template = Template.build(dir)
            if (output) println template.toPrettyString()
            def out = new File(dir, "cfn.template")
            out.write(template.toPrettyString())
            println "File generated:  ${out.absolutePath}"
        }
        project.task('cfnClean') << {
            println 'CloudFormation Builder'
            println 'Cleanup cfn directory....'
            def cfnDir = project.ext.cfnDir
            Paths.get(cfnDir).toFile().deleteDir()
        }
        project.task('cfnDeploy') << {
            println 'CloudFormation Builder'
            def templateFile = Paths.get(project.ext.cfnDir, "cfn.template")
            if (!project.hasProperty('cfnStackName')) {
                throw new GradleException("you must set option 'cfnStackName': such as -PcfnStackName=test")
            }
            def stackName = project.getProperty('cfnStackName')
            new CfnClient().create(stackName, templateFile)
        }
        project.task('generateTemplate') << {
            println 'CloudFormation Builder'
            println "Deprecated task. Please use 'cfnBuild' task"
        }
        // Packer tasks TODO 後でプラグインを分離すべき
        project.ext.ami = (project.hasProperty('ami')) ? project.getProperty('ami') : "Example"
        project.ext.amiDir = (project.hasProperty('amiDir')) ? project.getProperty('amiDir') : "./ami"
        project.ext.amiPlaybook = (project.hasProperty('amiPlaybook')) ? project.getProperty('amiPlaybook') : "setup"
        project.task('amiInit') << {
            println 'CloudFormation Builder'
            def ami = project.ext.ami
            def amiDir = project.ext.amiDir
            def amiPlaybook = project.ext.amiPlaybook
            if (Files.exists(Paths.get(amiDir, "${ami}.json"))) {
                println ""
                println "Sorry!!"
                println "Already exist ami: ${amiDir}/${ami}.json"
                return
            }
            if (!Files.exists(Paths.get(amiDir))) {
                Files.createDirectory(Paths.get(amiDir))
                Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/ami/variables.json.sample'), Paths.get(amiDir, 'variables.json.sample'))
                Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/ami/variables.json.sample'), Paths.get(amiDir, 'variables.json'))
            }
            def uri = CfnTemplateBuilderPlugin.class.getResource("/ami/Example.json").toURI()
            FileSystems.newFileSystem(uri, ["create": "true"]);
            def lines = Files.lines(Paths.get(uri)).map {
                it.replaceAll(/\[AMI_NAME\]/, ami).replaceAll(/\[AMI_PLAYBOOK\]/, amiPlaybook)
            }.collect(Collectors.toList())
            Files.write(Paths.get(amiDir, "${ami}.json"), lines)
            Files.createDirectory(Paths.get(amiDir, ami))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream("/ami/ansible/${amiPlaybook}.yml"), Paths.get(amiDir, ami, "${amiPlaybook}.yml"))
        }
        project.task('amiPlaybook') << {
            println 'CloudFormation Builder'
            def ami = project.ext.ami
            def amiDir = project.ext.amiDir
            def amiPlaybook = project.ext.amiPlaybook
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream("/ami/ansible/${amiPlaybook}.yml"), Paths.get(amiDir, ami, "${amiPlaybook}.yml"))
        }
        project.task('amiValid', type: Exec) {
            def ami = project.ext.ami
            def commands = ['packer', 'validate', '--var-file=variables.json', "${ami}.json"]
//            println 'CloudFormation Builder'
//            println 'Validate ami'
//            println 'Execute: ' + commands.join(' ')
            workingDir './ami'
            commandLine commands
        }
        project.task('amiBuild', type: Exec) {
            def ami = project.ext.ami
            def commands = ['packer', 'build', '--var-file=variables.json', "${ami}.json"]
//            println 'CloudFormation Builder'
//            println 'Build ami'
//            println 'Execute: ' + commands.join(' ')
            workingDir './ami'
            commandLine commands
        }
        project.task('amiClean') << {
            println 'CloudFormation Builder'
            println 'Cleanup ami directory....'
            def amiDir = project.ext.amiDir
            Paths.get(amiDir).toFile().deleteDir()
        }
        project.tasks.cfnInit.group = TASK_NAME
        project.tasks.cfnNew.group = TASK_NAME
        project.tasks.cfnClean.group = TASK_NAME
        project.tasks.cfnBuild.group = TASK_NAME
        project.tasks.generateTemplate.group = TASK_NAME
        project.tasks.cfnInit.description = "Initialize cfn-template-builder. Create cfn directory. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnNew.description = "Create new cfn-template-builder file. Option: -PcfnType=(VPC|InternetGateway|Routing|SecurityGroup|EC2|ALL) -PcfnDir=[cfnDir]."
        project.tasks.cfnBuild.description = "Build CloudFormation Template file. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnClean.description = "Cleanup cfn directory. Option: -PcfnDir=[cfnDir]."
        project.tasks.generateTemplate.description = "Deprecated task. Please use 'cfnBuild' task"
        project.tasks.amiInit.group = TASK_NAME
        project.tasks.amiPlaybook.group = TASK_NAME
        project.tasks.amiValid.group = TASK_NAME
        project.tasks.amiBuild.group = TASK_NAME
        project.tasks.amiClean.group = TASK_NAME
        project.tasks.amiInit.description = "Initialize cfn-template-builder. Create ami directory. Option: -PamiDir=[amiDir] -Pami=[ami] -PamiPlaybook=[amiPlaybook]."
        project.tasks.amiPlaybook.description = "Add playbook to ami.  Option: -PamiDir=[amiDir] -Pami=[ami] -PamiPlaybook=[amiPlaybook]."
        project.tasks.amiValid.description = "Validate ami configuration.  Option: -PamiDir=[amiDir] -Pami=[ami] -PamiPlaybook=[amiPlaybook]."
        project.tasks.amiBuild.description = "Build ami.  Option: -PamiDir=[amiDir] -Pami=[ami] -PamiPlaybook=[amiPlaybook]."
        project.tasks.amiClean.description = "Cleanup ami directory.  Option: -PamiDir=[amiDir] -Pami=[ami] -PamiPlaybook=[amiPlaybook]."
    }

    private getProjectProperty(Project project, String propertyName, defaultValue) {
        (project.hasProperty(propertyName)) ? project.getProperty(propertyName) : defaultValue
    }

    private createTemplateFile(String cfnDir, String cfnType) {
        println "createTemplateFile... $cfnType"
        switch (cfnType) {
            case 'VPC':
                copyTemplateFile('/templates/VPC/default.csv', Paths.get(cfnDir, '00_VPC.csv'))
                break
            case 'InternetGateway':
                copyTemplateFile('/templates/InternetGateway/default.csv', Paths.get(cfnDir, '00_InternetGateway.csv'))
                break
            case 'Subnet':
                copyTemplateFile('/templates/Subnet/default.csv', Paths.get(cfnDir, '00_Subnet.csv'))
                break
            case 'Routing':
                copyTemplateFile('/templates/Routing/RouteTable_default.csv', Paths.get(cfnDir, '00_RouteTable.csv'))
                copyTemplateFile('/templates/Routing/Route_default.csv', Paths.get(cfnDir, '00_Route.csv'))
                copyTemplateFile('/templates/Routing/SubnetRouteTableAssociation_default.csv', Paths.get(cfnDir, '00_SubnetRouteTableAssociation.csv'))
                break
            case 'SecurityGroup':
                copyTemplateFile('/templates/SecurityGroup/default.csv', Paths.get(cfnDir, '00_SecurityGroup.csv'))
                break
            case 'EC2':
                copyTemplateFile('/templates/EC2/default.csv', Paths.get(cfnDir, '00_EC2.csv'))
                break
            case 'RDS':
                copyTemplateFile('/templates/RDS/default_DBSubnetGroup.csv', Paths.get(cfnDir, '00_DBSubnetGroup.csv'))
                copyTemplateFile('/templates/RDS/default_DBInstance.csv', Paths.get(cfnDir, '00_DBInstance.csv'))
                break
            case 'SimplePublicVPC':
                [
                        ['/templates/simplepublicvpc/01_VPC.csv', '01_VPC.csv'],
                        ['/templates/simplepublicvpc/02_InternetGateway.csv', '02_InternetGateway.csv'],
                        ['/templates/simplepublicvpc/03_Subnet.csv', '03_Subnet.csv'],
                        ['/templates/simplepublicvpc/04_RouteTable.csv', '04_RouteTable.csv'],
                        ['/templates/simplepublicvpc/05_Route.csv', '05_Route.csv'],
                        ['/templates/simplepublicvpc/06_SubnetRouteTableAssociation.csv', '06_SubnetRouteTableAssociation.csv'],
                        ['/templates/simplepublicvpc/07_SecurityGroup.csv', '07_SecurityGroup.csv'],
                ].each { copyTemplateFile(it[0], Paths.get(cfnDir, it[1]))}
                break
            case 'ALL':
                [
                    ['/templates/VPC/default.csv', '01_VPC.csv'],
                    ['/templates/InternetGateway/default.csv', '02_InternetGateway.csv'],
                    ['/templates/Subnet/default.csv', '03_Subnet.csv'],
                    ['/templates/Routing/RouteTable_default.csv', '04_RouteTable.csv'],
                    ['/templates/Routing/Route_default.csv', '05_Route.csv'],
                    ['/templates/Routing/SubnetRouteTableAssociation_default.csv', '06_SubnetRouteTableAssociation.csv'],
                    ['/templates/SecurityGroup/default.csv', '07_SecurityGroup.csv'],
                    ['/templates/EC2/default.csv', '11_Instance.csv'],
                    ['/templates/RDS/default_DBSubnetGroup.csv', '21_DBSubnetGroup.csv'],
                    ['/templates/RDS/default_DBInstance.csv', '22_DBInstance.csv'],
                ].each { copyTemplateFile(it[0], Paths.get(cfnDir, it[1]))}
                break
        }
    }

    private copyTemplateFile(String resource, Path path) {
        if (path.toFile().exists()) {
            println "Already file exist: $path"
            return
        }
        Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream(resource), path)
        println "Create template file: $path"
    }
}

