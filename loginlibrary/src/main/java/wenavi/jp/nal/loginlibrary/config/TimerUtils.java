package wenavi.jp.nal.loginlibrary.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Copyright © Nals
 * Created by TrangLT on 1/7/19.
 */

public final class TimerUtils {
    public static String getDatetime(long timestamp, FormatType type) {
        Date date = new Date();
        date.setTime(timestamp);
        SimpleDateFormat format = new SimpleDateFormat(type.getValue(), Locale.JAPANESE);
        String dateTime = format.format(date);

        if (type == FormatType.TYPE_1 && dateTime.split(":").length == 3) {
            /**
             * For Samsung S3 Galaxy or Xperia A SO-04E(4.2.2) the format of timeZone is incorrect.
             * The value of shotDate for these devices like 2015-10-08T11:21:38+0700, but we want
             * like 2015-10-08T11:21:38+07:00. So the below code will convert it.
             */
            dateTime = dateTime.substring(0, dateTime.length() - 2) + ":" +
                    dateTime.substring(dateTime.length() - 2);
        }

        return dateTime;
    }

    public enum FormatType {
        TYPE_1("yyyy-MM-dd"),
        TYPE_2("yyyy年M月d日 kk:mm"),
        TYPE_3("MM/dd"),
        TYPE_4("EEE"),
        TYPE_5("HH:mm:ss"),
        TYPE_6("HH:mm"),
        TYPE_7("M/d"),
        TYPE_8("M/d (EEE)"),
        TYPE_9("kk:mm:ss"),
        TYPE_10("yyyy-MM-dd kk:mm:ss"),
        TYPE_11("MM/dd/yyyy"),
        TYPE_12("M月d日 kk:mm"),
        TYPE_13("yyyy年"),
        TYPE_14("yyyy/MM/dd"),
        TYPE_15("yyyy年M月d日 kk:mm"),;

        final String value;

        FormatType(String val) {
            this.value = val;
        }

        public String getValue() {
            return this.value;
        }
    }
}
