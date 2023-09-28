import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

enum TrafficLightColor {  
	RED, GREEN, YELLOW 
} 

public class SimPanel extends JPanel implements ActionListener {

	private CarManager carManager;

	private boolean isPaused = false;
	private boolean isStopped = false;

	private final int WIDTH = 500;
	private final int HEIGHT = 800;
	private final int CAR_SIZE = 40;
	private final int car_speed = 1;
	private JLabel timestampLabel;
	private JLabel timeLabel;
	private JLabel car1Label;
	private JLabel car2Label;
	private JLabel car3Label;

	private JLabel car1XLabel;
	private JLabel car2XLabel;
	private JLabel car3XLabel;

	private JLabel car1YLabel;
	private JLabel car2YLabel;
	private JLabel car3YLabel;

	private JLabel car1SpeedLabel;
	private JLabel car2SpeedLabel;
	private JLabel car3SpeedLabel;

	private JLabel car1ThreadLabel;  
	private JLabel car2ThreadLabel;  
	private JLabel car3ThreadLabel;  

	private Rectangle strip;
	private final int STRIP_WIDTH = 5;
	private final int STRIP_HEIGHT = 15;

	// Timers
	private Timer timer;
	private Thread timestampThread;
	private TimestampUpdater timestampUpdater;

	private TrafficLightSimulator trafficLight;
	private Thread tlThread;

	private TrafficLightSimulator trafficLight2;
	private Thread tlThread2;

	private TrafficLightSimulator trafficLight3;
	private Thread tlThread3;

	public SimPanel() {

		// setup traffic lights
		this.trafficLight = new TrafficLightSimulator(TrafficLightColor.GREEN);
		tlThread = new Thread(trafficLight);
		tlThread.start();


		this.trafficLight2 = new TrafficLightSimulator(TrafficLightColor.YELLOW);
		tlThread2 = new Thread(trafficLight2);
		tlThread2.start();



		this.trafficLight3 = new TrafficLightSimulator(TrafficLightColor.RED);
		tlThread3 = new Thread(trafficLight3);
		tlThread3.start();

		// check thread
		System.out.println("SimPanel is running on thread: " + Thread.currentThread().getName());

		// setup info panel
		setLayout(new BorderLayout());
		JPanel infoPanel = new JPanel();
		Dimension newPreferredSize = new Dimension(155, infoPanel.getPreferredSize().height);
		infoPanel.setPreferredSize(newPreferredSize);		
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));  // For vertical alignment

		// setup clock labels
		timeLabel = new JLabel("Current Time");
		timeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 2, 10));
		timestampLabel = new JLabel();
		timestampLabel.setBorder(BorderFactory.createEmptyBorder(2, 20, 10, 10));

		// label with default text
		car1Label = new JLabel("Car 1: White"); 
		car2Label = new JLabel("Car 2: Blue"); 
		car3Label = new JLabel("Car 3: Black"); 

		car1XLabel = new JLabel("X-Pos:"); 
		car2XLabel = new JLabel("X-Pos:"); 
		car3XLabel = new JLabel("X-Pos:"); 

		car1YLabel = new JLabel("Y-Pos"); 
		car2YLabel = new JLabel("Y-Pos"); 
		car3YLabel = new JLabel("Y-Pos"); 

		car1SpeedLabel = new JLabel("km/hour:"); 
		car2SpeedLabel = new JLabel("km/hour:"); 
		car3SpeedLabel = new JLabel("km/hour:"); 

		car1ThreadLabel = new JLabel("Running On:"); 
		car2ThreadLabel = new JLabel("Running On:");  
		car3ThreadLabel = new JLabel("Running On:"); 

		car1Label.setBorder(BorderFactory.createEmptyBorder(20, 10, 2, 10));
		car2Label.setBorder(BorderFactory.createEmptyBorder(20, 10, 2, 10));
		car3Label.setBorder(BorderFactory.createEmptyBorder(20, 10, 2, 10));

		car1YLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
		car2YLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
		car3YLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));

		car1XLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
		car2XLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
		car3XLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));

		car1SpeedLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
		car2SpeedLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
		car3SpeedLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));

		car1ThreadLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
		car2ThreadLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
		car3ThreadLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));

		// add labels to the infoPanel
		infoPanel.add(timeLabel);
		infoPanel.add(timestampLabel);

		infoPanel.add(car1Label);
		infoPanel.add(car1XLabel);
		infoPanel.add(car1YLabel);
		infoPanel.add(car1SpeedLabel);
		infoPanel.add(car1ThreadLabel);


		infoPanel.add(car2Label);
		infoPanel.add(car2XLabel);
		infoPanel.add(car2YLabel);
		infoPanel.add(car2SpeedLabel);
		infoPanel.add(car2ThreadLabel);


		infoPanel.add(car3Label);
		infoPanel.add(car3XLabel);
		infoPanel.add(car3YLabel);
		infoPanel.add(car3SpeedLabel);  
		infoPanel.add(car3ThreadLabel);


		add(infoPanel, BorderLayout.WEST);

		// create clock and thread
		timestampUpdater = new TimestampUpdater(timestampLabel);
		timestampThread = new Thread(timestampUpdater);
		timestampThread.start();
		timestampLabel.setVisible(false);

		// road strip size
		int stripX = WIDTH / 2;
		strip = new Rectangle(stripX, 10, STRIP_WIDTH, STRIP_HEIGHT);

		JPanel southPanel = new JPanel(new FlowLayout());

		// Create buttons
		JButton startButton = new JButton("Start");
		JButton pauseButton = new JButton("Pause");
		JButton stopButton = new JButton("Stop");

		// add ActionListener to each button
		startButton.addActionListener(e -> start());
		pauseButton.addActionListener(e -> pause());
		stopButton.addActionListener(e -> stop());

		// add buttons to south panel
		southPanel.add(startButton);
		southPanel.add(pauseButton);
		southPanel.add(stopButton);

		add(southPanel, BorderLayout.SOUTH);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.darkGray);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// paint cars
		if (carManager != null) {
			for (Car car : carManager.getCars()) {
				if (car != null) {
					g.setColor(car.getColor());
					Rectangle carShape = car.getShape();
					g.fillRect(carShape.x, carShape.y, carShape.width, carShape.height);
				}
			}
		}

		// paint road strips
		g.setColor(Color.YELLOW);
		int numOfStrips = 53;
		int stripPosition = 0;
		for (int i = 0; i < numOfStrips; i++) {

			if (i % 13 != 0) {
				g.fillRect(WIDTH /2 + 5, stripPosition, STRIP_WIDTH, STRIP_HEIGHT);
				g.fillRect(WIDTH /2 - 5, stripPosition, STRIP_WIDTH, STRIP_HEIGHT);

				stripPosition += 15;
			} else {
				stripPosition += 20;

			}

		}    


		// paint traffic lights

		if (isPaused == false) {

			switch(trafficLight.getColor()) {
			case RED:
				g.setColor(Color.RED);
				break;
			case YELLOW:
				g.setColor(Color.YELLOW);
				break;
			case GREEN:
				g.setColor(Color.GREEN);
				break;
			}

			g.fillOval(WIDTH/2 - 8, 600, 20, 20);


			switch(trafficLight2.getColor()) {
			case RED:
				g.setColor(Color.RED);
				break;
			case YELLOW:
				g.setColor(Color.YELLOW);
				break;
			case GREEN:
				g.setColor(Color.GREEN);
				break;
			}
			g.fillOval(WIDTH/2 - 8, 400, 20, 20);


			switch(trafficLight3.getColor()) {
			case RED:
				g.setColor(Color.RED);
				break;
			case YELLOW:
				g.setColor(Color.YELLOW);
				break;
			case GREEN:
				g.setColor(Color.GREEN);
				break;
			}
			g.fillOval(WIDTH/2 - 8, 200, 20, 20);

		}  



	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (isPaused == false ) {
			// update UI on event
			if (carManager != null) {
				updateCarLabels(carManager);
				for (Car car : carManager.getCars()) {
					if (car != null) {
						checkStopLight(car);
						car.move();
						if (car.getShape().y < 0) {
							car.resetPosition(HEIGHT - CAR_SIZE);
						}

					}
				}
			}
			repaint();    
		}
	}

	// check traffic signals and control car
	public void checkStopLight(Car car) {
		if(car.getShape().y == 620) {
			if (trafficLight.getColor() == TrafficLightColor.RED) {
				car.isMoving = false;
			}

			if (trafficLight.getColor() == TrafficLightColor.GREEN) {
				car.isMoving = true;}
		}

		if(car.getShape().y == 420) {
			if (trafficLight2.getColor() == TrafficLightColor.RED) {
				car.isMoving = false;
			}

			if (trafficLight2.getColor() == TrafficLightColor.GREEN) {
				car.isMoving = true;}
		}


		if(car.getShape().y == 220) {
			if (trafficLight2.getColor() == TrafficLightColor.RED) {
				car.isMoving = false;
			}

			if (trafficLight2.getColor() == TrafficLightColor.GREEN) {
				car.isMoving = true;}
		}
	}


	//
	public void updateCarLabels(CarManager carManager) {
		Car car1 = carManager.getCar(0);
		Car car2 = carManager.getCar(1);
		Car car3 = carManager.getCar(2);

		if (car1 != null) {
			car1XLabel.setText("X-Pos: " + car1.getXPosition());
			car1YLabel.setText("Y-Pos: " + car1.getYPosition());
			car1SpeedLabel.setText("km/hour: " + car1.calculateSpeed());
			car1ThreadLabel.setText("Running On: " + car1.getThreadNum());



		} 

		if (car2 != null) {
			car2XLabel.setText("X-Pos: " + car2.getXPosition());
			car2YLabel.setText("Y-Pos: " + car2.getYPosition());
			car2SpeedLabel.setText("km/hour: " + car2.calculateSpeed());
			car2ThreadLabel.setText("Running On: " + car2.getThreadNum());


		}

		if (car3 != null) {
			car3XLabel.setText("X-Pos: " + car3.getXPosition());
			car3YLabel.setText("Y-Pos: " + car3.getYPosition());
			car3SpeedLabel.setText("km/hour: " + car3.calculateSpeed());
			car3ThreadLabel.setText("Running On: " + car3.getThreadNum());

		}
	}



	public void start() {

		// start timer, create car manager, 
		timestampLabel.setVisible(true);

		// check current state
		if (isPaused == false ) {

			if (carManager == null) {
				timer = new Timer(30, this);
				timer.start();

				carManager = new CarManager(CAR_SIZE, car_speed, WIDTH, HEIGHT);
				carManager.startCars();

			}

		} else { 
			isPaused = false;
			timer.start();
		}

	}

	public void pause() {
		// stop timer, pause sim
		if (carManager != null) {
			timer.stop();
			timestampLabel.setVisible(false);
			isPaused = true;
		}
	}    

	public void stop() {
		// stop timers and remove cars
		isPaused = false;

		timer.stop();
		timestampLabel.setVisible(false);

		if (carManager != null) {
			carManager.stopCars();
			carManager = null;
		}

		repaint();
	}



	// Class Provided Example Traffic Light Class as Inner Class
	// A computerized traffic light. 
	static class TrafficLightSimulator implements Runnable { 
		private TrafficLightColor tlc; // holds the current traffic light color 
		private boolean stop = false; // set to true to stop the simulation 
		private boolean changed = false; // true when the light has changed

		TrafficLightSimulator(TrafficLightColor init) {  
			tlc = init; 
		} 

		TrafficLightSimulator() {  
			tlc = TrafficLightColor.RED; 
		} 

		// Start up the light. 
		public void run() { 
			System.out.println("Traffic Light running on thread: " + Thread.currentThread().getName());

			while(!stop) { 
				// check thread

				try { 
					switch(tlc) { 
					case GREEN: 
						Thread.sleep(8000); // green for 8 seconds 
						break; 
					case YELLOW: 
						Thread.sleep(2000);  // yellow for 2 seconds 
						break; 
					case RED: 
						Thread.sleep(8000); // red for 8 seconds 
						break; 
					} 
				} catch(InterruptedException exc) { 
					System.out.println(exc); 
				} 
				changeColor(); 
			}  
		} 

		// Change color. 
		synchronized void changeColor() { 
			switch(tlc) { 
			case RED: 
				tlc = TrafficLightColor.GREEN; 
				break; 
			case YELLOW: 
				tlc = TrafficLightColor.RED; 
				break; 
			case GREEN: 
				tlc = TrafficLightColor.YELLOW; 
			} 

			changed = true;
			notify(); // signal that the light has changed 

		} 

		// Wait until a light change occurs. 
		synchronized void waitForChange() { 
			try { 
				while(!changed) 
					wait(); // wait for light to change 
				changed = false;
			} catch(InterruptedException exc) { 
				System.out.println(exc); 
			} 
		} 

		// Return current color. 
		synchronized TrafficLightColor getColor() { 
			return tlc; 
		} 

		// Stop the traffic light. 
		synchronized void cancel() { 
			stop = true; 
		} 
	}  

	class TrafficLightDemo {  
		public static void main(String args[]) {  
			TrafficLightSimulator tl =
					new TrafficLightSimulator(TrafficLightColor.GREEN); 

			Thread thrd = new Thread(tl);
			thrd.start();

			for(int i=0; i < 9; i++) { 
				System.out.println(tl.getColor()); 
				tl.waitForChange(); 
			} 

			tl.cancel(); 
		}  
	}

}
