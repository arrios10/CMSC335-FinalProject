import java.awt.*;
import java.text.DecimalFormat;


public class Car implements Runnable {
    private int x, y;
    private final int size;
    private final int speed;
    private final Color color;
    private Rectangle shape;
    boolean isMoving = true;
    private String threadNum;

    public Car(int x, int y, int size, int speed, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.color = color;
       this.shape = new Rectangle(x, y, size/2, size);
    }

    public void move() {
        if (isMoving) { // Check if car is allowed to move
            this.y -= speed;
            // update the car's location
            this.shape.setLocation(this.x, this.y);
        }
        // update the car's location
        this.shape.setLocation(this.x, this.y);
    }

    public void resetPosition(int y) {
    	this.y = y;
    }

    public Rectangle getShape() {
        return this.shape;
    }

    public Color getColor() {
        return this.color;
    }
    
    public int getXPosition() {
        return this.x; // example
    }

    public int getYPosition() {
        return this.y; // example
    }
        
    public String calculateSpeed() {
    	if (isMoving == false) {
    		return "00";
    	} 
    	
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(this.speed * 50);
    }
    
    @Override
    public void run() {
    	setThreadNum(Thread.currentThread().getName());
        System.out.println("car is running on thread: " + Thread.currentThread().getName());

        while (true) {
            try {
            	// sleep the thread for update cycle
                Thread.sleep(100);              

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                break;
            }
        }
    }

	public String getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(String threadNum) {
		this.threadNum = threadNum;
	}
}