package bas.converter.jackson;

/**
 * Created by Lucio on 2021/7/21.
 */

public class SerializeException extends RuntimeException{
    public SerializeException(Throwable throwable) {
        super(throwable);
    }
}