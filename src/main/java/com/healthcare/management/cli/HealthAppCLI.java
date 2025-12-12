package com.healthcare.management.cli;

import com.healthcare.management.entity.*;
import com.healthcare.management.notification.EmailService;
import com.healthcare.management.notification.NotificationService;
import com.healthcare.management.notification.SMSService;
import com.healthcare.management.service.AppointmentService;
import com.healthcare.management.service.DoctorService;
import com.healthcare.management.service.PatientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class HealthAppCLI implements CommandLineRunner {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final EmailService emailService;
    private final SMSService smsService;
    private final Scanner scanner;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public HealthAppCLI(PatientService patientService,
                        DoctorService doctorService,
                        AppointmentService appointmentService,
                        EmailService emailService,
                        SMSService smsService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.emailService = emailService;
        this.smsService = smsService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) {
        System.out.println("\n==========================================");
        System.out.println("   SISTEMI I MENAXHIMIT TE SHENDETIT");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice.toUpperCase()) {
                case "P":
                    patientMenu();
                    break;
                case "D":
                    doctorMenu();
                    break;
                case "A":
                    appointmentMenu();
                    break;
                case "N":
                    notificationMenu();
                    break;
                case "X":
                    System.out.println("\nDuke u mbyllur...");
                    running = false;
                    break;
                default:
                    System.out.println("\nZgjedhje e pavlefshme. Provoni përsëri.");
            }
        }
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println("\n===== MENU KRYESORE =====");
        System.out.println("P - Veprime me Pacientët");
        System.out.println("D - Veprime me Doktorët");
        System.out.println("A - Veprime me Terminet");
        System.out.println("N - Dërgo Njoftim");
        System.out.println("X - Dil");
        System.out.print("\nZgjidhni opsionin: ");
    }

    private void patientMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== MENU E PACIENTËVE =====");
            System.out.println("1 - Regjistro pacient të ri");
            System.out.println("2 - Shiko të gjithë pacientët");
            System.out.println("3 - Kërko pacient sipas emrit");
            System.out.println("4 - Modifiko pacient");
            System.out.println("5 - Fshi pacient");
            System.out.println("6 - Kthehu në menu kryesore");
            System.out.print("\nZgjidhni: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    registerPatient();
                    break;
                case "2":
                    listAllPatients();
                    break;
                case "3":
                    searchPatientByName();
                    break;
                case "4":
                    updatePatient();
                    break;
                case "5":
                    deletePatient();
                    break;
                case "6":
                    back = true;
                    break;
                default:
                    System.out.println("Zgjedhje e pavlefshme!");
            }
        }
    }

    private void registerPatient() {
        System.out.println("\n--- REGJISTRIM I PACIENTIT TË RI ---");

        System.out.print("Emri: ");
        String name = scanner.nextLine().trim();

        System.out.print("Telefoni: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Mosha: ");
        int age = Integer.parseInt(scanner.nextLine().trim());

        try {
            Patient patient = patientService.register(name, phone, email, age);
            System.out.println("\n✅ Pacienti u regjistrua me sukses!");
            System.out.println("ID: " + patient.getId());
            System.out.println(patient.getDetails());
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void listAllPatients() {
        System.out.println("\n--- LISTA E PACIENTËVE ---");
        try {
            List<Patient> patients = patientService.getAll();
            if (patients.isEmpty()) {
                System.out.println("Nuk ka pacientë të regjistruar.");
            } else {
                patients.forEach(p -> {
                    System.out.println("ID: " + p.getId() + " - " + p.getDetails());
                });
                System.out.println("Total: " + patients.size() + " pacientë");
            }
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void searchPatientByName() {
        System.out.print("\nShkruani emrin për kërkim: ");
        String name = scanner.nextLine().trim();

        try {
            List<Patient> patients = patientService.searchByName(name);
            if (patients.isEmpty()) {
                System.out.println("Nuk u gjet asnjë pacient me këtë emër.");
            } else {
                System.out.println("\nRezultatet e kërkimit:");
                patients.forEach(p -> {
                    System.out.println("ID: " + p.getId() + " - " + p.getDetails());
                });
            }
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void updatePatient() {
        System.out.print("\nShkruani ID-në e pacientit për modifikim: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        try {
            Patient current = patientService.getById(id);
            System.out.println("Pacienti aktual: " + current.getDetails());

            System.out.println("\nLëreni bosh për të mos ndryshuar");
            System.out.print("Emri i ri: ");
            String name = scanner.nextLine();

            System.out.print("Telefoni i ri: ");
            String phone = scanner.nextLine();

            System.out.print("Email i ri: ");
            String email = scanner.nextLine();

            System.out.print("Mosha e re (shkruani 0 për të mos ndryshuar): ");
            String ageStr = scanner.nextLine();
            Integer age = ageStr.isBlank() || Integer.parseInt(ageStr) == 0 ? null : Integer.parseInt(ageStr);

            Patient updated = patientService.update(id,
                    name.isBlank() ? null : name,
                    phone.isBlank() ? null : phone,
                    email.isBlank() ? null : email,
                    age);

            System.out.println("\n✅ Pacienti u përditësua me sukses!");
            System.out.println(updated.getDetails());
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void deletePatient() {
        System.out.print("\nShkruani ID-në e pacientit për fshirje: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Jeni i sigurt? (P/J): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("P")) {
            try {
                patientService.delete(id);
                System.out.println("✅ Pacienti u fshi me sukses!");
            } catch (Exception e) {
                System.out.println("❌ Gabim: " + e.getMessage());
            }
        } else {
            System.out.println("Operacioni u anulua.");
        }
    }

    private void doctorMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== MENU E DOKTORËVE =====");
            System.out.println("1 - Regjistro doktor të ri");
            System.out.println("2 - Shiko të gjithë doktorët");
            System.out.println("3 - Kërko doktor sipas specialitetit");
            System.out.println("4 - Modifiko doktor");
            System.out.println("5 - Fshi doktor");
            System.out.println("6 - Kthehu në menu kryesore");
            System.out.print("\nZgjidhni: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createDoctor();
                    break;
                case "2":
                    listAllDoctors();
                    break;
                case "3":
                    searchDoctorBySpecialty();
                    break;
                case "4":
                    updateDoctor();
                    break;
                case "5":
                    deleteDoctor();
                    break;
                case "6":
                    back = true;
                    break;
                default:
                    System.out.println("Zgjedhje e pavlefshme!");
            }
        }
    }

    private void createDoctor() {
        System.out.println("\n--- REGJISTRIM I DOKTORIT TË RI ---");

        System.out.print("Emri: ");
        String name = scanner.nextLine().trim();

        System.out.print("Telefoni: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Specialiteti: ");
        String specialty = scanner.nextLine().trim();

        try {
            Doctor doctor = doctorService.create(name, phone, email, specialty);
            System.out.println("\n✅ Doktori u regjistrua me sukses!");
            System.out.println("ID: " + doctor.getId());
            System.out.println(doctor.getDetails());
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void listAllDoctors() {
        System.out.println("\n--- LISTA E DOKTORËVE ---");
        try {
            List<Doctor> doctors = doctorService.getAll();
            if (doctors.isEmpty()) {
                System.out.println("Nuk ka doktorë të regjistruar.");
            } else {
                doctors.forEach(d -> {
                    System.out.println("ID: " + d.getId() + " - " + d.getDetails());
                });
                System.out.println("Total: " + doctors.size() + " doktorë");
            }
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void searchDoctorBySpecialty() {
        System.out.print("\nShkruani specialitetin për kërkim: ");
        String specialty = scanner.nextLine().trim();

        try {
            List<Doctor> doctors = doctorService.getBySpecialty(specialty);
            if (doctors.isEmpty()) {
                System.out.println("Nuk u gjet asnjë doktor me këtë specialitet.");
            } else {
                System.out.println("\nRezultatet e kërkimit:");
                doctors.forEach(d -> {
                    System.out.println("ID: " + d.getId() + " - " + d.getDetails());
                });
            }
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void updateDoctor() {
        System.out.print("\nShkruani ID-në e doktorit për modifikim: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        try {
            Doctor current = doctorService.getById(id);
            System.out.println("Doktori aktual: " + current.getDetails());

            System.out.println("\nLëreni bosh për të mos ndryshuar");
            System.out.print("Emri i ri: ");
            String name = scanner.nextLine();

            System.out.print("Telefoni i ri: ");
            String phone = scanner.nextLine();

            System.out.print("Email i ri: ");
            String email = scanner.nextLine();

            System.out.print("Specialiteti i ri: ");
            String specialty = scanner.nextLine();

            Doctor updated = doctorService.update(id,
                    name.isBlank() ? null : name,
                    phone.isBlank() ? null : phone,
                    email.isBlank() ? null : email,
                    specialty.isBlank() ? null : specialty);

            System.out.println("\n✅ Doktori u përditësua me sukses!");
            System.out.println(updated.getDetails());
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void deleteDoctor() {
        System.out.print("\nShkruani ID-në e doktorit për fshirje: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Jeni i sigurt? (P/J): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("P")) {
            try {
                doctorService.delete(id);
                System.out.println("✅ Doktori u fshi me sukses!");
            } catch (Exception e) {
                System.out.println("❌ Gabim: " + e.getMessage());
            }
        } else {
            System.out.println("Operacioni u anulua.");
        }
    }

    private void appointmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== MENU E TERMINEVE =====");
            System.out.println("1 - Krijo termin të ri");
            System.out.println("2 - Shiko terminet sipas statusit");
            System.out.println("3 - Shiko terminet e një pacienti");
            System.out.println("4 - Përfundo termin (shto raport)");
            System.out.println("5 - Anulo termin");
            System.out.println("6 - Kthehu në menu kryesore");
            System.out.print("\nZgjidhni: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    scheduleAppointment();
                    break;
                case "2":
                    listAppointmentsByStatus();
                    break;
                case "3":
                    listAppointmentsByPatient();
                    break;
                case "4":
                    completeAppointment();
                    break;
                case "5":
                    cancelAppointment();
                    break;
                case "6":
                    back = true;
                    break;
                default:
                    System.out.println("Zgjedhje e pavlefshme!");
            }
        }
    }

    private void scheduleAppointment() {
        System.out.println("\n--- KRIJIM I TERMINIT TË RI ---");

        System.out.print("ID e pacientit: ");
        int patientId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("ID e doktorit: ");
        int doctorId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Data (dd/MM/yyyy): ");
        String dateStr = scanner.nextLine().trim();

        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);

            Appointment appointment = appointmentService.schedule(patientId, doctorId, date);
            System.out.println("\n✅ Termini u krijua me sukses!");
            printAppointmentDetails(appointment);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Format i gabuar i datës. Përdorni dd/MM/yyyy");
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void listAppointmentsByStatus() {
        System.out.println("\n--- TERMINET SIPAS STATUSIT ---");
        System.out.println("1 - Planifikuar");
        System.out.println("2 - Përfunduar");
        System.out.println("3 - Anuluar");
        System.out.print("Zgjidhni statusin: ");

        int choice = Integer.parseInt(scanner.nextLine().trim());
        AppointmentStatus status;

        switch (choice) {
            case 1: status = AppointmentStatus.SCHEDULED; break;
            case 2: status = AppointmentStatus.COMPLETED; break;
            case 3: status = AppointmentStatus.CANCELLED; break;
            default:
                System.out.println("Zgjedhje e pavlefshme!");
                return;
        }

        try {
            List<Appointment> appointments = appointmentService.getByStatus(status);
            if (appointments.isEmpty()) {
                System.out.println("Nuk ka termina me këtë status.");
            } else {
                appointments.forEach(this::printAppointmentDetails);
            }
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void listAppointmentsByPatient() {
        System.out.print("\nShkruani ID-në e pacientit: ");
        int patientId = Integer.parseInt(scanner.nextLine().trim());

        try {
            List<Appointment> appointments = appointmentService.getByPatient(patientId);
            if (appointments.isEmpty()) {
                System.out.println("Pacienti nuk ka termina.");
            } else {
                appointments.forEach(this::printAppointmentDetails);
            }
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void completeAppointment() {
        System.out.print("\nShkruani ID-në e terminit për të përfunduar: ");
        int appointmentId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Raporti i konsultimit: ");
        String report = scanner.nextLine().trim();

        try {
            Appointment appointment = appointmentService.complete(appointmentId, report);
            System.out.println("\n✅ Termini u përfundua me sukses!");
            printAppointmentDetails(appointment);
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void cancelAppointment() {
        System.out.print("\nShkruani ID-në e terminit për të anuluar: ");
        int appointmentId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Jeni i sigurt? (P/J): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("P")) {
            try {
                Appointment appointment = appointmentService.cancel(appointmentId);
                System.out.println("✅ Termini u anulua me sukses!");
                printAppointmentDetails(appointment);
            } catch (Exception e) {
                System.out.println("❌ Gabim: " + e.getMessage());
            }
        } else {
            System.out.println("Operacioni u anulua.");
        }
    }

    private void notificationMenu() {
        System.out.println("\n===== DËRGIMI I NJOFTIMEVE =====");
        System.out.println("1 - Dërgo njoftim me Email");
        System.out.println("2 - Dërgo njoftim me SMS");
        System.out.print("Zgjidhni: ");

        String choice = scanner.nextLine().trim();

        System.out.print("Mesazhi: ");
        String message = scanner.nextLine().trim();

        System.out.print("Marrësi (email për SMS, numër për SMS): ");
        String recipient = scanner.nextLine().trim();

        try {
            if (choice.equals("1")) {
                emailService.setRecipientEmail(recipient);
                emailService.sendNotification(message);
                System.out.println("✅ Email-i u dërgua!");
            } else if (choice.equals("2")) {
                smsService.setRecipientPhone(recipient);
                smsService.sendNotification(message);
                System.out.println("✅ SMS-u u dërgua!");
            } else {
                System.out.println("Zgjedhje e pavlefshme!");
            }
        } catch (Exception e) {
            System.out.println("❌ Gabim: " + e.getMessage());
        }
    }

    private void printAppointmentDetails(Appointment appointment) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("ID: " + appointment.getId());
        System.out.println("Pacienti: " + appointment.getPatient().getName());
        System.out.println("Doktori: " + appointment.getDoctor().getName());
        System.out.println("Specialiteti: " + appointment.getDoctor().getSpeciality());
        System.out.println("Data: " + appointment.getDate().format(DATE_FORMATTER));
        System.out.println("Statusi: " + appointment.getStatus().getAlbanianLabel());

        if (appointment.getReport() != null && !appointment.getReport().isBlank()) {
            System.out.println("Raporti: " + appointment.getReport());
        }
        System.out.println("════════════════════════════════════════");
    }
}