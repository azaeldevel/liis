
package process;

import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import core.bobeda.Archivo;
import sales.Invoice;
import core.bobeda.FTP;
import core.bobeda.Vault;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.internet.InternetAddress;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Azael Reyes
 */
public class Attached 
{
    private static final String MYSQL_AVATAR_TABLE = "ProcessMailerAttaches";
    private int id;
    private Mail mail;
    private Vault vault;
    private Enterprise enterprise;
    private String seudoKey;
    
    public Attached(Mail mail,Vault vault,Enterprise enterprise,String seudoKey)
    {
        this.mail = mail;
        this.vault = vault;
        this.enterprise = enterprise;
        this.seudoKey = seudoKey;
    }
    
    public static Return exists(Database dbServer, List<Invoice> ins) throws SQLException
    {        
        for(Invoice invoice : ins)
        {
            String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE seudoKey ='" + invoice.getFullFolio() + "' LIMIT 1";
            ResultSet rs = dbServer.query(sql);
            if(rs.next())
            {
                return new Return(true,invoice.getFullFolio());
            }
        }
        return new Return(false);
    }
            
    public static boolean exists(Database dbServer, String seudoKey) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE seudoKey ='" + seudoKey + "' LIMIT = 1";
        ResultSet rs = dbServer.query(sql);
        if(rs.next())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /*public static Return importCN(Database dbServer,FTP ftpServer, Office office,List<Invoice> invoicies,InternetAddress[] lsTO,Enterprise enterprise,Person person,boolean duplicateFile) throws Exception
    {
        if(invoicies.size() == 0) return new Return(false,"No hay facturas que adjuntar.");
        //Enterprise enterprise = invoicies.get(0).getEnterprise(dbServer);
        //Todos las facturas deven tener el mismo cliente
        for(Invoice invoice:invoicies)
        {
            if(!invoice.getRFC().equals(enterprise.getRFC())) return new Return(false,"Todas la facturas deven tener el mismo cliente.");
        }
        
        enterprise.download(dbServer);
        if(!ftpServer.isExist(Vault.Type.FACTURA_XML,Vault.Origen.INTERNO, enterprise.getNumber()))
        {
            if(!ftpServer.addSubdirectory(Vault.Type.FACTURA_XML,Vault.Origen.INTERNO, enterprise.getNumber()))
            {
                return new Return(false,"No se pudo agregar el suddirectoria para la fatura.");
            }
        }
        List<Archivo> vaults = new ArrayList<>();
        String folios = "";
        for(Invoice invoice : invoicies)
        {
            if(invoice.getID() > 0)                
            {
                invoice.select(dbServer, office, invoice.getFolio(), Operational.Type.SalesInvoice);                
            }
            else
            {
                invoice.fromCN(dbServer, office, invoice.getFolio(), person);
            }
            invoice.downFolio(dbServer);
            invoice.downSerie(dbServer);
            if(invoice.downXMLArchivo(dbServer) && invoice.downPDFArchivo(dbServer))// ya hay archivos asociados?
            {
                if(duplicateFile)
                {//si es asi continuar con la siguenta factura
                    Return retXML = Archivo.add(dbServer, ftpServer,invoice.getFullFolio() + ".xml", invoice.getXML(),enterprise ,"XML importado desde CN60.", Vault.Type.FACTURA_XML, Vault.Origen.INTERNO);
                    if(retXML.isFail()) return retXML;
                    vaults.add((Archivo)retXML.getParam());
                    if(!invoice.upXML(dbServer, (Archivo)retXML.getParam())) return new Return(false,"Falló la asignacion de XML a la operacion de Factura");

                    Return retPDF = Archivo.add(dbServer, ftpServer,invoice.getFullFolio() + ".pdf", invoice.getPDF(),enterprise ,"PDF importado desde CN60.", Vault.Type.FACTURA_PDF, Vault.Origen.INTERNO);
                    if(retPDF.isFail()) return retPDF;
                    if(!invoice.upPDF(dbServer, (Archivo)retPDF.getParam())) return new Return(false,"Falló la asignacion de PDF a la operacion de Factura");
                    vaults.add((Archivo)retPDF.getParam());  
                }
                else
                {
                    //invoice.getXMLArchivo().download(dbServer, ftpServer, new File(System.getProperty("java.io.tmpdir")));
                    vaults.add(invoice.getXMLArchivo());
                    //invoice.getPDFArchivo().download(dbServer, ftpServer, new File(System.getProperty("java.io.tmpdir")));
                    vaults.add(invoice.getPDFArchivo());
                }
            }
            else
            {
                Return retXML = Archivo.add(dbServer, ftpServer,invoice.getFullFolio() + ".xml", invoice.getXML(),enterprise ,"XML importado desde CN60.", Vault.Type.FACTURA_XML, Vault.Origen.INTERNO);
                if(retXML.isFail()) return retXML;
                vaults.add((Archivo)retXML.getParam());
                if(!invoice.upXML(dbServer, (Archivo)retXML.getParam())) return new Return(false,"Falló la asignacion de XML a la operacion de Factura");

                Return retPDF = Archivo.add(dbServer, ftpServer,invoice.getFullFolio() + ".pdf", invoice.getPDF(),enterprise ,"PDF importado desde CN60.", Vault.Type.FACTURA_PDF, Vault.Origen.INTERNO);
                if(retPDF.isFail()) return retPDF;
                if(!invoice.upPDF(dbServer, (Archivo)retPDF.getParam())) return new Return(false,"Falló la asignacion de PDF a la operacion de Factura");
                vaults.add((Archivo)retPDF.getParam());  
            }
            folios += invoice.getFullFolio() + ",";
        }
        enterprise.downMailFact(dbServer);        
        if(lsTO == null) return new Return(false,"No hay lista de correos para enviar la factura");
        Mail mail = new Mail(-1);
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy"); 
        /*String folios = invoicies.get(0).getFullFolio();
        for(int i = 1; i < invoicies.size(); i++)
        {
            folios += "," + invoicies.get(i).getFullFolio();
        }*/
        /*String messageText = ""
                + "SIIL S.A. de C.V. através del servicio de correo electronico y en cumplimiento de la Resolución Miscelánea Fiscal vigente "
                + " hace entrega de los Comprobante Fiscal Digital No. " + folios + " en el correo enviado el dia " + dt.format(new Date(dbServer.getTimestamp().getTime())) +
                ", el cual se encuentra adjunto a este mensaje.<br>" +
                "Este correo ha sido generado de forma automática, favor de no responder a este correo porque no será leído.<br>" +
                "Para contactarnos, puede comunicarse al teléfono : (664) 623 1803<br>" +
                "Gracias por su preferencia.<br><br>" +
                "Atentamente:<br>" +
                "SIIL S.A. de C.V."; */  
        
        /*String messageText = ""
        + "<T1>Para:  " + invoicies.get(0).getReceptorNombre() + " ( " + invoicies.get(0).getReceptorRFC() + " )</T1><br><br>"
  
        + "Estimado Cliente,<br><br>"

        + "SIIL SA DE CV  ( SII941019BY3 ) Timbro para Usted la(s) Factura(s) con las siguientes características: <br><br>" 

        + "<table cellpadding=\"1\" cellspacing=\"1\" > ";
        for(Invoice invoice : invoicies)
        {
            messageText += "<tr >"
            + "<td width=\"150\" style=\"border-top:1pt solid black;\" ><b>Serie y Folio</b></td><td width=\"150\" style=\"border-top:1pt solid black;\" >" + invoice.getFullFolio() + " </td>"
            + "</tr>"
            + "<tr>"
            + "<td width=\"150\"><b>Fecha de Emisión</b></td><td width=\"150\">" + dt.format(invoice.getComprobanteFecha()) + "</td>" 
            + "</tr>"
            + "<tr>"
            + "<td width=\"150\" ><b>Monto Total</b></td><td width=\"150\"> $" + invoice.getComprobanteTotal() + " " + invoice.getComprobanteMoneda() + "</td>" 
            + "</tr>";
        }
        messageText += "</table><br>"
        + "Consulte los  datos  adjuntos o llame al teléfono (664) 623 1803<br><br>"         
        + "Este Comprobante fue enviado automáticamente por el sistema de generación y recepción de documentos por favor no responda a esté correo ya que no será leído. <br><br>"; 
        
        String subject = "CFDI " + folios + " SIIL S.A. de C.V.";
        mail.insert(dbServer,lsTO,null,null,messageText,"factura",subject);
        Return ret = add(dbServer,mail,vaults,enterprise);
        if(ret.isFail()) return ret;
        for(Invoice invoice : invoicies)
        {
            invoice.upEmail(dbServer, mail);
        }
        if(ret.isFail()) return ret;                
        return new Return(true,mail);
    }*/
    
    public boolean download(Database dbServer,FTP ftpServer,String directory) throws SQLException, IOException
    {
        String sql = "SELECT mail,vault,company FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = dbServer.query(sql);
        if(rs.next())
        {
            mail = new Mail(rs.getInt(1));
            vault = new Vault(rs.getInt(2));
            vault.download(dbServer,ftpServer,new File(directory));
            enterprise = new Enterprise(rs.getInt(3));
            enterprise.download(dbServer);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Attached(int id)
    {
        this.id = id;
    }
    
    public static List<Attached> search(Database dbServer,Mail mail) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE mail = " + mail.getID();
        ResultSet rs = dbServer.query(sql);
        List<Attached> lst = new ArrayList<>();
        while(rs.next())
        {
            lst.add(new Attached(rs.getInt(1)));
        }
        if(lst.isEmpty()) return null;
        return lst;
    }
     
    /*public static Enterprise guessCompany(Database dbServer,Invoice f,Office office,int folio) throws SQLException, Exception
    {        
        Enterprise enterprise = Enterprise.findByRFC(dbServer, f.getReceptorRFC());
        if(enterprise == null) 
        {
            return null;//new Return(false, "El RFC '" + f.getComprobante().getReceptor().getRfc() + "' no esta asociado a ningun cliente");
        }
        enterprise.download(dbServer);
        return enterprise;
    }*/
    
    /*public static Return importCN(Database dbServer,FTP ftpServer, Office office,Invoice invoice,InternetAddress[] lsTO,Enterprise enterprise,boolean duplicateFile) throws Exception
    {
        if(!ftpServer.isExist(Vault.Type.FACTURA_XML,Vault.Origen.INTERNO, enterprise.getNumber()))
        {
            if(!ftpServer.addSubdirectory(Vault.Type.FACTURA_XML,Vault.Origen.INTERNO, enterprise.getNumber()))
            {
                return new Return(false,"No se pudo agregar el suddirectoria para la fatura.");
            }
        }
        
        if(invoice.getID() > 0)                
        {
            invoice.select(dbServer, office, invoice.getFolio(), Operational.Type.SalesInvoice);
        }
        else
        {
            invoice.fromCN(dbServer, office, invoice.getFolio(), null);
        }
        invoice.downFolio(dbServer);
        invoice.downSerie(dbServer);
        List<Archivo> vaults = new ArrayList<>();
        if(invoice.downXMLArchivo(dbServer) && invoice.downPDFArchivo(dbServer))// ya hay archivos asociados?
        {
            if(duplicateFile)
            {//si es asi continuar con la siguenta factura
                Return retXML = Archivo.add(dbServer, ftpServer,invoice.getFullFolio() + ".xml", invoice.getXML(),enterprise ,"XML importado desde CN60.", Vault.Type.FACTURA_XML, Vault.Origen.INTERNO);
                if(retXML.isFail()) return retXML;
                vaults.add((Archivo)retXML.getParam());
                if(!invoice.upXML(dbServer, (Archivo)retXML.getParam())) return new Return(false,"Falló la asignacion de XML a la operacion de Factura");

                Return retPDF = Archivo.add(dbServer, ftpServer,invoice.getFullFolio() + ".pdf", invoice.getPDF(),enterprise ,"PDF importado desde CN60.", Vault.Type.FACTURA_PDF, Vault.Origen.INTERNO);
                if(retPDF.isFail()) return retPDF;
                if(!invoice.upPDF(dbServer, (Archivo)retPDF.getParam())) return new Return(false,"Falló la asignacion de PDF a la operacion de Factura");
                vaults.add((Archivo)retPDF.getParam());  
            }
            else
            {
                //invoice.getXMLArchivo().download(dbServer, ftpServer, new File(System.getProperty("java.io.tmpdir")));
                vaults.add(invoice.getXMLArchivo());
                //invoice.getPDFArchivo().download(dbServer, ftpServer, new File(System.getProperty("java.io.tmpdir")));
                vaults.add(invoice.getPDFArchivo());
            }
        }
        else
        {
            Return retXML = Archivo.add(dbServer, ftpServer,invoice.getFullFolio() + ".xml", invoice.getXML(),enterprise ,"XML importado desde CN60.", Vault.Type.FACTURA_XML, Vault.Origen.INTERNO);
            if(retXML.isFail()) return retXML;
            vaults.add((Archivo)retXML.getParam());
            if(!invoice.upXML(dbServer, (Archivo)retXML.getParam())) return new Return(false,"Falló la asignacion de XML a la operacion de Factura");

            Return retPDF = Archivo.add(dbServer, ftpServer,invoice.getFullFolio() + ".pdf", invoice.getPDF(),enterprise ,"PDF importado desde CN60.", Vault.Type.FACTURA_PDF, Vault.Origen.INTERNO);
            if(retPDF.isFail()) return retPDF;
            if(!invoice.upPDF(dbServer, (Archivo)retPDF.getParam())) return new Return(false,"Falló la asignacion de PDF a la operacion de Factura");
            vaults.add((Archivo)retPDF.getParam());  
        }
            
        Return retXML = Archivo.add(dbServer, ftpServer,invoice.getFullFolio() + ".xml", invoice.getXML(),enterprise ,"XML importado desde CN60.", Vault.Type.FACTURA_XML, Vault.Origen.INTERNO);
        if(retXML.isFail()) return retXML;
        Return retPDF = Archivo.add(dbServer, ftpServer,invoice.getFullFolio() + ".pdf", invoice.getPDF(),enterprise ,"PDF importado desde CN60.", Vault.Type.FACTURA_PDF, Vault.Origen.INTERNO);
        if(retPDF.isFail()) return retPDF;
        enterprise.downMailFact(dbServer);        
        if(lsTO == null) return new Return(false,"No hay lista de correos para enviar la factura");
        Mail mail = new Mail(-1);
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy"); 
        String messageText = ""
                + "SIIL S.A. de C.V. a través del servicio de correo electronico y en cumplimiento de la Resolución Miscelánea Fiscal vigente"
                + " hace entrega del Comprobante Fiscales Digitales con folios " + invoice.getFilledFolio() +
                ", los cuales se encuentran adjuntos a este mensaje.<br>" +
                "Este correo ha sido generado de forma automática, favor de no responder a este correo porque no será leído.<br>" +
                "Para contactarnos, puede comunicarse al teléfono : (664) 623 1803<br>" +
                "Gracias por su preferencia.<br><br>" +
                "Atentamente:<br>" +
                "SIIL S.A. de C.V.";                
        mail.insert(dbServer,lsTO, null, null,messageText, "factura","CFDI " + invoice.getFullFolio() + " SIIL S.A. de C.V.");
        //List<Archivo> vaults = new ArrayList<>();
        //vaults.add((Archivo)retXML.getParam());
        //vaults.add((Archivo)retPDF.getParam());
        Return ret = add(dbServer,mail,vaults,enterprise); 
        if(ret.isFail()) return ret;                
        return new Return(true);
    }*/

    public static Return add(Database dbServer,Mail mail, List<Archivo> vaults, Enterprise enterprise) throws SQLException 
    {
        if(dbServer == null)
        {
            return new Return(false,"Servidor de base de datos incorrecto.");
        }
        if(mail ==null)
        {
            return new Return(false,"mail is null");
        }
        if(vaults == null)
        {
            return new Return(false,"bobeda is null");
        }
        if(enterprise == null)
        {
            return new Return(false,"cliente is null");
        }
        
        for(Archivo vault : vaults )
        {
            vault.download(dbServer);
            String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (mail,vault,company,seudoKey) VALUES(" + mail.getID() + "," + vault.getID() + "," + enterprise.getID() + ",'" + FilenameUtils.removeExtension(vault.getNombre()) + "')";
            //System.out.println(sql);
            Statement stmt = dbServer.getConnection().createStatement();
            int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            if(affected != 1)
            {
                return new Return(false,"Se afectaron " + affected + " registros(s) : " + sql);
            }
        }
        return new Return(true);
    }

    /**
     * @return the mail
     */
    public Mail getMail() {
        return mail;
    }

    /**
     * @return the vault
     */
    public Vault getVault() {
        return vault;
    }

    /**
     * @return the enterprise
     */
    public Enterprise getEnterprise() {
        return enterprise;
    }

    /**
     * @return the seudoKey
     */
    public String getSeudoKey() {
        return seudoKey;
    }
}
