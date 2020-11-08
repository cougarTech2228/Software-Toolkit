package frc.robot.Toolkit;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;
/**
 * Wire pins
 * 
 * White wire - SDA (Data)
 * Blue wire - SCL (Clock)
 * Red wire - Voltage
 * Black wire - Ground
 */

public class CT_ColorSensor extends ColorSensorV3{

    private final ColorMatch m_colorMatcher = new ColorMatch();

    // private final Color m_kBlueTarget = Color.kAqua;
    // private final Color m_kGreenTarget = Color.kLime;
    // private final Color m_kRedTarget = Color.kRed;
    // private final Color m_kYellowTarget = Color.kYellow;
    private final Color m_kBlueTarget = ColorMatch.makeColor(0.12, 0.42, 0.45);
    private final Color m_kGreenTarget = ColorMatch.makeColor(0.17, 0.57, 0.25);
    private final Color m_kRedTarget = ColorMatch.makeColor(0.51, 0.34, 0.13);
    private final Color m_kYellowTarget = ColorMatch.makeColor(0.32, 0.55, 0.12);

    private Runnable m_methodToRun;

    /**
     * Creates a new default ColorSensor instance.
     * 
     * @param port Port that the sensor is connected to. Either kMXP or kOnBoard.
     */
    public CT_ColorSensor(I2C.Port port) {
        this(port, null);
    }

    /**
     * Creates a new ColorSensor instance with a method. 
     * 
     * @param port Port that the sensor is connected to. Either kMXP or kOnBoard.
     * @param methodToRun the method that will be ran in the runWhenColorIsDetected method.
     */
    public CT_ColorSensor(I2C.Port port, Runnable methodToRun) {
        super(port);
        m_methodToRun = methodToRun;

        m_colorMatcher.addColorMatch(m_kBlueTarget);
        m_colorMatcher.addColorMatch(m_kGreenTarget);
        m_colorMatcher.addColorMatch(m_kRedTarget);
        m_colorMatcher.addColorMatch(m_kYellowTarget); 
    }

    /**
     * Sets the method that will be ram in the runWhenColorIsDetected method.
     * 
     * @param methodToRun the method that will be ran in the runWhenColorIsDetected method.
     */
    public void setMethodToRun(Runnable methodToRun) {
        m_methodToRun = methodToRun;
    }

    /**
     * Gets the color seen by the color sensor.
     * 
     * @return the Color object that is seen by the color sensor.
     */
    public Color getMatchedColor() {
        Color detectedColor = getColor();
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
        Color detectedColor = super.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        return match.confidence;
    }

    /**
     * Gives the current color as a string.
     * 
     * @return the current color in a readable format.
     */
    public String getMatchedColorString() {
        Color detectedColor = getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        if (match.color == m_kBlueTarget) {
            return "Blue";
        } else if (match.color == m_kRedTarget) {
            return "Red";
        } else if (match.color == m_kGreenTarget) {
            return "Green";
        } else if (match.color == m_kYellowTarget) {
            return "Yellow";
        } else {
            return "Unexpected error or color occurred.";
        }
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

        if(color.equals(getMatchedColor())) {

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
}