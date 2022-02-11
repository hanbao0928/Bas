package bas.lang.exception;

/**
 * Created by Lucio on 2021/11/10.
 */
public class RetryException extends Throwable{
    public RetryException() {
    }

    public RetryException(String s) {
        super(s);
    }

    public RetryException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RetryException(Throwable throwable) {
        super(throwable);
    }

    public RetryException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
