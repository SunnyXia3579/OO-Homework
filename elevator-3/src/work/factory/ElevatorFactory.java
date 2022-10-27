package work.factory;

import java.util.ArrayList;

import work.CircularElevator;
import work.VerticalElevator;
import work.point.ElevatorPoint;
import work.Elevator;

public class ElevatorFactory {
    public Elevator createElevator(ArrayList<ElevatorPoint> route, int id, int capacity,
                                   double movingTime, boolean isCircular) {
        Elevator elevator;
        if (isCircular) {
            elevator = new CircularElevator(id, capacity, 0.2, 0.2, movingTime, route,
                    route.get(0).getPoint());
        } else {
            elevator = new VerticalElevator(id, capacity, 0.2, 0.2, movingTime, route,
                    route.get(0).getPoint());
        }
        return elevator;
    }
}
