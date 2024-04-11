# Cards REST API Project

This is an API project for CRUD operations on a Card Entity having the following attributes; name, color, 
description, Status (of type ENUM -> ToDo, InProgress, Done).
The project leverages on Spring Security and JWT for user authentication/authorization and token generation/validation.

# Launching Project:

#### Database: 
 - Database supported: PostGresQl => The DBName, username & password information are available on >>src/main/resources/application.yml
 - Database Entities: The project has two tables, namely ("users", "cards"). These tables will be created automatically by Flyways db migrations scripts

#### Test Login Users:
 DB Migrations will insert two users into the DB upon project startup
 - ADMIN USER: `username`: harrison.kanda@logicea.com `role`: ADMIN `password`: $2a$12$PBSUv8oauj2BqlMrIz./7O6FaCkxFFpVEWEl6JaUQC5BfagiaqWRi
 - MEMBER USER: `username`: harrison.kiprono@logicea.com `role`: MEMBER `password`: $2a$12$PBSUv8oauj2BqlMrIz./7O6FaCkxFFpVEWEl6JaUQC5BfagiaqWRi

## APIS Documentation
 The APIs are documented using Swagger-ui & Open API specifications. 
 Swagger-ui URL = http://localhost:8081/swagger-ui/index.html#/ 

### Token Generation

Authentication Endpoint for generation of JWT accessToken
`POST /api/v1/auth/login`
`Request Body`
        {
        "email" : "",
        "password": ""
        }
`Response Status: 200 OK`
### Card Creation

Endpoint for creating cards
`POST /api/v1/cards`
`Request Body`
{
"name" : "{{$randomFirstName}}",
"description": "blue in color",
"color": "#1A2B3C"
}
`Response Status: 201 Created`
### Fetch Card By ID

Endpoint for fetching a card by it's cardId
`GET /api/v1/cards/{cardId}`
`Response Status: 200 OK`
### Fetch Cards (Paginated Result)

Endpoint for fetching a cards by Filters
`GET /api/v1/cards/`

#### CardFilters Request
{
"name": "string",
"color": "string",
"status": "ToDo",
"dateCreated": "2024-04-10T20:51:26.196Z"
}

#### Pagination Filters Request
{
"page": 0,
"pageSize": 0,
"sortBy": "string",
"sortOrder": "string"
}
`Response Status: 200 OK`

### Delete Cards
Endpoint for deleting a card by CardId
`DELETE /api/v1/cards/{cardId}`
`Response Status: 204`



