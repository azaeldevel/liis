
package purchases.contrarecibo;

import SIIL.Server.Database;
import core.bobeda.FTP;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
//import mx.bigdata.sat.cfdi.v32.schema.Comprobante;
import org.apache.commons.io.FilenameUtils;
import sales.Invoice;

/**
 * @version 0.1
 * @author Azael Reyes
 */
public class Contrarecibo 
{
    /*public static HashMap<String,Invoice> loadDirectory(Database dbServer,FTP ftpServer,File directory,HashMap<String,Invoice> ls) throws Exception
    {
        File[] lsFiles = directory.listFiles();
        for (final File fileEntry : lsFiles) 
        {            
            if(fileEntry.isDirectory())
            {
                loadDirectory(dbServer, ftpServer, fileEntry,ls);
            }
            else if(fileEntry.isFile())
            {
                String folioName = FilenameUtils.removeExtension(fileEntry.getName());
                if(!ls.containsKey(folioName))
                {
                    try
                    {
                        Invoice invoice = new Invoice(directory,folioName);
                        ls.put(folioName,invoice);
                    }
                    catch(Exception ex)
                    {
                        ;
                    }
                }
            }
        }
        
        return ls;
    }*/
    
    private FileInputStream xml;
    //private Comprobante cfdi;
    private FileInputStream pdf;
    /*
    public static List<Invoice> loadFromCN(String directory,String serie,int from,int to) throws FileNotFoundException, Exception
    {
        ArrayList<Invoice> invoices = new ArrayList<Invoice>();
        for(int i = from; i <= to; i++)
        {
            String filedFolio = StringUtils.leftPad(String.valueOf(i), 8, '0');
            File xmlF = new File(directory + "\\FEXML\\" + serie.toUpperCase() + filedFolio + ".xml");
            File pdfF = new File(directory + "\\PDF\\" + serie.toUpperCase() + filedFolio + ".pdf");
            Invoice factura = new Invoice();
            if(xmlF.exists())
            {
                factura.xml = new FileInputStream(xmlF);
                factura.cfdi = CFDv32.newComprobante(new FileInputStream(xmlF));
            }
            else
            {
                return null;
            }
            if(pdfF.exists())
            {
                factura.pdf = new FileInputStream(pdfF);
            }
            else
            {
                factura.xml.close();
                return null;
            }
            invoices.add(factura);
        }        
        return invoices;
    }*/

}
