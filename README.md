# Field service

This is an example of a set of services for managing fields and gathering weather history data.

### Requirements
* JDK 11+
* Lombok (installation for IDE)
* Maven
* Docker

### Build
```
mvn clean install
```

### Run

```
docker-composer up
```

Please notice the application runs with 'dev' profile when starting the field-service image in order to setup the connection to the MongoDB container through it's "db" name.

## API

### Create Field

API call:
http://localhost:8060/fields

Method: `POST`
Headers: Content-Type: application/json

Example of body:

```
{
    "id": "5f46aa28714b521409e0f8e9",
    "name": "Potato field",
    "countryCode": "DEU",
    "bounderies": {
        "id": "a0f63e74-d7ef-4924-acb3-0e960ae9ec98",
        "geoJson": {
            "type": "Feature",
            "properties": {},
            "geometry": {
                "type": "Polygon",
                "coordinates": [
                    [
                        [
                            -5.553604888914691,
                            33.88229680420605
                        ],
                        [
                            -5.5516736984239685,
                            33.88229680420605
                        ],
                        [
                            -5.5516736984239685,
                            33.88372189858022
                        ],
                        [
                            -5.555965232847882,
                            33.88390003370375
                        ],
                        [
                            -5.555965232847882,
                            33.88229680420605
                        ],
                        [
                            -5.553604888914691,
                            33.88229680420605
                        ]
                    ]
                ]
            }
        }
    }
}
```

### Get Field

API call:
http://localhost:8060/fields/5f46aa28714b521409e0f8e9

Method: `GET`
Headers: Content-Type: application/json

Response body example:

```
{
    "id": "5f46aa28714b521409e0f8e9",
    "name": "Potato field",
    "countryCode": "DEU",
    "bounderies": {
        "id": "a0f63e74-d7ef-4924-acb3-0e960ae9ec98",
        "geoJson": {
            "type": "Feature",
            "properties": {},
            "geometry": {
                "type": "Polygon",
                "coordinates": [
                    [
                        [
                            -5.553604888914691,
                            33.88229680420605
                        ],
                        [
                            -5.5516736984239685,
                            33.88229680420605
                        ],
                        [
                            -5.5516736984239685,
                            33.88372189858022
                        ],
                        [
                            -5.555965232847882,
                            33.88390003370375
                        ],
                        [
                            -5.555965232847882,
                            33.88229680420605
                        ],
                        [
                            -5.553604888914691,
                            33.88229680420605
                        ]
                    ]
                ]
            }
        }
    }
}
```

### Update Field

API call:
http://localhost:8060/fields/5f46aa28714b521409e0f8e9

Method: `PUT`
Headers: Content-Type: application/json

Example of body:

```
{
    "id": "5f46aa28714b521409e0f8e9",
    "name": "Strawberry fields",
    "countryCode": "DEU",
    "bounderies": {
        "id": "a0f63e74-d7ef-4924-acb3-0e960ae9ec98",
        "geoJson": {
            "type": "Feature",
            "properties": {},
            "geometry": {
                "type": "Polygon",
                "coordinates": [
                    [
                        [
                            -5.553604888914691,
                            33.88229680420605
                        ],
                        [
                            -5.5516736984239685,
                            33.88229680420605
                        ],
                        [
                            -5.5516736984239685,
                            33.88372189858022
                        ],
                        [
                            -5.555965232847882,
                            33.88390003370375
                        ],
                        [
                            -5.555965232847882,
                            33.88229680420605
                        ],
                        [
                            -5.553604888914691,
                            33.88229680420605
                        ]
                    ]
                ]
            }
        }
    }
}
```

### Delete Field

API call:
http://localhost:8060/fields/5f46aa28714b521409e0f8e9

Method: `DELETE`
Headers: Content-Type: application/json

### Get Weather History

API call:
http://localhost:8060/fields/5f46aa28714b521409e0f8e9/weather

Method: `GET`
Headers: Content-Type: application/json

Response body example:

```
{
   "weather":[
      {
         "timestamp":"1485705600",
         "temperature":288.15,
         "humidity":85,
         "temperatureMax":289.16,
         "temperatureMin":280.16
      },
      {
         "timestamp":"1485705700",
         "temperature":288.15,
         "humidity":85,
         "temperatureMax":289.16,
         "temperatureMin":280.16
      },
      "..."
   ]
}
```

### Known issues

### Possible improvements
* improve error handling (eg.: add proper Error response objects and handle 3rd party service calls failures with these new error codes instead of failing with internal error)
* consider use event driven architecture to post messages (eg by using kafka) to interact with 3rd party service and our DB to improve performance
* add configuration for timeouts
* add configuration for weather history length
* create unit tests using mock concepts similar to the one implemented in the integration test
* move secrets, users and psw to env variables
