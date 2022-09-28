import service.CsvServiceImplementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String LOG_TAG = "CSV:MAIN:: ";

    public static void main(String[] args) {

        File file=new File(args[0]);
        System.out.println(file.getName());
        if(file.getName().isEmpty())
        {
            System.out.println("empty");
        }
        if (args.length == 0) {
            LOGGER.log(Level.INFO, LOG_TAG + "path is  mandatory");
            return;
        } else if (args.length == 1) {
            LOGGER.log(Level.INFO, LOG_TAG + "required  json path and output path");
            return;
        } else if (args.length >= 3) {
            LOGGER.log(Level.INFO, LOG_TAG + "you can give only Json path and output path");
            return;
        }

        String jsonFilePath = args[0];
        String downloadPath = args[1];
        File inputPath = new File(jsonFilePath);
        File outputPath = new File(downloadPath);

        if (!isPathValid(outputPath)) {
            LOGGER.log(Level.SEVERE, LOG_TAG + "Invalid download path");
            return;
        }
        if (!isPathValid(inputPath)) {
            LOGGER.log(Level.SEVERE, LOG_TAG + "Invalid json path");
            return;
        }
        String fileExtension = com.google.common.io.Files.getFileExtension(jsonFilePath);
        if (!fileExtension.equals("json")) {
            LOGGER.log(Level.SEVERE, LOG_TAG + "invalid file format");
            return;
        }

        String json = readFileAsString(jsonFilePath);
        CsvServiceImplementation csvServiceImplementation = new CsvServiceImplementation();
        csvServiceImplementation.convertJsonToCsv(json, downloadPath);
    }

    public static String readFileAsString(String file) {
        String LOG_METHOD = "Read file as String ::";
        String jsonString = null;
        try {
            jsonString = new String(Files.readAllBytes(Paths.get(file)));
        } catch (NoSuchFileException e) {
            LOGGER.log(Level.SEVERE, LOG_TAG + LOG_METHOD + "File not found");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, LOG_TAG + LOG_METHOD + "invalid json");
        }
        return jsonString;
    }

    public static boolean isPathValid(File file) {
        if (file.exists()) {
            return true;
        }
        return false;
    }
}
