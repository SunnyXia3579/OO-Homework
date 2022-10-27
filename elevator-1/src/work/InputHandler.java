package work;

import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;
import work.factory.PassengerFactory;

import java.io.IOException;

public class InputHandler implements Runnable {
    private final Simulator simulator = Simulator.getInstance();
    private ElevatorInput elevatorInput = new ElevatorInput(System.in);
    private PassengerFactory passengerFactory = new PassengerFactory();
    private static InputHandler inputHandler;

    public static InputHandler getInstance() {
        if (inputHandler == null) {
            return new InputHandler();
        }
        return inputHandler;
    }

    @Override
    public void run() {
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            // System.out.println(request);
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                simulator.inputEnd();
                break;
            } else {
                // a new valid request
                simulator.passengerComeFromInput(passengerFactory.createPassenger(
                        request.getPersonId(), request.getFromBuilding(),
                        request.getFromFloor(), request.getToBuilding(), request.getToFloor()));
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
