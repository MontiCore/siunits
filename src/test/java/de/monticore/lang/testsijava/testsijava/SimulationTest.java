package de.monticore.lang.testsijava.testsijava;

import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

public class SimulationTest {

    @Before
    public void init() {
        Log.init();
        Log.enableFailQuick(false);
    }

    @Test
    public void simulate() {
        Simulation simulation = new Simulation();
        simulation.simulate("src/test/resources", "de/monticore/lang/testsijava/testsijava/Main.sijava", "main");
    }
}