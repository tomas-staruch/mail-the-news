Mail the news is self hosted free application for sending out bulk email messages. At the moment it support sending emails through SMTP server or a sevice which has public API (Mailgun).

The stateless application provides RESTful API supporting JSON and XML format through HTTP(S). Every request is secured by basic http authentication and requires to pass the credentials every time.

Note: the user passwords are keept encrypted by BCrypt, while third party passwords are encrypted by AES symmetric key algorithm.

The following table shows how HTTP methods are used to comunicate:

| URL | GET | POST | PUT | DELETE | PATCH |
| --- | --- | ---- | --- | --- | --- |
| /user | Get all details about authenticted user. | Create a new user. | Update given user. | Delete the user. | - |
| /user/configurations | Get list of all configured service. | Create a new configuration. | - | - | - |
| /user/configurations/{id} | Get configuration detail. | - | Update the configuration. | Delete the configuration. | - |
| /user/address_books | Get list of all books with email addresses. | Create a new address book. | - | - | - |
| /user/address_books/{id} | Get address book detail. | - | Update the address book. | Delete the address book. | - |
| /user/templates | Get list of all templates. | Create a new email template. | - | - | - |
| /user/templates/{id} | Get template detail. | - | Update the template. | Delete the template. | - |
| /user/templates/{id}/batches | Get list of all created email batches. | Create a new batch of emails from template. | - | - | - |
| /user/templates/{id}/batches/{id} | Get batch detail. | - | - | - | _{action:send}_ Send the batch of emails. |

A dummy user data are loaded into the application when it starts in order to do a simple demo.
Assuming that server runs on localhost, then simple _CURL_ commands are:
```
GET the user data in json back:
curl -k --header "Accept: application/json" --user dummy_user@not.existing.domain.com:RaNdOmPwD http://localhost:8080/user

If SSL is enabled, use secure HTTP through port 9000, e.g.:
curl -k --header "Accept: application/json" --user dummy_user@not.existing.domain.com:RaNdOmPwD https://localhost:9000/user

GET detail information about particular configuration:
curl -k --header "Accept: application/json" --user dummy_user@not.existing.domain.com:RaNdOmPwD http://localhost:8080/user/configurations/1

POST a new configuration:
curl -k -H "Content-Type: application/json" --user dummy_user@not.existing.domain.com:RaNdOmPwD --url http://localhost:8080/user/configurations --data "{\"port\":25,\"sslEnabled\":false,\"url\":\"another.smtp.server.com\",\"userName\":\"my@not.existing.domain.com\",\"password\":\"secrete_phrase\",\"type\":\"smtp\"}"

...
```

Create a new user:
```
POST a new user:
curl -k -H "Content-Type: application/json" --data "{\"email\":\"another.user@not.existing.domain.com\",\"password\":\"aNyPaSsWoRd\",\"name\":\"John The Tester\"}" http://localhost:8080/user
```

The main object is User which is created by POST without required authentication. Then all other objects can be created.

Relations between entities:
![General domain model diagramalt tag](https://github.com/tomas-staruch/mail-the-news/blob/master/domain_model.png)
