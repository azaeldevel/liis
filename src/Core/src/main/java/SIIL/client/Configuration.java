
package SIIL.client;

import SIIL.Server.Database;
import SIIL.core.Badable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Azael
 */
public class Configuration implements Badable
{
    private int ID;
    
    private String BD;
    private String office;
    private session.User user;
    
    private String object;
    private String attribute;
    private String value;
    
    private String comment;

    @Override
    public int getID() {
        return this.ID;
    }

    @Override
    public void setID(int id) {
        this.ID = id;
    }

    @Override
    public String getBD() {
        return this.BD;
    }

    @Override
    public void setBD(String bd) {
        this.BD = bd;
    }

    @Override
    public String getOffice() {
        return this.office;
    }

    @Override
    public void setOffice(String office) {
        this.office = office;
    }

    @Override
    public boolean isInMatrix()
    {
        return this.office.equals("bc.tj");
    }
    
    @Override
    public boolean isInSubsidiary()
    {
        return !this.office.equals("bc.tj");
    }     

    /**
     * @return the object
     */
    public String getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * @return the attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
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

    /**
     * @return the user
     */
    public session.User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(session.User user) {
        this.user = user;
    }

    public int update(Database db) throws SQLException 
    {        
        searchID(db);
        
        if(ID > 0)
        {
            String sqlUpdate = "UPDATE  Configuration SET value = ? WHERE ID = ?";
            PreparedStatement pstmtUpdate = db.getConnection().prepareStatement(sqlUpdate);
            pstmtUpdate.setString(1, value);
            pstmtUpdate.setInt(2, ID);
            return pstmtUpdate.executeUpdate();            
        }
        else
        {
            String sqlInsert = "INSERT INTO Configuration (BD,office,user,object,attribute,value) VALUES(?,?,?,?,?,?)";
            PreparedStatement pstmInsert = db.getConnection().prepareStatement(sqlInsert);
            pstmInsert.setString(1, BD);
            pstmInsert.setString(2, office);
            pstmInsert.setString(3, user.getAlias());
            pstmInsert.setString(4, object);
            pstmInsert.setString(5, attribute);
            pstmInsert.setString(6, value);
            return pstmInsert.executeUpdate();
        }
    }

    public boolean searchID(Database db) throws SQLException 
    {
        String sqlSelect = "SELECT ID,value FROM Configuration WHERE BD = ? and office = ? and user = ? and object = ? and attribute = ?";
        PreparedStatement pstmt = db.getConnection().prepareStatement(sqlSelect);
        pstmt.setString(1, BD);
        pstmt.setString(2, office);
        pstmt.setString(3, user.getAlias());
        pstmt.setString(4, object);
        pstmt.setString(5, attribute);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next())
        {
            ID = rs.getInt("ID");
            return true;
        }
        else
        {
            ID = -1;
            return false;
        }
    }

    public boolean check(Database db)  throws SQLException
    {
        String sqlSelect = "SELECT ID FROM Configuration WHERE BD = ? and office = ? and user = ? and object = ? and attribute = ? and value = ?";
        PreparedStatement pstmt = db.getConnection().prepareStatement(sqlSelect);
        pstmt.setString(1, BD);
        pstmt.setString(2, office);
        pstmt.setString(3, user.getAlias());
        pstmt.setString(4, object);
        pstmt.setString(5, attribute);
        pstmt.setString(6, value);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next())
        {
            ID = rs.getInt("ID");
            return true;
        }
        else
        {
            ID = -1;
            return false;
        }
    }
}
