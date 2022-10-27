package work.point;

public class ElevatorPoint {
    private final Point point;
    private final Boolean isReachable;

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
