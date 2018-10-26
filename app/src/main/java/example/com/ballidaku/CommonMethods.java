package example.com.ballidaku;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class CommonMethods
{
    private static CommonMethods instance = new CommonMethods();

    static CommonMethods getInstance()
    {
        return instance;
    }

    String fixedLengthString(int value, int length)
    {
        return String.format("%1$" + length + "s", String.valueOf(value));
    }

    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy",Locale.US);
    SimpleDateFormat tf = new SimpleDateFormat("hh:mm a",Locale.US);

    String getDate()
    {
        Date c = Calendar.getInstance().getTime();
        return df.format(c);
    }

    String getTime()
    {
        Date c = Calendar.getInstance().getTime();
        return tf.format(c);
    }

}
