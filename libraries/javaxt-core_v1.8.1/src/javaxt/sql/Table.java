package javaxt.sql;

//******************************************************************************
//**  Table Class
//******************************************************************************
/**
 *   Used to represent a table in the database.
 *
 ******************************************************************************/

public class Table implements Comparable {
    
    private java.sql.DatabaseMetaData dbmd = null;
    private String Name;
    private String Description;
    private String Schema;
    private String Catalog;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Used to instantiate this class. */

    protected Table(java.sql.ResultSet rs, java.sql.DatabaseMetaData dbmd){
        try{
            Name = rs.getString("TABLE_NAME");
            Description = rs.getString("REMARKS");
            Catalog = rs.getString("TABLE_CAT");
            Schema = rs.getString("TABLE_SCHEM");
            this.dbmd = dbmd;
        }
        catch(Exception e){
        }
    }
    
    private Table(){
    }


  //**************************************************************************
  //** getName
  //**************************************************************************
  /** Returns the name of this table.
   */
    public String getName(){return Name;}
    public String getDescription(){return Description;}
    public String getSchema(){return Schema;}
    public String getCatalog(){return Catalog;}


  //**************************************************************************
  //** getColumns
  //**************************************************************************
  /** Returns a list of columns in this table. Returns null if no columns are
   *  found.
   */
    public Column[] getColumns(){
        try{
            java.util.ArrayList<Column> columns = new java.util.ArrayList<Column>();
            Key[] Keys = getPrimaryKeys();
            Key[] FKeys = getForeignKeys();

            java.sql.ResultSet rs = dbmd.getColumns(Catalog, Schema, Name, null);
            while (rs.next()) {

              //Create Column
                Column column = new Column(rs, this);

                
              //Set Primary Key
                if (Keys!=null){
                    for (int i=0; i<Keys.length; i++){
                         if (column.getName().equals(Keys[i].Name)){
                             column.setIsPrimaryKey(true);
                         }
                    }
                }

              //Set Foreign Key
                if (FKeys!=null){
                    for (int i=0; i<FKeys.length; i++){
                         if (column.getName().equals(FKeys[i].Name)){
                             column.setForeignKey(FKeys[i]);
                         }
                    }
                }
                
              //Add Column to the Array
                columns.add(column);
            }
            
            rs.close();
            
            return columns.toArray(new Column[columns.size()]);
        }
        catch(Exception e){
            return null;
        }
    }


  //**************************************************************************
  //** getPrimaryKeys
  //**************************************************************************
  /** Returns a list of primary keys in this table. Usually there is only
   *  one primary key per table, but some vendors do support multiple keys per 
   *  table.
   */
    public Key[] getPrimaryKeys(){
        try{
            java.util.ArrayList<Key> keys = new java.util.ArrayList<Key>();
            java.sql.ResultSet rs = dbmd.getPrimaryKeys(Catalog, Schema, Name);
            while (rs.next()) {
                Key key = new Key();
                key.Name = rs.getString("PK_NAME");
                key.Table = new Table();
                key.Table.Name = rs.getString("TABLE_NAME");
                key.Table.Catalog = rs.getString("TABLE_CAT");
                key.Table.Schema = rs.getString("TABLE_SCHEM");
                key.Column = rs.getString("COLUMN_NAME");
                keys.add(key);
            }            
            rs.close();
            rs = null;
            return keys.toArray(new Key[keys.size()]);
        }
        catch(Exception e){
            return null;
        }
    }


  //**************************************************************************
  //** getForeignKeys
  //**************************************************************************
  /** Returns a list of foreign keys found in this table.
   */
    public Key[] getForeignKeys(){
        try{
            java.util.ArrayList<Key> keys = new java.util.ArrayList<Key>();
            java.sql.ResultSet rs = dbmd.getImportedKeys(Catalog, Schema, Name);
            while (rs.next()) {
                Key Key = new Key();
                Key.Name = rs.getString("FKCOLUMN_NAME");
                Key.Table = new Table();
                Key.Table.Name = rs.getString("PKTABLE_NAME");
                Key.Table.Catalog = rs.getString("PKTABLE_CAT");
                Key.Table.Schema = rs.getString("PKTABLE_SCHEM");
                Key.Column = rs.getString("PKCOLUMN_NAME");
                keys.add(Key);
            }
            rs.close();
            rs = null;
            return keys.toArray(new Key[keys.size()]);
        }
        catch(Exception e){
            return null;
        }
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /**  Returns the table name. */

    public String toString(){
        return getName();
    }

    public int hashCode(){
        return this.toString().hashCode();
    }

    //@Override
    public int compareTo(Object obj){
        if (obj==null) return -1;
        else return -obj.toString().compareTo(toString());
    }
}