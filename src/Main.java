import cli.TimeStampCLI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Main {

    private static final String PROPERTY_FILE = "config/app.properties";

    public static void main(String[] args) {
        // get properties
        Properties prop = getProperties();

        // exec command line
        var cli = new TimeStampCLI();
        cli.execParsedCommandLine(args, prop);
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
