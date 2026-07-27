# Billing & Inventory System - Phase 1

A complete **Java Full-Stack** Billing & Inventory Management System built with **Spring Boot** + **React** + **PostgreSQL**.

## Architecture

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2, Java 21, JPA/Hibernate |
| Frontend | React 18, TypeScript, Tailwind CSS, Chart.js |
| Database | PostgreSQL 16 |
| Auth | JWT (Spring Security) |
| Docs | OpenAPI/Swagger |
| QR/Barcode | ZXing |
| PDF | iText 8 |
| Excel | Apache POI |

## Features (Phase 1)

### Authentication
- Admin & Employee login with JWT
- Role-based access control (RBAC)
- Secure password hashing (BCrypt)

### Dashboard
- Today/Weekly/Monthly/Yearly sales
- Total revenue, bills, products, stock
- Low stock alerts
- Charts: Daily Sales, Monthly Trend, Top Products

### POS Billing
- Barcode scanner support
- Product search
- Auto GST (CGST/SGST) calculation
- Discount application
- Multiple payment methods (Cash/Card/UPI)
- Auto stock reduction
- Bill generation with unique bill numbers

### Bill History
- Permanent bill storage
- Search by bill number, date, cashier
- View bill details
- Re-print capability

### Product Management
- Add/Edit/Delete products
- Auto barcode & QR code generation
- GST & HSN code support
- Stock tracking with low stock alerts
- Image upload support

### Settings
- Company profile
- Invoice size (A4/Thermal)
- Dark/Light mode
- Tax settings

## Quick Start

### Prerequisites
- Java 21
- Node.js 18+
- PostgreSQL 16
- Maven

### Backend Setup
```bash
cd backend
# Update application.yml with your DB credentials
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```

### Docker Setup (Recommended)
```bash
docker-compose up -d
```

## Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Employee | cashier | cashier123 |

## API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Auth
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Register (Admin only)

### Products
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/barcode/{barcode}` - Get by barcode
- `GET /api/products/search?q=` - Search products
- `POST /api/products` - Create product (Admin)
- `PUT /api/products/{id}` - Update product (Admin)
- `DELETE /api/products/{id}` - Delete product (Admin)
- `GET /api/products/low-stock` - Low stock products

### Bills
- `POST /api/bills` - Create bill
- `GET /api/bills` - List all bills
- `GET /api/bills/{id}` - Get bill by ID
- `GET /api/bills/search?q=` - Search bills

### Dashboard
- `GET /api/dashboard` - Dashboard analytics

### Settings
- `GET /api/settings` - Get settings
- `PUT /api/settings` - Update settings (Admin)

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| DB_USERNAME | billing_user | PostgreSQL username |
| DB_PASSWORD | billing_pass | PostgreSQL password |
| JWT_SECRET | (default) | JWT signing key |
| CORS_ORIGINS | http://localhost:3000 | Allowed frontend origin |

## License
MIT
