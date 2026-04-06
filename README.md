# Food Ordering Management System (Java Swing + PostgreSQL)

A desktop-based Food Ordering Management System built with **Java Swing** (UI) and **PostgreSQL (psql)** (database).  
It supports full CRUD operations for customers, menu items, orders, payments, and ratings, plus reporting features like:

- **Annual expense per customer**
- **Calorie intake tracking (per order and summary)**

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Main Features](#main-features)
- [Project Structure](#project-structure)
- [Database Setup (PostgreSQL)](#database-setup-postgresql)
- [Java Setup & Run](#java-setup--run)
- [Default Login](#default-login)
- [Reports](#reports)
- [Notes / Improvements](#notes--improvements)
- [License](#license)

---

## Tech Stack

- **Java (Swing, JDBC)**
- **PostgreSQL**
- **JDBC Driver:** `postgresql-42.7.1.jar` (or compatible version)

---

## Main Features

### Authentication
- User login system (table: `users`)

### Customer Management
- Add / Update / Delete customers
- Search customers by name/email

### Menu Management
- Add / Update / Delete menu items
- Menu categories (`menu_type`)
- Stores **calories per serving** in `menu.calories_per_serving`

### Order Management
- Create orders
- Add/remove items from an order
- Auto-updates order totals
- Order status: Pending, Processing, Completed, Cancelled

### Payments
- Process payments against orders
- Payment method options (cash, card, online, etc.)

### Ratings
- Customer can rate menu items (1–5)
- Unique rating per `(menu_id, customer_id)` to prevent duplicates

### Reporting (Extra Features)
- **Annual Expense per Customer**
- **Calorie Intake Report**
  - Detail view: calories per order
  - Summary view: calories per customer per year

---
FoodOrderSystem/
│
├── src/                              ← Source code folder
│   │
│   ├── config/                       ← Database configuration
│   │   └── DBConnection.java         ← PostgreSQL connection class
│   │
│   ├── model/                        ← Data model classes
│   │   ├── Customer.java             ← Customer entity
│   │   ├── Menu.java                 ← Menu item entity
│   │   ├── Order.java                ← Order entity
│   │   └── OrderDetails.java         ← Order details entity
│   │
│   ├── dao/                          ← Data Access Objects (Backend Logic)
│   │   ├── CustomerDAO.java          ← Customer DB operations
│   │   ├── MenuDAO.java              ← Menu DB operations
│   │   └── OrderDAO.java             ← Order DB operations
│   │
│   ├── ui/                           ← User Interface (Frontend - Swing)
│   │   ├── LoginFrame.java           ← Login window
│   │   ├── RegisterFrame.java        ← Registration window
│   │   ├── MenuFrame.java            ← Menu browsing window
│   │   ├── CartFrame.java            ← Shopping cart window
│   │   ├── OrderFrame.java           ← Order history window
│   │   └── AdminDashboard.java       ← Admin panel window
│   │
│   └── TestDB.java                   ← Database test file (optional)
│
├── lib/                              ← External libraries (JAR files)
│   ├── postgresql-42.7.10.jar        ← PostgreSQL JDBC driver
│   
├── out/                              ← Compiled .class files (auto-generated)
│   └── (compiled Java files)
│


orders.customer_id → customer.id
order_details.order_id → orders.id
order_details.menu_id → menu.id
menu.type_id → menu_type.id
rating.customer_id → customer.id
rating.menu_id → menu.id
payment.order_id → orders.id
payment.paid_by → customer.id
payment.processed_by → users.id
site_information.user_id → users.id


