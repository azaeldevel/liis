
package process;

/**
 *
 * @author Azael Reyes
 * @param <T> El tipo para el parametro
 */
public class Return<T>
{
    /**
     * Determina si es un reustado de erros en base al flag indicado
     * @return 
     */
    public boolean  isFail()
    {
        return !flag;
    }
    
    /**
     * @return the flag
     */
    public boolean isFlag() 
    {
        return flag;
    }

    /**
     * @return the error
     */
    public ErrorCodes getError() {
        return error;
    }
    @Deprecated
    public enum Status
    {
        DONE,
        FAIL
    }
    private Return<T> cause;
    private String message;
    @Deprecated
    private Status status;
    private T param;
    private boolean flag;
    private ErrorCodes error;

    public Return(boolean flag)
    {
        this.flag = flag;
    }
       
        
    public Return(boolean flag,T param)
    {
        this.flag = flag;
        this.param = param;
    }
    
    public Return(boolean flag,T param,String message,Return cause)
    {
        this.flag = flag;
        this.param = param;
        this.message = message;
        this.cause = cause;
    }
    
    public Return(boolean flag,T param,String message)
    {
        this.flag = flag;
        this.param = param;
        this.message = message;
    }       
    
    public Return(boolean flag,String message)
    {
        this.flag = flag;
        this.message = message;
    }           
    
    public Return(boolean flag,String message,ErrorCodes error)
    {
        this.flag = flag;
        this.message = message;
        this.error = error;
    }
    
    /**
     * @return the cause
     */
    public Return getCause()
    {
        return cause;
    }

    /**
     * @return the message
     */
    public String getMessage() 
    {
        if(cause != null)
        {
            String msg = message;
            msg += "\n" + cause.getMessage();
            return msg;
        }
        else
        {
            return message;            
        }
    }

    /**
     * @return the status
     */
    @Deprecated
    public Status getStatus() {
        return status;
    }
    
    /**
     * 
     * @param status
     * @param param 
     */
    @Deprecated
    public Return(Status status, T param)
    {
        this.status = status;
        this.param = param;
        if(status == Status.DONE)
        {
            flag = true;
        }
        else
        {
            flag = false;
        }
    }
    
    /**
     * 
     * @param status
     * @param message
     * @param cause
     * @param param 
     */
    @Deprecated
    public Return(Status status,String message, Return cause, T param)
    {
        this.message = message;
        this.status = status;
        this.cause = cause;
        if(status == Status.DONE)
        {
            flag = true;
        }
        else
        {
            flag = false;
        }
    }
    
    /**
     * 
     * @param status
     * @param message
     * @param cause 
     */
    @Deprecated
    public Return(Status status,String message, Return cause)
    {
        this.message = message;
        this.status = status;
        this.cause = cause;
        if(status == Status.DONE)
        {
            flag = true;
        }
        else
        {
            flag = false;
        }
    }
    
    /**
     * 
     * @param status
     * @param message 
     */
    @Deprecated
    public Return(Status status,String message)
    {
        this.message = message;
        this.status = status;
        if(status == Status.DONE)
        {
            flag = true;
        }
        else
        {
            flag = false;
        }
    }
    
    
    /**
     * 
     * @param status 
     */
    @Deprecated
    public Return(Status status)
    {
        this.status = status;
        if(status == Status.DONE)
        {
            flag = true;
        }
        else if(status == Status.FAIL)
        {
            flag = false;
        }
    }
    
    /**
     * @return the param
     */
    public T getParam() 
    {
        return param;
    }
}
