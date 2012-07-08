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

	public UpdateQuery addTableName(String name) {
		queryBuilder.append(name);
		queryBuilder.append(" ");
		return this;
	}

	public UpdateQuery addSet() {
		queryBuilder.append("SET ");
		return this;
	}

	public UpdateQuery addSet(String field, Object value) {
		updateValueDependsOnClass(field, value);
		return this;
	}

	protected boolean isNotFirstLogicalConstraint() {
		return !queryBuilder.substring(queryBuilder.length() - 4, queryBuilder.length() - 1).equalsIgnoreCase("SET");
	}
}
