package frc.robot.Toolkit;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;

public class CT_DigitalInput {

    private DigitalInput m_digitalInput;
    private Runnable m_methodToRun;
    private boolean m_isInterruptEnabled;
    private boolean m_negateLogic;
    /**
     * Negate logic is useful if you want to reverse the output of getStatus(). A use for setting this variable true
     * would be if you were using a light beam sensor and wanted to differentiate between searching for something blocking
     * the sensors, or searching for the absence of something. 
     */ 
    

    /**
     * Creates a default DigitalIO instance with the ability to negate the logic.
     * Used for sensors connected through DIO ports on the RoboRio. Limit Switches, Light Beam sensors, etc.
     * 
     * @param pin the channel the sensor is on.
     * @param negateLogic can negate the return value of getStatus() for this instance.
     */
    public CT_DigitalInput(int pin, boolean negateLogic) {
        m_digitalInput = new DigitalInput(pin);
        m_negateLogic = negateLogic;
        m_isInterruptEnabled = false;
    }

    /**
     * Creates a default DigitalIO instance.
     * Used for sensors connected through DIO ports on the RoboRio. Limit Switches, Light Beam sensors, etc.
     * 
     * @param pin the channel the sensor is on.
     */
    public CT_DigitalInput(int pin) {
        this(pin, false);
    }

    /**
     * Creates a DigitalIO instance with a method and the ability to negate logic.
     * Used for sensors connected through DIO ports on the RoboRio. Limit Switches, Light Beam sensors, etc.
     * 
     * @param pin the channel the sensor is on.
     * @param methodToRun the method that will be ran in the runWhenTripped method.
     * @param negateLogic can negate the return value of getStatus() for this instance
     */
    public CT_DigitalInput(int pin, Runnable methodToRun, boolean negateLogic) { 
        m_digitalInput = new DigitalInput(pin);
        m_negateLogic = negateLogic;
        m_methodToRun = methodToRun;
        m_isInterruptEnabled = false;
    }

    /**
     * Creates a DigitalIO instance with a method.
     * Used for sensors connected through DIO ports on the RoboRio. Limit Switches, Light Beam sensors, etc.
     * 
     * @param pin the channel the sensor is on.
     * @param methodToRun the method that will be ran in the runWhenTripped method.
     */
    public CT_DigitalInput(int pin, Runnable methodToRun) { 
        this(pin, methodToRun, false);
    }

    /**
     * Sets the method that will be ran in the runWhenTripped method.
     * 
     * @param methodToRun the method that will be ran in the runWhenTripped method.
     */
    public void setMethodToRun(Runnable methodToRun) {
        m_methodToRun = methodToRun;
    }

    /**
     * Checks to see if the digital IO is tripped depending on if it is acive high or low.
     * If the sensor was tripped, the method either passed in to a constructor or DigitalIO method setMethodToRun will run.
     * This method will not work if an interrupt has been enabled. 
     * The given method will only run ONCE and will have to be reset by the DigitalIO method setMethodToRun.
     * This method is best used when called in the periodic method of a subsystem.
     * 
     * @return If the digital IO was tripped.
     */
    public boolean runWhenTripped() {
        if(getStatus() && !m_isInterruptEnabled) {

            if(m_methodToRun != null) {
                Runnable method = m_methodToRun;
                m_methodToRun = null;
                method.run();
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Set up an interrupt with a method to run when it is fired.
     * From testing, the U sensor's fire on falling edge when something blocks the sensor.
     * 
     * @param runnable the runnable to be run when the interrupt is fired.
     * @param interruptOnRisingEdge fire interrupt on the rising edge.
     * @param interruptOnFallingEdge fire interrupt on the falling edge
     * @param disableWhenFired disable the interrupt when fired.
     */
    public void setInterrupt(Runnable runnable, boolean interruptOnRisingEdge, boolean interruptOnFallingEdge, boolean disableWhenFired) {

        m_digitalInput.requestInterrupts(new InterruptHandlerFunction<Object>() {

            @Override
            public void interruptFired(int interruptAssertedMask, Object param) {
                runnable.run();

                if(disableWhenFired) {
                    disableInterrupts();
                }
            }
        });

        m_digitalInput.setUpSourceEdge(interruptOnRisingEdge, interruptOnFallingEdge);
        enableInterrupts();
    }

    /**
     * Enable interrupts.
     */
    public void enableInterrupts() {
        m_digitalInput.enableInterrupts();
        m_isInterruptEnabled = true;
    }

    /**
     * Disable interrupts.
     */
    public void disableInterrupts() {
        m_digitalInput.disableInterrupts();
        m_isInterruptEnabled = false;
    }


    /**
     * Gets the status of the digital input. 
     * Returned value is negated depending on the value of negateLogic passed into the constructor.
     * If no negateLogic boolean was passed into the constructor, then the logic will not be negated.
     * 
     * @return the status of the digital input.
     */
    public boolean getStatus() {
        if(m_negateLogic)
            return !m_digitalInput.get();
        else
            return m_digitalInput.get();
    }

}