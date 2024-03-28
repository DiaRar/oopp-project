/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import commons.Debt;
import commons.Event;
import commons.Participant;
import commons.primary_keys.DebtPK;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

import javax.inject.Inject;

public class ServerUtils {

	private final String server;
	private final Config config;
	@Inject
	public ServerUtils(Config config) throws IOException {
		this.config = config;
		this.server = config.getServer();
	}
	public void getQuotesTheHardWay() throws IOException, URISyntaxException {
		var url = new URI("http://localhost:8080/api/quotes").toURL();
		var is = url.openConnection().getInputStream();
		var br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

	public Event getEvent(UUID eventId) {
		return ClientBuilder
				.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/" + eventId)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(Event.class);
	}

	public Event addEvent(Event event) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(event, APPLICATION_JSON), Event.class);
	}

	public List<Event> getEvents() {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(new GenericType<List<Event>>() {});
	}

	public Event updateEvent(UUID id, Event event) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/" + id)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(event, APPLICATION_JSON), Event.class);
	}

	public List<Participant> getParticipants() {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/participants/")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(new GenericType<List<Participant>>() {});
	}

	public Event addParticipant(Participant participant, UUID eventID) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/" + eventID)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(participant, APPLICATION_JSON), Event.class);
	}

	public Participant getParticipant(UUID id) {
		return ClientBuilder
				.newClient(new ClientConfig())
				.target(server)
				.path("/api/participants/" + id)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(Participant.class);
	}

	public Participant updateParticipant(Participant participant, UUID id) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/participants/" + id)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(participant, APPLICATION_JSON), Participant.class);
	}

	public List<Debt> getDebts(Event event) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/" + event.getId() + "/debts")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(new GenericType<List<Debt>>() {});
	}

	public Debt updateDebt(Event event, DebtPK debtPK, Debt newDebt) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/" + event.getId() + "/debts/" + debtPK)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(newDebt, APPLICATION_JSON), Debt.class);
	}

	public Debt deleteDebt(UUID eventId, DebtPK debtId) {
		return ClientBuilder
				.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/" + eventId + "/debts/" + debtId)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.delete(Debt.class);
	}
}