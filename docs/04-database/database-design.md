# Database Design – Marucha Mobile App

## 1. Introduction

This document describes the database design for **Marucha Mobile App**, a restaurant mobile application that allows customers to browse products, manage a cart, place orders, and review their order history, while also allowing administrators to manage restaurant data such as products, categories, and order status.

The database is designed to support a single mobile application with two user roles:

- **Customer**: restaurant client who uses the mobile app to browse the menu, add products to the cart, place orders, and view order history.
- **Administrator**: restaurant manager or staff member who uses the same application to manage products, categories, and customer orders.

The application uses a **PHP backend** connected to a **MySQL database**, and the mobile app will communicate with the backend through an API.

---

## 2. Database Design Objectives

The database has been designed with the following objectives:

- Support authentication for both customers and administrators.
- Store user profile data, including standard registration and Google authentication.
- Manage restaurant categories and platillos (products).
- Persist shopping cart data for each user.
- Register orders and order details.
- Manage order states and order history tracking.
- Store payment method information.
- Allow the system to scale with future modules if needed.

---

## 3. General System Scope

The system is focused on the restaurant ordering process through a mobile application. The current version of the project includes the following main areas:

### Customer features
- User registration and login
- Google sign-in support
- Product browsing by category
- Platillo detail view
- Shopping cart management
- Order confirmation
- Payment method selection
- Order history
- User profile management

### Administrator features
- Login as administrator
- Category management
- Platillo management
- Order monitoring
- Order state updates

---

## 4. Database Model Overview

The database model for Marucha is divided into the following logical modules:

### 4.1 Security and Users
This module manages users, their roles, and their saved addresses.

**Tables:**
- `rol`
- `usuario`
- `direccion_usuario`

---

### 4.2 Product Catalog
This module stores restaurant categories and platillos available in the app.

**Tables:**
- `categoria`
- `platillo`

---

### 4.3 Shopping Cart
This module persists the user’s shopping cart and the selected products before order confirmation.

**Tables:**
- `carrito`
- `carrito_detalle`

---

### 4.4 Orders and Payments
This module manages order registration, order details, payment method, order state, and order state history.

**Tables:**
- `metodo_pago`
- `estado_pedido`
- `pedido`
- `detalle_pedido`
- `pedido_historial_estado`

---

## 5. Main Design Decisions

### 5.1 Single mobile application with role-based access
The system will use **a single Android mobile application** for both customers and administrators.  
The interface and available features will depend on the authenticated user’s role.

### 5.2 Cart persistence in the database
The shopping cart will be stored in the database to allow persistence and better control of the ordering flow.

### 5.3 No formal delivery module
The current version of Marucha **does not include a formal delivery management module**.  
However, the system still stores customer addresses or references to support order coordination and pickup/delivery references when needed.

### 5.4 Order state tracking
Orders will have both:
- a **current state** stored in the `pedido` table
- a **state history** stored in `pedido_historial_estado`

This allows the app to show progress information to the customer, such as:
- pending
- confirmed
- in preparation
- ready
- delivered
- cancelled

### 5.5 Payment methods
The initial version of the system will support payment methods such as:
- **Yape**
- **Cash**

---

## 6. Entity Description

### 6.1 `rol`
Stores the available roles in the system, such as customer and administrator.

### 6.2 `usuario`
Stores the personal, authentication, and profile information of each user.

### 6.3 `direccion_usuario`
Stores one or more saved addresses or references for each user.

### 6.4 `categoria`
Stores product categories such as *Entradas*, *Segundos*, and *Bebidas*.

### 6.5 `platillo`
Stores the products/platillos available in the restaurant menu.

### 6.6 `carrito`
Represents the current shopping cart of a user.

### 6.7 `carrito_detalle`
Stores the platillos added to a shopping cart.

### 6.8 `metodo_pago`
Stores the payment methods supported by the application.

### 6.9 `estado_pedido`
Stores the possible order states.

### 6.10 `pedido`
Stores the main order information, including customer, payment method, state, totals, and order date.

### 6.11 `detalle_pedido`
Stores the detail lines of each order.

### 6.12 `pedido_historial_estado`
Stores the state history of each order to provide traceability.

---

## 7. Entity Relationships

The main relationships of the system are the following:

- One **rol** can be assigned to many **usuarios**
- One **usuario** can have many **direcciones**
- One **categoria** can contain many **platillos**
- One **usuario** can have many **carritos**
- One **carrito** can contain many **carrito_detalle**
- One **platillo** can appear in many **carrito_detalle**
- One **usuario** can register many **pedidos**
- One **direccion_usuario** can be used in many **pedidos**
- One **metodo_pago** can be associated with many **pedidos**
- One **estado_pedido** can be associated with many **pedidos**
- One **pedido** can contain many **detalle_pedido**
- One **platillo** can appear in many **detalle_pedido**
- One **pedido** can have many **historiales de estado**

---

## 8. Notes for Future Versions

The following elements are intentionally left for future iterations if needed:

- formal delivery management
- invoice/comprobante module
- advanced payment registration
- promotions or discount system
- loyalty or points module
- inventory management for ingredients

---

## 9. Conclusion

The current database design for **Marucha Mobile App** provides a structured foundation for the restaurant’s mobile ordering process.  
It supports the essential operations of both customers and administrators while keeping the design scalable for future enhancements.