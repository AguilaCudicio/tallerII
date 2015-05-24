package fiuba.mensajero;


public class ServerAddressNotFoundException extends Exception {

    public ServerAddressNotFoundException(String message) {
        super(message);
    }

    public ServerAddressNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
