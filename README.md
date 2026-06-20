# Card Authentication System (CAS)

## serverCAS

#### Build and Run

```bash
./gradlew serverCAS:bootBuildImage
docker compose up
```

#### Endpoints
- Card and Reader managment UI: `http://{confiugred_url}/`
- Validation API: `http://{confiugred_url}/validation`

#### API

###### Request Example

`GET` `http://localhost:8080/validation`

```json
{
  "readerNumber": "1111222233334444",
  "cardNumber": "5555666677778888"
}
```

###### Response Example

```json
{
  "cardOwner": "Jane Doe",
  "readerName": "Server Room",
  "validation": "ACCESS_GRANTED"
}
```

###### Possible values for `validation`:
- `ACCESS_GRANTED`
- `ACCESS_DENIED`
- `ERROR` - if cardNumber or readerNumber is found not in the database

## readerUtils

Provides a wrapper around the Java Smart Card I/O API for getting UID
of contactless cards

#### demo
```bash
./gradlew runDemo
```

Lists avaliable terminals and displays UID of MiFare card inseted to the first connected terminal 


#### Example usage
```java
Reader reader = new Reader();
reader.initialize();

// Get list of readers and print them
System.out.println(reader.getReaders());

// Select the first reader and get UID
reader.setReader(0);
String cardUID = reader.getUID();
System.out.println("Card UID: " + cardUID);
```

#### Reading data from electronic student ID cards (ELS)
To ensure quick onboarding of new users, readerUtils provides an API for reading the user's name, surname, and student ID from an ELS card.
It is used by invoking `getElsData()` on the `reader` object, which returns an `elsData` object:

```java
public class elsData {
	private String name;
	private String surname;
	private  String albumNumber;
}
```

#### JavaDoc
```bash
./gradlew readerUtils:javadoc
```

Documentation of `Reader` class in HTML format is generated in `build/docs/javadoc/index.html`

## mockClient

An application for testing the CAS server and the `readerUtils` library. It provides a simulation of an RFID reader and full visibility of HTTP communication. It contains an interactive CLI tool for debugging and verification.

#### Run CLI

To run the interactive CLI tool:
```bash
./gradlew mockClient:run
```

The CLI menu provides options to:
- Perform a Server Health Check (checks connection and status of the CAS server).
- Initialize the simulated RFID Reader (manual ID or random ID).
- Scan a specific or random card and perform validation against the server.
- Show the total number of validation requests made.

#### Example programmatic usage

You can also use the `MockClient` class programmatically to simulate and test card scans:

```java
// Initialize MockClient (default server URL: http://localhost:8080)
MockClient client = new MockClient();

// Or specify a custom server URL
MockClient customClient = new MockClient("http://localhost:9090");

// Initialize the simulated reader
client.getRfidSimulator().initialize("1111222233334444", "Main Gate Reader");

// Simulate scanning a card and sending a validation request to the server
CommunicationLog log = client.simulateScanAndValidate("5555666677778888");

// Print complete HTTP communication logs (request and response details)
System.out.println(log.toDebugString());

// Parse and inspect the validation result
ValidationResult result = client.getValidationService().parseResult(log);
if (result != null) {
    System.out.println("Access validation: " + result.getValidation()); // e.g. ACCESS_GRANTED
    System.out.println("Card Owner: " + result.getCardOwner());
}
```
