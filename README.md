# RFC3161TimeStampTool

RFC-3161形式のタイムスタンプツール(CUI)のリポジトリです。PDFファイルへのタイムスタンプ埋込み(PAdES)に対応しています。

- 著者: 風来坊(@furaibo)
- 日時: 2024/11/2

下記の技術同人誌向けのサンプルとして作成しました。

- 書籍名: **自分でつくる！電帳法対応タイムスタンプツール**
- 販売URL: https://techbookfest.org/product/hRX58ECcTNDh3jZUTN16yA?productVariantID=6E7cNnimv1WgSC6fsSHuCw


## ツールの提供機能

* PDFファイルへのタイムスタンプ埋め込み
* PDFファイルへの添付ファイル追加
* 複数PDFファイルのマージ処理
* タイムスタンプ検証結果のCSVファイル出力


## 使用ライブラリ

* BouncyCastle ... 1.78.1
* Apache PDFBox ... 3.0.3
* Apache Commons CLI ... 1.9.0


## タイムスタンプ局(TSA)情報の設定

`config/app.properties` ファイル内の下記項目を編集してください。

```
### TSA account config ###
tsa.service.url=[タイムスタンプ局の接続先URL]
tsa.service.user.account=[BASIC認証・アカウント]
tsa.service.user.password=[BASIC認証・パスワード]
```


## ツール利用時のコマンド入力例

```
## タイムスタンプ局との通信のテスト
$ java -jar tstool.jar -m tsatest

## ファイル指定でのタイムスタンプ付与
$ java -jar tstool.jar -m timestamp -f input_file.pdf

## ファイル指定(+添付ファイルあり)のタイムスタンプ付与
$ java -jar tstool.jar -m timestamp -f input_file.pdf -a attach_file1.pdf attach_file2.jpg attach_file3.txt

## ファイル指定でのタイムスタンプ検証(CSV結果出力あり)
$ java -jar tstool.jar -m verify -f input_file.pdf
```