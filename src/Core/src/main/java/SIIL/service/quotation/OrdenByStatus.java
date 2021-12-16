
package SIIL.service.quotation;

import java.util.ArrayList;

/**
 *
 * @author Azael
 */
public class OrdenByStatus extends ArrayList<ServiceQuotation>
{
    private Estado estado;

    /**
     * @return the estado
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public OrdenByStatus(Estado edo)
    {
        estado = edo;
    }
}
