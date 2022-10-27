package work;

import work.point.Point;
import work.status.StartStatus;
import work.status.Status;
import work.strategy.Look;
import work.strategy.Strategy;

import java.util.ArrayList;
import java.util.Random;

import work.point.ElevatorPoint;

public abstract class Elevator implements Runnable {
    private final int id;
    private final int capacity;
    private final double closingTime;
    private final double openingTime;
    private final double movingTime;
    private final ArrayList<ElevatorPoint> route;
    private final Timer timer;
    private final Random random = new Random();

    private int destinationIndex;
    private final Strategy strategy = new Look();
    private int currentPointIndex;
    private int direction;
    private Status status;
    private final PassengerQueue ridingPassengers = new PassengerQueue();
    private final PassengerQueue waitingPassengers = new PassengerQueue();
    private volatile boolean isEnd = false;
    // private volatile boolean hasTransfer = false;

    public Elevator(int id, int capacity, double closingTime,
                    double openingTime, double movingTime, ArrayList<ElevatorPoint> route,
                    Point initialPoint) {
        this.id = id;
        this.capacity = capacity;
        this.closingTime = closingTime;
        this.openingTime = openingTime;
        this.movingTime = movingTime;
        this.route = route;
        this.currentPointIndex = indexOfPoint(initialPoint);
        this.status = new StartStatus();
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

    public Strategy getStrategy() {
        return strategy;
    }

    public int getDirection() {
        return direction;
    }

    protected Random getRandom() {
        return random;
    }

    protected int getDestinationIndex() {
        return destinationIndex;
    }

    protected double getMovingTime() {
        return movingTime;
    }

    public int getId() {
        return id;
    }

    /*
    public synchronized boolean isEnd() {
        return isEnd;
    }
     */

    public PassengerQueue getWaitingPassengers() {
        return waitingPassengers;
    }

    public PassengerQueue getRidingPassengers() {
        return ridingPassengers;
    }

    protected ArrayList<ElevatorPoint> getRoute() {
        return route;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    protected void setDirection(int direction) {
        this.direction = direction;
    }

    public void setDestination(int destinationIndex) {
        this.destinationIndex = destinationIndex;
    }

    protected void setCurrentPointIndex(int currentPointIndex) {
        this.currentPointIndex = currentPointIndex;
    }

    public abstract int findExtremeToPointIndex(int highestOrLowest);

    protected abstract int findExtremeToPointIndexInOneQueue(int queueType, int highestOrLowest);

    protected int indexOfPoint(Point point) {
        for (int i = 0;i < route.size();i++) {
            if (route.get(i).getPoint().equals(point)) {
                return i;
            }
        }
        return -1;
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
        synchronized (waitingPassengers) {
            for (Passenger passenger : waitingPassengers.getPassengers()) {
                if (pointOnTheWay(passenger.getToPoint()) &&
                        passenger.getFromPoint().equals(getCurrentPoint())) {
                    return true;
                }
            }
        }
        return false;
    }

    public abstract boolean pointOnTheWay(Point point);

    private synchronized boolean isEnd() {
        return isEnd;
    }

    @Override
    public void run() {
        while (!isEnd()) {
            /*
            if (id == 6) {
                OutputHandler.getInstance().test("elevator " + id + " " + status +
                        "\nwaiting: " + waitingPassengers.getSize() +
                        " riding: " + ridingPassengers.getSize() +
                        " direction: " + direction + "\n");
            }
            */

            status.act(this);
        }
        // OutputHandler.getInstance().test("elevator " + id + " end");
    }

    public abstract void updateDirection();

    public abstract void stepForward();

    public abstract void stepBack();

    public void doorOpen() {
        OutputHandler.getInstance().open(route.get(currentPointIndex).getBuilding(),
                                         route.get(currentPointIndex).getFloor(),
                                         id);
        passengerOut();
        halt(openingTime);
    }

    public void doorClose() {
        halt(closingTime);
        OutputHandler.getInstance().close(route.get(currentPointIndex).getBuilding(),
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
                        OutputHandler.getInstance().in(getCurrentPoint().getBuilding(),
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
                if (Simulator.getInstance().isEnd()) {
                    return;
                }
                try {
                    waitingPassengers.wait();
                    // OutputHandler.getInstance().test("elevator " + id + " wake up!");
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
            OutputHandler.getInstance().out(getCurrentPoint().getBuilding(),
                    getCurrentPoint().getFloor(),
                    id,
                    passenger.getId());
            Simulator.getInstance().passengerComeFromElevator(passenger);
        }
        /*
        if (hasTransfer) {
            updateTransfer();
        }
         */
    }

    /*
    public void updateTransfer() {
        synchronized (waitingPassengers) {
            for (Passenger passenger : waitingPassengers.getPassengers()) {
                if (!passenger.getPath().isEmpty()) {
                    hasTransfer = true;
                    return;
                }
            }
        }
        for (Passenger passenger : ridingPassengers.getPassengers()) {
            if (!passenger.getPath().isEmpty()) {
                hasTransfer = true;
                return;
            }
        }
        hasTransfer = false;
        if (ElevatorPool.getInstance().noTransfer() && Simulator.getInstance().isInputEnd()) {
            ElevatorPool.getInstance().notifyAllQueue();
        }
    }
    */

    public void passengerInQueue(Passenger passenger) {
        /*
        if (!passenger.getPath().isEmpty()) {
            hasTransfer = true;
        }
         */
        waitingPassengers.addOnePassenger(passenger);
    }

    public boolean isFull() {
        return ridingPassengers.getSize() == capacity;
    }

    public synchronized void end() {
        isEnd = true;
        synchronized (waitingPassengers) {
            waitingPassengers.notifyAll();
        }
    }

    /*
    public synchronized boolean hasTransfer() {
        return !isEnd && hasTransfer;
    }
     */
}
