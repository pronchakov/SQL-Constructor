package ru.gs.sql;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public void addFrom() {
        deleteLastCommaIFExist();
        queryBuilder.append("FROM ");
    }

    public void addFrom(String tableName) {
        deleteLastCommaIFExist();
        queryBuilder.append("FROM ");
        queryBuilder.append(tableName);
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
    
        public void addWhereClauseEquals(String name, Object value) {
        deleteLastCommaIFExist();
        queryBuilder.append(name);
        queryBuilder.append(" = ");
        insertValueDependsOnClass(value);
        queryBuilder.append(", ");
    }

    public void addWhereClauseBetween(String name, Object firstValue, Object secondValue) {
        deleteLastCommaIFExist();
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
        deleteLastCommaIFExist();
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
        deleteLastCommaIFExist();
        queryBuilder.append(name);
        queryBuilder.append(" LIKE '");
        queryBuilder.append(value);
        queryBuilder.append("', ");
    }
}
