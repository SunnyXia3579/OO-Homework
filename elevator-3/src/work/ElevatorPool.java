package work;

import work.point.Point;

import java.util.ArrayList;

public class ElevatorPool {
    private final ArrayList<Elevator> elevators = new ArrayList<>();
    private static ElevatorPool elevatorPool;
    private static final double PASSENGER_WEIGTH = 1;

    private ElevatorPool() {
    }

    public static ElevatorPool getInstance() {
        if (elevatorPool == null) {
            elevatorPool = new ElevatorPool();
        }
        return elevatorPool;
    }

    public synchronized void addOneElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    public synchronized void dispatch(Passenger passenger) {
        ArrayList<Elevator> rawElevators = new ArrayList<>();
        Elevator fineElevator = null;
        double min = 100000;
        for (Elevator elevator : elevators) {
            if ((passenger.getFromPoint().getBuilding() == passenger.getToPoint().getBuilding() &&
                elevator.getCurrentPoint().getBuilding() == passenger.getToPoint().getBuilding() &&
                elevator instanceof VerticalElevator) ||
                // (passenger.getFromPoint().getFloor() == passenger.getToPoint().getFloor() &&
                // elevator.getCurrentPoint().getFloor() == passenger.getToPoint().getFloor()) &&
                (elevator instanceof CircularElevator &&
                ((CircularElevator) elevator).hasPoint(passenger.getFromPoint()) &&
                ((CircularElevator) elevator).hasPoint(passenger.getToPoint()))) {
                rawElevators.add(elevator);
            }
        }
        for (Elevator elevator : rawElevators) {
            double weight = (elevator.getWaitingPassengers().getSize() +
                            elevator.getRidingPassengers().getSize()) /
                            (double) elevator.getCapacity() * PASSENGER_WEIGTH;
            if (!elevator.pointOnTheWay(passenger.getToPoint())) {
                weight += elevator.getRoute().size();
            }
            weight *= elevator.getMovingTime();
            if (weight < min) {
                min = weight;
                fineElevator = elevator;
            }
        }
        /*
        if (fineElevator.getId() == 82792688) {
            OutputHandler.getInstance().test("passenger: " + passenger);
        }
         */
        fineElevator.passengerInQueue(passenger);
    }

    public void arrangePath(Passenger passenger) {
        if (passenger.getFromPoint().getBuilding() != passenger.getToPoint().getBuilding()) {
            char fromBuilding = passenger.getFromPoint().getBuilding();
            int fromFloor = passenger.getFromPoint().getFloor();
            char toBuilding = passenger.getToPoint().getBuilding();
            int toFloor = passenger.getToPoint().getFloor();
            int minPathLength = 100000;
            int midFloor = -1;
            synchronized (this) {
                for (int i = 1;i <= 10;i++) {
                    for (Elevator elevator : elevators) {
                        if (elevator instanceof CircularElevator) {
                            CircularElevator circularElevator = (CircularElevator) elevator;
                            if (circularElevator.hasPoint(new Point(fromBuilding, i)) &&
                                circularElevator.hasPoint(new Point(toBuilding, i))) {
                                int pathLength = Math.abs(i - fromFloor) + Math.abs(i - toFloor);
                                if (pathLength < minPathLength) {
                                    minPathLength = pathLength;
                                    midFloor = i;
                                }
                            }
                        }
                    }
                }
                if (midFloor != fromFloor) {
                    passenger.getPath().add(new Point(fromBuilding, midFloor));
                }
                passenger.getPath().add(new Point(toBuilding, midFloor));
                if (midFloor != toFloor) {
                    passenger.getPath().add(passenger.getToPoint());
                }
                /*
                if (passenger.getId() == 107529291) {
                    OutputHandler.getInstance().test(passenger.getId() +
                            ": " + passenger.getFromPoint().toString());
                    for (Point point : passenger.getPath()) {
                        OutputHandler.getInstance().test(" " + point);
                    }
                }
                 */
                passenger.setToPoint(passenger.getPath().get(0));
                passenger.getPath().remove(0);
                /*
                OutputHandler.getInstance().test("passenger: " + passenger.getId() +
                        " toPoint； " + passenger.getToPoint());
                 */
            }
        }
    }

    public synchronized void startAll() {
        for (Elevator elevator : elevators) {
            new Thread(elevator).start();
        }
        // OutputHandler.getInstance().test(elevators.size() + " elevator started!");
    }

    /*
    public synchronized boolean noTransfer() {
        for (Elevator elevator : elevators) {
            if (elevator.hasTransfer()) {
                return false;
            }
        }
        return true;
    }
     */

    public void endAllElevator() {
        for (Elevator elevator : elevators) {
            elevator.end();
        }
    }

    /*
    public void notifyAllQueue() {
        for (Elevator elevator : elevators) {
            if (!elevator.isEnd()) {
                synchronized (elevator.getWaitingPassengers()) {
                    elevator.getWaitingPassengers().notifyAll();
                }
            }
        }
        Simulator.getInstance().notifyCommingPassengerQueue();
    }
     */
}
