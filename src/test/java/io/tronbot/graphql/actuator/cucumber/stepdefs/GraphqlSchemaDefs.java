package io.tronbot.graphql.actuator.cucumber.stepdefs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.tronbot.graphql.actuator.GraphqlActuatorApp;
import io.tronbot.graphql.actuator.cucumber.CucumberTest;

/**
 * 
 * @Author Juanyong Zhang
 * @Date Nov 17, 2016
 */
@EnableConfigurationProperties
@EnableFeignClients
@ComponentScan
@ContextConfiguration(classes = GraphqlActuatorApp.class, loader = SpringBootContextLoader.class)
public class GraphqlSchemaDefs extends StepDefs {
	@Autowired
	GraphqlActuatorClient graphqlActuatorClient;

	@Given("^request for the jwt token$")
	public void request_for_the_jwt_token() throws Throwable {
		String jwt = graphqlActuatorClient.authenticate();
		System.out.println(jwt);
	}

	@Then("^populate jwt in request header$")
	public void populate_jwt_in_request_header() throws Throwable {
	}

	@Then("^get request for generate GraphQL schema$")
	public void get_request_for_generate_GraphQL_schema() throws Throwable {
	}

	@FeignClient("localhost:7000")
	public interface GraphqlActuatorClient {
		@RequestMapping(value = "/api/authenticate", method = RequestMethod.POST)
		String authenticate();
	}
}
