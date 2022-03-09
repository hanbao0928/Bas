package bas.converter.jackson;

/**
 * Created by Lucio on 2021/7/21.
 */
public class DeserializeException extends RuntimeException{
    public DeserializeException(Throwable throwable) {
        super(throwable);
    }
}
