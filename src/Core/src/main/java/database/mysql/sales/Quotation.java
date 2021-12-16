
package database.mysql.sales;

import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.service.quotation.ServiceQuotation;
import java.sql.SQLException;
import java.sql.Statement;
import process.Return;
import process.State;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class Quotation extends Operational
{    
    public static final String MYSQL_AVATAR_TABLE = "SalesQuotation";    
      
    
    public boolean importQuotation_FromComercial(Database dbconn)
    {
        String str = "SELECT ";
        
        return false;
    }
    
    @Override
    public Return insert(Database connection,Office office,State state,Person operator,java.util.Date date,Enterprise company,int folio,String serie,String type) throws SQLException
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
        
        Return insertRet = super.insert(connection,office, state, operator, date, company, folio, serie, type);
        if(insertRet.isFail()) return  insertRet;        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id) VALUES(" + super.getID() + ")";
        Statement stmt = connection.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return new Return(false, "Deamasiados registros insertados"); 
        }
        return insertRet;        
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
     * @param orden
     * @return
     * @throws SQLException 
     */
    public Return insert(Database connection,Office office,State state,Person operator,java.util.Date date,Enterprise company,int folio,String serie,String type,ServiceQuotation orden) throws SQLException
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
        if(orden == null)
        {
            return new Return(false, "Bab parameter.");  
        }
        else if(orden.getID() < 1)
        {
            return new Return(false, "Bab parameter.");  
        }
        
        Return insertRet = super.insert(connection,office, state, operator, date, company, folio, serie, type);
        if(insertRet.isFail()) return  insertRet;        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id,orserv) VALUES(" + super.getID() + "," + orden.getID() + ")";
        //System.out.println(sql);
        Statement stmt = connection.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return new Return(false, "Deamasiados registros insertados"); 
        }
        return insertRet;        
    }

    
    public Quotation(int id) 
    {
        super(id);
    }

    private void clean() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
