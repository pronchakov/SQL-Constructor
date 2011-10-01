package ru.gs.sql.test.select;

import ru.gs.sql.WildcardPosition;
import org.junit.Test;
import java.util.Calendar;
import java.util.Date;
import org.junit.BeforeClass;
import ru.gs.sql.SelectQuery;
import ru.gs.sql.exceptions.SQLCreationException;
import static org.junit.Assert.*;

/**
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public class WhereTest {
    
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
    
    private void initQuery() throws SQLCreationException {
        query = new SelectQuery();
        query.addField("name").addField("family").addField("sex");
        query.addFrom("employee");
        query.addWhere();
    }

    @Test
    public void fromEqualsStringTest() throws SQLCreationException {
        initQuery();
        query.isEquals("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name='Petrov'", query.getQueryString());
    }
    
    @Test
    public void fromAndEqualsStringTest() throws SQLCreationException {
        initQuery();
        query.andIsEquals("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name='Petrov'", query.getQueryString());
    }
    
    @Test
    public void fromOrEqualsStringTest() throws SQLCreationException {
        initQuery();
        query.orIsEquals("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name='Petrov'", query.getQueryString());
    }

    @Test
    public void fromEqualsANDStringTest() throws SQLCreationException {
        initQuery();
        query.isEquals("name", "Petrov");
        query.andIsEquals("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name='Petrov' AND name='Petrov'", query.getQueryString());
    }
    
    @Test
    public void fromEqualsORStringTest() throws SQLCreationException {
        initQuery();
        query.isEquals("name", "Petrov");
        query.orIsEquals("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name='Petrov' OR name='Petrov'", query.getQueryString());
    }

    @Test
    public void fromEqualsIntegerTest() throws SQLCreationException {
        initQuery();
        query.isEquals("id", 143);

        assertEquals("SELECT name,family,sex FROM employee WHERE id=143", query.getQueryString());

        initQuery();
        query.isEquals("id", new Integer(143));

        assertEquals("SELECT name,family,sex FROM employee WHERE id=143", query.getQueryString());
    }

    @Test
    public void fromEqualsLongTest() throws SQLCreationException {
        initQuery();
        query.isEquals("id", 12345678912345L);

        assertEquals("SELECT name,family,sex FROM employee WHERE id=12345678912345", query.getQueryString());

        initQuery();
        query.isEquals("id", new Long(12345678912345L));

        assertEquals("SELECT name,family,sex FROM employee WHERE id=12345678912345", query.getQueryString());
    }

    @Test
    public void fromEqualsDateTest() throws SQLCreationException {
        initQuery();

        query.isEquals("birth", testDate);

        assertEquals("SELECT name,family,sex FROM employee WHERE birth='" + testDateString + "'", query.getQueryString());
    }

    @Test
    public void fromBetweenDateTest() throws SQLCreationException {
        initQuery();

        query.between("birth", testDate, testDate);

        assertEquals("SELECT name,family,sex FROM employee WHERE birth BETWEEN '" + testDateString + "' AND '" + testDateString + "'", query.getQueryString());
    }
    
    @Test
    public void fromAndBetweenDateTest() throws SQLCreationException {
        initQuery();

        query.andBetween("birth", testDate, testDate);

        assertEquals("SELECT name,family,sex FROM employee WHERE birth BETWEEN '" + testDateString + "' AND '" + testDateString + "'", query.getQueryString());
    }
    
    @Test
    public void fromOrBetweenDateTest() throws SQLCreationException {
        initQuery();

        query.orBetween("birth", testDate, testDate);

        assertEquals("SELECT name,family,sex FROM employee WHERE birth BETWEEN '" + testDateString + "' AND '" + testDateString + "'", query.getQueryString());
    }

    @Test
    public void fromBetweenNumbersTest() throws SQLCreationException {
        initQuery();

        query.between("id", 12345678912345L, 143);

        assertEquals("SELECT name,family,sex FROM employee WHERE id BETWEEN 12345678912345 AND 143", query.getQueryString());
    }

    @Test
    public void fromLikeSimpleTestString() throws SQLCreationException {
        initQuery();

        query.like("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov'", query.getQueryString());
    }
    
    @Test
    public void fromLikeSimpleTestInteger() throws SQLCreationException {
        initQuery();

        query.like("id", 123);

        assertEquals("SELECT name,family,sex FROM employee WHERE id LIKE '123'", query.getQueryString());
    }
    
    @Test
    public void fromAndLikeSimpleTest() throws SQLCreationException {
        initQuery();

        query.andLike("name", "Petrov");
        query.andLike("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov' AND name LIKE 'Petrov'", query.getQueryString());
    }
    
    @Test
    public void fromOrLikeSimpleTest() throws SQLCreationException {
        initQuery();

        query.orLike("name", "Petrov");
        query.orLike("name", "Petrov");

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov' OR name LIKE 'Petrov'", query.getQueryString());
    }

    @Test
    public void fromLikeWildcardAtStartTest() throws SQLCreationException {
        initQuery();

        query.like("name", "Petrov", '%', WildcardPosition.AT_START);

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE '%Petrov'", query.getQueryString());
    }

    @Test
    public void fromLikeWildcardAtEndTest() throws SQLCreationException {
        initQuery();

        query.like("name", "Petrov", '%', WildcardPosition.AT_END);

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov%'", query.getQueryString());
    }
}
