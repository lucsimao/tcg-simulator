package javaxt.sql;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;

//******************************************************************************
//**  Database
//******************************************************************************
/**
 *   Object used to represent all of the information required to connect to a
 *   database. 
 *
 ******************************************************************************/

public class Database implements Cloneable {
    
    private String name; //name of the catalog used to store tables, views, etc.
    private String host;
    private Integer port = -1;
    private String username;
    private String password;
    private Driver driver; 
    //private String props;
    private java.util.Properties properties;
    private String querystring;
    private ConnectionPoolDataSource ConnectionPoolDataSource;
    private static final Class<?>[] stringType = { String.class };
    private static final Class<?>[] integerType = { Integer.TYPE };
    
    
  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class. Note that you will need to set the
   *  name, host, port, username, password, and driver in order to create a
   *  connection to the database.
   */
    public Database(){
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class.
   *  @param name Name of the catalog used to store tables, views, etc.
   *  @param host Server name or IP address.
   *  @param port Port number used to establish connections to the database.
   *  @param username Username used to log into the database
   *  @param password Password used to log into the database
   */
    public Database(String name, String host, int port, String username, String password, Driver driver) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.driver = driver;
        //this.url = this.getURL();
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /**  Creates a new instance of Database using a jdbc connection. */
    
    public Database(java.sql.Connection conn){
        try{
            DatabaseMetaData dbmd = conn.getMetaData();
            this.name = conn.getCatalog();
            this.username = dbmd.getUserName();
            parseURL(dbmd.getURL());
            //dbmd.getDriverName();
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }


    
  //**************************************************************************
  //** Constructor 
  //**************************************************************************
  /** Creates a new instance of a Database using a jdbc connection string.
   *  Username and password may be appended to the end of the connection string
   *  in the property list.
   *  @param connStr A jdbc connection string/url. All connection URLs
   *  have the following form:
   *  <pre> jdbc:[dbVendor]://[dbName][propertyList] </pre>
   *
   *  Examples:
   *  <p>Derby:</p>
   *  <pre> jdbc:derby://temp/my.db;user=admin;password=mypassword </pre>
   *  <p>SQL Server:</p>
   *  <pre> jdbc:sqlserver://192.168.0.80;databaseName=master;user=admin;password=mypassword </pre>
   */
    public Database(String connStr){
        parseURL(connStr);
    }
    

  //**************************************************************************
  //** parseURL 
  //**************************************************************************
  /** Used to parse a JDBC connection string (url)
   */
    private void parseURL(String connStr){

        String[] arrConnStr = connStr.split(";");
        String jdbcURL = arrConnStr[0];

      //Update jdbc url for URL parser
        if (!jdbcURL.contains("//")){
            String protocol = jdbcURL.substring(jdbcURL.indexOf(":")+1);

            protocol = "jdbc:" + protocol.substring(0, protocol.indexOf(":")) + ":";
            String path = jdbcURL.substring(protocol.length());
            jdbcURL = protocol + "//" + path;
        }

      //Parse url and extract connection parameters
        javaxt.utils.URL url = new javaxt.utils.URL(jdbcURL);
        host = url.getHost();
        port = url.getPort();
        driver = Driver.findDriver(url.getProtocol());
        if (name==null){
            name = url.getPath();
            if (this.name!=null && this.name.startsWith("/")){
                this.name = this.name.substring(1);
            }
        }
        querystring = url.getQueryString();
        if (querystring.length()==0) querystring = null;


      //Extract additional connection parameters
        for (int i=1; i<arrConnStr.length; i++) {

            String[] arrParams = arrConnStr[i].split("=");
            String paramName = arrParams[0].toLowerCase();
            String paramValue = arrParams[1];

            if (paramName.equals("database")){
                this.name = paramValue;
            }
            else if (paramName.equals("user")){
                this.username = paramValue;
            }
            else if (paramName.equals("password")){
                this.password = paramValue;
            }
            else if (paramName.equalsIgnoreCase("derby.system.home")){
                //if (System.getProperty("derby.system.home")==null)
                System.setProperty("derby.system.home", paramValue);
            }
            else{
              //Extract additional properties
                if (properties==null) properties = new java.util.Properties();
                properties.put(arrParams[0], arrParams[1]);
            }
        }
    }


  //**************************************************************************
  //** setName 
  //**************************************************************************
  /** Sets the name of the catalog used to store tables, views, etc. */

    public void setName(String name){
        this.name = name;
    }


  //**************************************************************************
  //** getName
  //**************************************************************************
  /** Gets the name of the catalog used to store tables, views, etc. */

    public String getName(){
        return name;
    }

    
  //**************************************************************************
  //** setHost
  //**************************************************************************
  /** Used to set the path to the database (server name and port). */

    public void setHost(String host, int port){
        this.host = host;
        this.port = port;
    }


  //**************************************************************************
  //** setHost
  //**************************************************************************
  /** Used to set the path to the database.
   *  @param host Server name/port (e.g. localhost:9080) or a path to a file
   *  (e.g. /temp/firebird.db)
   */
    public void setHost(String host){
        host = host.trim();
        if (host.contains(":")){
            try{
            this.host = host.substring(0, host.indexOf(":"));
            this.port = Integer.valueOf(host.substring(host.indexOf(":")+1));
            }
            catch(Exception e){
                this.host = host;
            }
        }
        else{
            this.host = host;
        }
    }    
    
    
  //**************************************************************************
  //** getHost
  //**************************************************************************
  /** Returns the name or IP address of the server or a physical path to the 
   *  database file.
   */
    public String getHost(){
        return host;
    }

    
  //**************************************************************************
  //** setPort
  //**************************************************************************
    
    public void setPort(int port){
        this.port = port;
    }
    
    public int getPort(){
        return port;
    }

    
  //**************************************************************************
  //** setDriver
  //**************************************************************************
    
    public void setDriver(Driver driver){
        this.driver = driver;
    }


  //**************************************************************************
  //** setDriver
  //**************************************************************************
  /** Used to find a driver that corresponds to a given vendor name, class
   *  name, or protocol.
   */
    public void setDriver(String driver){ //throw exception?
        this.driver = Driver.findDriver(driver);
    }
    

    public void setDriver(java.sql.Driver driver){
        this.driver = new Driver(driver);
    }

    public void setDriver(Class driver){
        this.driver = Driver.findDriver(driver.getCanonicalName());
    }
    
    
  //**************************************************************************
  //** getDriver
  //**************************************************************************
    
    public Driver getDriver(){
        return driver;
    }

    
  //**************************************************************************
  //** setUserName
  //**************************************************************************
    
    public void setUserName(String username){
        this.username = username;
    }
    
    public String getUserName(){
        return username;
    }
    
  //**************************************************************************
  //** setPassword
  //**************************************************************************
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getPassword(){
        return password;
    }

    
    public void setProperties(java.util.Properties properties){
        this.properties = properties;
    }

    public java.util.Properties getProperties(){
        return properties;
    }
    
  //**************************************************************************
  //** getConnectionString
  //**************************************************************************
  /** Returns a JDBC connection string used to connect to the database.
   *  Username and password are appended to the end of the url.
   */
    public String getConnectionString(){
                    
      //Set User Info
        String path = getURL();
        if (username!=null) path += ";user=" + username;
        if (password!=null) path += ";password=" + password;      
        return path;
        
    }

    
  //**************************************************************************
  //** getURL
  //**************************************************************************
  /** Used to construct a JDBC connection string
   */
    protected String getURL(){

      //Update Server Name
        String server = host;
        if (port!=null && port>0) server += ":" + port;
        String vendor = driver.getVendor();
        if (vendor==null) vendor = "";
        if (vendor.equals("Derby") || vendor.equals("SQLite")){
            server = ":" + server;
        }


      //Update Initial Catalog
        String database = "";
        if (name!=null) {

            if (name.trim().length()>0){

                if (vendor.equals("SQLServer")){
                    database = ";databaseName=" + name;
                }
                else if (vendor.equals("Derby")){
                    database = ";databaseName=" + name;
                }
                else{
                    database = "/" + name;
                }

            }
        }

      //Append querystring as needed
        if (querystring!=null) database += "?" + querystring;


      //Set Path
        String path = "";
        path = driver.getProtocol() + "://";
        
                
      //Special case for Sybase
        if (vendor.equals("Sybase")){
            if (path.toLowerCase().contains((CharSequence) "tds:")==false){
                path = driver.getProtocol() + "Tds:"; 
            }
        }
        else if (vendor.equals("Derby") || vendor.equals("SQLite")){
            path = driver.getProtocol();
        }



      //Set properties
        StringBuffer props = new StringBuffer();
        if (properties!=null){
            java.util.Iterator it = properties.keySet().iterator();
            while (it.hasNext()){
                Object key = it.next();
                Object val = properties.get(key);
                props.append(";" + key + "=" + val);
            }
        }



      //Assemble Connection String
        return path + server + database; // + props.toString()
    }
    
    
  //**************************************************************************
  //** getConnection
  //**************************************************************************
  /** Used to open a connection to the database. Note the the connection will
   *  need to be closed afterwards.
   */     
    public Connection getConnection() throws SQLException {
        Connection connection = new Connection();
        connection.open(this);
        return connection;
    }

    
  //**************************************************************************
  //** setConnectionPoolDataSource
  //**************************************************************************
  /** Used to set the ConnectionPoolDataSource for the database. Typically,
   *  the getConnectionPoolDataSource() method is used to create a
   *  ConnectionPoolDataSource. This method allows you to specify a different
   *  ConnectionPoolDataSource.
   */
    public void setConnectionPoolDataSource(ConnectionPoolDataSource dataSource){
        this.ConnectionPoolDataSource = dataSource;
    }


  //**************************************************************************
  //** getConnectionPoolDataSource
  //**************************************************************************
  /** Used to instantiate a ConnectionPoolDataSource for the database. The
   *  ConnectionPoolDataSource is typically used to create a JDBC Connection
   *  Pool.
   */
    public ConnectionPoolDataSource getConnectionPoolDataSource() throws SQLException {

        if (ConnectionPoolDataSource!=null) return ConnectionPoolDataSource;


        if (driver==null) throw new SQLException(
            "Failed to create a ConnectionPoolDataSource. Please specify a driver.");

        String className = null;
        java.util.HashMap<String, Object> methods = new java.util.HashMap<String, Object>();


        if (driver.equals("sqlite")){

            className = "org.sqlite.SQLiteConnectionPoolDataSource";
            methods.put("setUrl", "jdbc:sqlite:" + host);

            /*
            javax.sql.DataSource sqliteDS = new DataSource();
            sqliteDS.setURL ("jdbc:sqlite://" + name);
            dataSource = sqliteDS;
            */
        }
        else if (driver.equals("derby")){

            className = ("org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource");

            methods.put("setDatabaseName", host);
            methods.put("setCreateDatabase", "create");

        }
        else if (driver.equals("h2")){

            className = ("org.h2.jdbcx.JdbcDataSource");

            methods.put("setURL", "jdbc:h2:file:" + host);
            methods.put("setUser", username);
            methods.put("setPassword", password);

        }
        else if (driver.equals("sqlserver")){ //mssql
        
            className = ("com.microsoft.sqlserver.jdbc.SQLServerXADataSource");

            methods.put("setDatabaseName", name);
            methods.put("setServerName", host);
            methods.put("setUser", username);
            methods.put("setPassword", password);

        }
        else if (driver.equals("postgresql")){ //pgsql

            className = ("org.postgresql.ds.PGConnectionPoolDataSource");

            methods.put("setDatabaseName", name);
            methods.put("setServerName", host);
            methods.put("setPortNumber", port);
            methods.put("setUser", username);
            methods.put("setPassword", password);

        }
        else if (driver.equals("mysql")){

            className = ("com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource");

            methods.put("setDatabaseName", name);
            methods.put("setServerName", host);
            methods.put("setPortNumber", port); //setPort?
            methods.put("setUser", username);
            methods.put("setPassword", password);

        }
        else if (driver.equals("oracle")){

            String connDriver = "thin";
            String connService = "";


            className = ("oracle.jdbc.pool.OracleConnectionPoolDataSource");

            methods.put("setDriverType", connDriver);
            methods.put("setServerName", host);
            methods.put("setPortNumber", port);
            methods.put("setServiceName", connService);
            methods.put("setUser", username);
            methods.put("setPassword", password);
        }
        else if (driver.equals("jtds")){

            className = ("net.sourceforge.jtds.jdbcx.JtdsDataSource");

            methods.put("setDatabaseName", name);
            methods.put("setServerName", host);
            methods.put("setUser", username);
            methods.put("setPassword", password);

        }

      //Instantiate the ConnectionPoolDataSource
        if (className!=null){
            try{
                Class classToLoad = Class.forName(className);
                Object instance = classToLoad.newInstance();

                java.util.Iterator<String> it = methods.keySet().iterator();
                while (it.hasNext()){
                    String methodName = it.next();
                    Object parameter = methods.get(methodName);
                    if (parameter!=null){
                        java.lang.reflect.Method method = null;
                        if (parameter instanceof String)
                            method = classToLoad.getMethod(methodName, stringType);
                        else if (parameter instanceof Integer)
                            method = classToLoad.getMethod(methodName, integerType);

                        if (method!=null) method.invoke(instance, new Object[] { parameter });
                    }
                }
                ConnectionPoolDataSource = (ConnectionPoolDataSource) instance;
                return ConnectionPoolDataSource;
            }
            catch(Exception e){
                throw new SQLException("Failed to instantiate the ConnectionPoolDataSource.", e);
            }

        }

        throw new SQLException("Failed to find a suitable ConnectionPoolDataSource.");
    }


  //**************************************************************************
  //** getTables
  //**************************************************************************
  /** Used to retrieve an array of tables found in this database. 
   */    
    public static Table[] getTables(Connection conn){
        try{
            java.util.ArrayList<Table> tables = new java.util.ArrayList<Table>();
            DatabaseMetaData dbmd = conn.getConnection().getMetaData();
            ResultSet rs = dbmd.getTables(null,null,null,new String[]{"TABLE"});
            while (rs.next()) {
                tables.add(new Table(rs, dbmd));  
            }
            rs.close();
            rs = null;
            return tables.toArray(new Table[tables.size()]);
        }
        catch(Exception e){
            return null;
        }
    }


  //**************************************************************************
  //** getCatalogs
  //**************************************************************************
  /**  Used to retrieve a list of available databases found on this server.
   */
    public static String[] getCatalogs(Connection conn){
        try{
            java.util.TreeSet<String> catalogs = new java.util.TreeSet<String>();
            DatabaseMetaData dbmd = conn.getConnection().getMetaData();
            ResultSet rs  = dbmd.getCatalogs();
            while (rs.next()) {
                catalogs.add(rs.getString(1));
            }
            rs.close();
            return catalogs.toArray(new String[catalogs.size()]);
        }
        catch(Exception e){
            return null;
        }
    }

    
  //**************************************************************************
  //** getReservedKeywords
  //**************************************************************************
  /**  Used to retrieve a list of reserved keywords for a given database.
   */
    public static String[] getReservedKeywords(Connection conn){
        javaxt.sql.Driver driver = conn.getDatabase().getDriver();
        if (driver==null) driver = new Driver("","","");

        if (driver.equals("Firebird")){
            return fbKeywords;
        }
        else if (driver.equals("SQLServer")){
            return msKeywords;
        }
        else if (driver.equals("PostgreSQL")){
          
          //Try to get reserved keywords from the database. Note that in PostgreSQL
          //"non-reserved" keywords are key words that are explicitly known to  
          //the parser but are allowed as column or table names. Therefore, we  
          //will ignore "non-reserved" keywords from our query.
            if (pgKeywords==null){
                java.util.HashSet<String> arr = new java.util.HashSet<String>();
                java.sql.ResultSet rs = null;
                java.sql.Statement stmt = null;
                try{
                    stmt = conn.getConnection().createStatement(rs.TYPE_FORWARD_ONLY, rs.CONCUR_READ_ONLY, rs.FETCH_FORWARD);
                    rs = stmt.executeQuery("select word from pg_get_keywords() where catcode='R'");
                    while(rs.next()){
                        arr.add(rs.getString(1));
                    }
                    rs.close();
                    stmt.close();
                }
                catch(java.sql.SQLException e){
                    e.printStackTrace();
                    if (rs!=null) try{ rs.close(); } catch(Exception ex){}
                    if (stmt!=null) try{ stmt.close(); } catch(Exception ex){}
                }
                String[] keywords = new String[arr.size()];
                int i=0;
                java.util.Iterator<String> it = arr.iterator();
                while (it.hasNext()){
                    keywords[i] = it.next();
                    i++;
                }
                pgKeywords = keywords;
            }
            
            return pgKeywords;
        }
        else{
            return ansiKeywords;
        }
    }
    

    public static void displayDbProperties(Connection conn){

        java.sql.DatabaseMetaData dm = null;
        java.sql.ResultSet rs = null;
        try{
            if (conn!=null){
                dm = conn.getConnection().getMetaData();
                System.out.println("Driver Information");
                System.out.println("\tDriver Name: "+ dm.getDriverName());
                System.out.println("\tDriver Version: "+ dm.getDriverVersion ());
                System.out.println("\nDatabase Information ");
                System.out.println("\tDatabase Name: "+ dm.getDatabaseProductName());
                System.out.println("\tDatabase Version: "+ dm.getDatabaseProductVersion());
                System.out.println("Avalilable Catalogs ");

                rs = dm.getCatalogs();
                while(rs.next()){
                     System.out.println("\tcatalog: "+ rs.getString(1));
                }
                rs.close();
                rs = null;

            }
            else
               System.out.println("Error: No active Connection");
        }catch(Exception e){
           e.printStackTrace();
        }
        dm=null;
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /**  Returns database connection information encapsulated by this class.
   */
    public String toString(){
        StringBuffer str = new StringBuffer();
        str.append("Name: " + name + "\r\n");
        str.append("Host: " + host + "\r\n");
        str.append("Port: " + port + "\r\n");
        str.append("UserName: " + username + "\r\n");
        str.append("Driver: " + driver + "\r\n");
        str.append("URL: " + getURL() + "\r\n");
        str.append("ConnStr: " + this.getConnectionString());
        return str.toString();
    }
    
    
  //**************************************************************************
  //** clone
  //**************************************************************************
    public Database clone(){
        Database db = new Database(name, host, port, username, password, driver);
        if (properties!=null) db.properties = (java.util.Properties) properties.clone();
        db.querystring = querystring;
        return db;
    }
    
    
  /** Firebird reserved keywords. */
    private static final String[] fbKeywords = new String[]{
        "ADD","ADMIN","ALL","ALTER","AND","ANY","AS","AT","AVG","BEGIN","BETWEEN",
        "BIGINT","BIT_LENGTH","BLOB","BOTH","BY","CASE","CAST","CHAR","CHAR_LENGTH",
        "CHARACTER","CHARACTER_LENGTH","CHECK","CLOSE","COLLATE","COLUMN","COMMIT",
        "CONNECT","CONSTRAINT","COUNT","CREATE","CROSS","CURRENT","CURRENT_CONNECTION",
        "CURRENT_DATE","CURRENT_ROLE","CURRENT_TIME","CURRENT_TIMESTAMP",
        "CURRENT_TRANSACTION","CURRENT_USER","CURSOR","DATE","DAY","DEC","DECIMAL",
        "DECLARE","DEFAULT","DELETE","DISCONNECT","DISTINCT","DOUBLE","DROP","ELSE",
        "END","ESCAPE","EXECUTE","EXISTS","EXTERNAL","EXTRACT","FETCH","FILTER",
        "FLOAT","FOR","FOREIGN","FROM","FULL","FUNCTION","GDSCODE","GLOBAL","GRANT",
        "GROUP","HAVING","HOUR","IN","INDEX","INNER","INSENSITIVE","INSERT","INT",
        "INTEGER","INTO","IS","JOIN","LEADING","LEFT","LIKE","LONG","LOWER","MAX",
        "MAXIMUM_SEGMENT","MERGE","MIN","MINUTE","MONTH","NATIONAL","NATURAL","NCHAR",
        "NO","NOT","NULL","NUMERIC","OCTET_LENGTH","OF","ON","ONLY","OPEN","OR",
        "ORDER","OUTER","PARAMETER","PLAN","POSITION","POST_EVENT","PRECISION",
        "PRIMARY","PROCEDURE","RDB$DB_KEY","REAL","RECORD_VERSION","RECREATE",
        "RECURSIVE","REFERENCES","RELEASE","RETURNING_VALUES","RETURNS","REVOKE",
        "RIGHT","ROLLBACK","ROW_COUNT","ROWS","SAVEPOINT","SECOND","SELECT","SENSITIVE",
        "SET","SIMILAR","SMALLINT","SOME","SQLCODE","SQLSTATE","START","SUM","TABLE",
        "THEN","TIME","TIMESTAMP","TO","TRAILING","TRIGGER","TRIM","UNION","UNIQUE",
        "UPDATE","UPPER","USER","USING","VALUE","VALUES","VARCHAR","VARIABLE","VARYING",
        "VIEW","WHEN","WHERE","WHILE","WITH","YEAR"
    };
    
    
  /** SQLServer reserved keywords. Source:
   *  https://msdn.microsoft.com/en-us/library/ms189822.aspx
   */
    private static final String[] msKeywords = new String[]{
        "ADD","ALL","ALTER","AND","ANY","AS","ASC","AUTHORIZATION","BACKUP","BEGIN",
        "BETWEEN","BREAK","BROWSE","BULK","BY","CASCADE","CASE","CHECK","CHECKPOINT",
        "CLOSE","CLUSTERED","COALESCE","COLLATE","COLUMN","COMMIT","COMPUTE",
        "CONSTRAINT","CONTAINS","CONTAINSTABLE","CONTINUE","CONVERT","CREATE","CROSS",
        "CURRENT","CURRENT_DATE","CURRENT_TIME","CURRENT_TIMESTAMP","CURRENT_USER",
        "CURSOR","DATABASE","DBCC","DEALLOCATE","DECLARE","DEFAULT","DELETE","DENY",
        "DESC","DISK","DISTINCT","DISTRIBUTED","DOUBLE","DROP","DUMP","ELSE","END",
        "ERRLVL","ESCAPE","EXCEPT","EXEC","EXECUTE","EXISTS","EXIT","EXTERNAL",
        "FETCH","FILE","FILLFACTOR","FOR","FOREIGN","FREETEXT","FREETEXTTABLE",
        "FROM","FULL","FUNCTION","GOTO","GRANT","GROUP","HAVING","HOLDLOCK","IDENTITY",
        "IDENTITY_INSERT","IDENTITYCOL","IF","IN","INDEX","INNER","INSERT","INTERSECT",
        "INTO","IS","JOIN","KEY","KILL","LEFT","LIKE","LINENO","LOAD","MERGE","NATIONAL",
        "NOCHECK","NONCLUSTERED","NOT","NULL","NULLIF","OF","OFF","OFFSETS","ON",
        "OPEN","OPENDATASOURCE","OPENQUERY","OPENROWSET","OPENXML","OPTION","OR",
        "ORDER","OUTER","OVER","PERCENT","PIVOT","PLAN","PRECISION","PRIMARY","PRINT",
        "PROC","PROCEDURE","PUBLIC","RAISERROR","READ","READTEXT","RECONFIGURE",
        "REFERENCES","REPLICATION","RESTORE","RESTRICT","RETURN","REVERT","REVOKE",
        "RIGHT","ROLLBACK","ROWCOUNT","ROWGUIDCOL","RULE","SAVE","SCHEMA","SECURITYAUDIT",
        "SELECT","SEMANTICKEYPHRASETABLE","SEMANTICSIMILARITYDETAILSTABLE",
        "SEMANTICSIMILARITYTABLE","SESSION_USER","SET","SETUSER","SHUTDOWN","SOME",
        "STATISTICS","SYSTEM_USER","TABLE","TABLESAMPLE","TEXTSIZE","THEN","TO",
        "TOP","TRAN","TRANSACTION","TRIGGER","TRUNCATE","TRY_CONVERT","TSEQUAL",
        "UNION","UNIQUE","UNPIVOT","UPDATE","UPDATETEXT","USE","USER","VALUES",
        "VARYING","VIEW","WAITFOR","WHEN","WHERE","WHILE","WITH","WITHIN GROUP",
        "WRITETEXT"
    };

    
  /** PostgreSQL reserved keywords. */
    private static String[] pgKeywords = null;


  /** Superset of SQL-92, SQL-99, SQL-2003 reserved keywords. */
    private static String[] ansiKeywords = new String[]{
        "ABSOLUTE","ACTION","ADD","AFTER","ALL","ALLOCATE","ALTER","AND","ANY","ARE",
        "ARRAY","AS","ASC","ASENSITIVE","ASSERTION","ASYMMETRIC","AT","ATOMIC",
        "AUTHORIZATION","AVG","BEFORE","BEGIN","BETWEEN","BIGINT","BINARY","BIT",
        "BIT_LENGTH","BLOB","BOOLEAN","BOTH","BREADTH","BY","CALL","CALLED","CASCADE",
        "CASCADED","CASE","CAST","CATALOG","CHAR","CHAR_LENGTH","CHARACTER","CHARACTER_LENGTH",
        "CHECK","CLOB","CLOSE","COALESCE","COLLATE","COLLATION","COLUMN","COMMIT",
        "CONDITION","CONNECT","CONNECTION","CONSTRAINT","CONSTRAINTS","CONSTRUCTOR",
        "CONTAINS","CONTINUE","CONVERT","CORRESPONDING","COUNT","CREATE","CROSS",
        "CUBE","CURRENT","CURRENT_DATE","CURRENT_DEFAULT_TRANSFORM_GROUP","CURRENT_PATH",
        "CURRENT_ROLE","CURRENT_TIME","CURRENT_TIMESTAMP","CURRENT_TRANSFORM_GROUP_FOR_TYPE",
        "CURRENT_USER","CURSOR","CYCLE","DATA","DATE","DAY","DEALLOCATE","DEC","DECIMAL",
        "DECLARE","DEFAULT","DEFERRABLE","DEFERRED","DELETE","DEPTH","DEREF","DESC",
        "DESCRIBE","DESCRIPTOR","DETERMINISTIC","DIAGNOSTICS","DISCONNECT","DISTINCT",
        "DO","DOMAIN","DOUBLE","DROP","DYNAMIC","EACH","ELEMENT","ELSE","ELSEIF",
        "END","EQUALS","ESCAPE","EXCEPT","EXCEPTION","EXEC","EXECUTE","EXISTS","EXIT",
        "EXTERNAL","EXTRACT","FALSE","FETCH","FILTER","FIRST","FLOAT","FOR","FOREIGN",
        "FOUND","FREE","FROM","FULL","FUNCTION","GENERAL","GET","GLOBAL","GO","GOTO",
        "GRANT","GROUP","GROUPING","HANDLER","HAVING","HOLD","HOUR","IDENTITY","IF",
        "IMMEDIATE","IN","INDICATOR","INITIALLY","INNER","INOUT","INPUT","INSENSITIVE",
        "INSERT","INT","INTEGER","INTERSECT","INTERVAL","INTO","IS","ISOLATION",
        "ITERATE","JOIN","KEY","LANGUAGE","LARGE","LAST","LATERAL","LEADING","LEAVE",
        "LEFT","LEVEL","LIKE","LOCAL","LOCALTIME","LOCALTIMESTAMP","LOCATOR","LOOP",
        "LOWER","MAP","MATCH","MAX","MEMBER","MERGE","METHOD","MIN","MINUTE","MODIFIES",
        "MODULE","MONTH","MULTISET","NAMES","NATIONAL","NATURAL","NCHAR","NCLOB",
        "NEW","NEXT","NO","NONE","NOT","NULL","NULLIF","NUMERIC","OBJECT","OCTET_LENGTH",
        "OF","OLD","ON","ONLY","OPEN","OPTION","OR","ORDER","ORDINALITY","OUT","OUTER",
        "OUTPUT","OVER","OVERLAPS","PAD","PARAMETER","PARTIAL","PARTITION","PATH",
        "POSITION","PRECISION","PREPARE","PRESERVE","PRIMARY","PRIOR","PRIVILEGES",
        "PROCEDURE","PUBLIC","RANGE","READ","READS","REAL","RECURSIVE","REF","REFERENCES",
        "REFERENCING","RELATIVE","RELEASE","REPEAT","RESIGNAL","RESTRICT","RESULT",
        "RETURN","RETURNS","REVOKE","RIGHT","ROLE","ROLLBACK","ROLLUP","ROUTINE",
        "ROW","ROWS","SAVEPOINT","SCHEMA","SCOPE","SCROLL","SEARCH","SECOND","SECTION",
        "SELECT","SENSITIVE","SESSION","SESSION_USER","SET","SETS","SIGNAL","SIMILAR",
        "SIZE","SMALLINT","SOME","SPACE","SPECIFIC","SPECIFICTYPE","SQL","SQLCODE",
        "SQLERROR","SQLEXCEPTION","SQLSTATE","SQLWARNING","START","STATE","STATIC",
        "SUBMULTISET","SUBSTRING","SUM","SYMMETRIC","SYSTEM","SYSTEM_USER","TABLE",
        "TABLESAMPLE","TEMPORARY","THEN","TIME","TIMESTAMP","TIMEZONE_HOUR","TIMEZONE_MINUTE",
        "TO","TRAILING","TRANSACTION","TRANSLATE","TRANSLATION","TREAT","TRIGGER",
        "TRIM","TRUE","UNDER","UNDO","UNION","UNIQUE","UNKNOWN","UNNEST","UNTIL",
        "UPDATE","UPPER","USAGE","USER","USING","VALUE","VALUES","VARCHAR","VARYING",
        "VIEW","WHEN","WHENEVER","WHERE","WHILE","WINDOW","WITH","WITHIN","WITHOUT",
        "WORK","WRITE","YEAR","ZONE"
    };
    
}