
package SIIL.Administracion.Cheques;

/**
 *
 * @author Azael
 */
public class ChequeRow 
{
    private int ID;
    private double total;
    private String moneda;
    private String strTotal;
    private String comment;
    private String number;

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
     * @return the strTotal
     */
    public String getStrTotal() {
        return strTotal;
    }

    /**
     * @param strTotal the strTotal to set
     */
    public void setStrTotal(String strTotal) {
        this.strTotal = strTotal;
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
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }
}
