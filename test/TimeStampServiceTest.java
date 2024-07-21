import jdk.jfr.Description;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import service.TimeStampService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class TimeStampServiceTest {

    private static final String TEST_PROPERTY_FILE = "resources/test.properties";

    @Test
    @Description("Test TimeStamp Response")
    public void test_testTimeStampResponse() {
        try {
            // Propertiesの読み込み
            Properties prop = getProperties();

            // タイムスタンプ情報の表示
            TimeStampService tss = new TimeStampService(prop);
            byte[] inputBytes = DigestUtils.sha256("Hello, World!".getBytes());
            tss.testTimeStampResponse(inputBytes);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @Description("Add a timeStamp to single file")
    public void test_addTimeStampToSingleFile() {
        try {
            // Propertiesの読み込み
            Properties prop = getProperties();

            // 入力ファイルパスの取得
            Path inputFilePath = Paths.get(prop.getProperty("TestInputFilePath1"));

            // タイムスタンプの取得処理(単一ファイル)
            TimeStampService tss = new TimeStampService(prop);
            tss.addTimeStampToSingleFile(inputFilePath);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @Description("Add timeStamps to multiple files")
    public void test_addTimeStampToMultipleFiles() {
        try {
            // Propertiesの読み込み
            Properties prop = getProperties();

            // 入力ファイルパスの取得
            Path inputFilePath1 = Paths.get(prop.getProperty("TestInputFilePath1"));
            Path inputFilePath2 = Paths.get(prop.getProperty("TestInputFilePath2"));
            Path inputFilePath3 = Paths.get(prop.getProperty("TestInputFilePath3"));
            List<Path> inputFilePathList = List.of(inputFilePath1, inputFilePath2, inputFilePath3);

            // タイムスタンプの取得処理(複数ファイル)
            TimeStampService tss = new TimeStampService(prop);
            tss.addTimeStampToMultipleFiles(inputFilePathList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @Description("Verify files including timestamp tokens")
    public void test_verifyTimeStampsInMultipleFiles() {
        try {
            // Propertiesの読み込み
            Properties prop = getProperties();

            // 入力ファイルパスの取得
            Path inputFilePath1 = Paths.get(prop.getProperty("TestInputFileSignedPath1"));
            Path inputFilePath2 = Paths.get(prop.getProperty("TestInputFileSignedPath2"));
            Path inputFilePath3 = Paths.get(prop.getProperty("TestInputFileSignedPath3"));
            List<Path> inputFilePathList = List.of(inputFilePath1, inputFilePath2, inputFilePath3);

            // タイムスタンプの検証処理
            TimeStampService tss = new TimeStampService(prop);
            tss.verifyTimeStampInMultipleFiles(inputFilePathList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 設定ファイルを読み取る
    public Properties getProperties() {
        Properties property = new Properties();

        try {
            property.load(new FileInputStream(TEST_PROPERTY_FILE));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IO exception");
        }

        return property;
    }
}
