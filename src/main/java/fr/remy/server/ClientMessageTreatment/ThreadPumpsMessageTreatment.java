package fr.remy.server.ClientMessageTreatment;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import fr.remy.gpio.Pump;

public class ThreadPumpsMessageTreatment {

    private String clientPumpsWithThreadMessage;

    public ThreadPumpsMessageTreatment(String clientPumpsWithThreadMessage) {
        this.clientPumpsWithThreadMessage = clientPumpsWithThreadMessage;
    }

    public String run () {
        String[] pumpInfos = clientPumpsWithThreadMessage.split(" ");
        GpioController gpio = GpioFactory.getInstance();

        System.out.println(clientPumpsWithThreadMessage);

        if ((pumpInfos.length - 1) % 2 != 0) {
            return "500 PUMPS_WITH_THREAD - BAD MESSAGE LENGTH";
        }

        Thread[] threads = new Thread[(pumpInfos.length - 1) / 2];
        int cptThread = 0;

        for (int i = 1; i < pumpInfos.length; i = i + 2) {
            Pump pump = new Pump(gpio, Integer.parseInt(pumpInfos[i]), Double.parseDouble(pumpInfos[i+1]));
            threads[cptThread] = new Thread(pump);
            threads[cptThread].start();
            cptThread++;
        }

        int i = 0;

        while (i < (pumpInfos.length - 1) / 2) {
            while (threads[i].isAlive()) {}
            i++;
        }

        gpio.shutdown();

        return "200 PUMPS_WITH_THREAD - OK";
    }

}
