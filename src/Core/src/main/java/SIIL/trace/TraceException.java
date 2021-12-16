
package SIIL.trace;

/**
 *
 * @author Azael
 */
public class TraceException extends Exception
{
    public TraceException(String message)
    {
        super(message);
    }
    public TraceException(String message, Throwable cause)
    {
        super(message,cause);
    }
}
