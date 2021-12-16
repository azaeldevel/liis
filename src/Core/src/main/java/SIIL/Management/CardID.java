
package SIIL.Management;

/**
 *
 * @author areyes
 */
public class CardID 
{
    private String BD;
    private SIIL.Server.User user;
    private String suc;
    
    public CardID()
    {
        user = new SIIL.Server.User();
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
     * @return the user
     */
    public SIIL.Server.User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(SIIL.Server.User user) {
        this.user = user;
    }

    /**
     * @return the suc
     */
    public String getSuc() {
        return suc;
    }

    /**
     * @param suc the suc to set
     */
    public void setSuc(String suc) {
        this.suc = suc;
    }
}
