# Marucha Mobile App

Mobile application for **Restaurant Marucha**, developed to manage the restaurant’s digital ordering process through an Android application connected to a PHP backend and a MySQL database.

---

# Project Overview

**Marucha Mobile App** is a restaurant mobile system designed for two types of users:

- **Customer**: the user who browses the menu, places orders, and manages their profile.
- **Administrator**: the user responsible for managing products, categories, orders, and general restaurant operations.

The project follows a client-server architecture:

- **Android mobile application** as the client interface
- **PHP backend/API** for business logic and data processing
- **MySQL database** for persistent data storage

The application is being developed locally using **XAMPP** for backend and database testing, with the intention of later deploying the backend and database to a cloud/hosting environment for production or portfolio demonstration.

---

# Project Objective

Develop a mobile application for Restaurant Marucha that allows customers to place and manage orders from their mobile devices, while also providing administrative tools for managing products, categories, and restaurant orders through a backend connected to a MySQL database.

---

# User Roles

## 1. Customer
The customer is the restaurant client who interacts with the mobile application to browse the menu and place orders.

### Planned customer features
- User registration and login
- Menu and product browsing
- Product search and category filtering
- Add products to cart
- Place orders
- View order history
- View order details
- Profile management
- Logout

---

## 2. Administrator
The administrator manages restaurant data and order operations.

### Planned administrator features
- Secure login
- Product management
- Category management
- Order monitoring
- Order status updates
- Sales overview or order control
- Basic user/customer management if required

---

# Main Modules

## Customer Module
- Authentication
- Product catalog
- Product detail view
- Shopping cart
- Order registration
- Order history
- Profile management

## Administrator Module
- Authentication
- Product management
- Category management
- Order management
- Order status control
- Sales or operational overview

## Backend Module
- User authentication
- Product and category endpoints
- Order processing
- Database interaction
- Business rules and validations

## Database Module
- User management tables
- Product and category tables
- Order and order detail tables
- Administrative support tables
- SQL scripts, procedures, views and triggers if required

---

# Tech Stack

## Mobile Application
- **Android Studio**
- **Kotlin or Java** *(to be defined according to implementation)*
- Android SDK / XML or modern Android components

## Backend
- **PHP**
- **Apache** through XAMPP for local development
- REST-style API structure

## Database
- **MySQL**

## Version Control and Documentation
- **Git**
- **GitHub**
- **Markdown documentation**

---

# Development and Deployment Strategy

## Local Development Environment
During development, the project will be built and tested locally using:

- Android Studio for the mobile application
- XAMPP for Apache and PHP execution
- MySQL for local database management

This environment will be used to:
- build the database
- develop the backend API
- test authentication and business logic
- connect the Android application to the local backend

## Future Cloud Deployment
Once the project becomes stable and functional, the backend and database are planned to be deployed to a cloud or hosting environment in order to:

- avoid dependency on local XAMPP execution
- expose the backend through a public URL
- make the project easier to demonstrate as a portfolio piece
- simulate a more realistic production environment

---

# Repository Structure

```bash
Mobile-Application-for-Restaurant-Marucha-/
├── app-android/        # Android Studio mobile application
├── backend-php/        # PHP backend / API
├── database/           # MySQL scripts, procedures, views and triggers
├── docs/               # Project documentation
├── assets/             # Screenshots, branding, mockups and diagrams
├── tools/              # Auxiliary resources such as Postman collections or scripts
├── README.md
├── LICENSE
└── CHANGELOG.md