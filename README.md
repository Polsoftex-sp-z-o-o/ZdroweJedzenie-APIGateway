# ZdroweJedzenie-APIGateway



## POST /login  
ROLES: ANY  
AUTH: NOT REQUIRED  
```json
{  
      "email" : "admin14@gmail.com",  
      "password" : "pass"  
}  
```
returns 200 with JWT authentication token or 403 forbidden in case of invalid credentials  

**TOKEN PAYLOAD**  
```json
{
      "jti": "zdrowe-jedzenie-jwt",
      "sub": "admin14@gmail.com",
      "authorities": [
            "ROLE_USER"
            ],
      "first-name": "Adam",
      "last-name": "Admiński",
      "user-id": "b9b49f47-de10-4929-bb28-42cf9e04a06b",
      "iat": 1620084842,
      "exp": 1620085442
} 
```  
**TOKEN ENCRYPTION**: HS512  

## POST /users   
ROLES: ANY  
AUTH: NOT REQUIRED  
 
```json
{
    "email" : "admin14@gmail.com",
    "address" : "testowo 10/168",
    "password" : "pass",
    "confirmPassword": "pass",
    "firstName" : "Adam",
    "lastName" : "Admiński"
}
```
creates user and returns 200 or 405 in case of bad password validation
user will have role USER by default (non elevated)

## PUT /users/b9b49f47-de10-4929-bb28-42cf9e04a06b
ROLES: USER, ADMIN  
AUTH: REQUIRED  

```json
{
    "email" : "admin14@gmail.com",
    "address" : "testowo 10/168",
    "password" : "pass",
    "confirmPassword": "pass",
    "firstName" : "Adam",
    "lastName" : "Admiński"
}
```
returns 200 or 405 in case of bad password validation  
can only edit your own user entity - otherwise forbidden!

## GET /products  
ROLES: ANY  
AUTH: NOT REQUIRED  
returns products list

## GET /products/getAllCategories  
ROLES: ANY  
AUTH: NOT REQUIRED  
returns all product categories

## GET /products/ids?ids=77372d83-041a-43b6-933a-9d2df61a182c&ids=77372d83-041a-43b6-933a-9d2df61a182c  
ROLES: ANY  
AUTH: NOT REQUIRED  
returns array of products of given id


## POST /products
ROLES: ADMIN  
AUTH: REQUIRED  

```json
{
    "name":"bułka",
    "description":"przysmak cymes",
    "quantity":10
}
```
adds product to database

## PUT /products/1f0ccf58-a390-4aab-af22-439e354d7f50
ROLES: ADMIN  
AUTH: REQUIRED  

```json
{
      "name": "BONDUELLE Gotowana na parze Cieciorka",
      "description": "Cieciorka gotowana na parze to mistrzowskie polaczenie chrupkosci, delikatnosci, pieknej zlotej barwy i skladników odzywczych.",
      "category": "Przetwory owocowe i warzywne",
      "quantity": 1000,
      "price": 5.49
}
```
edits product in database


## DELETE /products/1f0ccf58-a390-4aab-af22-439e354d7f50
ROLES: ADMIN  
AUTH: REQUIRED  

removes product from database

## GET /cart/?userid=c49f0df2-57f3-4ce6-93de-335898afc3ed
ROLES: USER  
AUTH: REQUIRED  
returns active user cart content, user id needs to be authenticated user - otherwise forbidden


## GET /images
ROLES: ANY  
AUTH: NOT REQUIRED  
returns all stored images - encoded via base64


## GET /images/1f0ccf58-a390-4aab-af22-439e354d7f50
ROLES: ANY  
AUTH: NOT REQUIRED  
returns image of product with specified id - encoded via base64


## POST /images
ROLES: ADMIN  
AUTH: REQUIRED  

```json
{
    "id":"1f0ccf58-a390-4aab-af22-439e354d7f50",
    "image":"{STRING BASE64}"
}
```
uploads image for product with specified id - image should be encoded via base64



## POST /cart/?userid=c49f0df2-57f3-4ce6-93de-335898afc3ed
ROLES: USER WITH ID  
AUTH: REQUIRED  

```json
{
    "productId": "bec096db-7e52-4a4d-9ca3-383dd20e02fb",
    "quantity": 3
}
```
adds product to cart  
returns cart content (like GET /cart)

## DELETE /cart/?userid=c49f0df2-57f3-4ce6-93de-335898afc3ed
ROLES: USER WITH ID  
AUTH: REQUIRED  
clears cart for user

## POST /payment/?userid=c49f0df2-57f3-4ce6-93de-335898afc3ed
ROLES: USER WITH ID  
AUTH: REQUIRED  

```json
{
    "number": "4485568736434905",
    "expiration" : "02/26",
    "code": "112"
}
```
creates order out of active user cart - returns 404 if cart doesnt exist


