# CloudFormation Template Builder

## これは何？
CSV形式びCloudFormationの定義ファイルをJSONに整形するGradleのPluginです。

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
defaultTasks 'generateTemplate'

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
-printTemplateJSON=[true|false]  標準出力にJSONを表示したくない場合はfalseを指定してください（デフォルト: true）




