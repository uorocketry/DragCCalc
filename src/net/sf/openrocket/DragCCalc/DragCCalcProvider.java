package net.sf.openrocket.DragCCalc;
import net.sf.openrocket.plugin.Plugin;
import net.sf.openrocket.simulation.extension.AbstractSimulationExtensionProvider;

@Plugin
public class DragCCalcProvider extends AbstractSimulationExtensionProvider {
    public DragCCalcProvider() {
        super(DragCCalc.class, "Flight", "Drag Coefficient Importer");
    }
}
