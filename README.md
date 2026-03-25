# Card Authentication System (CAS)

## serverCAS

### Build and Run

```bash
./gradlew serverCAS:bootBuildImage
docker compose up
```

### Endpoints
- Card and Reader managment UI: `http://{confiugred_url}/`
- Validation API: `http://{confiugred_url}/validation`

### API Documentation

#### Request Example

`GET` `http://localhost:8080/validation`

```json
{
  "readerNumber": "1111222233334444",
  "cardNumber": "5555666677778888"
}
```

#### Response Example

```json
{
  "cardOwner": "Jane Doe",
  "readerName": "Server Room",
  "validation": "ACCESS_GRANTED"
}
```

##### Possible values for `validation`:
- `ACCESS_GRANTED`
- `ACCESS_DENIED`
- `ERROR` - if cardNumber or readerNumber is found not in the database