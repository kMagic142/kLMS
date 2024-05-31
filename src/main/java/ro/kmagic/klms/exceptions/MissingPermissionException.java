package ro.kmagic.klms.exceptions;

public class MissingPermissionException extends Exception {
    public MissingPermissionException() {
        super("");
    }

    public MissingPermissionException(String string) {
        super(string);
    }
}
