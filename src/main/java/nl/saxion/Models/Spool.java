package nl.saxion.Models;

public class Spool {
    private final Integer id;
    private final String color;
    private final FilamentType filamentType;
    private double length;

    private String taskId;
    // this specifies what part of the print should be done by this spool
    private Integer printFilamentIndex;

    private String printId;


    public Spool(Integer id, String color, FilamentType filamentType, double length) {
        this.id = id;
        this.color = color;
        this.filamentType = filamentType;
        this.length = length;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public void setPrintFilamentIndex(Integer printFilamentIndex) {
        this.printFilamentIndex = printFilamentIndex;
    }

    public void setPrintId(String printId) {
        this.printId = printId;
    }

    public String getPrintId() {
        return printId;
    }

    public Integer getPrintFilamentIndex() {
        return printFilamentIndex;
    }

    public String getTaskId() {
        return taskId;
    }

    public Spool(Integer id, String color, String filamentTypeAsString, double length) {
        this.id = id;
        this.color = color;
        try {
            this.filamentType = FilamentType.valueOf(filamentTypeAsString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid filament type: " + filamentTypeAsString);
        }
        this.length = length;
    }

    public Integer getId() {
        return this.id;
    }

    public double getLength() {
        return length;
    }

    public boolean spoolMatch(String color, FilamentType type) {
        return color.equals(this.color) && type == this.getFilamentType();
    }
    /**
     * This method will try to reduce the length of the spool.
     *
     * @param byLength
     * @return boolean which tells you if it is possible or not.
     */
    public boolean reduceLength(double byLength) {
        boolean success = true;
        this.length -= byLength;
        if (this.length < 0) {
            this.length -= byLength;
            success = false;
        }
        return success;
    }

    // empty whole spool
    public void emptySpool() {
        this.length = 0;
    }

    public String getColor() {
        return color;
    }

    public FilamentType getFilamentType(){
        return filamentType;
    }

    @Override
    public String toString() {
        return  "--------" + System.lineSeparator() +
                "- id: " + id + System.lineSeparator() +
                "- color: " + color + System.lineSeparator() +
                "- filamentType: " + filamentType + System.lineSeparator() +
                "- length: " + length + System.lineSeparator() +
                "--------";
    }

    public String getRemainingLength() {
        return String.format("%.2f", length);
    }
}
