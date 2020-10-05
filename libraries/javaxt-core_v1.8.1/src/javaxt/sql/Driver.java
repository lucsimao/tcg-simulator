package javaxt.sql;

//******************************************************************************
//**  Driver
//******************************************************************************
/**
 *   Used to encapsulate basic driver information used to create a database
 *   connection.
 *
 ******************************************************************************/

public class Driver {
    
    private String vendor;
    private String driver;
    private String protocol;
    private java.sql.Driver Driver = null;
    
    
    /** Static list of drivers and corresponding metadata */
    private static Driver[] drivers = new Driver[]{
        new Driver("SQLServer","com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver"),
        new Driver("DB2","com.ibm.db2.jcc.DB2Driver","jdbc:db2"), //"COM.ibm.db2.jdbc.net.DB2Driver"
        new Driver("Sybase","com.sybase.jdbc3.jdbc.SybDriver","jdbc:sybase"),
        new Driver("PostgreSQL","org.postgresql.Driver","jdbc:postgresql"),
        new Driver("Derby","org.apache.derby.jdbc.EmbeddedDriver","jdbc:derby"),
        new Driver("SQLite","org.sqlite.JDBC","jdbc:sqlite"),
        new Driver("Microsoft Access","sun.jdbc.odbc.JdbcOdbcDriver","jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)}"),

      //The rest of these drivers have not been tested
        new Driver("FrontBase", "com.frontbase.jdbc.FBJDriver", "jdbc:FrontBase"),
        new Driver("Informix", "com.informix.jdbc.IfxDriver", "jdbc:informix-sqli"),
        new Driver("Cache", "com.intersys.jdbc.CacheDriver", "jdbc:Cache"),
        new Driver("microsoft", "com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:microsoft"),
        new Driver("Mimer", "com.mimer.jdbc.Driver", "jdbc:mimer"),
        new Driver("MySQL", "com.mysql.jdbc.Driver", "jdbc:mysql"),
        new Driver("Teradata", "com.ncr.teradata.TeraDriver", "jdbc:teradata"),
        new Driver("Pervasive", "com.pervasive.jdbc.v2.Driver", "jdbc:pervasive"),
        new Driver("Pointbase", "com.pointbase.jdbc.jdbcUniversalDriver", "jdbc:pointbase"),
        new Driver("pointbase micro", "com.pointbase.me.jdbc.jdbcDriver", "jdbc:pointbase:micro"),
        new Driver("Daffodil", "in.co.daffodil.db.jdbc.DaffodilDBDriver", "jdbc:daffodil"),
        new Driver("daffodilDB", "in.co.daffodil.db.rmi.RmiDaffodilDBDriver", "jdbc:daffodilDB"),
        new Driver("JTDS", "net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds"), //Open source JDBC 3.0 type 4 driver for Microsoft SQL Server and Sybase ASE
        new Driver("Oracle", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle"),
        new Driver("derby net", "org.apache.derby.jdbc.ClientDriver", "jdbc:derby:net"),
        //new Driver("derby //", "org.apache.derby.jdbc.ClientDriver", "jdbc:derby://"),
        new Driver("Firebird", "org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql"),
        new Driver("H2", "org.h2.Driver", "jdbc:h2"),
        new Driver("HyperSQL", "org.hsqldb.jdbcDriver", "jdbc:hsqldb"),
        new Driver("odbc", "sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc")

    };

    /** Microsoft SQL Server database driver. */
    public static Driver SQLServer = findDriver("SQLServer");

    /** IBM DB2 database driver. */
    public static Driver DB2 = findDriver("DB2");

    /** Sybase ASE database driver. */
    public static Driver Sybase = findDriver("Sybase");

    /** PostgreSQL database driver. */
    public static Driver PostgreSQL = findDriver("PostgreSQL");

    /** Derby database driver. */
    public static Driver Derby = findDriver("Derby");

    /** SQLite database driver. */
    public static Driver SQLite = findDriver("SQLite");

    /** Microsoft Access database driver. */
    public static Driver Access = findDriver("Microsoft Access");

    public static Driver FrontBase = findDriver("FrontBase");
    public static Driver Informix = findDriver("Informix");
    public static Driver Cache = findDriver("Cache");
    public static Driver Mimer = findDriver("Mimer");
    public static Driver MySQL = findDriver("MySQL");
    public static Driver Teradata = findDriver("Teradata");
    public static Driver Pervasive = findDriver("Pervasive");
    public static Driver Pointbase = findDriver("Pointbase");
    //public static Driver pointbase micro = findDriver("pointbase micro");
    public static Driver Daffodil = findDriver("Daffodil");
    //public static Driver daffodilDB = findDriver("daffodilDB");
    public static Driver JTDS = findDriver("JTDS");
    public static Driver Oracle = findDriver("Oracle");
    public static Driver Firebird = findDriver("Firebird");
    public static Driver H2 = findDriver("H2");
    public static Driver HyperSQL = findDriver("HyperSQL");
    public static Driver ODBC = findDriver("odbc");



  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class. Here are some common examples:
   <pre>
    new Driver("SQLServer","com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver");
    new Driver("DB2","com.ibm.db2.jcc.DB2Driver","jdbc:db2");
    new Driver("Sybase","com.sybase.jdbc3.jdbc.SybDriver","jdbc:sybase");
    new Driver("PostgreSQL","org.postgresql.Driver","jdbc:postgresql");
    new Driver("Derby","org.apache.derby.jdbc.EmbeddedDriver","jdbc:derby");
   </pre>
   *  @param vendor Name the database/vendor. This keyword used extensively in
   *  the javaxt.sql.Recordset class to accomodate inconsistant JDBC implementations
   *  between database vendors. As such, please use the names provided in the
   *  examples above when connecting to SQL Server, DB2, Sybase, and PostgreSQL.
   *  Other databases have not been tested and do not require reserved keywords.
   *  @param driver Class name used to create a java.sql.Driver.
   *  @param protocol Protocol used in the jdbc connection string.
   */
    public Driver(String vendor, String driver, String protocol){
        this.vendor = vendor;
        this.driver = driver;
        this.protocol = protocol;
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class with a given java.sql.Driver.
   */
    public Driver(java.sql.Driver driver){

        String className = driver.getClass().getCanonicalName();
        this.driver = className;
        this.Driver = driver;

        for (Driver d : drivers){
            if (d.equals(className)){
                this.vendor = d.vendor;
                this.protocol = d.protocol;
                break;
            }
        }
    }


  //**************************************************************************
  //** getProtocol
  //**************************************************************************
  /** Returns the url protocol used in the jdbc connection string (e.g.
   *  jdbc:sqlserver, jdbc:db2, jdbc:sybase, jdbc:postgresql, jdbc:derby).
   */
    public String getProtocol(){
        return protocol;
    }


  //**************************************************************************
  //** getClassName
  //**************************************************************************
  /** Returns the class name used to create a new java.sql.Driver (e.g.
   *  com.microsoft.sqlserver.jdbc.SQLServerDriver).
   */
    public String getClassName(){
        return driver;
    }


  //**************************************************************************
  //** getVendor
  //**************************************************************************
  /** Returns the name the database/vendor (e.g. SQLServer, DB2, Sybase, etc.)
   */
    public String getVendor(){
        return vendor;
    }


  //**************************************************************************
  //** load
  //**************************************************************************
  /** Used to create a new instance of a java.sql.Driver that corresponds to
   *  the driver class specified in the constructor.
   */
    public java.sql.Driver load() throws java.sql.SQLException{
        if (Driver==null){
            //System.out.print("Loading Driver...");
            try{
                Driver = (java.sql.Driver) Class.forName(driver).newInstance();
            }
            catch(Exception e){
                throw new java.sql.SQLException("Failed to load driver " + driver, e);
            }
            //DriverManager.registerDriver(Driver);
            //System.out.println("Done");
        }
        return Driver;
    }


  //**************************************************************************
  //** equals
  //**************************************************************************
  /** Used to compare drivers by class, protocol, and vendor.
   *  @param obj Accepts either a javaxt.sql.Driver or a String representing
   *  the driver class, protocol, or vendor.
   */
    public boolean equals(Object obj){
        if (obj instanceof Driver){
            Driver driver = (Driver) obj;
            if (driver.getClassName().equalsIgnoreCase(this.getClassName()) &&
                driver.getProtocol().toLowerCase().startsWith(this.getProtocol()) &&
                driver.getVendor().equalsIgnoreCase(this.getVendor())
            ){ 
                 return true;
            }
            else{
                return false;
            }
        }
        else if(obj instanceof java.lang.String){
            String driverName = obj.toString();
            
            //Fetch/update protocol to avoid NPE
            String protocol = this.getProtocol();
            if (protocol==null) protocol = "";
            
            if (driverName.equalsIgnoreCase(this.getClassName()) ||
                driverName.toLowerCase().startsWith(protocol.toLowerCase()) ||
                driverName.equalsIgnoreCase(this.getVendor())
            ){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }


  //**************************************************************************
  //** findDriver
  //**************************************************************************
  /** Used to try to find a driver that corresponds to the vendor name, class
   *  name, or protocol.
   */
    public static Driver findDriver(String driverName){

        for (Driver driver : drivers){
            if (driver.equals(driverName)) return driver;
        }

        if (driverName.contains(".")){
            try{
                return new Driver((java.sql.Driver) Class.forName(driverName).newInstance());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return null;
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /** Returns the name the database/vendor. Same as getVendor()
   */
    public String toString(){
        return this.getVendor();
    }
}