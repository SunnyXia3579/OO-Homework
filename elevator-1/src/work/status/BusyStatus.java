package work.status;

import work.Elevator;

public class BusyStatus implements Status {
    @Override
    public void act(Elevator elevator) {
    }

    @Override
    public String toString() {
        return "Busy";
    }
}
