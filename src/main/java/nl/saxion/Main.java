package nl.saxion;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    Scanner scanner = new Scanner(System.in);
    private final PrinterFacade manager = new PrinterFacade(
    );

    private String printStrategy = "Less Spool Changes";

    public static void main(String[] args) {
        new Main().run(args);
    }

    public void run(String[] args) {
        if(args.length > 0) {
            readPrintsFromFile(args[0]);
            readSpoolsFromFile(args[1]);
            readPrintersFromFile(args[2]);
        } else {
            readPrintsFromFile("");
            readSpoolsFromFile("");
            readPrintersFromFile("");
        }
        int choice = 1;
        while (choice > 0 && choice < 10) {
            menu();
            choice = menuChoice(9);
            System.out.println("-----------------------------------");
            if (choice == 1) {
                addNewPrintTask();
            } else if (choice == 2) {
                registerPrintCompletion();
            } else if (choice == 3) {
                registerPrinterFailure();
            } else if (choice == 4) {
                changePrintStrategy();
            } else if (choice == 5) {
                startPrintQueue();
            } else if (choice == 6) {
                showPrints();
            } else if (choice == 7) {
                showPrinters();
            } else if (choice == 8) {
                showSpools();
            } else if (choice == 9) {
                showPendingPrintTasks();
            }
        }
        exit();
    }

    public void menu() {
        System.out.println("------------- Menu ----------------");
        System.out.println("- 1) Add new Print Task");
        System.out.println("- 2) Register Printer Completion");
        System.out.println("- 3) Register Printer Failure");
        System.out.println("- 4) Change printing style");
        System.out.println("- 5) Start Print Queue");
        System.out.println("- 6) Show prints");
        System.out.println("- 7) Show printers");
        System.out.println("- 8) Show spools");
        System.out.println("- 9) Show pending print tasks");
        System.out.println("- 0) Exit");

    }

    private void startPrintQueue() {
        System.out.println("---------- Starting Print Queue ----------");
        manager.startInitialQueue();
        System.out.println("-----------------------------------");
    }

    private void exit() {

    }

    // This method only changes the name but does not actually work.
    // It exists to demonstrate the output.
    // in the future strategy might be added.
    private void changePrintStrategy() {
        System.out.println("---------- Change Strategy -------------");
        System.out.println("- Current strategy: " + printStrategy);
        System.out.println("- 1: Less Spool Changes");
        System.out.println("- 2: Efficient Spool Usage");
        System.out.println("- Choose strategy: ");
        int strategyChoice = numberInput(1, 2);
        if(strategyChoice == 1) {
            printStrategy = "- Less Spool Changes";
        } else if( strategyChoice == 2) {
            printStrategy = "- Efficient Spool Usage";
        }
        System.out.println("-----------------------------------");
    }

    // TODO: This should be based on which printer is finished printing.
    private void registerPrintCompletion() {
        List<String> runningPrinters = manager.getRunningPrintersWithTasks();
        System.out.println("---------- Currently Running Printers ----------");

        if (runningPrinters.isEmpty()) {
            System.out.println("No printers are currently running tasks.");
            return;
        }

        for (String printerInfo : runningPrinters) {
            System.out.println(printerInfo);
        }

        System.out.print("- Enter the ID of the printer that has completed its task: ");
        int printerId = numberInput(1, Integer.MAX_VALUE);
        System.out.println("-----------------------------------");

        manager.registerCompletion(printerId);
    }


    private void registerPrinterFailure() {
        List<String> printersWithTasks = manager.getRunningPrintersWithTasks();
        System.out.println("---------- Currently Running Printers ----------");

        if (printersWithTasks.isEmpty()) {
            System.out.println("No printers are currently running tasks.");
            return;
        }

        for (String printerInfo : printersWithTasks) {
            System.out.println(printerInfo);
        }

        System.out.print("- Enter the ID of the printer that failed: ");
        int printerId = numberInput(1, Integer.MAX_VALUE);
        System.out.println("-----------------------------------");

        manager.registerPrinterFailure(printerId);
    }


    private void addNewPrintTask() {
        // Display available prints
        List<String> printNames = manager.getAvailablePrints();
        displayOptions("Available Prints", printNames);

        System.out.print("- Print number: ");
        int printIndex = numberInput(1, printNames.size()) - 1;
        String printName = printNames.get(printIndex);

        // Display available filament types
        List<String> filamentTypes = manager.getAvailableFilamentTypes();
        displayOptions("Filament Types", filamentTypes);

        System.out.print("- Filament type number: ");
        int filamentTypeIndex = numberInput(1, filamentTypes.size()) - 1;
        String filamentType = filamentTypes.get(filamentTypeIndex);

        // Display available colors for the selected filament type
        List<String> availableColors = manager.getAvailableColorsForFilamentType(filamentType);
        displayOptions("Available Colors for " + filamentType, availableColors);

        // Collect color choices from user
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < availableColors.size(); i++) {
            System.out.print("- Color number " + (i + 1) + ": ");
            int colorIndex = numberInput(1, availableColors.size()) - 1;
            colors.add(availableColors.get(colorIndex));
        }

        // Call Facade to add the print task
        manager.addPrintTask(printName, colors, filamentType);
        System.out.println("Print task added successfully.");
    }

    private void displayOptions(String title, List<String> options) {
        System.out.println("---------- " + title + " ----------");
        for (int i = 0; i < options.size(); i++) {
            System.out.println("- " + (i + 1) + ": " + options.get(i));
        }
    }


    private void showPrints() {
        List<String> prints = manager.getFormattedPrints();
        System.out.println("---------- Available Prints ----------");
        for (String print : prints) {
            System.out.println(print);
        }
        System.out.println("--------------------------------------");
    }


    private void showSpools() {
        List<String> spools = manager.getFormattedSpools();
        System.out.println("---------- Spools ----------");
        for (String spool : spools) {
            System.out.println(spool);
        }
        System.out.println("----------------------------");
    }

    private void showPrinters() {
        List<String> printerInfoList = manager.getFormattedPrinterInfo();
        System.out.println("--------- Available Printers ---------");
        for (String printerInfo : printerInfoList) {
            System.out.println(printerInfo);
        }
        System.out.println("--------------------------------------");
    }


    private void showPendingPrintTasks() {
        List<String> pendingTasksInfo = manager.getFormattedPendingPrintTasks();
        System.out.println("--------- Pending Print Tasks ---------");
        for (String taskInfo : pendingTasksInfo) {
            System.out.println(taskInfo);
        }
        System.out.println("--------------------------------------");
    }

    private void readPrintsFromFile(String filename) {
        JSONParser jsonParser = new JSONParser();
        if(filename.length() == 0) {
            filename = "prints.json";
        }
        URL printResource = getClass().getResource("/" + filename);
        if (printResource == null) {
            System.err.println("Warning: Could not find prints.json file");
            return;
        }
        try (FileReader reader = new FileReader(URLDecoder.decode(printResource.getPath(), StandardCharsets.UTF_8))) {
            JSONArray prints = (JSONArray) jsonParser.parse(reader);
            for (Object p : prints) {
                JSONObject print = (JSONObject) p;
                String name = (String) print.get("name");
                int height = ((Long) print.get("height")).intValue();
                int width = ((Long) print.get("width")).intValue();
                int length = ((Long) print.get("length")).intValue();
                //int filamentLength = ((Long) print.get("filamentLength")).intValue();
                JSONArray fLength = (JSONArray) print.get("filamentLength");
                int printTime = ((Long) print.get("printTime")).intValue();
                ArrayList<Double> filamentLength = new ArrayList();
                for(int i = 0; i < fLength.size(); i++) {
                    filamentLength.add(((Double) fLength.get(i)));
                }
                manager.addPrint(name, height, width, length, filamentLength, printTime);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void readPrintersFromFile(String filename) {
        JSONParser jsonParser = new JSONParser();
        if(filename.length() == 0) {
            filename = "printers.json";
        }
        URL printersResource = getClass().getResource("/" + filename);
        if (printersResource == null) {
            System.err.println("Warning: Could not find printers.json file");
            return;
        }
        try (FileReader reader = new FileReader(URLDecoder.decode(printersResource.getPath(), StandardCharsets.UTF_8))) {
            JSONArray printers = (JSONArray) jsonParser.parse(reader);
            for (Object p : printers) {
                JSONObject printer = (JSONObject) p;
                int id = ((Long) printer.get("id")).intValue();
                int type = ((Long) printer.get("type")).intValue();
                String name = (String) printer.get("name");
                String manufacturer = (String) printer.get("manufacturer");
                int maxX = ((Long) printer.get("maxX")).intValue();
                int maxY = ((Long) printer.get("maxY")).intValue();
                int maxZ = ((Long) printer.get("maxZ")).intValue();
                int maxColors = ((Long) printer.get("maxColors")).intValue();
                manager.addPrinter(id, type, name, manufacturer, maxX, maxY, maxZ, maxColors);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void readSpoolsFromFile(String filename) {
        JSONParser jsonParser = new JSONParser();
        if(filename.length() == 0) {
            filename = "spools.json";
        }
        URL spoolsResource = getClass().getResource("/" + filename);
        if (spoolsResource == null) {
            System.err.println("Warning: Could not find spools.json file");
            return;
        }
        try (FileReader reader = new FileReader(URLDecoder.decode(spoolsResource.getPath(), StandardCharsets.UTF_8))) {
            JSONArray spools = (JSONArray) jsonParser.parse(reader);
            for (Object p : spools) {
                JSONObject spool = (JSONObject) p;
                int id = ((Long) spool.get("id")).intValue();
                String color = (String) spool.get("color");
                String filamentType = (String) spool.get("filamentType");
                double length = (Double) spool.get("length");
                manager.addSpool(id, color, filamentType, length);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public int menuChoice(int max) {
        int choice = -1;
        while (choice < 0 || choice > max) {
            System.out.print("- Choose an option: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                //try again after consuming the current line
                System.out.println("- Error: Invalid input");
                scanner.nextLine();
            }
        }
        return choice;
    }

    public String stringInput() {
        String input = null;
        while(input == null || input.length() == 0){
            input = scanner.nextLine();
        }
        return input;
    }

    public int numberInput() {
        int input = scanner.nextInt();
        return input;
    }

    public int numberInput(int min, int max) {
        int input = numberInput();
        while (input < min || input > max) {
            input = numberInput();
        }
        return input;
    }
}
