
package SIIL.client.sales;

import SIIL.CN.Engine.Clause;
import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Engine.Operator;
import SIIL.CN.Sucursal;
import SIIL.Server.Database;
import SIIL.Server.MySQL;
import SIIL.core.Office;
import SIIL.trace.Trace;
import SIIL.trace.Value;
import java.sql.Statement;
import core.FailResultOperationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import process.Mail;
import process.Return;
import session.Credential;

/**
 *
 * @author Azael
 */
public class Enterprise extends SIIL.Server.Company
{
    public enum RequirePO
    {
        NO,
        ANTERIOR,
        POSTERIOR,
        OPCIONAL
    }
    private RequirePO reqPO;    
    private String rfc;
    private InternetAddress[] emailFacturaTj;
    private InternetAddress[] emailFacturaEns;
    private InternetAddress[] emailFacturaMx;
    
    
    
    
    public Return<Integer> upName(Connection connection, String name) throws SQLException 
    {
        if(getID() < 1)
        {
            return new Return<>(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null");
        }
        this.name = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET name=";
        if(name != null)
        {
            sql += "'" + name + "'";
        }
        else
        {
            sql += "NULL";
        }
        sql += " WHERE id=" + getID();
        //System.out.println(sql);
        java.sql.Statement stmt = connection.createStatement();
        return new Return<>(true, stmt.executeUpdate(sql));
    }
    
    
    public boolean upMailFact(Database db, Office office, String guardianem, Trace traceContext) throws SQLException, AddressException 
    {
        if(getID() < 1)
        {
            return false; 
        }
        if(office == null)
        {
            return false;
        }
        if(guardianem == null)
        {
            return false;
        }
        emailFacturaEns = null;
        emailFacturaMx = null;
        emailFacturaTj = null;
        InternetAddress[] parsed = InternetAddress.parse(guardianem);
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET ";  
        if(office.getCode().equals("bc.tj"))
        {
            sql += "emailsFact_TJ";
        }
        else if(office.getCode().equals("bc.mx"))
        {
            sql += "emailsFact_MXL";
        }
        else if(office.getCode().equals("bc.ens"))
        {
            sql += "emailsFact_ENS";
        }
        if(guardianem != null)
        {
            sql += "='" + guardianem + "'";
        }
        else
        {
            sql += "NULL";
        }
        sql += " WHERE id=" + getID();
        
        if(traceContext != null)
        {
            Value val = new Value();
            int lgh = guardianem.length();
            if(lgh > 200) lgh = 200;
            val.setTraceID(traceContext.getTrace());
            val.setTable(MYSQL_AVATAR_TABLE);
            val.setField("Mail");
            val.setBrief("Permanente:" + guardianem.substring(0, lgh));
            val.setLlave("folio=" + getID());
            val.insert(db);
        }
        
        Statement stmt = (Statement) db.getConnection().createStatement();
        //System.out.println(sql);
        if(stmt.executeUpdate(sql) == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static boolean updateFromCNAll(Database db,List<String> logger) throws SQLException, IOException
    {        
        SIIL.CN.Tables.CLIENTE clienteTJ = new SIIL.CN.Tables.CLIENTE(Sucursal.BC_Tijuana);
        SIIL.CN.Tables.CLIENTE clienteMXL = new SIIL.CN.Tables.CLIENTE(Sucursal.BC_Mexicali);
        SIIL.CN.Tables.CLIENTE clienteENS = new SIIL.CN.Tables.CLIENTE(Sucursal.BC_Ensenada);
        
        String sql = "SELECT id,CONVERT(number,UNSIGNED INTEGER) AS nmb, number FROM " + MYSQL_AVATAR_TABLE;
        ResultSet rs = db.query(sql);
        while(rs.next())
        {
            //System.out.println("Comenzando...");
            Enterprise enterprise = new Enterprise(rs.getInt(1));            
            enterprise.download(db);
            logger.add("Procesando " + enterprise.getNumber());//System.out.println("Procesando " + enterprise.getNumber());
            
            //System.out.println("Tijauan... ");
            List<DBFRecord> fieldTJ = clienteTJ.readWhere(new Clause(0,Operator.TRIMEQUAL,enterprise.getNumber()),1,'R');
            if(fieldTJ.size() == 1)
            {            
                Office officeTJ = new Office(-1);
                officeTJ.selectCode(db, "bc.tj");
                officeTJ.download(db.getConnection());
                SIIL.CN.Records.CLIENTE recordTJ = new SIIL.CN.Records.CLIENTE(fieldTJ.get(0));
                String rfc = recordTJ.getRFC().replaceAll("\\s+","");
                if(rfc.length() > 15)
                {
                    rfc = rfc.substring(0, 15);
                }
                else if(rfc.length() < 1)
                {
                    rfc = null;
                }
                enterprise.upRFC(db.getConnection(), rfc);
                /*String guardian = recordTJ.getGUARDIANEM();
                if(guardian.length() < 1)
                {
                    guardian = null;
                }
                enterprise.upMailFact(db,officeTJ,guardian);*/
            }
            
            /*
            System.out.println("Mexicali...");
            enterprise.download(db);
            List<DBFRecord> fieldMXL = clienteMXL.readWhere(new Clause(0,Operator.TRIMEQUAL,enterprise.getNumber()),1,'R');
            if(fieldMXL.size() == 1)
            {
                Office officeMXL = new Office(-1);
                officeMXL.selectCode(db, "bc.mx");
                officeMXL.download(db.getConnection());
                SIIL.CN.Records.CLIENTE recordMXL = new SIIL.CN.Records.CLIENTE(fieldMXL.get(0));
                enterprise.upRFC(db.getConnection(), recordMXL.getRFC());
                String guardian = recordMXL.getGUARDIANEM();
                if(guardian.length() < 1)
                {
                    guardian = null;
                }
                enterprise.upMailFact(db,officeMXL,guardian);
            }
            */
            
            /*
            System.out.println("Ensenada...");
            enterprise.download(db);
            List<DBFRecord> fieldENS = clienteENS.readWhere(new Clause(0,Operator.TRIMEQUAL,enterprise.getNumber()),1,'R');
            if(fieldENS.size() == 1)
            {
                Office officeENS = new Office(-1);
                officeENS.selectCode(db, "bc.ens");
                officeENS.download(db.getConnection());
                SIIL.CN.Records.CLIENTE recordENS = new SIIL.CN.Records.CLIENTE(fieldENS.get(0));
                enterprise.upRFC(db.getConnection(), recordENS.getRFC());
                String guardian = recordENS.getGUARDIANEM();
                if(guardian.length() < 1)
                {
                    guardian = null;
                }
                enterprise.upMailFact(db,officeENS,guardian);
            }
            */          
        }
        return true;
    }
    
    public Boolean downMailFact(Database db) throws SQLException, AddressException
    {
        super.download(db);
        
        String sql = "SELECT emailsFact_TJ,emailsFact_MXL,emailsFact_ENS FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getID();
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            if(rs.getString(1) != null)
            {
                emailFacturaTj = Mail.convertToArray(rs.getString(1));
            }
            if(rs.getString(2) != null)
            {
                emailFacturaMx = Mail.convertToArray(rs.getString(2));
            }
            if(rs.getString(3) != null)
            {
                emailFacturaEns = Mail.convertToArray(rs.getString(3));
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public Boolean download(Database db) throws SQLException
    {
        super.download(db);
        
        String sql = "SELECT rfc FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getID();
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.rfc = rs.getString(1);            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public InternetAddress[] getEmailFactura(Office office)
    {
        if(office.getCode().equals("bc.tj"))
        {
            return emailFacturaTj;
        }
        else if(office.getCode().equals("bc.mx"))
        {
            return emailFacturaMx;
        }
        else if(office.getCode().equals("bc.ens"))
        {
            return emailFacturaEns;
        }
        else
        {
            return null;
        }
    }
    
    public Return<Integer> upRFC(Connection connection, String rfc) throws SQLException 
    {
        if(getID() < 1)
        {
            return new Return<>(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null");
        }
        if(rfc == null)
        {
            return new Return<>(false,"Quotation is null.");
        }
        this.rfc = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET rfc=";
        if(rfc != null)
        {
            sql += "'" + rfc + "'";
        }
        else
        {
            sql += "NULL";
        }
        sql += " WHERE id=" + getID();
        //System.out.println(sql);
        java.sql.Statement stmt = connection.createStatement();
        return new Return<>(true, stmt.executeUpdate(sql));
    }

    public String getRFC() 
    {
        return rfc;
    }
    
    public boolean updateCN_RFC(Database dbserver,Sucursal sucursal,String number) throws IOException, SQLException
    {
        SIIL.CN.Tables.CLIENTE cliente = new SIIL.CN.Tables.CLIENTE(sucursal);
        List<DBFRecord> field = cliente.readWhere(new Clause(0,Operator.TRIMEQUAL,number),1,'R');
        if(field.size() == 1)
        {
            upRFC(dbserver.getConnection(), field.get(0).getString(5));
            return true;
        }
        return false;
    }            
            
    /**
     * @return the reqPO
     */
    public RequirePO getRequirePO() 
    {
        return reqPO;
    }
    
    public boolean upRequirePO(Database dbserver, RequirePO requirepo) throws SQLException
    {
        if(getID() < 1)
        {
            return false; 
        }
        if(dbserver == null)
        {
            return false;
        }
        if(requirepo == null)
        {
            return false;
        }
        this.reqPO = null;
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET RequirePO="; 
        if(RequirePO.ANTERIOR == requirepo)
        {
            sql += "'A'";
        }
        else if(RequirePO.POSTERIOR == requirepo)
        {
            sql += "'P'";
        }
        else if(RequirePO.OPCIONAL == requirepo)
        {
            sql += "'O'";
        }
        else if(RequirePO.NO == requirepo)
        {
            sql += "'N'";
        }
        sql += " WHERE id=" + getID();
        Statement stmt = (Statement) dbserver.getConnection().createStatement();
        //System.out.println(sql);
        if(stmt.executeUpdate(sql) == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean downRequirePO(Database dbserver) throws SQLException 
    {
        if(dbserver == null)
        {
            return false;
        }
        String sql = "SELECT RequirePO FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getID();
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            if(rs.getString(1).contains("P"))
            {
                reqPO = RequirePO.POSTERIOR;
            }
            else if(rs.getString(1).contains("A"))
            {
                reqPO = RequirePO.ANTERIOR;
            }
            else if(rs.getString(1).contains("O"))
            {
                reqPO = RequirePO.OPCIONAL;
            }
            else if(rs.getString(1).contains("N"))
            {
                reqPO = RequirePO.NO;
            }
            else
            {
                return false;
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static Enterprise findByRFC(Database db,String rfc) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE rfc ='" + rfc + "'";
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            Enterprise cmp = new Enterprise(rs.getInt(1));
            if(rs.next())
            {
                throw new FailResultOperationException("Hay mas de una coincidencia para el RFC '" + rfc + "'");
            }
            return cmp;
        }
        else 
        {
            return null;
        }
    }
    
    public static Enterprise find(Database db,String number) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE number ='" + number + "'";
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            return new Enterprise(rs.getInt(1));
        }
        else 
        {
            return null;
        }
    }
    
    public static ArrayList<Enterprise> listing(String text, int limit, Database conn,String BD) 
    {
        String sql = "SELECT number,name,id FROM Companies WHERE  (number LIKE '%" + text + "%' or name LIKE '%" + text + "%') and BD = '" + BD + "' ORDER BY name ASC LIMIT " + limit;
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.getConnection().createStatement();  
            //System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Enterprise> arr = new ArrayList<>();
            Enterprise empresa;
            while(rs.next())
            {
                empresa = new Enterprise(rs.getInt("id"));
                empresa.setName(rs.getString("name"));
                empresa.setNumber(rs.getString("number"));
                arr.add(empresa);
            }
            rs.close();
            stmt.close();
            return arr;
        }
        catch(SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        finally
        {
            //finally block used to close resources
            try
            {
                if(stmt != null) stmt.close();
            }
            catch(SQLException se2)
            {
                ;
            }// nothing we can do
        }//end try 
        return null;
    }
    
    public static ArrayList<Enterprise> patron(String text, int limit, Database conn,String BD) 
    {
        String[] words = text.split(" ");
        String sql = "SELECT number,name,id FROM Companies WHERE ";
        for(int i = 0; i < words.length ; i++)
        {
            sql += "name LIKE '%" + words[i] + "%'";
            if(i < words.length - 1) sql += " and ";
        }
        //System.out.println("sql = " + sql);
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.getConnection().createStatement();  
            //System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Enterprise> arr = new ArrayList<>();
            Enterprise empresa;
            while(rs.next())
            {
                empresa = new Enterprise(rs.getInt("id"));
                empresa.setName(rs.getString("name"));
                empresa.setNumber(rs.getString("number"));
                arr.add(empresa);
            }
            rs.close();
            stmt.close();
            return arr;
        }
        catch(SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        finally
        {
            //finally block used to close resources
            try
            {
                if(stmt != null) stmt.close();
            }
            catch(SQLException se2)
            {
                ;
            }// nothing we can do
        }//end try 
        return null;
    }
    
    public Enterprise(int id)
    {
        super(id);
    }
    
    public Enterprise()
    {
        
    }
        
    public boolean valid(Database bd) 
    {
        return super.valid(bd);
    }

    public Throwable fill(Database db, Credential cred, String compNumber) 
    {
        return super.fill(db,cred,compNumber);
    }
}
