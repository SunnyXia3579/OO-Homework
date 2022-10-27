package work;

import work.point.Point;
import work.status.ArrivedAtPoint;
import work.status.Status;
import work.strategy.Look;
import work.strategy.Strategy;

import java.util.ArrayList;

import work.point.ElevatorPoint;

public class Elevator implements Runnable {
    private final int id;
    private final int capacity;
    private final double closingTime;
    private final double openingTime;
    private final double movingTime;
    private final ArrayList<ElevatorPoint> route;
    private final Simulator simulator;
    private final Timer timer;
    // private final OutputQueue outputQueue = OutputQueue.getInstance();
    private final OutputHandler outputHandler = OutputHandler.getInstance();

    private int destinationIndex;
    private final Strategy strategy = new Look();
    private int currentPointIndex;
    private int direction;
    private Status status;
    private final PassengerQueue ridingPassengers = new PassengerQueue();
    private final PassengerQueue waitingPassengers = new PassengerQueue();
    private boolean isEnd = false;

    public Elevator(int id, int capacity, double closingTime,
                    double openingTime, double movingTime, ArrayList<ElevatorPoint> route,
                    char initialBuilding, int initialFloor) {
        this.id = id;
        this.capacity = capacity;
        this.closingTime = closingTime;
        this.openingTime = openingTime;
        this.movingTime = movingTime;
        this.route = route;
        this.currentPointIndex = indexOfPoint(new Point(initialBuilding, initialFloor));
        this.status = new ArrivedAtPoint();
        this.simulator = Simulator.getInstance();
        this.timer = new Timer(openingTime, this);
        this.direction = 0;
        this.destinationIndex = currentPointIndex;
    }

    public Point getCurrentPoint() {
        return route.get(currentPointIndex).getPoint();
    }

    public int getCurrentPointIndex() {
        return currentPointIndex;
    }

    public int getDestinationIndex() {
        return destinationIndex;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public int getDirection() {
        return direction;
    }

    public PassengerQueue getWaitingPassengers() {
        return waitingPassengers;
    }

    public PassengerQueue getRidingPassengers() {
        return ridingPassengers;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDestination(int destinationIndex) {
        this.destinationIndex = destinationIndex;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int findExtremeToPointIndex(int highestOrLowest) {
        // System.out.println("waitingQueue size: " + waitingPassengers.getSize() +
        // "ridingQueue size: " + ridingPassengers.getSize());
        int extremeOfWaitingQueue = findExtremeToPointIndexInOneQueue(0, highestOrLowest);
        int extremeOfRidingQueue = findExtremeToPointIndexInOneQueue(1, highestOrLowest);
        if (extremeOfRidingQueue == -1) {
            if (extremeOfWaitingQueue == -1) {
                return 4;
            }
            // System.out.println("extremeOfWaitingQueue: " + extremeOfWaitingQueue);
            return extremeOfWaitingQueue;
        }
        if (extremeOfWaitingQueue == -1 || isFull()) {
            // System.out.println("extremeOfRidingQueue: " + extremeOfRidingQueue);
            return extremeOfRidingQueue;
        }
        if (highestOrLowest == 1) {
            return Math.max(extremeOfWaitingQueue, extremeOfRidingQueue);
        }
        return Math.min(extremeOfWaitingQueue, extremeOfRidingQueue);
    }

    public int findExtremeToPointIndexInOneQueue(int queueType, int highestOrLowest) {
        PassengerQueue passengers = ((queueType == 0) ? waitingPassengers : ridingPassengers);
        if (passengers.isEmpty()) {
            return -1;
        }
        int extremePointIndex = ((highestOrLowest == 1) ? 0 : route.size() - 1);
        for (Passenger passenger : passengers.getPassengers()) {
            int toPointIndex = ((queueType == 0) ? indexOfPoint(passenger.getFromPoint()) :
                                                   indexOfPoint(passenger.getToPoint()));
            if ((highestOrLowest == 1 && toPointIndex > extremePointIndex) ||
                (highestOrLowest == 0 && toPointIndex < extremePointIndex)) {
                extremePointIndex = toPointIndex;
            }
        }
        return extremePointIndex;
    }

    private int indexOfPoint(Point point) {
        for (int i = 0;i < route.size();i++) {
            if (route.get(i).getPoint().equals(point)) {
                return i;
            }
        }
        return 1 << 31;
    }

    public boolean whetherToOpen() {
        /* System.out.println("currentPointIndex: " + currentPointIndex +
                           " ridingPassengers size: " + ridingPassengers.getSize() +
                           " waitingPassengers size: " + waitingPassengers.getSize());
         */
        for (Passenger passenger : ridingPassengers.getPassengers()) {
            if (currentPointIndex == indexOfPoint(passenger.getToPoint())) {
                return true;
            }
        }
        if (isFull()) {
            return false;
        }
        for (Passenger passenger : waitingPassengers.getPassengers()) {
            if (pointOnTheWay(passenger.getToPoint()) &&
                passenger.getFromPoint().equals(getCurrentPoint())) {
                return true;
            }
        }
        return direction == 0;
    }

    private boolean pointOnTheWay(Point point) {
        if (direction == 0) {
            return true;
        } else if (direction == 1) {
            for (int i = currentPointIndex;i < route.size();i++) {
                if (route.get(i).getPoint().equals(point)) {
                    return true;
                }
            }
            return false;
        }
        for (int i = 0;i < currentPointIndex;i++) {
            if (route.get(i).getPoint().equals(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        while (!isEnd) {
            // System.out.println("elevator isEnd ? " + isEnd);
            // System.out.println("\nelevator status: " + status);
            status.act(this);
        }
        // outputQueue.eneElevatorEnd();
    }

    public void updateDirection() {
        direction = Integer.compare(destinationIndex, currentPointIndex);
    }

    public void stepForward() {
        // System.out.println("stepForward");
        int start = currentPointIndex++;
        while (currentPointIndex < route.size() && !route.get(currentPointIndex).isReachable()) {
            currentPointIndex++;
        }
        try {
            Thread.sleep((int) (1000 * (movingTime * (currentPointIndex - start))));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outputHandler.arrive(route.get(currentPointIndex).getBuilding(),
                             route.get(currentPointIndex).getFloor(),
                             id);
    }

    public void stepBack() {
        int start = currentPointIndex--;
        while (currentPointIndex >= 0 && !route.get(currentPointIndex).isReachable()) {
            currentPointIndex--;
        }
        try {
            Thread.sleep((int) (1000 * (movingTime * (start - currentPointIndex))));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outputHandler.arrive(route.get(currentPointIndex).getBuilding(),
                             route.get(currentPointIndex).getFloor(),
                             id);
    }

    public void doorOpen() {
        outputHandler.open(route.get(currentPointIndex).getBuilding(),
                           route.get(currentPointIndex).getFloor(),
                           id);
        passengerOut();
        // OutputQueue.getInstance().test("door open before halt");
        halt(openingTime);
    }

    public void doorClose() {
        // OutputQueue.getInstance().test("door close before halt");
        halt(closingTime);
        outputHandler.close(route.get(currentPointIndex).getBuilding(),
                            route.get(currentPointIndex).getFloor(),
                            id);
    }

    public void halt(double time) {
        timer.setDuration(time);
        new Thread(timer).start();
        synchronized (waitingPassengers) {
            while (true) {
                ArrayList<Passenger> passengersToRide = waitingPassengers.getRightPassenger(
                        getCurrentPoint(), capacity - ridingPassengers.getSize());
                // System.out.println(currentPointIndex + " " + passenger);
                // OutputQueue.getInstance().test("passengerToRide size: " +
                // passengersToRide.size());
                if (passengersToRide.size() == 0) {
                    try {
                        // OutputQueue.getInstance().test("wait: ");
                        waitingPassengers.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (Passenger passenger : passengersToRide) {
                        ridingPassengers.addOnePassenger(passenger);
                        outputHandler.in(getCurrentPoint().getBuilding(),
                                         getCurrentPoint().getFloor(),
                                         id,
                                         passenger.getId());
                    }
                }
                if (timer.isStopped()) {
                    return;
                }
            }
        }
    }

    public void halt() {
        synchronized (waitingPassengers) {
            while (waitingPassengers.isEmpty()) {
                if (waitingPassengers.isEnd()) {
                    return;
                }
                try {
                    waitingPassengers.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void passengerOut() {
        ArrayList<Passenger> passengersToOut = ridingPassengers
                                               .outRightPassenger(getCurrentPoint());
        for (Passenger passenger : passengersToOut) {
            simulator.passengerComeFromElevator(passenger);
            outputHandler.out(getCurrentPoint().getBuilding(),
                              getCurrentPoint().getFloor(),
                              id,
                              passenger.getId());
        }
    }

    public void passengerInQueue(Passenger passenger) {
        waitingPassengers.addOnePassenger(passenger);
    }

    public boolean isFull() {
        return ridingPassengers.getSize() == capacity;
    }

    public void end() {
        isEnd = true;
    }
}
