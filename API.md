# MedNex Enterprise API Documentation

## Authentication

### `POST /api/auth/register`

Registers a new user.

**Request Body:**

```json
{
  "username": "string",
  "password": "string",
  "role": "string" // ADMIN, DOCTOR, or NURSE
}
```

**Response:**

```json
{
  "message": "User registered successfully"
}
```

### `POST /api/auth/login`

Logs in a user and returns a JWT token.

**Request Body:**

```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**

```json
{
  "token": "string"
}
```

## Patients

### `POST /api/patients/admission`

Admits a new patient.

**Request Body:**

```json
{
  "firstName": "string",
  "lastName": "string",
  "dob": "yyyy-mm-dd",
  "gender": "string",
  "contactNumber": "string",
  "email": "string",
  "admissionDetails": {},
  "medicalHistory": {},
  "allergies": {},
  "medications": {}
}
```

**Response:**

```json
{
  "id": "number",
  "firstName": "string",
  "lastName": "string",
  "dob": "yyyy-mm-dd",
  "gender": "string",
  "contactNumber": "string",
  "email": "string"
}
```

### `GET /api/patients`

Returns a list of all patients.

**Response:**

```json
[
  {
    "id": "number",
    "firstName": "string",
    "lastName": "string",
    "dob": "yyyy-mm-dd",
    "gender": "string",
    "contactNumber": "string",
    "email": "string"
  }
]
```

## Appointments

### `GET /api/appointments`

Returns a list of all appointments.

**Response:**

```json
[
  {
    "id": "number",
    "doctorId": "number",
    "patientId": "number",
    "startTime": "yyyy-mm-ddTHH:mm:ss",
    "endTime": "yyyy-mm-ddTHH:mm:ss",
    "status": "string",
    "notes": "string"
  }
]
```
