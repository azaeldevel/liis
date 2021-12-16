
package SIIL.Server;

/**
 *
 * @author areyes
 */
public class FacComment 
{

    public static int insert(MySQL conn, FacComment comment) 
    {
        String sql = " FacturasComment(folio,uID,comment) VALUES('" + comment.getFolio() + "'," + comment.getUser().getuID() + ",\"" + comment.getText() + "\")" ;
        return conn.insert(sql);
    }
    private String text;
    private String folio;
    private User user;
    /**
     * @return the commnet
     */
    public String getText() 
    {
        return text;
    }

    /**
     * @param commnet the commnet to set
     */
    public void setText(String commnet) 
    {
        this.text = commnet;
    }

    /**
     * @return the folio
     */
    public String getFolio() 
    {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(String folio) 
    {
        this.folio = folio;
    }

    /**
     * @return the user
     */
    public User getUser() 
    {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) 
    {
        this.user = user;
    }
}
