# BitePlate SRMS — Final Assignment-Ready Version v5

This project was rebuilt against the Unit 27 Advanced Programming brief, not just as a generic Spring Boot app.

## Stack

- Java 17
- Spring Boot 3.5.0
- Spring Web
- Spring Data JPA
- PostgreSQL
- BCrypt password hashing
- HTML, CSS, JavaScript
- PlantUML diagrams

## Database setup

Create the PostgreSQL database first:

```sql
CREATE DATABASE biteplate_db;
```

Database settings are environment-based:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/biteplate_db}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:Password}
app.seed.default-password=${APP_SEED_PASSWORD:1234}
```

## Run

```bash
mvn clean spring-boot:run
```

Open:

```text
http://localhost:8080/login.html
```

## Default users

Default password is `1234`, unless you change `APP_SEED_PASSWORD`.

| Username | Role | Dashboard |
|---|---|---|
| manager | MANAGER | /manager-dashboard.html |
| waiter | WAITER | /waiter-dashboard.html |
| chef | HEAD_CHEF | /chef-dashboard.html |
| cashier | CASHIER | /cashier-dashboard.html |

If you previously ran an older version with plain-text passwords, drop and recreate the database.

## Dedicated role dashboards

This version has different dashboards by assigned staff task:

- `manager-dashboard.html`: overview, reservations, orders, kitchen, billing, audit/history.
- `waiter-dashboard.html`: table seating, reservations, order creation and modification.
- `chef-dashboard.html`: kitchen command queue, reprioritise, undo, cancellation, notifications.
- `cashier-dashboard.html`: billing/POS, order lookup, table status.

## Assignment requirement coverage

| Brief requirement | Implementation |
|---|---|
| Table Management | `RestaurantTable`, `TableStatus`, table UI |
| Reservation System | `Reservation`, `ReservationService`, reservation UI, reminder action |
| Order Management | `CustomerOrder`, `OrderItem`, add/remove before preparation |
| Kitchen Queue | Command Pattern with `KitchenQueue`, undo and reprioritise |
| Meal Customisation | Add-ons, substitutions, allergy notes + Decorator Pattern classes |
| Combo / Set Meals | `ComboMeal` composite relationship with `MenuItem` |
| Pricing & Discount Engine | Strategy Pattern: Standard, Happy Hour, Loyalty Card, Group Discount |
| Order History & Audit Log | `OrderHistoryLog` Singleton + `OrderHistoryRecord` + `AuditLog` |
| Billing & POS | `Bill`, `BillLineItem`, `BillRecord`, tip, split bill, tax |
| Staff Role Management | Login, registration, staff subclasses, role dashboards |

## Required implemented patterns

### Command Pattern

Package:

```text
com.biteplate.pattern.command
```

Evidence:

- `KitchenCommand`
- `PrepareOrderCommand`
- `CancelOrderCommand`
- `KitchenQueue`

### Singleton Pattern

Package:

```text
com.biteplate.pattern.singleton
```

Evidence:

- `OrderHistoryLog`
- `OrderHistoryRecord`
- `OrderHistoryRecordRepository`
- `/api/history`

### Strategy Pattern

Package:

```text
com.biteplate.pattern.pricing
```

Evidence:

- `PricingStrategy`
- `StandardPricing`
- `HappyHourPricing`
- `LoyaltyCardPricing`
- `GroupDiscountPricing`
- `PricingStrategyFactory`

## Additional design patterns included

- Observer Pattern: notification center for reservations and allergy alerts.
- Factory Method: menu item factories.
- Decorator Pattern: menu customisation decorators.
- Composite Pattern: combo meals contain multiple menu items.
- Facade Pattern: billing facade concept.
- Iterator: `OrderHistoryLog implements Iterable<OrderHistoryRecord>`.

## Diagrams included

Located in:

```text
diagrams/plantuml/
```

Included diagrams:

1. Core class diagram
2. Command pattern class diagram
3. Observer pattern class diagram
4. Strategy pattern class diagram
5. Decorator pattern class diagram
6. Factory Method pattern class diagram
7. Activity diagram: full order lifecycle
8. Activity diagram: order history recording and query
9. Sequence diagram: order to billing
10. Precedence diagram: system workflow dependencies

## Suggested screenshots for submission

Take at least four, but ideally capture these:

1. Login page.
2. Registration page.
3. Manager dashboard.
4. Waiter dashboard creating an order.
5. Chef dashboard processing kitchen queue.
6. Cashier dashboard generating split bill.
7. PostgreSQL tables in pgAdmin.
8. Audit/history page or endpoint.

## Academic integrity note

This is a learning prototype. You must understand the code, test it locally, and be ready to explain each design pattern to your tutor.


## Docker support added in v8

The project now includes:

```text
Dockerfile
docker-compose.yml
.env.example
.gitignore
.github/pull_request_template.md
docs/GITHUB_SUBMISSION_GUIDE.md
SUBMISSION_CHECKLIST.md
```

Run with Docker:

```bash
docker compose up --build
```

Then open:

```text
http://localhost:8080/login.html
```

Docker Compose starts:

- PostgreSQL on port `5432`
- Spring Boot app on port `8080`

## GitHub pull request submission

Read:

```text
docs/GITHUB_SUBMISSION_GUIDE.md
```

Recommended branch name:

```text
xojiakbar-unit27-biteplate
```
