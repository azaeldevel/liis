
package core;

import SIIL.Server.Database;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import process.Return;


/**
 *
 * @author Azael Reyes
 */
public class GUIRenglon extends core.Renglon
{
    public GUIRenglon(core.Renglon renlgon)
    {
        super(renlgon);
    }
    
    
    public boolean upCuentaFiscal(Database dbserver) throws SQLException 
    {
        String cuenta = (String)JOptionPane.showInputDialog
        (
            null,
            "Indique la cuenta Fiscal del Producto",
            "Capturar información",
            JOptionPane.PLAIN_MESSAGE,
            null,
            null,
            null
        );
        
        if(cuenta == null) return false;        
        return super.upCuentaFiscal(dbserver, cuenta);
    }
 
}
