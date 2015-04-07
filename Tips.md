# Tips

## Groovy全般

### 変数の参照
defキーワードで変数を定義し、DSL内で参照できる。
CloudFormationのMappingsを使うのと同様だが、テンプレートをビルドした時点で変数がインライン展開される。
```groovy
def vpcId = "Ref:VPC"
resources {
    subnet id: "SubnetA", VpcId: vpcId, CidrBlock: "10.0.0.0/24", AvailabilityZone: "ap-northeast-1a"
    subnet id: "SubnetC", VpcId: vpcId, CidrBlock: "10.0.1.0/24", AvailabilityZone: "ap-northeast-1c"
}
```


## 省略記法
### Ref
```groovy
"Ref:VPC" >> [Ref: "VPC"]
```
```json
{  "Ref": "VPC" }
```

### FindInMap
```groovy
"FindInMap:AddressMap:IpRange:VPC" >> ["Fn::FindInMap": ["AddressMap", "IpRange", "VPC"]]
```
```json
{  "Fn::FindInMap": [ "AddressMap", "IpRange", "VPC"] }
```

## EC2:Instance

### UserData
UserDataのスクリプトは内部展開を行うため、複数行テキストとしてDSLに定義できる。
```groovy
def userData = """\
#! /bin/sh

yum -y update
"""
resources {
    instance id: "Web", ImageId: "ami-a003a8a1", UserData: userData
}
```
```json
"Web": {
    "Type": "AWS::EC2::Instance",
    "Properties": {
        "ImageId": "ami-a003a8a1",
        "UserData": {
            "Fn::Base64": {
                "Fn::Join": [
                    "",
                    [
                        "#! /bin/sh\\n",
                        "\\n",
                        "yum -y update\\n"
                    ]
                ]
            }
        }
    }
}
```

#### UserData内のRef
次のようにUserData内で[Ref:XXX]の書式を埋め込むことで、リソースの参照（Ref）を展開できる。
```groovy
def userData = """\
#! /bin/bash -v

# Install packages
/opt/aws/bin/cfn-init -s [Ref:AWS::StackId] -r WebServer --region [Ref:AWS::Region]
"""
```

```json
"UserData": {
    "Fn::Base64": {
        "Fn::Join": [
            "",
            [
                "#! /bin/bash -v\\n",
                "\\n",
                "# Install packages\\n",
                "/opt/aws/bin/cfn-init -s ",
                {
                    "Ref": "AWS::StackId"
                },
                " -r WebServer --region ",
                {
                    "Ref": "AWS::Region"
                },
                "\\n"
            ]
        ]
    }
}
```



