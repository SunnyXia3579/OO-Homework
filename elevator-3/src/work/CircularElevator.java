package work;

import work.point.ElevatorPoint;
import work.point.Point;

import java.util.ArrayList;

public class CircularElevator extends Elevator {
    public CircularElevator(int id, int capacity, double closingTime,
                            double openingTime, double movingTime, ArrayList<ElevatorPoint> route,
                            Point initialPoint) {
        super(id, capacity, closingTime, openingTime, movingTime, route, initialPoint);
    }

    public int findExtremeToPointIndex(int highestOrLowest) {
        int extremeOfWaitingQueue = findExtremeToPointIndexInOneQueue(0, highestOrLowest);
        int extremeOfRidingQueue = findExtremeToPointIndexInOneQueue(1, highestOrLowest);

        /*
        if (getId() == 7) {
            OutputHandler.getInstance().test(getRidingPassengers().getPassengers().size() +
                    " " + extremeOfRidingQueue);
        }
         */

        if (extremeOfRidingQueue == -1) {
            if (extremeOfWaitingQueue == -1) {
                for (int i = getRoute().size() - 1;i >= 0;i--) {
                    if (getRoute().get(i).isReachable()) {
                        return (getRandom().nextInt(2) == 0) ? i : getCurrentPointIndex();
                    }
                }
            }
            return extremeOfWaitingQueue;
        }
        if (extremeOfWaitingQueue == -1 || isFull()) {
            return extremeOfRidingQueue;
        }
        if (highestOrLowest == 1) {
            if (circularDistance(extremeOfWaitingQueue) > circularDistance(extremeOfRidingQueue)) {
                return extremeOfWaitingQueue;
            }
            return extremeOfRidingQueue;
        }
        if (circularDistance(extremeOfWaitingQueue) < circularDistance(extremeOfRidingQueue)) {
            return extremeOfWaitingQueue;
        }
        return extremeOfRidingQueue;
    }

    protected int findExtremeToPointIndexInOneQueue(int queueType, int highestOrLowest) {
        PassengerQueue passengers = (queueType == 0) ?
                                    getWaitingPassengers() : getRidingPassengers();
        if (passengers.isEmpty()) {
            return -1;
        }

        synchronized (getWaitingPassengers()) {
            int extremePointIndex = -1;
            int highest = initHighest();
            int lowest = initLowest();

            extremePointIndex = (highestOrLowest == 1) ? highest : lowest;
            for (Passenger passenger : passengers.getPassengers()) {
                int toPointIndex = ((queueType == 0) ? indexOfPoint(passenger.getFromPoint()) :
                                    indexOfPoint(passenger.getToPoint()));
                if ((highestOrLowest == 1 &&
                    circularDistance(toPointIndex) > circularDistance(extremePointIndex)) ||
                    (highestOrLowest == 0 &&
                    circularDistance(toPointIndex) < circularDistance(extremePointIndex)) &&
                    getRoute().get(toPointIndex).isReachable()) {
                    extremePointIndex = toPointIndex;
                }
            }
            return extremePointIndex;
        }
    }

    private int initHighest() {
        int highest;
        switch (getCurrentPointIndex()) {
            case 0:
                if (getRoute().get(3).isReachable()) {
                    highest = 3;
                } else if (getRoute().get(4).isReachable()) {
                    highest = 4;
                } else {
                    highest = 0;
                }
                break;
            case 1:
                if (getRoute().get(4).isReachable()) {
                    highest = 4;
                } else if (getRoute().get(0).isReachable()) {
                    highest = 0;
                } else {
                    highest = 1;
                }
                break;
            case 2:
                if (getRoute().get(0).isReachable()) {
                    highest = 0;
                } else if (getRoute().get(1).isReachable()) {
                    highest = 1;
                } else {
                    highest = 2;
                }
                break;
            case 3:
                if (getRoute().get(1).isReachable()) {
                    highest = 1;
                } else if (getRoute().get(2).isReachable()) {
                    highest = 2;
                } else {
                    highest = 3;
                }
                break;
            default:
                if (getRoute().get(2).isReachable()) {
                    highest = 2;
                } else if (getRoute().get(3).isReachable()) {
                    highest = 3;
                } else {
                    highest = 4;
                }
        }
        return highest;
    }

    private int initLowest() {
        int lowest;
        switch (getCurrentPointIndex()) {
            case 0:
                if (getRoute().get(2).isReachable()) {
                    lowest = 2;
                } else if (getRoute().get(1).isReachable()) {
                    lowest = 1;
                } else {
                    lowest = 0;
                }
                break;
            case 1:
                if (getRoute().get(3).isReachable()) {
                    lowest = 3;
                } else if (getRoute().get(2).isReachable()) {
                    lowest = 2;
                } else {
                    lowest = 1;
                }
                break;
            case 2:
                if (getRoute().get(4).isReachable()) {
                    lowest = 4;
                } else if (getRoute().get(3).isReachable()) {
                    lowest = 3;
                } else {
                    lowest = 2;
                }
                break;
            case 3:
                if (getRoute().get(0).isReachable()) {
                    lowest = 0;
                } else if (getRoute().get(4).isReachable()) {
                    lowest = 4;
                } else {
                    lowest = 3;
                }
                break;
            default:
                if (getRoute().get(1).isReachable()) {
                    lowest = 1;
                } else if (getRoute().get(0).isReachable()) {
                    lowest = 0;
                } else {
                    lowest = 4;
                }
        }
        return lowest;
    }

    private int circularDistance(int index) {
        if (index > getCurrentPointIndex() + 2) {
            return -(getCurrentPointIndex() + (5 - index));
        } else if (index < getCurrentPointIndex() - 2) {
            return index + (5 - getCurrentPointIndex());
        }
        return index - getCurrentPointIndex();
    }

    @Override
    public boolean pointOnTheWay(Point point) {
        if (true/*getDirection() == 0*/) {
            for (ElevatorPoint elevatorPoint : getRoute()) {
                if (elevatorPoint.getPoint().equals(point)) {
                    return true;
                }
            }
            return false;
        } else if (getDirection() == 1) {
            int i = getCurrentPointIndex();
            for (int cnt = 0;cnt < 2;cnt++) {
                if (getRoute().get(i).getPoint().equals(point)) {
                    return true;
                }
                i = (i == getRoute().size() - 1) ? 0 : i + 1;
            }
            return false;
        }
        int i = getCurrentPointIndex();
        for (int cnt = 0;cnt < 2;cnt++) {
            if (getRoute().get(i).getPoint().equals(point)) {
                return true;
            }
            i = (i == 0) ? getRoute().size() - 1 : i - 1;
        }
        return false;
    }

    @Override
    public void updateDirection() {
        setDirection(Integer.signum(circularDistance(getDestinationIndex())));
    }

    @Override
    public void stepForward() {
        try {
            Thread.sleep((int) (1000 * (getMovingTime())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setCurrentPointIndex(((getCurrentPointIndex() == getRoute().size() - 1) ?
                              0 : getCurrentPointIndex() + 1));
        OutputHandler.getInstance().arrive(
                getRoute().get(getCurrentPointIndex()).getBuilding(),
                getRoute().get(getCurrentPointIndex()).getFloor(),
                getId());
        // OutputHandler.getInstance().test("circular stepforward!");
        while (!getRoute().get(getCurrentPointIndex()).isReachable()) {
            try {
                Thread.sleep((int) (1000 * (getMovingTime())));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setCurrentPointIndex((getCurrentPointIndex() == getRoute().size() - 1) ?
                                 0 : getCurrentPointIndex() + 1);
            OutputHandler.getInstance().arrive(
                    getRoute().get(getCurrentPointIndex()).getBuilding(),
                    getRoute().get(getCurrentPointIndex()).getFloor(),
                    getId());
        }
    }

    @Override
    public void stepBack() {
        try {
            Thread.sleep((int) (1000 * (getMovingTime())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setCurrentPointIndex(((getCurrentPointIndex() == 0) ?
                              getRoute().size() - 1 : getCurrentPointIndex() - 1));
        OutputHandler.getInstance().arrive(
                getRoute().get(getCurrentPointIndex()).getBuilding(),
                getRoute().get(getCurrentPointIndex()).getFloor(),
                getId());
        // OutputHandler.getInstance().test("circular stepback!");
        while (!getRoute().get(getCurrentPointIndex()).isReachable()) {
            try {
                Thread.sleep((int) (1000 * (getMovingTime())));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setCurrentPointIndex(((getCurrentPointIndex() == 0) ?
                                  getRoute().size() - 1 : getCurrentPointIndex() - 1));
            OutputHandler.getInstance().arrive(
                    getRoute().get(getCurrentPointIndex()).getBuilding(),
                    getRoute().get(getCurrentPointIndex()).getFloor(),
                    getId());
        }
    }

    public boolean hasPoint(Point point) {
        if (indexOfPoint(point) == -1) {
            return false;
        }
        return getRoute().get(indexOfPoint(point)).isReachable();
    }
}
