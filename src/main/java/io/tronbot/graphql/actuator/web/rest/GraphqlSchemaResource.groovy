package io.tronbot.graphql.actuator.web.rest;

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping('/api')
public class GraphqlSchemaResource {
	@Inject
	private DiscoveryClient discoveryClient;

	@GetMapping('/graphql-schema')
	public List<String> serviceUrl() {
		def services = [];
		discoveryClient.getInstances("graphqlActuator").each{s ->
			services << ToStringBuilder.reflectionToString(s)
		};
		//		List<ServiceInstance> list = discoveryClient.getInstances("graphqlActuator");
		//		if (list != null && list.size() > 0 ) {
		//			return list.get(0).getUri();
		//		}
		return services;
	}

	@FeignClient("GraphqlSchemaResource")
	public interface GraphqlSchemaResourceClient {

		@RequestMapping(value = '/api/graphql-schema',method = RequestMethod.GET)
		String getServiceUrl();
	}
}
