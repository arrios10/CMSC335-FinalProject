import java.awt.Color;

class CarManager {
    private Car[] cars = new Car[3];
    private Thread[] carThreads = new Thread[3];

    public CarManager(int carSize, int carSpeed, int width, int height) {
        cars[0] = new Car(width / 2 + 25, height - carSize, carSize, carSpeed, Color.WHITE);
        cars[1] = new Car(width / 2 + 55, height + carSize, carSize, carSpeed, Color.BLUE);
        cars[2] = new Car(width / 2 + 85, height + carSize * 2, carSize, carSpeed, Color.BLACK);
        for(int i = 0; i < cars.length; i++) {
            carThreads[i] = new Thread(cars[i]);
        }
    }
    
    public Car[] getCars() {
        return cars;
    }

    public void startCars() {
        for(Thread carThread : carThreads) {
            carThread.start();
        }
    }

    public void stopCars() {
        for(Thread carThread : carThreads) {
            carThread.interrupt();
        }
    }
    
    public Car getCar(int index) {
        if (index >= 0 && index < cars.length) {
            return cars[index];
        }
        return null;
    }

}
