# CloudFormation Template Builder

## これは何？
CSV形式びCloudFormationの定義ファイルをJSONに整形します。

## 使い方
cfnディレクトリ下に設定ファイルを定義します。
書き方はなんとなく・・・で

対応しないパラメータがあると思うので、それは要望するかPRしてください。

./gradlew

### オプション

-PcfnDir=[ベースディレクトリ]  cfn以外のディレクトリを指定する場合はパラメータを指定してください。
-printTemplateJSON=[true|false]  標準出力にJSONを表示したくない場合はfalseを指定してください（デフォルト: true）




