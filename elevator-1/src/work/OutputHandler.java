package work;

import com.oocourse.TimableOutput;

public class OutputHandler /*implements Runnable*/ {
    private static OutputHandler outputHandler;
    private final OutputQueue outputs = OutputQueue.getInstance();
    private int endElevator;

    private OutputHandler() {
    }

    public static OutputHandler getInstance() {
        if (outputHandler == null) {
            outputHandler = new OutputHandler();
        }
        return outputHandler;
    }

    /*
    @Override
    public void run() {
        String output;
        while (true) {
            output = outputs.getOneOutput();
            if (output == null) {
                return;
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            TimableOutput.println(output);
        }
    }
     */

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
}
