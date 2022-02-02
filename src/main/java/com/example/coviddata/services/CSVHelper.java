package com.example.coviddata.services;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.coviddata.entities.CovidData;
import com.opencsv.CSVReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Date", "Pays", "Infections", "Deces", "Guerisons", "TauxDeces", "TauxGuerison", "TauxInfection" };

    public static boolean hasCSVFormat(MultipartFile file) {
        if (TYPE.equals(file.getContentType())
                || file.getContentType().equals("application/vnd.ms-excel")) {
            return true;
        }

        return false;
    }

    public static List<CovidData> csvToTutorials(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<CovidData> CovidDataList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            CSVReader reader =null;

            /*try {
                reader = new CSVReader(new FileReader(String.valueOf(is)), ',');
            }*/

            for (CSVRecord csvRecord : csvRecords) {
                CovidData CovidData = new CovidData(
                     csvRecord.get("Date"),
                       csvRecord.get("Pays"),
                        Integer.parseInt(csvRecord.get("Infections")),
                        Integer.parseInt(csvRecord.get("Deces")),
                        Integer.parseInt(csvRecord.get("Guerisons")),
                        Float.parseFloat(csvRecord.get("TauxDeces")),
                        Float.parseFloat(csvRecord.get("TauxGuerison")),
                        Float.parseFloat(csvRecord.get("TauxInfection"))
                );

                CovidDataList.add(CovidData);
            }

            return CovidDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream tutorialsToCSV(List<CovidData> CovidDataList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (CovidData CovidData : CovidDataList) {
                List<String> data = Arrays.asList(
                        String.valueOf(CovidData.getId()),
                        CovidData.getDate(),
                        CovidData.getPays(),
                        String.valueOf(CovidData.getInfections()),
                        String.valueOf(CovidData.getDeces()),
                        String.valueOf(CovidData.getGuerisons()),
                        String.valueOf(CovidData.getTauxdeces()),
                        String.valueOf(CovidData.getTauxguerison()),
                        String.valueOf(CovidData.getTauxinfection())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
