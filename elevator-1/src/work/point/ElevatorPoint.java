package work.point;

public class ElevatorPoint {
    private final Point point;
    private Boolean isReachable = true;

    public ElevatorPoint(char building, int floor) {
        this.point = new Point(building, floor);
    }

    public ElevatorPoint(Point point) {
        this.point = point;
    }

    public ElevatorPoint(Point point, Boolean isReachable) {
        this.point = point;
        this.isReachable = isReachable;
    }

    public Point getPoint() {
        return point;
    }

    public char getBuilding() {
        return point.getBuilding();
    }

    public int getFloor() {
        return point.getFloor();
    }

    public Boolean isReachable() {
        return isReachable;
    }

    @Override
    public String toString() {
        return point + " " + isReachable;
    }
}
