package io.tronbot.graphql.actuator.cucumber.stepdefs;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;

//@WebAppConfiguration
//@ContextConfiguration(classes = GraphqlActuatorApp.class, loader = SpringBootContextLoader.class)
//@SpringBootTest({ "server.port=0", "management.port=0", "spring.profiles.active=dev" })
@SpringBootTest({ "spring.profiles.active=dev" })
public abstract class StepDefs {

	protected ResultActions actions;

}
