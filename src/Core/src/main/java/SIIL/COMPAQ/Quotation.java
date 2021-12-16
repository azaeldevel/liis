
package SIIL.COMPAQ;

import SIIL.Server.Database;
import SIIL.core.Office;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author areyes
 */
public class Quotation 
{
    private static final String SQLS_COMERCIAL_AVATAR_VIEW = "dbo.vwLBSDocCustomerQuoteList";
    private String serieFolio;
    private int numberFolio;
    private int DocumentID;

    /**
     * Descompone el String pasado en Serie y Numero.
     * @param df 
     * @throws java.lang.Exception si no se reconoce la serie en el Folio indicado.
     */
    public void setDocFolio(String df) throws Exception
    {
        if(df.substring(0, 3).equals(Office.SERIE_COMERCIAL_TIJUANA))
        {
            serieFolio = Office.SERIE_COMERCIAL_TIJUANA;
        }
        else if(df.substring(0, 3).equals(Office.SERIE_COMERCIAL_MEXICALI))
        {
            serieFolio = Office.SERIE_COMERCIAL_MEXICALI;
        }
        else if(df.substring(0, 3).equals(Office.SERIE_COMERCIAL_ENSENADA))
        {
            serieFolio = Office.SERIE_COMERCIAL_ENSENADA;
        }
        else
        {
            throw new Exception("La serie '" + df.substring(0, 2) + "' no esta soportada.'" +  df + "'");
        }
        
        numberFolio = Integer.valueOf(df.substring(3));
    }
    /**
     * @return the serie
     */
    public String getSerie() 
    {
        return serieFolio;
    }

    /**
     * @return the number
     */
    public int getNumber() 
    {
        return numberFolio;
    }
    
    /**
     * Retorna todas la coincidencias con el texto pasado como parametro.
     * @param dbconn
     * @param office si e diferenci de null agrega la serie al comienzo correspodiente texto de busqueda
     * @param number numero del folio a buscar
     * @param limit maxima cantidad de respuestas
     * @return ArrayList de todas las repuestas encontrada
     */
    public static ArrayList<Quotation> search(Database dbComercial,Office office,int number,int limit) throws SQLException, Exception
    {
        String strsql = "SELECT TOP " + limit + " DocumentID, DocFolio FROM " + SQLS_COMERCIAL_AVATAR_VIEW + " WHERE DocFolio LIKE ";
        if(office != null)
        {
            strsql = strsql + "'%" + office.getSerieOffice(Office.Platform.COMERCIAL) + number + "%'";
        }
        else
        {
            strsql = strsql + "'%"  + number + "%'";
        }
        ArrayList<Quotation> ls = new ArrayList<>();
        ResultSet rs = dbComercial.query(strsql);
        while(rs.next())
        {
            Quotation q = new Quotation();
            q.DocumentID = rs.getInt(1);
            q.setDocFolio(rs.getString(2));
            ls.add(q);
        }
        return ls;
    }
}
