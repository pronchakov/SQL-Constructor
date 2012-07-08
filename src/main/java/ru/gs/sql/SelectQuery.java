package ru.gs.sql;

import java.util.List;
import ru.gs.sql.exceptions.SQLCreationException;

/**
 * Class for building SELECT SQL query.<br><br>
 * 
 * Example: <br>
 * SelectQuery query = new SelectQuery();<br><br>
 * 
 * query.addField("id");<br>
 * query.addField("name");<br>
 * query.addField("family");<br><br>
 * 
 * query.addFrom("employee");<br>
 * query.addWhere();<br>
 * query.isEquals("age", 27);<br><br>
 * 
 * String queryString = query.toString();<br><br>
 * 
 * Result(queryString): <br>
 * SELECT id,name,family FROM employee WHERE age=27
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public final class SelectQuery extends CommonQuery {
    private static final String EXCEPTION_NULL_TABLE_NAME = "Table name cannot be null or empty";
    private static final String EXCEPTION_NULL_FIELD = "Field name cannot be null or empty";
    private static final String EXCEPTION_NULL_VALUE = "Value cannot be null";
    private static final String AND = "AND ";
    private static final String OR = "OR ";

    /**
     * Create SQL query with just "SELECT" word.
     * 
     */
    public SelectQuery() {
        queryBuilder.append("SELECT ");
    }

    /**
     * Create SQL query with "SELECT" at start and than fields listed, separated by comma.<br>
     * If fields list will be null, than only SELECT will be in query<br><br>
     * 
     * Example: <br>
     * List<String> fields = new ArrayList<String>();<br>
     * fields.add("name");<br>
     * fields.add("family");<br>
     * fields.add("sex");<br>
     * <b>SelectQuery query = new SelectQuery(fields);</b><br><br>
     * 
     * Result: <br>
     * SELECT name,family,sex
     * 
     * @param selectableFieldNames List of strings that represents table fields to select.
     * @throws SQLCreationException When input list of field names is null.
     */
    public SelectQuery(List<String> selectableFieldNames) throws SQLCreationException {
        if (selectableFieldNames == null) {
            throw new SQLCreationException("Selectable fields names list cannot be null");
        }
        queryBuilder.append("SELECT ");
        for (String name : selectableFieldNames) {
            addField(name);
        }
    }

    /**
     * Adds field name to SELECT clause.<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * <b>query.addField("name");<b><br>
     * <b>query.addField("family");<b><br>
     * <b>query.addField("sex");<b><br><br>
     * 
     * Result: 
     * SELECT name,family,sex
     * 
     * @param name
     * @return SelectQuery with added select field or original SelectQuery if name parameter is null
     * @throws SQLCreationException When input name is null or it's length is 0 
     */
    public SelectQuery addField(String name) throws SQLCreationException {
        if (isStringEmptyOrNull(name)) {
            throw new SQLCreationException(EXCEPTION_NULL_FIELD);
        }
        queryBuilder.append(name);
        queryBuilder.append(",");
        return this;
    }

    /**
     * Adds a table name after addFrom() method.
     * Can be invoked several times. In this case, tableNames will be separated by comma.
     * 
     * @param tableName Table name to add in select query
     * @return SelectQuery with added table name, or original SelectQuery is tableName parameter is null.
     * @throws SQLCreationException When table name is null or it's length is 0  
     */
    public SelectQuery addTableName(String tableName) throws SQLCreationException {
        if (isStringEmptyOrNull(tableName)) {
            throw new SQLCreationException(EXCEPTION_NULL_TABLE_NAME);
        }
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
     * @return SelectQuery with added "WHERE" word.
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
     * @return SelectQuery with added FROM clause with table name or original SelectQuery if tableName parameter is null
     * @throws SQLCreationException When table name is null or it's length is 0
     */
    public SelectQuery addFrom(String tableName) throws SQLCreationException {
        if (isStringEmptyOrNull(tableName)) {
            throw new SQLCreationException(EXCEPTION_NULL_TABLE_NAME);
        }
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
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null
     */
    public SelectQuery isEquals(String name, Object value) throws SQLCreationException {
        checkSimpleLikeParameters(name, value);
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
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null
     */
    public SelectQuery andIsEquals(String name, Object value) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(AND);
        }
        isEquals(name, value);
        return this;
    }

    protected boolean isNotFirstLogicalConstraint() {
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
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null
     */
    public SelectQuery orIsEquals(String name, Object value) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(OR);
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
     * @throws SQLCreationException When name parameter is null or empty, or firstValue parameter is null, or secondValue parameter is null
     */
    public SelectQuery between(String name, Object firstValue, Object secondValue) throws SQLCreationException {
        checkBetweenParameters(name, firstValue, secondValue);
        queryBuilder.append(name);
        commonBetween(firstValue, secondValue);
        return this;
    }
    
    private void checkBetweenParameters(String name, Object firstValue, Object secondValue) throws SQLCreationException {
        if (isStringEmptyOrNull(name)) {
            throw new SQLCreationException(EXCEPTION_NULL_FIELD);
        } else if (firstValue == null) {
            throw new SQLCreationException("First value for BETWEEN cannot be null");
        } else if (secondValue == null) {
            throw new SQLCreationException("Second value for BETWEEN cannot be null");
        }
    }
    
    private void commonBetween(Object firstValue, Object secondValue) {
        queryBuilder.append(" BETWEEN ");
        insertValueDependsOnClassNumberOrDate(firstValue);
        queryBuilder.append(" AND ");
        insertValueDependsOnClassNumberOrDate(secondValue);
        queryBuilder.append(" ");
    }
    
    /**
     * Adds a where clause between with prefix NOT<br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.notBetween("age", 12, 18);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE age NOT BETWEEN 12 AND 18
     * 
     * @param name Name of field to NOT BETWEEN
     * @param firstValue First value in BETWEEN expression. Can be String, Integer, Long and Date
     * @param secondValue Second value in BETWEEN expression. Can be String, Integer, Long and Date
     * @return SelectQuery with added BETWEEN where clause
     * @throws SQLCreationException When name parameter is null or empty, or firstValue parameter is null, or secondValue parameter is null
     */
    public SelectQuery notBetween(String name, Object firstValue, Object secondValue) throws SQLCreationException {
        checkBetweenParameters(name, firstValue, secondValue);
        queryBuilder.append(name);
        queryBuilder.append(" NOT");
        commonBetween(firstValue, secondValue);
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
     * <b>query.andBetween("age", 12, 18);</b><br>
     * query.andIsEquals("city", "London");<br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE age BETWEEN 12 AND 18 AND city='London'
     * 
     * @param name Name of field to BETWEEN
     * @param firstValue First value in BETWEEN expression. Can be String, Integer, Long and Date
     * @param secondValue Second value in BETWEEN expression. Can be String, Integer, Long and Date
     * @return SelectQuery with a "AND" keyword and then, added BETWEEN where clause
     * @throws SQLCreationException When name parameter is null or empty, or firstValue parameter is null, or secondValue parameter is null
     */
    public SelectQuery andBetween(String name, Object firstValue, Object secondValue) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(AND);
        }
        between(name, firstValue, secondValue);
        return this;
    }
    
    /**
     * Adds a "AND" keyword and then, where clause between with prefix NOT<br>
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
     * <b>query.andNotBetween("age", 12, 18);</b><br>
     * query.andIsEquals("city", "London");<br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE age NOT BETWEEN 12 AND 18 AND city='London'
     * 
     * @param name Name of field to NOT BETWEEN
     * @param firstValue First value in NOT BETWEEN expression. Can be String, Integer, Long and Date
     * @param secondValue Second value in NOT BETWEEN expression. Can be String, Integer, Long and Date
     * @return SelectQuery with a "AND" keyword and then, added NOT BETWEEN where clause
     * @throws SQLCreationException When name parameter is null or empty, or firstValue parameter is null, or secondValue parameter is null
     */
    public SelectQuery andNotBetween(String name, Object firstValue, Object secondValue) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(AND);
        }
        notBetween(name, firstValue, secondValue);
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
     * <b>query.orBetween("age", 12, 18);</b><br>
     * <b>query.orIsEquals("city", "London");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE age BETWEEN 12 AND 18 OR city='London'
     * 
     * @param name Name of field to BETWEEN
     * @param firstValue First value in BETWEEN expression. Can be String, Integer, Long and Date
     * @param secondValue Second value in BETWEEN expression. Can be String, Integer, Long and Date
     * @return SelectQuery with a "OR" keyword and then, added BETWEEN where clause
     * @throws SQLCreationException When name parameter is null or empty, or firstValue parameter is null, or secondValue parameter is null
     */
    public SelectQuery orBetween(String name, Object firstValue, Object secondValue) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(OR);
        }
        between(name, firstValue, secondValue);
        return this;
    }
    
    /**
     * Adds a "OR" keyword and then, where clause between with prefix NOT<br>
     * If it is first constraint, than "OR" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.isEquals("city", "London");</b><br>
     * <b>query.orNotBetween("age", 12, 18);</b><br><br>
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
     * <b>query.orBetween("age", 12, 18);</b><br>
     * <b>query.orIsEquals("city", "London");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE age BETWEEN 12 AND 18 OR city='London'
     * 
     * @param name Name of field to BETWEEN
     * @param firstValue First value in BETWEEN expression. Can be String, Integer, Long and Date
     * @param secondValue Second value in BETWEEN expression. Can be String, Integer, Long and Date
     * @return SelectQuery with a "OR" keyword and then, added BETWEEN where clause
     * @throws SQLCreationException When name parameter is null or empty, or firstValue parameter is null, or secondValue parameter is null
     */
    public SelectQuery orNotBetween(String name, Object firstValue, Object secondValue) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(OR);
        }
        notBetween(name, firstValue, secondValue);
        return this;
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
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null, or wildcard position parameter is null
     */
    public SelectQuery like(String name, Object value, char wildcard, WildcardPosition position) throws SQLCreationException {
        checkLikeParameters(name, value, position);
        queryBuilder.append(name);
        commonLike(position, wildcard, value);
        return this;
    }
    
    private void checkLikeParameters(String name, Object value, WildcardPosition position) throws SQLCreationException {
        if (isStringEmptyOrNull(name)) {
            throw new SQLCreationException(EXCEPTION_NULL_FIELD);
        } else if (value == null) {
            throw new SQLCreationException(EXCEPTION_NULL_VALUE);
        } else if (position == null) {
            throw new SQLCreationException("Wildcard position cannot be null");
        }
    }
    
    private void commonLike(WildcardPosition position, char wildcard, Object value) {
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
    
    /**
     * Adds a where clause LIKE with wildcard with NOT prefix<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.notLike("family", "Petr", '%', WildcardPosition.AT_END);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family NOT LIKE 'Pert%'
     * 
     * @param name Name of field to NOT LIKE
     * @param value NOT LIKE value. It will be wrapped by ' characters.
     * @param wildcard Wildcard character to add before or after value
     * @param position Position of the wildcard. It's one of the WildcardPosition
     * @return SelectQuery with added NOT LIKE where clause
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null, or wildcard position parameter is null
     */
    public SelectQuery notLike(String name, Object value, char wildcard, WildcardPosition position) throws SQLCreationException {
        checkLikeParameters(name, value, position);
        queryBuilder.append(name);
        queryBuilder.append(" NOT");
        commonLike(position, wildcard, value);
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
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null, or wildcard position parameter is null
     */
    public SelectQuery andLike(String name, Object value, char wildcard, WildcardPosition position) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(AND);
        }
        like(name, value, wildcard, position);
        return this;
    }
    
    /**
     * Adds a "AND" and than, where clause LIKE with wildcard with NOT prefix<br>
     * If it is first constraint, than "AND" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.andNotLike("family", "Petr", '%', WildcardPosition.AT_END);</b><br>
     * <b>query.andNotLike("family", "Petr", '%', WildcardPosition.AT_END);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family NOT LIKE 'Pert%' AND family NOT LIKE 'Pert%'
     * 
     * @param name Name of field to LIKE
     * @param value LIKE value. It will be wrapped by ' characters.
     * @param wildcard Wildcard character to add before or after value
     * @param position Position of the wildcard. It's one of the WildcardPosition
     * @return SelectQuery with added "AND" if it is not first constraint, and than, LIKE where clause
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null, or wildcard position parameter is null
     */
    public SelectQuery andNotLike(String name, Object value, char wildcard, WildcardPosition position) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(AND);
        }
        notLike(name, value, wildcard, position);
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
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null, or wildcard position parameter is null
     */
    public SelectQuery orLike(String name, Object value, char wildcard, WildcardPosition position) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(OR);
        }
        like(name, value, wildcard, position);
        return this;
    }
    
    /**
     * Adds a "OR" and than, where clause LIKE with wildcard with NOT prefix<br>
     * If it is first constraint, than "OR" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.andNotLike("family", "Petr", '%', WildcardPosition.AT_END);</b><br>
     * <b>query.orNotLike("family", "Petr", '%', WildcardPosition.AT_END);</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family NOT LIKE 'Petr%' OR family NOT LIKE 'Petr%'
     * 
     * @param name Name of field to NOT LIKE
     * @param value NOT LIKE value. It will be wrapped by ' characters.
     * @param wildcard Wildcard character to add before or after value
     * @param position Position of the wildcard. It's one of the WildcardPosition
     * @return SelectQuery with added "OR" if it is not first constraint, and than, NOT LIKE where clause
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null, or wildcard position parameter is null
     */
    public SelectQuery orNotLike(String name, Object value, char wildcard, WildcardPosition position) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(OR);
        }
        notLike(name, value, wildcard, position);
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
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null
     */
    public SelectQuery like(String name, Object value) throws SQLCreationException {
        checkSimpleLikeParameters(name, value);
        queryBuilder.append(name);
        commonSimpleLike(value);
        return this;
    }
    
    /**
     * Adds a where clause LIKE without wildcard with NOT prefix<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.notLike("family", "Petr");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family NOT LIKE 'Petr'
     * 
     * @param name Name of field to NOT LIKE
     * @param value NOT LIKE value. It will be wrapped by ' characters.
     * @return SelectQuery with added NOT LIKE where clause without wildcard.
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null
     */
    public SelectQuery notLike(String name, Object value) throws SQLCreationException {
        checkSimpleLikeParameters(name, value);
        queryBuilder.append(name);
        queryBuilder.append(" NOT");
        commonSimpleLike(value);
        return this;
    }
    
    private void checkSimpleLikeParameters(String name, Object value) throws SQLCreationException {
        if (isStringEmptyOrNull(name)) {
            throw new SQLCreationException(EXCEPTION_NULL_FIELD);
        } else if (value == null) {
            throw new SQLCreationException(EXCEPTION_NULL_VALUE);
        }
    }

    private void commonSimpleLike(Object value) {
        queryBuilder.append(" LIKE '");
        queryBuilder.append(value);
        queryBuilder.append("' ");
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
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null
     */
    public SelectQuery andLike(String name, Object value) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(AND);
        }
        like(name, value);
        return this;
    }
    
    /**
     * Adds a "AND" and than, where clause LIKE without wildcard with NOT prefix.<br>
     * If it is first constraint, than "AND" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.andNotLike("family", "Petr");</b><br>
     * <b>query.andNotLike("family", "Petr");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family NOT LIKE 'Petr' AND family NOT LIKE 'Petr'
     * 
     * @param name Name of field to NOT LIKE
     * @param value NOT LIKE value. It will be wrapped by ' characters.
     * @return SelectQuery with added "AND" if it is not first constraint, and than, NOT LIKE where clause without wildcard.
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null
     */
    public SelectQuery andNotLike(String name, Object value) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(AND);
        }
        notLike(name, value);
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
     * SELECT id,name FROM employee WHERE family LIKE 'Petr' OR family LIKE 'Petr'
     * 
     * @param name Name of field to LIKE
     * @param value LIKE value. It will be wrapped by ' characters.
     * @return SelectQuery with added "OR" if it is not first constraint, and than, LIKE where clause without wildcard.
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null
     */
    public SelectQuery orLike(String name, Object value) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(OR);
        }
        like(name, value);
        return this;
    }
    
    /**
     * Adds a "OR" and than, where clause LIKE without wildcard with NOT prefix.<br>
     * If it is first constraint, than "OR" will be ignored<br><br>
     * 
     * Example: <br>
     * SelectQuery query = new SelectQuery();<br>
     * query.addField("id");<br>
     * query.addField("name");<br>
     * query.addFrom("employee");<br>
     * addWhere();<br>
     * <b>query.orNotLike("family", "Petr");</b><br>
     * <b>query.orNotLike("family", "Petr");</b><br><br>
     * 
     * Result: <br>
     * SELECT id,name FROM employee WHERE family NOT LIKE 'Petr' OR family NOT LIKE 'Petr'
     * 
     * @param name Name of field to NOT LIKE
     * @param value NOT LIKE value. It will be wrapped by ' characters.
     * @return SelectQuery with added "OR" if it is not first constraint, and than, LIKE where clause without wildcard.
     * @throws SQLCreationException When name parameter is null or empty, or value parameter is null
     */
    public SelectQuery orNotLike(String name, Object value) throws SQLCreationException {
        if (isNotFirstLogicalConstraint()) {
            queryBuilder.append(OR);
        }
        notLike(name, value);
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
