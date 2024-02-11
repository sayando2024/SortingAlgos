package graphics;

import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit.*;
import javax.swing.*;
import java.io.*;
import java.util.*;


public class SimpleBarchart extends JFrame
{
    private final int OUTER_MARGIN = 20;
    private static final Color BACKGROUND_COLOR = Color.white;
    private static final Color BAR_COLOR = Color.red; 
    private int SPACE_ON_LEFT_RIGHT;
    private Image fImageBuffer;
    private Insets fInsets;
    private Graphics g;
    private Bar[] bars;
    private static int SLEEP = 500;
    private int[] inputData;


class Bar
{
    private int height, value;
    public int width;
    public int nwx;
    public int nwy;
    public Color color;

    public Bar() {}
    public Bar(int height, int width, int value, int nwx, int nwy)
    {
        this.height = height;
        this.width = width; 
        this.value = value; 
        this.nwx = nwx;
        this.nwy = nwy;
    }

}


public SimpleBarchart(final int[] inputData)
{
    this.inputData = inputData;
    addWindowListener(new WindowCloser());
    fInsets = getInsets();
    setSize(WIDTH + fInsets.left + fInsets.right, HEIGHT + fInsets.top + fInsets.bottom);
    setTitle("Bar Chart");
    if (((fImageBuffer = createImage(WIDTH, HEIGHT)) == null) ||
            ((g = fImageBuffer.getGraphics()) == null))
            System.exit(1);
    readData();
    createBars();
    getContentPane().add(new SimpleBarchart(inputData), BorderLayout.CENTER);
    setVisible(true);
}

/**
 *
 * @param g
 */
protected void paintComponent(final Graphics g) {
    g.drawImage(fImageBuffer, fInsets.left, fInsets.top, null);
}

class WindowCloser extends WindowAdapter
{
    @Override
    public void windowClosing(WindowEvent e)
    {
        System.exit(0);
    }
}

private void readData()
{
    String[] inputItems = JOptionPane.showInputDialog("Enter 1 to 9 integers > 0").trim().split(" +");
    int numData = inputItems.length;
    inputData = new int[numData];

    for (int itemIndex = 0; itemIndex < inputItems.length; itemIndex++)
        inputData[itemIndex] = numData;


}

private void createBars()
{

//Im confused on how to create the bars for this program. 
//This function requires 25 pixels of space on top and bottom of the display-    window, 10 pixels between the bars, and has to allow bar-heights to be **scaled to the form of inputData items.** 

    Bar[] bars = new Bar[];
    int pixelBetweenBars = 25;
    int width = 800 + 2*OUTER_MARGIN;
    int height = 600 + 2*OUTER_MARGIN;

}

private void drawBars(final Graphics g)
{
            int OUTER_MARGIN = 20,
            WIDTH = 800 + 2 * OUTER_MARGIN,
            HEIGHT = 600 + 2 * OUTER_MARGIN;


    g.setColor(BACKGROUND_COLOR);
    g.fillRect(0, 0, WIDTH, HEIGHT);

    g.setColor(BAR_COLOR);
    final int barWidth = 20;
    for (int itemIndex = 0; itemIndex < inputData.length; itemIndex++) {
        final int x = OUTER_MARGIN + 25 * itemIndex;
        final int barHeight = 10 * inputData[itemIndex];
        final int y = barHeight;
        g.fillRect(x, y, barWidth, barHeight);
    }
}
