# http4s example

## Create Account
Request
```curl
curl -d '{"name":"Livan","email":"example@example.com", "amount":200}' -X POST http://localhost:9000/accounts{"no":"5806"}
```
Response
```json
{"no":"8135"}
```
## Transfer
Request
```curl
curl -d '{"senderAccountNo":"8135","receiverAccountNo":"287", "amount":100}' -X POST http://localhost:9000/accounts/transfer
```
Response
```
Success transfer
```