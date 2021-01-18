import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Prop {
    private static final String PATH_TO_PROPERTIES = "src\\main\\resources\\config.properties";
    static Properties properties = new Properties();
    static {
        try {
            try(FileInputStream fileInputStream = new FileInputStream(PATH_TO_PROPERTIES)) {
                properties.load(fileInputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
