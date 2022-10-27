package work;

import java.util.ArrayList;

public class ElevatorPool {
    private final ArrayList<Elevator> elevators = new ArrayList<>();

    public int getSize() {
        return elevators.size();
    }

    public void addOneElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    public synchronized void dispatch(Passenger passenger) {
        for (Elevator elevator : elevators) {
            if (elevators.indexOf(elevator) == passenger.getFromPoint().getBuilding() - 'A') {
                elevator.passengerInQueue(passenger);
                notifyAll();
                return;
            }
        }
        elevators.get(0).passengerInQueue(passenger);
        notifyAll();
    }

    public void startAll() {
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
