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

    public InsertQuery() {
        queryBuilder.append("INSERT INTO ");
    }

    public InsertQuery(String dbName) {
        queryBuilder.append("INSERT INTO ");
        queryBuilder.append(dbName);
        queryBuilder.append(" ");
    }

    public InsertQuery addTableName(String name) throws SQLCreationException {
        if (isStringEmptyOrNull(name)) {
            throw new SQLCreationException("Field name cannot be null or empty");
        }
        queryBuilder.append(name);
        queryBuilder.append(" ");
        return this;
    }

    public void addInsertableFieldNames(String[] names) throws SQLCreationException {
        addInsertableFieldNamesCommonMethod(Arrays.asList(names));
    }

    public void addInsertableFieldNames(List<String> names) throws SQLCreationException {
        addInsertableFieldNamesCommonMethod(names);
    }

    private void addInsertableFieldNamesCommonMethod(List<String> names) throws SQLCreationException {
        queryBuilder.append("(");
        for (String name : names) {
            queryBuilder.append(name);
            queryBuilder.append(",");
        }
        deleteLastCommaIFExist();
        queryBuilder.append(") ");
    }

    public void startAddingInsertableFieldNames() {
        queryBuilder.append("(");
    }

    public void stopAddingInsertableFieldNames() {
        deleteLastCommaIFExist();
        queryBuilder.append(") ");
    }

    public void addInsertableFieldName(String name) {
        queryBuilder.append(name);
        queryBuilder.append(",");
    }
}
