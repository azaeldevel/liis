
package SIIL.Servicios.Orden;

import SIIL.service.quotation.Sender;
import SIIL.Server.Database;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.core.Exceptions.DatabaseException;
import SIIL.core.Office;
import SIIL.trace.Value;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.mail.internet.AddressException;

/**
 *
 * @author Azael
 */
public class OrdenCrear extends ServiceQuotation implements Runnable
{
    static boolean isExist(int folio,Database dbserver) throws DatabaseException 
    {
        String sql = "SELECT folio FROM Orcom WHERE folio = " + folio;
        Statement stmt = null;
        try 
        {
            stmt = dbserver.getConnection().createStatement();
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Fallo la creacion de Statement", ex);
        }   
        ResultSet rs = null;
        try 
        {
            rs = stmt.executeQuery(sql);
            //System.out.println(sql);
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Fallo la ejecucion de la consulta : " + sql , ex);
        }
        try 
        {
            if(rs.next())
            {//si exite
                return true;
            }
            else
            {//no exite
                return false;
            }
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Fallo la electura de ResultSet : " + sql , ex);
        }
    }

    static boolean isExist(Integer valueOf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public OrdenCrear(session.Credential cred) 
    {
        super(0,cred);
    }
    
    public Exception valid(Database db)
    {        
        if(folio < 1)
        {
            return new Exception("Folio incorrecto.");
        }
        if(owner.getpID() < 1)
        {
            return new Exception("ID de usuario para Encargado incorrecto '" + owner.getpID() + "'");
        }
        if(technical.getpID() < 1)
        {
            return new Exception("ID de usuario para Tecnico incorrecto '" + technical.getpID() + "'");
        }
        if(!company.valid(db))
        {
            return new Exception("Datos de cliente incorrectos.");
        }
        
        return null;
    }
    
    public int create(Database db) throws SQLException 
    {
        java.sql.Timestamp fhFolio = db.getTimestamp();
        String sql = "INSERT INTO Orcom(compBD,compNumber,folio,BD,estado,fhFolio,docType,ownerName,suc,creator,technicalPerson,ownerPerson,department,serie,ownerPerson2) VALUES('" + getCompany().getBD();
        sql += "','" + getCompany().getNumber() + "'," + folio + ",'" + credential.getBD() + "','docedit','" + new java.sql.Date(fhFolio.getTime()) + "','C'" ;
        sql += ",'" + owner.toString() + "','" + owner.getOffice().getCode() + "','" + creator.getAlias() + "'," + technical.getpID() + "," + owner.getpID() + ",";
        if(owner.getDepartment() != null)
        {
            sql += "'" + owner.getDepartment() + "',";
        }
        else
        {
            sql += "NULL,";
        }
        serie = owner.getOffice().getSerieOffice(Office.Platform.CN60);
        sql += "'" + serie + "',";        
        if(owner2 != null)
        {
            sql += owner2.getpID();
        }
        else
        {
            sql += "NULL";
        }
        sql += ")";
        
        Value val;
        int flV = 0;
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter("docedit");
        val.setField("estado");
        val.setBrief("Bandera de Estado");        
        val.setLlave("folio="+folio);
        flV += val.insert(db);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(String.valueOf(folio));
        val.setField("folio");
        val.setBrief("Folio del Documento");     
        val.setLlave("folio="+folio);
        flV += val.insert(db);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(String.valueOf(fhFolio.getTime()));
        val.setField("fhFolio");
        val.setBrief("Fecha del Documento");     
        val.setLlave("folio="+folio);
        flV += val.insert(db);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(owner.toString());
        val.setField("ownerName");
        val.setBrief("Nombre del Responsable");     
        val.setLlave("folio="+folio);
        flV += val.insert(db);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");        
        val.setAfter(creator.getOffice().getName());
        val.setField("suc");
        val.setBrief("Oficina");     
        val.setLlave("folio="+folio);
        flV += val.insert(db);
        val = new Value();
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(creator.getAlias());
        val.setField("creator");
        val.setBrief("Creador");     
        val.setLlave("folio="+folio);
        flV += val.insert(db);
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(technical.toString());
        val.setField("technicalPerson");
        val.setBrief("Tecnico");     
        val.setLlave("folio="+folio);
        flV += val.insert(db);
        val.setTraceID(trace.getTrace());
        val.setTable("Orcom");
        val.setAfter(creator.getDepartment());
        val.setField("department");
        val.setBrief("Department");     
        val.setLlave("folio="+folio);
        flV += val.insert(db);   
        System.out.println(sql);
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        int generated = -1;        
        if (rs.next())
        {
            ID = rs.getInt(1);
            return affected;
        }
        else
        {
            ID = -1;
            return affected;
        }
    }

    public boolean sendMail(Database db) throws SQLException, AddressException
    {
        process.Mail mail = new process.Mail(-1);
        return mail.insert(getServerDB(), Sender.generateTO(database, this), Sender.generateCC(database,credential,this), Sender.generateBCC(), Sender.addDocMensaje(database, credential, this), "orserv", Sender.addDocSubject(database, credential, this));
    }

    @Override
    public void run() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
