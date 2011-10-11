package ru.gs.sql;

/**
 * This is a common query class that extend primary query classes.
 * It has common methods and attributes for them.
 * 
 * @see SelectQuery
 * @see InsertQuery
 * @see UpdateQuery
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public abstract class CommonQuery {

    protected StringBuilder queryBuilder = new StringBuilder();
    /**
     * Default timestamp format to represent date and time in SQL known format.<br>
     * You can change it by <b>setDateTimeFormat</b> method
     * 
     * @see #setDateTimeFormat(String dateTimeFormat)
     */
    public static final String DEFAULT_SQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    protected String dateTimeFormat = DEFAULT_SQL_DATE_TIME_FORMAT;

    protected void deleteLastCommaIFExist() {
        char ch = queryBuilder.charAt(queryBuilder.length() - 2);
        if (ch == ',') {
            queryBuilder.deleteCharAt(queryBuilder.length() - 2);
        } else {
            ch = queryBuilder.charAt(queryBuilder.length() - 1);
            if (ch == ',') {
                queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            }
        }
    }

    protected void deleteLastSpaceIFExist() {
        char ch = queryBuilder.charAt(queryBuilder.length() - 1);
        if (ch == ' ') {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }
    }

    /**
     * This method clean prepare query and returns it.
     * 
     * @return SQL query of class type that was used
     */
    public String getQueryString() {
        deleteLastCommaIFExist();
        deleteLastSpaceIFExist();
        return queryBuilder.toString();
    }

    /**
     * This method returns string representation of date/time format for SQL queries.
     * 
     * @return Current Date/time format string
     */
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * This method replace current date/time format for SQL queries with one from value
     * 
     * @param dateTimeFormat New date/time format like <b>dd-MM-yyyy HH:mm:ss</b>
     */
    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    /**
     * This standard method can be used to get result SQL query.
     * It also prepare and clean query and than returns it.
     * 
     * @return Result SQL query
     */
    @Override
    public String toString() {
        return getQueryString();
    }
}
