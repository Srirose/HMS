import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions, EventClickArg, DateSelectArg, EventDropArg } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { AppointmentService, Appointment } from '../../services/appointment.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-appointment-calendar',
  standalone: true,
  imports: [CommonModule, FullCalendarModule, ReactiveFormsModule],
  templateUrl: './appointment-calendar.component.html',
  styleUrls: ['./appointment-calendar.component.scss']
})
export class AppointmentCalendarComponent implements OnInit {
  @ViewChild('bookingModal') bookingModal!: ElementRef;
  
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay'
    },
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    select: this.handleDateSelect.bind(this),
    eventClick: this.handleEventClick.bind(this),
    eventDrop: this.handleEventDrop.bind(this),
    events: []
  };

  bookingForm: FormGroup;
  doctors: any[] = [];
  patients: any[] = [];
  isModalOpen = false;
  submitError = '';

  constructor(
    private appointmentService: AppointmentService,
    private fb: FormBuilder,
    private http: HttpClient
  ) {
    this.bookingForm = this.fb.group({
      patientId: ['', Validators.required],
      doctorId: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.loadAppointments();
    this.loadDoctors();
    this.loadPatients();
  }

  loadAppointments(): void {
    this.appointmentService.getAppointments().subscribe({
      next: (appointments: Appointment[]) => {
        this.calendarOptions.events = appointments.map(appt => ({
          id: appt.id.toString(),
          title: `Patient #${appt.patientId} - Dr. #${appt.doctorId}`,
          start: appt.startTime,
          end: appt.endTime,
          extendedProps: appt,
          backgroundColor: appt.status === 'CONFIRMED' ? '#28a745' : '#ffc107'
        }));
      },
      error: (err) => console.error('Failed to load appointments', err)
    });
  }

  loadDoctors(): void {
    this.http.get<any[]>('http://localhost:8081/api/doctors').subscribe(docs => this.doctors = docs);
  }

  loadPatients(): void {
    this.http.get<any[]>('http://localhost:8081/api/patients').subscribe(pts => this.patients = pts);
  }

  handleDateSelect(selectInfo: DateSelectArg) {
    this.bookingForm.reset({
      startTime: selectInfo.startStr.substring(0, 16),
      endTime: selectInfo.endStr.substring(0, 16)
    });
    this.isModalOpen = true;
    this.submitError = '';
  }

  handleEventClick(clickInfo: EventClickArg) {
    if (confirm(`Are you sure you want to delete the appointment '${clickInfo.event.title}'?`)) {
      // Deletion logic would go here if needed
      // For now, we just log it
      console.log('Delete appointment:', clickInfo.event.id);
    }
  }

  handleEventDrop(dropInfo: EventDropArg) {
    const appt = dropInfo.event.extendedProps as Appointment;
    const updatedAppt: Appointment = {
      ...appt,
      startTime: dropInfo.event.startStr,
      endTime: dropInfo.event.endStr || dropInfo.event.startStr
    };

    this.appointmentService.updateAppointment(updatedAppt).subscribe({
      next: () => console.log('Appointment updated successfully'),
      error: (err) => {
        console.error('Failed to update appointment', err);
        dropInfo.revert();
        alert(err.error || 'Conflict detected. Could not move appointment.');
      }
    });
  }

  closeModal() {
    this.isModalOpen = false;
  }

  onSubmitBooking() {
    if (this.bookingForm.invalid) return;

    this.appointmentService.createAppointment(this.bookingForm.value).subscribe({
      next: () => {
        this.loadAppointments();
        this.closeModal();
      },
      error: (err) => {
        this.submitError = err.error || 'Failed to book appointment. Check for conflicts.';
      }
    });
  }
}
