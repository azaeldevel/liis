
package sales;

import SIIL.CN.Sucursal;
import SIIL.CN.Tables.CN60;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.services.Trabajo;
import SIIL.services.order.Association;
import SIIL.trace.Trace;
import SIIL.trace.Value;
import core.FailResultOperationException;
import core.Renglon;
import core.bobeda.Archivo;
import core.bobeda.FTP;
import database.mysql.sales.Operational;
import database.mysql.sales.Remision;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
//import mx.bigdata.sat.cfdi.CFDI;
//import mx.bigdata.sat.cfdi.CFDIFactory;
//import mx.bigdata.sat.cfdi.v33.schema.CTipoFactor;
//import mx.bigdata.sat.common.CFD;
//import mx.bigdata.sat.security.KeyLoaderEnumeration;
//import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import org.apache.commons.io.FilenameUtils;
import static org.jooq.impl.DSL.date;
import org.jooq.tools.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import process.Attached;
import process.Mail;
import process.Return;
import process.State;



/**
 *
 * @author Azael Reyes
 */
public class Invoice extends Operational
{
    private static final String MYSQL_AVATAR_TABLE = "SalesInvoice";

    
    public enum SATestado
    {
        INCOMPLETA,
        VIVA,
        CANCELADA
    }
    
    private FileInputStream xml;
    //private CFD cfdi;//private Object cfdi;
    private FileInputStream pdf;
    private Archivo ordenesArchivo;
    private Archivo pdfArchivo;
    private Archivo xmlArchivo;
    private PrivateKey key;
    private X509Certificate cert;  
    private SATestado satEstado;
    private Trabajo trabajo;
    
    /*public Date getComprobanteFecha() throws Exception
    {
        if(cfdi == null) return null;        
        Date fechaComprobande = null;
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            fechaComprobande = ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getFecha().toGregorianCalendar().getTime();
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            fechaComprobande = ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getFecha();
        }        
        return fechaComprobande;
    }*/
    
    /*public String getComprobanteMoneda() throws Exception
    {
        if(cfdi == null) return null;
        
        String strMoneda = "";
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            strMoneda = ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getSubTotal().toString();
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            strMoneda = ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getSubTotal().toString();
        }
        
        return strMoneda;
    }*/
    
    /*public String getComprobanteTotal() throws Exception
    {
        if(cfdi == null) return null;
        String strTotal = "";
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            strTotal = ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getTotal().toString();
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            strTotal = ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getTotal().toString();
        }
        return strTotal;
    }*/
    
    /*public String getReceptorRFC() throws Exception
    {
        if(cfdi == null) return null;
        String rfc = "";
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            rfc = ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getReceptor().getRfc();
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            rfc = ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getReceptor().getRfc();
        }
        return rfc;
    }*/
    
    /*public String getReceptorNombre() throws Exception
    {
        if(cfdi == null) return null;
        String nombre = "";
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            nombre =  ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getReceptor().getNombre();
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            nombre =  ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getReceptor().getNombre();
        }
        return nombre;
    }*/
    
    public boolean copyRows(Database dbserver, process.Operational operational) throws SQLException
    {
        List<Renglon> list = core.Renglon.select(dbserver,operational, null);
        for(Renglon renglon : list)
        {
            Return retInset = core.Renglon.insert(dbserver, this, list);
            if(retInset.isFail()) return false;
        }
        return true;
    }
    /**
     * @return the trabajo
     */
    public Trabajo getTrabajo() 
    {
        return trabajo;
    }
    
    public Return downTrabajo(Database database) throws SQLException 
    {
        if(database == null)
        {
            return new Return(false,"Conexion nula");
        }
        String sql = "SELECT trabajo FROM " + MYSQL_AVATAR_TABLE + " WHERE trabajo IS NOT NULL AND id = " + id;
        Statement stmt = database.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            trabajo = new Trabajo(rs.getInt(1));
            trabajo.download(database);
            return new Return(true);
        }
        else
        {
            trabajo = null;
            return new Return(false,"FallÃƒÂ³ la descarga del trabajo.");
        }
    }
    
    public Return upTrabajo(Database database, Trabajo trabajo) throws SQLException
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(database == null)
        {
            return new Return(false,"Connection is null.");
        }           
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET trabajo = " + trabajo.getId() + " WHERE id = " + id;
        Statement stmt = database.getConnection().createStatement();
        return new Return(true, stmt.executeUpdate(sql));
    }
    
    /*public Return cancelarNotif(Database dbserver,InternetAddress[] lsTO) throws SQLException, AddressException, Exception
    {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy"); 
        String nombre = "",rfc = "", strTotal = "", strMoneda = "";
        Date fechaComprobande = null;
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            nombre =  ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getReceptor().getNombre();
            rfc = ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getReceptor().getRfc();
            fechaComprobande = ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getFecha().toGregorianCalendar().getTime();
            strTotal = ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getTotal().toString();
            strMoneda = ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getSubTotal().toString();
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            nombre =  ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getReceptor().getNombre();
            rfc = ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getReceptor().getRfc();
            fechaComprobande = ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getFecha();
            strTotal = ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getTotal().toString();
            strMoneda = ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getSubTotal().toString();
        }
        
        String messageText = ""
        + "<T1>Para:  " + nombre + " ( " + rfc + " )</T1><br><br>"
  
        + "Estimado Cliente,<br><br>"

        + "SIIL SA DE CV  ( SII941019BY3 ) Cancelo para Usted la Factura con las siguientes caracterÃƒÂ­sticas: <br><br>" 

        + "<table cellpadding=\"1\" cellspacing=\"1\" > ";
                
              messageText += "<tr>"                
              + "<td width=\"150\" style=\"border-top:1pt solid black;\" ><b>Serie y Folio</b></td>      <td width=\"150\" style=\"border-top:1pt solid black;\" >" + getFullFolio() + " </td>"
              + "</tr>"
              + "<tr>"
              + "<td width=\"150\"><b>Fecha de EmisiÃƒÂ³n</b></td>   <td width=\"150\">" + dt.format(fechaComprobande) + "</td>" 
              + "</tr>"
              + "<tr>"
              + "<td width=\"150\"><b>Monto Total</b></td>       <td width=\"150\"> $" + strTotal + " " + strMoneda + "</td>"                 
              + "</tr>";
              
        messageText += "</table><br>"
        + "Consulte los  datos  adjuntos o llame al telÃƒÂ©fono (664) 623 1803<br><br>"         
        + "Este Comprobante fue enviado automÃƒÂ¡ticamente por el sistema de generaciÃƒÂ³n y recepciÃƒÂ³n de documentos por favor no responda a estÃƒÂ© correo ya que no serÃƒÂ¡ leÃƒÂ­do. <br><br>"; 
        Mail mail = new Mail(-1);
        mail.insert(dbserver,lsTO, null, null,messageText, "factura","CancelaciÃƒÂ³n de CFDI " + getFullFolio() + " SIIL S.A. de C.V.");
        List<Archivo> vaults = new ArrayList<>();
        if(getXMLArchivo() == null)
        {
            return new Return(false,"No hay archivos en bobeda para este folio");
        }
        else
        {
            vaults.add(getXMLArchivo());
        }
        if(getPDFArchivo() != null)
        {
            vaults.add(getPDFArchivo());
        }
        
        Return ret = Attached.add(dbserver,mail,vaults,getEnterprise());
        if(ret.isFail())return ret;
        
        return new Return(true);
    }*/
    
    
    public boolean downSatEstado(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT satEstado FROM " + MYSQL_AVATAR_TABLE + " WHERE satEstado IS NOT NULL AND id = " + id;
        //System.out.println(sql);
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            if(rs.getString(1).equals("V"))
            {
                satEstado = SATestado.VIVA;
            }
            else if(rs.getString(1).equals("C"))
            {
                satEstado = SATestado.CANCELADA;
            }
            else if(rs.getString(1).equals("I"))
            {
                satEstado = SATestado.INCOMPLETA;
            }
            else
            {
                return false;
            }
            return true;
        }
        else
        {
            this.pdfArchivo = null;
            return false;
        }
    }
    
    /**
     * @return the satEstado
     */
    public SATestado getSatEstado() {
        return satEstado;
    }
    
    
    public boolean upSatEstado(Database db,SATestado satEstado ) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET satEstado = " ;
        if(satEstado == SATestado.VIVA)
        {
            sql += "'V'";
        }
        else if(satEstado == SATestado.INCOMPLETA)
        {
            sql += "'I'";
        }
        else if(satEstado == SATestado.CANCELADA)
        {
            sql += "'C'";
        }
        else
        {
            sql += "NULL";
        }
        
        sql += " WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        int ret = stmt.executeUpdate(sql);
        if(ret == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static Return search(Database connection,String serie,String folio) throws SQLException
    {
        String sql = "SELECT id FROM " + process.Operational.MYSQL_AVATAR_TABLE + " WHERE `type`= 'SI' AND flag='A' AND ";
        if(serie != null)
        {
            sql = sql + " serie = '" + serie + "' AND " ;
        }
        sql = sql + " folio = " + folio;
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            Invoice invoice = new Invoice(rs.getInt(1));
            if(rs.next())
            {
                return new Return(false,"folio " + folio + "Tiene mas de una coincidencia");
            }
            else
            {
                return new Return(true,invoice);
            }
        }
        else
        {
            return new Return(false,"No se encontro el folio " + folio);
        }
    }
    
    /*public void leerComprobante(File file) throws Exception
    {
        cfdi = CFDIFactory.load(file);
    }*/
    
    
    /*public Return cancelar(Database dbserver, Trace traceContext) throws Exception
    {
        if(xmlArchivo == null)
        {
            return new Return(false,"No hay XML Asociado.");
        }
        File file = new File(xmlArchivo.getDownloadFileName());
        leerComprobante(file);
        if(getComprobante() == null)
        {
            return new Return(false,"Fallo la lectura del XML.");
        }
        
        TimbreFiscal timbre = new TimbreFiscal();
        String rfc = "";
        
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            rfc = ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getReceptor().getRfc();
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            rfc = ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getReceptor().getRfc();
        }
        
        String uuid = extracUUID();        
        if(timbre.cancelar(rfc, uuid,TimbreFiscal.XML.v33))
        {
            upSatEstado(dbserver, SATestado.CANCELADA);
            if(traceContext != null)
            {
                Value val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable("CancelaciÃƒÂ³n");
                val.setField("CancelaciÃƒÂ³n");
                val.setBrief(uuid);
                val.setLlave("folio=" + id);
                val.insert(dbserver);
            }            
            return new Return(true);
        }
        else
        {
            return new Return(false,"FallÃ³ la cancelacion del Folio '" + uuid + "' y RFC '" + rfc + "':(" + timbre.getResponseCode() + " - " + timbre.getResponseMessage() + ") " + timbre.getURL().getHost() + timbre.getURL().getPath());
        }
    }*/

    /*public String extracUUID() throws IOException, SAXException, XPathExpressionException, Exception 
    {
        if(xmlArchivo == null)
        {
            return null;
        }        
        File file = new File(xmlArchivo.getDownloadFileName());
        leerComprobante(file);
        if(getComprobante() == null)
        {
            return null;
        }
        String uuid = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        XPathFactory xfactory = XPathFactory.newInstance();
        XPath xpath = xfactory.newXPath();
        uuid = xpath.evaluate("/Comprobante/Complemento/TimbreFiscalDigital/@UUID", doc);        
        return uuid;
    }*/
    
    public boolean downXMLArchivo(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT xml FROM " + MYSQL_AVATAR_TABLE + " WHERE xml IS NOT NULL AND id = " + id;
        //System.out.println(sql);
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.xmlArchivo = new Archivo(rs.getInt(1));
            return true;
        }
        else
        {
            this.xmlArchivo = null;
            return false;
        }
    }
        
    public boolean downPDFArchivo(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT pdf FROM " + MYSQL_AVATAR_TABLE + " WHERE pdf IS NOT NULL AND id = " + id;
        //System.out.println(sql);
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.pdfArchivo = new Archivo(rs.getInt(1));
            return true;
        }
        else
        {
            this.pdfArchivo = null;
            return false;
        }
    }
        
    public static void massAssociationCleanDirectory(List<Association<Invoice>> associations) throws IOException
    {
        for(Association association : associations)
        {
            if(association.isActionable())
            {
                File file = association.getFile();
                Files.delete(file.toPath());
            }
        }
    }
        
    private static Return massAssociationUpFile(Database dbserver,FTP ftpServer,File file,Invoice invoice,Office office,Person person,Trace traceContext) throws IOException, SQLException, ParseException
    {
        if(invoice.getID() < 1)                
        {
            return new Return(false,"No se ha asignado un objeto.");
        }
        
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyy"); 
        FileInputStream inStr = new FileInputStream(file);
        Return ret = Archivo.add(dbserver, ftpServer, file.getName(), inStr, invoice.getEnterprise(), "Orden de Servicio Digitalizada : " + dt.format(invoice.getFhFolio() ) + " #" + invoice.getFolio().toString(), Archivo.Type.ORDEN_SERVICIO, Archivo.Origen.INTERNO);
        inStr.close();
        if(ret.isFlag())
        {
            if(invoice.upOrdenes(dbserver, (Archivo) ret.getParam()))
            {
                return new Return(true);
            }
            else
            {
                return new Return(false,"FallÃƒÂ³ la operacion de agregar archivo a la bobeda.");
            }
        }
        else
        {
            return ret;
        }
    }
    
    public static Return massAssociation(Database dbserver,FTP ftpServer,File directory,List<String> logger,List<Association<Invoice>> associations,Trace traceContext,Office office,Person person,com.galaxies.andromeda.util.Progress progress) throws IOException, SQLException, ParseException
    {
        if(!directory.isDirectory())
        {
            return new Return(false, "'"+ directory + "' no es un directorio valido.");
        }
        
        int totalFile = 0;
        int asociatedFile = 0;
        
        int oneStep = 100/associations.size();        
        if(progress != null) progress.setProgress(0, "Comenzando...");
        int progressStep = 0;
        
        
        for(Association<Invoice> association : associations)
        {            
            totalFile++;
            File file = association.getFile();
            if(association.isActionable())
            {
                if(progress != null) progress.setProgress(oneStep, "Presesando " + association.getObject().getFolio());
                if(association.getObject().getID() < 1)                
                {
                    return new Return(false,"No se ha asignado un objeto.");
                }
                //logger.add("Procesando '" + file.getName() + "' ...");
                if(association.getObject().getOrdenesArchivo() != null)//Ã‚Â¿tiene archivo asociado?
                {
                    association.getObject().downFolio(dbserver);
                    logger.add("La orden '" + association.getObject().getFolio() + "' ya estÃƒÂ¡ asociada serÃƒÂ¡ ignorada.");
                    continue;
                }
                
                Return ret = massAssociationUpFile(dbserver,ftpServer,file,association.getObject(),office,person,traceContext);
                if(ret.isFail()) return ret;
                asociatedFile++;
                //logger.add("'" + file.getName() + "' Done.");
            }
        }
        return new Return(true,"Se asociarion " + asociatedFile + " de un total de " + totalFile);
    }
    
    public static boolean massAssociationReadDirectory(Database dbserver, File directory,List<Association<Invoice>> associations,List<String> logger,Office office) throws SQLException
    {
        if(!directory.isDirectory()) return false;        
        File[] filesList = directory.listFiles();
        for (File file : filesList) 
        {
            if (file.isFile()) 
            {
                Return retFind = Invoice.exist(dbserver, office, FilenameUtils.removeExtension(file.getName()), Type.SalesInvoice);
                Association<Invoice> association = null;
                if(retFind.isFlag())
                {
                    Invoice invoice = new Invoice((int)retFind.getParam());
                    invoice.download(dbserver);
                    invoice.downCompany(dbserver);
                    invoice.downFolio(dbserver);
                    invoice.downSerie(dbserver);
                    association = new Association(file,true,invoice);                    
                }
                else
                {
                    logger.add("El archivo '" + file.getName() + "' serÃƒÂ¡ ignorado devido a que no nay folio para asociar.");
                    association = new Association(file,false,null);
                }
                associations.add(association);
            }
            else if(file.isDirectory())
            {
                return massAssociationReadDirectory(dbserver,file,associations,logger,office);
            }
            else
            {
                return false;
            }
        }
        
        return true;
    }
    
    /*public static Return asociarArchivo(Database dbServer,FTP ftpServer, Office office, Invoice invoice, FileInputStream fis) throws SQLException, IOException
    {   
        if(!ftpServer.isExist(Archivo.Type.ORDEN_SERVICIO,Archivo.Origen.INTERNO, invoice.getEnterprise().getNumber()))
        {
            if(!ftpServer.addSubdirectory(Archivo.Type.ORDEN_SERVICIO,Archivo.Origen.INTERNO, invoice.getEnterprise().getNumber()))
            {
                new Return(false,"FallÃƒÂ³ la creaciÃƒÂ³n de subdirectroio de cliente");
            }
        }
        Return ret = Archivo.add(dbServer, ftpServer, "orden.pdf" , fis, invoice.getEnterprise(), "Orden de Servicio de factura " + String.valueOf(invoice.getFolio()), Archivo.Type.ORDEN_SERVICIO, Archivo.Origen.INTERNO);
        if(ret.isFail()) return ret;
        Archivo archivo = (Archivo) ret.getParam();                
        if(!invoice.upOrdenes(dbServer, archivo))
        {
            return new Return(false,"FallÃƒÂ³ la asignaciÃƒÂ³n del archivo de ordenes");
        }
        return new Return(true);
    }*/
    
    public boolean downOrdenesArchivo(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT ordenesArchivo FROM " + MYSQL_AVATAR_TABLE + " WHERE ordenesArchivo IS NOT NULL AND id = " + id;
        //System.out.println(sql);
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.ordenesArchivo = new Archivo(rs.getInt(1));
            return true;
        }
        else
        {
            this.ordenesArchivo = null;
            return false;
        }
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return selectRandom(Connection connection) throws SQLException 
    {
        //clean();
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return new Return(true);
        }
        else
        {
            this.id = -1;
            return new Return(false);
        }
    }
    
    public boolean upOrdenes(Database db, Archivo archivo) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET ordenesArchivo = " + archivo.getID() + " WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        int ret = stmt.executeUpdate(sql);
        if(ret == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
     
    public static boolean existRemision(Database dbServer,Remision remision) throws SQLException
    {
        String sql = "SELECT id FROM SA_Factura WHERE sa = " + remision.getID();
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
    
    private boolean add(Database dbServer,Invoice invoice,Remision remision) throws SQLException
    {
        String sql = "INSERT INTO SA_Factura (invoice,sa) VALUES (" + invoice.getID() + "," + remision.getID() + ")";
        Statement stmt = dbServer.getConnection().createStatement();
        System.out.println(sql);
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return false; 
        }
        else
        {
            return true;
        }     
    }
    
    /*public static Return asociarArchivo(Database dbServer,FTP ftpServer, Office office, Invoice invoice, List<Remision> remisoines, FileInputStream fis) throws SQLException, IOException
    {   
        if(!ftpServer.isExist(Archivo.Type.ORDEN_SERVICIO,Archivo.Origen.INTERNO, invoice.getEnterprise().getNumber()))
        {
            if(!ftpServer.addSubdirectory(Archivo.Type.ORDEN_SERVICIO,Archivo.Origen.INTERNO, invoice.getEnterprise().getNumber()))
            {
                new Return(false,"FallÃƒÂ³ la creaciÃƒÂ³n de subdirectroio de cliente");
            }
        }
        for(Remision remision : remisoines)
        {
            if(!invoice.add(dbServer, invoice, remision))  
            {
                return new Return(false,"FallÃƒÂ³ la inserciÃƒÂ³n de SA = " + remision.getFolio());
            }
        }
        Return ret = Archivo.add(dbServer, ftpServer, "orden.pdf" , fis, invoice.getEnterprise(), "Orden de Servicio de factura " + String.valueOf(invoice.getFolio()), Archivo.Type.ORDEN_SERVICIO, Archivo.Origen.INTERNO);
        if(ret.isFail()) return ret;
        Archivo archivo = (Archivo) ret.getParam();                
        if(!invoice.upOrdenes(dbServer, archivo))
        {
            return new Return(false,"FallÃƒÂ³ la asignaciÃƒÂ³n del archivo de ordenes");
        }
        return new Return(true);
    }*/
    
    public boolean upPDF(Database db, Archivo archivo) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }        
        if(db == null)
        {
            return false;
        }        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET pdf = '" + archivo.getID() + "' WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        int ret = stmt.executeUpdate(sql);
        if(ret == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean upXML(Database db, Archivo archivo) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET xml = '" + archivo.getID() + "' WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        int ret = stmt.executeUpdate(sql);
        if(ret == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
        
    public boolean upEmail(Database db, Mail mail) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET email = '" + mail.getID() + "' WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        int ret = stmt.executeUpdate(sql);
        if(ret == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    @Override
    public Return insert(Database db,Office office,State state,Person operator,java.util.Date date,Enterprise company,int folio,String serie,String type) throws SQLException
    {
        if(getID() > 0)
        {
            throw new InvalidParameterException("Allacated tine un ID invalido."); 
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(company == null)
        {
            throw new InvalidParameterException("El cliente no puede ser nulo."); 
        }
        else if(company.getNumber() == null)
        {
            throw new InvalidParameterException("El numero de cliente es invalido."); 
        }
        
        Return insertRet = super.insert(db,office, state, operator, date, company, folio, serie, type);
        if(insertRet.isFail()) return insertRet;        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id) VALUES(" + super.getID() + ")";
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return new Return(false, "No unico registro insertado."); 
        }
        else
        {
            return insertRet;
        }     
    }
    
    public Return fromCN(Database db,Office office,String folio,Person operator) throws IOException, ParseException, SQLException
    {
        Return retFiend = exist(db, office, folio, Type.SalesInvoice);
        if(retFiend.isFail())
        {
            if(!CN60.isWorking())
            {
                return new Return(false,"CN60 no esta disponilbe para importar el folio " + folio);
            }        
            SIIL.CN.Tables.FACTURA tbFactura = null;
            switch (office.getCode()) 
            {
                case "bc.tj":
                    tbFactura = new SIIL.CN.Tables.FACTURA(Sucursal.BC_Tijuana);
                    break;
                case "bc.mx":
                    tbFactura = new SIIL.CN.Tables.FACTURA(Sucursal.BC_Mexicali);
                    break;
                case "bc.ens":
                    tbFactura = new SIIL.CN.Tables.FACTURA(Sucursal.BC_Ensenada);
                    break;
                default:
                    return null;
            }
            String filedFolio = "1" + StringUtils.leftPad(String.valueOf(folio), 8, '0');
            SIIL.CN.Records.FACTURA recFactura = tbFactura.readWhere(folio);
            if(recFactura != null)
            {//si existe el folio
                //Invoice invoice = new Invoice(-1);
                String numberCleint = recFactura.getClientNumber();
                Enterprise enterprise = Enterprise.find(db,numberCleint);
                if(enterprise != null)
                {
                    enterprise.download(db);
                }
                else
                {
                    tbFactura.close();
                    return null;
                }
                Return ret = super.insert(db, office, new State(1), operator, null,enterprise, Integer.parseInt(folio), office.getSerieOffice(Office.Platform.CN60), "SI");
                super.upFlag(db.getConnection(), 'A');
                if(ret.isFlag())
                {
                    //System.out.println("Se importo Remision desde CN60.");
                    tbFactura.close();
                }
                else
                {
                    tbFactura.close();
                    return ret;
                }

                String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id) VALUES(" + ((Integer) ret.getParam()) + ")";
                Statement stmt = db.getConnection().createStatement();
                int affected = stmt.executeUpdate(sql);
                if(affected != 1)
                {
                    //clean();
                    return new Return(false, "Deamasiados registros insertados"); 
                }
                return ret;            
            }
            else
            {
                tbFactura.close();
                return null;
            }
        }
        else
        {
            id = (int) retFiend.getParam();
            return retFiend;
        }
    }
        
    public double calcularSubTotal(List<core.Renglon> renglones)
    {
        double total = 0.0;
        
        for(core.Renglon renglon : renglones)
        {
            total += renglon.getCostSales() * renglon.getCantidad();
        }
        return total;
    }
    
    public double calcularIVA(List<core.Renglon> renglones, double iva)
    {
        double ret = 0.0;        
        ret = calcularSubTotal(renglones);        
        ret *= iva;        
        return ret;        
    }
    
    public double calcularTotal(double subtotal,double iva)
    {
        return subtotal + iva;
    }
    
    /*public CFD createCFDI32(Database dbserver, List<core.Renglon> renglones,File saveIn) throws FileNotFoundException, Exception 
    {
        mx.bigdata.sat.cfdi.v32.schema.ObjectFactory of = new mx.bigdata.sat.cfdi.v32.schema.ObjectFactory();
        mx.bigdata.sat.cfdi.v32.schema.Comprobante comprobante = of.createComprobante();
        comprobante.setVersion("3.2");
        Date date = dbserver.getDateToday();
        comprobante.setFecha(date);
        comprobante.setFormaDePago("PAGO EN UNA SOLA EXHIBICION");
        double subT = calcularSubTotal(renglones);
        double iva = calcularIVA(renglones, 0.16);
        double T = calcularTotal(subT, iva);
        comprobante.setMoneda("MXN");      
        comprobante.setSubTotal(new BigDecimal(String.valueOf(subT)));
        comprobante.setTotal(new BigDecimal(String.valueOf(T)));
        comprobante.setTipoDeComprobante("ingreso");
        comprobante.setMetodoDePago("efectivo");
        comprobante.setLugarExpedicion("Mexico");
        comprobante.setEmisor(createEmisor32(of));
        comprobante.setReceptor(createReceptor32(of));
        comprobante.setConceptos(createConceptos32(of,renglones));
        comprobante.setImpuestos(createImpuestos32(of,iva));
        if(downFolio(dbserver))
        {
            comprobante.setFolio(getFolio());            
        }
        if(downSerie(dbserver))
        {
            comprobante.setSerie(getSerie());            
        }
        
        cfdi = new mx.bigdata.sat.cfdi.CFDv32(comprobante);
        key = privateKey();
        cert = certificado();
        FileOutputStream out = new FileOutputStream(saveIn);        
        cfdi.guardar(out);
        out.flush();
        out.close();
        upSatEstado(dbserver, SATestado.INCOMPLETA);
        cfdi.sellar(key, cert);
        cfdi.validar();
        cfdi.verificar();
        out = new FileOutputStream(saveIn);        
        cfdi.guardar(out);
        out.flush();
        out.close();
        upSatEstado(dbserver, SATestado.VIVA);
        return cfdi;
    }*/
	
	
    /*private static mx.bigdata.sat.cfdi.v32.schema.Comprobante.Emisor createEmisor32(mx.bigdata.sat.cfdi.v32.schema.ObjectFactory of) 
    {
        mx.bigdata.sat.cfdi.v32.schema.Comprobante.Emisor emisor = of.createComprobanteEmisor();
        emisor.setNombre("SIIL SA DE CV");
        emisor.setRfc("LAN7008173R5");
        mx.bigdata.sat.cfdi.v32.schema.TUbicacionFiscal uf = of.createTUbicacionFiscal();
        uf.setCalle("Calle Uno Poniente");
        uf.setCodigoPostal("22444");
        uf.setColonia("Ciudad Industrial"); 
        uf.setEstado("Baja California"); 
        uf.setMunicipio("Tijuana"); 
        uf.setNoExterior("19268"); 
        uf.setPais("Mexico");
        emisor.setDomicilioFiscal(uf);
        mx.bigdata.sat.cfdi.v32.schema.TUbicacion u = of.createTUbicacion();
        u.setCalle("Calle Uno Poniente");
        u.setCodigoPostal("22444");
        u.setColonia("Ciudad Industrial"); 
        u.setEstado("Baja California"); 
        u.setMunicipio("Tijuana"); 
        u.setNoExterior("19268"); 
        u.setPais("Mexico"); 
        emisor.setExpedidoEn(u); 
        mx.bigdata.sat.cfdi.v32.schema.Comprobante.Emisor.RegimenFiscal rf = of.createComprobanteEmisorRegimenFiscal();
        rf.setRegimen("Persona Moral Regimen General de Ley");
        emisor.getRegimenFiscal().add(rf);
        return emisor;
    }*/

    /*private static mx.bigdata.sat.cfdi.v32.schema.Comprobante.Receptor createReceptor32(mx.bigdata.sat.cfdi.v32.schema.ObjectFactory of) 
    {
        mx.bigdata.sat.cfdi.v32.schema.Comprobante.Receptor receptor = of.createComprobanteReceptor();
        receptor.setNombre("JUAN PEREZ PEREZ");
        receptor.setRfc("AAA010101AAA");
        mx.bigdata.sat.cfdi.v32.schema.TUbicacion uf = of.createTUbicacion();
        uf.setCalle("AV UNIVERSIDAD");
        uf.setCodigoPostal("04360");
        uf.setColonia("COPILCO UNIVERSIDAD"); 
        uf.setEstado("DISTRITO FEDERAL"); 
        uf.setMunicipio("COYOACAN"); 
        uf.setNoExterior("16 EDF 3"); 
        uf.setNoInterior("DPTO 101"); 
        uf.setPais("Mexico"); 
        receptor.setDomicilio(uf);
        return receptor;
    }*/

    /*private static mx.bigdata.sat.cfdi.v32.schema.Comprobante.Conceptos createConceptos32(mx.bigdata.sat.cfdi.v32.schema.ObjectFactory of,List<core.Renglon> renglones) throws DatatypeConfigurationException 
    {
	mx.bigdata.sat.cfdi.v32.schema.Comprobante.Conceptos cps = of.createComprobanteConceptos();
	List<mx.bigdata.sat.cfdi.v32.schema.Comprobante.Conceptos.Concepto> list = cps.getConcepto();        
	for(core.Renglon renglon : renglones)
	{
            if(renglon.isAduanaActive())
            {
		mx.bigdata.sat.cfdi.v32.schema.Comprobante.Conceptos.Concepto c1 = of.createComprobanteConceptosConcepto();              
		if(renglon.getUnidad() != null)
		{
                    c1.setUnidad(renglon.getUnidad());
		}
		else
		{
                    c1.setUnidad("Unidad");
		}
                
		c1.setImporte(new BigDecimal(String.valueOf(renglon.getTotal())));
		c1.setCantidad(new BigDecimal(String.valueOf(renglon.getCantidad())));
		c1.setDescripcion(renglon.getDescripcion());
		c1.setValorUnitario(new BigDecimal(String.valueOf(renglon.getImporte())));   
		mx.bigdata.sat.cfdi.v32.schema.TInformacionAduanera ia = new mx.bigdata.sat.cfdi.v32.schema.TInformacionAduanera();
		ia.setNumero(renglon.getPedimentoNumero());
		ia.setAduana(renglon.getPedimentoAduana());
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(renglon.getPedimentoFecha());
		DatatypeFactory dtf = null;
		dtf = DatatypeFactory.newInstance();  
		XMLGregorianCalendar fechaPed = dtf.newXMLGregorianCalendar(gc);
		fechaPed.setHour(DatatypeConstants.FIELD_UNDEFINED);
		fechaPed.setMinute(DatatypeConstants.FIELD_UNDEFINED); 
		fechaPed.setSecond(DatatypeConstants.FIELD_UNDEFINED);
		fechaPed.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
		fechaPed.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		ia.setFecha(fechaPed);
		c1.getInformacionAduanera().add(ia);
		list.add(c1);
            }
            else
            {
		mx.bigdata.sat.cfdi.v32.schema.Comprobante.Conceptos.Concepto c2 = of.createComprobanteConceptosConcepto();
		if(renglon.getUnidad() != null) 
		{
                    c2.setUnidad(renglon.getUnidad());
		}
		else
		{
                    c2.setUnidad("Pieza");
		}
		c2.setImporte(new BigDecimal(String.valueOf(renglon.getTotal())));
		c2.setCantidad(new BigDecimal(String.valueOf(renglon.getCantidad())));
		c2.setDescripcion(renglon.getDescripcion());
		c2.setValorUnitario(new BigDecimal(String.valueOf(renglon.getCostSales())));
		list.add(c2);
            }
	}
	return cps;
    }*/

    /*private static mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos createImpuestos32(mx.bigdata.sat.cfdi.v32.schema.ObjectFactory of,double iva) 
    {
        mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos imps = of.createComprobanteImpuestos();
        mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos.Traslados trs = of.createComprobanteImpuestosTraslados();
        List<mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos.Traslados.Traslado> list = trs.getTraslado(); 
        mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos.Traslados.Traslado t1 = of.createComprobanteImpuestosTrasladosTraslado();        
        t1.setImporte(new BigDecimal(String.valueOf(iva)));
        t1.setImpuesto("IVA");
        t1.setTasa(new BigDecimal("16.00"));
        list.add(t1);        
        imps.setTraslados(trs);
        return imps;
    }*/
    
    /*public CFD createCFDI33(Database dbserver, List<core.Renglon> renglones, File saveIn, HashMap<String,Object> params) throws FileNotFoundException, Exception 
    {
        mx.bigdata.sat.cfdi.v33.schema.ObjectFactory of = new mx.bigdata.sat.cfdi.v33.schema.ObjectFactory();
        mx.bigdata.sat.cfdi.v33.schema.Comprobante comprobante = of.createComprobante();
        comprobante.setVersion("3.3");
        if(params.containsKey("Serie"))
        {
            comprobante.setSerie((String)params.get("Serie"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'Serie'");
        }
        if(params.containsKey("Folio"))
        {
            comprobante.setFolio((String)params.get("Folio"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'Folio'");
        }
        Timestamp date = dbserver.getTimestamp();
        //comprobante.setFecha(DatatypeFactory.newInstance().newXMLGregorianCalendar(date.getYear() + 1900, date.getMonth(), date.getDay(), 0, 0, 0, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED));
        try 
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //formato de fecha CFDI
            comprobante.setFecha( DatatypeFactory.newInstance().newXMLGregorianCalendar(df.format(date)));
        } 
        catch (DatatypeConfigurationException ex) 
        {
            //manejar excepción
        }
        if(params.containsKey("FormaPago"))
        {
            comprobante.setFormaPago((String)params.get("FormaPago"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'FormaPago'");
        }
        double subT = calcularSubTotal(renglones);
        double iva = calcularIVA(renglones, 0.16);
        double T = calcularTotal(subT, iva);
        comprobante.setNoCertificado(certificado().getSigAlgOID());
        if(params.containsKey("CondicionesDePago"))
        {
            comprobante.setCondicionesDePago((String)params.get("CondicionesDePago"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'CondicionesDePago'");
        }
        comprobante.setMoneda(mx.bigdata.sat.cfdi.v33.schema.CMoneda.MXN);
        if(params.containsKey("TipoCambio"))
        {
            comprobante.setTipoCambio(new BigDecimal((String)params.get("TipoCambio")));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'TipoCambio'");
        }
        comprobante.setSubTotal(new BigDecimal(String.valueOf(subT)));
        comprobante.setTotal(new BigDecimal(String.valueOf(T)));
        comprobante.setTipoDeComprobante(mx.bigdata.sat.cfdi.v33.schema.CTipoDeComprobante.I);
        comprobante.setMetodoPago(mx.bigdata.sat.cfdi.v33.schema.CMetodoPago.PUE);
        if(params.containsKey("LugarExpedicion"))
        {
            comprobante.setLugarExpedicion((String)params.get("LugarExpedicion"));//codigo postal///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'LugarExpedicion'");
        }
        if(params.containsKey("Confirmacion"))
        {
            comprobante.setConfirmacion((String)params.get("Confirmacion"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<        
        }
        else
        {
            throw new Exception("No se encontro el parametro 'Confirmacion'");
        }
        
        //comprobante.setCfdiRelacionados(createCfdiRelacionados33(of));
        comprobante.setEmisor(createEmisor33(of,params));
        comprobante.setReceptor(createReceptor33(of,params));
        comprobante.setConceptos(createConceptos33(of,renglones,params));
        comprobante.setImpuestos(createImpuestos33(of,iva,params));
        
        if(downFolio(dbserver))
        {
            comprobante.setFolio(getFolio());            
        }
        if(downSerie(dbserver))
        {
            comprobante.setSerie(getSerie());            
        }
        
        upSatEstado(dbserver, SATestado.INCOMPLETA);        
        cfdi = new mx.bigdata.sat.cfdi.CFDv33(comprobante);
        key = privateKey();
        cert = certificado();
        cfdi.sellar(key, cert);
        cfdi.validar();
        cfdi.verificar(); 
        
        FileOutputStream out = new FileOutputStream(saveIn);       
        cfdi.guardar(out);
        out.flush();
        out.close(); 
        upSatEstado(dbserver, SATestado.VIVA);
        
        return cfdi;
    }*/
    
    /*public static PrivateKey privateKey() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException 
    {
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server(); 
        serverConfig.loadFile(new java.io.File(".").getCanonicalPath());   
        Database dbserver = null;
        return KeyLoaderFactory.createInstance(KeyLoaderEnumeration.PRIVATE_KEY_LOADER,new FileInputStream(serverConfig.getPrefiFileDiversa() + ".key"),serverConfig.getPasswordDiversa()).getKey();
    }*/

    /*public static X509Certificate certificado()  throws FileNotFoundException, IOException, ParserConfigurationException, SAXException 
    {
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server(); 
        serverConfig.loadFile(new java.io.File(".").getCanonicalPath());  
        return KeyLoaderFactory.createInstance(KeyLoaderEnumeration.PUBLIC_KEY_LOADER,new FileInputStream(serverConfig.getPrefiFileDiversa() + ".cer")).getKey();
    }*/
      
    /*private static mx.bigdata.sat.cfdi.v33.schema.Comprobante.Emisor createEmisor33(mx.bigdata.sat.cfdi.v33.schema.ObjectFactory of, HashMap<String,Object> params) throws Exception 
    {
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Emisor emisor = of.createComprobanteEmisor();
        if(params.containsKey("Emisor.Nombre"))
        {
            emisor.setNombre((String)params.get("Emisor.Nombre"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'Emisor.Nombre'");
        }
        if(params.containsKey("Emisor.RFC"))
        {
            emisor.setRfc((String)params.get("Emisor.RFC"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'Emisor.RFC'");
        }
        if(params.containsKey("Emisor.RegimenFiscal"))
        {
            emisor.setRegimenFiscal((String)params.get("Emisor.RegimenFiscal"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'Emisor.RegimenFiscal'");
        }
        return emisor;
    }*/
    
    /*private static mx.bigdata.sat.cfdi.v33.schema.Comprobante.CfdiRelacionados createCfdiRelacionados33(mx.bigdata.sat.cfdi.v33.schema.ObjectFactory of, HashMap<String,String> params) throws Exception 
    {
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.CfdiRelacionados cfdir = of.createComprobanteCfdiRelacionados();
        if(params.containsKey("Relacionados.TipoRelacion"))
        {
            cfdir.setTipoRelacion(params.get("Relacionados.TipoRelacion"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'Relacionados.TipoRelacion'");
        }
        cfdir.getCfdiRelacionado().add(createCfdiRelacionado33(of,params));
        return cfdir;
    }*/
    
    /*private static mx.bigdata.sat.cfdi.v33.schema.Comprobante.CfdiRelacionados.CfdiRelacionado createCfdiRelacionado33(mx.bigdata.sat.cfdi.v33.schema.ObjectFactory of, HashMap<String,String> params) throws Exception 
    {
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.CfdiRelacionados.CfdiRelacionado cfdir = of.createComprobanteCfdiRelacionadosCfdiRelacionado();
        if(params.containsKey("Relacionado.UUID"))
        {
            cfdir.setUUID(params.get("Relacionado.UUID"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }   
        else
        {
            throw new Exception("No se encontro el parametro 'Relacionado.UUID'");
        }
        return cfdir;
    }*/
    
    /*private static mx.bigdata.sat.cfdi.v33.schema.Comprobante.Receptor createReceptor33(mx.bigdata.sat.cfdi.v33.schema.ObjectFactory of, HashMap<String,Object> params) throws Exception 
    {
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Receptor receptor = of.createComprobanteReceptor();
        if(params.containsKey("Receptor.RFC"))
        {
            receptor.setRfc((String)params.get("Receptor.RFC"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'Receptor.RFC'");
        }
        if(params.containsKey("Receptor.Nombre"))
        {
            receptor.setNombre((String)params.get("Receptor.Nombre"));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'Receptor.Nombre'");
        }
        receptor.setResidenciaFiscal(mx.bigdata.sat.cfdi.v33.schema.CPais.MEX);
        if(params.containsKey("Receptor.NumRegIdTrib"))
        {
            receptor.setNumRegIdTrib("Receptor.NumRegIdTrib");///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        }
        else
        {
            throw new Exception("No se encontro el parametro 'Receptor.NumRegIdTrib'");
        }
        receptor.setUsoCFDI(mx.bigdata.sat.cfdi.v33.schema.CUsoCFDI.G_01);
        return receptor;
    }*/

    /*private static mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos createConceptos33(mx.bigdata.sat.cfdi.v33.schema.ObjectFactory of,List<core.Renglon> renglones, HashMap<String,Object> params) throws DatatypeConfigurationException, Exception 
    {
      mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos cps = of.createComprobanteConceptos();
      List<mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto> list = cps.getConcepto();        
      for(core.Renglon renglon : renglones)
      {
          if(renglon.isAduanaActive())
          {
              mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto c1 = of.createComprobanteConceptosConcepto();  
              c1.setClaveProdServ("10101501");///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              c1.setNoIdentificacion("GEN01");///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              c1.setClaveUnidad("EA");///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              if(renglon.getUnidad() != null)
              {
                  c1.setUnidad(renglon.getUnidad());///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              }
              else
              {
                  c1.setUnidad("Unidad");
              }
              c1.setImporte(new BigDecimal(String.valueOf(renglon.getTotal())));
              c1.setCantidad(new BigDecimal(String.valueOf(renglon.getCantidad())));
              c1.setDescripcion(renglon.getDescripcion());
              c1.setValorUnitario(new BigDecimal(String.valueOf(renglon.getImporte())));  
              mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.InformacionAduanera ia = new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.InformacionAduanera();
              ia.setNumeroPedimento(renglon.getPedimentoNumero());///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              c1.getInformacionAduanera().add(ia);
              list.add(c1);
              throw new Exception("La informacionde aduana no esta soportada.");
          }
          else
          {
              mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto c2 = of.createComprobanteConceptosConcepto();
              c2.setClaveProdServ("10101501");///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              c2.setNoIdentificacion("GEN01");///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              c2.setClaveUnidad("EA");///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              if(renglon.getUnidad() != null) 
              {
                  c2.setUnidad(renglon.getUnidad());///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              }
              else
              {
                  c2.setUnidad("Pieza");
              }
              c2.setImporte(new BigDecimal(String.valueOf(renglon.getTotal())));
              c2.setCantidad(new BigDecimal(String.valueOf(renglon.getCantidad())));
              c2.setDescripcion(renglon.getDescripcion());
              c2.setValorUnitario(new BigDecimal(String.valueOf(renglon.getCostSales())));
              mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos impuesto = new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos();
              mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos.Traslados traslados = new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos.Traslados();
              mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado traslado = new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado();
              traslado.setBase(new BigDecimal(String.valueOf(renglon.getTotal())));
              if(params.containsKey("TipoFactor"))
              {
                traslado.setTipoFactor((CTipoFactor) params.get("TipoFactor"));//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              }
              else
              {
                  throw new Exception("No se encontro el parametro 'TipoFactor'");
              }
              if(params.containsKey("TasaOCuota"))
              {
                traslado.setTasaOCuota(new BigDecimal((String)params.get("TasaOCuota")));//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              }
              else
              {
                  throw new Exception("No se encontro el parametro 'TasaOCuota'");
              }
              if(params.containsKey("Impuesto"))
              {
                traslado.setImpuesto((String)params.get("Impuesto"));//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
              }
              else
              {
                  throw new Exception("No se encontro el parametro 'Impuesto'");
              }
              traslado.setImporte(new BigDecimal(renglon.getTotal() * 0.16));
              traslados.getTraslado().add(traslado);            
              impuesto.setTraslados(traslados);
              c2.setImpuestos(impuesto);
              list.add(c2);
          }
      }
      return cps;
    }*/

    /*private static mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos createImpuestos33(mx.bigdata.sat.cfdi.v33.schema.ObjectFactory of,double iva, HashMap<String,Object> params) 
    {
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos imps = of.createComprobanteImpuestos();
        imps.setTotalImpuestosRetenidos(BigDecimal.ZERO);
        imps.setTotalImpuestosTrasladados(new BigDecimal(iva));
        imps.setTraslados(createTraslados33(of,iva,params));
        return imps;
    }*/
    
    
    /*private static mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados createTraslados33(mx.bigdata.sat.cfdi.v33.schema.ObjectFactory of,double iva, HashMap<String,Object> params) 
    {
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados its = of.createComprobanteImpuestosTraslados();
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados.Traslado it = of.createComprobanteImpuestosTrasladosTraslado();
        it.setImpuesto("002");///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        it.setTipoFactor(mx.bigdata.sat.cfdi.v33.schema.CTipoFactor.TASA);
        it.setTasaOCuota(new BigDecimal(0.160000));///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        it.setImporte(new BigDecimal(iva));
        its.getTraslado().add(it);
        return its;
    }*/
    
    /*public Invoice(File directory,String folioArch) throws Exception
    {
        super(-1);
        loadFiles(directory, folioArch);        
    }*/

    /*public void loadFiles(File directory, String folioArch) throws Exception 
    {
        loadXML(directory, folioArch);        
        loadPDF(directory, folioArch);
    }*/

    public void loadPDF(File directory, String folioArch) throws Exception 
    {
        String strPDF = directory + "\\" + folioArch + ".pdf";
        File pdfF = new File(strPDF);
        if(!pdfF.exists()) throw new FailResultOperationException("No se encontrÃƒÂ³n el archivo '" + strPDF + "'");        
        this.pdf = new FileInputStream(pdfF);
    }

    /*public void loadXML(File directory, String folioArch) throws Exception 
    {
        String strXML = directory + "\\" + folioArch + ".xml";
        File xmlF = new File(strXML);
        if(!xmlF.exists()) throw new FailResultOperationException("No se encontrÃƒÂ³n el archivo '" + strXML + "'");
        this.xml = new FileInputStream(xmlF);
        leerComprobante(xmlF);
    }*/
    
    public Invoice()
    {        
        super(-1);
        //clean();        
    }
    
    public Invoice(int id)
    {        
        super(id);
    }
    
    /*public static List<Invoice> loadFromCN(File directory,String serie,int from,int to) throws FileNotFoundException, Exception
    {
        ArrayList<Invoice> invoices = new ArrayList<>();
        for(int i = from; i <= to; i++)
        {
            String filedFolio = serie.toUpperCase() + StringUtils.leftPad(String.valueOf(i), 8, '0');
            Invoice factura = new Invoice();
            factura.loadXML(new File(directory.getAbsoluteFile() + "\\FEXML"),filedFolio);
            factura.loadPDF(new File(directory.getAbsoluteFile() + "\\PDF"),filedFolio);
            invoices.add(factura);
        }
        return invoices;
    }*/
  
    /*@Override
    public String getFolio()
    {
        if(super.getFolio() != null && id > 0)
        {
            return super.getFolio();
        }
          
        try
        {
            if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
            {
                return ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getFolio();
            }
            else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
            {
                return ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getFolio();
            }
        }
        catch(Exception exception)
        {
            
        }
        
        return null;        
    }*/
    
    /*public String getComprobanteFullFolio() throws Exception
    {
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            return ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getFolio();
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            return ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getFolio();
        }
        return "";
    }*/
    
    /*public static Invoice loadFromCN(File directory,String serie,int folio) throws FileNotFoundException, Exception
    {
        String filedFolio = serie.toUpperCase() + StringUtils.leftPad(String.valueOf(folio), 8, '0');
        Invoice factura = new Invoice();
        factura.loadXML(new File(directory.getAbsoluteFile()+"\\FEXML"),filedFolio);
        factura.loadPDF(new File(directory.getAbsoluteFile()+"\\PDF"),filedFolio);        
        return factura;
    }*/

    /*public String getFilledFolio() throws Exception 
    {
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            return ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getSerie() + ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getFolio();
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            return ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getSerie() + ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getFolio();
        }
        return "";
    }*/
    
    /**
     * @return the serie
     */
    /*public String getSerie()
    {
        try
        {
            if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
            {
                return ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getSerie();
            }
            else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
            {
                return ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getSerie();
            }
        }
        catch(Exception ex)
        {
            
        }
        return null;
    }*/

    /**
     * @return the folio
     */
    /*public String getFolio() 
    {
        return cfdi.getFolio();
    }*/

    /**
     * @return the XML
     */
    /*public Object getComprobante() 
    {
        return cfdi;
    }*/

    /**
     * @return the PDF
     */
    public FileInputStream getPDF() 
    {
        return pdf;
    }

    /**
     * @return the xml
     */
    
    public FileInputStream getXML() {
        return xml;
    }
    
    /*public String getRFC() throws Exception
    {
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            return ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getReceptor().getRfc();
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            return ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getReceptor().getRfc();
        }
        return "";
    }*/
    
    /*public Enterprise getEnterprise(Database dbServer) throws SQLException, Exception
    {
        Enterprise enterprise = null;
        if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv33)
        {
            enterprise = Enterprise.findByRFC(dbServer, ((mx.bigdata.sat.cfdi.v33.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv33)cfdi).getComprobante().getComprobante()).getReceptor().getRfc());
            enterprise.download(dbServer);
        }
        else if(cfdi instanceof mx.bigdata.sat.cfdi.CFDv32)
        {
            enterprise = Enterprise.findByRFC(dbServer, ((mx.bigdata.sat.cfdi.v32.schema.Comprobante)((mx.bigdata.sat.cfdi.CFDv32)cfdi).getComprobante().getComprobante()).getReceptor().getRfc());
            enterprise.download(dbServer);
        }
        
        return enterprise;//new Return(false, "El RFC '" + f.getComprobante().getReceptor().getRfc() + "' no esta asociado a ningun cliente");
    }*/

    /*private void clean() 
    {
        if(xml != null) try 
        {
            xml.close();
        } catch (IOException ex) {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        xml = null;
        if(pdf != null) try
        {
            pdf.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        pdf = null;
        cfdi = null;
    }*/

    /**
     * @return the ordenesArchivo
     */
    public Archivo getOrdenesArchivo() {
        return ordenesArchivo;
    }

    /**
     * @return the pdfArchivo
     */
    public Archivo getPDFArchivo() {
        return pdfArchivo;
    }

    /**
     * @return the xmlArchivo
     */
    public Archivo getXMLArchivo() {
        return xmlArchivo;
    }
}
