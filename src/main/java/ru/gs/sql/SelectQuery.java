package ru.gs.sql;

import java.util.Arrays;
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

    public void addInsertInto() {
        queryBuilder.append("INSERT INTO ");
    }

    public void addInsertIntoDBName(String dbName) {
        queryBuilder.append("INSERT INTO ");
        queryBuilder.append(dbName);
        queryBuilder.append(" ");
    }

    public void addInsertDBName(String name) {
        queryBuilder.append(name);
        queryBuilder.append(" ");
    }

    public void addInsertNames(String[] names) {
        addInsertNamesCommonMethod(Arrays.asList(names));
    }

    public void addIndertNames(List<String> names) {
        addInsertNamesCommonMethod(names);
    }

    private void addInsertNamesCommonMethod(List<String> names) {
        queryBuilder.append("(");
        for(String name: names) {
            queryBuilder.append(name);
            queryBuilder.append(", ");
        }
        deleteLastCommaIFExist();
        deleteLastSpaceIFExist();
        queryBuilder.append(") ");
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    @Override
    public String toString() {
        return getQueryString();
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
