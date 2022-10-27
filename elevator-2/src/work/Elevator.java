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
    private final boolean isCircular;
    private final Simulator simulator;
    private final Timer timer;
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
                    Point initialPoint, boolean isCircular) {
        this.id = id;
        this.capacity = capacity;
        this.closingTime = closingTime;
        this.openingTime = openingTime;
        this.movingTime = movingTime;
        this.route = route;
        this.currentPointIndex = indexOfPoint(initialPoint);
        this.status = new ArrivedAtPoint();
        this.simulator = Simulator.getInstance();
        this.timer = new Timer(openingTime, this);
        this.direction = 0;
        this.destinationIndex = currentPointIndex;
        this.isCircular = isCircular;
    }

    public Point getCurrentPoint() {
        return route.get(currentPointIndex).getPoint();
    }

    public int getCurrentPointIndex() {
        return currentPointIndex;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public int getDirection() {
        return direction;
    }

    public int getDestinationIndex() {
        return destinationIndex;
    }

    public PassengerQueue getWaitingPassengers() {
        return waitingPassengers;
    }

    public PassengerQueue getRidingPassengers() {
        return ridingPassengers;
    }

    public int getId() {
        return id;
    }

    public boolean isCircular() {
        return isCircular;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDestination(int destinationIndex) {
        this.destinationIndex = destinationIndex;
    }

    public int findExtremeToPointIndex(int highestOrLowest) {
        int extremeOfWaitingQueue = findExtremeToPointIndexInOneQueue(0, highestOrLowest);
        int extremeOfRidingQueue = findExtremeToPointIndexInOneQueue(1, highestOrLowest);
        /*
        if (id == 5) {
            OutputHandler.getInstance().test("\nhighestOrLowest: " + highestOrLowest +
                    " waitingQueue: " + extremeOfWaitingQueue +
                    " ridingQueue: " + extremeOfRidingQueue + "\n");
        }
         */
        if (extremeOfRidingQueue == -1) {
            if (extremeOfWaitingQueue == -1) {
                return 4;
            }
            return extremeOfWaitingQueue;
        }
        if (extremeOfWaitingQueue == -1 || isFull()) {
            return extremeOfRidingQueue;
        }
        if (highestOrLowest == 1) {
            if (!isCircular) {
                return Math.max(extremeOfWaitingQueue, extremeOfRidingQueue);
            }
            if (circularDistance(extremeOfWaitingQueue) > circularDistance(extremeOfRidingQueue)) {
                return extremeOfWaitingQueue;
            }
            return extremeOfRidingQueue;
        }
        if (!isCircular) {
            return Math.min(extremeOfWaitingQueue, extremeOfRidingQueue);
        }
        if (circularDistance(extremeOfWaitingQueue) < circularDistance(extremeOfRidingQueue)) {
            return extremeOfWaitingQueue;
        }
        return extremeOfRidingQueue;
    }

    private int findExtremeToPointIndexInOneQueue(int queueType, int highestOrLowest) {
        PassengerQueue passengers = ((queueType == 0) ? waitingPassengers : ridingPassengers);
        if (passengers.isEmpty()) {
            return -1;
        }

        synchronized (waitingPassengers) {
            int extremePointIndex;
            if (!isCircular) {
                extremePointIndex = ((highestOrLowest == 1) ? 0 : route.size() - 1);
                for (Passenger passenger : passengers.getPassengers()) {
                    int toPointIndex = ((queueType == 0) ? indexOfPoint(passenger.getFromPoint()) :
                            indexOfPoint(passenger.getToPoint()));
                    if ((highestOrLowest == 1 && toPointIndex > extremePointIndex) ||
                            (highestOrLowest == 0 && toPointIndex < extremePointIndex)) {
                        extremePointIndex = toPointIndex;
                    }
                }
            } else {
                int highest = (currentPointIndex < 2) ?
                        currentPointIndex + 3 : currentPointIndex - 2;
                int lowest = (currentPointIndex <= 2) ?
                        currentPointIndex + 2 : currentPointIndex - 3;
                extremePointIndex = (highestOrLowest == 1) ? highest : lowest;
                for (Passenger passenger : passengers.getPassengers()) {
                    int toPointIndex = ((queueType == 0) ? indexOfPoint(passenger.getFromPoint()) :
                            indexOfPoint(passenger.getToPoint()));
                    if ((highestOrLowest == 1 &&
                        circularDistance(toPointIndex) > circularDistance(extremePointIndex)) ||
                        (highestOrLowest == 0 &&
                         circularDistance(toPointIndex) < circularDistance(extremePointIndex))) {
                        extremePointIndex = toPointIndex;
                    }
                }
            }
            return extremePointIndex;
        }
    }

    private int circularDistance(int index) {
        if (index > currentPointIndex + 2) {
            return -(currentPointIndex + (5 - index));
        } else if (index < currentPointIndex - 2) {
            return index + (5 - currentPointIndex);
        }
        return index - currentPointIndex;
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
        return false;
    }

    public boolean pointOnTheWay(Point point) {
        if (direction == 0) {
            for (ElevatorPoint elevatorPoint : route) {
                if (elevatorPoint.getPoint().equals(point)) {
                    return true;
                }
            }
            // System.out.println("1111111111111111111111111111111");
            return false;
        } else if (direction == 1) {
            for (int i = currentPointIndex;i < route.size();i++) {
                if (route.get(i).getPoint().equals(point)) {
                    return true;
                }
            }
            // System.out.println("333333333333333333333333333333333333333");
            return false;
        }
        for (int i = 0;i < currentPointIndex;i++) {
            if (route.get(i).getPoint().equals(point)) {
                return true;
            }
        }
        // System.out.println("222222222222222222222222222222222222222222222222");
        return false;
    }

    @Override
    public void run() {
        while (!isEnd) {
            /*
            if (id == 7 && status.toString().equals("ArrivedAtPoint")) {
                synchronized (OutputHandler.getInstance()) {
                    OutputHandler.getInstance().test("\n" + id + ": " + status +
                            " ridingPassenger: " + ridingPassengers.getSize() +
                            " waitingPassenger: " + waitingPassengers.getSize() +
                            " currentPointIndex: " + currentPointIndex +
                            " destionation: " + destinationIndex + "\nwaiting:");
                    for (Passenger passenger : waitingPassengers.getPassengers()) {
                        OutputHandler.getInstance().test(passenger.toString());
                    }
                    OutputHandler.getInstance().test("riding:");
                    for (Passenger passenger : ridingPassengers.getPassengers()) {
                        OutputHandler.getInstance().test(passenger.toString());
                    }
                    OutputHandler.getInstance().test("");
                }
            }
            */
            status.act(this);
        }
    }

    public void updateDirection() {
        if (!isCircular) {
            direction = Integer.compare(destinationIndex, currentPointIndex);
        } else {
            direction = Integer.signum(circularDistance(destinationIndex));
        }
    }

    public void stepForward() {
        currentPointIndex = (isCircular && currentPointIndex == route.size() - 1) ?
                0 : currentPointIndex + 1;
        int cnt = 1;
        while ((currentPointIndex < route.size()) && !route.get(currentPointIndex).isReachable()) {
            currentPointIndex = (currentPointIndex == route.size() - 1) ? 0 : currentPointIndex + 1;
            cnt++;
        }
        try {
            Thread.sleep((int) (1000 * (movingTime * cnt)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outputHandler.arrive(route.get(currentPointIndex).getBuilding(),
                             route.get(currentPointIndex).getFloor(),
                             id);
    }

    public void stepBack() {
        currentPointIndex =  (isCircular && currentPointIndex == 0) ?
                route.size() - 1 : currentPointIndex - 1;
        int cnt = 1;
        while ((currentPointIndex >= 0) && !route.get(currentPointIndex).isReachable()) {
            currentPointIndex = (currentPointIndex == 0) ? route.size() - 1 : currentPointIndex - 1;
            cnt++;
        }
        try {
            Thread.sleep((int) (1000 * (movingTime * cnt)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outputHandler.arrive(route.get(currentPointIndex).getBuilding(),
                             route.get(currentPointIndex).getFloor(),
                             id);
        /*
        if (id == 5 && waitingPassengers.getSize() == 2) {
            System.out.println("waitingPassengers.getSize(): " + waitingPassengers.getSize() + " " +
                    "ridingPassengers.getSize(): " + ridingPassengers.getSize() +
                    " who is waiting: " + waitingPassengers.getPassengers().get(0).getId() + " & " +
                    waitingPassengers.getPassengers().get(1).getId());
        }
        */
    }

    public void doorOpen() {
        outputHandler.open(route.get(currentPointIndex).getBuilding(),
                           route.get(currentPointIndex).getFloor(),
                           id);
        passengerOut();
        halt(openingTime);
    }

    public void doorClose() {
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
                        getCurrentPoint(), capacity - ridingPassengers.getSize(), this);
                if (passengersToRide.size() == 0) {
                    try {
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
