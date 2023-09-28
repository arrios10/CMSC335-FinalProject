import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TimestampUpdater implements Runnable {
    private final JLabel timestampLabel;
    private boolean running = true;

    public TimestampUpdater(JLabel timestampLabel) {
        this.timestampLabel = timestampLabel;
    }

    @Override
    public void run() {
    	
        System.out.println("Clock is running on thread: " + Thread.currentThread().getName());

        while (running) {
            SwingUtilities.invokeLater(() -> {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String formatDateTime = now.format(formatter);
                timestampLabel.setText(formatDateTime);

            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
