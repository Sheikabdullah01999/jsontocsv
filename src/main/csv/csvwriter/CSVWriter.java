package csvwriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * it provide service to flatten json
 *
 * @author <a href="sheikabdullah.m@grootan.com">sheik abdullah</a>
 */
public class CSVWriter {
    //private static final Logger LOGGER = LoggerFactory.getLogger(CSVWriter.class);

    /**
     * Convert the given List of String keys-values as a CSV String.
     *
     * @param flatJson The List of key-value pairs generated from the JSON String
     * @return The generated CSV string
     */
    public static String getCSV(List<Map<String, String>> flatJson) {
        return getCSV(flatJson, ",");
    }

    /**
     * Convert the given List of String keys-values as a CSV String.
     *
     * @param flatJson  The List of key-value pairs generated from the JSON String
     * @param separator The separator can be: ',', ';' or '\t'
     * @return The generated CSV string
     */
    public static String getCSV(List<Map<String, String>> flatJson, String separator) {
        Set<String> headers = collectHeaders(flatJson);
        StringBuilder csvString = new StringBuilder(StringUtils.join(headers.toArray(), separator) + "\n");

        for (Map<String, String> map : flatJson) {
            csvString.append(getSeperatedColumns(headers, map, separator)).append("\n");
        }
        return csvString.toString();
    }

    /**
     * Write the given CSV string to the given file.
     *
     * @param csvString The csv string to write into the file
     * @param fileName  The file to write (included the path)
     */
    public static boolean writeToFile(String csvString, String fileName)  {
        try {
            FileUtils.write(new File(fileName), csvString);
            return true;
        } catch (IOException e) {
           // LOGGER.error("failed to write csv");
            return false;
        }
    }


    /**
     * Get separated comlumns used a separator (comma, semi column, tab).
     *
     * @param headers The CSV headers
     * @param map     Map of key-value pairs contains the header and the value
     * @return a string composed of columns separated by a specific separator.
     */
    private static String getSeperatedColumns(Set<String> headers, Map<String, String> map, String separator) {
        List<String> items = new ArrayList<>();
        for (String header : headers) {
            String value = map.get(header) == null ? "" : map.get(header).replaceAll("[\\,\\;\\r\\n\\t\\s]+", " ");
            items.add(value);
        }

        return StringUtils.join(items.toArray(), separator);
    }

    /**
     * Get the CSV header.
     *
     * @param flatJson
     * @return a Set of headers
     */
    private static Set<String> collectHeaders(List<Map<String, String>> flatJson) {
        Set<String> headers = new LinkedHashSet<>();

        for (Map<String, String> map : flatJson) {
            headers.addAll(map.keySet());
        }
        return headers;
    }
}
