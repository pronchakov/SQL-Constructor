package ru.gs.sql.test.select;

import java.util.Calendar;
import java.util.Date;
import org.junit.BeforeClass;
import ru.gs.sql.UpdateQuery;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public class UpdateTest {
	private static final String testDateString = "2011-09-15 00:22:13.870";
	private static Date testDate;
	private UpdateQuery query;
	
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
	public void simpleUpdateQuery() {
        query = new UpdateQuery();
        query.addTableName("employee").addSet();
		query.addSet("name", "Anton");
		query.addSet("age", 45);
		query.addSet("birth", testDate);
		assertEquals("UPDATE employee SET name = 'Anton', age = 45, birth = '2011-09-15 00:22:13.870'", query.toString());
    }

}
