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
    /* */
    private static final String simpleQueryTest = "SELECT name,family,sex FROM employee";
    private static final String fromEqualsIntegerQueryTest = "SELECT name,family,sex FROM employee WHERE id=143";
    private static final String fromEqualsStringQueryTest = "SELECT name,family,sex FROM employee WHERE name='Petrov'";
    private static final String fromEqualsANDStringQueryTest = "SELECT name,family,sex FROM employee WHERE name='Petrov' AND name='Petrov'";
    private static final String fromEqualsORStringQueryTest = "SELECT name,family,sex FROM employee WHERE name='Petrov' OR name='Petrov'";
    private static final String fromEqualsLongQueryTest = "SELECT name,family,sex FROM employee WHERE id=12345678912345";
    private static final String fromEqualsDateQueryTest = "SELECT name,family,sex FROM employee WHERE birth='2011-09-15 00:22:13.870'";
    private static final String fromBetweenDatesQueryTest = "SELECT name,family,sex FROM employee WHERE birth BETWEEN '2011-09-15 00:22:13.870' AND '2011-09-15 00:22:13.870'";
    private static final String fromBetweenNumbersQueryTest = "SELECT name,family,sex FROM employee WHERE id BETWEEN 12345678912345 AND 143";
    private static final String fromLikeSimpleQueryTest = "SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov'";
    private static final String fromLikeWildcardAtstartQueryTest = "SELECT name,family,sex FROM employee WHERE name LIKE '%Petrov'";
    private static final String fromLikeWildcardAtEndQueryTest = "SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov%'";
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
        fields.add("name");
        fields.add("family");
        fields.add("sex");
        query = new SelectQuery(fields);
        query.addFrom("employee");

        assertEquals(simpleQueryTest, query.getQueryString());
    }

    private void initQuery() {
        query = new SelectQuery();
        query.addTableField("name");
        query.addTableField("family");
        query.addTableField("sex");
        query.addFrom("employee");
        query.addWhere();
    }

    @Test
    public void fromEqualsStringTest() {
        initQuery();
        query.addWhereClauseEquals("name", "Petrov");

        assertEquals(fromEqualsStringQueryTest, query.getQueryString());
    }

    @Test
    public void fromEqualsANDStringTest() {
        initQuery();
        query.addWhereClauseEquals("name", "Petrov");
        query.addWhereClauseANDEquals("name", "Petrov");

        assertEquals(fromEqualsANDStringQueryTest, query.getQueryString());
    }
    
    @Test
    public void fromEqualsORStringTest() {
        initQuery();
        query.addWhereClauseEquals("name", "Petrov");
        query.addWhereClauseOREquals("name", "Petrov");

        assertEquals(fromEqualsORStringQueryTest, query.getQueryString());
    }

    @Test
    public void fromEqualsIntegerTest() {
        initQuery();
        query.addWhereClauseEquals("id", 143);

        assertEquals(fromEqualsIntegerQueryTest, query.getQueryString());

        initQuery();
        query.addWhereClauseEquals("id", new Integer(143));

        assertEquals(fromEqualsIntegerQueryTest, query.getQueryString());
    }

    @Test
    public void fromEqualsLongTest() {
        initQuery();
        query.addWhereClauseEquals("id", 12345678912345L);

        assertEquals(fromEqualsLongQueryTest, query.getQueryString());

        initQuery();
        query.addWhereClauseEquals("id", new Long(12345678912345L));

        assertEquals(fromEqualsLongQueryTest, query.getQueryString());
    }

    @Test
    public void fromEqualsDateTest() {
        initQuery();

        query.addWhereClauseEquals("birth", testDate);

        assertEquals(fromEqualsDateQueryTest, query.getQueryString());
    }

    @Test
    public void fromBetweenDateTest() {
        initQuery();

        query.addWhereClauseBetween("birth", testDate, testDate);

        assertEquals(fromBetweenDatesQueryTest, query.getQueryString());
    }

    @Test
    public void fromBetweenNumbersTest() {
        initQuery();

        query.addWhereClauseBetween("id", 12345678912345L, 143);

        assertEquals(fromBetweenNumbersQueryTest, query.getQueryString());
    }

    @Test
    public void fromLikeSimpleTest() {
        initQuery();

        query.addWhereClauseLike("name", "Petrov");

        assertEquals(fromLikeSimpleQueryTest, query.getQueryString());
    }

    @Test
    public void fromLikeWildcardAtStartTest() {
        initQuery();

        query.addWhereClauseLike("name", "Petrov", '%', WildcardPosition.AT_START);

        assertEquals(fromLikeWildcardAtstartQueryTest, query.getQueryString());
    }

    @Test
    public void fromLikeWildcardAtEndTest() {
        initQuery();

        query.addWhereClauseLike("name", "Petrov", '%', WildcardPosition.AT_END);

        assertEquals(fromLikeWildcardAtEndQueryTest, query.getQueryString());
    }
    
    @Test
    public void wholeSelectTest() {
        initQuery();
        query.addWhereClauseLike("name", "Petrov", '%', WildcardPosition.AT_END);
        query.addWhereClauseOREquals("name", "Petrov");
        query.addWhereClauseANDEquals("name", "Petrov");
        query.addWhereClauseORBetween("birth", testDate, testDate);
        query.addWhereClauseANDBetween("birth", testDate, testDate);
        query.addWhereClauseORLike("name", "Petrov", '_', WildcardPosition.AT_START);
        query.addWhereClauseANDLike("name", "Petrov", '%', WildcardPosition.AT_START);

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov%' OR name='Petrov' AND name='Petrov' OR birth BETWEEN '2011-09-15 00:22:13.870' AND '2011-09-15 00:22:13.870' AND birth BETWEEN '2011-09-15 00:22:13.870' AND '2011-09-15 00:22:13.870' OR name LIKE '_Petrov' AND name LIKE '%Petrov'", query.getQueryString());
    }

}
