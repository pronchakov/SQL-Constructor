package ru.gs.sql.test.select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.BeforeClass;
import ru.gs.sql.InsertQuery;
import static org.junit.Assert.*;
import org.junit.Test;
import ru.gs.sql.exceptions.SQLCreationException;

/**
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public class InsertQueryTest {
    private static Date testDate;
    private static final String testDateString = "2011-09-15 00:22:13.870";
    /* */
    private InsertQuery query;

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
    public void simpleInsertTest() throws SQLCreationException {
        query = new InsertQuery();
        query.addTableName("employee");
        query.startAddingInsertableFieldNames();
        query.addInsertableFieldName("name");
        query.addInsertableFieldName("family");
        query.stopAddingInsertableFieldNames();
        
        List<Object> values = new ArrayList<Object>();
        values.add("Ivan");
        values.add("Petrov");
        
        query.addValues(values);
        
        assertEquals("INSERT INTO employee (name,family) VALUES ('Ivan','Petrov')", query.getQueryString());
    }
}
