package fiuba.mensajero;

/**
 * Excepcion arrojada cuando no se encuentra la URL del servidor
 */
public class ServerAddressNotFoundException extends Exception {

    public ServerAddressNotFoundException(String message) {
        super(message);
    }

    public ServerAddressNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
