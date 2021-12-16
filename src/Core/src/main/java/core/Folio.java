
package core;

/**
 *
 * @author Azael Reyes
 */

public class Folio 
{
    private String serie;
    private int number;
        
    public Folio(String folString)
    {
        split(folString);
    }

    private void split(String folString) throws FailResultOperationException 
    {
        String[] ret = folString.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        if(ret.length == 1)
        {
            try
            {
                number = Integer.parseInt(ret[0]);
            }
            catch(NumberFormatException ex)
            {
                serie = ret[0];
            }
        }
        else if(ret.length == 2)
        {
            serie = ret[0];
            number = Integer.parseInt(ret[1]);            
        }
        else if(ret.length > 2) 
        {
            throw new FailResultOperationException(folString + " no es un folio valido");
        }            
    }

    /**
    * @return the serie
    */
    public String getSerie() 
    {
        return serie;
    }

        /**
         * @return the number
         */
   public int getNumber() {
        return number;
    }
}
