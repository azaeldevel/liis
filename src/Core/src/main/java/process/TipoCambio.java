
package process;

/**
 *
 * @author Azael Reyes
 */
public class TipoCambio 
{
    private Moneda local;
    private Moneda foreign;
    private double foreignValor;//el valor de la moneda foranea en terminos de la local

    public TipoCambio(String local,String foreign,double foreignValor)
    {
        if(local != null)
        {
            if(local.equals("MXN"))
            {
                this.local = Moneda.MXN;
            }
            else if(local.equals("USD"))
            {
                this.local = Moneda.USD;
            }
        }
        
        if(foreign != null)
        {
            if(foreign.equals("MXN"))
            {
                this.foreign = Moneda.MXN;
            }
            else if(foreign.equals("USD"))
            {
                this.foreign = Moneda.USD;
            }
        }
        
        this.foreignValor = foreignValor;
    }
    
    public TipoCambio(Moneda local,Moneda foreign,double foreignValor)
    {
        this.local = local;
        this.foreign = foreign;
        this.foreignValor = foreignValor;
    }
    
    /**
     * @return the local
     */
    public Moneda getLocal() 
    {
        return local;
    }

    /**
     * @return the foreign
     */
    public Moneda getForeign() 
    {
        return foreign;
    }

    /**
     * @return the foreignValor
     */
    public double getForeignValor() 
    {
        return foreignValor;
    }
}
