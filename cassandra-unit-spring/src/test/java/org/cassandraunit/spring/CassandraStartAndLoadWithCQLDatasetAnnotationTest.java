package org.cassandraunit.spring;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Olivier Bazoud
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "classpath:/default-context.xml" })
@TestExecutionListeners({ CassandraUnitTestExecutionListener.class })
@CassandraDataSet(value = { "cql/dataset1.cql" })
@EmbeddedCassandra
public class CassandraStartAndLoadWithCQLDatasetAnnotationTest {

  @Test
  public void should_work() {
    test();
  }

  @Test
  public void should_work_twice() {
    test();
  }

  private void test() {
    Cluster cluster = Cluster.builder()
        .addContactPoints("127.0.0.1")
        .withPort(9142)
        .build();
    Session session = cluster.connect("cassandra_unit_keyspace");
    ResultSet result = session.execute("select * from testCQLTable1 WHERE id=1690e8da-5bf8-49e8-9583-4dff8a570717");
    String val = result.iterator().next().getString("value");
    assertEquals("1- Cql loaded string", val);
  }

}
