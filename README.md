# Virtual Pet Care Application

A complete web application for caring and interacting with virtual virtualpets. Users can create, feed, and play with them while managing their energy, hunger, and happiness levels.

## Description

The Virtual Pets Application allows users to:

- **Register and login** with JWT authentication
- **Create virtual virtualpets** (mole, magpie, toad)
- **Care for virtualpets** by feeding and playing with them
- **Manage stats** for energy, hunger, and happiness
- **Role system** (USER/ADMIN) with differentiated permissions


## ️ Technologies Used

### Backend

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA** (Persistence)
- **MySQL** (Database)
- **Lombok** (Boilerplate code reduction)
- **Maven** (Dependency management)


### Frontend

- **React 18**
- **JavaScript/JSX**
- **CSS3** (Animations and styling)
- **Axios** (HTTP requests)
- **React Router** (Navigation)


## Prerequisites

- **Java 21** or higher
- **Node.js 18** or higher
- **Maven 3.8** or higher
- **XAMPP** (for MySQL) or MySQL installed
- **Git**


## Installation and Setup

### 1. Clone Repository

```shellscript
git clone https://github.com/Meriekje/5.2.-Spring_virtual-virtualpets.git
cd virtual-virtualpets
```

### 2. Database Setup

1. Start **XAMPP** and activate **MySQL**
2. Go to [http://localhost/phpmyadmin](http://localhost/phpmyadmin)
3. Create database: `pets_db`


### 3. Backend Configuration

```shellscript
cd backend
```

Configure `src/main/resources/application.properties`:

```plaintext
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/pets_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# JWT Configuration
jwt.secret=mySecretKey123456789012345678901234567890123456789012345678901234567890
jwt.expiration=86400000

# Server Configuration
server.port=8080
```

### 4. Run Backend

```shellscript
mvn clean install
mvn spring-boot:run
```

### 5. Frontend Configuration

```shellscript
cd ../frontend
npm install
npm start
```

## ️ Project Structure

```plaintext
virtual-virtualpets/
├── backend/
│   ├── src/main/java/virtual_pets/
│   │   ├── controller/          # REST Controllers
│   │   ├── service/             # Business logic
│   │   ├── entity/              # JPA Entities
│   │   ├── repository/          # Data repositories
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── security/            # JWT Configuration
│   │   └── config/              # Configurations
│   └── src/main/resources/
│       ├── application.properties
│       └── data.sql
├── frontend/
│   ├── src/
│   │   ├── components/          # React Components
│   │   ├── services/            # API Services
│   │   ├── assets/              # SVG Images
│   │   └── utils/               # Utilities
│   └── public/
└── README.md
```

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register user
- `POST /api/auth/login` - User login


### Pets

- `GET /api/virtualpets` - Get user's virtualpets
- `POST /api/virtualpets` - Create new pet
- `GET /api/virtualpets/{id}` - Get specific pet
- `PUT /api/virtualpets/{id}` - Update pet
- `DELETE /api/virtualpets/{id}` - Delete pet


### Actions

- `POST /api/virtualpets/{id}/alimentar` - Feed pet
- `POST /api/virtualpets/{id}/jugar` - Play with pet
- `POST /api/virtualpets/{id}/dormir` - Make pet sleep


### Administration (ROLE_ADMIN)

- `GET /api/admin/virtualpets` - View all virtualpets
- `GET /api/admin/users` - Manage users


## Features

### Level 1 - Basic Functionality

- ✅ JWT Authentication
- ✅ Pet CRUD operations
- ✅ Role system (USER/ADMIN)
- ✅ Stats management (energy, hunger, happiness)


### Level 2 - Improvements

- ✅ Logging system (SLF4J)
- ✅ In-memory cache for optimization
- ✅ Advanced validations


### Level 3 - Testing

- ✅ Integration tests
- ✅ Unit tests
- ✅ Code coverage


## Pet Types

### Mole

- **Special ability**: Digging
- **Favorite food**: Worms
- **Characteristics**: High endurance, low initial energy


### Magpie ‍

- **Special ability**: Flying and finding shiny objects
- **Favorite food**: Seeds
- **Characteristics**: High energy, curious


### Toad

- **Special ability**: Jumping and swimming
- **Favorite food**: Flies
- **Characteristics**: Balanced, adaptable


## Visual Features

- **Scalable SVG images** for each pet
- **CSS animations** for states (happy, sad, tired)
- **Responsive interface** for desktop and mobile
- **Progress bars** for energy, hunger, and happiness
- **Visual effects** for actions (feeding, playing)


## Development

### Run in Development Mode

```shellscript
# Backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Frontend
npm run dev
```

### Run Tests

```shellscript
# Backend
mvn test

# Frontend
npm test
```

### Production Build

```shellscript
# Backend
mvn clean package

# Frontend
npm run build
```

## Troubleshooting

### MySQL Connection Error

```shellscript
# Verify XAMPP MySQL is running
# Check that 'pets_db' database exists
```

### JWT Error

```shellscript
# Verify jwt.secret has at least 64 characters
# Check JWT dependencies are in pom.xml
```

### CORS Error

```shellscript
# Verify cors.allowed-origins in application.properties
# Ensure frontend runs on http://localhost:3000
```

## Environment Variables

### Backend

```plaintext
# Database
DB_URL=jdbc:mysql://localhost:3306/pets_db
DB_USERNAME=root
DB_PASSWORD=

# JWT
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
```

### Frontend

```plaintext
REACT_APP_API_URL=http://localhost:8080/api
```

## Contributing

1. Fork the project
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -m 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Open a Pull Request


## License

This project is licensed under the MIT License. See `LICENSE` for more details.


## Acknowledgments

- Spring Boot for the excellent framework
- React for the user interface
- MySQL for data persistence
- The Noun Project for pet SVG images