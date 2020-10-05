package javaxt.sql;

//******************************************************************************
//**  Function Class
//******************************************************************************
/**
 *  Used to encapsulate SQL functions for database inserts and updates. This
 *  class is used in conjunction with the Recordset class. Example:
 *  <pre>rs.setValue("LAST_UPDATE", new javaxt.sql.Function("NOW()"));</pre>
 *
 *  Functions can be parameterized for more efficient inserts and updates. This
 *  is especially important for batch inserts. For example, instead of this:
 *  <pre>rs.setValue("DATEDIFF_TEST", new javaxt.sql.Function("DATEDIFF(year, '2012/04/28', '2014/04/28')"));</pre>
 *  The function can be parameterized like this:
 *  <pre>rs.setValue("DATEDIFF_TEST", new javaxt.sql.Function("DATEDIFF(year, ?, ?)", new Object[]{"2012/04/28", "2014/04/28"}));</pre>
 *
 ******************************************************************************/

public class Function {

    private String function;
    private Object[] values;


    public Function(String function, Object[] values){
        this.function = function;
        this.values = values;
    }

    public Function(String function){
        this.function = function;
    }

    public String getFunction(){
        return function;
    }

    public boolean hasValues(){
        if (values!=null){
            return (values.length>0);
        }
        return false;
    }

    public Object[] getValues(){
        return values;
    }
    
    public String toString(){
        return function;
    }
}