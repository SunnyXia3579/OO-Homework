package work;

import work.point.Point;

import java.util.ArrayList;

public class Passenger {
    private final int id;
    private Point fromPoint;
    private Point toPoint;
    private final ArrayList<Point> path = new ArrayList<>();

    public Passenger(int id, char fromBuilding, int fromFloor, char toBuilding, int toFloor) {
        this.id =  id;
        fromPoint = new Point(fromBuilding, fromFloor);
        toPoint = new Point(toBuilding, toFloor);
    }

    public int getId() {
        return id;
    }

    public Point getFromPoint() {
        return fromPoint;
    }

    public Point getToPoint() {
        return toPoint;
    }

    public ArrayList<Point> getPath() {
        return path;
    }

    public void arrangePath() {
    }

    public void updatePath() {
        if (isArrived()) {
            return;
        }
        toPoint = path.get(0);
        path.remove(0);
    }

    public Boolean isArrived() {
        return path.isEmpty();
    }

    @Override
    public String toString() {
        return id + " " + fromPoint + " " + toPoint;
    }
}
