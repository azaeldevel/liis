
package process;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import core.FailResultOperationException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Azael Reyes
 */
public class CSV 
{    
    private ArrayList<String[]> parseFile(String strPath,javax.swing.text.Document document) throws IOException
    {
        Path path = Paths.get(strPath);
        //if(!path.endsWith(".csv")) throw  new FailResultOperationException("El archivo '" + strPath + "' deve tener extecion CSV para realizar esta operación");
        Charset charset = Charset.forName("ISO-8859-1");
        List<String> lines = Files.readAllLines(path, charset);
        ArrayList<String[]> ln = new ArrayList<String[]>();
        try 
        {
            if(document != null) document.insertString(document.getLength(), "Prosesando archivo '" + strPath + "' ...\n" , null);
        } 
        catch (BadLocationException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        for(String str : lines)
        {
            String[] tmArray = str.split(",");            
            //System.out.println(tmArray[0]);
            ln.add(tmArray);
        }        
        
        return ln;
    }
    
    /**
     * Importa cliente con PO anterior
     * @param database
     * @param path
     * @return true si se sealiza la importacion false en otro caso
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public boolean importCPOA(Database database,String strPath,javax.swing.text.Document document) throws IOException, SQLException
    {
        Path path = Paths.get(strPath);
        if(!path.endsWith("cpoa.csv")) throw  new FailResultOperationException("El archivo '" + strPath + "' deve llamarse 'cpoa.csv' para realizar esta operación");
        
        ArrayList<String[]> list = parseFile(strPath,document);
        Enterprise enterprise = null;
        for(String[] line : list)
        {
            enterprise = new Enterprise();
            enterprise.complete(database, line[0]);
            ArrayList<String[]> ln = new ArrayList<String[]>();
            try 
            {
                if(document != null) document.insertString(document.getLength(), "    Actializando '" + enterprise.getNumber() + "' ..." , null);
            } 
            catch (BadLocationException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
            if(line[1].equals("A"))
            {
                enterprise.upRequirePO(database, Enterprise.RequirePO.ANTERIOR);
            }
            else if(line[1].equals("P"))
            {
                enterprise.upRequirePO(database, Enterprise.RequirePO.POSTERIOR);
            }            
            else if(line[1].equals("N"))
            {
                enterprise.upRequirePO(database, Enterprise.RequirePO.NO);
            }
            else if(line[1].equals("O"))
            {
                enterprise.upRequirePO(database, Enterprise.RequirePO.OPCIONAL);
            }
            else
            {
                try 
                {
                    if(document != null) document.insertString(document.getLength(), "Fail. \n" , null);
                    continue;
                } 
                catch (BadLocationException ex) 
                {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
            try 
            {
                if(document != null) document.insertString(document.getLength(), "Done. \n" , null);
            } 
            catch (BadLocationException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return true;
    }
}
