package at.foobartech.wheremymoneyat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Thomas Feichtinger
 */

public class WMMAUtils {

    private final static SimpleDateFormat DATE_FORMAT_ISO = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final static SimpleDateFormat DATE_FORMAT_PRETTY = new SimpleDateFormat("dd MMMM", Locale.getDefault());

    public static String formatDate(Date date) {
        return DATE_FORMAT_ISO.format(date);
    }

    public static String formatDatePretty(Date date) {
        return DATE_FORMAT_PRETTY.format(date);
    }

    public static String formatAmount(int amount) {
        return String.format(Locale.getDefault(), "%.2f", amount / 100d);
    }

    public static Date parseDate(String s) throws ParseException {
        return DATE_FORMAT_ISO.parse(s);
    }
}
