package work;

import work.point.Point;

import java.util.ArrayList;
import java.util.Iterator;

public class PassengerQueue {
    private final ArrayList<Passenger> passengers = new ArrayList<>();
    private Boolean isEnd = false;

    public synchronized int getSize() {
        return passengers.size();
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public synchronized void addOnePassenger(Passenger passenger) {
        passengers.add(passenger);  // System.out.println("addOnePassenger: " + passenger);
        notifyAll();
    }

    public synchronized Passenger getOnePassenger(boolean wetherToWait) {
        assert wetherToWait;
        while (isEmpty()) {
            if (isEnd()) {
                // System.out.println("input end");
                return null;
            }
            try {
                // System.out.println("before wait");
                wait();
                // System.out.println("after wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Passenger passenger = passengers.get(0);
        passengers.remove(0);
        return passenger;
    }

    public ArrayList<Passenger> getRightPassenger(Point point, int maxPassenger) {
        Iterator<Passenger> iterator = passengers.iterator();
        int cnt = 0;
        ArrayList<Passenger> passengersToRide = new ArrayList<>();
        while (cnt < maxPassenger && iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getFromPoint().equals(point)) {
                passengersToRide.add(passenger);
                iterator.remove();
                cnt++;
            }
        }
        return passengersToRide;
    }

    public ArrayList<Passenger> outRightPassenger(Point point) {
        Iterator<Passenger> iterator = passengers.iterator();
        ArrayList<Passenger> passengersToOut = new ArrayList<>();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getToPoint().equals(point)) {
                passengersToOut.add(passenger);
                iterator.remove();
            }
        }
        return passengersToOut;
    }

    public synchronized void end() {
        isEnd = true;
        notifyAll();
    }

    public synchronized Boolean isEmpty() {
        return passengers.size() == 0;
    }

    public synchronized Boolean isEnd() {
        return isEnd;
    }
}
