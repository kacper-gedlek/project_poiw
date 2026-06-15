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
