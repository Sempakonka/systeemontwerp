package nl.saxion.adaptor;

import nl.saxion.Models.FilamentType;
import nl.saxion.PrinterFacade;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVFileDataReader implements FileDataReader {

    private PrinterFacade printerFacade;

    public CSVFileDataReader(PrinterFacade printerFacade) {
        this.printerFacade = printerFacade;
    }

    @Override
    public void readPrints(String filename) {
        // Implementation for reading prints (to be done later)
    }

    @Override
    public void readPrinters(String filename) {
        // Implementation for reading printers (to be done later)
    }

    @Override
    public void readSpools(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                // Skip the header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length != 4) {
                    // Handle error or skip the line
                    continue;
                }

                try {
                    int id = Integer.parseInt(values[0].trim());
                    String color = values[1].trim();
                    String filamentTypeAsString = values[2].trim();
                    double length = Double.parseDouble(values[3].trim());

                    FilamentType filamentType = FilamentType.valueOf(filamentTypeAsString.toUpperCase());

                    printerFacade.addSpool(id, color, filamentType, length);
                } catch (NumberFormatException e) {
                    // Handle parsing error
                }
            }
        } catch (IOException e) {
            // Handle IO Exception
        }
    }
}

