package ru.gs.sql;

import java.util.Arrays;
import java.util.List;

/**
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

    @Override
    public void addTableName(String name) {
        queryBuilder.append(name);
        queryBuilder.append(" ");
    }

    public void addInsertableFieldNames(String[] names) {
        addInsertableFieldNamesCommonMethod(Arrays.asList(names));
    }

    public void addInsertableFieldNames(List<String> names) {
        addInsertableFieldNamesCommonMethod(names);
    }

    private void addInsertableFieldNamesCommonMethod(List<String> names) {
        queryBuilder.append("(");
        for(String name: names) {
            queryBuilder.append(name);
            queryBuilder.append(",");
        }
        deleteLastCommaIFExist();
        queryBuilder.append(") ");
    }
    
}
