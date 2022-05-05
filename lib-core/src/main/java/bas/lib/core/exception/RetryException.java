package bas.lib.core.exception;

/**
 * Created by Lucio on 2021/11/10.
 * <p>
 * 重试异常，捕获之后重试逻辑：用于协程代码块在捕获对应类型异常时重新执行
 */
public class RetryException extends RuntimeException {
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
