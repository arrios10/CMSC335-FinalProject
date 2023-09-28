import javax.swing.*;

public class SimulationGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            SimPanel panel = new SimPanel();

            frame.setSize(500, 900);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.setVisible(true);
            
    		// check thread
    		System.out.println("SimulationGUI is running on thread: " + Thread.currentThread().getName());

        });
    }
}
