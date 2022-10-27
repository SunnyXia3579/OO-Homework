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

        elevator.updateDirection();

        if (elevator.getRidingPassengers().isEmpty() &&
            elevator.getWaitingPassengers().isEmpty() &&
            elevator.getCurrentPointIndex() == 4) {
            elevator.halt();
        }

        if (elevator.whetherToOpen()) {
            elevator.setStatus(new OpeningStatus());
            elevator.doorOpen();
        } else {
            elevator.setStatus(new ReadyToGo());
        }
    }

    @Override
    public void openingAct(Elevator elevator) {
        elevator.doorClose();
        elevator.setStatus(new ReadyToGo());
    }

    public void readyToGoAct(Elevator elevator) {
        if (checkEnd(elevator)) {
            elevator.end();
            return;
        }
        nextDestination(elevator);
        elevator.updateDirection();
        if (elevator.getDirection() == 1) {
            elevator.setStatus(new BusyStatus());
            elevator.stepForward();
        } else if (elevator.getDirection() == -1) {
            elevator.setStatus(new BusyStatus());
            elevator.stepBack();
        }
        elevator.setStatus(new ArrivedAtPoint());
    }

    private boolean checkEnd(Elevator elevator) {
        return elevator.getRidingPassengers().isEmpty() &&
                elevator.getWaitingPassengers().isEmpty() &&
                elevator.getWaitingPassengers().isEnd();
    }

    private void nextDestination(Elevator elevator) {
        int highestPointIndex;
        int lowestPointIndex;
        if (elevator.getDirection() == 0) {
            highestPointIndex = elevator.findExtremeToPointIndex(1);
            lowestPointIndex = elevator.findExtremeToPointIndex(0);
            if (Math.abs(highestPointIndex - elevator.getCurrentPointIndex()) >
                Math.abs(elevator.getCurrentPointIndex() - lowestPointIndex)) {
                elevator.setDestination(highestPointIndex);
                return;
            }
            elevator.setDestination(lowestPointIndex);
            return;
        } else if (elevator.getDirection() == 1) {
            elevator.setDestination(elevator.findExtremeToPointIndex(1));
            return;
        }
        elevator.setDestination(elevator.findExtremeToPointIndex(0));
    }
}
