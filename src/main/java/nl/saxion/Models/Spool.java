package nl.saxion.Models;

public class Spool {
    private final int id;
    private final String color;
    private final FilamentType filamentType;
    private double length;

    private String taskId;

    public Spool(int id, String color, FilamentType filamentType, double length) {
        this.id = id;
        this.color = color;
        this.filamentType = filamentType;
        this.length = length;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public Spool(int id, String color, String filamentTypeAsString, double length) {
        this.id = id;
        this.color = color;
        try {
            this.filamentType = FilamentType.valueOf(filamentTypeAsString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid filament type: " + filamentTypeAsString);
        }
        this.length = length;
    }

    public int getId() {
        return this.id;
    }

    public double getLength() {
        return length;
    }

    public boolean spoolMatch(String color, String type) {
        return color.equals(this.color) && String.valueOf(type).equals(String.valueOf(this.getFilamentType()));
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
