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

## GET /cart?userid=c49f0df2-57f3-4ce6-93de-335898afc3ed
ROLES: USER  
AUTH: REQUIRED  
returns active user cart content, user id needs to be authenticated user - otherwise forbidden



## POST /cart?userid=c49f0df2-57f3-4ce6-93de-335898afc3ed
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

## DELETE /cart?userid=c49f0df2-57f3-4ce6-93de-335898afc3ed
ROLES: USER WITH ID  
AUTH: REQUIRED  
clears cart for user

## POST /order?userid=c49f0df2-57f3-4ce6-93de-335898afc3ed
ROLES: USER WITH ID  
AUTH: REQUIRED  
creates order out of active user cart - returns 404 if cart doesnt exist


