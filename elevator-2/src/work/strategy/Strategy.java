package work.strategy;

import work.Elevator;

public interface Strategy {
    void arrivedAtPointAct(Elevator elevator);

    void openingAct(Elevator elevator);

    void readyToGoAct(Elevator elevator);
}
