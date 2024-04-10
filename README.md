# Cards REST API Project

This is a small API project for CRUD operations on a Card Entity which fields such as; name, color, 
description, Status (of type ENUM -> To_DO, In_Progress, Done).

# APIS

### Token Generation
Authentication Endpoint for generation of JWT accessToken
`POST /api/v1/auth/login`
`Request Body`
        {
        "email" : "",
        "password": ""
        }

### Card Creation
Endpoint for creating cards
`POST /api/v1/cards`
`Request Body`
{
"name" : "{{$randomFirstName}}",
"description": "blue in color",
"color": "#1A2B3C"
}

### Fetch Card By ID
Endpoint for fetching a card by it's cardId
`GET /api/v1/cards/{cardId}`

### Fetch Cards (Paginated Result)
Endpoint for fetching a cards by Filters
`GET /api/v1/cards/`

#### CardFilters Request
{
"name": "string",
"color": "string",
"status": "To_Do",
"dateCreated": "2024-04-10T20:51:26.196Z"
}

#### Pagination Filters Request
{
"page": 0,
"pageSize": 0,
"sortBy": "string",
"sortOrder": "string"
}

### Delete Cards
Endpoint for deleting a card by CardId
`GET /api/v1/cards/{cardId}`




