package work;

public class Timer implements Runnable {
    private double duration;
    private final Elevator elevator;
    private Boolean isStopped = false;

    public Timer(double duration, Elevator elevator) {
        this.duration = duration;
        this.elevator = elevator;
    }

    public void setDuration(double duration) {
        this.duration = duration;
        isStopped = false;
    }

    public Boolean isStopped() {
        return isStopped;
    }

    @Override
    public void run() {
        // isStopped = false;
        try {
            Thread.sleep((int) (duration * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isStopped = true;
        synchronized (elevator.getWaitingPassengers()) {
            elevator.getWaitingPassengers().notifyAll();
        }
    }
}
