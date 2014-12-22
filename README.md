# CloudFormation Template Builder

## これは何？
CSV形式びCloudFormationの定義ファイルをJSONに整形するGradleのPluginです。

## Quick Start
build.gradle を作成します。
```groovy
apply plugin: 'cfn-template-builder'

buildscript {
    repositories {
        mavenCentral()
        maven {
            url "http://watanabeshuji.github.io/cfn-template-builder-repo"
        }
    }
    dependencies {
        classpath group: 'jp.classmethod.aws', name: 'cfn-template-builder', version: '+'
    }
}
```

cfnInitタスクを実行します。
```
$ gradle cfnInit
```
cfnディレクトリが作成され、最小限のファイルとMappingsのサンプルファイルが作成されます。

cfnBuildタスクを実行し、テンプレートファイルを作成します。
```
$ gradle cfnBuild
```
cfn.template がテンプレートファイルです。

cfnNewタスクを実行し、デフォルト構成を追加します。
```
$ gradle cfnNew
```
作成可能なテンプレートはオプションで指定可能です。

再度、cfnBuildタスクを実行し、テンプレートファイルを再生成します。
```
$ gradle cfnBuild
```

## タスク
Key          |Desc                           
-----------  |-------------------------------
cfnClean     |cfnディレクトリを削除します                
cfnInit      |cfnディレクトリを作成し、最小限のファイルを作成します                
cfnNew       |テンプレートファイルを追加します。-PcfnTypeで追加する種別を個別に指定するか、指定しない場合はデフォルト構成を追加します
cfnBuild     |cfnテンプレートを作成します                


## 設定
Gradleプロジェクトを作成し、以下の内容に従ってプラグインを設定します。

### プラグインの追加
cfn-template-builderプラグインを追加します。


```groovy
apply plugin: 'cfn-template-builder'
```

### buildscriptの設定
buildscriptのクラスパスにcfn-template-builderとGroovyを追加します。
cfn-template-builderは、GitHub上でMavenリポジトリとして公開されているため、それを指定します。

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url "http://watanabeshuji.github.io/cfn-template-builder-repo"
        }
    }
    dependencies {
        classpath group: 'org.codehaus.groovy', name: 'groovy-all', version: '2.3.2'
        classpath group: 'jp.classmethod.aws', name: 'cfn-template-builder', version: '+'
    }
}
```

Versionは、常に最新版を利用したいのであれば+を、特定のバージョンを指定したいのであれば特定のバージョン（例: 0.1.1）を指定してください。

### build.gradleのサンプル

```groovy
apply plugin: 'cfn-template-builder'
apply plugin: 'idea'

version = '1.0'
defaultTasks 'cfnBuild'

buildscript {
    repositories {
        mavenCentral()
        maven {
            url "http://watanabeshuji.github.io/cfn-template-builder-repo"
        }
    }
    dependencies {
        classpath group: 'org.codehaus.groovy', name: 'groovy-all', version: '2.3.2'
        classpath group: 'jp.classmethod.aws', name: 'cfn-template-builder', version: '+'
    }
}
```

## 環境定義ファイル
環境定義ファイルは、CSV形式で、cfnディレクトリ以下に配置します。
ファイル名のフォーマットは、 XX_Instance.csv など、先頭に数字を付与したリソース名です。
各リソースで設定できる項目は、随時対応しています。

基本的にはサンプルに含まれていない項目は対応していません（PRください）。

### Meta.txt
Key=Valueの形式で記述します。
現在対応している項目は、Descriptionのみです。

Key          |Desc                           |Sample
-----------  |-------------------------------|----------------------------------
Description  |テンプレートの説明                |Template for VPC with NAT instance

Meta.txt
```
Description=Template for VPC with NAT instance
```

### Mappings
CfnのMappingsに対応。
Mappingsディレクトリを作成し、テキストファイルを配置する。

Mappings/Common
```
Key,            Name,           Value
KeyPair,        Ec2KeyName,     default-key
Role,           Ec2Role,        ec2-user
```

この場合、Common/KeyPair/Ec2KeyName = default-key というMappingが定義される。
各種リソースから、Mappingを参照する場合は、「Common:KeyPair:Ec2KeyName」のようにコロンで区切って指定すること。

### Parameters.csv
CfnのParametersに対応。

Parameters.csv
```
Name          ,Type     ,Default      ,AllowedValues      ,Description
InstanceType  ,String   ,t2.small     ,t2.small|m3.large  ,Instance type for EC2 Instance
Env           ,String   ,-            ,blue|green         ,Environment for deployment(Blue/Green)
```

各種リソースから、Parameters、「P[InstanceType]」のようにP[パラメータ名]の書式とすること。


### VPC
AWS::EC2::VPC リソースを定義します。

Key          |Required  |Properties                     |Desc
-------------|----------|-------------------------------|----------------------------------
Name         |YES       |Tags/Name                      |VPC名
CidrBlock    |YES       |CidrBlock                      |CIDR Block

XX_VPC.csv
```
Name,         CidrBlock
vpc,          10.0.0.0/16
```

template
```json
"Vpc": {
    "Type": "AWS::EC2::VPC",
    "Properties": {
        "CidrBlock": "10.0.0.0/16",
        "Tags": [
            {
                "Key": "Name",
                "Value": "vpc"
            },
            {
                "Key": "Application",
                "Value": {
                    "Ref": "AWS::StackId"
                }
            }
        ]
    }
}
```

### InternetGateway
TODO

### Subnet
TODO


### RouteTable
TODO

### Route
TODO

### SubnetRouteTableAssociation
TODO

### SecurityGroup
TODO

### Volume
TODO

### Instance
TODO


## 使い方
cfnディレクトリ下に設定ファイルを定義します。
書き方はなんとなく・・・で

対応しないパラメータがあると思うので、それは要望するかPRしてください。

./gradlew

### オプション

-PcfnDir=[ベースディレクトリ]  cfn以外のディレクトリを指定する場合はパラメータを指定してください。
-Poutput=[true|false]  標準出力にJSONを表示したくない場合はfalseを指定してください（デフォルト: true）




