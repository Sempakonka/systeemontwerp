package nl.saxion.adaptor;
import nl.saxion.PrinterFacade;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class JSONFileDataReader implements FileDataReader {
    PrinterFacade printerFacade = new PrinterFacade();

    public JSONFileDataReader() {}

    @Override
    public void readPrints(String filename) {
        JSONArray jsonPrints = readJSONArrayFromFile(filename);
        for (Object obj : jsonPrints) {
            JSONObject printJSON = (JSONObject) obj;
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
            printerFacade.addPrint(name, height, width, length, filamentLengths, printTime);
        }
    }

    @Override
    public void readPrinters(String filename) {
        JSONArray jsonPrinters = readJSONArrayFromFile(filename);
        for (Object obj : jsonPrinters) {
            JSONObject printerJSON = (JSONObject) obj;
            String id = ((String) printerJSON.get("id"));
            int type = ((Long) printerJSON.get("type")).intValue();
            String name = (String) printerJSON.get("name");
            String manufacturer = (String) printerJSON.get("manufacturer");
            int maxX = ((Long) printerJSON.get("maxX")).intValue();
            int maxY = ((Long) printerJSON.get("maxY")).intValue();
            int maxZ = ((Long) printerJSON.get("maxZ")).intValue();
            int maxColors = ((Long) printerJSON.get("maxColors")).intValue();
            printerFacade.addPrinter(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors);
        }
    }

    @Override
    public void readSpools(String filename) {
        JSONArray jsonSpools = readJSONArrayFromFile(filename);
        for (Object obj : jsonSpools) {
            JSONObject spoolJSON = (JSONObject) obj;
            int id = ((Long) spoolJSON.get("id")).intValue();
            String color = (String) spoolJSON.get("color");
            String filamentType = (String) spoolJSON.get("filamentType");
            double length = (Double) spoolJSON.get("length");
            printerFacade.addSpool(id, color, filamentType, length);
        }
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
}
