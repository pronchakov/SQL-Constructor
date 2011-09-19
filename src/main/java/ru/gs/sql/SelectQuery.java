package ru.gs.sql;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public final class SelectQuery extends CommonQuery {
    
    /**
     * 
     */
    public SelectQuery() {
        queryBuilder.append("SELECT ");
    }
    
    /**
     * 
     * @param selectableFieldNames
     */
    public SelectQuery(List<String> selectableFieldNames) {
        queryBuilder.append("SELECT ");
        for (String name: selectableFieldNames) {
            addField(name);
        }
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public SelectQuery addField(String name) {
        queryBuilder.append(name);
        queryBuilder.append(",");
        return this;
    }

    /**
     * 
     * @param tableName
     * @return
     */
    public SelectQuery addTableName(String tableName) {
        deleteLastCommaIFExist();
        queryBuilder.append(tableName);
        queryBuilder.append(",");
        return this;
    }

    /**
     * 
     * @return
     */
    public SelectQuery addWhere() {
        deleteLastCommaIFExist();
        queryBuilder.append(" WHERE ");
        return this;
    }
    
    /**
     * 
     * @return 
     */
    public SelectQuery addFrom() {
        deleteLastCommaIFExist();
        queryBuilder.append(" FROM ");
        return this;
    }

    /**
     * Adds "FROM [Table Name]" to SQL query.<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * <b>query.addFrom("employee");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee
     * 
     * @param tableName Table name to select from.
     * @return  
     */
    public SelectQuery addFrom(String tableName) {
        deleteLastCommaIFExist();
        queryBuilder.append(" FROM ");
        queryBuilder.append(tableName);
        queryBuilder.append(",");
        return this;
    }
    
    /**
     * Adds a where clause
     * 
     * @param name
     * @param value
     * @return  
     */
    public SelectQuery isEquals(String name, Object value) {
        queryBuilder.append(name);
        queryBuilder.append("=");
        insertValueDependsOnClass(value);
        queryBuilder.append(" ");
        return this;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return  
     */
    public SelectQuery andIsEquals(String name, Object value) {
        queryBuilder.append("AND ");
        isEquals(name, value);
        return this;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return  
     */
    public SelectQuery orIsEquals(String name, Object value) {
        queryBuilder.append("OR ");
        isEquals(name, value);
        return this;
    }

    /**
     * 
     * @param name
     * @param firstValue
     * @param secondValue
     * @return  
     */
    public SelectQuery between(String name, Object firstValue, Object secondValue) {
        queryBuilder.append(name);
        queryBuilder.append(" BETWEEN ");
        insertValueDependsOnClassNumberOrDate(firstValue);
        queryBuilder.append(" AND ");
        insertValueDependsOnClassNumberOrDate(secondValue);
        queryBuilder.append(" ");
        return this;
    }
    
    /**
     * 
     * @param name
     * @param firstValue
     * @param secondValue
     * @return  
     */
    public SelectQuery andBetween(String name, Object firstValue, Object secondValue) {
        queryBuilder.append("AND ");
        between(name, firstValue, secondValue);
        return this;
    }
    
    /**
     * 
     * @param name
     * @param firstValue
     * @param secondValue
     * @return  
     */
    public SelectQuery orBetween(String name, Object firstValue, Object secondValue) {
        queryBuilder.append("OR ");
        between(name, firstValue, secondValue);
        return this;
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

    /**
     * 
     * @param name
     * @param value
     * @param wildcard
     * @param position
     * @return  
     */
    public SelectQuery like(String name, Object value, char wildcard, WildcardPosition position) {
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
        return this;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @param wildcard
     * @param position
     * @return  
     */
    public SelectQuery andLike(String name, Object value, char wildcard, WildcardPosition position) {
        queryBuilder.append("AND ");
        like(name, value, wildcard, position);
        return this;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @param wildcard
     * @param position
     * @return  
     */
    public SelectQuery orLike(String name, Object value, char wildcard, WildcardPosition position) {
        queryBuilder.append("OR ");
        like(name, value, wildcard, position);
        return this;
    }

    /**
     * 
     * @param name
     * @param value
     * @return  
     */
    public SelectQuery like(String name, Object value) {
        queryBuilder.append(name);
        queryBuilder.append(" LIKE '");
        queryBuilder.append(value);
        queryBuilder.append("' ");
        return this;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return  
     */
    public SelectQuery andLike(String name, Object value) {
        queryBuilder.append("AND ");
        like(name, value);
        return this;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return  
     */
    public SelectQuery orLike(String name, Object value) {
        queryBuilder.append("OR ");
        like(name, value);
        return this;
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
