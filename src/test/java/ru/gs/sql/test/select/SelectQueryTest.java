package ru.gs.sql.test.select;

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
    
    private void initQuery() throws SQLCreationException {
        query = new SelectQuery();
        query.addField("name").addField("family").addField("sex");
        query.addFrom("employee");
        query.addWhere();
    }

    @Test
    public void simpleSelectTest() throws SQLCreationException {        
        List<String> fields = new ArrayList<String>();
        fields.add("name");
        fields.add("family");
        fields.add("sex");
        query = new SelectQuery(fields);
        query.addFrom();
        query.addTableName("employee");

        assertEquals("SELECT name,family,sex FROM employee", query.getQueryString());
    }
    
    @Test
    public void wholeSelectTest() throws SQLCreationException {
        initQuery();
        query.like("name", "Petrov", '%', WildcardPosition.AT_END).orIsEquals("name", "Petrov").andIsEquals("name", "Petrov");
        query.orNotBetween("birth", testDate, testDate).andNotBetween("birth", testDate, testDate);
        query.orLike("name", "Petrov", '_', WildcardPosition.AT_START).andLike("name", "Petrov", '%', WildcardPosition.AT_START);
        query.andBetween("name", 1, 2);
        query.orBetween("name", 1, 2);

        assertEquals("SELECT name,family,sex FROM employee WHERE name LIKE 'Petrov%' OR name='Petrov' AND name='Petrov' OR birth NOT BETWEEN '" + testDateString + "' AND '" + testDateString + "' AND birth NOT BETWEEN '" + testDateString + "' AND '" + testDateString + "' OR name LIKE '_Petrov' AND name LIKE '%Petrov' AND name BETWEEN 1 AND 2 OR name BETWEEN 1 AND 2", query.getQueryString());
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
