
package SIIL.service.quotation;

import SIIL.Server.Database;
import SIIL.core.Exceptions.DatabaseException;
import SIIL.trace.Value;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import process.Return;

/**
 *
 * @author Azael
 */
public class OrdenUpdate extends ServiceQuotation
{    
    private int newFolio;
    
    public OrdenUpdate(ServiceQuotation orden)
    {
        super(orden);
        fail = null;
    }
    
    public Return upFolio(Connection connection,int newFolio) throws SQLException
    {
        this.newFolio = newFolio;
        String sql = "UPDATE Orcom SET folio = " + newFolio + " WHERE ID = " + getID();
        Statement stmt = connection.createStatement();
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }
    
    public OrdenUpdate()
    {
        fail = null;
    }
    
    private boolean ownerSQL(Database db) 
    {
        String sql = "UPDATE Orcom SET ownerName = ?, ownerPerson = ?, department = ? WHERE ID = ?";
        try 
        {
            PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
            pstmt.setString(1, owner.toString());
            pstmt.setInt(2, owner.getpID());
            if(owner.getDepartment() != null)
            {
                pstmt.setString(3,owner.getDepartment());
            }
            else
            {
                pstmt.setNull(3, 0);
            }
            pstmt.setInt(4, ID);
                        
            int flV = 0;
            Value val;        
            val = new Value();
            val.setTraceID(trace.getTrace());
            val.setTable("Orcom");
            val.setAfter(owner.toString());
            val.setField("ownerName");
            val.setBrief("Nombre del Responsable");     
            val.setLlave("folio="+folio);
            flV += val.insert(db);
            if(owner.getDepartment() != null)
            {
                val = new Value();
                val.setTraceID(trace.getTrace());
                val.setTable("Orcom");
                val.setAfter(owner.getDepartment());
                val.setField("department");
                val.setBrief("Departamento del Responsable");     
                val.setLlave("folio = " + folio);
                flV += val.insert(db);
            }
            
            
            if(pstmt.executeUpdate() == 1) 
            {
                return true;
            }
            else
            {
                fail = new Exception("Retorno de operacion ambiguo en " + sql);
                return false;
            }
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OrdenUpdate.class.getName()).log(Level.SEVERE, null, ex);
            fail = new Exception("Falló la operacion de actualización en la base de datos",ex);
            return false;
        }        
    }

    public boolean ownerMail(Database db) 
    {
        Sender mail =  new Sender();
        mail.init();
        try {
            if(mail.updateOwner(db,credential,this))
            {
                //System.out.println("Se envio el correo electronico para ownerMail.");
                return mail.send();
            }
            else
            {
                //System.out.println("No se envio el correo electronico para ownerMail.");
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }
    
    public Throwable updateOwner(Database db)
    {
        //>>>validacion de entrada
        if(super.getBD() == null || super.getBD().length() < 1)
        {
            fail = new Exception("No se indico el descriptor DB.");
            return fail;
        }
        if(super.getFolio() == null || super.getFolio() < 1)
        {
            fail = new Exception("No se indico el Folio.");
            return fail;
        }
        if(owner == null)
        {
            fail = new Exception("No se indico el nuevo encargado.");
            return fail;
        }
        if(owner.getpID() < 1)
        {
            fail = new Exception("No se indico el nuevo encargado.");
            return fail;
        }
        
        //>>>procesamiento
        //escribiendo en base de datos
        //System.out.println("SQL Ejecutando...");
        boolean flSQL = ownerSQL(db);
        if(fail != null) return fail;
        
        return fail;
    }

    public Throwable updateCompany(Database db) throws DatabaseException 
    {
        //>>>validacion de entrada
        if(super.getBD() == null || super.getBD().length() < 1)
        {
            fail = new Exception("No se indico el descriptor DB.");
            return fail;
        }
        if(getID() < 1)
        {
            fail = new Exception("No se indico el Folio.");
            return fail;
        }
        
        try 
        {
            boolean flSQL = companySQL(db);
            return null;
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Fallo la instrccion SQL para actualizar.", ex);
        }       
        
    }
    
    private boolean companySQL(Database db) throws SQLException
    {
        String sql = "UPDATE Orcom SET compNumber = ? WHERE ID = ?";
        PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
            pstmt.setString(1, company.getNumber());
            pstmt.setInt(2, getID());
                        
            int flV = 0;
            Value val;        
            val = new Value();
            val.setTraceID(trace.getTrace());
            val.setTable("Orcom");
            val.setAfter(company.getNumber());
            val.setField("compNumber");
            val.setBrief("Número de Cliente");     
            val.setLlave("folio="+folio);
            flV += val.insert(db);            
            //System.out.println("Ejecutando cambio de cliente con la cadena: " + sql);
            //System.out.println("Numero de cliente : " + company.getNumber());
            //System.out.println("ID de registro : " + getID());
            if(pstmt.executeUpdate() == 1) 
            {
                return true;
            }
            else
            {
                fail = new Exception("Retorno de operacion ambiguo en " + sql);
                return false;
            }    
    }
    
    public boolean companyMail(Database db) 
    {
        Sender mail =  new Sender();
        mail.init();
        try {
            if(mail.updateCompany(db,credential,this))
            {
                return mail.send();
            }
            else
            {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }
    
    public boolean folioEmail(Database connection) 
    {
        Sender mail =  new Sender();
        mail.init();
        try 
        {
            if(mail.updateFolio(connection,credential,this))
            {
                return mail.send();
            }
            else
            {
                return false;
            }
        } 
        catch (SQLException ex) 
        {
            return false;
        }
    }
    
    public int getNewFolio()
    {
        return newFolio;
    }
}
