package frc.robot.Toolkit;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

public class CT_ColorSensor {

    private final ColorSensorV3 m_colorSensor;
    private final ColorMatch m_colorMatcher = new ColorMatch();

    // private final Color m_kBlueTarget = Color.kAqua;
    // private final Color m_kGreenTarget = Color.kLime;
    // private final Color m_kRedTarget = Color.kRed;
    // private final Color m_kYellowTarget = Color.kYellow;
    private final Color m_kBlueTarget = ColorMatch.makeColor(0.12, 0.42, 0.45);
    private final Color m_kGreenTarget = ColorMatch.makeColor(0.17, 0.57, 0.25);
    private final Color m_kRedTarget = ColorMatch.makeColor(0.51, 0.34, 0.13);
    private final Color m_kYellowTarget = ColorMatch.makeColor(0.32, 0.55, 0.12);

    private Runnable methodToRun;

    /**
     * Creates a new default ColorSensor instance.
     * 
     * @param port Port that the sensor is connected to. Either kMXP or kOnBoard.
     */
    public CT_ColorSensor(I2C.Port port) {
        m_colorSensor = new ColorSensorV3(port);
    }

    /**
     * Creates a new ColorSensor instance with a method. 
     * 
     * @param port Port that the sensor is connected to. Either kMXP or kOnBoard.
     * @param methodToRun the method that will be ran in the runWhenColorIsDetected method.
     */
    public CT_ColorSensor(I2C.Port port, Runnable methodToRun) {
        m_colorSensor = new ColorSensorV3(port);
        this.methodToRun = methodToRun;
    }

    /**
     * Sets the method that will be ram in the runWhenColorIsDetected method.
     * 
     * @param methodToRun the method that will be ran in the runWhenColorIsDetected method.
     */
    public void setMethodToRun(Runnable methodToRun) {
        this.methodToRun = methodToRun;
    }

    /**
     * Gets the color seen by the color sensor.
     * 
     * @return the Color object that is seen by the color sensor.
     */
    public Color getColor() {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        if (match.color == m_kBlueTarget) {
            return Color.kBlue;
        } else if (match.color == m_kRedTarget) {
            return Color.kRed;
        } else if (match.color == m_kGreenTarget) {
            return Color.kGreen;
        } else if (match.color == m_kYellowTarget) {
            return Color.kYellow;
        } else {
            return null;
        }
    }

    /**
     * Gets the confidence value of the color match.
     * 
     * @return a double where 0 is the low confidence and 1 is high confidence.
     */
    public double getConfidence() {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        return match.confidence;
    }
    
    /**
     * Checks to see if the passed in color is the same one the sensor sees. 
     * If the color is the same, the method either given by the constructor 
     * ColorSensor(port, methodToRun) or ColorSensor method setMethodToRun will run.
     * The given method will only run ONCE and will have to be reset by the ColorSensor method setMethodToRun.
     * This method is best used when called in the periodic method of a subsystem.
     * 
     * @param color the Color object that will be compared to the current color. Color.kBlue, Color.kRed, etc.
     * @return If the two colors were the same and the method was run. 
     */
    public boolean runWhenColorIsDetected(Color color) {

        if(color.equals(getColor())) {

            if(methodToRun != null) {
                Runnable method = methodToRun;
                methodToRun = null;
                method.run();
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }
}