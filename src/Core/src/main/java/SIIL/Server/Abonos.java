package SIIL.Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author areyes
 */
public class Abonos 
{ 
    private String factID;
    private float valor;
    private String numCheque;
    private String moneda;
    private String type;
    private int id;
    private User user;
    private String comment;
    
    public static ArrayList<Abonos> fillCheque(MySQL conn, String cheque) 
    {
        String sql = " SELECT id, factID, numCheque, valor, moneda, comment FROM Abonos ";
        if(cheque==null)
        {
            sql = sql + " WHERE numCheque is not null";
        }
        else
        {
            sql = sql + " WHERE numCheque = '" + cheque + "'";
        }
        sql = sql + " ORDER BY numCheque ASC, factID ASC";
        ResultSet rs = conn.select(sql);
        try
        {
            ArrayList<Abonos> arr = new ArrayList<>();
            while(rs.next())
            {
                Abonos ab = new Abonos();
                ab.factID = rs.getString("factID");
                ab.numCheque = rs.getString("numCheque");
                ab.valor = rs.getFloat("valor");
                ab.moneda = rs.getString("moneda");
                ab.comment = rs.getString("comment");
                arr.add(ab);
            }           
            return arr;
        }
        catch(SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        catch(Exception e)
        {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return the numCheque
     */
    public String getNumCheque() {
        return numCheque;
    }

    /**
     * @param numCheque the numCheque to set
     */
    public void setNumCheque(String numCheque) {
        this.numCheque = numCheque;
    }

    /**
     * @return the moneda
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the factID
     */
    public String getFactID() {
        return factID;
    }

    /**
     * @param factID the factID to set
     */
    public void setFactID(String factID) {
        this.factID = factID;
    }

    /**
     * @return the valor
     */
    public float getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(float valor) {
        this.valor = valor;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void fillAbono(MySQL conn, String strab) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public enum Options
    {
        SelectionResumen
    }
    
    public void fillJTable(Options type,String folio, JTable tb, MySQL conx)
    {
        if(type == Options.SelectionResumen)
        {
            String sql = "SELECT valor, moneda, type, comment,DATE_FORMAT(fecha,'%d-%m-%Y') as fecha,numCheque,factID FROM Abonos WHERE factID = '" + folio + "'";
            ResultSet rs = conx.select(sql);
            try
            {
                Object[] ab;
                while(rs.next())
                {
                    ab = new Objeto[5];
                    ab[0] = rs.getString("factID"); 
                    ab[1] = rs.getString("valor") + " " + rs.getString("moneda"); 
                    ab[2] = rs.getString("numCheque"); 
                    ab[3] = rs.getString("fecha");                    
                    ab[4] = rs.getString("comment");
                    ((DefaultTableModel) tb.getModel()).addRow(ab);
                }                
            }
            catch(SQLException se)
            {
                //Handle errors for JDBC
                se.printStackTrace();
            }
            catch(Exception e)
            {
                //Handle errors for Class.forName
                e.printStackTrace();
            }
        }
    }
}
