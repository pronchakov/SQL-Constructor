package ru.gs.sql;

/**
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public abstract class CommonQuery {

    protected StringBuilder queryBuilder = new StringBuilder();
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

    public String getQueryString() {
        deleteLastCommaIFExist();
        deleteLastSpaceIFExist();
        return queryBuilder.toString();
    }

    public void addTableField(String name) {
        queryBuilder.append(name);
        queryBuilder.append(", ");
    }

    public void addTableName(String tableName) {
        deleteLastCommaIFExist();
        queryBuilder.append(tableName);
        queryBuilder.append(", ");
    }

    public void addWhere() {
        deleteLastCommaIFExist();
        queryBuilder.append("WHERE ");
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    @Override
    public String toString() {
        return getQueryString();
    }
}
