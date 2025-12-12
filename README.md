# 🏥 Healthcare Management System (CLI)

A **Java Spring Boot CLI-based Healthcare Management System** for managing patients, doctors, appointments, and notifications.  
The project demonstrates **Object-Oriented Programming**, **Spring Boot**, **JPA/Hibernate**, and **MySQL** in a real-world healthcare domain.

---

## 📌 Features

### 👤 Patient Management
- Register patients
- View all patients
- Search patients by name
- Update patient information
- Delete patients
- Enforces unique email and phone number

### 👨‍⚕️ Doctor Management
- Register doctors with specialties
- View all doctors
- Search doctors by specialty
- Update doctor information
- Delete doctors

### 📅 Appointment Management
- Schedule appointments
- View appointments by status:
    - Planifikuar
    - Përfunduar
    - Anuluar
- View appointments per patient
- Complete appointments with medical report
- Cancel appointments

### 🔔 Notification System
- Send notifications via:
    - Email
    - SMS

---

## 🧱 Architecture

Layered architecture:
```
CLI (Presentation Layer)
Service Layer (Business Logic)
Repository Layer (Data Access)
Entity Layer (Domain Model)
```
---

## 🗂️ Project Structure

```text
healthcare-management/
├── README.md
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── healthcare/
│       │           └── management/
│       └── resources/
│           └── application.properties
```

---

## 🛠️ Technologies Used

- Java 17
- Spring Boot 3.4.12
- Spring Data JPA
- Hibernate
- MySQL
- Maven

---

## 🧪 Business Rules

- Patient age must be greater than 0
- Patient email and phone must be unique
- Completed appointments cannot be canceled
- Canceled appointments cannot be completed
- Medical report is mandatory when completing an appointment

---

## ⚙️ Configuration

spring.datasource.url=jdbc:mysql://localhost:3306/healthcare_db  
spring.datasource.username=root  
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update  
spring.jpa.show-sql=true  
spring.jpa.properties.hibernate.format_sql=true  
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

---

## ▶️ How to Run
### 1️⃣ Prerequisites

- Java 17
- Maven
- MySQL Server

### 2️⃣ Create the Database

Log in to MySQL and create the database:
```CREATE DATABASE healthcare_db;```

### 3️⃣ Configure MySQL Credentials

Open the following file:

```src/main/resources/application.properties```


Update it with your MySQL username and password:

```
spring.datasource.url=jdbc:mysql://localhost:3306/healthcare_db
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

⚠️ Add your own MySQL username and password.

### 4️⃣ Run the Application

From the project root (where pom.xml is located), run:

```mvn spring-boot:run```

### 5️⃣ Use the CLI

After startup, the Command Line Interface (CLI) menu will appear in the terminal.
Use it to manage patients, doctors, appointments, and notifications.

---

## 📖 CLI Menu Example

```
P - Veprime me Pacientët  
D - Veprime me Doktorët  
A - Veprime me Terminet  
N - Dërgo Njoftim  
X - Dil
```