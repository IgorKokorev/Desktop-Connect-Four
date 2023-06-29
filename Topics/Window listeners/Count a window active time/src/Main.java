import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class ActiveTimeCounterWindowAdapter extends WindowAdapter {
    private long activationCounter = 0; // do not change it

    // override a method
    public void windowActivated(WindowEvent e) {
        activationCounter++;
    }
}