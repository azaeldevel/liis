
package core.bobeda;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import SIIL.service.quotation.ServiceQuotation;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import process.ImportsCN;
import process.Moneda;
import process.Return;
import process.TipoCambio;

/**
 *
 * @author Azael Reyes
 */
public class Business 
{
    public static final String MYSQL_AVATAR_TABLE = "BobedaBusiness";
    private Archivo bobeda;
    private double monto;
    private TipoCambio tc;
    private Enterprise enterprise;
    private String folio;
        
    public static Return link(Database dbserver,ServiceQuotation sq,Business business) throws SQLException, IOException
    {
        ImportsCN imp = new ImportsCN(); 
        imp.importCNQuotation(sq, dbserver);
        return sq.upPOFile(dbserver, business);
        /*List<ServiceQuotation> lst = ServiceQuotation.getSamePO(dbserver, business);
        double purchased = 0;
        for(ServiceQuotation qs : lst)
        {
            qs.download(dbserver);
            imp.importCNQuotation(qs, dbserver);            
            if(qs.downQuotation(dbserver.getConnection()).isFlag()) 
            {
                qs.getQuotation().downTotal(dbserver);
                System.out.println(qs.getQuotation().getTotal());
                purchased = purchased + qs.getQuotation().getTotal();
                
                if(business.getMoneda() != null)
                {
                    if(business.getMoneda() != qs.getQuotation().getMoneda())
                    {
                        return new Return(false,"La moneda de la cotizacion '" + qs.getQuotation().getFolio() + "' es diferente de la moneda del PO");
                    }
                }
            }
        }*/
        //System.out.println("-------------");
        //System.out.println(purchased);
        //¿Es el monto comprado mayor al del PO en cuestion?
        /*if(purchased > business.getMonto())
        {            
            return new Return(false,"Se han excedido el monto de la orden de compra, hay '" + lst.size() + "' cotizaciones asociadas al PO '" + business.getFolio() + "'.");               
        }
        else
        {
            return new Return(true);
        }*/
    }
    
    public Return download(Database dbserver) throws SQLException
    {
        if(dbserver == null)
        {
            return new Return(false);
        }
        
        Return ret = bobeda.download(dbserver);
        if(ret.isFail()) return ret;
        
        String sql = "SELECT monto,company,folio,monedaLocal,monedaForeign,monedaForeignValor FROM " + MYSQL_AVATAR_TABLE + " WHERE bobeda = " + bobeda.getID();
        Statement stmt = dbserver.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            monto = rs.getDouble(1);
            enterprise = new Enterprise(rs.getInt(2));
            enterprise.download(dbserver);
            folio = rs.getString(3);
            tc = new TipoCambio(rs.getString(4), rs.getString(5), rs.getDouble(6));
            return new Return(true);
        }
        else
        {
            return new Return(false);
        }
    }
    
    public boolean selectRandom(Database connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT bobeda FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        ResultSet rs = connection.query(sql);
        if(rs.next())
        {
            bobeda = new Vault(rs.getInt(1));
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Business(Archivo bobeda)
    {
        this.bobeda = bobeda;
    }
    
    public static Return add(Database dbserver,FTP ftpServer,String name,FileInputStream in,Enterprise enterprise,double monto,String biref,Vault.Type type,Vault.Origen origen,String folio,TipoCambio tipoCambio) throws IOException, SQLException
    {
        if(dbserver == null)
        {
            return new Return(false,"Servidor de base de datos incorrecto.");
        }
        
        if(ftpServer == null)
        {
            new Return(false,"Servidor de archivos de datos incorrecto.");
        }
        
        Return ret = Vault.add(dbserver, ftpServer, name, in, enterprise, biref,type,origen);
        if(ret.isFlag())
        {
            Archivo bobeda = (Archivo)ret.getParam(); 
            String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (bobeda,monto,company,folio,type,monedaLocal,monedaForeign,monedaForeignValor";
            sql += ") VALUES(" + bobeda.getID() + ",";
            if(monto > -1)
            {
                sql += String.valueOf(monto);
            }
            else
            {
                sql += "NULL";
            }
            sql += "," + enterprise.getID() + ",'" + folio + "'";
            if(type == Vault.Type.PO)
            {
                sql += ",'PO'";
            }
            else if(type == Vault.Type.FACTURA_XML)
            {
                sql += ",'FXML'";
            }
            else if(type == Vault.Type.FACTURA_PDF)
            {
                sql += ",'FPDF'";
            }
            else
            {
                sql += ",NULL";
            }
            
            if(tipoCambio != null)
            {
                if(tipoCambio.getLocal() == Moneda.MXN)
                {
                    sql += ",'MXN'";
                }
                else if(tipoCambio.getLocal() == Moneda.USD)
                {
                    sql += ",'USD'";
                }
                else
                {
                    sql += ",NULL";
                }
            }
            else
            {
                sql += ",NULL";
            }            
            if(tipoCambio != null)
            {
                if(tipoCambio.getForeign() == Moneda.MXN)
                {
                    sql += ",'MXN'";
                }
                else if(tipoCambio.getForeign() == Moneda.USD)
                {
                    sql += ",'USD'";
                }
                else
                {
                    sql += ",NULL";
                }
            }
            else
            {
                sql += ",NULL";
            }
            sql += ",";
            if(tipoCambio != null)
            {
                sql += tipoCambio.getForeignValor();
            }
            else
            {
                sql += "NULL";
            }
            sql += ")";
            //System.out.println(sql);            
            Statement stmt = dbserver.getConnection().createStatement();
            int affected = stmt.executeUpdate(sql);
            if(affected != 1)
            {
                return new Return(false,"Se afectaron " + affected + " registros(s)");
            }
            else
            {
                return new Return(true,new Business(bobeda));
            }
        }
        else
        {
            return ret;
        }
    }
    /**
     * @return the bobeda
     */
    public Archivo getBobeda() 
    {
        return bobeda;
    }

    /**
     * @return the monto
     */
    public double getMonto() 
    {
        return monto;
    }

    /**
     * @return the enterprise
     */
    public Enterprise getEnterprise() 
    {
        return enterprise;
    }

    private void clean() 
    {
        bobeda = null;
        enterprise = null;
        monto = -1.0;
        folio = null;
    }

    /**
     * @return the folio
     */
    public String getFolio() 
    {
        return folio;
    }

    /**
     * @return the moneda
     */
    public Moneda getMonedaLocal() {
        return tc.getLocal();
    }

    /**
     * @return the monedaForeign
     */
    public Moneda getMonedaForeign() {
        return tc.getForeign();
    }

    /**
     * @return the monedaForeignValor
     */
    public double getMonedaForeignValor() {
        return tc.getForeignValor();
    }
}
