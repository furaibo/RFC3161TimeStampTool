package cli;

import org.apache.commons.cli.*;
import org.apache.commons.codec.digest.DigestUtils;
import service.PDFService;
import service.TimeStampService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class TimeStampCLI {

    // メンバ変数
    private final Options options;

    // コンストラクタ
    public TimeStampCLI() {
        // コマンドラインオプションの定義
        Option optionMode = Option.builder("m")
                .argName("mode")
                .longOpt("mode")
                .hasArg()
                .desc("select operation mode")
                .build();

        Option optionFile = Option.builder("f")
                .argName("input files")
                .longOpt("file")
                .hasArgs()
                .desc("input files")
                .build();

        Option optionAttach = Option.builder("a")
                .argName("attachment files")
                .longOpt("attach")
                .hasArgs()
                .desc("attachment files (only for pdfattach mode)")
                .build();

        Option optionHelp = Option.builder("h")
                .argName("show help")
                .longOpt("help")
                .desc("show help message")
                .build();

        // コマンドラインオプションの追加
        Options options = new Options();
        options.addOption(optionMode);
        options.addOption(optionFile);
        options.addOption(optionAttach);
        options.addOption(optionHelp);

        // メンバ変数の設定
        this.options = options;
    }

    // コマンドラインオプションのパース処理
    public void execParsedCommandLine(String[] args, Properties property) {
        // コマンドライン解析
        CommandLine cmd = null;
        CommandLineParser parser = new DefaultParser();

        try {
            cmd = parser.parse(options, args);

            // ヘルプメッセージ表示の場合
            if (cmd.hasOption("h")) {
                printHelpMessage();
                printAvailableModeList();
                return;
            }

            // モード設定の取得
            String mode;
            if (cmd.hasOption("m")) {
                mode = cmd.getOptionValue("m");
            } else {
                System.out.println("Notice: Set -m option");
                printHelpMessage();
                printAvailableModeList();
                return;
            }

            // 入力ファイル一覧の取得
            List<Path> inputFilePathList = new ArrayList<Path>();
            if (cmd.hasOption("f")) {
                for (String pathStr : cmd.getOptionValues("f")) {
                    Path path = Paths.get(pathStr);

                    // 指定パスがディレクトリかファイルかどうかで分岐
                    if (Files.isDirectory(path)) {
                        // 指定パスがディレクトリの場合は内部のファイル全体のパスを追加
                        try (Stream<Path> stream = Files.list(path)) {
                            stream.forEach(inputFilePathList::add);
                        }
                    } else {
                        // 指定パスがファイルの場合はそのまま追加
                        inputFilePathList.add(path);
                    }
                }
            }

            // 添付ファイル一覧の取得
            List<Path> attachFilePathList = new ArrayList<Path>();
            if (cmd.hasOption("a")) {
                for (String pathStr : cmd.getOptionValues("a")) {
                    Path path = Paths.get(pathStr);

                    // 指定パスがディレクトリかファイルかどうかで分岐
                    if (Files.isDirectory(path)) {
                        // 指定パスがディレクトリの場合は内部のファイル全体のパスを追加
                        try (Stream<Path> stream = Files.list(path)) {
                            stream.forEach(attachFilePathList::add);
                        }
                    } else {
                        // 指定パスがファイルの場合はそのまま追加
                        attachFilePathList.add(path);
                    }
                }
            }

            // 各種サービスの初期化
            TimeStampService tss = new TimeStampService(property);
            PDFService pdfs = new PDFService(property);

            // モードによる分岐処理
            switch (mode) {
                case "tsatest":
                    // サンプル文字列のハッシュ値によるタイムスタンプ情報取得
                    System.out.println("TSA test start...");
                    byte[] inputBytes = DigestUtils.sha256("Hello, World!".getBytes());
                    tss.testTimeStampResponse(inputBytes);
                    break;

                case "timestamp":
                    // タイムスタンプの付与
                    System.out.println("Timestamping start...");
                    if (inputFilePathList.size() == 1) {
                        tss.addTimeStampToSingleFile(inputFilePathList.getFirst());
                    } else {
                        tss.addTimeStampToMultipleFiles(inputFilePathList);
                    }
                    break;

                case "verify":
                    // タイムスタンプ付与済みファイルの検証
                    System.out.println("Verification start...");
                    tss.verifyTimeStampInMultipleFiles(inputFilePathList);
                    break;

                case "pdfmerge":
                    // 複数PDFのマージ処理
                    System.out.println("Merge PDFs...");
                    if (inputFilePathList.size() <= 1) {
                        System.out.println("You must select multiple files as input");
                        System.exit(1);
                    } else {
                        pdfs.mergeFiles(inputFilePathList);
                    }
                    break;

                case "pdfattach":
                    // PDFへの添付ファイル追加処理
                    System.out.println("Attach files to PDF...");
                    if (inputFilePathList.size() == 1 && !attachFilePathList.isEmpty()) {
                        pdfs.attachFiles(inputFilePathList.getFirst(), attachFilePathList);
                    } else {
                        System.out.println("You must select one input file and at least one attach files.");
                        System.exit(1);
                    }
                    break;

                default:
                    printAvailableModeList();
            }

        } catch (ParseException e) {
            // オプションのパースエラー発生時
            System.out.println(e.getMessage());
            printHelpMessage();
            System.exit(1);
        } catch (Exception e){
            // その他エラーの発生時
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    // コマンド利用時のヘルプ情報を表示する
    private void printHelpMessage() {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("[opts]", this.options);
    }

    // 利用可能なモード情報を表示する
    private void printAvailableModeList() {
        String[] modeStringList = {
                "\n[Available operation modes]",
                " * tsatest   - Test connection with TSA",
                " * timestamp - Add timestamps to input PDF files",
                " * verify    - Verify PDF files with timestamps",
                " * pdfmerge  - Merge multiple PDF files",
                " * pdfattach - Add attachment files to base PDF file",
            };

        for (String line : modeStringList) {
            System.out.println(line);
        }
    }
}
