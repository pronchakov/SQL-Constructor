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
import ru.gs.sql.exceptions.SQLCreationException;

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
    public void simpleSelectText() throws SQLCreationException {        
        List<String> fields = new ArrayList<String>();
        fields.add("name");
        fields.add("family");
        fields.add("sex");
        query = new SelectQuery(fields);
        query.addFrom();
        query.addTableName("employee");

        assertEquals("SELECT name,family,sex FROM employee", query.getQueryString());
    }

    private void initQuery() throws SQLCreationException {
        query = new SelectQuery();
        query.addField("name").addField("family").addField("sex");
        query.addFrom("employee");
        query.addWhere();
    }
    
    private void initSimpleQuery() {
        query = new SelectQuery();
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
    
    @Test
    public void wholeSelectTest() throws SQLCreationException {
        initQuery();
        query.like("name", "Petrov", '%', WildcardPosition.AT_END).orIsEquals("name", "Petrov").andIsEquals("name", "Petrov");
        query.orBetween("birth", testDate, testDate).andBetween("birth", testDate, testDate);
        query.orLike("name", "Petrov", '_', WildcardPosition.AT_START).andLike("name", "Petrov", '%', WildcardPosition.AT_START);

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov%' OR name='Petrov' AND name='Petrov' OR birth BETWEEN '" + testDateString + "' AND '" + testDateString + "' AND birth BETWEEN '" + testDateString + "' AND '" + testDateString + "' OR name LIKE '_Petrov' AND name LIKE '%Petrov'", query.getQueryString());
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectConstructorExceptionTest() throws SQLCreationException {
        List<String> nullList = null;
        SelectQuery nullQuery = new SelectQuery(nullList);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectAddFieldExceptionTest() throws SQLCreationException {
        initSimpleQuery();
        query.addField(null);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectAddTableNameExceptionTest() throws SQLCreationException {
        initSimpleQuery();
        query.addField("name");
        query.addFrom();
        query.addTableName(null);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectAddFromTableNameExceptionTest() throws SQLCreationException {
        initSimpleQuery();
        query.addField("name");
        query.addFrom(null);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectIsEqualsFieldNameExceptionTest() throws SQLCreationException {
        initQuery();
        query.isEquals(null, "sample string");
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectIsEqualsValueExceptionTest() throws SQLCreationException {
        initQuery();
        query.isEquals("sample string", null);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectBetweenFieldNameExceptionTest() throws SQLCreationException {
        initQuery();
        query.between(null, 14, 16);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectBetweenFirstValueExceptionTest() throws SQLCreationException {
        initQuery();
        query.between("name", null, 16);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectBetweenSecondValueExceptionTest() throws SQLCreationException {
        initQuery();
        query.between("name", 14, null);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectLikeFieldNameExceptionTest() throws SQLCreationException {
        initQuery();
        query.like(null, "value", '%', WildcardPosition.AT_START);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectLikeValueExceptionTest() throws SQLCreationException {
        initQuery();
        query.like("name", null, '%', WildcardPosition.AT_START);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectLikeWildcardPositionExceptionTest() throws SQLCreationException {
        initQuery();
        query.like("name", "value", '%', null);
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectSimpleLikeFieldNameExceptionTest() throws SQLCreationException {
        initQuery();
        query.like(null, "value");
    }
    
    @Test(expected=SQLCreationException.class)
    public void selectSimpleLikeValueExceptionTest() throws SQLCreationException {
        initQuery();
        query.like("name", null);
    }
    
    @Test
    public void objectEqualsTest() throws SQLCreationException {
        SelectQuery query1 = new SelectQuery();
        SelectQuery query2 = new SelectQuery();
        
        query1.addField("name");
        query1.addFrom("employee");
        query1.addWhere();
        query1.isEquals("id", 1);
        
        query2.addField("name");
        query2.addFrom("employee");
        query2.addWhere();
        query2.isEquals("id", 1);
        
        assertEquals(query1, query2);
        assertEquals(query1.hashCode(), query2.hashCode());
        assertFalse(query1.equals("some string"));
    }

}
