# Defects found in aplication under test.

NOTE: You can find tests related to issues reported by looking for related tag above scenario. E.g. @Issue1


## Issue1: PUT /users/register API doesn't store first name in Db properly

#### Steps to reproduce:

* Perform following API call: 
```
Endpoint	 - http://localhost:8080/users/register
Method	 - PUT
Request	 - {"firstName":"FirstName","lastName":"LastName","password":"Test1234!","username":"{SomeUniqueValue}"}
```
* Observe response firstName field value:
```
Response - {"id":136,"username":"smmqxfiaoa","createdAt":"2021-06-29T10:32:17.5257+02:00","user_email":null,"user_fname":null,"user_mobile":null,"user_lname":null,"firstName":"LastName","lastName":"LastName"}
```
* Perform following SQL query against userdb:
```
SELECT FIRSTNAME FROM USERS WHERE USERNAME='{YourValue}'
```
* Observe result.

##### Expected behavior:
firstName field has ```FirstName``` value.

##### Actual behavior:
firstName field has ```LastName``` value, value from lastName field.

## Issue2: PUT /users/register API throws 400 error when firstName or lastName field has 1 character

#### Steps to reproduce:

* Perform following API call: 
```
Endpoint	 - http://localhost:8080/users/register
Method	 - PUT
Request	 - {"firstName":"F","lastName":"LastName","password":"Test1234!","username":"{SomeUniqueValue}"}
```

NOTE: It is also reproducible for lastName field value length 1 char.

* Observe response status code

NOTE: It is not clear what should be lower boundary for firstName and lastName fields - According to backend error msg it should be < 15 char.
For this issue business should be consulted about defining lower length boundaries.

##### Expected behavior:
User is created with firstName or lastName value length 1 and service responds with 201.

##### Actual behavior:
Bad request 400 status code and error message: First Name should be less than 15.

## Issue3: PUT /users/register API firstName and lastName invalid length issues

#### Steps to reproduce:

* Perform following API call with test data from below: 
```
Endpoint	 - http://localhost:8080/users/register
Method	 - PUT
Request	 - {"firstName":"{ValuesFromTestDataTableBelow}","lastName":"LastName","password":"Test1234!","username":"{SomeUniqueValue}"}
```

* Or:

```
Endpoint	 - http://localhost:8080/users/register
Method	 - PUT
Request	 - {"firstName":"FirstName","lastName":"{ValuesFromTestDataTableBelow}","password":"Test1234!","username":"{SomeUniqueValue}"}
```

* See data table below for scenario and expected / actual behaviors:

| Field name 	| length 	|               value              	|             expected behavior             	|          actual behavior          	|                                  comment                                 	|
|:----------:	|:------:	|:--------------------------------:	|:-----------------------------------------:	|:---------------------------------:	|:------------------------------------------------------------------------:	|
|  firstName 	|   15   	|          FirstNameFirstN         	|     First Name should be less than 15     	|            201 No error           	|  It is not clear if length should be 15 inclusive, consult with business 	|
|  firstName 	|  empty 	|                                  	| First Name should have more than one char 	| First Name should be less than 15 	| Lower boundary error message should be displayed, consult with business. 	|
|  lastName  	|   15   	|          LastNameLastNam         	|     First Name should be less than 15     	|            201 No error           	|  It is not clear if length should be 15 inclusive, consult with business 	|
|  lastName  	|   16   	|         LastNameLastName         	|      Last Name should be less than 15     	|  LAst Name should be less than 15 	|                          Typo in error message.                          	|
|  lastName  	|   32   	| LastNameLastNameLastNameLastName 	|      Last Name should be less than 15     	|  LAst Name should be less than 15 	|                          Typo in error message.                          	|
|  lastName  	|  empty 	|                                  	|  Last Name should have more than one char 	|  Last Name should be less than 15 	| Lower boundary error message should be displayed, consult with business. 	|

## Issue4: PUT /users/register API password invalid input value issues

#### Steps to reproduce:

* Perform following API call with test data from below: 
```
Endpoint	 - http://localhost:8080/users/register
Method	 - PUT
Request	 - {"firstName":"FirstName","lastName":"LastName","password":"{ValuesFromTestDataTableBelow}","username":"{SomeUniqueValue}"}
```

NOTE: According to the requirements password should contain combination of capital and small letters, special chars and numbers.

* See data table below for scenarios:

| Password value                           	| Value description                          	|
|------------------------------------------	|--------------------------------------------	|
| test1234!                                	| Without capital letters                    	|
| TEST1234!                                	| Without small letters                      	|
| Testing!                                 	| Without numbers                            	|
| Test12345                                	| Without special characters                 	|
| Testing1234567890!!!!!!!!!!!!!!!!!!!!!!! 	| Long password with all required characters 	|
|                                          	| Empty password                             	|

Expected behavior:
400 Bad request with descriptive error message about password constrains.
Min and Max password length restrictions should be defined. Question for business.

Actual behavior:
201 user is created for all listed scenarios. One potential denial of service security risk is password max length restriction - https://www.acunetix.com/vulnerabilities/web/long-password-denial-of-service/

## Issue5: PUT /users/register API invalid username length issues

#### Steps to reproduce:

* Perform following API call: 
```
Endpoint	 - http://localhost:8080/users/register
Method	 - PUT
Request	 - {"firstName":"FirstName","lastName":"LastName","password":"Test1234!","username":"{SomeUniqueValueWithVariousLengths}"}
```

NOTE1: According to requirements username field length restrictions are that it should be longer than 6 and shorter than 50 characters.

NOTE2: It is not clear if min and max length boundaries should be inclusive or exclusive - question for business.

Expected behavior: Lower boundary is 6 and upper boundary is 50. 201 API status code for valid length inputs.

Actual behavior: Lower boundary is 5 Upper boundary is 20. Error in application logs for > 20 char inputs is:

```
2021-06-30 22:44:31.149  WARN 58765 --- [io-8080-exec-10] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 22001, SQLState: 22001
2021-06-30 22:44:31.149 ERROR 58765 --- [io-8080-exec-10] o.h.engine.jdbc.spi.SqlExceptionHelper   : Value too long for column "USERNAME VARCHAR(20)": "'mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdjxzz-TST079' (50)"; SQL statement:
```