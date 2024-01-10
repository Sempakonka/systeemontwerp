package nl.saxion.adaptor;

import nl.saxion.Models.Print;
import nl.saxion.Models.Printer;
import nl.saxion.Models.Spool;

import java.util.List;

import nl.saxion.factory.HousedPrinterFactory;
import nl.saxion.factory.MultiColorPrinterFactory;
import nl.saxion.factory.PrinterFactory;
import nl.saxion.factory.StandardFDMPrinterFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JSONFileDataReader implements FileDataReader {

    @Override
    public List<Print> readPrints(String filename) {
        List<Print> prints = new ArrayList<>();
        JSONArray jsonPrints = readJSONArrayFromFile(filename);
        for (Object obj : jsonPrints) {
            JSONObject printJSON = (JSONObject) obj;
            Print print = parsePrintObject(printJSON);
            prints.add(print);
        }
        return prints;
    }

    @Override
    public List<Printer> readPrinters(String filename) {
        List<Printer> printers = new ArrayList<>();
        JSONArray jsonPrinters = readJSONArrayFromFile(filename);
        for (Object obj : jsonPrinters) {
            JSONObject printerJSON = (JSONObject) obj;
            Printer printer = parsePrinterObject(printerJSON);
            printers.add(printer);
        }
        return printers;
    }

    @Override
    public List<Spool> readSpools(String filename) {
        List<Spool> spools = new ArrayList<>();
        JSONArray jsonSpools = readJSONArrayFromFile(filename);
        for (Object obj : jsonSpools) {
            JSONObject spoolJSON = (JSONObject) obj;
            Spool spool = parseSpoolObject(spoolJSON);
            spools.add(spool);
        }
        return spools;
    }

    private JSONArray readJSONArrayFromFile(String filename) {
        JSONParser jsonParser = new JSONParser();
        if (filename.length() == 0) {
            filename = "default.json"; // Default filename if none provided
        }

        try (FileReader reader = new FileReader(filename)) {
            Object obj = jsonParser.parse(reader);
            return (JSONArray) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new JSONArray(); // Return empty array on error
        }
    }

    private Print parsePrintObject(JSONObject printJSON) {
        String name = (String) printJSON.get("name");
        int height = ((Long) printJSON.get("height")).intValue();
        int width = ((Long) printJSON.get("width")).intValue();
        int length = ((Long) printJSON.get("length")).intValue();
        JSONArray filamentLengthsJSON = (JSONArray) printJSON.get("filamentLength");
        ArrayList<Double> filamentLengths = new ArrayList<>();
        for (Object lengthObj : filamentLengthsJSON) {
            filamentLengths.add(((Double) lengthObj));
        }
        int printTime = ((Long) printJSON.get("printTime")).intValue();

        return new Print(name, height, width, length, filamentLengths, printTime);
    }

    private Printer parsePrinterObject(JSONObject printerJSON) {
        int id = ((Long) printerJSON.get("id")).intValue();
        int type = ((Long) printerJSON.get("type")).intValue();
        String name = (String) printerJSON.get("name");
        String manufacturer = (String) printerJSON.get("manufacturer");
        int maxX = ((Long) printerJSON.get("maxX")).intValue();
        int maxY = ((Long) printerJSON.get("maxY")).intValue();
        int maxZ = ((Long) printerJSON.get("maxZ")).intValue();
        int maxColors = ((Long) printerJSON.get("maxColors")).intValue();
        PrinterFactory factory = getPrinterFactory(type);
        
        if (factory != null) {
            return factory.createPrinter(id, name, manufacturer, maxX, maxY, maxZ, maxColors);
        } else {
            throw new IllegalArgumentException("Invalid printer type: " + type);
        }
    }

    private PrinterFactory getPrinterFactory(int printerType) {
        return switch (printerType) {
            case 1 -> new StandardFDMPrinterFactory();
            case 2 -> new HousedPrinterFactory();
            case 3 -> new MultiColorPrinterFactory();
            default -> null;
        };
    }

    private Spool parseSpoolObject(JSONObject spoolJSON) {
        int id = ((Long) spoolJSON.get("id")).intValue();
        String color = (String) spoolJSON.get("color");
        String filamentType = (String) spoolJSON.get("filamentType");
        double length = (Double) spoolJSON.get("length");

        return new Spool(id, color, filamentType, length);
    }
}
