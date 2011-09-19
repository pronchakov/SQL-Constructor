package ru.gs.sql.test;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.gs.sql.SelectQuery;
import ru.gs.sql.WildcardPosition;
import static org.junit.Assert.*;

public class SelectQueryTest {

    private static Date testDate;
    private static final String testDateString = "2011-09-15 00:22:13.870";
    private static final long testLong = 12345678912345L;
    private static final int testInt = 143;
    private static final String testNameString = "Petrov";
    /* */
    private static final String fieldName = "name";
    private static final String fieldFamily = "family";
    private static final String fieldSex = "sex";
    /* */
    private static final String tableName = "employee";
    /* */
    private static final String whereClauseId = "id";
    private static final String whereClauseName = "name";
    private static final String whereClauseBirth = "birth";
    /* */
    private static final String simpleQueryTest = "SELECT name, family, sex FROM " + tableName;
    private static final String fromEqualsIntegerQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseId + " = " + testInt;
    private static final String fromEqualsStringQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseName + " = '" + testNameString + "'";
    private static final String fromEqualsANDStringQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseName + " = '" + testNameString + "' AND " + whereClauseName + " = '" + testNameString + "'";
    private static final String fromEqualsORStringQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseName + " = '" + testNameString + "' OR " + whereClauseName + " = '" + testNameString + "'";
    private static final String fromEqualsLongQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseId + " = " + testLong;
    private static final String fromEqualsDateQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseBirth + " = '" + testDateString + "'";
    private static final String fromBetweenDatesQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseBirth + " BETWEEN '" + testDateString + "' AND '" + testDateString + "'";
    private static final String fromBetweenNumbersQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseId + " BETWEEN " + testLong + " AND " + testInt + "";
    private static final String fromLikeSimpleQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseName + " LIKE '" + testNameString + "'";
    private static final String fromLikeWildcardAtstartQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseName + " LIKE '%" + testNameString + "'";
    private static final String fromLikeWildcardAtEndQueryTest = "SELECT name, family, sex FROM " + tableName + " WHERE " + whereClauseName + " LIKE '" + testNameString + "%'";
    private SelectQuery query;

    @BeforeClass
    public static void init() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, 2011);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 15);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 22);
        calendar.set(Calendar.SECOND, 13);

        calendar.set(Calendar.MILLISECOND, 870);

        testDate = calendar.getTime();
    }

    @Test
    public void simpleQueryTest() {
        List<String> fields = new ArrayList<String>();
        fields.add(fieldName);
        fields.add(fieldFamily);
        fields.add(fieldSex);
        query = new SelectQuery(fields);
        query.addFrom(tableName);

        assertEquals(simpleQueryTest, query.getQueryString());
    }

    private void initQuery() {
        query = new SelectQuery();
        query.addTableField(fieldName);
        query.addTableField(fieldFamily);
        query.addTableField(fieldSex);
        query.addFrom(tableName);
        query.addWhere();
    }

    @Test
    public void fromEqualsStringTest() {
        initQuery();
        query.addWhereClauseEquals(whereClauseName, testNameString);

        assertEquals(fromEqualsStringQueryTest, query.getQueryString());
    }

    @Test
    public void fromEqualsANDStringTest() {
        initQuery();
        query.addWhereClauseEquals(whereClauseName, testNameString);
        query.addWhereClauseANDEquals(whereClauseName, testNameString);

        assertEquals(fromEqualsANDStringQueryTest, query.getQueryString());
    }
    
    @Test
    public void fromEqualsORStringTest() {
        initQuery();
        query.addWhereClauseEquals(whereClauseName, testNameString);
        query.addWhereClauseOREquals(whereClauseName, testNameString);

        assertEquals(fromEqualsORStringQueryTest, query.getQueryString());
    }

    @Test
    public void fromEqualsIntegerTest() {
        initQuery();
        query.addWhereClauseEquals(whereClauseId, testInt);

        assertEquals(fromEqualsIntegerQueryTest, query.getQueryString());

        initQuery();
        query.addWhereClauseEquals(whereClauseId, new Integer(testInt));

        assertEquals(fromEqualsIntegerQueryTest, query.getQueryString());
    }

    @Test
    public void fromEqualsLongTest() {
        initQuery();
        query.addWhereClauseEquals(whereClauseId, testLong);

        assertEquals(fromEqualsLongQueryTest, query.getQueryString());

        initQuery();
        query.addWhereClauseEquals(whereClauseId, new Long(testLong));

        assertEquals(fromEqualsLongQueryTest, query.getQueryString());
    }

    @Test
    public void fromEqualsDateTest() {
        initQuery();

        query.addWhereClauseEquals(whereClauseBirth, testDate);

        assertEquals(fromEqualsDateQueryTest, query.getQueryString());
    }

    @Test
    public void fromBetweenDateTest() {
        initQuery();

        query.addWhereClauseBetween(whereClauseBirth, testDate, testDate);

        assertEquals(fromBetweenDatesQueryTest, query.getQueryString());
    }

    @Test
    public void fromBetweenNumbersTest() {
        initQuery();

        query.addWhereClauseBetween(whereClauseId, testLong, testInt);

        assertEquals(fromBetweenNumbersQueryTest, query.getQueryString());
    }

    @Test
    public void fromLikeSimpleTest() {
        initQuery();

        query.addWhereClauseLike(whereClauseName, testNameString);

        assertEquals(fromLikeSimpleQueryTest, query.getQueryString());
    }

    @Test
    public void fromLikeWildcardAtStartTest() {
        initQuery();

        query.addWhereClauseLike(whereClauseName, testNameString, '%', WildcardPosition.AT_START);

        assertEquals(fromLikeWildcardAtstartQueryTest, query.getQueryString());
    }

    @Test
    public void fromLikeWildcardAtEndTest() {
        initQuery();

        query.addWhereClauseLike(whereClauseName, testNameString, '%', WildcardPosition.AT_END);

        assertEquals(fromLikeWildcardAtEndQueryTest, query.getQueryString());
    }

}
