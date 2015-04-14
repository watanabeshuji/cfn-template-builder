# EC2::VPC
AWS::EC2::VPC リソースを定義します。

Key                 |Required  |Default     |Example                        |Desc
--------------------|----------|------------|-------------------------------|----------------------------------
id                  |YES       |-           |"VPC"                          |VPC名
CidrBlock           |YES       |-           |"10.0.0.0/16"                  |CIDR Block
EnableDnsSupport    |NO        |false       |true                           |DNSサポートの有無
EnableDnsHostnames  |NO        |false       |true                           |DBSホスト名解決の有無
Tags                |NO        |[:]         |[Name: "vpc-dev"]              |リソースに定義するタグ、Mapで定義

## Simple VPC
```groovy
resources {
    vpc id: "VPC", CidrBlock: "10.0.0.0/16"
}
```

## Enable DNS Hostname and Name tag
```groovy
resources {
    vpc id: "VPC", CidrBlock: "192.168.0.0/16", EnableDnsSupport: true, EnableDnsHostnames: true, Tags: [Name: 'my-vpc']
}
```