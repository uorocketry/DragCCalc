package net.sf.openrocket.DragCCalc;

import net.sf.openrocket.simulation.SimulationConditions;
import net.sf.openrocket.simulation.exception.SimulationException;
import net.sf.openrocket.simulation.extension.AbstractSimulationExtension;


public class DragCCalc extends AbstractSimulationExtension{
    private String csvFilePath;

    @Override
    public String getName() {
        return "Drag Coefficient Calculator";
    }

    @Override
    public String getDescription() {
        return "Calculates the drag coefficient of the rocket";
    }

    @Override
    public void initialize(SimulationConditions conditions) throws SimulationException {
        conditions.getSimulationListenerList().add(new DragCCalcSimulationListener(csvFilePath));
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

}
