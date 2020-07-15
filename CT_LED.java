package frc.robot.Toolkit;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;

public class CT_LED {

    private AddressableLED LED;
    private AddressableLEDBuffer LEDBuffer;
    private int m_rainbowFirstPixelHue = 0;
    private int m_colorIndex = 0;
    private int snakeLoopIndex = 0;
    private int m_doMoveCounter = 0;

    public enum Speed {
        Slow, // 50 loop iterations
        Fast, // 25 loop iterations
        Ludicrous // 1 loop iteration
    }

    /**
     * Creates a default CT_LED instance where the LED length is the max.
     * 
     * @param PWMPort the PWM port the LED Strip is connected.
     */
    public CT_LED(int PWMPort) {
        this(PWMPort, 150); // 150 is the max amount of LEDS on a standard LED strip
    }

    /**
     * Creates a CT_LED instance where the LED length can be passed in.
     * 
     * @param PWMPort the PWM port the LED Strip is connected.
     * @param length the amount of individual LEDS that will be turned on and affected by color chanes.
     */
    public CT_LED(int PWMPort, int length) {
        LED = new AddressableLED(PWMPort);
        LEDBuffer = new AddressableLEDBuffer(length);
        LED.setLength(LEDBuffer.getLength());
        LED.setData(LEDBuffer);
    }

    /**
     * Turn on the LED Strip.
     */
    public void startLEDs() {
        LED.start();
    }

    /**
     * Turn off the LED Strip.
     */
    public void stopLEDS() {
        LED.stop();
    }

    /**
     * Sets the colors of the LED strip. Colors passed in will retain their order on the LED strip.
     * For example, if (Color.kRed, Color.kBlue) is passed in, the pattern "red, blue, red, blue, red, blue" 
     * will repeat on the LED strip. 
     * If the LED Strip draws too much current, using the color white too often or having the LED strip length too long 
     * might cause colors to be dimmed or show up on the LED strip incorrectly. (e.g. 2 amps will not support a 150 white LED strip)
     * 
     * @param color the colors to be shown on the LED Strip.
     */
    public void setColor(Color... color) {

        if(color.length > 0) {

            int colorIndex = 0;

            for (int ledIndex = 0; ledIndex < LEDBuffer.getLength(); ledIndex++) {

                if(colorIndex == color.length - 1) { // Cycles the colorIndex variable to rotate through the color array.
                    colorIndex = 0;
                } else {
                    colorIndex++;
                }

                LEDBuffer.setLED(ledIndex, color[colorIndex]);

            }

            LED.setData(LEDBuffer);
        }
    }

    /**
     * Sets colors that will move along the LED strip. This method should be called
     * in the periodic of a subsystem to gain full effect. 
     * 
     * @param speed the speed at which the snake will move. Current speeds:
     *              Slow, Fast, Ludicrous.
     * @param color the colors that will be moving on the LED strip. More than 1 color should be passed in or the method
     * will not function.
     */
    public void setMovingColors(Speed speed, Color... color) {

        if (color.length <= 1) {
            System.out.println("Too little amount of colors passed in, pass in more colors or use the setColor method.");
        }

        int colorLoopIndex = m_colorIndex;

        // Waits for the correct amount of loop iterations to complete.
        if(m_doMoveCounter == 25 && speed == Speed.Fast ||
            m_doMoveCounter == 50 && speed == Speed.Slow ||
            m_doMoveCounter == 1 && speed == Speed.Ludicrous) {

            // Loops through the whole LED strip.
            for (int ledIndex = 0; ledIndex < LEDBuffer.getLength(); ledIndex++) {
    
                // Increment the color loop index
                colorLoopIndex++;

                // Reset the color loop index back to 0 if it excedes the amount of values in the array.
                if (colorLoopIndex > (color.length - 1)) {
                    colorLoopIndex = 0;
                } 

                LEDBuffer.setLED(ledIndex, color[colorLoopIndex]);
            }

            // Increment the color index so the color pattern is off by one on the next time this method is run. 
            // This gives the effect of the colors moving down the LED strip.
            m_colorIndex++;

            // Put the color index back to 0 if it excedes the amount of values in the array.
            if (m_colorIndex > (color.length - 1)) {
                m_colorIndex = 0;
            }
            m_doMoveCounter = 0;

        } else {
            m_doMoveCounter++;
        }

        LED.setData(LEDBuffer);
    }

    /**
     * Creates a snake pattern on the LED strip where the pattern contained in the
     * snakeColorPattern array moves over the background color. This method should
     * be called in the periodic of a subsystem to gain full effect.
     * 
     * @param speed             the speed at which the snake will move. Current 
     *                          speeds: Slow, Fast, Ludicrous.
     * @param backgroundColor   the color that will be the background that the snake will travel over.
     * @param snakeColorPattern the snake pattern that will traverse the LED strip. 
     * Length needs to be greater than 0 or the method will not function.
     */
    public void doSnake(Speed speed, Color backgroundColor, Color[] snakeColorPattern) {
        
        if (snakeColorPattern.length == 0) {
            System.out.println("Snake length is zero, create a longer snake by making the snakeColorPattern array longer.");
        }

        // Waits for the correct amount of loop iterations to complete.
        if(m_doMoveCounter == 25 && speed == Speed.Fast ||
            m_doMoveCounter == 50 && speed == Speed.Slow ||
            m_doMoveCounter == 1 && speed == Speed.Ludicrous) {

            // Loops through the whole LED strip.
            for (int ledIndex = 0; ledIndex < LEDBuffer.getLength(); ledIndex++) {

                 // Checks if the current led is the one that will start the snake.
                if(ledIndex == snakeLoopIndex) {

                    int endOfSnakeIndex = ledIndex + snakeColorPattern.length;
                    int currentColorIndex = 0;

                    // Loops through the entire snake.
                    for(int snakeIndex = ledIndex; snakeIndex < LEDBuffer.getLength() + snakeColorPattern.length; snakeIndex++) {
                        

                        // If the current color index is outside the color array, that means the snake is complete.
                        if(currentColorIndex == snakeColorPattern.length) { 
                            break; 
                        }

                        int newSnakeIndex = snakeIndex;
                         
                        // Moves the new snake index back to the beginning when the snake goes over 
                        // the end so it flows smoothly to the beginning again.
                        if(snakeIndex > LEDBuffer.getLength() - 1) {
                            newSnakeIndex -= LEDBuffer.getLength(); 
                        }

                        LEDBuffer.setLED(newSnakeIndex, snakeColorPattern[currentColorIndex]);
                        currentColorIndex++;

                    }

                    // Advances the ledIndex to the end of the snake.
                    ledIndex = endOfSnakeIndex - 1;

                } else {
                    LEDBuffer.setLED(ledIndex, backgroundColor);
                }

            }
            m_doMoveCounter = 0;

            snakeLoopIndex++;

            // Reset the snake loop index back to the beginning when it reaches the end.
            if (snakeLoopIndex > LEDBuffer.getLength() - 1) {
                snakeLoopIndex = 0;
            }

        } else {
            m_doMoveCounter++;
        }

        LED.setData(LEDBuffer);
    }

    /**
     * Creates a rainbow effect on the LED strip.
     * This method should be called in the periodic of a subsystem to gain full effect.
     * 
     * Code is from https://docs.wpilib.org/en/stable/docs/software/actuators/addressable-leds.html 
     * with a few modifcations to make it more readable and standard.
     */
    public void doRainbow() {
        // For every pixel
        for (int i = 0; i < LEDBuffer.getLength(); i++) {
            // Calculate the hue - hue is easier for rainbows because the color
            // shape is a circle so only one value needs to precess
            int hue = (m_rainbowFirstPixelHue + (i * 180 / LEDBuffer.getLength())) % 180;
            // Set the value
            LEDBuffer.setHSV(i, hue, 255, 128);
        }

        // Increase by to make the rainbow "move"
        m_rainbowFirstPixelHue += 3;
        // Check bounds
        m_rainbowFirstPixelHue %= 180;

        LED.setData(LEDBuffer);
    }
}