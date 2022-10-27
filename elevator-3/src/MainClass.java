import com.oocourse.TimableOutput;
import work.InputHandler;
import work.Simulator;

public class MainClass {
    public static void main(String[] args) {
        // TimableOutput.initStartTimestamp();

        Simulator simulator = Simulator.getInstance();
        InputHandler inputHandler = InputHandler.getInstance();
        TimableOutput.initStartTimestamp();

        new Thread(inputHandler).start();
        new Thread(simulator).start();
    }
}
/*
ADD-floor-10-7
1-FROM-C-7-TO-A-7

ADD-floor-7-7-4-0.6-31
2-FROM-B-7-TO-D-8
 */