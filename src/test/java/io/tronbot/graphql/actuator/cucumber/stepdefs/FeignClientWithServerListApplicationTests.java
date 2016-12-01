package io.tronbot.graphql.actuator.cucumber.stepdefs;
/**
 * 
 * @Author Juanyong Zhang
 * @Date Nov 17, 2016
 */

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.tronbot.graphql.actuator.GraphqlActuatorApp;

@RunWith(SpringRunner.class)
// We disable Hystrix because we are not concerned about testing circuit
// breakers in this
// test and it eliminates hystrix timeouts from messing with the request
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "spring.profiles.active=dev" })
@DirtiesContext
@EnableFeignClients
@ContextConfiguration(classes = GraphqlActuatorApp.class, loader = SpringBootContextLoader.class)
public class FeignClientWithServerListApplicationTests {

	@Autowired
	private RestClient client;

	@Test
	public void clientConnects() {
		assertTrue(client.hello().contains("<html"));
	}

	// @Configuration
	// @EnableAutoConfiguration
	// @EnableFeignClients
	// protected static class TestApplication {
	//
	// public static void main(String[] args) {
	// SpringApplication.run(GraphqlActuatorApp.class, args);
	// }
	// }

	@FeignClient(name = "example", url = "example.com")
	static interface RestClient {
		@RequestMapping(value = "/", method = RequestMethod.GET)
		String hello();
	}
}
