
package database.mysql.sales;

import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
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
    private final String MYSQL_AVATAR_TABLE = "SalesOperational";
    private Enterprise enterprise;
    
    
    public Boolean downCompany(Database database) throws SQLException 
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT company FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = database.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            enterprise = new Enterprise(rs.getInt(1));
            return true;
        }
        else
        {
            enterprise = null;
            return false;
        }
    }
    
    /**
     * 
     * @param connection
     * @param office
     * @param state
     * @param operator
     * @param date
     * @param company
     * @param serie
     * @param folio
     * @param type
     * @return
     * @throws SQLException 
     */
    public Return insert(Database connection,Office office,State state,Person operator,Date date,Enterprise company,int folio,String serie,String type) throws SQLException
    {        
        if(getID() > 0)
        {
            return new Return(false, "Bab parameter."); 
        }
        if(connection == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        if(company == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        else if(company.getNumber() == null)
        {
            return new Return(false, "Bab parameter.");  
        }
        
        Return insertRet = super.insert(connection,office, state, operator, date,folio,serie,type);
        if(insertRet.isFail()) return insertRet;        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id,company) VALUES(" + super.getID()+ "," + company.getID()+ ")";
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return new Return(false, "No unico registro insertado."); 
        }
        return insertRet;        
    }

    
    public Operational(int id) 
    {
        super(id);
    }
    
    private void clean() 
    {
        ;
    }

    /**
     * @return the enterprise
     */
    public Enterprise getEnterprise() {
        return enterprise;
    }
    
}
