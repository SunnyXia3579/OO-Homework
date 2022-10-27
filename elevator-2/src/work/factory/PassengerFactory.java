package work.factory;

import work.Passenger;

public class PassengerFactory {
    public Passenger createPassenger(int id,
                                     char fromBuilding, int fromFloor,
                                     char toBuilding, int toFloor) {
        return new Passenger(id, fromBuilding, fromFloor, toBuilding, toFloor);
    }
}
