package com.example.demo;

import com.example.demo.helpers.HttpHelper;
import com.example.demo.users.User;
import com.example.demo.users.services.UserRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTests {

	@Resource
	private UserRepository userRepository;

	HttpClient httpClient = HttpClient.newHttpClient();
	Gson gson = new Gson();

	@Test
	void shouldGetUserById() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users/2");
		String responseBody = "";
		responseBody = getResponseBody(getRequest, responseBody);
		User user = gson.fromJson(responseBody, User.class);
		assertEquals(new User("razvan", "adrian", 30, "raz@email.com", "abcd12"), user);
	}

	@Test
	void shouldGetUserByFirstName() {
		HttpRequest getRequest = HttpHelper.createGetRequest("http://localhost:8082/users?firstName=raz");
		String responseBody = "";
		responseBody = getResponseBody(getRequest, responseBody);
//		List<User> users = gson.fromJson(responseBody, List<User>.class);
		Type listOfUser = new TypeToken<ArrayList<User>>() {}.getType();
		List<User> users = gson.fromJson(responseBody, listOfUser);
		assertEquals(new User("razvan", "adrian", 30, "raz@email.com", "abcd12"), users.get(0));
	}

	private String getResponseBody(HttpRequest getRequest, String responseBody) {
		try {
			HttpResponse<String> response = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
			responseBody = response.body();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return responseBody;
	}

}
