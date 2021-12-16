
package SIIL.service.quotation;

import SIIL.Server.Database;
import SIIL.Server.Person;
import session.Credential;
import SIIL.core.Exceptions.DatabaseException;
import database.mysql.sales.Quotation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @version 0.2
 * @author areyes
 */
public class Sender extends SIIL.Server.SenderLocal
{
    private Throwable fail;
    
    public static List<String> generateTO(Database conn,ServiceQuotation serviceQuotation) throws SQLException
    {
        serviceQuotation.downOwner(conn);
        serviceQuotation.downOwner2(conn);
        List<String> ls = new ArrayList<>();
        serviceQuotation.getOwner().downloadEmail(conn);
        if(serviceQuotation.getOwner().getEmail() != null)           
        {
            ls.add(serviceQuotation.getOwner().getEmail());
        }
        serviceQuotation.getOwner2().downloadEmail(conn);
        if(serviceQuotation.getOwner2().getEmail() != null)           
        {
            ls.add(serviceQuotation.getOwner2().getEmail());
        }
        return ls;
    }
    
    public static String getRowsDescriptions(Database database,ServiceQuotation orden) throws SQLException
    {
        String detail = "";
        
        orden.downQuotation(database.getConnection());
        if(orden.getQuotation() == null) return detail;
        Quotation oper = orden.getQuotation();
        
        List<core.Renglon> rows = core.Renglon.select(database, oper, null);
        for(core.Renglon row : rows)
        {
            detail += "<br>";
            detail += "&#8195";
            detail += row.getDescription();
            detail += ",";
            detail += row.getCantidad();
        }
        return detail;
    }

    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String addDocMensaje(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String mensage = "El documento " + orden.getFullFolio(db.getConnection()) + " se ha creado y esta en espera de edición.<br> Cliente " + orden.getCompany() + " <br>Atte. " + credential.getUser();
        return mensage;
    }
    
    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String addDocSubject(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String subject = "Notificacion de documento creado " + orden.getFolio();
        return subject;
    }
    
    @Deprecated
    public boolean addDoc(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        Subject = "Notificacion de documento creado " + orden.getFullFolio(db.getConnection()); 
        //String pasedUser = credential.getUser().toString().equals("Jesus Garcias") ? "Jesus Garcia" : credential.getUser().toString();
        Mensage = "El documento " + orden.getFullFolio(db.getConnection()) + " se ha creado y esta en espera de edicion.<br>Cliente " + orden.getCompany() + " <br>Atte. " + credential.getUser();
        //Get email list                         
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return true;
    }
    
    @Deprecated
    public boolean confirmBuild(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        Subject = "Confirmacion de Arribo " + orden.getFullFolio(db.getConnection()); 
        Mensage = "El documento " + orden.getFullFolio(db.getConnection()) + " se ha completado. <br>Cliente " + orden.getCompany() ;
        
        Mensage += " <br>Atte. " + credential.getUser();
        //Get email list                       
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return false;
    }

    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String pedMensaje(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");        
        String mensage = "Se estima que para " + dt.format(((OrdenETA)orden).getFhETA()) + " se completará el documento " + orden.getFolio() + " <br>Cliente " + orden.getCompany();
        mensage += getRowsDescriptions(db, orden);
        mensage += " <br>Atte. " + credential.getUser();
        return mensage;
    }
    
    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String pedSubject(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String subject = "Asignacion de ETA " + orden.getFolio();        
        return subject;
    }    
    
    @Deprecated
    public boolean pedBuild(Database db,Credential credential,ServiceQuotation orden,Date fecha) throws SQLException
    {
        Subject = "Asignacion de ETA " + orden.getFullFolio(db.getConnection()); 
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");        
        Mensage = "Se estima que para " + dt.format(fecha) + " se completará el documento " + orden.getFolio() + " <br>Cliente " + orden.getCompany();
        Mensage += getRowsDescriptions(db, orden);
        Mensage += " <br>Atte. " + credential.getUser();
        //Get email list                       
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return false;
    }

    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String authoMensaje(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String mensage = "El documento " + orden.getFolio() + " fúe autorizada, Cliente " + orden.getCompany();
        mensage += getRowsDescriptions(db, orden);
        mensage += "<BR>Atte. " + credential.getUser();
        return mensage;
    }
    
    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String authoSubject(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String subject = "Autorizacion de Documento " + orden.getFolio();
        return subject;
    }
    
    @Deprecated
    public boolean authoBuild(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        Subject = "Autorizacion de Documento " + orden.getFullFolio(db.getConnection()); 
        Mensage = "El documento " + orden.getFullFolio(db.getConnection()) + " fúe autorizada, Cliente " + orden.getCompany();
        Mensage += getRowsDescriptions(db, orden);
        Mensage += "<BR>Atte. " + credential.getUser();
        //Get email list                       
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return false;
    }
    

    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String editMensaje(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String mensage = "El documento " + orden.getFolio() + " fúe editado y está en espera de autorización <br>Cliente " + orden.getCompany() ;
        mensage += getRowsDescriptions(db,orden);
        mensage += " <br>Atte. " + credential.getUser();
        return mensage;
    }
    
    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String editSubject(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String subject = "Documento editado " + orden.getFolio();
        return subject;
    }

    @Deprecated
    boolean Edit(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        Subject = "Documento editado " + orden.getFolio();
        Mensage = "El documento " + orden.getFullFolio(db.getConnection()) + " fúe editado y está en espera de autorización <br>Cliente " + orden.getCompany() ;
        Mensage += getRowsDescriptions(db,orden);
        Mensage += " <br>Atte. " + credential.getUser();
        //Get email list                       
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return false; 
    }

    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String confirmMensaje(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String mensage = "El pedido " + orden.getFolio() + " está en almacen <br>Cliente "  + orden.getCompany();
        mensage += getRowsDescriptions(db, orden);
        mensage +=" <br>Atte. " + credential.getUser();
        return mensage;
    }
    
    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String confirmSubject(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String subject = "Confirmacion de Arribo " + orden.getFolio();
        return subject;
    }
    
    @Deprecated
    boolean confirm(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        Subject = "Confirmacion de Arribo " + orden.getFullFolio(db.getConnection()); 
        Mensage = "El pedido " + orden.getFullFolio(db.getConnection()) + " está en almacen <br>Cliente "  + orden.getCompany();
        Mensage += getRowsDescriptions(db, orden);
        Mensage +=" <br>Atte. " + credential.getUser();
        //Get email list                       
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return false; 
    }
    
    @Deprecated
    public boolean cancel(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        Subject = "Cancelacion de Documento " + orden.getFullFolio(db.getConnection()); 
        Mensage = "El documento " + orden.getFullFolio(db.getConnection()) + " ha sido cancelado <br>Cliente " + orden.getCompany() + " <br>Atte. " + credential.getUser();
        //Get email list                       
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return false; 
    }

    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String surtirMensaje(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String mensage = "El documento " + orden.getFolio() + " Sele asigno el S.A. "  + orden.getSA() + "  <br>Cliente "  + orden.getCompany() + " <br>Atte. " + credential.getUser();
        return mensage;
    }
    
    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String surtirSubject(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String subject = "Asignacion de S.A. " + orden.getFolio();
        return subject;
    }
    
    @Deprecated
    boolean surtir(Database db,Credential credential,ServiceQuotation orden,String sa) throws SQLException
    {
        Subject = "Asignacion de S.A. " + orden.getFullFolio(db.getConnection()); 
        Mensage = "El documento " + orden.getFullFolio(db.getConnection()) + " Sele asigno el S.A. "  + sa + "  <br>Cliente "  + orden.getCompany() + " <br>Atte. " + credential.getUser();
        //Get email list                       
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return false;    
    }

    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String termMensaje(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String mensage = "El documento " + orden.getFolio() + " ha sido finalizado  <br>Cliente "  + orden.getCompany() + " <br>Atte. " + credential.getUser();
        return mensage;
    }
    
    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return
     * @throws SQLException 
     */
    public static String termSubject(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        String subject = "Finalizacion de documento " + orden.getFolio() + "";        
        return subject;
    }
    
    @Deprecated
    boolean term(Database db,Credential credential,ServiceQuotation orden) throws SQLException
    {
        Subject = "Finalizacion de documento " + orden.getFullFolio(db.getConnection()); 
        Mensage = "El documento " + orden.getFullFolio(db.getConnection()) + " ha sido finalizado  <br>Cliente "  + orden.getCompany() + " <br>Atte. " + credential.getUser();
        //Get email list                       
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return false;  
    }
    public static List<String> generateTO(Database conn, Person manager, Person manager2)
    {
        String sql;
        PreparedStatement stmt;
        ResultSet rs;
        //Correo de Responsable
        sql = "SELECT email FROM Persons WHERE pID = ? ";
        if(manager2 != null)
        {
            sql = sql + " OR pID = " + manager2.getpID();
        }
        try 
        {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setInt(1, manager.getpID());
            rs = stmt.executeQuery();
            while(rs.next())
            {
                List<String> list = new ArrayList<>();
                list.add(rs.getString("email"));
                return list;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }
    
    public static List<String> generateTO(Database conn, Person manager)
    {
        String sql;
        PreparedStatement stmt;
        ResultSet rs;
        //Correo de Responsable
        sql = "SELECT email FROM Persons WHERE pID = ?";
        try 
        {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setInt(1, manager.getpID());
            rs = stmt.executeQuery();
            if(rs.next())
            {
                List<String> list = new ArrayList<>();
                list.add(rs.getString("email"));
                return list;
            }
            else
            {
                System.err.println("No Se encontro elementos en la lista de correos responsables");
                return null;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * 
     * @param conn
     * @param manager 
     */
    @Deprecated
    private void setTO(Database conn, Person manager)
    {
        String sql;
        PreparedStatement stmt;
        ResultSet rs;
        //Correo de Responsable
        sql = "SELECT email FROM Persons WHERE pID = ?";
        try 
        {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setInt(1, manager.getpID());
            rs = stmt.executeQuery();
            if(rs.next())
            {
                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(rs.getString("email")));
                return;
            }
            else
            {
                System.err.println("No Se encontro elementos en la lista de correos responsables");
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            fail = ex;
            return;
        } 
        catch (MessagingException ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            fail = ex;
            return;
        }
    }

    @Deprecated
    public boolean updateOwner(Database db,Credential credential,ServiceQuotation orden) throws SQLException 
    {
        Subject = "Modificación de Encargado en " + orden.getFolio(); 
        Mensage = "En el documento " + orden.getFolio() + " se cambio el campo de encargado para asignar a " + orden.getOwner().toString() + "  como nuevo encargado <br>Atte. " + credential.getUser() ;
        //Get email list                    
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return true;
    }

    /**
     * 
     * @param db
     * @param credential
     * @param orden
     * @return 
     */
    public static List<String> generateCC(Database db, Credential credential, ServiceQuotation orden) 
    {
        SIIL.client.Scope scope = new SIIL.client.Scope();
        List<String> list = scope.requestEmailByScope(db,credential.getBD(),credential.getOffice(),"orserv","email",orden.getOwner(),orden.getState());
        //quien hace la operacion
        credential.getUser().downloadEmail(db);
        String email3 = credential.getUser().getEmail();
        list.add(email3);
        //el encargado de la orden
        orden.getOwner().downloadEmail(db);
        String mailOwner = orden.getOwner().getEmail();
        list.add(mailOwner);
        
        //notificaciones para tecnico en esta de edicion y confirmacion de arribo.
        if((orden.getState().getCode().equals("docpen") | orden.getState().getCode().equals("pedArrb")) && orden.getTechnical() != null)
        {
            orden.getTechnical().downloadEmail(db);
            String mailTechnical = orden.getTechnical().getEmail();
            if(mailTechnical != null)
            {
                list.add(mailTechnical);
            }
        }
        return list;
    }
    
    @Deprecated
    private void setCC(Database db, Credential credential, ServiceQuotation orden) 
    {
        SIIL.client.Scope scope = new SIIL.client.Scope();
        List<String> list = scope.requestEmailByScope(db,credential.getBD(),credential.getOffice(),"orserv","email",orden.getOwner(),orden.getState());
        for(String email : list)
        {
            try 
            {
                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(email));
            } 
            catch (AddressException ex) 
            {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                fail = ex;
                return;
            } 
            catch (MessagingException ex) 
            {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                fail = ex;
                return;
            }
        }
        try 
        {
            credential.getUser().downloadEmail(db);
            String email3 = credential.getUser().getEmail();
            if(email3 != null) message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(email3));
        } 
        catch (MessagingException ex) 
        {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                fail = ex;
                return;
        } 
        catch (DatabaseException ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        try 
        {          
            orden.getOwner().downloadEmail(db);
            String mailOwner = orden.getOwner().getEmail();
            if(mailOwner != null)
            {
                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mailOwner));
            }
        } 
        catch (MessagingException ex) 
        {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                fail = ex;
                return;
        } 
        catch (DatabaseException ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //notificaciones para tecnico en esta de edicion y confirmacion de arribo.
        if(orden.getState().getCode().equals("docpen") | orden.getState().getCode().equals("pedArrb"))
        {
            try 
            {          
                orden.getTechnical().downloadEmail(db);
                String mailTechnical = orden.getTechnical().getEmail();
                if(mailTechnical != null)
                {
                    message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mailTechnical));
                }
            } 
            catch (MessagingException ex) 
            {
                    Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                    fail = ex;
                    return;
            } 
            catch (DatabaseException ex) 
            {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        setBCC();
    }
    
    @Deprecated
    private void setBCC() 
    {
        {
            try 
            {
                message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse("app@siil.mx"));
            } 
            catch (AddressException ex) 
            {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                fail = ex;
                return;
            } 
            catch (MessagingException ex) 
            {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                fail = ex;
                return;
            }
        }
    }
    
    public static List<String> generateBCC() 
    {
        List<String> l = new ArrayList<>();
        l.add("app@siil.mx");
        return l;
    }
    
    @Deprecated
    public boolean updateCompany(Database db,Credential credential,ServiceQuotation orden) throws SQLException 
    {
        Subject = "Modificacion de cliente en " + orden.getFolio(); 
        Mensage = "En el documento " + orden.getFolio() + " se ha modificado el campo de cliente para asignar a " + orden.getCompany() + " como nuevo cliente <br>Atte. " + credential.getUser();
        //Get email list
        //Get email list                         
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return true;
    }

    @Deprecated
    public boolean updateFolio(Database db,Credential credential,OrdenUpdate orden) throws SQLException 
    {
        Subject = "Modificación de folio " + orden.getFolio() + " --> " + orden.getNewFolio(); 
        Mensage = "En el documento " + orden.getFolio() + " se ha modificado el campo de folio para asignar a " + orden.getNewFolio() + " como nuevo folio<br>Atte. " + credential.getUser();
        //Get email list                        
        setTO(db, orden.getOwner());        
        if(fail != null)
        {
            return false;
        }
        
        setCC(db,credential,orden); 
        if(fail != null)
        {
            return false;
        }
        
        return true;
    }    
}
