
package SIIL.client;

import SIIL.core.Office;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.service.quotation.Estado;
import session.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import process.State;

/**
 * Resulve el acceso a diferentes modulos. No valida ni es un gentor de seguridad.
 * @version 0.0.1
 * @author Azael Reyes
 */
public class Scope 
{
    private Integer ID;
    public String BD;
    public Office office;
    public String module;
    public String context;
    public String scope;
    public User user;
        
    public Scope(Database db,Integer id)
    {
        ID = id;
        Throwable th = download(db);
    }

    public Scope() 
    {
        
    }

    private Throwable download(Database db) 
    {
        if(ID > 0)
        {
            return downloadByID(db);
        }
        else if(BD != null & office != null & module != null & context != null & user != null)
        {
            return downloadByExpresion(db);
        }
        else
        {
            return new Error("Scope no determinable");
        }
    }

    public String getExpresionWhere(Boolean personal, Department department, Office office) 
    {
        String where = "";
        
        if(scope.equals("E"))
        {
            return getExpresionWhereEnterprise(personal, department,office);
        }
        else if(scope.equals("S"))
        {
            return getExpresionWhereOficina(personal, department);
        }
        else if(scope.equals("D"))
        {
            return getExpresionWhereDepartment(personal);
        }
        else
        {
            return null;
        }
    }

    private String getExpresionWhereEnterprise(Boolean personal, Department department, Office officeSelect) 
    {
        String where = "";
        int subExpreCount = 0;
        
        if (personal) 
        {
            if(where.length() > 0)
            {
                where += "(ownerPerson = " + user.getpID() + " OR ownerPerson IS NULL)";
            }
            else
            {
                where = "(ownerPerson = " + user.getpID() + " OR ownerPerson IS NULL)";
            }
            subExpreCount++;
        } 
        
        if(officeSelect != null) 
        {
            if(where.length() > 0)
            {
                where += " AND (suc = '" + officeSelect.getCode() + "' OR suc IS NULL)";
            }
            else
            {
                where = " (suc = '" + officeSelect.getCode() + "' OR suc IS NULL)";
            }
            subExpreCount++;
        } 
        
        if(department != null)
        {
            if(where.length() > 0)
            {
                where += " AND (department = '" + department.code + "' OR department IS NULL)";                
            }
            else
            {
                where = " (department = '" + department.code + "' OR department IS NULL)"; 
            }
            subExpreCount++;
        }
        
        if(subExpreCount == 1)
        {
            return where;
        }
        else if(subExpreCount > 1)
        {
            return "(" + where + ")";
        }
        else
        {
            return null;
        }        
    }

    private Throwable downloadByID(Database db) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Throwable downloadByExpresion(Database db) 
    {
        String sql = "SELECT ID,scope FROM Scope WHERE BD = ? AND office = ? AND module = ? AND context = ? AND user = ? ";
        PreparedStatement stmt;
        try 
        {
            stmt = db.getConnection().prepareStatement(sql);
            stmt.setString(1, BD);
            stmt.setString(2, office.getCode());
            stmt.setString(3, module);
            stmt.setString(4, context);
            stmt.setString(5, user.getAlias());
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                this.ID = rs.getInt("ID");
                this.scope = rs.getString("scope");
                return null;
            } 
            return new java.lang.Error("Falló la consulta a la base de datos para descargar contenido Scope, no match.");
        } 
        catch (SQLException ex) 
        {
            return new java.lang.Error("Fallo la consulta a la base de datos para descargar contenido Scope",ex);
        }
    }

    private String getExpresionWhereOficina(Boolean personal, Department department) 
    {
        String where = "";
        int subExpreCount = 0;
        
        if (personal) 
        {
            if(where.length() > 0)
            {
                where += "(ownerPerson = " + user.getpID() + " OR ownerPerson IS NULL)";
            }
            else
            {
                where = "(ownerPerson = " + user.getpID() + " OR ownerPerson IS NULL)";
            }
            subExpreCount++;
        } 
        
        if(department != null)
        {
            if(where.length() > 0)
            {
                where += " AND (department = '" + department.code + "' OR department IS NULL)";                
            }
            else
            {
                where = " (department = '" + department.code + "' OR department IS NULL)"; 
            }
            subExpreCount++;
        }
        
        if(subExpreCount == 1)
        {
            return where;
        }
        else if(subExpreCount > 1)
        {
            return "(" + where + ")";
        }
        else
        {
            return null;
        }        
    }

    private String getExpresionWhereDepartment(Boolean personal) 
    {
        String where = "";
        int subExpreCount = 0;
        
        if (personal) 
        {
            where = "(ownerPerson = " + user.getpID() + " ) AND (department = '" + scope + "')";            
        } 
        else
        {
            
        }
                
        if(subExpreCount == 1)
        {
            return where;
        }
        else if(subExpreCount > 1)
        {
            return "(" + where + ")";
        }
        else
        {
            return null;
        }        
    }

    public Throwable requestScope(Database db,String BD,Office office,String module,String context,User user) 
    {
        this.ID = -1;
        this.BD = BD;
        this.office = office;
        this.module = module;
        this.context = context;
        this.user = user;        
        return download(db);
    }

    /**
     * Restorna una lista con los correos electronicos de insterados en la 
     * operación realiza por el usuario indicado. Esta lista es construida según
     * la tabla de Scope. Se retorna los correos de aquellos que tiene alcance en 
     * el departamento del usuario, la sucursal y/o la empresa según la tabla de 
     * Scope.
     * @param db
     * @param bd
     * @param office
     * @param orserv
     * @param contex
     * @param user
     * @param estado El codigo de estado indica la subcategoria 1 en la tabla de Scope.
     * @return 
     */
    public List<String> requestEmailByScope(Database db, String bd, Office office, String orserv, String contex, Person user,State estado) 
    {
        List<String> emails = new ArrayList<>();
        String department,sucursal,BD;
        //System.out.println(">>>Usuarios");
        String sql;
        // todos los del mismo departamento con visivilidad de departamento
        department = user.getDepartment();
        //System.out.println("-------");
        sql = "SELECT email,alias FROM Scope_Resolved WHERE scope = 'D' AND email IS NOT NULL AND department = '" + department + "' AND context = '" + contex + "' AND subcat1 = '" + estado.getCode() + "' AND office = '" + user.getOffice().getCode() + "'";
        try 
        {
            Statement stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) 
            {
                emails.add(rs.getString(1));
                //System.out.println(rs.getString(2));
            }            
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Scope.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        // todos los de la misma sucursal con visivilidad de sucursal
        sucursal = user.getOffice().getCode();
        //System.out.println("-------");
        sql = "SELECT email,alias FROM Scope_Resolved WHERE scope = 'S' AND email IS NOT NULL AND office = '" + sucursal + "' AND context = '" + contex + "' AND subcat1 = '" + estado.getCode() + "'";
        //System.out.println("Cadena sql para requestEmailByScope : " + sql);
        try 
        {
            Statement stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) 
            {
                emails.add(rs.getString(1));
                //System.out.println(rs.getString(2));
            }          
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Scope.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        // todos los de la misma empresa con visivilidad de empresa
        BD = user.getOffice().getBD();
        //System.out.println("-------");
        sql = "SELECT email,alias FROM Scope_Resolved WHERE scope = 'E' AND email IS NOT NULL AND BD = '" + BD + "' AND context = '" + contex + "' AND subcat1 = '" + estado.getCode() + "' AND office = '" + user.getOffice().getCode() + "'";
        try 
        {
            Statement stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) 
            {
                emails.add(rs.getString(1));
                //System.out.println(rs.getString(2));
            }          
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Scope.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        //System.out.println("<<<");
        return emails;
    }
}
