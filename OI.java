package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class OI {
    private static final int kXboxChannel = 0;
    private static XboxController m_xboxController;

    public OI() {
        m_xboxController = new XboxController(kXboxChannel);
    }

    public static boolean getXboxAButton() {
        return m_xboxController.getAButton();
    }

    public static boolean getXboxBButton() {
        return m_xboxController.getBButton();
    }

    public static boolean getXboxXButton() {
        return m_xboxController.getXButton();
    }

    public static boolean getXboxYButton() {
        return m_xboxController.getYButton();
    }

    public static boolean getXboxStartButton() {
        return m_xboxController.getStartButton();
    }

    public static boolean getXboxBackButton() {
        return m_xboxController.getBackButton();
    }

    public static boolean getXboxRightBumper() {
        return m_xboxController.getRightBumper();
    }

    public static boolean getXboxLeftBumper() {
        return m_xboxController.getLeftBumper();
    }

    public static boolean getXboxLeftJoystickPress() {
        return m_xboxController.getLeftStickButton();
    }

    public static boolean getXboxRightJoystickPress() {
        return m_xboxController.getRightStickButton();
    }

    public static double getXboxRightTrigger() {
        return m_xboxController.getRightTriggerAxis();
    }

    public static boolean getXboxRightTriggerPressed() {
        return m_xboxController.getRightTriggerAxis() > 0.2;
    }

    public static double getXboxLeftTrigger() {
        return m_xboxController.getLeftTriggerAxis();
    }

    public static boolean getXboxLeftTriggerPressed() {
        return m_xboxController.getLeftTriggerAxis() > 0.2;
    }

    public static double getXboxRightJoystickX() {
        return m_xboxController.getRightX();
    }

    public static double getXboxRightJoystickY() {
        return m_xboxController.getRightY();
    }

    public static double getXboxLeftJoystickX() {
        return m_xboxController.getLeftX();
    }

    public static double getXboxLeftJoystickY() {
        return m_xboxController.getLeftY();
    }

    public static boolean getXboxDpadUp() {
        int pov = m_xboxController.getPOV(0);
        return (((pov >= 0) && (pov <= 45)) || ((pov >= 315) && (pov <= 360)));
    }

    public static boolean getXboxDpadRight() {
        int pov = m_xboxController.getPOV(0);
        return ((pov >= 45) && (pov <= 135));
    }

    public static boolean getXboxDpadDown() {
        int pov = m_xboxController.getPOV(0);
        return ((pov >= 135) && (pov <= 225));
    }

    public static boolean getXboxDpadLeft() {
        int pov = m_xboxController.getPOV(0);
        return ((pov >= 225) && (pov <= 315));
    }

    public static void setXboxRumbleSpeed(double rumbleSpeed) {
        m_xboxController.setRumble(RumbleType.kLeftRumble, rumbleSpeed);
        m_xboxController.setRumble(RumbleType.kRightRumble, rumbleSpeed);
    }

    public static void setXboxLeftRumbleSpeed(double rumbleSpeed) {
        m_xboxController.setRumble(RumbleType.kLeftRumble, rumbleSpeed);
    }

    public static void setXboxRightRumbleSpeed(double rumbleSpeed) {
        m_xboxController.setRumble(RumbleType.kRightRumble, rumbleSpeed);
    }

    public static void setXboxRumbleStop() {
        OI.setXboxRumbleSpeed(0);
    }

}