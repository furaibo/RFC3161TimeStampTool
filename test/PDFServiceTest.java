import jdk.jfr.Description;
import org.junit.Test;
import service.PDFService;
import service.TimeStampService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class PDFServiceTest {

    private static final String TEST_PROPERTY_FILE = "resources/test.properties";

    @Test
    @Description("Merge PDF Files")
    public void test_mergeFiles() {
        try {
            // Propertiesの読み込み
            Properties prop = getProperties();

            // 入力ファイルパスの取得
            Path inputFilePath1 = Paths.get(prop.getProperty("TestInputFilePath1"));
            Path inputFilePath2 = Paths.get(prop.getProperty("TestInputFilePath2"));
            Path inputFilePath3 = Paths.get(prop.getProperty("TestInputFilePath3"));
            List<Path> inputFilePathList = List.of(inputFilePath1, inputFilePath2, inputFilePath3);

            // PDFファイルのマージ
            PDFService pdfs = new PDFService(prop);
            pdfs.mergeFiles(inputFilePathList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @Description("Attach files to PDF")
    public void test_attachFiles() {
        try {
            // Propertiesの読み込み
            Properties prop = getProperties();

            // 入力ファイルパスの取得
            Path baseFilePath = Paths.get(prop.getProperty("TestInputFilePath1"));
            Path attachFilePath1 = Paths.get(prop.getProperty("TestInputFilePath2"));
            Path attachFilePath2 = Paths.get(prop.getProperty("TestInputFilePath3"));
            List<Path> attachFilePathList = List.of(attachFilePath1, attachFilePath2);

            // PDFファイルへの添付ファイル追加処理
            PDFService pdfs = new PDFService(prop);
            pdfs.attachFiles(baseFilePath, attachFilePathList);

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