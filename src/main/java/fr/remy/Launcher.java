package fr.remy;

import fr.remy.exception.ServerException;
import fr.remy.server.Server;

public class Launcher {

    public static void main (String[] serverParameters) throws ServerException {
        if (serverParameters.length != 1) {
            throw new ServerException("Aucun paramètre n'a été spécifié");
        }

        String portStr = serverParameters[0];

        if (!portStr.matches("-?\\d+(\\.\\d+)?")) {
            throw new ServerException("Le port indiqué est incorrect : " + portStr);
        }

        new Server(Integer.valueOf(portStr)).open();
    }

}
