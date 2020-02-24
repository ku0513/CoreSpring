package accounts.web;

import accounts.AccountManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import rewards.internal.account.Account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO-06: Get yourself familiarized with various testing utility classes
// as described in the lab document

// TODO-07: Use `@WebMvcTest` and `@AutoConfigureDataJpa` annotations
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureDataJpa
public class AccountControllerBootTests {

	// TODO-08: Autowire MockMvc bean
	@Autowired
	private MockMvc mockMvc;

	// TODO-09: Create `AccountManager` mock bean
	@MockBean
	private AccountManager accountManager;


	// TODO-12: Experiment with @MockBean vs @Mock
	// - Change `@MockBean` to `@Mock` for the `AccountManager dependency
	// - Run the test and observe a test failure

	// TODO-10: Write positive unit test for GET request for an accont
	@Test
	public void accountDetails() throws Exception {

		// arrange
		given(accountManager.getAccount(0L)).willReturn(new Account("1234567890", "John Doe"));

		// act and assert
		mockMvc.perform(get("/accounts/0"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("name").value("John Doe"))
				.andExpect(jsonPath("number").value("1234567890"));

		// verify
		verify(accountManager).getAccount(0L);
	}

	// TODO-11: Write negative unit test for GET request for an account
	@Test
	public void accountDetailsFail() throws Exception {

		// arrange
		given(accountManager.getAccount(any(Long.class)))
				.willThrow(new IllegalArgumentException("No such account with id " + 0L));

		// act and assert
		mockMvc.perform(get("/accounts/0"))
				.andExpect(status().isNotFound());

		// verify
		verify(accountManager).getAccount(any(Long.class));

	}

    // TODO-13: Write unit test for `POST` request for an account
	@Test
	public void createAccount() throws Exception {

		// arrange
		Account newAccount = new Account("1234567890", "David");
		Account resultAccount = new Account("1234567890", "David");
		resultAccount.setEntityId(100L);

		given(accountManager.save(newAccount))
				.willReturn(resultAccount);

		// act and assert
//		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON)
//				.content(asJsonString(newAccount)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
//				.andExpect(header().string("Location", "http://localhost/accounts/100"));
		mockMvc.perform(post("/accounts")
				.content(asJsonString(newAccount))
				.contentType(MediaType.APPLICATION_JSON))
				//.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/accounts/100"));

		// verify
		verify(accountManager).save(newAccount);
	}


    // Utility class for converting an object into JSON string
	protected static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
