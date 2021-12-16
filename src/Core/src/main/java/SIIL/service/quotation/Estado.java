
package SIIL.service.quotation;

import SIIL.Server.Database;
import SIIL.Server.User;
import session.Credential;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Azael
 */
@Deprecated
public class Estado 
{
    private String BD;
    private String code;
    private String label;
    private int ordinal;

    public static final String STEP_ONE = "docedit";
    public static final String STEP_TWO = "docpen";
    public static final String STEP_THREE = "pedpen";
    public static final String STEP_FOUR = "pedtrans";
    public static final String STEP_FIVE = "pedArrb";
    public static final String STEP_SIX = "pedsur";
    public static final String STEP_SEVEN = "pedfin";
    public static final String transitionCodeForFinalizada = "pedfin";
    public static final String transitionCodeForCancel = "cancel";

    
    public Estado(Credential cred,String edo)
    {
        code = edo;
        BD = cred.getBD();
    }

    public Estado() 
    {
        ;
    }

    public Throwable fill(Database db, Credential cred,String code) 
    {
        String query = "SELECT * FROM ServiosOrdenEstado WHERE code = '" + code + "'";
        ResultSet rs;
        Throwable ret = null;
        
        java.sql.Statement stmt;
        try 
        {
            stmt = (Statement) db.getConnection().prepareStatement(query);
            rs = stmt.executeQuery(query);
            if(rs.next())
            {
                this.code = code;
                this.BD = cred.getBD();
                this.label = rs.getString("label");
                this.ordinal = rs.getInt("ordinal");
                return null;
            }
            else
            {
                //return new com.galaxies.andromeda.util.Texting.Error("Fallo el relledo del estado de Servicio con código " + code + "'");
                return null;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
    }

    /**
     * @return the BD
     */
    public String getBD() {
        return BD;
    }

    /**
     * @param BD the BD to set
     */
    public void setBD(String BD) {
        this.BD = BD;
    }

    /**
     * @return the estado
     */
    public String getCode() {
        return code;
    }

    /**
     * @param estado the estado to set
     */
    public void setCode(String estado) {
        this.code = estado;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the ordinal
     */
    public int getOrdinal() {
        return ordinal;
    }

    /**
     * @param ordinal the ordinal to set
     */
    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
}
