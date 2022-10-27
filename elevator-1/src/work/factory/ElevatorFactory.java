package work.factory;

import java.util.ArrayList;
import java.util.HashMap;

import work.point.Point;
import work.point.ElevatorPoint;
import work.Elevator;

public class ElevatorFactory {
    public Elevator createElevator(HashMap<Character, HashMap<Integer, Point>> points,
                                   int id, char building) {
        Elevator elevator;
        ArrayList<ElevatorPoint> route = new ArrayList<>();
        for (int i = 1;i <= 10;i++) {
            route.add(new ElevatorPoint(points.get(building).get(i), true));
        }
        elevator = new Elevator(id, 6, 0.2, 0.2, 0.4, route, building, 1);
        return elevator;
    }
}
