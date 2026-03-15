# MedNex - Multi-Tenant Hospital Management System (HMS)

MedNex is a production-ready, full-stack Hospital Management System designed with a multi-tenant architecture. It supports multiple hospitals (tenants) using a shared database with separate schemas, ensuring data isolation and high security.

## 🚀 Key Features

### **1. Multi-Tenant Architecture**
- **Schema Isolation**: Each hospital (`hospital_a`, `hospital_b`) has its own dedicated schema.
- **Dynamic Routing**: The backend automatically switches database contexts based on the `X-Tenant-ID` header.
- **Centralized Admin**: A global public schema manages cross-tenant hospital configurations.

### **2. Electronic Medical Records (EMR)**
- **Comprehensive Admission**: A 50+ field admission form with real-time BMI calculation.
- **Medical History**: Secure storage of patient vitals, medications, and allergies using PostgreSQL `JSONB` for flexibility.
- **Encrypted PDF Export**: Doctors and Admins can export patient records as password-protected, encrypted PDFs.

### **3. Smart Appointment Calendar**
- **FullCalendar Integration**: A drag-and-drop interface for scheduling appointments.
- **Conflict Detection**: Prevents double-booking for doctors across time slots.
- **Role-Based Access**: Doctors can manage their own schedules, while admins have global visibility.

### **4. Advanced Analytics & Compliance**
- **Real-time Dashboard**: Visualizes bed occupancy, admission trends, and doctor workloads using `ng2-charts`.
- **System Audit Logs**: Automated logging of all critical actions (View, Create, Export) with User ID, IP, and Timestamp for compliance.

---

## 📂 Project Structure

- **`hospital-backend/`**: Spring Boot 3.x application (Java 17, Hibernate, Spring Security, JWT).
- **`hospital-management-system/`**: Angular 17 application (Standalone components, Bootstrap 5, ng2-charts).

---

## 🗄️ Database Structure (PostgreSQL)

The system uses a schema-per-tenant strategy.

### **Global Configuration (Public Schema)**
```sql
CREATE TABLE hospitals (
    id UUID PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL, -- e.g., hospital_a, hospital_b
    name VARCHAR(100),
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE public.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50)
);
```

### **Tenant-Specific Tables (hospital_a / hospital_b)**
```sql
-- Users (Doctors, Nurses, Admins)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Doctors
CREATE TABLE doctors (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    specialization VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Patients (with JSONB EMR)
CREATE TABLE patients (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    admission_details JSONB,
    medical_history JSONB,
    assigned_doctor_username VARCHAR(100)
);

-- Appointments
CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    doctor_username VARCHAR(100) NOT NULL,
    patient_id INTEGER REFERENCES patients(id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    title VARCHAR(200),
    notes TEXT
);

CREATE INDEX idx_appointments_doctor_time ON appointments (doctor_username, start_time, end_time);
```

---

## 🛠️ Installation & Setup

### **Prerequisites**
- Java 17+
- Node.js 18+ & Angular CLI
- PostgreSQL 14+

### **1. Database Setup**
1. Create a PostgreSQL database named `hospital_db`.
2. Ensure you have schemas `hospital_a` and `hospital_b` (the app will attempt to create these on startup if permissions allow).

### **2. Backend Setup**
```bash
cd hospital-backend
./mvnw spring-boot:run
```
- The backend will start on `http://localhost:8081`.
- Default credentials (seeded automatically):
  - Admin: `admin` / `admin123`
  - Doctor: `doctor` / `doctor123`

### **3. Frontend Setup**
```bash
cd hospital-management-system
npm install
npm start
```
- The frontend will start on `http://localhost:4200`.

---

## 🔒 Security Note
- **JWT Authentication**: All requests require a valid Bearer token.
- **PDF Encryption**: When exporting a patient record, you must provide a password. This password is required to open the resulting PDF file.
