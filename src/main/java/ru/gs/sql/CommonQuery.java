package ru.gs.sql;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a common query class that extend primary query classes. It has common
 * methods and attributes for them.
 *
 * @see SelectQuery
 * @see InsertQuery
 * @see UpdateQuery
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
abstract class CommonQuery {

	protected StringBuilder queryBuilder = new StringBuilder();
	/**
	 * Default timestamp format to represent date and time in SQL known
	 * format.<br> You can change it by <b>setDateTimeFormat</b> method
	 *
	 * @see #setDateTimeFormat(String dateTimeFormat)
	 */
	public static final String DEFAULT_SQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	protected String dateTimeFormat = DEFAULT_SQL_DATE_TIME_FORMAT;

	protected boolean isStringEmptyOrNull(String string) {
		return string == null || string.isEmpty();
	}

	protected void deleteLastCommaIFExist() {
		char ch = queryBuilder.charAt(queryBuilder.length() - 2);
		if (ch == ',') {
			queryBuilder.deleteCharAt(queryBuilder.length() - 2);
		} else {
			ch = queryBuilder.charAt(queryBuilder.length() - 1);
			if (ch == ',') {
				queryBuilder.deleteCharAt(queryBuilder.length() - 1);
			}
		}
	}

	protected void deleteLastSpaceIFExist() {
		char ch = queryBuilder.charAt(queryBuilder.length() - 1);
		if (ch == ' ') {
			queryBuilder.deleteCharAt(queryBuilder.length() - 1);
		}
	}

	/**
	 * This method clean prepare query and returns it.
	 *
	 * @return SQL query of class type that was used
	 */
	public String getQueryString() {
		deleteLastCommaIFExist();
		deleteLastSpaceIFExist();
		return queryBuilder.toString();
	}

	/**
	 * This method returns string representation of date/time format for SQL
	 * queries.
	 *
	 * @return Current Date/time format string
	 */
	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	/**
	 * This method replace current date/time format for SQL queries with one
	 * from value
	 *
	 * @param dateTimeFormat New date/time format like <b>dd-MM-yyyy
	 * HH:mm:ss</b>
	 */
	public void setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	/**
	 * This standard method can be used to get result SQL query. It also prepare
	 * and clean query and than returns it.
	 *
	 * @return Result SQL query
	 */
	@Override
	public String toString() {
		return getQueryString();
	}

	protected void insertValueDependsOnClass(Object value) {
		insertValueDependsOnClassNumberOrDate(value);
		if (value instanceof String) {
			queryBuilder.append("'");
			queryBuilder.append(value);
			queryBuilder.append("'");
		}
	}

	protected void insertValueDependsOnClassNumberOrDate(Object value) {
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

	protected void updateValueDependsOnClass(String field, Object value) {
		if (isNotFirstLogicalConstraint()) {
			queryBuilder.append(", ");
		}
		updateValueDependsOnClassNumberOrDate(field, value);
		if (value instanceof String) {
			queryBuilder.append(field);
			queryBuilder.append(" = '");
			queryBuilder.append(value);
			queryBuilder.append("'");
		}
	}

	private void updateValueDependsOnClassNumberOrDate(String field, Object value) {
		if (value instanceof Integer || value instanceof Long) {
			queryBuilder.append(field);
			queryBuilder.append(" = ");
			queryBuilder.append(value);
		} else if (value instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
			String formatedDate = sdf.format(value);
			queryBuilder.append(field);
			queryBuilder.append(" = '");
			queryBuilder.append(formatedDate);
			queryBuilder.append("'");
		}
	}

	protected abstract boolean isNotFirstLogicalConstraint();
}
