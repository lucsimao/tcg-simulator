package javaxt.sql;

//******************************************************************************
//**  SQL Parser
//******************************************************************************
/**
 *   Used to parse and modify SQL Select Statements ("Select * from MyTable").
 *   Other SQL commands are not supported (insert, update, create, etc.). <br>
 *
 *   Note that this implementation is incomplete and needs a lot more testing.
 *   For example, the parser does not yet handle unions, subselects, and
 *   having clauses. Other potential problems include arithmetic operators in
 *   the where clause ("where posting_time + 60 > sysdate").
 *
 ******************************************************************************/

public class Parser {
    
    private java.util.HashMap sql = new java.util.HashMap();

    private static String[] sqlOperators =
    new String[]{"IS NULL","IS NOT NULL","BETWEEN","CONTAINS","LIKE","<>","<=",">=","=","<",">","IN","MATCHES","SOME","NOT EXISTS","EXISTS"};
   
    private static String[] sqlKeywords = 
    new String[]{"SELECT","FROM","WHERE","ORDER BY","GROUP BY","HAVING"};

    private static String[] joinTypes = new String[]{
      //Order is important!
        "RIGHT OUTER JOIN",
        "RIGHT INNER JOIN",
        "FULL OUTER JOIN",
        "LEFT OUTER JOIN",
        "LEFT INNER JOIN",
        "NATURAL JOIN",
        "RIGHT JOIN",
        "INNER JOIN",
        "OUTER JOIN",
        "CROSS JOIN",
        "LEFT JOIN",
        "JOIN"
    };

    private SelectStatement[] selectStatements = null;
    private WhereStatement[] whereStatements = null;
    private OrderByStatement[] orderByStatements = null;
    private GroupByStatement[] groupByStatements = null;
    private FromStatement fromStatement = null;
    
    public String getSelectString() { return (String)sql.get("SELECT"); }
    public String getFromString()   { return (String)sql.get("FROM"); }
    public String getWhereString()  { return (String)sql.get("WHERE"); }
    public String getOrderByString(){ return (String)sql.get("ORDER BY"); }
    public String getGroupByString() { return (String)sql.get("GROUP BY"); }
    public String getHavingString() { return (String)sql.get("HAVING"); }


    
  //**************************************************************************
  //** Creates a new instance of sql
  //**************************************************************************
    
    public Parser(String sql){

      //Validate input sql
        if (sql==null) return;


      //Trim the sql statement
        sql = sql.replace("\n"," ");
        sql = sql.replace("\r"," ");
        sql = sql.replace("\t"," ");
        sql = sql.trim();
        if (sql.endsWith(";")) sql = sql.substring(0, sql.length()-1).trim();
        if (sql.length()<=0) return;


      //Set local variables;
        boolean insideSingleQuotes = false;
        boolean insideDoubleQuotes = false;
        boolean insideParenthesis = false;

        int parenthesis = 0;

        String s = sql;
        String c = "";


      //Create a list of possible sql keywords found in the sql statement
        java.util.Vector keywords = new java.util.Vector();
        String t = sql.toUpperCase();
        for (int i=0; i<sqlKeywords.length; i++){
            if (t.contains(sqlKeywords[i])) keywords.add(sqlKeywords[i]);
        }


        String currentKeyword = null;
        String previousKeyword = null;


        StringBuffer phrase = new StringBuffer();

        for (int i = 0; i < s.length(); i++){

            c = s.substring(i,i+1);

            if (c.equals("\"")){
                if (!insideDoubleQuotes) insideDoubleQuotes = true;
                else insideDoubleQuotes = false;
            }
            if (c.equals("'")){
                if (!insideSingleQuotes) insideSingleQuotes = true;
                else insideSingleQuotes = false;
            }

            if ((c.equals("(") && !insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                insideParenthesis = true;
                parenthesis = 0;
            }
            if ((c.equals("(") ) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                parenthesis += 1;
            }
            if ((c.equals(")") && insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)){
                parenthesis = parenthesis - 1;
                if (parenthesis==0) insideParenthesis = false;
            }


            phrase.append(c);


            if (!insideDoubleQuotes && !insideSingleQuotes && !insideParenthesis){

                for (int j=0; j<keywords.size(); j++){
                    String keyword = keywords.get(j).toString();

                    if (keyword.startsWith(c.toUpperCase()) && (i+keyword.length())<=s.length()){


                        String str = s.substring(i,i+keyword.length());
                        if (str.equalsIgnoreCase(keyword)){ //Found a string that contains an sql keyword!

                          //Check whether the string is an actual keyword or part of another word
                            String a = "";
                            String b = "";
                            if (i>0) a = s.substring(i-1,i);
                            if (i+keyword.length()+1<s.length()) b = s.substring(i+keyword.length(), i+keyword.length()+1);

                            if ((a.equals("") || a.equals(" ") || a.equals(")") || a.equals("]") || a.equals("\"") || (keyword.equals("FROM") && a.equals("*"))) &&
                                (b.equals("") || b.equals(" ") || b.equals("(") || b.equals("[") || b.equals("\"") || (keyword.equals("SELECT") && b.equals("*")))
                            ){

                                currentKeyword = keyword;

                                String entry = phrase.substring(0, phrase.length()-1).trim();
                                if (entry.length()>0){

                                    Object prevStatement = this.sql.get(previousKeyword);
                                    if (prevStatement==null) {
                                        this.sql.put(previousKeyword, entry);
                                    }

                                    this.sql.put(currentKeyword, null);
                                }


                                phrase = new StringBuffer();
                                i = i + (keyword.length()-1);


                                previousKeyword = currentKeyword;
                                keywords.remove(j);
                                break;
                            }
                        }

                    }

                }
            }
                    


            if (i==(s.length()-1)){
                if (phrase.toString().trim().equals(s)){
                }
                else{
                    this.sql.put(previousKeyword, phrase.toString().trim());

                }
            }

        }//end parsing text
    }

    
//  //**************************************************************************
//  //** addWhere
//  //**************************************************************************
//  /** Used to update the where clause in the SQL String. Preserves existing
//   *  where clause, if one exists, by adding an "AND" statement.
//   */
//    public String addWhereStatement(String whereClause){
//
//        if (whereClause!=null){
//            whereClause = whereClause.trim();
//            if (whereClause.length()>0){
//                String orgWhere = this.getWhereString();
//                if (orgWhere==null || orgWhere.trim().length()==0){
//                    setWhere(whereClause);
//                }
//                else{
//                    setWhere("(" + orgWhere.trim() + ") AND (" + whereClause + ")");
//                }
//            }
//        }
//
//        return this.toString();
//    }


  //**************************************************************************
  //** setWhere
  //**************************************************************************
  /** Used to update the where clause in the SQL String. The entire where
   *  clause will be replaced with the given string. The input select clause
   *  is accepted "AS IS". Returns an updated SQL statement.
   */
    public String setWhere(String whereClause){
        if (whereClause==null) whereClause = "";
        else whereClause = whereClause.trim();
        sql.put("WHERE", whereClause);
        this.whereStatements = null;
        getWhereStatements();
        return this.toString();
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /**  Returns an sql String, including any updates */
    
    public String toString() {

        String selectClause = this.getSelectString();
        String fromClause = this.getFromString();
        String whereClause = this.getWhereString();
        String orderByClause = this.getOrderByString();
        String groupByClause = this.getGroupByString();
        String havingClause = this.getHavingString();

        if (selectClause==null || fromClause==null){
            return null;
        }
        else if(selectClause.length()<=0 || fromClause.length()<=0){
            return null;
        }
        else{
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT " + selectClause + " FROM " + fromClause);
            if (whereClause!=null) sql.append(" WHERE " + whereClause);
            if (orderByClause!=null) sql.append(" ORDER BY " + orderByClause);
            if (groupByClause!=null) sql.append(" GROUP BY " + groupByClause);
            if (havingClause!=null) sql.append(" HAVING " + havingClause);
            return sql.toString();
        }
    }


  //**************************************************************************
  //** Set Select String
  //**************************************************************************
  /**  Used to update the select clause. The input select clause is accepted
   *   "AS IS". Returns an updated SQL statement.
   */
    public String setSelect(String selectClause){
        if (selectClause==null) selectClause = "*";
        else selectClause = selectClause.trim();
        if (selectClause.length()==0) selectClause = "*";
        sql.put("SELECT", selectClause);
        selectStatements = null;
        getSelectStatements();
        return this.toString();
    }


  //**************************************************************************
  //** getSelectStatements 
  //**************************************************************************
  /** Used to break down the select clause into individual elements. For 
   *  example, "Select FirstName, LastName from Contacts" would return an 
   *  array with 2 entries: "FirstName" and "LastName".
   */
    public SelectStatement[] getSelectStatements(){
        
        if (selectStatements!=null) return selectStatements;
        else{
            String[] array = this.split(getSelectString());
            selectStatements = new SelectStatement[array.length];
            for (int i=0; i<selectStatements.length; i++){
                selectStatements[i] = new SelectStatement(array[i]);
            }
            return selectStatements;
        }
    }


  //**************************************************************************
  //** SelectStatement
  //**************************************************************************
  /** Used to represent an individual select statement found in the select 
   *  clause. 
   */
    public class SelectStatement{

        private String alias = null;
        private String statement = null;
        private String columnName = null;   
        private java.util.List exposedElements = new java.util.LinkedList();        

        public SelectStatement(String statement){
            
            this.statement = statement;
            this.columnName = stripFunctions(statement);
            
          //Find the alias, defined by the "AS" keyword
            if (statement.toUpperCase().contains("AS")){
        
                boolean insideSingleQuotes = false;
                boolean insideDoubleQuotes = false;
                boolean insideParenthesis = false;
                int parenthesis = 0;
                String s = statement;
                String c = "";
                
                for (int i = 0; i < s.length(); i++){

                    c = s.substring(i,i+1);

                    if (c.equals("\"")){
                        if (!insideDoubleQuotes) insideDoubleQuotes = true;
                        else insideDoubleQuotes = false;
                    }
                    if (c.equals("'")){
                        if (!insideSingleQuotes) insideSingleQuotes = true;
                        else insideSingleQuotes = false;
                    }
                    if ((c.equals("(") && !insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                        insideParenthesis = true;
                        parenthesis = 0;
                    }
                    if ((c.equals("(") ) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                        parenthesis += 1;
                    }
                    if ((c.equals(")") && insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)){
                        parenthesis = parenthesis - 1;
                        if (parenthesis==0) insideParenthesis = false;
                    }


                    if (!insideDoubleQuotes && !insideSingleQuotes && !insideParenthesis){

                        if (c.equalsIgnoreCase("A") && (i+3)<s.length()){
                            String keyword = s.substring(i,i+2);
                            if (keyword.equalsIgnoreCase("AS")){
                                
                                String as = s.substring(i+2);
                                
                                String prevChar = "";
                                String nextChar = "";
                                if (i-1>=0 && i+3<=s.length()){
                                    prevChar = s.substring(i-1,i);
                                    nextChar = s.substring(i+2,i+3);
                                }
                                
                                if ((prevChar.equals(" ") || prevChar.equals(")") || prevChar.equals("]") || prevChar.equals("\"")) &&
                                    (nextChar.equals(" ") || nextChar.equals("(") || nextChar.equals("[") || nextChar.equals("\"")))
                                {
                                    this.alias = removeParentheses(as);
                                    this.columnName = stripFunctions(s.substring(0,i-1).trim());                                    
                                }
                                
                            }
                        }
                    }


                }//end parsing text       

                
            }//end if
            


          //Iterate throught the list of operands and identify any exposed columns (columns that are not wrapped in quotes)
            String[] elements = new String[]{columnName, alias};
            for (int i=0; i<elements.length; i++){
                String entry = elements[i];
                if (entry!=null){
                    if (entry.endsWith("*")){//<-- Get rid of this case
                        if (entry.endsWith(".*")) {
                            exposedElements.add(entry.substring(0, entry.length()-2));
                        }
                    }
                    else if(entry.startsWith("\"") && entry.endsWith("\"")){
                    }
                    else if(entry.startsWith("'") && entry.endsWith("'")){
                    }
                    else{
                        exposedElements.add(entry);
                    }
                }
            }
        
        }//end constructor
        
        
        public String getColumnName(){
            return columnName;
        }
        
        public String getAlias(){
            return alias;
        }
        
        public String toString(){
            return statement;
        }
    }


  //**************************************************************************
  //** getFromStatement
  //**************************************************************************
  /** Used to parse the "FROM" statement and extracts */

    private FromStatement getFromStatement(){

        if (fromStatement!=null) return fromStatement;
        
        String fromClause = getFromString();

        fromClause = fromClause.replace("(", " ");
        fromClause = fromClause.replace(")", " ");
        fromClause = fromClause.trim();


        fromStatement = new FromStatement(fromClause);

        if (fromClause.toUpperCase().contains("JOIN")){

            boolean insideSingleQuotes = false;
            boolean insideDoubleQuotes = false;
            String s = fromClause;
            String c = "";

            StringBuffer phrase = new StringBuffer();

            for (int i = 0; i < s.length(); i++){

                c = s.substring(i,i+1);

                if (c.equals("\"")){
                    if (!insideDoubleQuotes) insideDoubleQuotes = true;
                    else insideDoubleQuotes = false;
                }
                if (c.equals("'")){
                    if (!insideSingleQuotes) insideSingleQuotes = true;
                    else insideSingleQuotes = false;
                }

                phrase.append(c);

                if (!insideDoubleQuotes && !insideSingleQuotes){                   
                    
                    for (int j=0; j<joinTypes.length; j++){
                        String joinType = joinTypes[j];
                        if (joinType.startsWith(c.toUpperCase()) && (i+joinType.length())<=s.length()){
                            String keyword = s.substring(i,i+joinType.length());

                            if (keyword.equalsIgnoreCase(joinType)){
                                
                                
                                String entry = phrase.substring(0, phrase.length()-1).trim();
                                if (entry.length()>0){ 
                                    
                                    phrase = new StringBuffer();
                                    i = i + (keyword.length()-1);
                                    
                                    fromStatement.addEntry(entry);

                                    //System.out.println("(+) " + entry + " (" + keyword + ")");

                                }
                                
                                break;
                            }

                        }
                    }

                }


                if (i==(s.length()-1)){
                    if (phrase.toString().trim().equals(s)){
                    }
                    else{
                        String entry = phrase.toString().trim();
                        fromStatement.addEntry(entry);
                    }
                }

            }//end parsing text

        }
        else{
            String[] tables = javaxt.utils.string.split(fromClause, ",");            
            for (int i=0; i<tables.length; i++){
                String tableName = tables[i].trim();
                fromStatement.addTable(tableName);
            }
        }

        return fromStatement;
    }


  //**************************************************************************
  //** FromStatement
  //**************************************************************************
  /** Used to represent the "FROM" clause.
   */
    public class FromStatement {

        private String statement = null;
        private java.util.HashSet tables = new java.util.HashSet();
        private java.util.HashSet columns = new java.util.HashSet();
        private java.util.HashSet exposedElements = new java.util.HashSet();

        private FromStatement(String statement){
            this.statement = statement;
        }

        public void addTable(String tableName){
            tableName = tableName.trim();
            tables.add(tableName);
            if (isExposed(tableName)) exposedElements.add(tableName);
        }
        public void addColumn(String columnName){
            columnName = columnName.trim();
            columns.add(columnName);
            if (isExposed(columnName)) exposedElements.add(columnName);
        }
        private void addEntry(String entry){
            String tableName = null;
            String joinCondition = null;
            if (entry.toUpperCase().contains(" ON ")){

                tableName = entry.substring(0, entry.toUpperCase().indexOf(" ON ")).trim();
                this.addTable(tableName);


                joinCondition = entry.substring(entry.toUpperCase().indexOf(" ON ") + 4).trim();                
                WhereStatement statement = new WhereStatement(joinCondition);
                this.addColumn(statement.getLeftOperand());
                this.addColumn(statement.getRightOperand());

            }
            else{
                tableName = entry;
                this.addTable(tableName);
            }

            //System.out.println("--> " + tableName + " [" + joinCondition + "]");
        }
        public java.util.HashSet getExposedElements(){ return exposedElements; }
        public String[] getTables(){
            String[] array = new String[tables.size()];
            java.util.Iterator it = tables.iterator();
            int i=0;
            while (it.hasNext()){
                String tableName = (String)it.next();
                array[i] = tableName;
                i++;
            }
            return array;
        }
        public String toString(){ return statement; }
    }


  //**************************************************************************
  //** getTables
  //**************************************************************************
  /** Returns an array of Tables Found in the SQL String */

    public String[] getTables(){
        return getFromStatement().getTables();
    }
    
    
  //**************************************************************************
  //** getWhereStatements
  //**************************************************************************
  /** Used to retrieve a list of where statements found in the where clause.
   *  Returns an empty array if no where statements are found.
   */
    public WhereStatement[] getWhereStatements(){
        
        if (this.whereStatements!=null) return this.whereStatements;
        
        String whereClause = this.getWhereString();
        if (whereClause==null || whereClause.trim().length()<=0) {
            return new WhereStatement[0];
        }
        else{
     
          //Create a list of sql fragments to parse
            java.util.List list = new java.util.LinkedList(); 
            
          //Add Where Clause to the list of sql fragments to parse
            list.add(whereClause);
            
          //Iterate through all the sql fragments and extract individual statements
            for (int x=0; x<list.size(); x++){            

                boolean insideSingleQuotes = false;
                boolean insideDoubleQuotes = false;
                boolean insideParenthesis = false;

                int parenthesis = 0;
                
                String s = list.get(x).toString().trim();
                String c = "";

                /*
                System.out.println();
                System.out.println("-----------------------------------------");
                System.out.println(s);
                System.out.println("-----------------------------------------");
                */

                
                StringBuffer phrase = new StringBuffer();

                for (int i = 0; i < s.length(); i++){

                    c = s.substring(i,i+1);

                    if (c.equals("\"")){
                        if (!insideDoubleQuotes) insideDoubleQuotes = true;
                        else insideDoubleQuotes = false;
                    }
                    if (c.equals("'")){
                        if (!insideSingleQuotes) insideSingleQuotes = true;
                        else insideSingleQuotes = false;
                    }

                    if ((c.equals("(") && !insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                        insideParenthesis = true;
                        parenthesis = 0;
                    }
                    if ((c.equals("(") ) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                        parenthesis += 1;
                    }
                    if ((c.equals(")") && insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)){
                        parenthesis = parenthesis - 1;
                        if (parenthesis==0) insideParenthesis = false;
                    }



                    phrase.append(c);

                    if (!insideDoubleQuotes && !insideSingleQuotes && !insideParenthesis){

                        String keyword = null;
                        if (c.equalsIgnoreCase("A") && (i+3)<s.length()){
                            keyword = s.substring(i,i+3);
                            if (!keyword.equalsIgnoreCase("AND")) keyword = null;
                        }
                        else if (c.equalsIgnoreCase("O") && (i+2)<s.length()){
                            keyword = s.substring(i,i+2);
                            if (!keyword.equalsIgnoreCase("OR")) keyword = null;
                        }

                        if (keyword!=null){
                            
                            String entry = phrase.substring(0, phrase.length()-1).trim();
                            entry = removeParentheses(entry);
                            if (entry.length()>0){
                                list.add(entry);
                                phrase = new StringBuffer();
                                i = i + (keyword.length()-1);
                                //System.out.println("(+) " + entry);
                            }
                        }
                    }



                    if (i==(s.length()-1)){                   
                        if (phrase.toString().trim().equals(s)){ //<--might need to check the size of the list as well
                        }
                        else{
                            
                            String entry = phrase.toString().trim();
                            //entry = phrase.substring(0, phrase.length()-1).trim();
                            entry = removeParentheses(entry);
                            list.add(entry);
                            list.remove(x); 
                            
                            //System.out.println("(+) " + entry + " *");
                            //System.out.println("(-) " + s);
                        }
                    }

                }//end parsing text                       

            }


          //Create a list of possible SQL Operators found in the Where clause    
            java.util.List operators = new java.util.LinkedList();
            for (int i=0; i<this.sqlOperators.length; i++){
                String operator = this.sqlOperators[i];
                if (whereClause.toUpperCase().contains(operator)){
                    operators.add(operator);
                }
            }
            String[] sqlOperators = new String[operators.size()];
            for (int i=0; i<sqlOperators.length; i++){
                sqlOperators[i] = (String) operators.get(i);
            }
            
            
          //Iterate through all the where statements and create an array
            WhereStatement[] statements = new WhereStatement[list.size()];
            for (int i=0; i<list.size(); i++){
                String entry = list.get(i).toString();
                //System.out.println("xx " + entry);
                statements[i] = new WhereStatement(entry, sqlOperators);
            }
            this.whereStatements = statements;
            return statements;
        }
    }
    

  //**************************************************************************
  //** WhereStatement
  //**************************************************************************
  /** Used to represent an individual where condition found in the "WHERE"
   *  clause. Note that this class is also used to parse join logic found
   *  in the "FROM" clause.
   */
    public class WhereStatement {
        
        private String statement = null;
        private String leftOperand = null;
        private String rightOperand = null;
        private String operator = null;
        private java.util.List exposedColumns = new java.util.LinkedList();
        
        public WhereStatement(String statement) {
            this(statement, sqlOperators);
        }
        
        public WhereStatement(String statement, String[] sqlOperators) {
            this.statement = statement;
            
            if (statement!=null){

              //Trim down the list of sql operators
                java.util.Vector array = new java.util.Vector();
                for (int i=0; i<sqlOperators.length; i++){
                    String sqlOperator = sqlOperators[i];
                    if (statement.toUpperCase().contains(sqlOperator)){
                        array.add(sqlOperator);
                    }
                }
                sqlOperators = new String[array.size()];
                for (int i=0; i<sqlOperators.length; i++){
                    sqlOperators[i] = (String) array.get(i);
                }
                

                boolean insideSingleQuotes = false;
                boolean insideDoubleQuotes = false;
                boolean insideParenthesis = false;
                int parenthesis = 0;
                String s = statement;
                String c = "";
                StringBuffer phrase = new StringBuffer();
                java.util.List list = new java.util.LinkedList(); 
                
                for (int i = 0; i < s.length(); i++){

                    c = s.substring(i,i+1);

                    if (c.equals("\"")){
                        if (!insideDoubleQuotes) insideDoubleQuotes = true;
                        else insideDoubleQuotes = false;
                    }
                    if (c.equals("'")){
                        if (!insideSingleQuotes) insideSingleQuotes = true;
                        else insideSingleQuotes = false;
                    }

                    if ((c.equals("(") && !insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                        insideParenthesis = true;
                        parenthesis = 0;
                    }
                    if ((c.equals("(") ) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                        parenthesis += 1;
                    }
                    if ((c.equals(")") && insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)){
                        parenthesis = parenthesis - 1;
                        if (parenthesis==0) insideParenthesis = false;
                    }

                    phrase.append(c);

                    if (!insideDoubleQuotes && !insideSingleQuotes && !insideParenthesis){

                        for (int j=0; j<sqlOperators.length; j++){
                            String sqlOperator = sqlOperators[j];

                            if (sqlOperator.toUpperCase().startsWith(c.toUpperCase()) && (i+sqlOperator.length())<=s.length()){
                               
                                String keyword = s.substring(i,i+sqlOperator.length());
                                if (keyword.equalsIgnoreCase(sqlOperator)){

                                    String a = "";
                                    String b = "";
                                    if (i>0) a = s.substring(i-1,i);
                                    if (i+keyword.length()+1<s.length()) b = s.substring(i+keyword.length(), i+keyword.length()+1);

                                    if (
                                      //Check if keyword is part of another word
                                        ( (a.equals("") || a.equals(" ") || a.equals(")") || a.equals("]") || a.equals("\"") ) &&
                                          (b.equals("") || b.equals(" ") || b.equals("(") || b.equals("[") || b.equals("\"") )
                                        ) || (
                                      //Check if keyword is a symbol
                                         keyword.equals("<>") || keyword.equals("<=") || keyword.equals(">=") ||
                                         keyword.equals("=") || keyword.equals("<") || keyword.equals(">")
                                        )
                                    ){

                                        String entry = phrase.substring(0, phrase.length()-1).trim();
                                        i = i + (keyword.length()-1);
                                        phrase = new StringBuffer();

                                        if (entry.length()>0){
                                            list.add(entry);
                                            this.operator = keyword;
                                            this.leftOperand = entry;
                                        }
                                        else{
                                          //Things like "NOT EXISTS" statements
                                            this.operator = keyword;
                                            this.leftOperand = null;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    
                    if (i==(s.length()-1)){                   
                        if (phrase.toString().trim().equals(s)){
                        }
                        else{
                            
                            String entry = phrase.toString().trim();
                            if (entry.length()>0){
                                
                                if (!entry.equalsIgnoreCase("null")){
                                    list.add(entry);
                                }

                                //System.out.println("(+) " + entry + " **");
                                //System.out.println("(-) " + s);

                                this.rightOperand = entry;
                            }
                            
                        }
                    }
                    
                    
                }//end parsing text

                
              //Iterate throught the list of operands and identify any exposed columns (columns that are not wrapped in quotes)
                exposedColumns = new java.util.LinkedList();
                for (int i=0; i<list.size(); i++){
                    String entry = (String) list.get(i);
                    if (entry!=null){    
                        entry = stripFunctions(entry);
                        if (isExposed(entry)){
                            exposedColumns.add(entry);
                            //System.out.println("-- " + entry);
                        }
                    }
                }

            }
        }
        
        public String getLeftOperand(){
            return leftOperand;
        }
        
        public String getRightOperand(){
            return rightOperand;
        }
        
        public String getOperator(){
            return operator;
        }
        
        public String toString(){
            return statement;
        }
    }


  //**************************************************************************
  //** isExposed
  //**************************************************************************
  /** Used to determine whether a block of text is wrapped in quotes.
   */
    private boolean isExposed(String element){
        if (javaxt.utils.string.isNumeric(element)){
            return false;
        }
        else if (element.startsWith("\"") && element.endsWith("\"")){
            return false;
        }
        else if (element.startsWith("'") && element.endsWith("'")){
            return false;
        }
        else{
            return true;
        }
    }
    

  //**************************************************************************
  //** getOrderByStatements
  //**************************************************************************
  /** Used to break down the "ORDER BY" clause into individual statements.
   */
    public OrderByStatement[] getOrderByStatements(){

        if (orderByStatements!=null) return orderByStatements;
        else{
            String[] array = this.split(getOrderByString());
            orderByStatements = new OrderByStatement[array.length];
            for (int i=0; i<orderByStatements.length; i++){
                orderByStatements[i] = new OrderByStatement(array[i]);
            }
            return orderByStatements;
        }
    }


  //**************************************************************************
  //** OrderByStatement
  //**************************************************************************
  /** Simple class used to represent a single statement in an "ORDER BY"
   *  clause.
   */
    public class OrderByStatement{
        private String statement = null;
        private String columnName = null;
        private boolean isAscending = true;
        private boolean isExposed = false;

        public OrderByStatement(String orderBy){
            this.statement = orderBy;
            if (orderBy.toUpperCase().endsWith(" ASC")){
                columnName = orderBy.substring(0, orderBy.length()-4).trim();
            }
            else if(orderBy.toUpperCase().endsWith(" DESC")){
                columnName = orderBy.substring(0, orderBy.length()-5).trim();
                isAscending = false;
            }
            else{
                columnName = orderBy;
            }
            
            isExposed = isExposed(columnName);
        }
        public String getColumnName(){ return columnName; }
        public boolean isDescending(){ return !isAscending; }
        public boolean isAscending(){ return isAscending; }
        public String toString(){ return statement; }
    }


  //**************************************************************************
  //** getGroupByStatements
  //**************************************************************************
  /**  Used to break down the "GROUP BY" clause into individual statements. */

    public GroupByStatement[] getGroupByStatements(){

        if (groupByStatements!=null) return groupByStatements;
        else{

            String[] array = this.split(getGroupByString());
            groupByStatements = new GroupByStatement[array.length];
            for (int i=0; i<groupByStatements.length; i++){
                groupByStatements[i] = new GroupByStatement(array[i]);
            }
            return groupByStatements;
        }
    }


  //**************************************************************************
  //** GroupByStatement
  //**************************************************************************
  /** Simple class used to represent a single statement in a "GROUP BY" clause.
   */
    public class GroupByStatement {

        private String statement = null;
        private boolean isExposed = false;

        public GroupByStatement(String groupBy){
            this.statement = groupBy;
            this.isExposed = isExposed(groupBy);
        }

        public String getColumnName(){ return statement; }
        public String toString(){ return statement; }
    }



  //**************************************************************************
  //** getExposedDataElements
  //**************************************************************************
  /**  Used to retrieve a list of database elements (tables, columns, etc.) 
   *   found in the sql string that are not wrapped in quotes.
   */
    protected String[] getExposedDataElements(){

        java.util.TreeSet exposedElements = new java.util.TreeSet();


      //Find exposed elements in the "SELECT" clause
        SelectStatement[] selectStatements = getSelectStatements();
        for (int i=0; i<selectStatements.length; i++){
            SelectStatement statement = selectStatements[i];
            java.util.List list = statement.exposedElements;
            for (int j=0; j<list.size(); j++){
                String element = (String) list.get(j);
                if (element!=null) exposedElements.add(element);
            }
        }



      //Find exposed elements in the "FROM" clause
        java.util.Iterator it = getFromStatement().exposedElements.iterator();
        while (it.hasNext()){
            String exposedElement = (String) it.next();
            exposedElements.add(exposedElement);
        }



      //Find exposed elements in the "WHERE" clause
        WhereStatement[] whereStatements = getWhereStatements();
        if (whereStatements.length>0){
            for (int i=0; i<whereStatements.length; i++){
                WhereStatement statement = whereStatements[i];
                java.util.List list = statement.exposedColumns;
                for (int j=0; j<list.size(); j++){
                    String element = (String) list.get(j);
                    if (element!=null) exposedElements.add(element);
                }
            }
        }


      //Find exposed elements in the "ORDER BY" clause
        OrderByStatement[] orderByStatements = getOrderByStatements();
        if (orderByStatements.length>0){
            for (int i=0; i<orderByStatements.length; i++){
                OrderByStatement statement = orderByStatements[i];
                if (isExposed(statement.getColumnName())){
                    exposedElements.add(statement.getColumnName());
                }
            }
        }


      //Find exposed elements in the "GROUP BY" clause
        GroupByStatement[] groupByStatements = getGroupByStatements();
        if (groupByStatements.length>0){
            for (int i=0; i<groupByStatements.length; i++){
                GroupByStatement statement = groupByStatements[i];
                if (isExposed(statement.getColumnName())){
                    exposedElements.add(statement.getColumnName());
                }
            }
        }


      //Convert the exposedElements (HashSet) into an array
        String[] array = new String[exposedElements.size()];
        int i=0; it = exposedElements.iterator();
        while (it.hasNext()){
            String exposedElement = (String) it.next();
            array[i] = exposedElement;
            i++;
        }
        return array;
    }

    
  //**************************************************************************
  //** addQuotes
  //**************************************************************************
  /** Used to identify tables and columns in the SQL and wrap them in quotes.
   *  Note that this method is fairly brittle and needs a lot more work.
   */
    protected String addQuotes(){

        String selectClause = this.getSelectString();
        String fromClause = this.getFromString();
        String whereClause = this.getWhereString();
        String orderByClause = this.getOrderByString();
        String groupByClause = this.getGroupByString();
        String havingClause = this.getHavingString();

        java.util.HashSet exposedElements = new java.util.HashSet();
        
      //Update the "SELECT" clause
        SelectStatement[] selectStatements = getSelectStatements();
        for (int i=0; i<selectStatements.length; i++){
            SelectStatement statement = selectStatements[i];
            java.util.List list = statement.exposedElements;
            for (int j=0; j<list.size(); j++){
                String element = (String) list.get(j);
                if (element!=null) exposedElements.add(element);
            }
        }
        selectClause = addQuotes(exposedElements, selectClause);



      //Update the "FROM" clause
        fromClause = addQuotes(getFromStatement().exposedElements, fromClause);



      //Update the "WHERE" clause
        WhereStatement[] whereStatements = getWhereStatements();
        if (whereStatements.length>0){
            exposedElements.clear();
            for (int i=0; i<whereStatements.length; i++){
                WhereStatement statement = whereStatements[i];
                java.util.List list = statement.exposedColumns;
                for (int j=0; j<list.size(); j++){
                    String element = (String) list.get(j);
                    if (element!=null) exposedElements.add(element);
                }
            }
            whereClause = addQuotes(exposedElements, whereClause);
        }


      //Update the "ORDER BY" clause
        OrderByStatement[] orderByStatements = getOrderByStatements();
        if (orderByStatements.length>0){
            exposedElements.clear();
            for (int i=0; i<orderByStatements.length; i++){
                OrderByStatement statement = orderByStatements[i];
                if (isExposed(statement.getColumnName())){
                    exposedElements.add(statement.getColumnName());
                }
            }
            orderByClause = addQuotes(exposedElements, orderByClause);
        }


      //Update the "GROUP BY" clause
        GroupByStatement[] groupByStatements = getGroupByStatements();
        if (groupByStatements.length>0){
            exposedElements.clear();
            for (int i=0; i<groupByStatements.length; i++){
                GroupByStatement statement = groupByStatements[i];
                if (isExposed(statement.getColumnName())){
                    exposedElements.add(statement.getColumnName());
                }
            }
            groupByClause = addQuotes(exposedElements, groupByClause);
        }



      //Update the "HAVING" clause



      //Assemble SQL
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT "); sql.append(selectClause);
        sql.append(" FROM "); sql.append(fromClause);

        if (whereClause!=null)  { sql.append(" WHERE "); sql.append(whereClause); }
        if (orderByClause!=null){ sql.append(" ORDER BY "); sql.append(orderByClause); }
        if (groupByClause!=null){ sql.append(" GROUP BY "); sql.append(groupByClause); }
        if (havingClause!=null) { sql.append(" HAVING "); sql.append(havingClause); }


        return sql.toString();
    }


  //**************************************************************************
  //** addQuotes
  //**************************************************************************
  /**  Used to wrap exposed database elements in quotes. */

    private String addQuotes(java.util.HashSet exposedElements, String sqlFragment){

        if (sqlFragment==null) return null;
        if (exposedElements.isEmpty()) return sqlFragment;


        /*
        System.out.println("------------------------------------");
        System.out.println(sqlFragment);
        System.out.println("------------------------------------");
        java.util.Iterator itt = exposedElements.iterator();
        while (itt.hasNext()){
            String exposedElement = (String) itt.next();
            System.out.println(exposedElement + " <--");
        }
        */

        

      //Set local variables;
        boolean insideSingleQuotes = false;
        boolean insideDoubleQuotes = false;

        String s = sqlFragment;
        String c = "";


        StringBuffer phrase = new StringBuffer();
        StringBuffer newSQL = new StringBuffer();


        

        for (int i = 0; i < s.length(); i++){

            c = s.substring(i,i+1);

            if (c.equals("\"")){
                if (!insideDoubleQuotes) insideDoubleQuotes = true;
                else insideDoubleQuotes = false;
            }
            if (c.equals("'")){
                if (!insideSingleQuotes) insideSingleQuotes = true;
                else insideSingleQuotes = false;
            }
            phrase.append(c);


            if (!insideDoubleQuotes && !insideSingleQuotes){

                java.util.Iterator it = exposedElements.iterator();
                while (it.hasNext()){

                    String exposedElement = (String)it.next();
                    if (exposedElement.length()>0){


                        if (c.equalsIgnoreCase(exposedElement.substring(0,1)) && (i+exposedElement.length())<=s.length()){


                            String str = s.substring(i,i+exposedElement.length());
                            if (str.equalsIgnoreCase(exposedElement)){



                                str = phrase.substring(0, phrase.length()-1);



                                String prevChar = "";
                                String nextChar = "";
                                if (i-1>=0 && i+exposedElement.length()+1<=s.length()){
                                    prevChar = s.substring(i-1,i);
                                    nextChar = s.substring(i+exposedElement.length(),i+exposedElement.length()+1);
                                }

                                //System.out.println("[" + prevChar + "]" + exposedElement + "[" + nextChar + "]");


                                if (wrapElement(prevChar, nextChar)){


                                    newSQL.append(str + "\"" + exposedElement + "\"");


                                    phrase = new StringBuffer();
                                    i = i + (exposedElement.length()-1);


                                  //For efficiency, remove any unused elements from the exposedElements HashSet
                                    if (i+exposedElement.length()<=s.length()){
                                        if (s.substring(i+exposedElement.length()).toUpperCase().contains(exposedElement.toUpperCase())==false){
                                            exposedElements.remove(exposedElement);
                                            it = exposedElements.iterator();
                                            //System.out.println("(-) " + exposedElement);
                                        }
                                    }

                                    break;

                                }


                            }

                        }
                    
                    }


                }

            }



            if (i==(s.length()-1)){
                if (phrase.toString().trim().equals(s)){
                }
                else{
                    newSQL.append(phrase.toString());

                }
            }

        }//end parsing text
        
        return newSQL.toString();
    }


  //**************************************************************************
  //** wrapElement
  //**************************************************************************
  /**  Used to help determine whether to add quotes around a block of text. */

    private boolean wrapElement(String prevChar, String nextChar){
        if ((prevChar.equals("") || prevChar.equals(" ") || prevChar.equals(")") || prevChar.equals("(") || prevChar.equals(",") || prevChar.equals("=") || prevChar.equals(">") || prevChar.equals("<")) &&
            (nextChar.equals("") || nextChar.equals(" ") || nextChar.equals(")") || nextChar.equals("(") || nextChar.equals(",") || nextChar.equals("=") || nextChar.equals(">") || nextChar.equals("<")))
        {
            return true;
        }
        else{
            return false;
        }
    }


    
  //**************************************************************************
  //** stripFunctions (getColumnName)
  //**************************************************************************
  /** Used to strip functions from an SQL fragment and return the column name.
   *  For example, "SUM(Sales.SaleAmount)" would return "Sales.SaleAmount". In
   *  cases where the function uses multiple parameters, the first parameter
   *  is assumed to be the column name (or alias). For example,
   *  "SUBSTR(LastName,0,1)" would return "LastName".
   */
    private String stripFunctions(String str){

        boolean FunctionsPresent = true;

        if ( 
             (InStr(str, "(") > 0 && InStr(str, ")") > 0) && 
             (InStr(str, "(") < InStr(str, ")")) 
           ) 
        {

            while (FunctionsPresent){
                str = Right(str, Len(str) - InStrRev(str, "("));
                str = Left(str, InStrRev(str, ")") - 1);
                str = str.trim();

                /*
                'Special Case and one BIG assumption:
                'if the resultant str has a comma, then the 
                'function has multiple parameters. The assumption
                'is that the first parameter is the column name
                 */

                if (InStr(str, ",") > 0) {
                    str = Left(str, InStr(str, ",") - 1);
                    str = str.trim();
                }

                if (InStr(str, "(") == 0 && InStr(str, ")") == 0) {
                    FunctionsPresent = false;
                }

            }
        }

        return str;
    }
    

  //**************************************************************************
  //** Legacy VB String Functions
  //**************************************************************************
    
    private int InStr(String str, String ch){return str.indexOf(ch)+1;}
    private int InStrRev(String str, String ch){return str.lastIndexOf(ch)+1;}
    private int Len(String str){return str.length();}
    private String Left(String str, int n){return str.substring(0,n);}
    private String Right(String str, int n){ int iLen = str.length(); return str.substring(iLen - n, iLen);}

    

  //**************************************************************************
  //** removeParentheses
  //**************************************************************************
  /**  Used to remove the outer paratheses surrounding a block of text. */
        
    private String removeParentheses(String text){
        
        text = text.trim();        
        
        if (text.startsWith("(") && text.endsWith(")")){
        
            boolean insideSingleQuotes = false;
            boolean insideDoubleQuotes = false;        
            boolean insideParenthesis = false;

            int parenthesis = 0;
            int gaps = 0;

            String s = text;
            String c = "";

            for (int i = 0; i < s.length(); i++){

                c = s.substring(i,i+1); 

                if (c.equals("\"")){
                    if (!insideDoubleQuotes) insideDoubleQuotes = true;
                    else insideDoubleQuotes = false;
                }
                if (c.equals("'")){
                    if (!insideSingleQuotes) insideSingleQuotes = true;
                    else insideSingleQuotes = false;
                }

                
                if ((c.equals("(") && !insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                    insideParenthesis = true;
                    parenthesis = 0;
                }
                if ((c.equals("(") ) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                    parenthesis += 1;
                }
                if ((c.equals(")") && insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)){
                    parenthesis = parenthesis - 1;
                    if (parenthesis==0) {
                        
                        insideParenthesis = false;
                        
                        if (i+1 < s.length()){
                            String nextString = s.substring(i+1).trim();
                            if (!nextString.startsWith("(")) gaps++;
                        }
                        
                    }
                }
            }
            
            if (gaps==0) text = text.substring(1, text.length()-1).trim();

        }

        return text;
    }


  //**************************************************************************
  //** Split
  //**************************************************************************
  /**  Used to break down the select clause into individual elements. For
   *   example, "Select FirstName, LastName from Contacts" would return an
   *   array with 2 entries: "FirstName" and "LastName".
   */
    private String[] split(String statement){


        if (statement==null || statement.length()<=0) return new String[0];
        else statement = statement.trim();


        if (statement.contains(",")==false){
            return new String[]{statement};

        }
        else{


            boolean insideSingleQuotes = false;
            boolean insideDoubleQuotes = false;
            boolean insideParenthesis = false;

            int parenthesis = 0;


            String s = statement + " "; //<-- something's not quite right b/c we need this extra space at the end...
            String c = "";
            StringBuffer str = new StringBuffer();
            java.util.List list = new java.util.LinkedList();

            for (int i = 0; i < s.length(); i++){

                c = s.substring(i,i+1);

                if (c.equals("\"")){
                    if (!insideDoubleQuotes) insideDoubleQuotes = true;
                    else insideDoubleQuotes = false;
                }
                if (c.equals("'")){
                    if (!insideSingleQuotes) insideSingleQuotes = true;
                    else insideSingleQuotes = false;
                }

                if ((c.equals("(") && !insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                    insideParenthesis = true;
                    parenthesis = 0;
                }
                if ((c.equals("(") ) && (!insideDoubleQuotes && !insideSingleQuotes)) {
                    parenthesis += 1;
                }
                if ((c.equals(")") && insideParenthesis) && (!insideDoubleQuotes && !insideSingleQuotes)){
                    parenthesis = parenthesis - 1;
                    if (parenthesis==0) insideParenthesis = false;
                }



                str.append(c);


                if (c.equals(",") || i==(s.length()-1)){
                    if (!insideDoubleQuotes && !insideSingleQuotes && !insideParenthesis){

                        String element = str.substring(0,str.length()-1).toString().trim();
                        list.add(element);
                        str = new StringBuffer();
                    }
                }

            }//end for loop



          //Convert list into an array of statements
            String[] statements = new String[list.size()];
            for (int i=0; i<statements.length; i++){
                statements[i] = list.get(i).toString();
            }
            return statements;


        }

    }



    public void debug(){

      
      //Print SQL Statements
        System.out.println();
        System.out.println("-----------------------------------------");
        System.out.println("Original SQL Statement");
        System.out.println("-----------------------------------------");
        for (int i=0; i<sqlKeywords.length; i++){
            String key = sqlKeywords[i];
            String val = (String) this.sql.get(key);
            if (val!=null){
                System.out.println(key.toString());
                System.out.println("   " + val.toString());
            }
        }



      //Print "Select" Statements
        System.out.println();
        System.out.println("-----------------------------------------");
        System.out.println("SELECT Statements");
        System.out.println("-----------------------------------------");
        SelectStatement[] selectStatements = getSelectStatements();
        for (int i=0; i<selectStatements.length; i++){
            SelectStatement statement = selectStatements[i];
            System.out.println(statement);
        }


      //Print Selected Columns
        System.out.println();
        System.out.println("-----------------------------------------");
        System.out.println("Selected Columns");
        System.out.println("-----------------------------------------");
        for (int i=0; i<selectStatements.length; i++){
            SelectStatement statement = selectStatements[i];
            String columnName = statement.getColumnName();
            if (statement.getAlias()!=null) columnName = statement.getAlias();
            System.out.println(columnName);
        }


      //Print Selected Columns
        System.out.println();
        System.out.println("-----------------------------------------");
        System.out.println("Selected Tables");
        System.out.println("-----------------------------------------");
        String[] selectTables = getTables();
        for (int i=0; i<selectTables.length; i++){
            String tableName = selectTables[i];
            System.out.println(tableName);
        }




      //Print "Where" Statements
        WhereStatement[] whereStatements = getWhereStatements();
        if (whereStatements.length>0){
            System.out.println();
            System.out.println("-----------------------------------------");
            System.out.println("WHERE Statements");
            System.out.println("-----------------------------------------");

            for (int i=0; i<whereStatements.length; i++){
                WhereStatement statement = whereStatements[i];
                System.out.println(statement.toString() + " [OPERATOR IS \"" + statement.getOperator() + "\"]");
            }
        }


      //Print "Order By" Statements
        OrderByStatement[] orderByStatements = getOrderByStatements();
        if (orderByStatements.length>0){
            System.out.println();
            System.out.println("-----------------------------------------");
            System.out.println("ORDER BY Statements");
            System.out.println("-----------------------------------------");

            for (int i=0; i<orderByStatements.length; i++){
                OrderByStatement statement = orderByStatements[i];
                System.out.println(statement);
            }
        }


      //Print "Group By" Statements
        GroupByStatement[] groupByStatements = getGroupByStatements();
        if (groupByStatements.length>0){
            System.out.println();
            System.out.println("-----------------------------------------");
            System.out.println("GROUP BY Statements");
            System.out.println("-----------------------------------------");

            for (int i=0; i<groupByStatements.length; i++){
                GroupByStatement statement = groupByStatements[i];
                System.out.println(statement);
            }
        }


      //Print "Having" Statements
        if (getHavingString()!=null){
            System.out.println();
            System.out.println("-----------------------------------------");
            System.out.println("HAVING Statements");
            System.out.println("-----------------------------------------");
            System.out.println(getHavingString());
        }


      //Print Tables and Columns
        System.out.println();
        System.out.println("-----------------------------------------");
        System.out.println("Exposed Tables and Columns");
        System.out.println("-----------------------------------------");
        String[] exposedElements = getExposedDataElements();
        for (int i=0; i<exposedElements.length; i++){
            String element = exposedElements[i];
            System.out.println(element);
        }



      //Print "NEW" SQL
        System.out.println();
        System.out.println("-----------------------------------------");
        System.out.println("NEW SQL");
        System.out.println("-----------------------------------------");
        System.out.println(addQuotes());
    }

}