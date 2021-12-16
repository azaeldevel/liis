
package core.bobeda;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import SIIL.core.MD5;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;
import process.Return;

/**
 *
 * @author Azael Reyes
 */
public class Archivo
{
    private static final String MYSQL_AVATAR_TABLE = "Bobeda";
    
    private int id;
    private Date fhFolio;
    private String directory;
    private String nombre;
    private String codeName;
    private String downloadFileName;    
    private OutputStream downloadedFile;
    private String brief;
    public enum Type
    {
        PO,
        FACTURA_XML,
        FACTURA_PDF,
        ORDEN_SERVICIO
    }
    public enum Origen
    {
        CLIENTE,
        PROVEEDOR,
        INTERNO
    }    
        
        
    public static Return add(Database dbserver,FTP ftpServer,String name, FileInputStream in,Enterprise enterprise,String brief,Vault.Type type,Vault.Origen origen) throws IOException, SQLException
    {        
        if(dbserver == null)
        {
            return new Return(false,"Servidor de base de datos incorrecto.");
        }
        if(ftpServer == null)
        {
            new Return(false,"Servidor de archivos de datos incorrecto.");
        }
        
        MD5 md5 = new MD5();
        Date date = dbserver.getTimestamp();
        String md5StrCode = md5.generate(date.toString() + "-" + name);
        String subdir = null;
        if(enterprise != null)
        {
            subdir = enterprise.getNumber();
        }
        else
        {
            subdir = null;
        }
        if(!ftpServer.upload(type,origen,subdir,md5StrCode,in))
        {
            return new Return(false,"Fallo la insercción de archivo en Bobeda.");
        }
        String strBbrief = brief;
        if(brief != null)
        {
            strBbrief = "'" + brief + "'";
        }
        else
        {
            strBbrief = "NULL";
        }
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(fhFolio,directory,nombre,codeName,brief) VALUES('" + new java.sql.Date(date.getTime()) + "','" + ftpServer.convertBDString(type,origen,subdir) + "','"  + name + "','" + md5StrCode + "'," + strBbrief + ")";
        Statement stmt = dbserver.getConnection().createStatement();
        System.out.println(sql);
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return new Return(false,"Se afectaron " + affected + " registros(s) : " + sql);
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            return new Return(true,new Archivo(rs.getInt(1)));
        }
        else
        {
            return new Return(false,"Fallo la consulta : " + sql);
        }
    }
    
    public String getBrief() 
    {
        return brief;
    }
           
    public void selectRandom(Database dbserver) throws SQLException 
    {
        if(dbserver == null)
        {
            return;
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            id = rs.getInt(1);
        }
        else
        {
            id = -1;
        }
    }
           
    public boolean download(Database dbserver,FTP ftpServer,File into) throws SQLException, IOException
    {
        download(dbserver); 
        if(into.isDirectory())
        {
            downloadFileName = into.getAbsolutePath() + "\\"+ nombre;
        }
        else
        {
            downloadFileName = into.getAbsolutePath();
        }
        if(FilenameUtils.getExtension(downloadFileName) == null)
        {
            downloadFileName += "." + FilenameUtils.getExtension(nombre);
        }
        downloadedFile = ftpServer.download(directory, codeName, new File(downloadFileName));
        if(downloadedFile == null) return false;
        return true;
    }
        
    public Archivo(int id)
    {
        this.id = id;
    }        
        
    public Return download(Database dbserver) throws SQLException
    {
        if(dbserver == null)
        {
            return new Return(false);
        }
        String sql = "SELECT fhFolio,directory,nombre,codeName FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = dbserver.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            fhFolio = rs.getDate(1);
            directory = rs.getString(2);
            nombre = rs.getString(3);
            codeName = rs.getString(4);
            return new Return(true);
        }
        else
        {
            return new Return(false);
        }
    }

        /**
         * @return the id
         */
    public int getID() 
    {
            return id;
    }

        /**
         * @return the fhFolio
         */
        public Date getFhFolio() {
            return fhFolio;
        }

        /**
         * @return the directory
         */
        public String getDirectory() {
            return directory;
        }

        /**
         * @return the nombre
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * @return the codeName
         */
        public String getCodeName() {
            return codeName;
        }

    /**
     * @return the DownloadFileName
     */
    public String getDownloadFileName() 
    {
        return downloadFileName;
    }

    /**
     * @return the downloadFile
     */
    public OutputStream getDownloadedFile() {
        return downloadedFile;
    }
}
