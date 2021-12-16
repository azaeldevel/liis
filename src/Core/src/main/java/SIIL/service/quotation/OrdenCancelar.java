
package SIIL.service.quotation;

import SIIL.trace.Trace;
import com.galaxies.andromeda.util.Texting.Confirmation;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 *
 * @author Azael Reyes
 */
public class OrdenCancelar extends ServiceQuotation implements Runnable
{    
    public OrdenCancelar(ServiceQuotation orden) 
    {
        super(orden);
    }

    private boolean sendMailCancel() throws MessagingException, AddressException, SQLException, UnsupportedEncodingException 
    {
        Sender mail =  new Sender();
        mail.init();   
        downCompany(database);
        getCompany().complete(database);
        mail.cancel(database,credential,this);
        return mail.send();
    }
    
    private void cancel()
    {
        progress.setProgress(60, "Iniciando registro de trace...");
        setTrace(new Trace(credential.getBD(), credential.getUser(), "Cancelación de Documento - Cotización " + getFolio()));
        try 
        {
            downCompany(database);
            progress.setProgress(70, "Enviando datos a servidor...");
            int flCancel = updateCancel(database,terminalComment);
            getTrace().insert(database);
            progress.setProgress(80, "Enviando correo a servidor...");
            boolean flMail = sendMailCancel();
            progress.setProgress(90, "Correo enviado.");
            if(flMail)
            {
                if(flCancel == 1)
                {
                    database.commit();
                    progress.finalized(new Confirmation("Se cancelo el Documento " + getFolio()));
                }
                else
                {
                    database.rollback();
                    progress.fail(new java.lang.Error("Problemas al enviar el correo eléctronico"));
                }
            }
        } 
        catch (MessagingException | UnsupportedEncodingException ex) 
        {
            progress.fail(new com.galaxies.andromeda.util.Texting.Error("Problema inisperado con el envio de email"));
            try 
            {
                database.rollback();
            } 
            catch (SQLException ex1) 
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("Se intento retroceder en la operacion y ocurrio un error",ex1));
                Logger.getLogger(ServiceQuotation.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ServiceQuotation.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) 
        {
            progress.fail(new Error("Problema inisperado con de datos"));
            try 
            {
                database.rollback();
            } 
            catch (SQLException ex1) 
            {
                progress.fail(new Error("Se intento retroceder en la operacion y ocurrio un error",ex1));
                Logger.getLogger(ServiceQuotation.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ServiceQuotation.class.getName()).log(Level.SEVERE, null, ex);
        }  
        catch (Exception ex) 
        {
            progress.fail(new com.galaxies.andromeda.util.Texting.Error("Erro desconocido",ex));
            try 
            {
                database.rollback();
            } 
            catch (SQLException ex1) 
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("Se intento retroceder en la operacion y ocurrio un error",ex1));
                Logger.getLogger(ServiceQuotation.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ServiceQuotation.class.getName()).log(Level.SEVERE, null, ex);
        }         
    }
    
    @Override
    public void run() {
        cancel();
    }
    
}
