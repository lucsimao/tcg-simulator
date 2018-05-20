package javaxt.sql;

//******************************************************************************
//**  Key Class
//******************************************************************************
/**
 *   Used to represent a unique primary key or a foreign key in a table.
 *
 ******************************************************************************/

public class Key {

    protected String Name;
    protected Table Table;
    protected String Column;
    
    public String getName(){return Name;}
    public Table getTable(){return Table;}
    public String getColumn(){return Column;}
    public String toString(){
        return Table.getName() + "." + Column;
    }
}