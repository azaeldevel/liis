
package SIIL.Server;

import SIIL.Server.Company;

/**
 *
 * @author areyes
 */
public class Where 
{
    String flFolio;
    int opFolio;
    Company flEmpresa;
    int opEmpresa;
    float flTotal;
    int opTotal;
    String flMoneda;
    int opMoneda;
    
    public Where()
    {
        flEmpresa = null;
        flFolio = null;
        flMoneda = null;
        flTotal = 0.0f;
    }
    public String getMoneda()
    {
        return flMoneda;
    }
    public Company getEmpresa()
    {
        return flEmpresa;
    }
    public void setFolio(String folio, int op)
    {
        flFolio = folio;
        opFolio = op;
    }
    public void setEmpresa(Company empresa, int op)
    {
        flEmpresa = empresa;
        opEmpresa = op;
    }
    public void setTotal(float total, int op)
    {
        flTotal = total;
        opTotal = op;
    }
    public void setMoneda(String moneda, int op)
    {
        flMoneda = moneda;
        opMoneda = op;
    }
    @Deprecated
    private String buildClient() 
    {
        String cond;
        
        switch(opEmpresa)
        {
            case 1:
                if(flEmpresa.getNumber() != null)
                {
                    cond = " cnNumber = '" + flEmpresa.getNumber() + "'";
                    return cond;
                }
                return null;
            case 2:
                if(flEmpresa.getNumber() != null)
                {
                    cond = " cnNumber NOT LIKE '" + flEmpresa.getNumber() + "'";
                    return cond;
                }
                return null;
        }
        
        return null;
    }
    @Deprecated
    private String buildTotal() 
    {
        String cond;
        
        switch(opTotal)
        {
            case 1:
                if(flTotal != 0.0f)
                {
                    cond = " total = " + flTotal ;
                    return cond;
                }
                return null;
            case 2:
                if(flTotal != 0.0f)
                {
                    cond = " total > " + flTotal;
                    return cond;
                }
                return null;
            case 3:
                if(flTotal != 0.0f)
                {
                    cond = " total >= " + flTotal;
                    return cond;
                }
                return null;
            case 4:
                if(flTotal != 0.0f)
                {
                    cond = " total < " + flTotal;
                    return cond;
                }
                return null;
            case 5:
                if(flTotal != 0.0f)
                {
                    cond = " total <= " + flTotal;
                    return cond;
                }
                return null;
            case 6:
                if(flTotal != 0.0f)
                {
                    cond = " total != " + flTotal;
                    return cond;
                }
                return null;
        }
        
        return null;
    }
    @Deprecated
    private String buildMoneda() 
    {
        String cond;
        
        switch(opMoneda)
        {
            case 1:
                if(!flMoneda.isEmpty())
                {
                    if(flMoneda.equals("Pesos"))
                    {
                        cond = " moneda = 'MXN'";
                    }
                    else if(flMoneda.equals("Dolar"))
                    {
                        cond = " moneda = 'DLL'";
                    }
                    else
                    {
                        cond = " moneda = 'MXN'";
                    }                        
                    return cond;
                }
                return null;
        }
        
        return null;
    }
    @Deprecated
    private String buildFolio() 
    {
        String cond;
        
        switch(opFolio)
        {
            case 1:
                if(!flFolio.isEmpty())
                {
                    cond = " folio = '%" + flFolio + "'";
                    return cond;
                }
                return null;
            case 2:
                if(!flFolio.isEmpty())
                {
                    cond = " folio NOT LIKE '" + flFolio + "'";
                    return cond;
                }
                return null;
            case 3:
                if(!flFolio.isEmpty())
                {
                    cond = " folio LIKE '%" + flFolio + "%'";
                    return cond;
                }
                return null;
        }        
        return null;
    }
    @Deprecated
    public String buildWhere()
    {
        String where = "";
        String strFolio = buildFolio();
        if(strFolio != null)
        {
            if(where.isEmpty())
            {
                where = strFolio;
            }
            else
            {
                where = where + " and " +  strFolio;
            }
        }
        String strClient = buildClient();
        if(strClient != null)
        {
            if(where.isEmpty())
            {
                where = strClient;
            }
            else
            {
                where = where + " and " +  strClient;
            }
        }
        String strTotal = buildTotal();
        if(strTotal != null)
        {
            if(where.isEmpty())
            {
                where = strTotal;
            }
            else
            {
                where = where + " and " +  strTotal;
            }
        }
        String strMoneda = buildMoneda();
        if(strMoneda != null)
        {
            if(where.isEmpty())
            {
                where = strMoneda;
            }
            else
            {
                where = where + " and " +  strMoneda;
            }
        }
        //System.out.println(where);
    return where;
    }

    public void copy(Where w) 
    {
        flEmpresa=w.flEmpresa;
        flFolio=w.flFolio;
        flMoneda=w.flMoneda;
        flTotal=w.flTotal;
        opEmpresa=w.opEmpresa;
        opFolio=w.opFolio;
        opMoneda=w.opMoneda;
        opTotal=w.opTotal;
    }
}
