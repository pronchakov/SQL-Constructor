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
     * @param tableName Table name to insert to
     */
    public InsertQuery(String tableName) {
        queryBuilder.append("INSERT INTO ");
        queryBuilder.append(tableName);
        queryBuilder.append(" ");
    }

    /**
     * When you created InsertQuery like new InsertQuery()
     * to add a table name to insert to, use this method.
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
     * 
     * @param names
     * @return 
     * @throws SQLCreationException
     */
    public InsertQuery addInsertableFieldNames(String[] names) throws SQLCreationException {
        return addInsertableFieldNamesCommonMethod(Arrays.asList(names));
    }

    /**
     * 
     * @param names
     * @return 
     * @throws SQLCreationException
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
     * 
     * @return 
     */
    public InsertQuery startAddingInsertableFieldNames() {
        queryBuilder.append("(");
        return this;
    }

    /**
     * 
     * @return 
     */
    public InsertQuery stopAddingInsertableFieldNames() {
        deleteLastCommaIFExist();
        queryBuilder.append(") ");
        return this;
    }

    /**
     * 
     * @param name
     * @return 
     * @throws SQLCreationException
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
