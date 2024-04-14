# OOPP team 12 - Splitty
> this project is still in its pre-release stage, so there may be small bugs

---
## Requirements
* `Java 21` is required to run this application
* commands should be ran in a compatible shell such as: 
  * `bash`, `zsh`, `fish` for `Linux`/`MacOS`
  * `PowerShell` for `Windows`
---
## Config
### Server
To configure the server,
we recommend that you modify these
two lines in `application.properties`:
```properties
server.address=localhost
server.port=8080
```
### Client
To configure the client, we have provided a `config.properties` file in 
which you can setup the `server` and (optional) `mail`:
```properties
# This is an example config
#Sun Apr 14 16:01:07 CEST 2024
country=RO
language=en
server=http\://localhost\:8080/
mail.host=
mail.port=
mail.username=
mail.password=
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.transport.protocol=smtp
mail.debug=false
```
---
## Building
To `build` this application you need to run the following command:
```console
./gradlew build
```
---
## Running
### Server
To run the server we recommend you to use the gradle wrapper that we provide, along with the `bootRun` task:
- Have [built](#building) the app
```console
./gradlew bootRun -a
```

### Client
To run the client we recommend you to use the gradle wrapper that we provide, along with the `client:run` task:
- Have the server already running
- Have [built](#building) the app
- Run:
```console
./gradlew client:run -a
```

### Admin
To run the admin we recommend you to use the gradle wrapper that we provide, along with the `admin:run` task:
- Have the server already running
- Have [built](#building) the app
- Run:
```console
./gradlew admin:run -a
```
---
## Usage
`Splitty` is meant to be used as an expense managers for different events.

The client is straightforward and easy to use, when you start the client you are on the `starting` page of the app.
In this page, a user can create, join an event using an invite code, or join a previously joined event:

![Start Page](docs/images/start.png)

From here, a user can enter the `overview` page, where they can manage all the entities within
an `event` (`participants`, `expenses`, `tags`, and `debts`):

![Overview Page](docs/images/overview.png)

Additionally, the `MenuBar` at the top can be used to switch between `languages`,
to change an event's name (`overview` page), to send a default email to `test the configuration` 
(note: it is disabled when email has empty/missing properties), 
or to change the theme of the `app`:

![Language Switch](docs/images/language.png)
![Edit Event Name](docs/images/edit.png)
![Test Mail Config](docs/images/testMail.png)
![Theme Switch](docs/images/theme.png)

By clicking the `add participant` icon, a user can create a new `participant` for an `event`:

![Add Participant Icon](docs/images/addParticipantIcon.png)
![Add Participant Page](docs/images/addParticipant.png)

Similarly, by clicking the icon next to it (the edit participant icon), you can edit an existing `participant`:

![Edit Participant Page](docs/images/editParticipant.png)

To add a new expense, a user can just click the add expense button and it will lead you to the `add expense` page:
![Add Expense Page](docs/images/addEditExpense.png)

To edit an expense, a user can click on the `edit` icon next to a created expense, similarly,
to delete it a user just have to click on the `trash` icon:

![Edit Expense Icon](docs/images/editExpenseIcon.png)

To add/edit a tag, a user can click on the `Tags` button, which will show the page for managing tags:

![Add or Edit a tag](docs/images/addEditTag.png)

To filter `expenses`, a user can use the filter button. The reset button resets all filters, while the X shaped buttons 
inside the filter dialog only reset the filter next to them:

![Filter expenses](docs/images/filter.png)

The `settle debts` page can be opened by clicking on the settle debts button, it will lead to a menu where one can 
fully or partially settle debts between participants, remind participants of debts and see the bank account of an 
indebted participant in order to wire the money:

![Settle Debts](docs/images/debts.png)

In order to view `statistics` of an event, a user can click on the statistics button, where they will see the total
cost of the event and a pie chart containing tags and their cost:

![Statistics](docs/images/statistics.png)

To invite other users to the event, a user can open the `invite` page from the invite button at the top right of the
`overview` page and the can email other people with an invitation code:

![Invite](docs/images/invite.png)

---
## Communication
The app communicates in `real-time` using `WebSockets` (in overview, add/edit participants, add/edit expenses, 
add/edit tags, settle debts) and `long-polling` (in statistics).

To see the `WebSockets` in action, you can find them in 
[WebSocketConfig](server/src/main/java/server/api/ws/WebSocketConfig.java) - server,
[WebSocketUpdateService](server/src/main/java/server/services/WebSocketUpdateService.java) - server,
[WSSessionHandler](client/src/main/java/client/implementations/WSSessionHandler.java) - client,
[WebSocketUtils](client/src/main/java/client/utils/WebSocketUtils.java) - client.

[WebSocketUtils](client/src/main/java/client/utils/WebSocketUtils.java) is being used in the client by
[MainCtrl](client/src/main/java/client/scenes/MainCtrl.java).

For `long-polling` please see the code for [StatisticsCtrl](client/src/main/java/client/scenes/StatisticsCtrl.java) - 
client and [ExpensesController](server/src/main/java/server/api/rest/ExpensesController.java) - server.

---

## Contributions

### Accessibility features
// TODO

### Participants
Jerzy Karremans, Rares Diaconescu, Blago Gunev, Alessandro Neri, Cristian Ţurcan, Ștefan Lupşan.

### Checkstyle rules
These are some of the checkstyle rules that we agreed on and checked throughout the project implementation:

- **NestedIfDepth** max value = 3
- **No Magic Numbers**
- **White space after comma**
- **camelCase** for variable, method names
- **PascalCase** for Class names

