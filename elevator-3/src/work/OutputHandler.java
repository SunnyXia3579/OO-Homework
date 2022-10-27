package work;

import com.oocourse.TimableOutput;

public class OutputHandler /*implements Runnable*/ {
    private static OutputHandler outputHandler = new OutputHandler();

    private OutputHandler() {
    }

    public static OutputHandler getInstance() {
        /*
        if (outputHandler == null) {
            outputHandler = new OutputHandler();
        }
         */
        return outputHandler;
    }

    public synchronized void arrive(char building, int floor, int elevatorId) {
        TimableOutput.println("ARRIVE-" + building + "-" + floor + "-" + elevatorId);
    }

    public synchronized void open(char building, int floor, int elevatorId) {
        TimableOutput.println("OPEN-" + building + "-" + floor + "-" + elevatorId);
    }

    public synchronized void close(char building, int floor, int elevatorId) {
        TimableOutput.println("CLOSE-" + building + "-" + floor + "-" + elevatorId);
        // System.out.println("111111111111111111");
    }

    public synchronized void in(char building, int floor, int elevatorId, int passengerId) {
        TimableOutput.println("IN-" + passengerId + "-" + building +
                "-" + floor + "-" + elevatorId);
    }

    public synchronized void out(char building, int floor, int elevatorId, int passengerId) {
        TimableOutput.println("OUT-" + passengerId + "-" + building +
                "-" + floor + "-" + elevatorId);
    }

    public void test(String s) {
        System.out.println(s);
    }
}
