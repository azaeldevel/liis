
package SIIL.core.Exceptions;

/**
 *
 * @author Azael Reyes
 */
public class DatabaseException extends RuntimeException
{
    public DatabaseException(String message,Exception cause)
    {
        super(message, cause);
    }
    public DatabaseException(String message)
    {
        super(message);
    }
}
