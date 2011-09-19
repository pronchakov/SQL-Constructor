package ru.gs.sql;

/**
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public class UpdateQuery extends CommonQuery {
    
    public UpdateQuery() {
        queryBuilder.append("UPDATE ");
    }
    
    public UpdateQuery(String tableName) {
        queryBuilder.append("UPDATE ");
        queryBuilder.append(tableName);
        queryBuilder.append(" SET ");
    }
    
    @Override
    public void addTableName(String name) {
        queryBuilder.append(name);
        queryBuilder.append(" ");
    }
    
    public void addSET() {
        queryBuilder.append(" SET ");
    }
}
