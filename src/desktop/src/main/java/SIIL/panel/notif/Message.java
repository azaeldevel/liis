
package SIIL.panel.notif;

import SIIL.Server.MySQL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Azael
 */
public class Message 
{
    private int id;
    private Date folio;
    private String comment;
    private String title;

    /**
     * @return the id
     */
    public int getID() {
        return id;
    }

    /**
     * @param id the id to set
     */
    /*public void setID(int id) {
        this.id = id;
    }*/

    /**
     * @return the folio
     */
    public Date getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(Date folio) {
        this.folio = folio;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    void createID(MySQL conn) throws SQLException, Exception 
    {
        String sql = "SELECT MAX(ID) FROM NotifMessage";
        ResultSet rs;
          
        Statement stmt;
        stmt = (Statement) conn.getConnection().createStatement();
        rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            id = rs.getInt("MAX(ID)") + 1;
        }
        else
        {
            throw new Exception("Imposible crear el ID para el mensaje");
        }
    }
    
    int insert(MySQL conn) throws SQLException 
    {
        String sql = "INSERT INTO NotifMessage(ID,fhFolio,comment,title) VALUES(?,NOW(),?,?)";
        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.setString(2, comment);
        pstmt.setString(3, title);
        return pstmt.executeUpdate();
    }
}
