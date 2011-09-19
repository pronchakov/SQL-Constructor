package ru.gs.sql;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public final class SelectQuery extends CommonQuery {
    
    public SelectQuery() {
        queryBuilder.append("SELECT ");
    }
    
    public SelectQuery(List<String> selectableFieldNames) {
        queryBuilder.append("SELECT ");
        for (String name: selectableFieldNames) {
            addTableField(name);
        }
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
    
    public void addWhereClauseEquals(String name, Object value) {
        queryBuilder.append(name);
        queryBuilder.append(" = ");
        insertValueDependsOnClass(value);
        queryBuilder.append(" ");
    }
    
    public void addWhereClauseANDEquals(String name, Object value) {
        queryBuilder.append("AND ");
        queryBuilder.append(name);
        queryBuilder.append(" = ");
        insertValueDependsOnClass(value);
        queryBuilder.append(" ");
    }
    
    public void addWhereClauseOREquals(String name, Object value) {
        queryBuilder.append("OR ");
        queryBuilder.append(name);
        queryBuilder.append(" = ");
        insertValueDependsOnClass(value);
        queryBuilder.append(" ");
    }

    public void addWhereClauseBetween(String name, Object firstValue, Object secondValue) {
        queryBuilder.append(name);
        queryBuilder.append(" BETWEEN ");
        insertValueDependsOnClassNumberOrDate(firstValue);
        queryBuilder.append(" AND ");
        insertValueDependsOnClassNumberOrDate(secondValue);
        queryBuilder.append(" ");
    }
    
    public void addWhereClauseANDBetween(String name, Object firstValue, Object secondValue) {
        queryBuilder.append("AND ");
        queryBuilder.append(name);
        queryBuilder.append(" BETWEEN ");
        insertValueDependsOnClassNumberOrDate(firstValue);
        queryBuilder.append(" AND ");
        insertValueDependsOnClassNumberOrDate(secondValue);
        queryBuilder.append(" ");
    }
    
    public void addWhereClauseORBetween(String name, Object firstValue, Object secondValue) {
        queryBuilder.append("OR ");
        queryBuilder.append(name);
        queryBuilder.append(" BETWEEN ");
        insertValueDependsOnClassNumberOrDate(firstValue);
        queryBuilder.append(" AND ");
        insertValueDependsOnClassNumberOrDate(secondValue);
        queryBuilder.append(" ");
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
        queryBuilder.append("' ");
    }

    public void addWhereClauseLike(String name, Object value) {
        deleteLastCommaIFExist();
        queryBuilder.append(name);
        queryBuilder.append(" LIKE '");
        queryBuilder.append(value);
        queryBuilder.append("' ");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SelectQuery) {
            SelectQuery remoteQuery = (SelectQuery) obj;
            String remoteQueryString = remoteQuery.getQueryString();
            String localQueryString = getQueryString();
            return localQueryString.equals(remoteQueryString);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        String localQueryString = getQueryString();
        return 5 * localQueryString.length() + localQueryString.hashCode();
    }
    
}
