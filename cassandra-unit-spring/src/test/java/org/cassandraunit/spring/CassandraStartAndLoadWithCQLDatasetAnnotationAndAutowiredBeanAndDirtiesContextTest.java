package org.cassandraunit.spring;

import com.datastax.driver.core.ResultSet;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

/**
 * @author Gaëtan Le Brun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:/autowired-context.xml"})
@TestExecutionListeners({CassandraUnitDependencyInjectionTestExecutionListener.class, DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
@CassandraDataSet(value = {"cql/dataset1.cql"})
@EmbeddedCassandra
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class CassandraStartAndLoadWithCQLDatasetAnnotationAndAutowiredBeanAndDirtiesContextTest {
    @Autowired
    private DummyCassandraConnector dummyCassandraConnector;

    @BeforeClass
    public static void beforeClass() {
        DummyCassandraConnector.resetInstancesCounter();
    }

    @Test
    public void should_work() {
        test();
    }

    @Test
    public void should_work_twice() {
        test();
    }

    private void test() {
        ResultSet result = dummyCassandraConnector.getSession().execute("select * from testCQLTable1 WHERE id=1690e8da-5bf8-49e8-9583-4dff8a570717");
        String val = result.iterator().next().getString("value");
        assertEquals("1- Cql loaded string", val);
    }

    @AfterClass
    public static void afterClass(){
        assertEquals(2, DummyCassandraConnector.getInstancesCounter());
    }

}
