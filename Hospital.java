//Sinan Yaşbek
//Fulya Koru
//Arda Oğulcan Üzer
//Yağmur Zeynep Toprak
//Patient Monitoring System
import java.util.*;

public class Hospital {
    public static void main(String[] args) {
        // Create physician and nurse objects
        RadiologyAdapter radiologyAdapter = RadiologyDepartment.getInstance();
        LabAdapter labAdapter = LabDepartment.getInstance();
        Nurse nurse = new Nurse("Nurse Yağmur Zeynep Toprak", radiologyAdapter, labAdapter);
        Physician physician = new Physician("Dr. Arda Oğulcan Üzer", radiologyAdapter, labAdapter);


        // Create patients by using the factory
        List<Patient> patients = new ArrayList<>();

        CardiologicalPatient cardiologicalPatient1 = (CardiologicalPatient) PatientFactory.createPatient("cardiological", "Fulya Koru");
        CardiologicalPatient cardiologicalPatient2 = (CardiologicalPatient) PatientFactory.createPatient("cardiological", "Sinan Yaşbek");
        GastroenterologicalPatient gastroenterologicalPatient = (GastroenterologicalPatient) PatientFactory.createPatient("gastroenterological", "Belinay Koru");

        // Register nurse and physician as observers for patients
        cardiologicalPatient1.addObserver(nurse);
        gastroenterologicalPatient.addObserver(nurse);

        cardiologicalPatient2.addObserver(physician);
        gastroenterologicalPatient.addObserver(physician);

        patients.add(cardiologicalPatient1);
        patients.add(cardiologicalPatient2);
        patients.add(gastroenterologicalPatient);

        // Simulate dangerous blood pressure for a patient
        // Assuming 180 is a dangerous blood pressure level

        cardiologicalPatient1.setBloodPressure(120);
        cardiologicalPatient2.setBloodPressure(181);
        gastroenterologicalPatient.setBloodPressure(200);

        // Physician performs morning consultation and orders procedures
        physician.performMorningConsultation(patients);

        // Nurse performs afternoon procedures
        nurse.performAfternoonProcedures(patients);

    }
}

// Abstract Class to represent a patient
abstract class Patient extends Observable {
    private String name;
    protected boolean bloodPressureNotified;
    private boolean isMorningConsultationPerformed;

    private int bloodPressure;

    public Patient(String name) {
        this.name = name;
        this.bloodPressureNotified = false;
        isMorningConsultationPerformed = false;
    }

    public String getName() {
        return name;
    }

    public abstract void performConsultation();

    public abstract Iterator<String> getProcedureIterator();

    public void setBloodPressure(int bloodPressure) {
        if (bloodPressure > 180 && !isBloodPressureNotified()) {
            this.bloodPressure = bloodPressure;
            bloodPressureNotified = true;
            setChanged();
            notifyObservers();
        }
    }

    public int getBloodPressure() {
        return bloodPressure;
    }

    public boolean isBloodPressureNotified() {
        return bloodPressureNotified;
    }


    public void performMorningBloodPressureCheck() {
    }
}



// Concrete class to represent a cardiological patient
class CardiologicalPatient extends Patient {
    private int bloodPressure;

    public CardiologicalPatient(String name) {
        super(name);
    }

    @Override
    public void performConsultation() {
        System.out.println("Cardiological consultation for patient: " + getName());
    }

    @Override
    public Iterator<String> getProcedureIterator() {
        List<String> procedures = new ArrayList<>();
        procedures.add("Virtual angiography");
        procedures.add("Hema blood test");
        return procedures.iterator();
    }

    @Override
    public void setBloodPressure(int bloodPressure) {
        if (bloodPressure > 180 && !bloodPressureNotified) {
            this.bloodPressure = bloodPressure;
            bloodPressureNotified = true;
            setChanged();
            notifyObservers();
        } else {
            this.bloodPressure = bloodPressure;
        }
    }


    private void notifyNurse() {
        setChanged();
        notifyObservers(this);
    }


    public int getBloodPressure() {
        return bloodPressure;
    }

}

// Concrete Class to represent a gastroenterological patient
class GastroenterologicalPatient extends Patient {
    private int bloodPressure;
    public GastroenterologicalPatient(String name) {
        super(name);
    }

    @Override
    public void performConsultation() {
        System.out.println("Gastroenterological consultation for patient: " + getName());
    }

    @Override
    public Iterator<String> getProcedureIterator() {
        List<String> procedures = new ArrayList<>();
        procedures.add("MRI");
        procedures.add("Gastro blood test");
        return procedures.iterator();
    }



    @Override
    public void setBloodPressure(int bloodPressure) {
        if (bloodPressure > 180 && !bloodPressureNotified) {
            this.bloodPressure = bloodPressure;
            bloodPressureNotified = true;
            setChanged();
            notifyObservers();
        } else {
            this.bloodPressure = bloodPressure;
        }
    }


    private void notifyNurse() {
        setChanged();
        notifyObservers(this);
    }


    public int getBloodPressure() {
        return bloodPressure;
    }
}

// Singleton Radiology department class implementing RadiologyAdapter
class RadiologyDepartment implements RadiologyAdapter {
    private static RadiologyDepartment instance;
    // Prevent direct instantiation
    private RadiologyDepartment() {

    }

    public static RadiologyDepartment getInstance() {
        if (instance == null) {
            instance = new RadiologyDepartment();
        }
        return instance;
    }

    @Override
    public void performRadiologyProcedure(String procedure, String patientName) {
        System.out.println("Nurse will take " + procedure + " the test for patient: " + patientName + " (Radiology Department)");
        System.out.println(" ");
    }

    public void performRadiologyProcedure2(String procedure, String patientName) {
        System.out.println("Nurse performed " + procedure + " the test for patient: " + patientName + " (Radiology Department)");
        System.out.println(" ");
    }
}

// Singleton Lab Department Class implementing LabAdapter
class LabDepartment implements LabAdapter {
    private static LabDepartment instance;


    //Prevent direct instantiation
    private LabDepartment() {
    }

    public static LabDepartment getInstance() {
        if (instance == null) {
            instance = new LabDepartment();
        }
        return instance;
    }

    @Override
    public void performLabProcedure(String procedure, String patientName) {
        System.out.println("Nurse will take " + procedure + " the test for patient: " + patientName + " (Lab Department)");
        System.out.println(" ");
    }

    public void performLabProcedure2(String procedure, String patientName) {
        System.out.println("Nurse performed " + procedure + " the test for patient: " + patientName + " (Lab Department)");
        System.out.println(" ");
    }


}

// Adapter interface for Radiology Procedures
interface RadiologyAdapter {
    void performRadiologyProcedure(String procedure, String patientName);
    void performRadiologyProcedure2(String procedure, String patientName);

}

// Adapter interface for Lab Procedures
interface LabAdapter {
    void performLabProcedure(String procedure, String patientName);
    void performLabProcedure2(String procedure, String patientName);
}

class Nurse implements Observer {
    private String name;
    private RadiologyAdapter radiologyAdapter;
    private LabAdapter labAdapter;

    public Nurse(String name, RadiologyAdapter radiologyAdapter, LabAdapter labAdapter) {
        this.name = name;
        this.radiologyAdapter = radiologyAdapter;
        this.labAdapter = labAdapter;
    }

    public void performAfternoonProcedures(List<Patient> patients) {
        System.out.println(" ");
        System.out.println("----------PERFORMING AFTERNOON PROCEDURES FOR PATIENTS BY NURSE---------");
        System.out.println(" ");
        for (Patient patient : patients) {
            patient.performConsultation();

            Iterator<String> procedureIterator = patient.getProcedureIterator();
            while (procedureIterator.hasNext()) {
                String procedure = procedureIterator.next();
                if (procedure.equals("Virtual angiography")) {
                    System.out.println(" ");
                    radiologyAdapter.performRadiologyProcedure(procedure, patient.getName());
                    radiologyAdapter.performRadiologyProcedure2(procedure,patient.getName());
                } else if (procedure.equals("MRI")) {
                    radiologyAdapter.performRadiologyProcedure(procedure, patient.getName());
                    radiologyAdapter.performRadiologyProcedure2(procedure,patient.getName());
                } else if (procedure.equals("Hema blood test")) {
                    labAdapter.performLabProcedure(procedure, patient.getName());
                    measureBloodPressure(patient);
                    labAdapter.performLabProcedure2(procedure, patient.getName());
                } else if (procedure.equals("Gastro blood test")) {
                    labAdapter.performLabProcedure(procedure, patient.getName());
                    measureBloodPressure(patient);
                    labAdapter.performLabProcedure2(procedure, patient.getName());
                }
            }
        }
        System.out.println(" ");
        System.out.println("ALL AFTERNOON PROCEDURES FOR PATIENTS PERFORMED BY NURSES!");
        System.out.println("****************GET WELL SOON*****************");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Patient) {
            Patient patient = (Patient) o;
            if (patient.getBloodPressure() > 180) {
                notifyNurse(patient);
                notifyPhysician(patient);
            }
        }
    }

    private void measureBloodPressure(Patient patient) {
        int bloodPressure = patient.getBloodPressure();
        System.out.println("Blood pressure for patient " + patient.getName() + " is: " + bloodPressure);
        if (bloodPressure > 180) {
            bloodPressure = measureBloodPressureManually(patient);
            System.out.println("Manually measured blood pressure for patient " + patient.getName() + " is: " + bloodPressure);
        }
    }


    private int measureBloodPressureManually(Patient patient) {
        notifyNurse2(patient);
        System.out.println("Nurse will measure the blood pressure manually because it's critical.");
        Scanner scanner = new Scanner(System.in);
        notifyPhysician2(patient);

        System.out.print("Enter the blood pressure for patient " + patient.getName() + ": ");
        int bloodPressure = scanner.nextInt();

        if (bloodPressure >= 180) {
            System.out.println("Blood pressure is still high.");
            System.out.println("Dear patient: " + patient.getName() + ". Your blood pressure is a bit high. Do not worry. Our nurse will take care of you.");
        } else if (bloodPressure < 180){
            System.out.println("Manually measured blood pressure is within the safe range.");
            System.out.println("Dear patient: " + patient.getName() + ". Your blood pressure is normal. Have a good day.");
        }
        return bloodPressure;
    }

    // Notify nurses if there is a patient with high blood pressure
    public void notifyNurse(Patient patient) {
        System.out.println(name + " will take care of patient: " + patient.getName() + " and prescribe certain procedure in the afternoon.");
    }

    public void notifyNurse2(Patient patient) {
        System.out.println(name + " has been notified for the patient " + patient.getName());
    }

    public void notifyPhysician(Patient patient) {
        System.out.println("Physician will be notified about high blood pressure for patient: " + patient.getName() + " if require.");
    }

    public void notifyPhysician2(Patient patient) {
        System.out.println("Physician will be notified about high blood pressure for patient: " + patient.getName());
    }

}

// Physician class
class Physician implements Observer {
    private String name;
    private RadiologyAdapter radiologyAdapter;
    private LabAdapter labAdapter;

    public Physician(String name, RadiologyAdapter radiologyAdapter, LabAdapter labAdapter) {
        this.name = name;
        this.radiologyAdapter = radiologyAdapter;
        this.labAdapter = labAdapter;
    }

    public void performMorningConsultation(List<Patient> patients) {
        System.out.println(" ");
        System.out.printf("  ");
        System.out.println("--------PERFORMING MORNING PROCEDURES FOR PATIENTS BY PHYSICIAN---------");
        System.out.println(" ");
        for (Patient patient : patients) {
            patient.performConsultation();
            patient.performMorningBloodPressureCheck();
            Iterator<String> procedureIterator = patient.getProcedureIterator();
            while (procedureIterator.hasNext()) {
                String procedure = procedureIterator.next();
                if (procedure.equals("Virtual angiography")) {
                    radiologyAdapter.performRadiologyProcedure(procedure, patient.getName());
                    System.out.println(" ");
                } else if (procedure.equals("MRI")) {
                    radiologyAdapter.performRadiologyProcedure(procedure, patient.getName());
                    System.out.println(" ");
                } else if (procedure.equals("Hema blood test")) {
                    labAdapter.performLabProcedure(procedure, patient.getName());
                    System.out.println(" ");
                } else if (procedure.equals("Gastro blood test")) {
                    labAdapter.performLabProcedure(procedure, patient.getName());
                    System.out.println(" ");
                }
            }
        }

        System.out.println(" ");
        System.out.println("ALL MORNING CONSULTATION FOR PATIENTS PERFORMED BY PHYSICIANS!");

    }

    // Perform necessary actions based on the blood pressure notification
    public void update(Observable o, Object arg) {
        if (o instanceof Patient) {
            Patient patient = (Patient) o;
            if (patient.getBloodPressure() > 180) {
                System.out.println("Physician: " + name + " will perform a consultation for patient: " + patient.getName());
            }
        }
    }

}

// Factory class for creating patients
class PatientFactory {
    public static Patient createPatient(String type, String name) {
        if (type.equalsIgnoreCase("cardiological")) {
            return new CardiologicalPatient(name);
        } else if (type.equalsIgnoreCase("gastroenterological")) {
            return new GastroenterologicalPatient(name);
        } else {
            throw new IllegalArgumentException("Invalid patient type: " + type);
        }
    }
}