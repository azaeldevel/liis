
package SIIL.Server;

/**
 *
 * @author areyes
 */
public class TipoCambio 
{

    @Override
    public String toString()
    {
        return moneda.toString();
    }
    
    public TipoCambio() 
    {
        moneda = Moneda.Desconocida;
        valor = 0.0f;
        id = 0;
    }
    

    /**
     * @return the moneda
     */
    public Moneda getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    /**
     * @return the valor
     */
    public float getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(float valor) {
        this.valor = valor;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    public enum Moneda
    {
        Desconocida,
        Peso,
        Dolar,
        Euro
    }
    private Moneda moneda;
    private float valor;
    private int id;
}
