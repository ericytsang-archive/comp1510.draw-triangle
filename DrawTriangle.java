package q2;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import static java.lang.Math.pow;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.atan;
import static java.lang.Math.PI;

/**
 * <p>draws an equilateral triangle rubber band style.
 * mouse pressed is the center of the triangle.
 * mouse dragged and released is one of the vertices of the triangle.</p>
 * @author Eric Tsang, 1A, A00841554
 * @version 1.0
 */
public class DrawTriangle extends JFrame {

    /** <p>unique version of this panel.</p> */
    private static final long serialVersionUID = 3562239838934569L;

    /** <p>half of a full revolution in radians.</p> */
    private static final double DEMI_REVOLUTION = PI;

    /** <p>one full revolution in radians.</p> */
    private static final double FULL_REVOLUTION = 2 * DEMI_REVOLUTION;

    /** <p>three.</p> */
    private static final double THREE = 3;

    /** <p>one third of a full revolution in radians.</p> */
    private static final double THIRD_OF_REVOLUTION = FULL_REVOLUTION / THREE;

    /** <p>width of the frame.</p> */
    private static final int FRAME_WIDTH = 400;

    /** <p>height of the frame.</p> */
    private static final int FRAME_HEIGHT = 400;

    /** <p>quadrants on a cartesian plane.</p> */
    private static enum Quadrant {
        /** <p>nothing.</p> */
        NULL,
        /** <p>quadrant one of the cartesian plane.</p> */
        QUADRANT_ONE,
        /** <p>quadrant two of the cartesian plane.</p> */
        QUADRANT_TWO,
        /** <p>quadrant three of the cartesian plane.</p> */
        QUADRANT_THREE,
        /** <p>quadrant four of the cartesian plane.</p> */
        QUADRANT_FOUR
    }

    /**
     * <p>sets up the frame of the application that will draw triangles...<p>
     */
    public DrawTriangle() {
        super("Eric Tsang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new DrawTrianglePanel());
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setVisible(true);
    }

    /**
     * <p>This is the main method (entry point) that gets called by the JVM.</p>
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        new DrawTriangle();
    }

    /**
     * <p>draws an equilateral triangle rubber band style.
     * mouse pressed is the center of the triangle.
     * mouse dragged and released is one of the vertices of the triangle.</p>
     * @author Eric Tsang, 1A, A00841554
     * @version 1.0
     */
    private class DrawTrianglePanel extends JPanel {

        /** Unique version of this panel. */
        private static final long serialVersionUID = 8841735628934569L;

        /** center point of the triangle. */
        private Point startPoint;

        /** one of the triangle's vertices. */
        private Point endPoint;

        /**
         * <p>instantiate Points.
         * adds listeners.</p>
         */
        public DrawTrianglePanel() {
            startPoint = new Point();
            endPoint = new Point();
            addMouseListener(new Listener());
            addMouseMotionListener(new Listener());
        }

        /**
         * <p>calculates the points of a triangle and draws it.</p>
         * @param g     Graphics page to draw upon.
         */
        public void paintComponent(Graphics g) {
            Point p1;
            Point p2;
            Point p3;
            double dist;
            double angle;

            super.paintComponent(g);

            // calculate the points of the triangle
            dist = getPointDistance(startPoint, endPoint);
            angle = getPointAngle(startPoint, endPoint);
            p1 = projectPoint(startPoint, angle, dist);
            p2 = projectPoint(startPoint, angle + THIRD_OF_REVOLUTION, dist);
            p3 = projectPoint(startPoint, angle - THIRD_OF_REVOLUTION, dist);

            // draw the triangle
            drawTrangle(p1, p2, p3, g);
        }

        /**
         * <p>returns the angle in radians between the line segment formed from the
         * two points given, and the positive x axis.</p>
         * @param p1    start point of the line.
         * @param p2    end point of the line.
         * @return      angle in radians between line given and positive x axis.
         */
        private double getPointAngle(Point p1, Point p2) {
            // declare variables
            double xDiff;
            double yDiff;
            double ret;
            double angleToXAxis;
            Quadrant quadrant;

            // initiate variables
            ret = 0;
            xDiff = abs(p1.x - p2.x);
            yDiff = abs(p1.y - p2.y);
            angleToXAxis = atan(yDiff / xDiff);
            quadrant = Quadrant.NULL;

            // calculate the quadrant p2 is in relative to p1
            if (p2.x - p1.x > 0) {
                if (p2.y - p1.y > 0) {
                    quadrant = Quadrant.QUADRANT_ONE;
                } else {
                    quadrant = Quadrant.QUADRANT_FOUR;
                }
            } else {
                if (p2.y - p1.y > 0) {
                    quadrant = Quadrant.QUADRANT_TWO;
                } else {
                    quadrant = Quadrant.QUADRANT_THREE;
                }
            }

            // calculate and return angle from the positive x axis in radians
            switch (quadrant) {
            case QUADRANT_ONE:
                ret = angleToXAxis;
                break;
            case QUADRANT_TWO:
                ret = DEMI_REVOLUTION - angleToXAxis;
                break;
            case QUADRANT_THREE:
                ret = DEMI_REVOLUTION + angleToXAxis;
                break;
            case QUADRANT_FOUR:
                ret = FULL_REVOLUTION - angleToXAxis;
                break;
            default:
                // do nothing
                break;
            }
            return ret;
        }

        /**
         * <p>returns the distance between two points.</p>
         * @param p1    point.
         * @param p2    another point.
         * @return      distance between the two points in pixels.
         */
        private double getPointDistance(Point p1, Point p2) {
            // declare variables
            double xDiff;
            double yDiff;

            // initiate variables
            xDiff = p1.x - p2.x;
            yDiff = p1.y - p2.y;

            // calculate and return distance between the given points
            return sqrt(pow(xDiff, 2) + pow(yDiff, 2));
        }

        /**
         * <p>plots a point a distance from the point given
         * at an angle starting at the positive x axis.
         * returns the plotted point</p>
         * @param projectionPoint    point to project from.
         * @param angle              angle to project to in radians.
         * @param distance           distance from point to projected point in pixels.
         * @return                   projected point.
         */
        private Point projectPoint(Point projectionPoint, double angle, double distance) {
            // declare variables
            int pointX;
            int pointY;

            // calculate point
            pointX = (int) (cos(angle) * distance);
            pointY = (int) (sin(angle) * distance);
            pointX += projectionPoint.x;
            pointY += projectionPoint.y;

            // instantiate and return point
            return new Point(pointX, pointY);
        }

        /**
         * <p>draws a triangle when given the 3 vertices
         * of the triangle and the Graphics page.</p>
         * @param p1    first vertices.
         * @param p2    second vertices.
         * @param p3    third vertices.
         * @param g     Graphics page to draw upon.
         */
        private void drawTrangle(Point p1, Point p2, Point p3, Graphics g) {
            // connect the points to draw a triangle
            drawLine(p1, p2, g);
            drawLine(p2, p3, g);
            drawLine(p3, p1, g);
        }

        /**
         * <p>draws a line between 2 given points 
         * and the Graphics page.</p>
         * @param p1    first vertices.
         * @param p2    second vertices.
         * @param g     Graphics page to draw upon.
         */
        private void drawLine(Point p1, Point p2, Graphics g) {
            // connect the 2 points to draw a line
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        /**
         * <p>mouse listener.</p>
         * @author Eric Tsang, 1A, A00841554
         * @version 1.0
         */
        private class Listener extends MouseAdapter {
            /**
             * <p>sets start point and end point of the rubber band.
             * repaints.</p>
             * @param e    event generated by the listener.
             */
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getSource());
                startPoint = e.getPoint();
                endPoint = e.getPoint();
                repaint();
            }

            /**
             * <p>sets end point of the rubber band.
             * repaints.</p>
             * @param e    event generated by the listener.
             */
            public void mouseReleased(MouseEvent e) {
                System.out.println(e.getSource());
                endPoint = e.getPoint();
                repaint();
            }

            /**
             * <p>sets end point of the rubber band.
             * repaints.</p>
             * @param e    event generated by the listener.
             */
            public void mouseDragged(MouseEvent e) {
                System.out.println(e.getSource());
                endPoint = e.getPoint();
                repaint();
            }
        }

    }

}
