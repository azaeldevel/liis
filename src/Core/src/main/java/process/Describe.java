
package process;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class Describe 
{
    public enum Type
    {
        Battery,
        Charger,
        Mina,
        Forklift,
        Company
    }
    
    public static Type getParse(String number)
    {
        String cnNum = number.trim();
        if(cnNum.equals("B-1051"))//excluir
        {
            return null;
        }
        else if(cnNum.matches("^09[0-9]{3,3}|9[0-9]{3,3}|M-[0-9]+"))
        {
            return Type.Forklift;
        }
        else if(cnNum.matches("^[0-9]{4,4}[LGPMNJFR]"))//Garantu Maintace - Mantenimiento total.
        {
            return Type.Forklift;
        }
        else if(cnNum.matches("^C\\-[0-9]+$"))
        {
            return Type.Charger;
        }
        else if(cnNum.matches("^B\\-[0-9]+$"))
        {
            return Type.Battery;
        }
        else if(cnNum.equals("MINA"))
        {
            return Type.Charger;
        }
        else if(cnNum.equals("FBAT"))
        {
            return Type.Company;
        }
        else if(cnNum.matches("^[0-9]+$"))
        {
            int val = Integer.parseInt(cnNum);
            if(val > 998 && val < 8000)
            {
                return Type.Company;
            }
            else if(val > 299 && val < 400)
            {
                return Type.Charger;
            }
            else if(val > 405 && val < 418)
            {
                return Type.Battery;
            }
            else if(val > 699 && val < 997)
            {
                return Type.Forklift;
            }
            else if(val > 9999 && val < 18000)
            {
                return Type.Forklift;
            }
            else
            {
               return null;
            }
        }
        else
        {
            return null;
        }
    }
}
