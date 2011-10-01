package ru.gs.sql.test.select;

import java.util.List;
import ru.gs.sql.exceptions.SQLCreationException;
import org.junit.Test;
import ru.gs.sql.SelectQuery;
import ru.gs.sql.WildcardPosition;

/**
 *
 * @author APronchakov <artem.pronchakov@gmail.com>
 */
public class ExceptionsTest {

    private SelectQuery query;
    
    private void initQuery() throws SQLCreationException {
        query = new SelectQuery();
        query.addField("name").addField("family").addField("sex");
        query.addFrom("employee");
        query.addWhere();
    }
    
    private void initSimpleQuery() {
        query = new SelectQuery();
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
    
}
