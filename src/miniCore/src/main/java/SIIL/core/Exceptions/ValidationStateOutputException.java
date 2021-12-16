
package SIIL.core.Exceptions;

/**
 *
 * @author Azael Reyes
 */
public class ValidationStateOutputException extends Exception 
{
    public ValidationStateOutputException(String message,Exception cause)
    {
        super(message, cause);
    }
    
    public ValidationStateOutputException(String message)
    {
        super(message);
    }
}
