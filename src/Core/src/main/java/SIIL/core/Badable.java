
package SIIL.core;

/**
 *
 * @author Azael Reyes
 */
public interface Badable 
{
    public int getID();
    public void setID(int id);
    
    public String getBD();
    public void setBD(String bd);
    
    public String getOffice();
    public void setOffice(String office);
    
    public boolean isInMatrix();
    public boolean isInSubsidiary();
}
