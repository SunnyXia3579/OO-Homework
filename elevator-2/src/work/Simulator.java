package work;

import work.factory.ElevatorFactory;
import work.point.Point;

import java.util.ArrayList;
import java.util.HashMap;

public class Simulator implements Runnable {
    private final ElevatorPool elevators = new ElevatorPool();
    private final PassengerQueue comingPassengers = new PassengerQueue();
    private static Simulator simulator = null;
    private final HashMap<Character, HashMap<Integer, Point>> points = new HashMap<>();

    private Simulator() {
    }

    public static Simulator getInstance() {
        if (simulator == null) {
            simulator = new Simulator();
            ElevatorFactory elevatorFactory = new ElevatorFactory();
            int id = 1;
            for (char building : new char[]{'A', 'B', 'C', 'D', 'E'}) {
                HashMap<Integer, Point> tmp = new HashMap<>();
                for (int floor = 1;floor <= 10;floor++) {
                    tmp.put(floor, new Point(building, floor));
                }
                simulator.points.put(building, tmp);
                ArrayList<Point> points = new ArrayList<>();
                for (int i = 1;i <= 10;i++) {
                    points.add(new Point(building, i));
                }
                simulator.elevators.addOneElevator(elevatorFactory.createElevator(points, id,
                        0.4, false));
                id++;
            }
        }
        return simulator;
    }

    public void elevatorComeFromInput(Elevator elevator) {
        elevators.addOneElevator(elevator);
        new Thread(elevator).start();
    }

    public void passengerComeFromInput(Passenger passenger) {
        passenger.arrangePath();
        comingPassengers.addOnePassenger(passenger);
    }

    public void passengerComeFromElevator(Passenger passenger) {
        passenger.updatePath();
        if (!passenger.isArrived()) {
            comingPassengers.addOnePassenger(passenger);
        }
    }

    public void inputEnd() {
        comingPassengers.end();
    }

    @Override
    public void run() {
        elevators.startAll();
        while (true) {
            Passenger passenger = comingPassengers.getOnePassenger(true);
            if (passenger == null) {
                elevators.endAllWaitingPassenger();
                return;
            }
            elevators.dispatch(passenger);
        }
    }
}
