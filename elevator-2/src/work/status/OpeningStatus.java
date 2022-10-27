package work.status;

import work.Elevator;

public class OpeningStatus implements Status {
    @Override
    public void act(Elevator elevator) {
        elevator.getStrategy().openingAct(elevator);
    }

    @Override
    public String toString() {
        return "Opening";
    }
}
