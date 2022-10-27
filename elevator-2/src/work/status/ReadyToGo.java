package work.status;

import work.Elevator;

public class ReadyToGo implements Status {
    @Override
    public void act(Elevator elevator) {
        elevator.getStrategy().readyToGoAct(elevator);
    }

    @Override
    public String toString() {
        return "ReadyToGO";
    }
}
