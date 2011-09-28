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
     * Adds WHERE to SQL query.<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * <b>query.addWhere();</b><br>
     * query.isEquals("name", "Petrov");<br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE name='Petrov'
     * 
     * @return
     */
    public SelectQuery addWhere() {
        deleteLastCommaIFExist();
        queryBuilder.append(" WHERE ");
        return this;
    }
    
    /**
     * Adds "FROM" to SQL query.<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * <b>query.addFrom();</b><br>
     * <b>query.addTableName("employee");</b><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee
     * 
     * 
     * @return SelectQuery with added FROM.
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
     * @return SelectQuery with added FROM clause with table name
     */
    public SelectQuery addFrom(String tableName) {
        deleteLastCommaIFExist();
        queryBuilder.append(" FROM ");
        queryBuilder.append(tableName);
        queryBuilder.append(",");
        return this;
    }

    /**
     * Adds a where clause equals<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.isEquals("age", 4);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE age=4
     * 
     * @param name Name of field to equal
     * @param value Object to equals to. Can be String, Integer, Long and Date
     * @return SelectQuery with added equals where clause
     */
    public SelectQuery isEquals(String name, Object value) {
        queryBuilder.append(name);
        queryBuilder.append("=");
        insertValueDependsOnClass(value);
        queryBuilder.append(" ");
        return this;
    }
    
    /**
     * Adds a "AND" keyword and then, where clause equals<br>
     * If it is first constraint, than "AND" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * query.andIsEquals("age", 4);<br>
     * <b>query.andIsEquals("city", "London");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE age=4 AND city='London'
     * 
     * @param name Name of field to equal
     * @param value Object to equals to. Can be String, Integer, Long and Date
     * @return SelectQuery with added "AND" and equals where clause 
     */
    public SelectQuery andIsEquals(String name, Object value) {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append("AND ");
        }
        isEquals(name, value);
        return this;
    }

    private boolean isNotFirstLogicalConstraint() {
        return !queryBuilder.substring(queryBuilder.length() - 6, queryBuilder.length() - 1).equalsIgnoreCase("WHERE");
    }
    
    /**
     * Adds a "OR" keyword and then, where clause equals<br>
     * If it is first constraint, than "OR" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * query.isEquals("age", 4);<br>
     * <b>query.orIsEquals("city", "London");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE age=4 OR city='London'<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * query.orIsEquals("age", 4);<br>
     * <b>query.orIsEquals("city", "London");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE age=4 OR city='London'
     * 
     * @param name Name of field to equal
     * @param value Object to equals to. Can be String, Integer, Long and Date
     * @return SelectQuery with added "OR" and equals where clause 
     */
    public SelectQuery orIsEquals(String name, Object value) {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append("OR ");
        }
        isEquals(name, value);
        return this;
    }

    /**
     * Adds a where clause between<br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.between("age", 12, 18);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE age BETWEEN 12 AND 18
     * 
     * @param name Name of field to BETWEEN
     * @param firstValue First value in BETWEEN expression. Can be String, Integer, Long and Date
     * @param secondValue Second value in BETWEEN expression. Can be String, Integer, Long and Date
     * @return SelectQuery with added BETWEEN where clause
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
     * Adds a "AND" keyword and then, where clause between<br>
     * If it is first constraint, than "AND" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * query.isEquals("city", "London");<br>
     * <b>query.andBetween("age", 12, 18);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE city='London' AND age BETWEEN 12 AND 18<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * query.andIsEquals("city", "London");<br>
     * <b>query.andBetween("age", 12, 18);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE city='London' AND age BETWEEN 12 AND 18
     * 
     * @param name Name of field to BETWEEN
     * @param firstValue First value in BETWEEN expression. Can be String, Integer, Long and Date
     * @param secondValue Second value in BETWEEN expression. Can be String, Integer, Long and Date
     * @return SelectQuery with a "AND" keyword and then, added BETWEEN where clause
     */
    public SelectQuery andBetween(String name, Object firstValue, Object secondValue) {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append("AND ");
        }
        between(name, firstValue, secondValue);
        return this;
    }
    
    /**
     * Adds a "OR" keyword and then, where clause between<br>
     * If it is first constraint, than "OR" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.isEquals("city", "London");</b><br>
     * <b>query.orBetween("age", 12, 18);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE city='London' OR age BETWEEN 12 AND 18<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.orIsEquals("city", "London");</b><br>
     * <b>query.orBetween("age", 12, 18);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE city='London' OR age BETWEEN 12 AND 18
     * 
     * @param name Name of field to BETWEEN
     * @param firstValue First value in BETWEEN expression. Can be String, Integer, Long and Date
     * @param secondValue Second value in BETWEEN expression. Can be String, Integer, Long and Date
     * @return SelectQuery with a "OR" keyword and then, added BETWEEN where clause
     */
    public SelectQuery orBetween(String name, Object firstValue, Object secondValue) {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append("OR ");
        }
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
     * Adds a where clause LIKE with wildcard<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.like("family", "Petr", '%', WildcardPosition.AT_END);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family LIKE 'Pert%'
     * 
     * @param name Name of field to LIKE
     * @param value LIKE value. It will be wrapped by ' characters.
     * @param wildcard Wildcard character to add before or after value
     * @param position Position of the wildcard. It's one of the WildcardPosition
     * @return SelectQuery with added LIKE where clause
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
     * Adds a "AND" and than, where clause LIKE with wildcard<br>
     * If it is first constraint, than "AND" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.andLike("family", "Petr", '%', WildcardPosition.AT_END);</b><br>
     * <b>query.andLike("family", "Petr", '%', WildcardPosition.AT_END);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family LIKE 'Pert%' AND family LIKE 'Pert%'
     * 
     * @param name Name of field to LIKE
     * @param value LIKE value. It will be wrapped by ' characters.
     * @param wildcard Wildcard character to add before or after value
     * @param position Position of the wildcard. It's one of the WildcardPosition
     * @return SelectQuery with added "AND" if it is not first constraint, and than, LIKE where clause
     */
    public SelectQuery andLike(String name, Object value, char wildcard, WildcardPosition position) {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append("AND ");
        }
        like(name, value, wildcard, position);
        return this;
    }
    
    /**
     * Adds a "OR" and than, where clause LIKE with wildcard<br>
     * If it is first constraint, than "OR" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.andLike("family", "Petr", '%', WildcardPosition.AT_END);</b><br>
     * <b>query.orLike("family", "Petr", '%', WildcardPosition.AT_END);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family LIKE 'Petr%' OR family LIKE 'Petr%'
     * 
     * @param name Name of field to LIKE
     * @param value LIKE value. It will be wrapped by ' characters.
     * @param wildcard Wildcard character to add before or after value
     * @param position Position of the wildcard. It's one of the WildcardPosition
     * @return SelectQuery with added "OR" if it is not first constraint, and than, LIKE where clause
     */
    public SelectQuery orLike(String name, Object value, char wildcard, WildcardPosition position) {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append("OR ");
        }
        like(name, value, wildcard, position);
        return this;
    }

    /**
     * Adds a where clause LIKE without wildcard.<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.like("family", "Petr");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family LIKE 'Petr'
     * 
     * @param name Name of field to LIKE
     * @param value LIKE value. It will be wrapped by ' characters.
     * @return SelectQuery with added LIKE where clause without wildcard.
     */
    public SelectQuery like(String name, Object value) {
        queryBuilder.append(name);
        queryBuilder.append(" LIKE '");
        queryBuilder.append(value);
        queryBuilder.append("' ");
        return this;
    }
    
    /**
     * Adds a "AND" and than, where clause LIKE without wildcard.<br>
     * If it is first constraint, than "AND" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.andLike("family", "Petr");</b><br>
     * <b>query.andLike("family", "Petr");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family LIKE 'Petr' AND family LIKE 'Petr'
     * 
     * @param name Name of field to LIKE
     * @param value LIKE value. It will be wrapped by ' characters.
     * @return SelectQuery with added "AND" if it is not first constraint, and than, LIKE where clause without wildcard.
     */
    public SelectQuery andLike(String name, Object value) {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append("AND ");
        }
        like(name, value);
        return this;
    }
    
    /**
     * Adds a "OR" and than, where clause LIKE without wildcard.<br>
     * If it is first constraint, than "OR" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.orLike("family", "Petr");</b><br>
     * <b>query.orLike("family", "Petr");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family LIKE 'Petr'
     * 
     * @param name Name of field to LIKE
     * @param value LIKE value. It will be wrapped by ' characters.
     * @return SelectQuery with added "OR" if it is not first constraint, and than, LIKE where clause without wildcard.
     */
    public SelectQuery orLike(String name, Object value) {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append("OR ");
        }
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
