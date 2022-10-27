package work.status;

import work.Elevator;

public interface Status {
    void act(Elevator elevator);

    String toString();
}
