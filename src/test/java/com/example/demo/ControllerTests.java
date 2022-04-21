package com.example.demo;

import com.example.demo.accounts.AccountRepository;
import com.example.demo.accounts.models.Account;
import com.example.demo.accounts.models.AccountRequest;
import com.example.demo.accounts.models.Currency;
import com.example.demo.helpers.HttpHelper;
import com.example.demo.transfers.models.TransferRequest;
import com.example.demo.users.User;
import com.example.demo.users.services.UserRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ControllerTests {

	@Resource
	private UserRepository userRepository;

	@Resource
	private AccountRepository accountRepository;

	HttpClient httpClient = HttpClient.newHttpClient();
	Gson gson = new Gson();

	@Test
	@Order(1)
	void shouldGetUserById() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/2");
		String responseBody = getResponseBody(getRequest);
		User user = gson.fromJson(responseBody, User.class);
		assertEquals(new User("razvan", "adrian", 30, "raz@email.com", "abcd12"), user);
	}

	@Test
	@Order(2)
	void shouldNotGetUserById() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/100");
		String responseBody = getResponseBody(getRequest);
		assertEquals("No user found for this id!", responseBody);
	}

	@Test
	@Order(3)
	void shouldGetUserByFirstName() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users?firstName=raz");
		String responseBody = getResponseBody(getRequest);
		Type listOfUser = new TypeToken<ArrayList<User>>() {}.getType();
		List<User> users = gson.fromJson(responseBody, listOfUser);
		assertEquals(new User("razvan", "adrian", 30, "raz@email.com", "abcd12"), users.get(0));
	}

	@Test
	@Order(4)
	void shouldNotGetUserByFirstName() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users?firstName=dqwuihdqwb");
		String responseBody = getResponseBody(getRequest);
		assertEquals("No users found!", responseBody);
	}

	@Test
	@Order(5)
	void shouldGetUserAccounts() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/1/accounts");
		String responseBody = getResponseBody(getRequest);
		Type listOfAccount = new TypeToken<ArrayList<Account>>() {}.getType();
		List<Account> accounts = gson.fromJson(responseBody, listOfAccount);
		assertEquals(new Account("ROWB11RON", 1, "RON", 500), accounts.get(0));
	}

	@Test
	@Order(6)
	void shouldNotGetUserForAccounts() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/34316/accounts");
		String responseBody = getResponseBody(getRequest);
		assertEquals("No user found for this id!", responseBody);
	}

	@Test
	@Order(7)
	void shouldNotGetAnyUserAccounts() {
		userRepository.save(new User("a", "a", 1, "a", "a"));
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/3/accounts");
		String responseBody = getResponseBody(getRequest);
		assertEquals("No accounts found for this user!", responseBody);
		userRepository.deleteById(3);
	}

	@Test
	@Order(8)
	void shouldGetUserInitials() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/initials");
		String responseBody = getResponseBody(getRequest);
		Type listOfString = new TypeToken<ArrayList<String>>() {}.getType();
		List<String> initials = gson.fromJson(responseBody, listOfString);
		List<String> expectedInitials = Arrays.asList("ii", "ra");
		assertArrayEquals(expectedInitials.toArray(), initials.toArray());
	}

	@Test
	@Order(9)
	void shouldCountGmailUsers() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/gmailCount");
		String responseBody = getResponseBody(getRequest);
		assertEquals("0", responseBody);
	}

	@Test
	@Order(10)
	void shouldGetUserLastName() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/lastNames");
		String responseBody = getResponseBody(getRequest);
		Type listOfString = new TypeToken<ArrayList<String>>() {}.getType();
		List<String> lastNames = gson.fromJson(responseBody, listOfString);
		List<String> expectedLastNames = Arrays.asList("ionescu", "adrian");
		assertArrayEquals(expectedLastNames.toArray(), lastNames.toArray());
	}

	@Test
	@Order(11)
	void shouldGetFirstNameInitials() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/firstNameInitials");
		String responseBody = getResponseBody(getRequest);
		assertEquals("ir", responseBody);
	}

	@Test
	@Order(12)
	void shouldCountUsersWithA20() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/A20");
		String responseBody = getResponseBody(getRequest);
		assertEquals("0", responseBody);
	}

	@Test
	@Order(13)
	void shouldCreateUser() {
		User bodyUser = new User("dummy", "user", 12, "abc@yahoo.com", "aaaaaaa");
		HttpRequest postRequest = HttpHelper.createPostRequest("http://localhost:8082/users", bodyUser);
		String responseBody = getResponseBody(postRequest);
		assertEquals("User succesfully created", responseBody);
		//userRepository.deleteById(3);
	}

	@Test
	@Order(14)
	void shouldNotCreateUser() {
		User bodyUser = new User("dummy", "user", 12, "ion@email.com", "aaaaaaa");
		HttpRequest postRequest = HttpHelper.createPostRequest("http://localhost:8082/users", bodyUser);
		String responseBody = getResponseBody(postRequest);
		assertEquals("This email is already used!", responseBody);
	}

	@Test
	@Order(15)
	void shouldCreateAccount() {
		AccountRequest accountRequest = new AccountRequest(Currency.RON, 100);
		HttpRequest postRequest = HttpHelper.createPostRequest("http://localhost:8082/users/2/accounts", accountRequest);
		String responseBody = getResponseBody(postRequest);
		assertEquals("Account succesfully created", responseBody);
	}

	@Test
	@Order(16)
	void shouldTransferMoney() {
		TransferRequest transferRequest = new TransferRequest("ROWB21RON", "ROWB11RON", 1);
		HttpRequest postRequest = HttpHelper.createPostRequest("http://localhost:8082/transfer", transferRequest);
		String responseBody = getResponseBody(postRequest);
		assertEquals("Transfer succesful", responseBody);
	}

	private String getResponseBody(HttpRequest request) {
		String responseBody = "";
		try {
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			responseBody = response.body();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return responseBody;
	}

}
