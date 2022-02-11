package bas.lang.exception;

/**
 * Created by Lucio on 2021/7/27.
 */
public class SilentException extends RuntimeException{
    public SilentException() {
    }

    public SilentException(String s) {
        super(s);
    }

    public SilentException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SilentException(Throwable throwable) {
        super(throwable);
    }

}
