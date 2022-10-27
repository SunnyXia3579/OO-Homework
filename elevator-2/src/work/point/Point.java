package work.point;

public class Point {
    private final char building;
    private final int floor;

    public Point(char building, int floor) {
        this.building = building;
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }

    public char getBuilding() {
        return building;
    }

    public boolean equals(Point another) {
        return building == another.getBuilding() && floor == another.getFloor();
    }

    public String toString() {
        return building + " " + floor;
    }
}
