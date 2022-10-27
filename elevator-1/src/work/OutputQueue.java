package work;

import java.util.ArrayList;

public class OutputQueue {
    private final ArrayList<String> outputs = new ArrayList<>();
    private Boolean isEnd = false;
    private static OutputQueue outputQueue;
    private static int endElevator;

    private OutputQueue() {
    }

    public static OutputQueue getInstance() {
        if (outputQueue == null) {
            outputQueue = new OutputQueue();
        }
        return outputQueue;
    }

    private void addOutput(String output) {
        outputs.add(output);
        notifyAll();
    }

    public synchronized void test(String s) {
        addOutput(s);
    }

    public synchronized String getOneOutput() {
        while (isEmpty()) {
            if (isEnd) {
                return null;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String output = outputs.get(0);
        outputs.remove(0);
        return output;
    }

    public synchronized void arrive(char building, int floor, int elevatorId) {
        addOutput("ARRIVE-" + building + "-" + floor + "-" + elevatorId);
    }

    public synchronized void open(char building, int floor, int elevatorId) {
        addOutput("OPEN-" + building + "-" + floor + "-" + elevatorId);
    }

    public synchronized void close(char building, int floor, int elevatorId) {
        addOutput("CLOSE-" + building + "-" + floor + "-" + elevatorId);
        // System.out.println("111111111111111111");
    }

    public synchronized void in(char building, int floor, int elevatorId, int passengerId) {
        addOutput("IN-" + passengerId + "-" + building + "-" + floor + "-" + elevatorId);
    }

    public synchronized void out(char building, int floor, int elevatorId, int passengerId) {
        addOutput("OUT-" + passengerId + "-" + building + "-" + floor + "-" + elevatorId);
    }

    public synchronized Boolean isEmpty() {
        return outputs.size() == 0;
    }

    private synchronized void end() {
        isEnd = true;
        notifyAll();
    }

    public synchronized void eneElevatorEnd() {
        endElevator++;
        if (endElevator == 5) {
            end();
            // System.out.println(endElevator);
        }
    }
}
