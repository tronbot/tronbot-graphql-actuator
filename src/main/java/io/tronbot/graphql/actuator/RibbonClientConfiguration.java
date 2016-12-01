package io.tronbot.graphql.actuator;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

/**
 * 
 * @Author Juanyong Zhang
 * @Date Nov 17, 2016
 */
@Configuration
public class RibbonClientConfiguration {
	@Bean
	public ILoadBalancer ribbonLoadBalancer() {
		//because of this, it doesn't use eureka to lookup the server,
		// but the classpath is tested
		BaseLoadBalancer balancer = new BaseLoadBalancer();
		balancer.setServersList(Arrays.asList(new Server("example.com", 80)));
		return balancer;
	}
}
