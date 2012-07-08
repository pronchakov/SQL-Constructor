package ru.gs.sql;

import java.text.SimpleDateFormat;
import java.util.Date;

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

	public UpdateQuery addSet(String field, String value) {
		if (isNotFirstLogicalConstraint()) {
			queryBuilder.append(", ");
		}
		queryBuilder.append(field);
		queryBuilder.append(" = '");
		queryBuilder.append(value);
		queryBuilder.append("'");
		return this;
	}

	public UpdateQuery addSet(String field, Integer value) {
		if (isNotFirstLogicalConstraint()) {
			queryBuilder.append(", ");
		}
		queryBuilder.append(field);
		queryBuilder.append(" = ");
		queryBuilder.append(value);
		return this;
	}

	public UpdateQuery addSet(String field, Date value) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
		String formatedDate = sdf.format(value);
		if (isNotFirstLogicalConstraint()) {
			queryBuilder.append(", ");
		}
		queryBuilder.append(field);
		queryBuilder.append(" = '");
		queryBuilder.append(formatedDate);
		queryBuilder.append("'");
		return this;
	}

	private boolean isNotFirstLogicalConstraint() {
		return !queryBuilder.substring(queryBuilder.length() - 4, queryBuilder.length() - 1).equalsIgnoreCase("SET");
	}
}
