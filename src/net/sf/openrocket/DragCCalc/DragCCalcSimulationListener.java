package net.sf.openrocket.DragCCalc;

import net.sf.openrocket.aerodynamics.AerodynamicForces;
import net.sf.openrocket.simulation.SimulationStatus;
import net.sf.openrocket.simulation.exception.SimulationException;
import net.sf.openrocket.simulation.listeners.AbstractSimulationListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class DragCCalcSimulationListener extends AbstractSimulationListener{
    // return a double array of the drag coefficient
    private final String csvFilePath;
    double MACH_SCALE = 100;
    double ALPHA_SCALE = 0.5;
    ArrayList<ArrayList<Double>> data = new ArrayList<>(3);

    public DragCCalcSimulationListener(String csvFilePath) {
        super();
        this.csvFilePath = csvFilePath;
        try {
            readDataCsv();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void readDataCsv() throws FileNotFoundException {
        // implement a function that reads 2 specific columns from a csv file and returns a hashmap for the data
//        String user = System.getProperty("user.name");
//        File file = new File("C:\\Users\\" + user + "\\Documents\\DragCCalc\\src\\data.csv");
        if (csvFilePath == null || csvFilePath.isEmpty()) {
            throw new FileNotFoundException("CSV file path is not set.");
        }
        File file = new File(csvFilePath);
        Scanner scanner = new Scanner(file);
        ArrayList<Double> dragCoefficient0 = new ArrayList<>();
        ArrayList<Double> dragCoefficient2 = new ArrayList<>();
        ArrayList<Double> dragCoefficient4 = new ArrayList<>();
        while (scanner.hasNext()) {
            // read the data from the csv file and skip the first line
            String line = scanner.nextLine();
            if (line.startsWith("Mach")) {
                dragCoefficient0.add(0.0);
                dragCoefficient2.add(0.0);
                dragCoefficient4.add(0.0);
                continue;
            }
            String[] values = line.split(",");
            double angleOfAttack = Double.parseDouble(values[1]);
            if (angleOfAttack == 0) {
                dragCoefficient0.add(Double.parseDouble(values[2]));
            } else if (angleOfAttack == 2) {
                dragCoefficient2.add(Double.parseDouble(values[2]));
            } else if (angleOfAttack == 4) {
                dragCoefficient4.add(Double.parseDouble(values[2]));
            }

        }
        data.add(dragCoefficient0);
        data.add(dragCoefficient2);
        data.add(dragCoefficient4);
        scanner.close();
    }


    public double getDragCoefficient(double mach, double angleOfAttack) {
        // implement a function that returns the drag coefficient based on the mach number and angle of attack

        angleOfAttack = clamp(angleOfAttack, 0, 4);
        mach = clamp(mach, 0, 24.99);

        int machIndex = (int) Math.floor(mach * MACH_SCALE);
        int angleOfAttackIndex = (int) Math.floor(angleOfAttack * ALPHA_SCALE);

        double dragCoefficient00 = data.get(angleOfAttackIndex).get(machIndex);
        double dragCoefficient01 = data.get(angleOfAttackIndex).get(machIndex + 1);
        double dragCoefficient10 = data.get(angleOfAttackIndex + 1).get(machIndex);
        double dragCoefficient11 = data.get(angleOfAttackIndex + 1).get(machIndex + 1);

        double dragCoefficient0 = dragCoefficient00 + (mach * MACH_SCALE - machIndex) * (dragCoefficient01 - dragCoefficient00);
        double dragCoefficient1 = dragCoefficient10 + (mach * MACH_SCALE - machIndex) * (dragCoefficient11 - dragCoefficient10);
        return dragCoefficient0 + (angleOfAttack * ALPHA_SCALE - angleOfAttackIndex) * (dragCoefficient1 - dragCoefficient0);
    }

    public AerodynamicForces postAerodynamicCalculation(SimulationStatus status, AerodynamicForces forces) throws SimulationException {
        // implement a function that calculates the drag coefficient and adds it to the aerodynamic forces
//        double mach = status.getFlightConditions().getMachNumber();
//        double angleOfAttack = status.getFlightConditions().getAngleOfAttack();
//        double dragCoefficient = getDragCoefficient(mach, angleOfAttack);
//        forces.setDragCoefficient(dragCoefficient);
        double vx = status.getRocketVelocity().x;
        double vy = status.getRocketVelocity().y;
        double vz = status.getRocketVelocity().z;
        double speedOfSound = clamp(340 - 0.00408 * status.getRocketPosition().z, 295.1, 340);
        double mach = Math.sqrt(vx * vx + vy * vy + vz * vz) / speedOfSound;
        forces.setCD(getDragCoefficient(mach,0));
        return forces;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

}