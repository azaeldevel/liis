
package SIIL.purchase.cr;

import SIIL.Server.Database;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Azael
 */
public class Factura 
{
    String serie;
    private String folio;
    private ArrayList<Document> documents;
    private float monto;
    private int fID;

    Factura(List<Document> list) 
    {
        documents = (ArrayList<Document>) list;
    }
    
    public boolean insert(Database conn) throws SQLException
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance();        
        PreparedStatement pstmt;
        
        String sql = "INSERT INTO PurchaseCRF(fID,folio,monto,montoStr) VALUES(?,?,?,?)";
        pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setInt(1, getfID());
        pstmt.setString(2, folio);
        pstmt.setDouble(3, monto);
        pstmt.setString(4, nf.format(monto));        
        pstmt.executeUpdate();
        
        for(int i = 0; i < documents.size(); i++)
        {
            documents.get(i).setfID(fID);
            documents.get(i).setFolio(folio);
            documents.get(i).insert(conn);
        }
        
        return true;
    }
    public Factura()
    {
        documents = new ArrayList<>();
    }
    
    /**
     * @return the folio
     */
    public String getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * @return the documents
     */
    public ArrayList<Document> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(ArrayList<Document> documents) {
        this.documents = documents;
    }

    /**
     * @return the monto
     */
    public float getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(float monto) {
        this.monto = monto;
    }

    /**
     * @return the fID
     */
    public int getfID() {
        return fID;
    }

    /**
     * @param fID the fID to set
     */
    public void setfID(int fID) {
        this.fID = fID;
    }
}
