package ru.gs.sql;

import java.util.Arrays;
import java.util.List;
import ru.gs.sql.exceptions.SQLCreationException;

/**
 * Class for building SQL INSERT queries.
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public class InsertQuery extends CommonQuery {

    /**
     *  Creates initial state query with just only "INSERT INTO"
     */
    public InsertQuery() {
        queryBuilder.append("INSERT INTO ");
    }

    /**
     * Creates initial state query with "INSERT INTO" statements and a table name after it
     * 
     * Example:
     * InsertQuery query = new InsertQuery("employee");
     * 
     * Result:
     * INSERT INTO employee
     * 
     * @param tableName Table name to insert to
     */
    public InsertQuery(String tableName) {
        queryBuilder.append("INSERT INTO ");
        queryBuilder.append(tableName);
        queryBuilder.append(" ");
    }

    /**
     * When you create InsertQuery like new InsertQuery()
     * to add a table name to insert to, use this method.
     * 
     * Example:
     * InsertQuery query = new InsertQuery();
     * query.addTableName("employee");
     * 
     * Result:
     * INSERT INTO employee
     * 
     * @param name Table name to insert to
     * @return InsertQuery with added table name to insert to
     * @throws SQLCreationException When name parameter is null or zero length
     */
    public InsertQuery addTableName(String name) throws SQLCreationException {
        if (isStringEmptyOrNull(name)) {
            throw new SQLCreationException("Field name cannot be null or empty");
        }
        queryBuilder.append(name);
        queryBuilder.append(" ");
        return this;
    }

    /**
     * If you have already defined list(or array) of field names which you want to insert, use this method
     * 
     * Example:
     * String[] fieldNames = new String[]{"id", "name", "family"};
     * 
     * InsertQuery query = new InsertQuery("employee");
     * query.addInsertableFieldNames(fieldNames);
     * 
     * Result:
     * INSERT INTO employee (id,name,family)
     * 
     * @param names String array with field names
     * @return InsertQuery with added field names after table name
     * @throws SQLCreationException If names array is null or empty
     */
    public InsertQuery addInsertableFieldNames(String[] names) throws SQLCreationException {
        return addInsertableFieldNamesCommonMethod(Arrays.asList(names));
    }

    /**
     * If you have already defined list(or array) of field names which you want to insert, use this method
     * 
     * Example:
     * List<String> fieldNames = new ArrayList<String>();
     * fieldNames.add("id");
     * fieldNames.add("name");
     * fieldNames.add("family");
     * 
     * InsertQuery query = new InsertQuery("employee");
     * query.addInsertableFieldNames(fieldNames);
     * 
     * Result:
     * INSERT INTO employee (id,name,family)
     * 
     * @param names List of field names to insert
     * @return InsertQuery with added field names after table name
     * @throws SQLCreationException If names list is null or empty
     */
    public InsertQuery addInsertableFieldNames(List<String> names) throws SQLCreationException {
        return addInsertableFieldNamesCommonMethod(names);
    }

    private InsertQuery addInsertableFieldNamesCommonMethod(List<String> names) throws SQLCreationException {
        if (names == null || names.isEmpty()) {
            throw new SQLCreationException("Names list cannot be null or empty");
        }
        queryBuilder.append("(");
        for (String name : names) {
            queryBuilder.append(name);
            queryBuilder.append(",");
        }
        deleteLastCommaIFExist();
        queryBuilder.append(") ");
        return this;
    }

    /**
     * If you don't know what fields you want to insert, or you don't want to build list or array of it
     * use increment methods to add insertable field names.
     * 
     * With this method you will add start "("
     * 
     * Example:
     * InsertQuery query = new InsertQuery("employee");
     * query.startAddingInsertableFieldNames();
     * // some your code, for example some for() {}
     * addInsertableFieldName(someYourObj.netName());
     * // some your other code
     * query.stopAddingInsertableFieldNames();
     * 
     * Result:
     * INSERT INTO employee (yourname1, yourname2, yourname3)
     * 
     * @return InsertQuery with start "(" for adding insertable field names later
     */
    public InsertQuery startAddingInsertableFieldNames() {
        queryBuilder.append("(");
        return this;
    }

    /**
     * If you don't know what fields you want to insert, or you don't want to build list or array of it
     * use increment methods to add insertable field names.
     * 
     * With this method you will add end ")"
     * 
     * Example:
     * InsertQuery query = new InsertQuery("employee");
     * query.startAddingInsertableFieldNames();
     * // some your code, for example some for() {}
     * addInsertableFieldName(someYourObj.netName());
     * // some your other code
     * query.stopAddingInsertableFieldNames();
     * 
     * Result:
     * INSERT INTO employee (yourname1, yourname2, yourname3)
     * 
     * @return InsertQuery with end ")" after adding insertable field names before
     */
    public InsertQuery stopAddingInsertableFieldNames() {
        deleteLastCommaIFExist();
        queryBuilder.append(") ");
        return this;
    }

    /**
     * Adds field name to insert.
     * 
     * Example:
     * InsertQuery query = new InsertQuery("employee");
     * query.startAddingInsertableFieldNames();
     * query.addInsertableFieldName("id");
     * query.addInsertableFieldName("name");
     * query.addInsertableFieldName("family");
     * query.stopAddingInsertableFieldNames();
     * 
     * Result:
     * INSERT INTO employee (id, name, family)
     * 
     * @param name String with field name to insert
     * @return InsertQuery with added another field name
     * @throws SQLCreationException When name parameter is null or empty
     */
    public InsertQuery addInsertableFieldName(String name) throws SQLCreationException {
        if (name == null || name.length() == 0) {
            throw new SQLCreationException("Field name cannot be null or wmpty string");
        }
        queryBuilder.append(name);
        queryBuilder.append(",");
        return this;
    }
}
