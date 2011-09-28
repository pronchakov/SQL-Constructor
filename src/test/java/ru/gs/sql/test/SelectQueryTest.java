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
    /* */
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
    public void simpleSelectText() {
        List<String> fields = new ArrayList<String>();
        fields.add("name");
        fields.add("family");
        fields.add("sex");
        query = new SelectQuery(fields);
        query.addFrom("employee");

        assertEquals("SELECT name,family,sex FROM employee", query.getQueryString());
    }

    private void initQuery() {
        query = new SelectQuery();
        query.addField("name").addField("family").addField("sex");
        query.addFrom("employee");
        query.addWhere();
    }

    @Test
    public void fromEqualsStringTest() {
        initQuery();
        query.isEquals("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name='Petrov'", query.getQueryString());
    }
    
    @Test
    public void fromAndEqualsStringTest() {
        initQuery();
        query.andIsEquals("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name='Petrov'", query.getQueryString());
    }
    
    @Test
    public void fromOrEqualsStringTest() {
        initQuery();
        query.orIsEquals("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name='Petrov'", query.getQueryString());
    }

    @Test
    public void fromEqualsANDStringTest() {
        initQuery();
        query.isEquals("name", "Petrov");
        query.andIsEquals("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name='Petrov' AND name='Petrov'", query.getQueryString());
    }
    
    @Test
    public void fromEqualsORStringTest() {
        initQuery();
        query.isEquals("name", "Petrov");
        query.orIsEquals("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name='Petrov' OR name='Petrov'", query.getQueryString());
    }

    @Test
    public void fromEqualsIntegerTest() {
        initQuery();
        query.isEquals("id", 143);

        assertEquals("SELECT name,family,sex FROM employee WHERE id=143", query.getQueryString());

        initQuery();
        query.isEquals("id", new Integer(143));

        assertEquals("SELECT name,family,sex FROM employee WHERE id=143", query.getQueryString());
    }

    @Test
    public void fromEqualsLongTest() {
        initQuery();
        query.isEquals("id", 12345678912345L);

        assertEquals("SELECT name,family,sex FROM employee WHERE id=12345678912345", query.getQueryString());

        initQuery();
        query.isEquals("id", new Long(12345678912345L));

        assertEquals("SELECT name,family,sex FROM employee WHERE id=12345678912345", query.getQueryString());
    }

    @Test
    public void fromEqualsDateTest() {
        initQuery();

        query.isEquals("birth", testDate);

        assertEquals("SELECT name,family,sex FROM employee WHERE birth='" + testDateString + "'", query.getQueryString());
    }

    @Test
    public void fromBetweenDateTest() {
        initQuery();

        query.between("birth", testDate, testDate);

        assertEquals("SELECT name,family,sex FROM employee WHERE birth BETWEEN '" + testDateString + "' AND '" + testDateString + "'", query.getQueryString());
    }
    
    @Test
    public void fromAndBetweenDateTest() {
        initQuery();

        query.andBetween("birth", testDate, testDate);

        assertEquals("SELECT name,family,sex FROM employee WHERE birth BETWEEN '" + testDateString + "' AND '" + testDateString + "'", query.getQueryString());
    }
    
    @Test
    public void fromOrBetweenDateTest() {
        initQuery();

        query.orBetween("birth", testDate, testDate);

        assertEquals("SELECT name,family,sex FROM employee WHERE birth BETWEEN '" + testDateString + "' AND '" + testDateString + "'", query.getQueryString());
    }

    @Test
    public void fromBetweenNumbersTest() {
        initQuery();

        query.between("id", 12345678912345L, 143);

        assertEquals("SELECT name,family,sex FROM employee WHERE id BETWEEN 12345678912345 AND 143", query.getQueryString());
    }

    @Test
    public void fromLikeSimpleTest() {
        initQuery();

        query.like("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov'", query.getQueryString());
    }
    
    @Test
    public void fromAndLikeSimpleTest() {
        initQuery();

        query.andLike("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov'", query.getQueryString());
    }
    
    @Test
    public void fromOrLikeSimpleTest() {
        initQuery();

        query.orLike("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov'", query.getQueryString());
    }

    @Test
    public void fromLikeWildcardAtStartTest() {
        initQuery();

        query.like("name", "Petrov", '%', WildcardPosition.AT_START);

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE '%Petrov'", query.getQueryString());
    }

    @Test
    public void fromLikeWildcardAtEndTest() {
        initQuery();

        query.like("name", "Petrov", '%', WildcardPosition.AT_END);

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov%'", query.getQueryString());
    }
    
    @Test
    public void wholeSelectTest() {
        initQuery();
        query.like("name", "Petrov", '%', WildcardPosition.AT_END).orIsEquals("name", "Petrov").andIsEquals("name", "Petrov");
        query.orBetween("birth", testDate, testDate).andBetween("birth", testDate, testDate);
        query.orLike("name", "Petrov", '_', WildcardPosition.AT_START).andLike("name", "Petrov", '%', WildcardPosition.AT_START);

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov%' OR name='Petrov' AND name='Petrov' OR birth BETWEEN '" + testDateString + "' AND '" + testDateString + "' AND birth BETWEEN '" + testDateString + "' AND '" + testDateString + "' OR name LIKE '_Petrov' AND name LIKE '%Petrov'", query.getQueryString());
    }

}
