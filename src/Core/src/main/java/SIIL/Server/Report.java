package SIIL.Server;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author areyes
 */
public class Report 
{
    private int id;
    private String report;
    private String folio;
    private int uID;
    
    public static ArrayList<Report> Listar(MySQL conn, String report)
    {
        String sql = "SELECT id,report,folio,uID FROM Reports WHERE report = '" + report + "'";
        Statement stmt = null;
        try
        {
            //System.out.println("Creating statement...");
            stmt = (Statement) conn.getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery(sql);

            Report rep;
            ArrayList<Report> vReps = new ArrayList<>();
            while(rs.next())
            {
                rep = new Report();
                vReps.add(rep);
                rep.setId(rs.getInt("id"));
                rep.setFolio(rs.getString("folio"));
                rep.setReport(rs.getString("report"));
                rep.setuID(rs.getInt("uID"));
                //System.out.println("Adding " + rs.getString("emprID"));                
            }
            rs.close();
            stmt.close();
            return vReps;
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
        finally
        {
            //finally block used to close resources
            try
            {
                if(stmt != null) stmt.close();
            }
            catch(SQLException se2)
            {
                ;
            }// nothing we can do
        }//end try 
        return null;
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
     * @return the report
     */
    public String getReport() {
        return report;
    }

    /**
     * @param report the report to set
     */
    public void setReport(String report) {
        this.report = report;
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
     * @return the uID
     */
    public int getuID() {
        return uID;
    }

    /**
     * @param uID the uID to set
     */
    public void setuID(int uID) {
        this.uID = uID;
    }
}
