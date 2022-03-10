package bas.lib.core.converter.moshi;

import bas.lib.core.date.DateUtils;
import bas.lib.core.lang.StringUtils;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.text.ParseException;
import java.util.Date;

/**
 * 对Date类型进行解析
 */
public class UtcDateAdapter {

    @ToJson
    String toJson(Date date) {
        if (date == null)
            return null;
        return DateUtils.toUTCDateTimeFormat(date);
    }

    @FromJson
    Date fromJson(String json) throws ParseException {
        if (StringUtils.isNullOrEmpty(json))
            return null;
        return DateUtils.getUTCDateTimeFormat().parse(json);
    }
}