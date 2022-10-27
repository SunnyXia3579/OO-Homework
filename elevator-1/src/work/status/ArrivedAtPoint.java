package work.status;

import work.Elevator;

public class ArrivedAtPoint implements Status {
    @Override
    public void act(Elevator elevator) {
        elevator.getStrategy().arrivedAtPointAct(elevator);
    }

    @Override
    public String toString() {
        return "ArrivedAtPoint";
    }
}
