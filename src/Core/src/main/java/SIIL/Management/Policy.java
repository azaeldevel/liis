package SIIL.Management;

import SIIL.Server.MySQL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author areyes
 */
public class Policy 
{
    protected int user;
    protected String policy;
    protected String pred;

    public boolean check(MySQL conn)
    {
        String sql = "SELECT * FROM PoliciesByGroups_Resolved WHERE uID = " + user + " and policy = '" + policy + "' and pred = '" + pred + "'" ;
        try 
        {
            Statement st = conn.getConnection().createStatement();
            //System.out.println(sql);
            ResultSet rs = st.executeQuery(sql);
            if(rs.next())
            {
                return true;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return false;
    }
    
    /**
     * @return the user
     */
    public int getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(int user) {
        this.user = user;
    }

    /**
     * @return the police
     */
    public String getPolicy() {
        return policy;
    }

    /**
     * @param police the police to set
     */
    public void setPolicy(String policy) {
        this.policy = policy;
    }

    /**
     * @return the pred
     */
    public String getPred() {
        return pred;
    }

    /**
     * @param pred the pred to set
     */
    public void setPred(String pred) {
        this.pred = pred;
    }
}
