package SIIL.Server;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author areyes
 */
public class PC
{
    public static void fillTB(JTable tb, MySQL conn, Company empre) 
    {
        DefaultTableModel modelo=(DefaultTableModel) tb.getModel(); 
        
        String sql = "SELECT id,dia, desde_H,desde_min,desde_Me, hasta_H,hasta_min,hasta_Me FROM CobranzaPeriodos WHERE emprID = '" + empre.getNumber() + "'";
        Statement stmt = null;
        ResultSet rs;
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            //System.out.println(sql);
            rs = stmt.executeQuery(sql);

            PC pc ;  
            Object[] arpc;
            while(rs.next()) //tiene algun otro periodo
            {
                pc = new PC();
                pc.setDia(rs.getInt("dia"));
                pc.setDesde_H(rs.getByte("desde_H"));
                pc.setDesde_min(rs.getByte("desde_min"));
                pc.setDesde_Me(rs.getString("desde_Me"));
                pc.setHasta_H(rs.getByte("hasta_H"));
                pc.setHasta_min(rs.getByte("hasta_min"));
                pc.setHasta_Me(rs.getString("hasta_Me"));
                arpc = new Objeto[1];
                arpc[0] = pc.toString();
                modelo.addRow(arpc);
            }            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int insert(Company empresa,MySQL conn) 
    {
        PC pc = new PC();
        pc.setEmpresa(empresa);
        pc.setDesde_H((byte)8);
        pc.setDesde_min((byte)0);
        pc.setDesde_Me("am");
        pc.setHasta_H((byte)6);
        pc.setHasta_min((byte)0);
        pc.setHasta_Me("pm");
        pc.setTipo("EF");
        int rest = 0;
        //
        pc.setDia(2);
        String sql = getInsert(pc,empresa);
        rest += conn.insert(sql);
        //
        pc.setDia(3);
        sql = getInsert(pc,empresa);
        rest += conn.insert(sql);
        //
        pc.setDia(4);
        sql = getInsert(pc,empresa);
        rest += conn.insert(sql);
        //
        pc.setDia(5);
        sql = getInsert(pc,empresa);
        rest += conn.insert(sql);
        //
        pc.setDia(6);
        sql = getInsert(pc,empresa);
        rest += conn.insert(sql);
        
        return rest;
    }

    private static String getInsert(PC pc,Company empresa) 
    {
        String sql = " CobranzaPeriodos(dia, desde_H,desde_min,desde_Me, hasta_H,hasta_min,hasta_Me, emprID, type) VALUES (" +
                pc.getIntegerDay() + "," +
                pc.desde_H + "," +
                pc.desde_min + ",'" +
                pc.desde_Me + "'," +
                pc.hasta_H + "," +
                pc.hasta_min + ",'" +
                pc.hasta_Me + "','" +
                empresa.getNumber()  + "','" +
                pc.getTipo() + "')";
                
        return sql;
    }

    public static ArrayList<PC> getList(MySQL conn, Company empre) 
    {
        
        String sql = "SELECT id, dia, desde_H,desde_min,desde_Me, hasta_H,hasta_min,hasta_Me, type, emprID FROM CobranzaPeriodos WHERE emprID = '" + empre.getNumber() + "'";
        Statement stmt = null;
        ResultSet rs;
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            //System.out.println(sql);
            rs = stmt.executeQuery(sql);
  
            ArrayList<PC> pcs = new ArrayList<>();
            PC pc;
            while(rs.next()) //tiene algun otro periodo
            {
                pc = new PC();
                pc.setId(rs.getInt("id"));
                pc.setDia(rs.getInt("dia"));
                pc.setDesde_H(rs.getByte("desde_H"));
                pc.setDesde_min(rs.getByte("desde_min"));
                pc.setDesde_Me(rs.getString("desde_Me"));
                pc.setHasta_H(rs.getByte("hasta_H"));
                pc.setHasta_min(rs.getByte("hasta_min"));
                pc.setHasta_Me(rs.getString("hasta_Me"));
                pc.setTipo(rs.getString("type"));
                pc.setEmpresa(new Company(rs.getString("emprID"), null,null));
                pcs.add(pc);
            }   
            return pcs;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static int update(PC pc, MySQL conn) 
    {
        String sql = " CobranzaPeriodos SET dia=" + pc.getIntegerDay() +
                ", desde_H=" + pc.getDesde_H() +
                ",desde_min=" + pc.getDesde_min() + 
                ",desde_Me='" + pc.getDesde_Me() + 
                "', hasta_H=" + pc.getHasta_H() +
                ",hasta_min=" + pc.getHasta_min() + 
                ",hasta_Me='" + pc.getHasta_Me() +
                "' WHERE id=" + pc.getId();
        return conn.update(sql);
    }

    public static int delete(MySQL conn, int id) 
    {
        String sql = " CobranzaPeriodos WHERE id=" + id;
        return conn.delete(sql);
    }
    private String dia;
    private byte desde_H;
    private byte desde_min;
    private String desde_Me;
    private byte hasta_H;
    private byte hasta_min;
    private String hasta_Me;
    private Company empresa;
    private String tipo;
    private int id;
    
    public PC(PC pc)
    {
        dia = pc.dia;
        desde_H = pc.desde_H;
        desde_min = pc.desde_min;
        desde_Me = pc.desde_Me;
        hasta_H = pc.hasta_H;
        hasta_min = pc.hasta_min;
        hasta_Me = pc.hasta_Me;
    }
    public PC()
    {
        ;
    }
    
    @Override
    public String toString()
    {
        String val = dia ;
        val += " " + desde_H ;
        val += ":" + desde_min;
        val += " " + desde_Me;
        val += " a " + hasta_H + ":" + hasta_min + " " + hasta_Me;
        return val;
    }
    
    public static String getRevision(int dia, String Empre, MySQL conn) 
    {        
        String sql = "SELECT id,dia, desde_H,desde_min,desde_Me, hasta_H,hasta_min,hasta_Me FROM CobranzaPeriodos WHERE emprID = '" + Empre + "' and dia = '" + dia + "'";
        Statement stmt = null;
        ResultSet rs;
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            //System.out.println(sql);
            rs = stmt.executeQuery(sql);

            PC pc = new PC();
            ArrayList<PC> pcs = new ArrayList<>();            
            while(rs.next()) //tiene algun otro periodo
            {
                pc = new PC();
                pc.setDia(rs.getInt("dia"));
                pc.setDesde_H(rs.getByte("desde_H"));
                pc.setDesde_min(rs.getByte("desde_min"));
                pc.setDesde_Me(rs.getString("desde_Me"));
                pc.setHasta_H(rs.getByte("hasta_H"));
                pc.setHasta_min(rs.getByte("hasta_min"));
                pc.setHasta_Me(rs.getString("hasta_Me"));
                pcs.add(pc);
            }
            if(pcs.size()==1)
            {               
                return pcs.get(0).toString(); 
            }
            else if( pcs.size() > 1)
            {
                JFrame frm = new JFrame();
                JDialog dlg = new JDialog(frm,"Seleccion de PC",true);
                PC sel = new PC();
                PCSelect pcsel = new PCSelect(pcs,sel);
                dlg.setContentPane(pcsel);
                dlg.setSize(304, 189);
                dlg.setVisible(true);
                return sel.toString();
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
/*
public enum PC
{
    EF,
    DP
}
*/

    /**
     * @return the dia
     */
    public String getDia() {
        return dia;
    }

    /**
     * @param dia the dia to set
     */
    public void setDia(int dia) 
    {
        switch(dia)
        {
            case 2:
                this.dia = "Lunes";
                break;
            case 3:
                this.dia = "Martes";
                break;
            case 4:
                this.dia = "Miercoles";
                break;
            case 5:
                this.dia = "Jueves";
                break;
            case 6:
                this.dia = "Viernes";
                break;
            case 7:
                this.dia = "Sabado";
                break;                
        }
    }

    /**
     * @return the desde_H
     */
    public byte getDesde_H() {
        return desde_H;
    }

    /**
     * @param desde_H the desde_H to set
     */
    public void setDesde_H(byte desde_H) {
        this.desde_H = desde_H;
    }

    /**
     * @return the desde_min
     */
    public byte getDesde_min() {
        return desde_min;
    }

    /**
     * @param desde_min the desde_min to set
     */
    public void setDesde_min(byte desde_min) {
        this.desde_min = desde_min;
    }

    /**
     * @return the desde_Me
     */
    public String getDesde_Me() {
        return desde_Me;
    }

    /**
     * @param desde_Me the desde_Me to set
     */
    public void setDesde_Me(String desde_Me) {
        this.desde_Me = desde_Me;
    }

    /**
     * @return the hasta_H
     */
    public byte getHasta_H() {
        return hasta_H;
    }

    /**
     * @param hasta_H the hasta_H to set
     */
    public void setHasta_H(byte hasta_H) {
        this.hasta_H = hasta_H;
    }

    /**
     * @return the hasta_min
     */
    public byte getHasta_min() {
        return hasta_min;
    }

    /**
     * @param hasta_min the hasta_min to set
     */
    public void setHasta_min(byte hasta_min) {
        this.hasta_min = hasta_min;
    }

    /**
     * @return the hasta_Me
     */
    public String getHasta_Me() {
        return hasta_Me;
    }

    /**
     * @param hasta_Me the hasta_Me to set
     */
    public void setHasta_Me(String hasta_Me) {
        this.hasta_Me = hasta_Me;
    }

    public void copy(PC pc) 
    {
        dia = pc.dia;
        desde_H = pc.desde_H;
        desde_min = pc.desde_min;
        desde_Me = pc.desde_Me;
        hasta_H = pc.hasta_H;
        hasta_min = pc.hasta_min;
        hasta_Me = pc.hasta_Me;
    }

    /**
     * @return the empresa
     */
    public Company getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Company empresa) {
        this.empresa = empresa;
    }

    public int getIntegerDay() 
    {
        switch(dia)
        {
            case "Lunes":
                return 2;
            case "Martes":
                return 3;
            case "Miercoles":
                return 4;
            case "Jueves":
                return 5;
            case "Viernes":
                return 6;
            case "Sabado":
                return 7; 
        }
        return 0;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getDesde() 
    {
        return (desde_H + ":" + desde_min + " " + desde_Me);
    }

    public String getHasta() 
    {
        return (hasta_H + ":" + hasta_min + " " + hasta_Me);
    }

    public void fill(MySQL conn) 
    {        
        String sql = "SELECT id, dia, desde_H,desde_min,desde_Me, hasta_H,hasta_min,hasta_Me, type, emprID FROM CobranzaPeriodos WHERE id = " + id ;
        Statement stmt = null;
        ResultSet rs;
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            //System.out.println(sql);
            rs = stmt.executeQuery(sql);
  
            if(rs.next()) //tiene algun otro periodo
            {
                setDia(rs.getInt("dia"));
                setDesde_H(rs.getByte("desde_H"));
                setDesde_min(rs.getByte("desde_min"));
                setDesde_Me(rs.getString("desde_Me"));
                setHasta_H(rs.getByte("hasta_H"));
                setHasta_min(rs.getByte("hasta_min"));
                setHasta_Me(rs.getString("hasta_Me"));
                setTipo(rs.getString("type"));
                setEmpresa(new Company(rs.getString("emprID"), null,null));
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
