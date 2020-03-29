package fr.remy.gpio;

import com.pi4j.io.gpio.*;

/**
 * Class implementation to run one pump
 * Implements Runnable
 */
public class Pump implements Runnable {

    private GpioController gpio;
    private String[] gpioTable;
    private int id;
    private double time;

    /**
     * The constructor of the class
     *
     * @param gpio instance of the GPIO
     * @param id the id of the pump to run
     * @param time the time to run pump
     */
    public Pump (GpioController gpio, int id, double time) {
        this.gpio = gpio;
        this.id = id;
        this.time = time;
        gpioTable = new String[] { "GPIO 1", "GPIO 2", "GPIO 3", "GPIO 4", "GPIO 5", "GPIO 6", "GPIO 7", "GPIO 21",
                "GPIO 22", "GPIO 23", "GPIO 24", "GPIO 25", "GPIO 26", "GPIO 27", "GPIO 28", "GPIO 29" };
    }

    /**
     * The method run of Runnable
     */
    public void run () {
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(gpioTable[id]), "MyPump", PinState.HIGH);
        pin.setState(PinState.LOW);

        try {
            Thread.sleep(new Double(time).intValue());
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        pin.setState(PinState.HIGH);
        gpio.unprovisionPin(pin);
    }

}
