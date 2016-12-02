package io.tronbot.graphql.actuator.cucumber.consul

import java.util.concurrent.atomic.AtomicReference

import com.google.common.io.BaseEncoding
import com.google.common.net.HostAndPort
import com.orbitz.consul.AgentClient
import com.orbitz.consul.Consul
import com.orbitz.consul.HealthClient
import com.orbitz.consul.KeyValueClient
import com.orbitz.consul.StatusClient
import com.orbitz.consul.async.ConsulResponseCallback
import com.orbitz.consul.cache.ConsulCache
import com.orbitz.consul.cache.ServiceHealthCache
import com.orbitz.consul.model.ConsulResponse
import com.orbitz.consul.model.agent.Agent
import com.orbitz.consul.model.health.ServiceHealth
import com.orbitz.consul.model.kv.Value
import com.orbitz.consul.option.QueryOptions

import cucumber.api.Scenario
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Then

/**
 * 
 * @Author Juanyong Zhang
 * @Date Dec 2, 2016
 */
class ConsulDefs {
	private Scenario scenario
	private Consul consul
	private AgentClient agentClient
	private HealthClient healthClient
	private KeyValueClient keyValueClient
	private StatusClient statusClient
	private String serviceId = 'MyService'
	private String serviceName = 'My Service'
	private ServiceHealthCache svHealth

	@Before
	public void before(Scenario scenario) throws Throwable {
		this.scenario = scenario
		consul = Consul.builder().build()// connect to Consul on localhost
		agentClient = consul.agentClient()
		healthClient = consul.healthClient()
		keyValueClient = consul.keyValueClient()
		statusClient = consul.statusClient()
	}

	@Then('^Register and check your service in with Consul$')
	public void register_and_check_your_service_in_with_Consul() throws Throwable {
		agentClient.register(8080, 3L, serviceName, serviceId) // registers with a TTL of 3 seconds
		boolean pass = agentClient.pass(serviceId) // check in with Consul, serviceId required only.  client will prepend 'service:' for service level checks.
		scenario.write  "Testing if ${serviceName} is pass : ${pass}"
	}

	@Then('^Find available \\(healthy\\) services$')
	public void find_available_healthy_services() throws Throwable {
		healthClient.getHealthyServiceInstances('consul').getResponse().each { ServiceHealth node -> scenario.write(node.toString()) }
	}

	@Then('^Store key/values$')
	public void store_key_values() throws Throwable {
		keyValueClient.putValue('foo', 'bar')
		scenario.write  keyValueClient.getValueAsString('foo').get() // bar
	}

	@Then('^Blocking call for value$')
	public void blocking_call_for_value() throws Throwable {
		ConsulResponseCallback<Optional<Value>> callback = new ConsulResponseCallback<Optional<Value>>() {
					AtomicReference<BigInteger> index = new AtomicReference<BigInteger>(null)
					public void onComplete(ConsulResponse<Optional<Value>> consulResponse) {
						if (consulResponse.getResponse().isPresent()) {
							Value v = consulResponse.getResponse().get()
							scenario.write  "Value is: ${v}"
							scenario.write  "Value is: ${BaseEncoding.base64().decode(v.getValue().toString())}"
						}
						index.set(consulResponse.getIndex())
						watch()
					}

					void watch() {
						keyValueClient.getValue("foo", QueryOptions.blockMinutes(5, index.get()).build(), this)
					}

					public void onFailure(Throwable throwable) {
						scenario.write  "Error encountered : ${throwable}"
						watch()
					}
				}
		keyValueClient.getValue('foo', QueryOptions.blockMinutes(0, new BigInteger('0')).build(), callback)
	}

	@Then('^Subscribe to healthy services$')
	public void subscribe_to_healthy_services() throws Throwable {
		Agent agent = agentClient.getAgent()
		ServiceHealthCache svHealth = ServiceHealthCache.newCache(healthClient, serviceName)
		svHealth.addListener(new ConsulCache.Listener<HostAndPort, ServiceHealth>() {
					@Override
					public void notify(Map<HostAndPort, ServiceHealth> newValues) {
						// do Something with updated server map
					}
				})
		svHealth.start()
	}

	@Then('^Find Raft peers$')
	public void find_Raft_peers() throws Throwable {
		for(String peer : statusClient.getPeers()) {
			scenario.write("Raft peers: ${peer}") // 127.0.0.1:8300
		}
	}

	@Then('^Find Raft leader$')
	public void find_Raft_leader() throws Throwable {
		scenario.write("Raft leader: ${statusClient.getLeader()}" ) // 127.0.0.1:8300
	}

	@After
	public void after() throws Throwable {
		agentClient?.deregister(serviceId)
		keyValueClient?.deleteKey('foo')
		svHealth?.stop()
	}
}
