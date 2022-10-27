package work;

import java.util.ArrayList;

public class ElevatorPool {
    private final ArrayList<Elevator> elevators = new ArrayList<>();

    public synchronized void addOneElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    public synchronized void dispatch(Passenger passenger) {
        ArrayList<Elevator> rawElevators = new ArrayList<>();
        Elevator fineElevator = null;
        int min = 100000;
        for (Elevator elevator : elevators) {
            if ((passenger.getFromPoint().getBuilding() == passenger.getToPoint().getBuilding() &&
                elevator.getCurrentPoint().getBuilding() == passenger.getToPoint().getBuilding() &&
                !elevator.isCircular()) ||
                (elevator.isCircular() &&
                passenger.getFromPoint().getFloor() == passenger.getToPoint().getFloor() &&
                elevator.getCurrentPoint().getFloor() == passenger.getToPoint().getFloor())) {
                rawElevators.add(elevator);
            }
        }
        for (Elevator elevator : rawElevators) {
            if (elevator.getWaitingPassengers().getSize() +
                elevator.getRidingPassengers().getSize() < min) {
                min = elevator.getWaitingPassengers().getSize() +
                      elevator.getRidingPassengers().getSize();
                fineElevator = elevator;
            }
        }
        fineElevator.passengerInQueue(passenger);
        notifyAll();
    }

    public synchronized void startAll() {
        for (Elevator elevator : elevators) {
            new Thread(elevator).start();
        }
    }

    public void endAllWaitingPassenger() {
        for (Elevator elevator : elevators) {
            elevator.getWaitingPassengers().end();
        }
    }
}
