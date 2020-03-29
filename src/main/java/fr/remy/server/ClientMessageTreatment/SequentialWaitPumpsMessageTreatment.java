package fr.remy.server.ClientMessageTreatment;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import fr.remy.gpio.Pump;

public class SequentialWaitPumpsMessageTreatment {

    private String clientSequentialWaitPumpsMessage;

    public SequentialWaitPumpsMessageTreatment(String clientSequentialWaitPumpsMessage) {
        this.clientSequentialWaitPumpsMessage = clientSequentialWaitPumpsMessage;
    }

    public String run () {
        String[] pumpInfos = clientSequentialWaitPumpsMessage.split(" ");
        GpioController gpio = GpioFactory.getInstance();

        System.out.println(clientSequentialWaitPumpsMessage);

        if ((pumpInfos.length - 2) % 2 != 0) {
            return "500 SEQUENTIAL_WAIT_PUMPS - BAD MESSAGE LENGTH";
        }

        for (int i = 2; i < pumpInfos.length; i = i + 2) {
            new Pump(gpio, Integer.parseInt(pumpInfos[i]), Double.parseDouble(pumpInfos[i+1])).run();

            try {
                Thread.sleep(new Double(Double.parseDouble(pumpInfos[1])).intValue());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        gpio.shutdown();

        return "200 SEQUENTIAL_WAIT_PUMPS - OK";
    }

}
