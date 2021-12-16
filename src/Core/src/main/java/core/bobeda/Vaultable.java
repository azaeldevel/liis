
package core.bobeda;

import SIIL.Server.Database;
import java.sql.SQLException;

/**
 *
 * @author Azael Reyes
 */
public interface Vaultable 
{
    public String getFolioInTable();
    public Business getBusinesDocument();
    public boolean downloadDataVault(Database dbserver) throws SQLException;
}
