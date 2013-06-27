package nl.frankkie.ouyarandom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 *
 * @author frankkie
 */
public class ShowException {

    public static boolean printExceptionsToLogCat = true;


    public static void showException(Exception e, Context c) {
        if (printExceptionsToLogCat) {
//      Log.e("RandomApp", e.p);
            e.printStackTrace();
        }
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setTitle(e.getMessage());
        String stackTrace = getStackTrace(e);
        //e.getCause().getMessage() + "\n\n" + 
        b.setMessage(stackTrace);
        b.setPositiveButton("ok..", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//        throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        b.create().show();
    }

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * Defines a custom format for the stack trace as String.
     */
    public static String getCustomStackTrace(Throwable aThrowable) {
        //add the class name and any message passed to constructor
        final StringBuilder result = new StringBuilder("BOO-BOO: ");
        result.append(aThrowable.toString());
        final String NEW_LINE = System.getProperty("line.separator");
        result.append(NEW_LINE);

        //add each element of the stack trace
        for (StackTraceElement element : aThrowable.getStackTrace()) {
            result.append(element);
            result.append(NEW_LINE);
        }
        return result.toString();
    }
}
