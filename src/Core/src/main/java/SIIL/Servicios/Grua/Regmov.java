
package SIIL.Servicios.Grua;

import SIIL.Server.Database;
import SIIL.Server.MySQL;
import java.sql.Statement;
//import SIIL.Servicios.Regmov.MovRead;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael
 */
public class Regmov 
{
    private String where;
    private int length;
    private String restrict;

    /**
     * @return the where
     */
    public String getWhere() {
        return where;
    }

    /**
     * @param where the where to set
     */
    public void setWhere(String where) {
        this.where = where;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }
    public void fillModel(DefaultTableModel dm, String restrict,String suc) 
    {
        MySQL conn = new MySQL();
        conn.Create();
        if(conn.getConnection() == null)
        {
            JOptionPane.showMessageDialog(null,
                "Conexion a Servidor Invalida",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            return;
        }
        Statement stmt = null;
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            
            String SQL = "SELECT folio,DATE_FORMAT(fhmov,'%d/%m/%Y') as fhmov,tmov,uso,sa,compNumber,compName,firma,numeco,marca,modelo,serie,horometro,note  FROM Movements_Resolved";
                        
            if(!restrict.isEmpty())
            {
                if(suc.equals("bc.tj"))
                {
                    SQL = SQL + " WHERE " + restrict;
                }
                else
                {
                    SQL = SQL + " WHERE " + restrict + " AND suc='" + suc + "'";
                } 
            }
            System.out.println(SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            Object[] row;
            while(rs.next())
            {
                row = new Object[14];
                row[0] = rs.getString("folio");
                row[1] = rs.getString("fhmov");
                if(rs.getString("tmov").equals("ent"))
                {
                    row[2] = "Entrada";
                }
                else if(rs.getString("tmov").equals("sal"))
                {
                    row[2] = "Salida";
                }
                else if(rs.getString("tmov").equals("mov"))
                {
                    row[2] = "Movimiento";
                }
                else if(rs.getString("tmov").equals("aj"))
                {
                    row[2] = "Ajuste";
                }
                
                if(rs.getString("uso").equals("rta"))
                {
                    row[3] = "Renta";
                }
                else if(rs.getString("uso").equals("rtacp"))
                {
                    row[3] = "Renta Corto Plazo";
                }
                else if(rs.getString("uso").equals("rtaoc"))
                {
                    row[3] = "Renta Opcion de compra";
                }
                else if(rs.getString("uso").equals("rep"))
                {
                    row[3] = "Reparacion";
                }
                else if(rs.getString("uso").equals("pres"))
                {
                    row[3] = "Prestamo";
                }
                else if(rs.getString("uso").equals("vta"))
                {
                    row[3] = "Venta";
                }
                else if(rs.getString("uso").equals("mov"))
                {
                    row[3] = "Movimiento";
                }
                else if(rs.getString("uso").equals("disp"))
                {
                    row[3] = "Disponible";
                }
                else if(rs.getString("uso").equals("baja"))
                {
                    row[3] = "Baja";
                }
                else if(rs.getString("uso").equals("tpint"))
                {
                    row[3] = "T. de Pintura";
                }
                else if(rs.getString("uso").equals("corr"))
                {
                    row[3] = "Cubriendo";
                }
                else if(rs.getString("uso").equals("otra"))
                {
                    row[3] = "Otra...";
                }
                else if(rs.getString("uso").equals("aj"))
                {
                    row[3] = "Ajuste";
                }
                
                row[4] = rs.getString("sa");
                row[5] = rs.getString("compNumber");
                row[6] = rs.getString("compName");
                row[7] = rs.getString("firma");
                row[8] = rs.getString("numeco");
                row[9] = rs.getString("marca");
                row[10] = rs.getString("modelo");
                row[11] = rs.getString("serie");
                row[12] = rs.getString("horometro");
                row[13] = rs.getString("note");
                dm.addRow(row);
            }
            rs.close();
            stmt.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Regmov.class.getName()).log(Level.SEVERE, null, ex);
        }
        conn.Close();
    }
    public void fillModel(DefaultTableModel dm, String suc) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException, SQLException 
    {
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
        Database dbserver = null;
        serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
        dbserver = new Database(serverConfig);
        
        if(dbserver.getConnection() == null)
        {
            JOptionPane.showMessageDialog(null,
                "Conexion a Servidor Invalida",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            return;
        }
        Statement stmt = null;
        try 
        {
            stmt = (Statement) dbserver.getConnection().createStatement();
            String SQL = "SELECT folio,DATE_FORMAT(fhmov,'%d/%m/%Y') as fhmov,tmov,uso,sa,compNumber,compName,firma,numeco,marca,modelo,serie,horometro,note  FROM Movements_Resolved";
            if(!where.isEmpty())
            {
                if(suc.equals("bc.tj"))
                {
                    restrict = " WHERE numeco LIKE '%" + where + "%' OR modelo LIKE '%" + where + "%' OR serie LIKE '%" + where + "%' OR compName LIKE '%" + where + "%' OR folio LIKE '%" + where + "%'";
                }
                else
                {
                    restrict = " WHERE (numeco LIKE '%" + where + "%' OR modelo LIKE '%" + where + "%' OR serie LIKE '%" + where + "%' OR compName LIKE '%" + where + "%' OR folio LIKE '%" + where + "%') AND ( suc='" + suc + "')";
                }
            }
            
            if(length == 0)
            {
               ;
            }
            else if(length > 0 && !where.isEmpty())
            {
                restrict = restrict + " LIMIT " + length;
            }
            else if(length > 0 && where.isEmpty())
            {
                if(suc.equals("bc.tj"))
                {
                    restrict =  " LIMIT " + length;
                }
                else
                {
                    restrict = " WHERE suc='" + suc + "' LIMIT " + length;
                }
            }
            
            if(!restrict.isEmpty())
            {
                SQL = SQL + restrict; 
            }
            System.out.println(SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            Object[] row;
            while(rs.next())
            {
                row = new Object[14];
                row[0] = rs.getString("folio");
                row[1] = rs.getString("fhmov");
                if(rs.getString("tmov") != null)
                {
                    if(rs.getString("tmov").equals("ent"))
                    {
                        row[2] = "Entrada";
                    }
                    else if(rs.getString("tmov").equals("sal"))
                    {
                        row[2] = "Salida";
                    }
                    else if(rs.getString("tmov").equals("mov"))
                    {
                        row[2] = "Movimiento";
                    }
                    else if(rs.getString("tmov").equals("aj"))
                    {
                        row[2] = "Ajustes";
                    }
                    else if(rs.getString("tmov").equals("ret"))
                    {
                        row[2] = "Retorno";
                    }
                    else if(rs.getString("tmov").equals("canc"))
                    {
                        row[2] = "Cancelación";
                    }
                    else if(rs.getString("tmov").equals("corr"))
                    {
                        row[2] = "Correción";
                    }
                }
                
                if(rs.getString("uso") != null)
                {
                    if(rs.getString("uso").equals("rtacp"))
                    {
                        row[3] = "Renta Corto Plazo";
                    }
                    else if(rs.getString("uso").equals("rtaoc"))
                    {
                        row[3] = "Renta Opción de Compra";
                    }
                    else if(rs.getString("uso").equals("rep"))
                    {
                        row[3] = "Reparacion";
                    }
                    else if(rs.getString("uso").equals("pres"))
                    {
                        row[3] = "Prestamo";
                    }
                    else if(rs.getString("uso").equals("vta"))
                    {
                        row[3] = "Venta";
                    }
                    else if(rs.getString("uso").equals("mov"))
                    {
                        row[3] = "Movimiento";
                    }
                    else if(rs.getString("uso").equals("disp"))
                    {
                        row[3] = "Disponible";
                    }
                    else if(rs.getString("uso").equals("baja"))
                    {
                        row[3] = "Baja";
                    }
                    else if(rs.getString("uso").equals("tpint"))
                    {
                        row[3] = "T. de Píntura";
                    }
                    else if(rs.getString("uso").equals("corr"))
                    {
                        row[3] = "Cubriendo";
                    }
                    else if(rs.getString("uso").equals("otra"))
                    {
                        row[3] = "Otra...";
                    }
                    else if(rs.getString("uso").equals("aj"))
                    {
                        row[3] = "Ajuste";
                    }
                }
                
                row[4] = rs.getString("sa");
                row[5] = rs.getString("compNumber");
                row[6] = rs.getString("compName");
                row[7] = rs.getString("firma");
                row[8] = rs.getString("numeco");
                row[9] = rs.getString("marca");
                row[10] = rs.getString("modelo");
                row[11] = rs.getString("serie");
                row[12] = rs.getString("horometro");
                row[13] = rs.getString("note");
                dm.addRow(row);
            }
            rs.close();
            stmt.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Regmov.class.getName()).log(Level.SEVERE, null, ex);
        }
        dbserver.close();
    }

    /**
     * @return the restrict
     */
    public String getRestrict() {
        return restrict;
    }

    /**
     * @param restrict the restrict to set
     */
    public void setRestrict(String restrict) {
        this.restrict = restrict;
    }
}
