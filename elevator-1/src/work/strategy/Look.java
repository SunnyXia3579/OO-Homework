package work.strategy;

import work.Elevator;
import work.status.OpeningStatus;
import work.status.ArrivedAtPoint;
import work.status.BusyStatus;
import work.status.ReadyToGo;

public class Look implements Strategy {
    @Override
    public void arrivedAtPointAct(Elevator elevator) {
        if (checkEnd(elevator)) {
            elevator.end();
            return;
        }
        updateDestination(elevator);
        // System.out.println("elevator.getDestinationIndex(): " + elevator.getDestinationIndex() +
        //                  " elevator.getCurrentPointIndex(): " + elevator.getCurrentPointIndex());
        // System.out.println("elevator getDirectionIndex(): " + elevator.getDirection());
        if (elevator.whetherToOpen()) {
            elevator.setStatus(new OpeningStatus());
            // OutputQueue.getInstance().out('Q', 9999, 9999, 9999);
            elevator.doorOpen();
        } else {
            elevator.setStatus(new ReadyToGo());
        }
    }

    @Override
    public void openingAct(Elevator elevator) {
        updateDestination(elevator);
        /*
        OutputQueue.getInstance().test(
                "updated destinationIndex: " + elevator.getDestinationIndex() +
                " waitingQueue size: " + elevator.getWaitingPassengers().getSize() +
                " ridingQueue size: " + elevator.getRidingPassengers().getSize());
         */
        if (elevator.getDirection() == 0) {
            elevator.halt();
        }
        // OutputQueue.getInstance().out('T', 1000, 1000, 1000);
        elevator.doorClose();
        if (checkEnd(elevator)) {
            elevator.end();
            return;
        }
        updateDestination(elevator);
        // System.out.println("updated destinationIndex: " + elevator.getDestinationIndex());
        elevator.setStatus(new ReadyToGo());
    }

    public void readyToGoAct(Elevator elevator) {
        if (elevator.getDirection() == 1) {
            elevator.setStatus(new BusyStatus());
            elevator.stepForward();
            elevator.setStatus(new ArrivedAtPoint());
        } else if (elevator.getDirection() == -1) {
            elevator.setStatus(new BusyStatus());
            elevator.stepBack();
            elevator.setStatus(new ArrivedAtPoint());
        }
        /* else {
            System.out.println("halt at pointIndex: " + elevator.getCurrentPointIndex());
            if (elevator.halt()) {
                elevator.doorOpen();
                elevator.setStatus(new OpeningStatus());
            }
        }*/
    }

    private boolean checkEnd(Elevator elevator) {
        return elevator.getRidingPassengers().isEmpty() &&
                elevator.getWaitingPassengers().isEmpty() &&
                elevator.getWaitingPassengers().isEnd();
    }

    private void updateDestination(Elevator elevator) {
        elevator.updateDirection();
        if (elevator.getDirection() == 0) {
            nextDestination(elevator);
            elevator.updateDirection();
        }
    }

    private void nextDestination(Elevator elevator) {
        int highestPointIndex;
        int lowestPointIndex;
        if (elevator.getDirection() == 0) {
            if (elevator.isFull()) {
                highestPointIndex = elevator.findExtremeToPointIndexInOneQueue(1, 1);
                lowestPointIndex = elevator.findExtremeToPointIndexInOneQueue(1, 0);
            } else {
                highestPointIndex = elevator.findExtremeToPointIndex(1);
                lowestPointIndex = elevator.findExtremeToPointIndex(0);
                // System.out.println(highestPointIndex + " " + lowestPointIndex);
            }
            if (highestPointIndex - elevator.getCurrentPointIndex() >
                elevator.getCurrentPointIndex() - lowestPointIndex) {
                elevator.setDestination(highestPointIndex);
                return;
            }
            elevator.setDestination(lowestPointIndex);
            return;
        } else if (elevator.getDirection() == 1) {
            if (elevator.isFull()) {
                elevator.setDestination(elevator.findExtremeToPointIndexInOneQueue(1, 1));
                return;
            }
            elevator.setDestination(elevator.findExtremeToPointIndex(1));
        }
        if (elevator.isFull()) {
            elevator.setDestination(elevator.findExtremeToPointIndexInOneQueue(1, 0));
            return;
        }
        elevator.setDestination(elevator.findExtremeToPointIndex(0));
    }
}
