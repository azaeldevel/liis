
package database.mysql.purchases;

import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.core.Office;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import process.Return;
import process.State;

/**
 *
 * @author Azael Reyes
 */
public class Operational extends process.Operational
{
    private final String MYSQL_AVATAR_TABLE = "PurchasesOperational";    
    private database.mysql.purchases.Provider provider;
    
    
   
    
    /**
     * 
     * @param connection
     * @return 
     * @throws java.sql.SQLException 
     */
    @Override
    public Return download(Database db) throws SQLException
    {
        Return ret = super.download(db);
        if(ret.isFail()) return ret;
        if(ret.getStatus() == Return.Status.FAIL) return ret;
        
        String sql = "SELECT provider FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getID();
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            provider = new database.mysql.purchases.Provider(rs.getInt(1));
            provider.downNameShort(db.getConnection());
            provider.downRazonSocial(db.getConnection());
            provider.downRFC(db.getConnection());
            return new Return(Return.Status.DONE);
        }
        else
        {
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + getID());
        }
    }
    
    public Operational(int id) 
    {
        super(id);
    }    
    
    /**
     * 
     * @param connection
     * @param office
     * @param state
     * @param operator
     * @param date
     * @param serie
     * @param folio
     * @param provider
     * @param type
     * @return
     * @throws SQLException 
     */
    public Return insert(Database connection,Office office,State state,Person operator,Date date,database.mysql.purchases.Provider provider,int folio,String serie,String type) throws SQLException
    {
        if(getID() > 0)
        {
            return new Return(false, "Bab parameter.");
        }
        if(connection == null)
        {
            return new Return(false, "Bab parameter.");
        }
        if(provider == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        else if(provider.getID() < 1)
        {
            return new Return(false, "Bab parameter."); 
        }
        
        Return insertRet = super.insert(connection,office, state, operator, date,folio,serie,type);
        if(insertRet.isFail()) return insertRet;        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id,provider) VALUES(" + super.getID()+ "," + provider.getID()+ ")";
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            clean();
            return new Return(false, "Deamasiados registros insertados"); 
        }
        return insertRet;        
    }

    private void clean() 
    {
        provider = null;
    }
    
    /**
     * @return the provider
     */
    public database.mysql.purchases.Provider getProvider() {
        return provider;
    }
}
