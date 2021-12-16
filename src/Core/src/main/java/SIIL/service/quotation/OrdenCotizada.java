
package SIIL.service.quotation;

import SIIL.trace.Trace;
import com.galaxies.andromeda.util.Texting.Confirmation;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import process.ImportsCN;
import process.Mail;

/**
 *
 * @author Azael
 */
public final class OrdenCotizada extends ServiceQuotation implements Runnable
{    

    public OrdenCotizada(ServiceQuotation o) 
    {
        super(o);
    }

    public OrdenCotizada() 
    {
    }

    
    public void editar() throws SQLException, MessagingException, AddressException, UnsupportedEncodingException 
    {
        progress.setProgress(10, "Iniciando trace...");
        setTrace(new Trace(credential.getBD(), credential.getUser() , "Edición - Cotización " + getFolio()));
        getTrace().insert(getServerDB());
        progress.setProgress(20, "Importando Cotizacion desde el CN60...");
        ImportsCN imp = new ImportsCN();
        try 
        {
            downFolio(getServerDB().getConnection());
            imp.importCNQuotation(this, getServerDB());
            /*if(imp.getCountCNQuote() < 1)
            {
                progress.fail(new java.lang.Error("No hay renglones en la cotización, deve agegarlos antes de realizar esta operación."));
                return;
            }*/
        } 
        catch (SQLException | IOException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            progress.fail(new java.lang.Error("Falló en importación de cotización " + ex.getMessage()));
            return;
        }
        progress.setProgress(20, "Insertando datos..."); 
        int fl = updateEdit();       
        progress.setProgress(40, "Datos insertados.");
        progress.setProgress(50, "Enviando correo...");
        boolean flMail = sendMail();        
        progress.setProgress(80, "Correo enviado.");
        progress.setProgress(80, "Confirmando transacción...");
        if(flMail)
        {
            int mailCount = Mail.getCount(database);
            String comment;
            if(fl == 1)
            {
                getServerDB().commit();
                if(mailCount < 10)
                {
                    comment = "Operacion completada satisfactoriamente.";
                }
                else
                {
                    comment = "Operacion completada satisfactoriamente. Hay " + mailCount + " correos espera.";
                }
                progress.finalized(new Confirmation(comment));                
            }
            else
            {
                getServerDB().rollback();
                progress.fail(new java.lang.Error("La inserrcion de datos resulto incosistente, no se guardanran los datos."));
            }
        }
        else
        {
            getServerDB().rollback();
            progress.fail(new java.lang.Error("Fallo al enviar el correo electronico, la operacion se cancela."));
            
        }
    }

    @Override
    public void run() 
    {
        try 
        {
            editar();
        } 
        catch (SQLException ex) 
        {
            try 
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("Un error inseperado ocurrio, la base de datos no puede aceptar la operación",ex));
                getServerDB().rollback();
            } 
            catch (SQLException ex1) 
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("Se intento cancelar lo operación, sin embargo, un error inesperado ocurrio, el estado actual de los datos es ambiguo, consulte a su administrador.",ex1));
                Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex);            
        } 
        catch (MessagingException | UnsupportedEncodingException ex) 
        {
            try 
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("Falló el envio de correo electrónico",ex));
                getServerDB().rollback();
            } 
            catch (SQLException ex1) 
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("Se intento cancelar lo operación, sin embargo, un error inesperado ocurrio, el estado actual de los datos es ambiguo, consulte a su administrador.",ex1));
                Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(java.lang.Throwable ex)
        {
            progress.fail(new com.galaxies.andromeda.util.Texting.Error("Falló inesperado, consulte a su administrador",ex));
            Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean sendMail() throws MessagingException, AddressException, SQLException, UnsupportedEncodingException 
    {
        process.Mail mail = new process.Mail(-1);
        return mail.insert(getServerDB(), Sender.generateTO(database, this), Sender.generateCC(database,credential,this), Sender.generateBCC(), Sender.editMensaje(database, credential, this), "orserv", Sender.editSubject(database, credential, this));
    }
}
