package gist.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CSVDataReader {
    
    private static final String GIST_DATA_CSV = "src/test/resources/gist-data.csv";
    private static final String NEGATIVE_GIST_DATA_CSV = "src/test/resources/gist-negative-data.csv";
    private static final String UPDATE_GIST_DATA_CSV = "src/test/resources/update-gist-data.csv";
    private static final String UPDATE_GIST_NEGATIVE_DATA_CSV = "src/test/resources/update-gist-negative-data.csv";
    
    public static Iterator<Object[]> getGistData(String csvPath) throws IOException, CsvValidationException {
        List<Object[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                if (line.length >= 3) {
                    data.add(new Object[]{line[0], line[1], line[2]});
                }
            }
        }
        return data.iterator();
    }
    
    public static Iterator<Object[]> getNegativeGistData(String csvPath) throws IOException, CsvValidationException {
        List<Object[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                // Ensure we handle lines even if content (last field) is empty
                String desc = line.length > 0 ? line[0] : "";
                String fname = line.length > 1 ? line[1] : "";
                String cont = line.length > 2 ? line[2] : "";
                data.add(new Object[]{desc, fname, cont});
            }
        }
        return data.iterator();
    }
    
    public static Iterator<Object[]> getUpdateGistData(String csvPath) throws IOException, CsvValidationException {
        List<Object[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                if (line.length >= 6) {
                    data.add(new Object[]{
                        line[0], line[1], line[2], // original: description, filename, content
                        line[3], line[4], line[5]  // new: description, filename, content
                    });
                }
            }
        }
        return data.iterator();
    }
    
    @DataProvider(name = "gistData")
    public static Iterator<Object[]> gistDataProvider() throws IOException, CsvValidationException {
        return getGistData(GIST_DATA_CSV);
    }
    
    @DataProvider(name = "negativeGistData")
    public static Iterator<Object[]> negativeGistDataProvider() throws IOException, CsvValidationException {
        return getNegativeGistData(NEGATIVE_GIST_DATA_CSV);
    }
    
    @DataProvider(name = "updateGistData")
    public static Iterator<Object[]> updateGistDataProvider() throws IOException, CsvValidationException {
        return getUpdateGistData(UPDATE_GIST_DATA_CSV);
    }
    
    public static Iterator<Object[]> getUpdateGistNegativeData(String csvPath) throws IOException, CsvValidationException {
        List<Object[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                if (line.length >= 7) {
                    data.add(new Object[]{
                        line[0], line[1], line[2], // original: description, filename, content
                        line[3], line[4], line[5], // new: description, filename, content
                        line[6] // expectedError
                    });
                }
            }
        }
        return data.iterator();
    }
    
    @DataProvider(name = "updateGistNegativeData")
    public static Iterator<Object[]> updateGistNegativeDataProvider() throws IOException, CsvValidationException {
        return getUpdateGistNegativeData(UPDATE_GIST_NEGATIVE_DATA_CSV);
    }
}
