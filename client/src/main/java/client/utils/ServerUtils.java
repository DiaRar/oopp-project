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

import client.uicomponents.Alerts;
import com.google.inject.Inject;
import commons.*;
import commons.primary_keys.DebtPK;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


public class ServerUtils {

	private final String server;
	private final Config config;
	@Inject
	public ServerUtils(Config config) throws IOException {
		this.config = config;
		this.server = config.getServer();
	}

	public void handleConnectionException(Exception ex) {
		if (ex instanceof ProcessingException) {
			Alerts.connectionRefusedAlert();
		} else {
			throw new NotFoundException(ex);
		}
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
		try {
			return ClientBuilder
					.newClient(new ClientConfig())
					.target(config.getServer())
					.path("/api/events/" + eventId)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.get(Event.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public Event addEvent(Event event) {
		try {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(event, APPLICATION_JSON), Event.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public List<Event> getEvents() {
		try {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(new GenericType<List<Event>>() {});
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public List<Expense> getExpenses(UUID eventId) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/{eventId}/expenses")
				.resolveTemplate("eventId", eventId)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get(new GenericType<List<Expense>>() {});
	}

	public Event updateEvent(UUID id, Event event) {
		try {
		return ClientBuilder.newClient(new ClientConfig())
				.target(server)
				.path("/api/events/" + id)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(event, APPLICATION_JSON), Event.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public List<Participant> getParticipants() {
		try {
			return ClientBuilder.newClient(new ClientConfig())
					.target(server)
					.path("/api/participants/")
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.get(new GenericType<List<Participant>>() {});
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public Participant addParticipant(Participant participant, UUID eventID) {
        try {
            return ClientBuilder.newClient(new ClientConfig())
                    .target(server)
                    .path("/api/events/" + eventID + "/participants/")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.entity(participant, APPLICATION_JSON), Participant.class);
        } catch (Exception ex) {
            handleConnectionException(ex);
            return null;
        }
	}

	public Participant getParticipant(UUID eventId, UUID id) {
        try {
            return ClientBuilder
                    .newClient(new ClientConfig())
                    .target(server)
                    .path("/api/events/" + eventId + "/participants/" + id)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(Participant.class);
        } catch (Exception ex) {
            handleConnectionException(ex);
            return null;
        }
	}

	public Participant updateParticipant(Participant participant, UUID eventId, UUID id) {
		try {
            return ClientBuilder.newClient(new ClientConfig())
                    .target(server)
                    .path("/api/events/" + eventId + "/participants/" + id)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .put(Entity.entity(participant, APPLICATION_JSON), Participant.class);
        } catch (Exception ex) {
            handleConnectionException(ex);
            return null;
        }
	}

	public Response deleteParticipant(UUID eventId, UUID id) {
		try {
			return ClientBuilder.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/{eventId}/participants/{id}")
					.resolveTemplate("eventId", eventId)
					.resolveTemplate("id", id)
					.request()
					.delete();
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public List<Debt> getDebts(Event event) {
		try {
			return ClientBuilder.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/" + event.getId() + "/debts")
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.get(new GenericType<List<Debt>>() {});
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public Debt updateDebt(Event event, DebtPK debtPK, Debt newDebt) {
		try {
			return ClientBuilder.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/" + event.getId() + "/debts/" + debtPK)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.put(Entity.entity(newDebt, APPLICATION_JSON), Debt.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public Debt deleteDebt(UUID eventId, DebtPK debtId) {
		try {
			return ClientBuilder
					.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/" + eventId + "/debts/" + debtId)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.delete(Debt.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public Expense addExpense(UUID eventID, Expense expense) {
		try {
			return ClientBuilder.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/" + eventID + "/expenses/")
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}
	public Response deleteExpense(UUID eventID, UUID expenseId) {
		try {
			return ClientBuilder.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/{eventId}/expenses/{expenseId}")
					.resolveTemplate("eventId", eventID)
					.resolveTemplate("expenseId", expenseId)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.delete();
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}
	public Expense updateExpense(UUID eventId, UUID expenseId, Expense expense) {
		try {
			return ClientBuilder.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/{eventId}/expenses/{expenseId}")
					.resolveTemplate("eventId", eventId)
					.resolveTemplate("expenseId", expenseId)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.put(Entity.entity(expense, APPLICATION_JSON), Expense.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public List<Tag> getTags(UUID eventId) {
		try {
			return ClientBuilder
					.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/" + eventId + "/tags")
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.get(new GenericType<List<Tag>>() {});
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public Tag getTag(UUID eventId, UUID tagId) {
		try {
			return ClientBuilder
					.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/" + eventId + "/tags/" + tagId)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.get(Tag.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public Tag updateTag(UUID eventId, UUID tagId, Tag newTag) {
		try {
			return ClientBuilder.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/" + eventId + "/tags/" + tagId)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.put(Entity.entity(newTag, APPLICATION_JSON), Tag.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}

	public Tag deleteTag(UUID eventId, UUID tagId) {
		try {
			return ClientBuilder
					.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/" + eventId + "/tags/" + tagId)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.delete(Tag.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}
	public Tag addTag(UUID eventId, Tag tag) {
		try {
			return ClientBuilder
					.newClient(new ClientConfig())
					.target(server)
					.path("/api/events/" + eventId + "/tags/")
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
		} catch (Exception ex) {
			handleConnectionException(ex);
			return null;
		}
	}
}