
package core.bobeda;

import SIIL.core.config.Server;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author Azael Reyes
 */
public class FTP extends FTPClient
{
    private String base;    
    
    
    public void close()
    {
        try 
        {
            super.disconnect();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(FTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean connect(Server server) throws IOException
    {
        super.connect(server.getFtpHost(), server.getFtpPort());
        boolean ret = super.login(server.getFtpUser(), server.getFtpPasss());
        base = server.getFtpBase();
        super.enterLocalPassiveMode();
        setFileType(FTP.BINARY_FILE_TYPE);
        return ret;
    }
    
    public boolean addSubdirectory(Vault.Type type,Vault.Origen origen, String subdir) throws IOException
    {
        if(!isConnected()) return false;
        String dir = null;
        if(type == Vault.Type.PO && origen == Vault.Origen.CLIENTE)
        {
            dir = base + "\\Cliente\\PO";
        }        
        else if((type == Vault.Type.FACTURA_PDF | type == Vault.Type.FACTURA_XML) && origen == Vault.Origen.INTERNO)
        {
            dir = base + "\\Interno\\Factura";
        }
        else if(type == Vault.Type.ORDEN_SERVICIO && origen == Vault.Origen.INTERNO)
        {
            dir = base + "\\Interno\\Orden";
        } 
        else
        {
            return false;
        }
        if(subdir != null)
        {
            dir += "\\" + subdir;
        }
        //System.out.println(dir);
        return makeDirectory(dir);
    }
    
    public boolean isExist(Vault.Type type,Vault.Origen origen, String subdir) throws IOException
    {   
        if(!isConnected()) return false;
        String dir = null;
        
        if(type == Vault.Type.PO && origen == Vault.Origen.CLIENTE)
        {
            dir = base + "\\Cliente\\PO";
        }
        else if((type == Vault.Type.FACTURA_PDF | type == Vault.Type.FACTURA_XML) && origen == Vault.Origen.INTERNO)
        {
            dir = base + "\\Interno\\Factura";
        }
        else if(type == Vault.Type.ORDEN_SERVICIO && origen == Vault.Origen.INTERNO)
        {
            dir = base + "\\Interno\\Orden";
        } 
        else
        {
            return false;
        }
        if(subdir != null)
        {
            dir += "\\" + subdir;
        }
                
        changeWorkingDirectory(dir);
        int returnCode = getReplyCode();
        if (returnCode == 550) 
        {
            return false;
        }
        return true;        
    }
    
    public String convertBDString(Vault.Type type,Vault.Origen origen, String subdir)
    {   
        if(!isConnected()) return null;
        String dir = null;
        
        if(type == Vault.Type.PO && origen == Vault.Origen.CLIENTE)
        {
            String string  = base.replace("\\", "\\\\");
            dir = string  + "\\\\Cliente\\\\PO";
        }
        else if((type == Vault.Type.FACTURA_PDF | type == Vault.Type.FACTURA_XML) && origen == Vault.Origen.INTERNO)
        {
            String string  = base.replace("\\", "\\\\");
            dir = string  + "\\\\Interno\\\\Factura";
        }        
        else if(type == Vault.Type.ORDEN_SERVICIO && origen == Vault.Origen.INTERNO)
        {
            String string  = base.replace("\\", "\\\\");
            dir = string + "\\\\Interno\\\\Orden";
        }
        else
        {
            return null;
        }
        
        if(subdir != null)
        {
            dir += "\\\\" + subdir;
        }
        
        return dir;        
    }
    public boolean upload(Vault.Type type,Vault.Origen origen, String subdir,String name, FileInputStream in) throws IOException
    {
        if(!isConnected()) return false;
        String dir = null;
        
        if(type == Vault.Type.PO && origen == Vault.Origen.CLIENTE)
        {
            dir = base + "\\Cliente\\PO";
        }
        else if((type == Vault.Type.FACTURA_PDF | type == Vault.Type.FACTURA_XML) && origen == Vault.Origen.INTERNO)
        {
            dir = base + "\\Interno\\Factura";
        }
        else if(type == Vault.Type.ORDEN_SERVICIO && origen == Vault.Origen.INTERNO)
        {
            dir = base + "\\Interno\\Orden";
        } 
        else
        {
            return false;
        }
        
        if(subdir != null)
        {
            dir += "\\" + subdir;
        }
        if(name != null)
        {
            dir += "\\" + name;
        }        
        //System.out.println(dir);
        boolean ret = storeFile(dir, in);
        return ret;
    }
    
    public OutputStream download(Vault.Type type,Vault.Origen origen, String subdir,String name,String local) throws IOException
    {
        if(!isConnected()) return null;
        String dir = null;        
        
        if(type == Vault.Type.PO && origen == Vault.Origen.CLIENTE)
        {
            dir = base + "\\Cliente\\PO";
        }
        else if((type == Vault.Type.FACTURA_PDF | type == Vault.Type.FACTURA_XML) && origen == Vault.Origen.INTERNO)
        {
            dir = base + "\\Interno\\Factura";
        }
        else if(type == Vault.Type.ORDEN_SERVICIO && origen == Vault.Origen.INTERNO)
        {
            dir = base + "\\Interno\\Orden";
        } 
        if(subdir != null)
        {
            dir += "\\" + subdir;
        }
        if(name != null)
        {
            dir += "\\" + name;
        }
        
        //System.out.println("Server : " + dir);
        FileOutputStream out = new FileOutputStream(local);
        if(retrieveFile(dir, out))        
        {
            out.flush();
            return out;
        }
        else
        {
            return null;
        }
    }
    
    public OutputStream download(String directories,String name,File local) throws IOException
    {
        if(!isConnected()) return null;
        String dir = null;        
        
        if(directories != null)
        {
            dir = directories;
        }      
        if(name != null)
        {
            dir += "\\" + name;
        }
        
        //System.out.println("Server : " + dir);
        //File loc = new File(local);
        if (!local.canRead()) 
        {
            local.setReadable(true);
        }
        
        FileOutputStream out = new FileOutputStream(local);
        if(retrieveFile(dir, out))        
        {
            out.flush();
            return out;
        }
        else
        {
            return null;
        }
    }
    
    @Deprecated
    public OutputStream download(String directories,String name,String local) throws IOException
    {
        if(!isConnected()) return null;
        String dir = null;        
        
        if(directories != null)
        {
            dir = directories;
        }
        
        if(name != null)
        {
            dir += "\\" + name;
        }
        
        //System.out.println("Server : " + dir);
        File loc = new File(local);
        if (!loc.canRead()) 
        {
            loc.setReadable(true);
        }
        
        FileOutputStream out = new FileOutputStream(loc);
        if(retrieveFile(dir, out))        
        {
            out.flush();
            return out;
        }
        else
        {
            return null;
        }
    }
}
