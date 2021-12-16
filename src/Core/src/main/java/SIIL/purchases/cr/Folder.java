
package SIIL.purchases.cr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
//import mx.bigdata.sat.cfdi.CFDv3;
//import mx.bigdata.sat.cfdi.examples.ExampleCFDFactory;
//import mx.bigdata.sat.cfdi.schema.Comprobante;
//import mx.bigdata.sat.security.KeyLoaderEnumeration;
//import mx.bigdata.sat.security.factory.KeyLoaderFactory;

/**
 *
 * @author Azael Reyes
 */
public class Folder 
{
    public Folder()
    {
        String filename = "C:\\Users\\Azael Reyes\\Desktop\\A00020975.xml";
        try {
            open(filename);
        } catch (Exception ex) {
            Logger.getLogger(Folder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void open(String file) throws FileNotFoundException, Exception
    {
        //Comprobante comp = CFDv3.newComprobante(new FileInputStream(file));
        //System.out.println("Folio: " + comp.getFolio());
        //System.out.println("Serie: " + comp.getSerie());
        //System.out.println("Version del documento : " + comp.getVersion());
        //System.out.println("RFC Emisor:" + comp.getEmisor().getRfc());
        //System.out.println("RFC Receptor:" + comp.getReceptor().getRfc());
    }

    /*private void load() throws FileNotFoundException, Exception 
    {
        String fileCer = "cer.cer";
        String fileKey  ="key.key";
        String password = "1234568";
        
        CFDv3 cfd = new CFDv3(ExampleCFDFactory.createComprobante(),
                "mx.bigdata.sat.cfdi.examples");
        cfd.addNamespace("http://www.bigdata.mx/cfdi/example", "example");
        
        PrivateKey key = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
                new FileInputStream(fileKey),
                password
        ).getKey();
        
        X509Certificate cert = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                new FileInputStream(fileCer)
        ).getKey();
        
        mx.bigdata.sat.cfdi.schema.Comprobante sellado = cfd.sellarComprobante(key, cert);
        //System.err.println(sellado.getSello());
        cfd.validar();
        cfd.verificar();
        cfd.guardar(System.out);
    }*/
}
