package fr.remy.exception;

public class ServerException extends Exception {

    public ServerException() {
        super();
    }

    public ServerException(String userErrorMessage) {
        super(userErrorMessage);
    }

    public ServerException(String userErrorMessage, String technicalErrorMessage) {
        super(userErrorMessage + "\n" + technicalErrorMessage);
    }

}
