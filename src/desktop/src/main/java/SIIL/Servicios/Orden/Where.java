
package SIIL.Servicios.Orden;

import SIIL.Server.Database;
import SIIL.client.Department;
import SIIL.core.Office;
import process.State;
import session.Credential;

/**
 *
 * @author Azael
 */
public class Where 
{
    private Database database;
    private State status;
    private Credential credential;
    private Integer lengh;
    private String qSearch;
    private String[] fieldsQS;
    private Boolean personal;
    private Department department;
    private Office office;
    
    public Where(Database db, State status,Credential credential)
    {
        database = db;
        this.status = status;
        this.credential = credential; 
        this.lengh = 0;
        fieldsQS = new String[5];
        fieldsQS[0] = "sa";
        fieldsQS[1] = "folio";
        fieldsQS[2] = "compNumber";
        fieldsQS[3] = "compName";
        fieldsQS[4] = "ownerName";
    }
    
    public void setQuick(Integer lengh, String qSearch)
    {
        this.lengh = lengh;
        this.qSearch = qSearch;
    }
    
    public void setFilter(boolean personal,Department department,Office office)
    {
        this.personal = personal;
        this.department = department;
        this.office = office;        
    }
    
    public String getString()
    {
        String where;
        
        if(status != null && credential != null)
        {
            where = " estado = '" + status.getCode() + "' AND BD = '" + credential.getBD() + "' ";
        }
        else
        {
            return null;
        }
        SIIL.client.Scope scope = new SIIL.client.Scope();
        scope.requestScope(database,credential.getBD(),credential.getOffice(),"orserv","default",credential.getUser());
        if(scope.scope.equals("E"))
        {
            if(office != null && department != null && personal)
            {
                where += " AND suc = '" + office.getCode() + "' AND department ='" + department.code + "' AND ownerPerson = " + credential.getUser().getpID();
            }
            else if(office != null && department != null)
            {
                where += " AND suc = '" + office.getCode() + "' AND department ='" + department.code + "'";
            }
            else if(office != null && personal)
            {
                where += " AND suc = '" + office.getCode() + "' AND ownerPerson = " + credential.getUser().getpID();
            }
            else if(department != null && personal)
            {
                where += " AND department = '" + department.code + "' AND ownerPerson = " + credential.getUser().getpID();
            }
            else if(office != null)
            {
                where += " AND suc = '" + office.getCode() + "'";
            }
            else if(department != null)
            {
                where += " AND department = '" + department.code + "'";
            }
            else if(personal)
            {
                where += " AND ownerPerson = " + credential.getUser().getpID();
            }
            else
            {
                ;
            }
        }
        else if(scope.scope.equals("S"))
        {
            where += " AND (suc = '" + credential.getSuc() + "' OR suc IS NULL)" ;
            
            if(department != null && personal)
            {
                where += " AND department = '" + department.code + "' AND ownerPerson = " + credential.getUser().getAlias();
            }
            else if(department != null)
            {
                where += " AND department = '" + department.code + "'";
            }
            else if(personal)
            {
                where += " AND ownerPerson = " + credential.getUser().getpID();
            }
            else
            {
                ;
            }
        }
        else if(scope.scope.equals("D"))
        {
            where += " AND (suc = '" + credential.getSuc() + "' OR suc IS NULL) AND (department = '" + credential.getUser().getDepartment() + "' OR department IS NULL)";
                        
            if(personal)
            {
                where += " AND ownerPerson = " + credential.getUser().getpID();
            }
            else
            {
                ;
            }
        }
        
        if(this.lengh > 0 && this.qSearch.length() > 0)
        {
            where += " AND ( ";
            for(int i = 1; i < fieldsQS.length; i++)
            {
                where += fieldsQS[i] + " LIKE '%" + this.qSearch + "%' OR ";
            }
            where += fieldsQS[0] + " LIKE '%" + this.qSearch + "%' )";            
        }
        //System.out.println("Where : " + where);
        return where;
    }
}
