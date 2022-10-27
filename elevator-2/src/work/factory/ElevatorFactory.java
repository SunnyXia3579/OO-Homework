package work.factory;

import java.util.ArrayList;

import work.point.Point;
import work.point.ElevatorPoint;
import work.Elevator;

public class ElevatorFactory {
    public Elevator createElevator(ArrayList<Point> points, int id,
                                   double movingTime, boolean isCircular) {
        Elevator elevator;
        ArrayList<ElevatorPoint> route = new ArrayList<>();
        for (Point point : points) {
            route.add(new ElevatorPoint(point, true));
        }
        elevator = new Elevator(id, 6, 0.2, 0.2, movingTime, route, points.get(0), isCircular);
        return elevator;
    }
}
