package javaxt.sql;

//******************************************************************************
//**  Column Class
//******************************************************************************
/**
 *   Used to represent a column in a table.
 *
 ******************************************************************************/

public class Column {
    
    private String Name;
    private String Description;
    private String Type;
    private String Length;
    private String IsRequired;
    private boolean isPrimaryKey = false;
    private boolean isForeignKey = false;
    private Table Table;
    private Key ForeignKey;

    
    protected Column(java.sql.ResultSet rs, Table table) throws Exception {

        this.Name = rs.getString("COLUMN_NAME");
        this.Type = rs.getString("TYPE_NAME"); ////DATA_TYPE?
        this.Length = rs.getString("COLUMN_SIZE");
        this.Description = rs.getString("REMARKS");
        this.IsRequired = rs.getString("IS_NULLABLE");
        this.Table = table;
        
    }
    
    

    public String getName(){return Name;}
    
    public String getType(){return Type;}

    public Table getTable(){return Table;}

    public String getDescription(){
        if (Description==null) return "";
        else return Description;
    }

    public int getLength(){
        if (Length==null) return 0;
        else return Integer.valueOf(Length).intValue();
    }

    public boolean isRequired(){
        if (IsRequired==null) return true;
        else{
            IsRequired = IsRequired.trim();
            if (IsRequired.equalsIgnoreCase("NO")){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean isPrimaryKey(){
        return isPrimaryKey;
    }
    
    protected void setIsPrimaryKey(boolean isPrimaryKey){
        this.isPrimaryKey = isPrimaryKey;
    }
    

    public boolean isForeignKey(){
        return isForeignKey;
    }

    
    protected void setForeignKey(Key key){
        this.ForeignKey = key;
        this.isForeignKey = true;
    }
    
    
    public Key getForeignKey(){
        return ForeignKey;
    }

    public String toString(){
        return Table.getName() + "." + Name;
    }

}