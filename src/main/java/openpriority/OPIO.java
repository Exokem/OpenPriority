package openpriority;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class OPIO
{
    private static final Logger OUTPUT = Logger.getLogger("OpenPriority");

    public static void log(Level level, String message)
    {
        OUTPUT.log(level, message);
    }

    public static void logf(Level level, String format, Object... values)
    {
        OUTPUT.log(level, String.format(format, values));
    }

    public static void warnf(String format, Object... values)
    {
        logf(Level.WARNING, format, values);
    }

    public static void inff(String format, Object... values)
    {
        logf(Level.INFO, format, values);
    }

    public static void sevf(String format, Object... values)
    {
        logf(Level.SEVERE, format, values);
    }
}
