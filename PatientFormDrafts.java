import java.time.LocalDate;

public class PatientFormDrafts {
        // Personal
        String patientName, address, phoneNumber, gender, email, emergencyContact, relationship, emergencyPhone,
                        guardianName, guardianPhone;

        int year, month, day;

        public LocalDate getDOB() {
                if (year > 0 && month > 0 && day > 0) {
                        return LocalDate.of(year, month, day);
                }
                return null;
        }

        // Medical
        String reason, pastProblems, medications, allergies;

        // Insurance
        String insuranceProvider, insuranceID, name, insuranceAddress, insurancePhone, billingAddress, paymentMethod,
                        cardNumber;

}