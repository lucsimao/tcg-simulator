package javaxt.sql;

//******************************************************************************
//**  Connection Class
//******************************************************************************
/**
 *   Used to connect to a database via JDBC
 *
 ******************************************************************************/

public class Connection {    

    
    private java.sql.Connection Conn = null;
    private long Speed;
    private Database database;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
    
    public Connection(){
    }

    public Connection(java.sql.Connection conn){
        open(conn);
    }

        
  //**************************************************************************
  //** isOpen
  //**************************************************************************
  /** Used to determine whether the connection is open. */
    
    public boolean isOpen(){
        return !isClosed();
    } 

  //**************************************************************************
  //** isClosed
  //**************************************************************************
  /** Used to determine whether the connection is closed. */

    public boolean isClosed(){
        try{
            return Conn.isClosed();
        }
        catch(Exception e){
            return true;
        }
    }
    
  //**************************************************************************
  //** getConnectionSpeed
  //**************************************************************************
  /** Used to retrieve the time it took to open the database connection 
   * (in milliseconds) 
   */
    
    public long getConnectionSpeed(){
        return Speed;
    }

   
  //**************************************************************************
  //** getConnection
  //**************************************************************************
  /** Used to retrieve the java.sql.Connection for this Connection */
    
    public java.sql.Connection getConnection(){
        return Conn;
    }


    
  //**************************************************************************
  //** Open
  //**************************************************************************
  /** Used to open a connection to the database.
   *
   *  @param ConnectionString A jdbc connection string/url. All connection URLs
   *  have the following form:
   *  <pre> jdbc:[dbVendor]://[dbName][propertyList] </pre>
   *
   *  Example:
   *  <pre> jdbc:derby://temp/my.db;user=admin;password=mypassword </pre>
   */
    
    public boolean open(String ConnectionString) throws java.sql.SQLException {
        return open(new Database(ConnectionString));
    }

    
    
  //**************************************************************************
  //** Open
  //**************************************************************************
  /** Used to open a connection to the database. */
    
    public boolean open(Database database) throws java.sql.SQLException {
                
        long startTime = java.util.Calendar.getInstance().getTimeInMillis();
        this.database = database;
        boolean isClosed = true;
        

      //Load JDBC Driver
        java.sql.Driver Driver = (java.sql.Driver) database.getDriver().load();


        //if (Conn!=null && Conn.isOpen()) Conn.close();


        String url = database.getURL();
        String username = database.getUserName();
        String password = database.getPassword();

        java.util.Properties properties = database.getProperties();
        if (properties==null) properties = new java.util.Properties();
        if (username!=null){
            properties.put("user", username);
            properties.put("password", password);
        }


        Conn = Driver.connect(url, properties);
        

        isClosed = Conn.isClosed();

        
        long endTime = java.util.Calendar.getInstance().getTimeInMillis();
        Speed = endTime-startTime;
        return isClosed;
    }


  //**************************************************************************
  //** Open
  //**************************************************************************
  /** Used to open a connection to the database using a JDBC Connection. This
   *  is particularly useful when using JDBC connection pools.
   */
    public boolean open(java.sql.Connection conn){

        boolean isClosed = true;
        try{
            database = new Database(conn);
            Conn = conn;
            isClosed = Conn.isClosed();
        }
        catch(Exception e){
            //System.out.println("Failed");
            //System.out.println(database.getDriver().getVendor() + " ERROR: " + e.toString());
            isClosed = true;
        }

        Speed = 0;
        return isClosed;
    }


    
  //**************************************************************************
  //** Close
  //**************************************************************************
  /** Used to close a connection to the database, freeing up connections */
    
    public void close(){
        try{Conn.close();}
        catch(Exception e){
            //e.printStackTrace();
        }
    }
    
    
  //**************************************************************************
  //** Execute
  //**************************************************************************
  /** Used to execute a prepared sql statement (e.g. "delete from my_table").
   */
    public void execute(String sql) throws java.sql.SQLException {
        java.sql.PreparedStatement preparedStmt = Conn.prepareStatement(sql);
        preparedStmt.execute();
        preparedStmt.close();
        preparedStmt = null;
    } 
    
    
  //**************************************************************************
  //** Commit
  //**************************************************************************
  /** Used to explicitely commit changes made to the database. */
    
    public void commit() throws java.sql.SQLException {
        execute("COMMIT");
    }
    
  //**************************************************************************
  //** getDatabase
  //**************************************************************************
  /** Used to return database information associated with this connection. */
    
    public Database getDatabase(){
        return database;
    }

}