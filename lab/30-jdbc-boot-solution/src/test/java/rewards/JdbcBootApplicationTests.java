package rewards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JdbcBootApplicationTests {
	public static final String QUERY = "SELECT count(*) FROM T_ACCOUNT";

	@Autowired JdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setUp() {
		System.out.println("setting up in junit 5");
	}

	@Test
	public void testNumberAccount() throws Exception {

		long count = jdbcTemplate.queryForObject(QUERY, Long.class);

		assertEquals(21L, count);
	}

}
