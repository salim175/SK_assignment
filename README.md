# Welcome to salim's assignment

## 1. clone the project:
```bash
git clone https://github.com/salim175/SK_assignment.git

cd SK_assignment
```

## 2. start everything:
```bash
docker-compose up --build
```

## 3. browser:
**open application:** http://localhost:5173

**API Docs:** http://localhost:8081/swagger-ui/index.html

## 4. EndPoints (6):

### without Authentication:
-  `POST /api/v1/users/register` - Register user
- `POST /api/v1/users/login` - Login user

### requires Token
- `GET /api/v1/users/allUsers` - get all users
- `GET /api/v1/users/{id}` - get user by Id
- `PUT /api/v1/users/update/{id}` - update user
- `DELETE /api/v1/users/delete/{id}` - delete user

**NOTE:** to test endpoint that required Token, you should login to get and copy TOKEN, then click on `Authorize` in swagger UI and paste it

