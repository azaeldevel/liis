
package SIIL.Servicios.Orden;

import SIIL.service.quotation.ServiceQuotation;
import java.text.SimpleDateFormat;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

/**
 *
 * @author Azael
 */
public class OrdenNode extends AbstractMutableTreeTableNode
{
    private static final int COLUMN_STATUS  = 0;
    private static final int COLUMN_FOLIO   = 1;
    private static final int COLUMN_OWNER   = 2;
    private static final int COLUMN_CLIENT  = 3;
    private static final int COLUMN_ETA     = 4;
    private static final int COLUMN_SA      = 5;
    private static final int COLUMN_PO      = 6;
    private static final int COLUMN_COUNT   = 7;
    
    
    public OrdenNode(ServiceQuotation ord)
    {
        super(ord);
    }

    @Override
    public Object getValueAt(int column) 
    {
        switch(column)
        {
            case COLUMN_STATUS:
                if(getData().getState().getName() != null)
                {
                    return getData().getState().getName();
                }
                else
                {
                    return "";
                }
            case COLUMN_FOLIO:
                if(getData().getFolio() != null)
                {
                    return getData().getFolio().toString();
                }
                else
                {
                    return "";
                }                  
            case COLUMN_OWNER:
                    if(getData().getOwner() != null && getData().getOwner().toString() != null)
                    {
                        return getData().getOwner().toString();
                    }
                    else
                    {
                        return "";
                    }
            case COLUMN_CLIENT:
                    if(getData().getEntreprise() != null && getData().getEntreprise().toString() != null)
                    {
                        return getData().getEntreprise().toString();
                    }
                    else
                    {
                        return "";
                    }
            case COLUMN_ETA:
                    if(getData().getFhETA() != null)
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
                        return sdf.format(getData().getFhETA());
                    }
                    else
                    {
                        return "";
                    }
            case COLUMN_SA:
                    if(getData().getSA() != null)
                    {
                        return getData().getSA();
                    }
                    else
                    {
                        return "";
                    }            
            case COLUMN_PO:
                if(getData().getPOFile() == null)
                {
                    return "";
                }
                else
                {
                    return getData().getPOFile().getFolio();
                }
                default:
                    return null;
        }
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }   
    
    public ServiceQuotation getData()
    {
        return (ServiceQuotation)getUserObject();
    }
    
}
