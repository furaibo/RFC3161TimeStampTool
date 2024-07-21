import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Main {

    private static final String PROPERTY_FILE = "config/app.properties";

    public static void main(String[] args) {
        System.out.println("Main function");
    }

    public static Properties getProperties() {
        Properties property = new Properties();
        try {
            property.load(new FileInputStream(PROPERTY_FILE));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IO exception");
            System.exit(1);
        }
        return property;
    }
}
