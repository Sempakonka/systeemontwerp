import nl.saxion.PrinterFacade;
import nl.saxion.managers.PrintManager;
import nl.saxion.managers.PrintTaskManager;
import nl.saxion.managers.PrinterManager;
import nl.saxion.managers.SpoolManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class PrinterCompatibilityTest {

    private PrinterFacade printerFacade;
    private PrinterManager printerManager;
    private PrintManager printManager;
    private PrintTaskManager printTaskManager;
    private SpoolManager spoolManager;
    // Add other managers if needed

    @BeforeEach
    void setUp() {
        // Initialize your managers with test data
        printerManager = PrinterManager.getInstance();
        printManager = PrintManager.getInstance();
        printTaskManager = PrintTaskManager.getInstance();
        spoolManager = SpoolManager.getInstance();
        // Initialize other managers

        // Set up the managers with necessary data or configurations for testing
        // Assuming you have methods to clear previous data and set up new test data
//        printerManager.clearData();
//        printManager.clearData();
//        printTaskManager.clearData();
//        spoolManager.clearData();
        // Clear and set up other managers

        printerFacade = new PrinterFacade();
    }

    @Test
    void testIncompatiblePrintAssignment() {
        // Create an instance of Print that is incompatible with the Printer
        ArrayList<Double> filamentLength = new ArrayList<>();
        filamentLength.add(9.05); filamentLength.add(1.41); filamentLength.add(13.95); filamentLength.add(0.31);
        printManager.addPrint("Earth Globe", 57, 57, 57, filamentLength, 770);

        // Add a printer to your system
        printerManager.addPrinter("1", 1, "Enterprise", "Creality", 220, 220, 250, 1);

        // Attempt to select a print task for the printer
        boolean isTaskSelected = printerFacade.selectPrintTask("1");

        // Verify that no suitable print task was found
        assertFalse(isTaskSelected, "No suitable print task should be found for the printer");
    }
}
