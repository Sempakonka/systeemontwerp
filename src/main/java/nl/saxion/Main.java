package nl.saxion;


import nl.saxion.adaptor.FileDataReader;
import nl.saxion.adaptor.JSONFileDataReader;
import nl.saxion.factory.HousedPrinterFactory;
import nl.saxion.factory.MultiColorPrinterFactory;
import nl.saxion.factory.StandardFDMPrinterFactory;

import java.util.*;

public class Main implements nl.saxion.observer.Observer {
    private final PrinterFacade manager = new PrinterFacade();
    Scanner scanner = new Scanner(System.in);
    private String printStrategy = "Less Spool Changes";

    public static void main(String[] args) {
        new Main().run(args);
    }

    public void registerFactories() {
        manager.registerFactory(1, new HousedPrinterFactory());
        manager.registerFactory(2, new StandardFDMPrinterFactory());
        manager.registerFactory(3, new MultiColorPrinterFactory());
    }

    public void run(String[] args) {
        registerFactories();
        FileDataReader dataReader = new JSONFileDataReader();
        if (args.length > 0) {
            dataReader.readPrints(args[0]);
            dataReader.readPrinters(args[1]);
            dataReader.readSpools(args[2]);
        } else {
            dataReader.readPrints("/Users/sempakonka/Desktop/school/JAAR 3/kwartiel2/3dPrintScheduler/src/main/resources/prints.json");
            dataReader.readPrinters("/Users/sempakonka/Desktop/school/JAAR 3/kwartiel2/3dPrintScheduler/src/main/resources/printers.json");
            dataReader.readSpools("/Users/sempakonka/Desktop/school/JAAR 3/kwartiel2/3dPrintScheduler/src/main/resources/spools.json");
        }

        manager.addObserver(this);

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

    private void changePrintStrategy() {
        System.out.println("---------- Change Strategy -------------");
        System.out.println("- Current strategy: " + printStrategy);
        System.out.println("- 1: Less Spool Changes");
        System.out.println("- 2: Efficient Spool Usage");
        System.out.println("- Choose strategy: ");
        int strategyChoice = numberInput(1, 2);
        manager.changePrintStrategy(strategyChoice);
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
        String printerId = stringInput();
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
        String printerId = stringInput();
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
        manager.addPrintTask(printName, colors, filamentTypeIndex);
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
        while (input == null || input.length() == 0) {
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


    @Override
    public void update(String message) {
        System.out.println(message);
    }
}
