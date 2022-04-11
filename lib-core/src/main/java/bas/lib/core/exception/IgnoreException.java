package bas.lib.core.exception;

/**
 * Created by Lucio on 2022/2/12.
 * 忽略异常；即捕获异常之后什么都不处理
 */
public class IgnoreException extends RuntimeException {
    public IgnoreException() {
    }

    public IgnoreException(String s) {
        super(s);
    }

    public IgnoreException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IgnoreException(Throwable throwable) {
        super(throwable);
    }

}
