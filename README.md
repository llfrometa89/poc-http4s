# poc-http4s

## Prerequisites
To follow this **Readme** you need to have installed: [cURL](https://curl.haxx.se/) or [HTTPie](https://httpie.org/)

## Create Account
Request
```curl
$ curl -d '{"name":"Livan","email":"example@example.com", "amount":200}' -X POST http://localhost:9000/accounts
or
$ http POST :9000/accounts name="Livan" email=:"example@example.com" amount=200
```
Response
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
    "no": "1546"
}
```

## Transfer Money
Request
```curl
curl -d '{"senderAccountNo":"8135","receiverAccountNo":"287", "amount":100}' -X POST http://localhost:9000/accounts/transfer
or
http POST :9000/accounts/transfer senderAccountNo="8135" receiverAccountNo="287" amount=200
```
Response
```
Success transfer
```

## Resources
- https://http4s.org/
