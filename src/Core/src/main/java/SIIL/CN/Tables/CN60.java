
package SIIL.CN.Tables;

import SIIL.CN.Engine.Table;
import SIIL.CN.Sucursal;
import java.io.File;
import java.io.IOException;

/**
 * @version 0.2.0
 * @author Azael Reyes
 */
public class CN60  extends Table
{
    
    public static String DIR_CN60_TJ = "C:\\CN60\\";
    public static String DIR_CN60_MXL = "C:\\CN60_MXL\\";
    public static String DIR_CN60_ENS = "C:\\CN60_ENS\\";
    private Sucursal sucursal;    
    
    /**
     * @return the sucursal
     */
    public Sucursal getSucursal() 
    {
        return sucursal;
    }
    
    /**
     * Indica si el acceso al CN60esta disponible.
     * @return true si todas las bases de datos esta instalas accesibles
     */
    public static boolean isWorking()
    {
        File dirT,dirM,dirE;
        dirT = new File(DIR_CN60_TJ);
        dirM = new File(DIR_CN60_MXL);
        dirE = new File(DIR_CN60_ENS);
        return (dirT.exists() & dirM.exists() & dirE.exists());
    }
    
    public static boolean isWorking(Sucursal sucursal)
    {
        File dir = null;
        switch (sucursal) 
        {
            case BC_Tijuana:
                dir = new File(DIR_CN60_TJ);
                break;
            case BC_Mexicali:
                dir = new File(DIR_CN60_MXL);
                break;
            case BC_Ensenada:
                dir = new File(DIR_CN60_ENS);
                break;
            default:
                break;
        }
        return dir.exists();
    }
    
    public <C extends CN60> void Load(Class<C> clase,Sucursal sucursal) throws IOException
    {
        if(null != sucursal)
        switch (sucursal) 
        {
            case BC_Tijuana:
                super.Load(DIR_CN60_TJ + clase.getSimpleName() + ".dbf");
                break;
            case BC_Mexicali:
                super.Load(DIR_CN60_MXL + clase.getSimpleName() + ".dbf");
                break;
            case BC_Ensenada:
                super.Load(DIR_CN60_ENS + clase.getSimpleName() + ".dbf");
                break;
            default:
                break;
        }        
        this.sucursal = sucursal;
        //System.out.println("Loading " + CNROOT + clase.getSimpleName() + ".dbf");
    }
    
    public <C extends CN60> void Load(Class<C> clase,Sucursal sucursal,boolean activeDBF,boolean activeFPT) throws IOException
    {
        if(null != sucursal)
        switch (sucursal) 
        {
            case BC_Tijuana:
                super.Load(DIR_CN60_TJ + clase.getSimpleName(),activeDBF,activeFPT);
                break;
            case BC_Mexicali:
                super.Load(DIR_CN60_MXL + clase.getSimpleName(),activeDBF,activeFPT);
                break;
            case BC_Ensenada:
                super.Load(DIR_CN60_ENS + clase.getSimpleName(),activeDBF,activeFPT);
                break;
            default:
                break;
        }        
        this.sucursal = sucursal;
        //System.out.println("Loading " + CNROOT + clase.getSimpleName() + ".dbf");
    }
}
