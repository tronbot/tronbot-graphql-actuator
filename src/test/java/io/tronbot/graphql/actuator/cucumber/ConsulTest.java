package io.tronbot.graphql.actuator.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty", features = "src/test/features/consul/consul.feature", glue="io.tronbot.graphql.actuator.cucumber.consul")
public class ConsulTest {

}
