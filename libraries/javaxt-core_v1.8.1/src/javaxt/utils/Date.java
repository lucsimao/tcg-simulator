package javaxt.utils;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.HashMap;

//******************************************************************************
//**  Date Utils - By Peter Borissow
//******************************************************************************
/**
 *   Used to parse, format, and compute dates
 *
 ******************************************************************************/

public class Date implements Comparable {
    
    private Locale currentLocale = Locale.getDefault();
    private java.util.TimeZone timeZone = Calendar.getInstance().getTimeZone();
    private java.util.Date currDate;
    
 
    public static final String INTERVAL_MILLISECONDS = "S";
    public static final String INTERVAL_SECONDS = "s";
    public static final String INTERVAL_MINUTES = "m";
    public static final String INTERVAL_HOURS = "h";
    public static final String INTERVAL_DAYS = "d";
    public static final String INTERVAL_WEEKS = "w";
    public static final String INTERVAL_MONTHS = "m";
    public static final String INTERVAL_YEARS = "y";
    
    private static final HashMap<String, String> timezones = new HashMap<String, String>();

    private static final String[] SupportedFormats = new String[] {

         "EEE, d MMM yyyy HH:mm:ss z",  // Mon, 7 Jun 1976 13:02:09 EST
         "EEE, dd MMM yyyy HH:mm:ss z", // Mon, 07 Jun 1976 13:02:09 EST
         "EEE, dd MMM yyyy HH:mm:ss",   // Mon, 07 Jun 1976 13:02:09

         "EEE MMM dd HH:mm:ss z yyyy",  // Mon Jun 07 13:02:09 EST 1976
         "EEE MMM d HH:mm:ss z yyyy",   // Mon Jun 7 13:02:09 EST 1976

         "EEE MMM dd HH:mm:ss yyyy",    // Mon Jun 07 13:02:09 1976
         "EEE MMM d HH:mm:ss yyyy",     // Mon Jun 7 13:02:09 1976

         "EEE MMM dd yyyy HH:mm:ss z",  //"Mon Jun 07 2013 00:00:00 GMT-0500 (Eastern Standard Time)"

         "yyyy-MM-dd HH:mm:ss.SSS Z",   // 1976-06-07 13:02:36.000 America/New_York
         "yyyy-MM-dd HH:mm:ss.SSSZ",    // 1976-06-07 01:02:09.000-0500
         "yyyy-MM-dd HH:mm:ss.SSS",     // 1976-06-07 01:02:09.000

         "yyyy-MM-dd HH:mm:ss Z",       // 1976-06-07 13:02:36 America/New_York
         "yyyy-MM-dd HH:mm:ssZ",        // 1976-06-07 13:02:36-0500
         "yyyy-MM-dd HH:mm:ss",         // 1976-06-07 01:02:09


         "yyyy:MM:dd HH:mm:ss",         // 1976:06:07 01:02:09 (exif metadata)

         "yyyy-MM-dd-HH:mm:ss.SSS",     // 1976-06-07-01:02:09.000
         "yyyy-MM-dd-HH:mm:ss",         // 1976-06-07-01:02:09

       //"yyyy-MM-ddTHH:mm:ss.SSS",     // 1976-06-07T01:02:09.000
       //"yyyy-MM-ddTHH:mm:ss",         // 1976-06-07T01:02:09

         "dd-MMM-yyyy h:mm:ss a",       // 07-Jun-1976 1:02:09 PM
         "dd-MMM-yy h:mm:ss a",         // 07-Jun-76 1:02:09 PM
       //"d-MMM-yy h:mm:ss a",          // 7-Jun-76 1:02:09 PM

         "yyyy-MM-dd HH:mm Z",          // 1976-06-07 13:02 America/New_York"
         "yyyy-MM-dd HH:mmZ",           // 1976-06-07T13:02-0500
         "yyyy-MM-dd HH:mm",            // 1976-06-07T13:02
         "yyyy-MM-dd",                  // 1976-06-07

         "dd-MMM-yy",                   // 07-Jun-76
       //"d-MMM-yy",                    // 7-Jun-76
         "dd-MMM-yyyy",                 // 07-Jun-1976

         "MMMMMM d, yyyy",              // June 7, 1976

         "M/d/yy h:mm:ss a",            // 6/7/1976 1:02:09 PM
         "M/d/yy h:mm a",               // 6/7/1976 1:02 PM

         "MM/dd/yy HH:mm:ss Z",         // 06/07/1976 13:02:09 America/New_York
         "MM/dd/yy HH:mm:ss",           // 06/07/1976 13:02:09
         "MM/dd/yy HH:mm Z",            // 06/07/1976 13:02 America/New_York
         "MM/dd/yy HH:mm",              // 06/07/1976 13:02

         "MM/dd/yyyy HH:mm:ss Z",       // 06/07/1976 13:02:09 America/New_York
         "MM/dd/yyyy HH:mm:ss",         // 06/07/1976 13:02:09
         "MM/dd/yyyy HH:mm Z",          // 06/07/1976 13:02 America/New_York
         "MM/dd/yyyy HH:mm",            // 06/07/1976 13:02

         "M/d/yy",                      // 6/7/76
         "MM/dd/yyyy",                  // 06/07/1976
         "M/d/yyyy",                    // 6/7/1976

         "yyyyMMddHHmmssSSS",           // 19760607130200000
         "yyyyMMddHHmmss",              // 19760607130200
         "yyyyMMdd"                     // 19760607

    };

    
  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of date using current time stamp */
    
    public Date(){
        currDate = new java.util.Date();
    }

  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of date using supplied java.util.Date */
    
    public Date(java.util.Date date){
        if (date==null) throw new IllegalArgumentException("Date is null.");
        currDate = date;
    }
    
    
  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of date using supplied java.util.Calendar */
    
    public Date(Calendar calendar){
        if (calendar==null) throw new IllegalArgumentException("Calendar is null.");
        currDate = calendar.getTime();
        timeZone = calendar.getTimeZone();
    }
    
    
  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of date using a timestamp (in milliseconds) 
   *  since 1/1/1970.
   */
    public Date(long milliseconds){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        currDate = cal.getTime();
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /**  Creates a new instance of date using a String representation of a date.
   */
    public Date(String date) throws ParseException {

        try{

          //Loop through all known date formats and try to convert the string to a date
            for (String format : SupportedFormats){
                
                if (format.endsWith("Z")){

                    //Special Case: Java fails to parse the "T" in strings like
                    //"1976-06-07T01:02:09.000" and "1976-06-07T13:02-0500"
                    int idx = date.indexOf("T");
                    if (idx==10 && format.startsWith("yyyy-MM-dd HH:mm")){
                        date = date.substring(0, idx) + " " + date.substring(idx+1);
                    }
                    
                    
                    
                    if (date.endsWith("Z") && date.length()==format.length()){
                      //If the date literally ends with the letter "Z", then the
                      //date is probably referencing "Zulu" timezone (i.e. UTC).
                      //Example: "1976-06-07 00:00:00Z". Java doesn't understand
                      //what the "Z" timezone is so we'll replace the "Z" with
                      //"UTC".
                        date = date.substring(0, date.length()-1) + "UTC";
                    }
                    else{

                        
                      //Check if the timezone offset is specified in "+/-HH:mm" 
                      //format (e.g. "2018-01-17T01:00:35+07:00"). If so, update
                      //the timezone offset by removing the colon. 
                        if (date.length()>=format.length()){
                            int len = format.length()-1;
                            String tz = date.substring(len);
                            if (tz.length()==6){
                                String a = tz.substring(0,1);
                                if ((a.equals("-") || a.equals("+")) && tz.indexOf(":")==3){
                                    tz = tz.replace(":", "");
                                    date = date.substring(0, len) + tz;
                                }
                            }
                            
                        }
                    }
                }
                
                try{
                    currDate = parseDate(date, format);
                    return;
                }
                catch(ParseException e){
                }
            }
        
        }
        catch(Exception e){
        }
        
      //If we're still here, throw an exception
        throw new ParseException("Failed to parse date: " + date, 0);
        
    }

    
  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of date using a date string. The format string is
   *  used to create a SimpleDateFormat to parse the input date string.
   */
    
    public Date(String date, String format) throws ParseException {
        currDate = parseDate(date, format);
    }
    
    
    
  //**************************************************************************
  //** setDate
  //**************************************************************************
  /** Used to update the current date using a date string. The format parameter
   *  is used to create a SimpleDateFormat to parse the input date string.
   */    
    public void setDate(String date, String format) throws ParseException {
        currDate = parseDate(date, format);
    }
    
    
  //**************************************************************************
  //** setDate
  //**************************************************************************
  /**  Used to update the current date using a predefined java.util.Date */
    
    public void setDate(java.util.Date date){
        currDate = date;
    }
    
    
  //**************************************************************************
  //** setLocale
  //**************************************************************************
  /**  Used to update the current local */
    
    public void setLocale(Locale locale){
        this.currentLocale = locale;
    }
    
    
    
  //**************************************************************************
  //** getLocale
  //**************************************************************************
  /**  Used to retrieve the current local */
    
    public Locale getLocale(){
        return currentLocale;
    }
    
    
  //**************************************************************************
  //** ParseDate
  //**************************************************************************
  /**  Attempts to convert a String to a Date via the user-supplied Format */
    
    private java.util.Date parseDate(String date, String format) throws ParseException {
        if (date!=null){
            date = date.trim();
            if (date.length()==0) date = null; 
        }
        if (date==null) throw new ParseException("Date is null.", 0);
        
        SimpleDateFormat formatter = new SimpleDateFormat(format, currentLocale);
        if (timeZone!=null) formatter.setTimeZone(timeZone);
        
        try{
            java.util.Date d = formatter.parse(date);
            timeZone = formatter.getTimeZone();
            return d;
        }
        catch(java.text.ParseException e){

          //Parse the error. If it's a time zone issue, try to resolve it.
            int zIndex = format.toUpperCase().indexOf("Z");
            if (zIndex>0){
                int errorOffset = e.getErrorOffset();
                String tz = null;


                if (errorOffset < format.length()){

                  //Check if the parser choked on the timezone format
                    String ch = format.substring(errorOffset, errorOffset+1);
                    if (ch.equalsIgnoreCase("Z") && date.length()>errorOffset){
                        tz = date.substring(errorOffset);
                        date = date.substring(0, errorOffset-1);
                        format = format.substring(0, errorOffset-1);
                    }
                }
                else if (errorOffset>format.length()){

                  //Special Case: "Fri Jan 04 2013 00:00:00 GMT-0500 (Eastern Standard Time)"
                    tz = date.substring(zIndex);
                    date = date.substring(0, zIndex-1);
                    format = format.substring(0, zIndex-1);
                }

                if (tz!=null){
                    try{
                        java.util.TimeZone zone = getTimeZone(tz);
                        if (zone!=null){
                            timeZone = zone;
                            formatter = new SimpleDateFormat(format, currentLocale);
                            formatter.setTimeZone(timeZone);
                            return formatter.parse(date);
                        }
                    }
                    catch(Exception ex){
                    }
                }
            }
            throw e;
        }
    }


  //**************************************************************************
  //** setTimeZone
  //**************************************************************************
  /** Used to set the current time zone. The time zone is used when comparing
   *  and formatting dates.
   *  @param timeZone Name of the time zone (e.g. "UTC", "EDT", etc.)
   *  @param preserveTimeStamp Flag used to indicate whether to preserve the
   *  timestamp when changing time zones. Normally, when updating the timezone, 
   *  the timestamp is updated to the new timezone. For example, if the current 
   *  time is 4PM EST and you wish to switch to UTC, the timestamp would be
   *  updated to 8PM. The preserveTimeStamp flag allows users to preserve the
   *  the timestamp so that the timestamp remains fixed at 4PM.
   */
    public void setTimeZone(String timeZone, boolean preserveTimeStamp){
        setTimeZone(getTimeZone(timeZone), preserveTimeStamp);
    }


  //**************************************************************************
  //** setTimeZone
  //**************************************************************************
  /** Used to set the current time zone. The time zone is used when comparing
   *  and formatting dates.
   *  @param timeZone Time zone (e.g. "UTC", "EDT", etc.)
   *  @param preserveTimeStamp Flag used to indicate whether to preserve the
   *  timestamp when changing time zones. Normally, when updating the timezone,
   *  the timestamp is updated to the new timezone. For example, if the current
   *  time is 4PM EST and you wish to switch to UTC, the timestamp would be
   *  updated to 8PM. The preserveTimeStamp flag allows users to preserve the
   *  the timestamp so that the timestamp remains fixed at 4PM.
   */
    public void setTimeZone(java.util.TimeZone timeZone, boolean preserveTimeStamp){

        if (timeZone==null) return;
        

        if (preserveTimeStamp){
            Calendar cal = Calendar.getInstance(timeZone, currentLocale);
            cal.set(Calendar.YEAR, this.getYear());
            cal.set(Calendar.MONTH, this.getMonth()-1);
            cal.set(Calendar.DAY_OF_MONTH, this.getDay());
            cal.set(Calendar.HOUR_OF_DAY, this.getHour());
            cal.set(Calendar.MINUTE, this.getMinute());
            cal.set(Calendar.SECOND, this.getSecond());
            cal.set(Calendar.MILLISECOND, this.getMilliSecond());
            currDate = cal.getTime();
        }


      //Do this last! Otherwise the getHour(), getMinute(), etc will be off...
        this.timeZone = timeZone;
    }


  //**************************************************************************
  //** setTimeZone
  //**************************************************************************
  /** Used to set the current time zone. The time zone is used when comparing
   *  and formatting dates.
   *  @param timeZone Name of the time zone (e.g. "UTC", "EST", etc.)
   */
    public void setTimeZone(String timeZone){
        setTimeZone(timeZone, false);
    }


  //**************************************************************************
  //** setTimeZone
  //**************************************************************************
  /** Used to set the current time zone. The time zone is used when comparing
   *  and formatting dates.
   */
    public void setTimeZone(java.util.TimeZone timeZone){
        setTimeZone(timeZone, false);
    }


  //**************************************************************************
  //** getTimeZone
  //**************************************************************************
  /** Returns the current time zone. The time zone is used when comparing
   *  and formatting dates.
   */
    public java.util.TimeZone getTimeZone(){
        return timeZone;
    }


    public int hashCode(){
        return currDate.hashCode();
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /** Returns the current date as a String in the following format:
   *  "EEE MMM dd HH:mm:ss z yyyy"
   */
    public String toString(){
        return toString("EEE MMM dd HH:mm:ss z yyyy");
    }

    
  //**************************************************************************
  //** toString
  //**************************************************************************
  /** Used to format the current date into a string.
   *  @param format Pattern used to format the date (e.g. "MM/dd/yyyy hh:mm a", 
   *  "EEE MMM dd HH:mm:ss z yyyy", etc). Please refer to the
   *  java.text.SimpleDateFormat class for more information.
   */
    public String toString(String format){
        SimpleDateFormat currFormatter = new SimpleDateFormat(format, currentLocale);
        currFormatter.setTimeZone(timeZone==null ? Calendar.getInstance().getTimeZone() : timeZone);
        return currFormatter.format(currDate);
    }

    public String toString(String format, String timeZone){
        return this.toString(format, getTimeZone(timeZone));
    }

    public String toString(String format, java.util.TimeZone timeZone){
        SimpleDateFormat currFormatter =
            new SimpleDateFormat(format, currentLocale);
        if (timeZone!=null) currFormatter.setTimeZone(timeZone);
        return currFormatter.format(currDate);
    }


  //**************************************************************************
  //** toISOString
  //**************************************************************************
  /** Returns the date in ISO 8601 format (e.g. "2013-01-04T05:00:00.000Z").
   *  Note that ISO dates are in UTC.
   */
    public String toISOString(){
        return toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "UTC");
    }


  //**************************************************************************
  //** toLong
  //**************************************************************************
  /** Returns a long integer used to represent the Date in the following
   *  format: "yyyyMMddHHmmssSSS". The time zone is automatically set to UTC.
   *  This is useful for perform simple date comparisons and storing dates
   *  in a database as integers (e.g. SQLite). Here's an example of how to
   *  go from a date to a long and a long to a date:
   <pre>
        javaxt.utils.Date orgDate = new javaxt.utils.Date();
        Long l = orgDate.toLong(); //"yyyyMMddHHmmssSSS" formatted long in UTC
        javaxt.utils.Date newDate = new javaxt.utils.Date(l+"");
        newDate.setTimeZone("UTC", true);
        System.out.println(newDate);
   </pre>
   *  Note that this method is different from the getTime() method which
   *  returns the number of milliseconds since January 1, 1970, 00:00:00 UTC.
   */
    public long toLong(){
        Date d = this.clone();
        d.setTimeZone("UTC");
        return Long.parseLong(d.toString("yyyyMMddHHmmssSSS"));
    }


//  //**************************************************************************
//  //** toInt
//  //**************************************************************************
//  /** Returns an integer used to represent the Date in the following format:
//   *  "yyyyMMdd". The time zone is automatically set to UTC. Here's an example
//   *  of how to go from a date to an int and an int to a date:
//   <pre>
//        javaxt.utils.Date orgDate = new javaxt.utils.Date();
//        int i = orgDate.toInt(); //"yyyyMMdd" formatted integer in UTC
//        javaxt.utils.Date newDate = new javaxt.utils.Date(i+"");
//        newDate.setTimeZone("UTC", true);
//        System.out.println(newDate);
//   </pre>
//   */
//    public int toInt(){
//        Date d = this.clone();
//        d.setTimeZone("UTC");
//        return Integer.parseInt(d.toString("yyyyMMdd"));
//    }


  //**************************************************************************
  //** clone
  //**************************************************************************
  /** Creates a copy of this object. Any modifications to the clone, will not
   *  affect the original.
   */
    public Date clone(){
        return new Date(getCalendar());
    }


  //**************************************************************************
  //** equals
  //**************************************************************************
  /** Used to compare dates and determine whether they are equal.
   *  @param obj Accepts a java.util.Date, a javaxt.utils.Date, or a String.
   */
    public boolean equals(Object obj){
        if (obj==null) return false;
        if (obj instanceof javaxt.utils.Date){
            return ((javaxt.utils.Date) obj).getDate().equals(currDate);
        }
        else if (obj instanceof java.util.Date){
            return ((java.util.Date) obj).equals(currDate);
        }
        else if (obj instanceof String){
            try{
                return new javaxt.utils.Date((String) obj).equals(currDate);
            }
            catch(ParseException e){}
        }
        return false;
    }
    

    
  //**************************************************************************
  //** FormatDate
  //**************************************************************************
    
    private String FormatDate(java.util.Date date, String OutputFormat){
        SimpleDateFormat formatter = 
                new SimpleDateFormat(OutputFormat, currentLocale);
        if (timeZone != null) formatter.setTimeZone(timeZone);
        return formatter.format(date);
    }
   
    
  //**************************************************************************
  //** compareTo
  //**************************************************************************
  /** Used to compare dates. Returns the number of intervals between two dates.
   *  If the given date is in the future, returns a negative value. If the
   *  given date is in the past, returns a positive value. 
   *  @param units Units of measure (e.g. hours, minutes, seconds, weeks,
   *  months, years, etc.)
   */    
    public long compareTo(javaxt.utils.Date date, String units){
        return DateDiff(currDate, date.getDate(), units);
    }

    
  //**************************************************************************
  //** compareTo
  //**************************************************************************
  /** Used to compare dates. Returns the number of intervals between two dates
   *  @param units Units of measure (e.g. hours, minutes, seconds, weeks, 
   *  months, years, etc.)
   */
    public long compareTo(java.util.Date date, String units){
        return DateDiff(currDate, date, units);
    }
    
    
  //**************************************************************************
  //** DateDiff
  //**************************************************************************
  /**  Implements compareTo public members */
    
    private long DateDiff(java.util.Date date1, java.util.Date date2, String interval){
                
        double div = 1;
        if (interval.equals("S") || interval.toLowerCase().startsWith("sec")){
            div = 1000L;
        }        
        if (interval.equals("m") || interval.toLowerCase().startsWith("min")){
            div = 60L * 1000L;
        }
        if (interval.equals("H") || interval.toLowerCase().startsWith("h")){
            div = 60L * 60L * 1000L;
        }
        if (interval.equals("d") || interval.toLowerCase().startsWith("d")){
            div = 24L * 60L * 60L * 1000L;
        }
        if (interval.equals("w") || interval.toLowerCase().startsWith("w")){
            div = 7L * 24L * 60L * 60L * 1000L;
        }       
        if (interval.equals("M") || interval.toLowerCase().startsWith("mon")){
            div = 30L * 24L * 60L * 60L * 1000L;
        }     
        if (interval.equals("y") || interval.toLowerCase().startsWith("y")){
            div = 365L * 24L * 60L * 60L * 1000L;
        }    
        
        long d1 = date1.getTime();
        long d2 = date2.getTime();
        

        int i2 = (int)Math.abs((d1 - d2) / div);         
        if (date2.after(date1)){
            i2 = -i2;
        }
        
        return i2;
    }


    public boolean isBefore(String date) throws ParseException {
        return isBefore(new javaxt.utils.Date(date));
    }
    
    public boolean isBefore(javaxt.utils.Date Date){
        return currDate.before(Date.getDate());
    }

    public boolean isAfter(String date) throws ParseException {
        return isAfter(new javaxt.utils.Date(date));
    }
    
    public boolean isAfter(javaxt.utils.Date Date){
        return currDate.after(Date.getDate());
    }


    
    
  //**************************************************************************
  //** Add
  //**************************************************************************
  /** Used to update the current date by adding to (or subtracting from) the
   *  current date. Example:
   <pre>
    javaxt.utils.Date date = new javaxt.utils.Date();
    System.out.println("Today is: " + date);
    date.add(-1, "day");
    System.out.println("Yesterday was: " + date);
   </pre>
   *  @param units Unit of measure (e.g. hours, minutes, seconds, days, weeks,
   *  months, years, etc.)
   */    
    public java.util.Date add(int amount, String units){

        Calendar cal = Calendar.getInstance();
        cal.setTime(currDate);
        
        int div = 0;
        if (units.equals("S") || units.toLowerCase().startsWith("ms") || units.toLowerCase().startsWith("mil")){
            div = cal.MILLISECOND;
        }
        if (units.equals("s") || units.toLowerCase().startsWith("sec")){
            div = cal.SECOND;
        }
        else if(units.equals("m") || units.toLowerCase().startsWith("min")){
            div = cal.MINUTE;
        }
        else if (units.equals("H") || units.toLowerCase().startsWith("h")){
            div = cal.HOUR_OF_DAY;
        }
        else if (units.toLowerCase().startsWith("d")){
            div = cal.DAY_OF_YEAR;
        }
        else if (units.toLowerCase().startsWith("w")){
            div = cal.WEEK_OF_YEAR;
        }
        else if (units.equals("M") || units.toLowerCase().startsWith("mon")){
            div = cal.MONTH;
        }
        else if (units.toLowerCase().startsWith("y")){
            div = cal.YEAR;
        }
        cal.add(div, amount);
        currDate = cal.getTime();
        return currDate;
    }

    
  //**************************************************************************
  //** setTime
  //**************************************************************************
  /** Used to update the hours, minutes, seconds, and milliseconds of the
   *  current date.
   */
    public java.util.Date setTime(int hours, int minutes, int seconds, int milliseconds){
        Calendar cal = getCalendar();
        
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);
        cal.set(Calendar.MILLISECOND, milliseconds);
        
        currDate = cal.getTime();
        return currDate;
    }

    
  //**************************************************************************
  //** getDate
  //**************************************************************************
  /**  Returns the java.utils.Date representation of this object */
    
    public java.util.Date getDate(){
        return currDate;
    }
    
    
  //**************************************************************************
  //** getTime
  //**************************************************************************
  /** Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
   *  represented by this Date object.
   */
    public long getTime(){
        return getCalendar().getTimeInMillis();
    }

    
  //**************************************************************************
  //** getCalendar
  //**************************************************************************
  /**  Returns the java.utils.Calender representation of this object */
    
    public Calendar getCalendar(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currDate);
        if (this.timeZone!=null) cal.setTimeZone(timeZone);
        return cal;
    }


  //**************************************************************************
  //** getWeekdayName
  //**************************************************************************
  /**  Returns the name of the day of the week. Example: "Monday" */
    
    public String getWeekdayName(){
        return FormatDate(currDate, "EEEEEE");
    }
    
    
  //**************************************************************************
  //** getWeekdayName
  //**************************************************************************
  /**  Returns the name of the month. Example: "January" */
    
    public String getMonthName(){
        return FormatDate(currDate, "MMMMMM");
    }
    
    
  //**************************************************************************
  //** getDayOfWeek
  //**************************************************************************
  /**  Returns the day of the week. Example: Monday = 1 */
    
    public int getDayOfWeek(){
        int dayOfWeek = this.getCalendar().get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) dayOfWeek = 7;
        return dayOfWeek;
    }
    
    
  //**************************************************************************
  //** getWeekInMonth
  //**************************************************************************
  /**  Returns the week number in a given month. Example: 11/14/2006 = 3 */
    
    public int getWeekInMonth(){
        return Integer.valueOf(FormatDate(currDate, "W")).intValue();
    }
    

    
  //**************************************************************************
  //** getDayInYear
  //**************************************************************************
  /**  Returns the day of the year. Example: 11/14/2006 = 318 */
    
    public int getDayInYear(){
        return Integer.valueOf(FormatDate(currDate, "D")).intValue();
    }

    
  //**************************************************************************
  //** getWeekInYear
  //**************************************************************************
  /**  Returns the week number within a given year. Example: 11/14/2006 = 46 */
    
    public int getWeekInYear(){
        return Integer.valueOf(FormatDate(currDate, "w")).intValue();
    }
    
    
  //**************************************************************************
  //** getYear
  //**************************************************************************
  /**  Returns the current year. Example: 11/14/2006 = 2006 */
    
    public int getYear(){
        return Integer.valueOf(FormatDate(currDate, "yyyy")).intValue();
    }
    
    
  //**************************************************************************
  //** getMonth
  //**************************************************************************
  /**  Returns the current month. Example: 11/14/2006 = 11 */
    
    public int getMonth(){
        return Integer.valueOf(FormatDate(currDate, "MM")).intValue();
    }    
    
  //**************************************************************************
  //** getDay
  //**************************************************************************
  /**  Returns the current day of the month. Example: 11/14/2006 = 14 */
    
    public int getDay(){
        return Integer.valueOf(FormatDate(currDate, "dd")).intValue();
    }
    
    
  //**************************************************************************
  //** getHour
  //**************************************************************************
  /**  Returns the current hour of the day. Example: 12:00 AM = 0, 1:00 PM = 13 
   */
    
    public int getHour(){
        return Integer.valueOf(FormatDate(currDate, "HH")).intValue();
    }
    
    
  //**************************************************************************
  //** getMinute
  //**************************************************************************
  /**  Returns the current minute of the hour. Example: 12:01 = 1  */
    
    public int getMinute(){
        return Integer.valueOf(FormatDate(currDate, "m")).intValue();
    }
    
    
  //**************************************************************************
  //** getSecond
  //**************************************************************************
  /**  Returns the current second of the minute. Example: 12:00:01 = 1  */
    
    public int getSecond(){
        return Integer.valueOf(FormatDate(currDate, "s")).intValue();
    }
    
    
  //**************************************************************************
  //** getMilliSecond
  //**************************************************************************
  /**  Returns the current millisecond of the second. Example: 12:00:00:01 = 1*/
    
    public int getMilliSecond(){
        return Integer.valueOf(FormatDate(currDate, "S")).intValue();
    }


  //**************************************************************************
  //** hasTimeStamp
  //**************************************************************************
  /** Used to determine whether the date has a timestamp.
   */
    public boolean hasTimeStamp(){
        int hour = this.getHour();
        int min = this.getMinute();
        int sec = this.getSecond();
        int ms = this.getMilliSecond();
        if (hour>0 || min>0 || sec>0 || ms>0) return true;
        return false;
    }


  //**************************************************************************
  //** removeTimeStamp
  //**************************************************************************
  /** Updates the date by removing the timestamp
   */
    public void removeTimeStamp(){
        currDate = getShortDate().currDate;
    }


    private javaxt.utils.Date getShortDate(){
        try{
            java.util.TimeZone tz = getTimeZone();
            javaxt.utils.Date date = new javaxt.utils.Date(toString("MM/dd/yyyy"));
            date.setTimeZone(tz, true);
            return date;
        }
        catch(Exception e){
            return this; //Should never happen!
        }
    }
    

  //**************************************************************************
  //** compareTo
  //**************************************************************************
  /** Compares two dates for ordering. Older dates appear first in an ordered
   *  list like a TreeSet.
   */
    public int compareTo(Object obj){
        return new DateComparer().compare(this, obj);
    }


  //**************************************************************************
  //** DateComparer Class
  //**************************************************************************
  /** Used to compare dates.
   */
    private static class DateComparer implements java.util.Comparator {

        public final int compare(Object a, Object b){

            if (a==null || b==null) return -1;

            java.util.Date d1 = null;
            java.util.Date d2 = null;

            if (a instanceof java.util.Date) d1 = ((java.util.Date) a);
            else if (a instanceof javaxt.utils.Date) d1 = ((javaxt.utils.Date) a).getDate();

            if (b instanceof java.util.Date) d2 = ((java.util.Date) b);
            else if (b instanceof javaxt.utils.Date) d2 = ((javaxt.utils.Date) b).getDate();


            if (d1==null || d2==null) return -1;


            Long x = d1.getTime();
            Long y = d2.getTime();
            return x.compareTo(y);
        }
    }
    

  //**************************************************************************
  //** sortDates
  //**************************************************************************
  /** Static method used to sort dates in a list. Older dates appear first in
   *  the output.
   */
    public static java.util.List sortDates(java.util.List dates){
        while(dates.contains(null)){
            dates.remove(null);
        }
        java.util.Collections.sort(dates, new DateComparer());
        return dates;
    }


  //**************************************************************************
  //** getTimeZone
  //**************************************************************************
  /** Static method used to return a timezone for a given ID. Unlike the 
   *  java.util.TimeZone.getTimeZone() method, this method will return a null
   *  if a given ID cannot be understood.
   *
   *  @param timezone The name or ID of a TimeZone. Supports common
   *  abbreviations such as "EST" or "EDT", full names such as "Eastern
   *  Standard Time" or "America/New York", and raw GMT offsets such as
   *  "GMT-8:00".
   */
    public static java.util.TimeZone getTimeZone(String timezone){

      //Validate timezone name/id
        if (timezone==null) return null;
        timezone = timezone.trim();
        if (timezone.length()==0) return null;
        

      //Update the string
        timezone = timezone.toUpperCase();
        if (timezone.startsWith("UTC+") || timezone.startsWith("UTC-")){
            timezone = "GMT" + timezone.substring(3);
        }
        else if (( 
            timezone.startsWith("AMERICA/") || timezone.startsWith("AFRICA/") ||
            timezone.startsWith("EUROPE/") || timezone.startsWith("ASIA/") ||
            timezone.startsWith("AUSTRALIA/") || timezone.startsWith("PACIFIC/") ||
            timezone.startsWith("ATLANTIC/") || timezone.startsWith("INDIAN/")
            ) && timezone.contains(" ")){
            timezone = timezone.replace(" ", "_");
        }


      //Special case for timezones like "GMT-0500 (Eastern Standard Time)"
        if (timezone.startsWith("GMT")){

            if (timezone.contains(" ")){
                timezone = timezone.substring(0, timezone.indexOf(" ")).trim();
            }

            if (!timezone.contains(":")){
                int x = 3;
                String str = timezone.substring(x);
                if (str.startsWith("+") || str.startsWith("-")){
                    str = str.substring(1);
                    x++;
                }

                int y = str.indexOf(" ");
                if (y==-1) y = str.length();

                str = str.substring(0, y);
                if (str.length()==4) x = x+2;
                else x = -1;

                if (x>0){
                    timezone = timezone.substring(0, x) + ":" + timezone.substring(x);
                }
            }
        }


        timezone = timezones.get(timezone);
        if (timezone!=null) return java.util.TimeZone.getTimeZone(timezone);
        

        return null;
    }


  //**************************************************************************
  //** TimeZones
  //**************************************************************************
  /** Returns a hashmap of all known time zones. Includes time zones packaged
   *  with Java, Microsoft, and a few others. 
   */
    public static HashMap<String, String> getTimeZones(){
        return timezones;
    }
    

  //**************************************************************************
  //** Java TimeZones
  //**************************************************************************
  /** Updates the hashmap of all known time zones with ones found in Java.
   */
    static {

        timezones.put("EDT","EST5EDT");
        timezones.put("CDT","CST6CDT");
        timezones.put("MDT","MST7MDT");
        timezones.put("PDT","PST8PDT");

        for (String id : java.util.TimeZone.getAvailableIDs()){
            java.util.TimeZone timezone = java.util.TimeZone.getTimeZone(id);

          //Add standard timezone name/ID
            timezones.put(id.toUpperCase(), id);

          //Add short name
            String shortName = timezone.getDisplayName(true, java.util.TimeZone.SHORT);
            if (!timezones.containsKey(shortName)) timezones.put(shortName.toUpperCase(), id);

          //Add GMT offset
            double offset = timezone.getRawOffset();
            String str = (offset/(60*60*1000.0))+"";
            int h = Integer.parseInt(str.substring(0, str.indexOf(".")));
            double m = Double.parseDouble("0." + str.substring(str.indexOf(".")+1))*60;
            String gmt =
                    "GMT" + (h>-1 ? "+" : "-") +
                    String.format("%02d", (h<0 ? -h : h)) + ":" +
                    String.format("%02d", (int) javaxt.utils.string.round(m, 0));
            if (!timezones.containsKey(gmt)) timezones.put(gmt, gmt);
        }
    }


  //**************************************************************************
  //** Microsoft TimeZones
  //**************************************************************************
  /** Updates the hashmap of all known time zones with a list of time of time
   *  zones included in the Windows 7 and Windows Server 2008 R2 products.
   *  These time zone values were available as of October 22, 2009, and may
   *  have changed since then. For a current list of time zones, on a computer
   *  running an updated version of Windows 7 or Windows Server 2008 R2, use
   *  the tzutil /l command. Source:
   *  http://technet.microsoft.com/en-us/library/ff715394%28v=ws.10%29.aspx
   *  <p/>
   *  Time zone mappings were taken from this source:
   *  http://code.google.com/p/java-time-zone-list/source/browse/TimeZones/src/TimeZoneList.java
   */
    static {
        String[][] kvp = {
            { "(UTC+13:00) Nuku'alofa", "Tonga Standard Time", "Pacific/Tongatapu" },
            { "(UTC+12:00) Petropavlovsk-Kamchatsky", "Kamchatka Standard Time", "Asia/Kamchatka" },
            { "(UTC+12:00) Fiji, Marshall Is.", "Fiji Standard Time", "Pacific/Fiji" },
            { "(UTC+12:00) Auckland, Wellington", "New Zealand Standard Time", "Pacific/Auckland" },
            { "(UTC+11:00) Magadan, Solomon Is., New Caledonia", "Central Pacific Standard Time", "Pacific/Guadalcanal" },
            { "(UTC+10:00) Vladivostok", "Vladivostok Standard Time", "Asia/Vladivostok" },
            { "(UTC+10:00) Hobart", "Tasmania Standard Time", "Australia/Hobart" },
            { "(UTC+10:00) Guam, Port Moresby", "West Pacific Standard Time", "Pacific/Port_Moresby" },
            { "(UTC+10:00) Canberra, Melbourne, Sydney", "AUS Eastern Standard Time", "Australia/Sydney" },
            { "(UTC+10:00) Brisbane", "E. Australia Standard Time", "Australia/Brisbane" },
            { "(UTC+09:30) Darwin", "AUS Central Standard Time", "Australia/Darwin" },
            { "(UTC+09:30) Adelaide", "Cen. Australia Standard Time", "Australia/Adelaide" },
            { "(UTC+09:00) Yakutsk", "Yakutsk Standard Time", "Asia/Yakutsk" },
            { "(UTC+09:00) Seoul", "Korea Standard Time", "Asia/Seoul" },
            { "(UTC+09:00) Osaka, Sapporo, Tokyo", "Tokyo Standard Time", "Asia/Tokyo" },
            { "(UTC+08:00) Taipei", "Taipei Standard Time", "Asia/Taipei" },
            { "(UTC+08:00) Perth", "W. Australia Standard Time", "Australia/Perth" },
            { "(UTC+08:00) Kuala Lumpur, Singapore", "Singapore Standard Time", "Asia/Singapore" },
            { "(UTC+08:00) Irkutsk, Ulaan Bataar", "North Asia East Standard Time", "Asia/Irkutsk" },
            { "(UTC+08:00) Beijing, Chongqing, Hong Kong, Urumqi", "China Standard Time", "Asia/Shanghai" },
            { "(UTC+07:00) Krasnoyarsk", "North Asia Standard Time", "Asia/Krasnoyarsk" },
            { "(UTC+07:00) Bangkok, Hanoi, Jakarta", "SE Asia Standard Time", "Asia/Bangkok" },
            { "(UTC+06:30) Yangon (Rangoon)", "Myanmar Standard Time", "Asia/Rangoon" },
            { "(UTC+06:00) Astana, Dhaka", "Central Asia Standard Time", "Asia/Almaty" },
            { "(UTC+06:00) Almaty, Novosibirsk", "N. Central Asia Standard Time", "Asia/Novosibirsk" },
            { "(UTC+05:45) Kathmandu", "Nepal Standard Time", "Asia/Katmandu" },
            { "(UTC+05:30) Sri Jayawardenepura", "Sri Lanka Standard Time", "Asia/Colombo" },
            { "(UTC+05:30) Chennai, Kolkata, Mumbai, New Delhi", "India Standard Time", "Asia/Calcutta" },
            { "(UTC+05:00) Tashkent", "West Asia Standard Time", "Asia/Tashkent" },
            { "(UTC+05:00) Islamabad, Karachi", "Pakistan Standard Time", "Asia/Karachi" },
            { "(UTC+05:00) Ekaterinburg", "Ekaterinburg Standard Time", "Asia/Yekaterinburg" },
            { "(UTC+04:30) Kabul", "Afghanistan Standard Time", "Asia/Kabul" },
            { "(UTC+04:00) Yerevan", "Caucasus Standard Time", "Asia/Yerevan" },
            { "(UTC+04:00) Port Louis", "Mauritius Standard Time", "Indian/Mauritius" },
            { "(UTC+04:00) Baku", "Azerbaijan Standard Time", "Asia/Baku" },
            { "(UTC+04:00) Abu Dhabi, Muscat", "Arabian Standard Time", "Asia/Dubai" },
            { "(UTC+03:30) Tehran", "Iran Standard Time", "Asia/Tehran" },
            { "(UTC+03:00) Tbilisi", "Georgian Standard Time", "Asia/Tbilisi" },
            { "(UTC+03:00) Nairobi", "E. Africa Standard Time", "Africa/Nairobi" },
            { "(UTC+03:00) Moscow, St. Petersburg, Volgograd", "Russian Standard Time", "Europe/Moscow" },
            { "(UTC+03:00) Kuwait, Riyadh", "Arab Standard Time", "Asia/Riyadh" },
            { "(UTC+03:00) Baghdad", "Arabic Standard Time", "Asia/Baghdad" },
            { "(UTC+02:00) Windhoek", "Namibia Standard Time", "Africa/Windhoek" },
            { "(UTC+02:00) Minsk", "E. Europe Standard Time", "Europe/Minsk" },
            { "(UTC+02:00) Jerusalem", "Israel Standard Time", "Asia/Jerusalem" },
            { "(UTC+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius", "FLE Standard Time", "Europe/Kiev" },
            { "(UTC+02:00) Harare, Pretoria", "South Africa Standard Time", "Africa/Johannesburg" },
            { "(UTC+02:00) Cairo", "Egypt Standard Time", "Africa/Cairo" },
            { "(UTC+02:00) Beirut", "Middle East Standard Time", "Asia/Beirut" },
            { "(UTC+02:00) Athens, Bucharest, Istanbul", "GTB Standard Time", "Europe/Istanbul" },
            { "(UTC+02:00) Amman", "Jordan Standard Time", "Asia/Amman" },
            { "(UTC+01:00) West Central Africa", "W. Central Africa Standard Time", "Africa/Lagos" },
            { "(UTC+01:00) Sarajevo, Skopje, Warsaw, Zagreb", "Central European Standard Time", "Europe/Warsaw" },
            { "(UTC+01:00) Brussels, Copenhagen, Madrid, Paris", "Romance Standard Time", "Europe/Paris" },
            { "(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague", "Central Europe Standard Time", "Europe/Budapest" },
            { "(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna", "W. Europe Standard Time", "Europe/Berlin" },
            { "(UTC) Monrovia, Reykjavik", "Greenwich Standard Time", "Atlantic/Reykjavik" },
            { "(UTC) Dublin, Edinburgh, Lisbon, London", "GMT Standard Time", "Europe/London" },
            { "(UTC) Coordinated Universal Time", "UTC", "UTC" },
            { "(UTC) Casablanca", "Morocco Standard Time", "Africa/Casablanca" },
            { "(UTC-12:00) International Date Line West", "Dateline Standard Time", "Etc/GMT+12" },
            { "(UTC-11:00) Midway Island, Samoa", "Samoa Standard Time", "Pacific/Apia" },
            { "(UTC-10:00) Hawaii", "Hawaiian Standard Time", "Pacific/Honolulu" },
            { "(UTC-09:00) Alaska", "Alaskan Standard Time", "America/Anchorage" },
            { "(UTC-08:00) Tijuana, Baja California", "Pacific Standard Time (Mexico)", "America/Tijuana" },
            { "(UTC-08:00) Pacific Time (US & Canada)", "Pacific Standard Time", "America/Los_Angeles" }, //vs "PST8PDT"
            { "(UTC-07:00) Mountain Time (US & Canada)", "Mountain Standard Time", "America/Denver" }, //vs "MST7MDT"
            { "(UTC-07:00) Chihuahua, La Paz, Mazatlan", "Mountain Standard Time (Mexico)", "America/Chihuahua" },
            { "(UTC-07:00) Arizona", "US Mountain Standard Time", "America/Phoenix" },
            { "(UTC-06:00) Saskatchewan", "Canada Central Standard Time", "America/Regina" },
            { "(UTC-06:00) Guadalajara, Mexico City, Monterrey", "Central Standard Time (Mexico)", "America/Mexico_City" },
            { "(UTC-06:00) Central Time (US & Canada)", "Central Standard Time", "America/Chicago" }, //vs "CST6CDT"
            { "(UTC-06:00) Central America", "Central America Standard Time", "America/Guatemala" },
            { "(UTC-05:00) Indiana (East)", "US Eastern Standard Time", "America/Indianapolis" },
            { "(UTC-05:00) Eastern Time (US & Canada)", "Eastern Standard Time", "America/New_York" }, //vs "EST5EDT"
            { "(UTC-05:00) Bogota, Lima, Quito", "SA Pacific Standard Time", "America/Bogota" },
            { "(UTC-04:30) Caracas", "Venezuela Standard Time", "America/Caracas" },
            { "(UTC-04:00) Santiago", "Pacific SA Standard Time", "America/Santiago" },
            { "(UTC-04:00) Manaus", "Central Brazilian Standard Time", "America/Cuiaba" },
            { "(UTC-04:00) Georgetown, La Paz, San Juan", "SA Western Standard Time", "America/La_Paz" },
            { "(UTC-04:00) Atlantic Time (Canada)", "Atlantic Standard Time", "America/Halifax" },
            { "(UTC-04:00) Asuncion", "Paraguay Standard Time", "America/Asuncion" },
            { "(UTC-03:30) Newfoundland", "Newfoundland Standard Time", "America/St_Johns" },
            { "(UTC-03:00) Montevideo", "Montevideo Standard Time", "America/Montevideo" },
            { "(UTC-03:00) Greenland", "Greenland Standard Time", "America/Godthab" },
            { "(UTC-03:00) Cayenne", "SA Eastern Standard Time", "America/Cayenne" },
            { "(UTC-03:00) Buenos Aires", "Argentina Standard Time", "America/Buenos_Aires" },
            { "(UTC-03:00) Brasilia", "E. South America Standard Time", "America/Sao_Paulo" },
            { "(UTC-02:00) Mid-Atlantic", "Mid-Atlantic Standard Time", "Etc/GMT+2" },
            { "(UTC-01:00) Cape Verde Is.", "Cape Verde Standard Time", "Atlantic/Cape_Verde" },
            { "(UTC-01:00) Azores", "Azores Standard Time", "Atlantic/Azores" }
            /*
            ----------------------
            Asia/Ulaanbaatar", "Ulaanbaatar Standard Time
            Asia/Damascus", "Syria Standard Time
            Etc/GMT", "GMT
            Asia/Dhaka", "Bangladesh Standard Time
            Etc/GMT-12", "GMT +12
            Asia/Magadan", "Magadan Standard Time
            Etc/GMT+11", "GMT -11
            Etc/GMT+2", "GMT -02
            */
        };
        for (String[] pair : kvp) {
            timezones.put(pair[0].toUpperCase(), pair[2]);
            timezones.put(pair[1].toUpperCase(), pair[2]);

            String nk = pair[0].toUpperCase().substring(pair[0].indexOf(" ")+1);
            if (!timezones.containsKey(nk)) timezones.put(nk, pair[2]);
        }
    }
}