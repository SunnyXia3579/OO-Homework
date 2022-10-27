package work;

import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.Request;
import work.factory.ElevatorFactory;
import work.factory.PassengerFactory;
import work.point.ElevatorPoint;
import work.point.Point;

import java.io.IOException;
import java.util.ArrayList;

public class InputHandler implements Runnable {
    private final Simulator simulator = Simulator.getInstance();
    private final ElevatorInput elevatorInput = new ElevatorInput(System.in);
    private final PassengerFactory passengerFactory = new PassengerFactory();
    private final ElevatorFactory elevatorFactory = new ElevatorFactory();
    private static InputHandler inputHandler;

    public static InputHandler getInstance() {
        if (inputHandler == null) {
            inputHandler = new InputHandler();
        }
        return inputHandler;
    }

    @Override
    public void run() {
        while (true) {
            Request request = elevatorInput.nextRequest();
            // System.out.println(request);
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                simulator.inputEnd();
                // OutputHandler.getInstance().test("input end!");
                break;
            } else {
                if (request instanceof PersonRequest) {
                    PersonRequest personRequest = (PersonRequest) request;
                    // a PersonRequest
                    simulator.passengerComeFromInput(passengerFactory.createPassenger(
                              personRequest.getPersonId(), personRequest.getFromBuilding(),
                              personRequest.getFromFloor(), personRequest.getToBuilding(),
                              personRequest.getToFloor()));
                } else if (request instanceof ElevatorRequest) {
                    // an ElevatorRequest
                    ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                    final int id = elevatorRequest.getElevatorId();
                    char building = 'A';
                    int floor = 1;
                    boolean isCircular = false;
                    double movingTIme = elevatorRequest.getSpeed();
                    int capacity = elevatorRequest.getCapacity();
                    ArrayList<ElevatorPoint> elevatorPoints = new ArrayList<>();
                    if (elevatorRequest.toString().contains("floor")) {  // 环形电梯
                        floor = elevatorRequest.getFloor();
                        for (char bd : new char[]{'A', 'B', 'C', 'D', 'E'}) {
                            if (((elevatorRequest.getSwitchInfo() >> (bd - 'A')) & 1) == 1) {
                                elevatorPoints.add(new ElevatorPoint(new Point(bd, floor), true));
                            } else {
                                elevatorPoints.add(new ElevatorPoint(new Point(bd, floor), false));
                            }
                        }
                        isCircular = true;
                    } else {  // 正常电梯
                        building = elevatorRequest.getBuilding();
                        for (int i = 1;i <= 10;i++) {
                            elevatorPoints.add(new ElevatorPoint(new Point(building, i), true));
                        }
                    }
                    simulator.elevatorComeFromInput(elevatorFactory.createElevator(elevatorPoints,
                            id, capacity, movingTIme, isCircular));
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
