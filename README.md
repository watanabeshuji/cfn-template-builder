# CloudFormation Template Builder

- [Resources](docs/Resources.md)
- [Tips](docs/Tips.md)

## これは何？
Groovy DSLからCloudFormationの定義ファイルをJSONに整形するGradleのPluginです。

## 動作環境
- Java 1.8以上
- Gradle 2.3以上

## Quick Start
build.gradle を作成します。
```groovy
apply plugin: 'cfn-template-builder'

// setting for cfn-template-builder
cfn {
    // directory for DSL and template files [Default: cfn ]
//  cfnDir = "cfn"
    // DSL and template file names (as List) [Default: cfn ]
//  cfnTemplates = ['cfn']
//  cfnTemplates = ['vpc', 'servers']
    // enviroments 
//    cfnEnvironments = ['dev', 'stg', 'prd']
}

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

テンプレート用のディレクトリ（cfn）を作成し、DSLファイル（cfn.groovy）を作成します。
```groovy
cloudformation {
    description "a cloudformation template."
    resources {
        vpc id: 'VPC', CidrBlock: "10.0.0.0/16"
    }
}
```

cfnBuildタスクを実行し、テンプレートファイルを作成します。
```
$ gradle cfnBuild
```
cfn/cfn.template がテンプレートファイルです。

## タスク
Task         |Desc
-----------  |-------------------------------------------------------------------------------------------------------------------------
cfnBuild     |cfnテンプレートを作成します                
cfnValidate  |DLSのバリデーションを行います                
cfnHelp      |ヘルプを表示します                

### オプション
Gradleの環境変数としてオプションを指定。

Option       |Task                  |Desc
------------ |--------------------  |------------------------------------------------------------
cfnTemplate  |cfnBuild, cfnValidate |CloudFormation DSLの名称（build.gradleに定義されたリストから選択）
cfnDir       |cfnBuild, cfnValidate |CloudFormation DSLを配置するディレクトリ（build.gradleを上書き）
cfnType      |cfnHelp               |CloudFormation DSLの種別

### cfnType
- EC2::VPC
- EC2::InternetGateway
（追加中）

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
        classpath group: 'jp.classmethod.aws', name: 'cfn-template-builder', version: '+'
    }
}
```

Versionは、常に最新版を利用したいのであれば+を、特定のバージョンを指定したいのであれば特定のバージョン（例: 0.5.0）を指定してください。
なお、0.5.0以降とそれ以前ではフォーマットが全く異なり、一切の互換性はありません。

### build.gradleのサンプル

```groovy
apply plugin: 'cfn-template-builder'
apply plugin: 'idea'

version = '1.0'
defaultTasks 'cfnBuild'

// setting for cfn-template-builder
cfn {
    // directory for DSL and template files [Default: cfn ]
    cfnDir = "cfn"
    // DSL and template file names (as List) [Default: cfn ]
    cfnTemplates = ['vpc', 'servers']
}

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

## DSLファイル
DSLファイルは、groovy DSL形式で、cfnディレクトリ以下に配置します。
cfn-template-builderは、cfnディレクトリ以下のDSLを読み込みます。
この時、読み込むテンプレートファイル名はcfn.cfnTemplates に定義します（デフォルトはcfn）。

# DSL
cfn-template-builderはGroovy DSLで記述します。
したがって、Groovyで記述できる範囲で柔軟な記述や、分岐やループといったプログラム的な定義が可能です
（ただし、複雑なロジックは可読性を損ねるのでオススメしません）。

## Ref
参照（Ref）を指定する場合は、次のようにキーをRefとしたMapを指定します。
```groovy
cloudformation {
    resources {
        vpcGatewayAttachment id: "InternetGatewayAttach", VpcId: [Ref: "VPC"], InternetGatewayId: [Ref: "InternetGateway"]
    }
}
```
また、次のように文字列として"Ref:参照名"と記述した場合は、ビルド時に参照として展開します。
```groovy
cloudformation {
    resources {
        vpcGatewayAttachment id: "InternetGatewayAttach", VpcId: "Ref:VPC", InternetGatewayId: "Ref:InternetGateway"]
    }
}
```

## FindInMap
Mappingからの参照を得たい場合は、’Fn::FindInMap’をキーとしたMapを作成し、値に参照するMappingに対応するリストを指定します。
```groovy
cloudformation {
    resources {
        vpc id: "VPC", CidrBlock: ['Fn::FindInMap': ['Common', 'KeyPair', 'Ec2KeyName']]
    }
}
```

Refと同様に次のように記述できます。
```groovy
cloudformation {
    resources {
        vpc id: "VPC", CidrBlock: "FindInMap:AddressMap:IpRange:VPC"
    }
}
```


```groovy
cloudformation {
    resources {
        vpc id: "VPC", CidrBlock: "FindInMap:AddressMap:IpRange:VPC"
    }
}
```


## resources
resourcesブロックには、AWSのリソースを定義します。

基本フォーマットは、次のようにresourceブロックにリソース名と、パラメータを指定します。
```groovy
resources {
    [リソース名（メソッド）] id: "ResourceId"
}
```

### resourcesのインクルード
複数のテンプレートで共通して利用するセキュリティグループや定番パターンのテンプレートは、共通部分を抽出して外部定義することができます。
```groovy
resources "commonSecurityGroups.groovy"
```
ファイルの参照は、cfnディレクトリからの相対パスです。

絶対パスには対応していません。
絶対パスは、ライブラリのリソースを参照します。

## Groovy DSLの記法
Groovy DSLでは、ブロックはクロージャとして実行されます。
リソース名部分はメソッド呼び出し扱いとなり、<リソース名>メソッドが名前付き引数で呼び出されます。
名前付き引数は、Mapを渡していると考えれば良いでしょう。
複数のパラメータを渡す場合は、次のようにカンマ区切りで、Key:Valueを並べてください。

```groovy
resources {
    vpc id: "VPC", CidrBlock: "10.0.0.0/16"
}
```

省略されているメソッド呼び出しの括弧を記述すると、次のようになります。
```groovy
resources {
    vpc (id: "VPC", CidrBlock: "10.0.0.0/16")
}
```

Mapは省略された記法となっているので、次のように記述するのが省略しない形式です。
```groovy
resources {
    vpc ([id: "VPC", CidrBlock: "10.0.0.0/16"])
}
```

結局のところ、vpcメソッドにMapを渡せばいいので、次のように書いてもOKです。
```groovy
def vpcConf = [id: "VPC", CidrBlock: "10.0.0.0/16"]
resources {
    vpc vpcConf
}
```

一部の値を変数として展開することも容易です。
```groovy
def cidrBlock = "10.0.0.0/16"
resources {
    vpc id: "VPC", CidrBlock: cidrBlock
}
```
