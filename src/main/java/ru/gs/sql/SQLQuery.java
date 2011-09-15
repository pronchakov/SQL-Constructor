package ru.gs.sql;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public class SQLQuery {
    
    public static final String DEFAULT_SQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    
    private StringBuilder queryBuilder = new StringBuilder();
    private String dateTimeFormat = DEFAULT_SQL_DATE_TIME_FORMAT;
    
    public String getQueryString() {
        deleteLastComma();
        deleteLastSpace();
        return queryBuilder.toString();
    }
    
    private void deleteLastComma() {
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
    
    private void deleteLastSpace() {
        char ch = queryBuilder.charAt(queryBuilder.length() - 1);
        if (ch == ' ') {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }
    }
    
    public void addSelect() {
        queryBuilder.append("SELECT ");
    }
    
    public void addField(String name) {
        queryBuilder.append(name);
        queryBuilder.append(", ");
    }
    
    public void addFrom() {
        deleteLastComma();
        queryBuilder.append("FROM ");
    }
    
    public void addFrom(String tableName) {
        deleteLastComma();
        queryBuilder.append("FROM ");
        queryBuilder.append(tableName);
        queryBuilder.append(", ");
    }
    
    public void addTableName(String tableName) {
        deleteLastComma();
        queryBuilder.append(tableName);
        queryBuilder.append(", ");
    }
    
    public void addWhere() {
        deleteLastComma();
        queryBuilder.append("WHERE ");
    }
    
    public void addWhereClauseEquals(String name, Object value) {
        deleteLastComma();
        queryBuilder.append(name);
        queryBuilder.append(" = ");
        insertValueDependsOnClass(value);
        queryBuilder.append(", ");
    }
    
    public void addWhereClauseBetween(String name, Object firstValue, Object secondValue) {
        deleteLastComma();
        queryBuilder.append(name);
        queryBuilder.append(" BETWEEN ");
        insertValueDependsOnClassNumberOrDate(firstValue);
        queryBuilder.append(" AND ");
        insertValueDependsOnClassNumberOrDate(secondValue);
        queryBuilder.append(", ");
    }

    private void insertValueDependsOnClass(Object value) {
        insertValueDependsOnClassNumberOrDate(value);
        if (value instanceof String) {
            queryBuilder.append("'");
            queryBuilder.append(value);
            queryBuilder.append("'");
        } 
    }
    
    private void insertValueDependsOnClassNumberOrDate(Object value) {
        if (value instanceof Integer || value instanceof Long) {
            queryBuilder.append(value);
        } else if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
            String formatedDate = sdf.format(value);
            queryBuilder.append("'");
            queryBuilder.append(formatedDate);
            queryBuilder.append("'");
        }
    }
    
    public void addWhereClauseLike(String name, Object value, char wildcard, WildcardPosition position) {
        deleteLastComma();
        queryBuilder.append(name);
        queryBuilder.append(" LIKE '");
        switch (position) {
            case AT_START: 
                queryBuilder.append(wildcard);
                queryBuilder.append(value);
                break;
            case AT_END: 
                queryBuilder.append(value);
                queryBuilder.append(wildcard);
                break;
            default: 
                queryBuilder.append(value);
                break;
        }
        queryBuilder.append("', ");
    }
    
    public void addWhereClauseLike(String name, Object value) {
        deleteLastComma();
        queryBuilder.append(name);
        queryBuilder.append(" LIKE '");
        queryBuilder.append(value);
        queryBuilder.append("', ");
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }
    
}
