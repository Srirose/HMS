import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-patient-admission',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './patient-admission.component.html',
  styleUrl: './patient-admission.component.scss'
})
export class PatientAdmissionComponent implements OnInit {
  admissionForm: FormGroup;
  isSubmitting = false;
  submitSuccess = false;
  submitError = '';

  departments = ['Cardiology', 'Neurology', 'Orthopedics', 'Pediatrics', 'General Medicine'];
  doctors: Doctor[] = [];
  roomTypes = ['General Ward', 'Semi-Private', 'Private', 'ICU'];
  bloodGroups = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.admissionForm = this.fb.group({
      personalInfo: this.fb.group({
        firstName: ['', [Validators.required, Validators.minLength(2)]],
        lastName: ['', [Validators.required, Validators.minLength(2)]],
        dob: ['', Validators.required],
        gender: ['', Validators.required],
        bloodGroup: ['', Validators.required],
        maritalStatus: [''],
        email: ['', [Validators.email]],
        phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
        address: ['', Validators.required],
        city: ['', Validators.required],
        state: ['', Validators.required],
        zip: ['', [Validators.required, Validators.pattern('^[0-9]{5,6}$')]],
        nationality: [''],
        occupation: [''],
        idProofType: ['', Validators.required],
        idProofNumber: ['', Validators.required]
      }),
      emergencyContact: this.fb.group({
        name: ['', Validators.required],
        relation: ['', Validators.required],
        phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
        email: ['', Validators.email],
        address: ['']
      }),
      admissionDetails: this.fb.group({
        admissionDate: [new Date().toISOString().substring(0, 10), Validators.required],
        admissionType: ['Emergency', Validators.required],
        department: ['', Validators.required],
        assignedDoctor: ['', Validators.required],
        roomType: ['', Validators.required],
        bedNumber: ['', Validators.required],
        reasonForAdmission: ['', [Validators.required, Validators.minLength(10)]],
        insuranceProvider: [''],
        policyNumber: [''],
        expectedDischargeDate: ['']
      }),
      vitals: this.fb.group({
        height: ['', [Validators.min(0), Validators.max(300)]],
        weight: ['', [Validators.min(0), Validators.max(500)]],
        bmi: [{ value: '', disabled: true }],
        bpSystolic: ['', [Validators.min(50), Validators.max(250)]],
        bpDiastolic: ['', [Validators.min(30), Validators.max(150)]],
        temperature: ['', [Validators.min(30), Validators.max(45)]],
        pulseRate: ['', [Validators.min(30), Validators.max(200)]],
        respRate: ['', [Validators.min(10), Validators.max(60)]],
        o2Saturation: ['', [Validators.min(50), Validators.max(100)]],
        painLevel: ['0', [Validators.min(0), Validators.max(10)]]
      }),
      medicalHistory: this.fb.group({
        allergies: this.fb.array([]),
        currentMedications: this.fb.array([]),
        pastSurgeries: this.fb.array([]),
        chronicConditions: this.fb.array([]),
        smokingStatus: ['Never'],
        alcoholConsumption: ['Never'],
        diet: ['Regular'],
        exerciseFrequency: ['None'],
        notes: ['']
      })
    });
  }

  ngOnInit(): void {
    this.loadDoctors();
    this.admissionForm.get('vitals')?.valueChanges.subscribe(values => {
      if (values.height && values.weight) {
        const heightM = values.height / 100;
        const bmi = (values.weight / (heightM * heightM)).toFixed(1);
        this.admissionForm.get('vitals.bmi')?.setValue(bmi, { emitEvent: false });
      }
    });
  }
  private loadDoctors(): void {
    const tenant = localStorage.getItem('tenant') || 'hospital_a';
    const headers = new HttpHeaders().set('X-Tenant-ID', tenant);

    this.http.get<Doctor[]>('http://localhost:8081/api/doctors', { headers })
      .subscribe({
        next: (docs) => this.doctors = docs,
        error: (err) => {
          console.error('Failed to load doctors', err);
          this.doctors = [];
        }
      });
  }

  isFieldInvalid(path: string): boolean {
    const control = this.admissionForm.get(path);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  get allergies() { return this.admissionForm.get('medicalHistory.allergies') as FormArray; }
  get currentMedications() { return this.admissionForm.get('medicalHistory.currentMedications') as FormArray; }
  get pastSurgeries() { return this.admissionForm.get('medicalHistory.pastSurgeries') as FormArray; }

  addAllergy() { this.allergies.push(this.fb.control('')); }
  removeAllergy(index: number) { this.allergies.removeAt(index); }

  addMedication() { this.currentMedications.push(this.fb.control('')); }
  removeMedication(index: number) { this.currentMedications.removeAt(index); }

  addSurgery() { this.pastSurgeries.push(this.fb.control('')); }
  removeSurgery(index: number) { this.pastSurgeries.removeAt(index); }

  onSubmit() {
    if (this.admissionForm.invalid) {
      this.admissionForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    const formValue = this.admissionForm.getRawValue();

    const payload = {
      firstName: formValue.personalInfo.firstName,
      lastName: formValue.personalInfo.lastName,
      dob: formValue.personalInfo.dob,
      gender: formValue.personalInfo.gender,
      contactNumber: formValue.personalInfo.phone,
      email: formValue.personalInfo.email,
      assignedDoctorUsername: formValue.admissionDetails.doctor,
      admissionDetails: formValue.admissionDetails,
      medicalHistory: {
        ...formValue.medicalHistory,
        vitals: formValue.vitals,
        emergencyContact: formValue.emergencyContact,
        personalInfoExtra: {
          bloodGroup: formValue.personalInfo.bloodGroup,
          address: formValue.personalInfo.address
        }
      }
    };

    const tenant = localStorage.getItem('tenant') || 'hospital_a';
    const token = localStorage.getItem('token');

    if (!token) {
      this.submitError = 'Authentication token is missing. Please log in again.';
      window.scrollTo(0, 0);
      return;
    }

    console.log('Submitting Admission Form:', { tenant, tokenExists: !!token, role: localStorage.getItem('role') });

    this.http.post('http://localhost:8081/api/patients/admission', payload).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.submitSuccess = true;
        this.submitError = '';
        this.admissionForm.reset();
        window.scrollTo(0, 0);
      },
      error: (err) => {
        this.isSubmitting = false;
        this.submitError = `Failed to submit: ${err.status} ${err.statusText}. Check console for details.`;
        console.error('Admission Error:', err);
      }
    });
  }
}

interface Doctor {
  id: number;
  username: string;
  fullName: string;
  specialization: string;
}
