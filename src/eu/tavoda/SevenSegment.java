package eu.tavoda;

import javax.swing.*;
import java.awt.*;
//import java.awt.geom.AffineTransform;

/**
 * @author kiunwong
 */
public class SevenSegment extends JPanel {
	private final static int SEGMENT_COUNT = 7;
	private final static int A = 0;
	private final static int B = 1;
	private final static int C = 2;
	private final static int D = 3;
	private final static int E = 4;
	private final static int F = 5;
	private final static int G = 6;
	private final static int OFF = 0;
	private final static int ON = 1;

	private final static int[] zero = {ON, ON, ON, ON, ON, ON, OFF};
	private final static int[] one = {OFF, ON, ON, OFF, OFF, OFF, OFF};
	private final static int[] two = {ON, ON, OFF, ON, ON, OFF, ON};
	private final static int[] three = {ON, ON, ON, ON, OFF, OFF, ON};
	private final static int[] four = {OFF, ON, ON, OFF, OFF, ON, ON};
	private final static int[] five = {ON, OFF, ON, ON, OFF, ON, ON};
	private final static int[] six = {ON, OFF, ON, ON, ON, ON, ON};
	private final static int[] seven = {ON, ON, ON, OFF, OFF, OFF, OFF};
	private final static int[] eight = {ON, ON, ON, ON, ON, ON, ON};
	private final static int[] nine = {ON, ON, ON, ON, OFF, ON, ON};
	private final static int[][] SEGMENT_CONFIG = {
			zero, one, two, three, four, five, six, seven, eight, nine
	};
	private final static Color off = Color.green.darker().darker().darker().darker();
	private final static Color on = Color.green.brighter().brighter().brighter().brighter().brighter();
	private double xScale = 1d;
	private double yScale = 1d;
	private final Polygon[] segments = new Polygon[SEGMENT_COUNT];
	private int[] number;

	public SevenSegment(int width, int height) {
		xScale = width / 110d;
		yScale = height / 180d;
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setOpaque(true);
		setBackground(Color.black);

		createSegments();
		number = zero;
	}

	public void writeNumber(int n) {
		if (n > 0 && n < 10) {
			number = SEGMENT_CONFIG[n];
		} else {
			number = zero;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < SEGMENT_COUNT; i++) {
			setSegmentState(g, segments[i], number[i]);
		}
	}

	int [] HORIZONTAL_SEGMENT = {20, 8, 90, 8, 98, 15, 90, 22, 20, 22, 12, 15};
	int [] VERTICAL_SEGMENT = {91, 23, 98, 18, 105, 23, 105, 81, 98, 89, 91, 81};

	private Polygon createPolygon(int[] polygonTemplate, int startX, int startY) {
		Polygon segment = new Polygon();
		for (int i = 0; i < polygonTemplate.length; i+=2) {
			segment.addPoint((int) (polygonTemplate[i] * xScale + startX), (int) (polygonTemplate[i + 1] * yScale + startY));
		}
		return segment;
	}

	private void createSegments() {
		segments[A] = createPolygon(HORIZONTAL_SEGMENT, 0, 0);
		segments[B] = createPolygon(VERTICAL_SEGMENT, 0, 0);
		segments[C] = createPolygon(VERTICAL_SEGMENT, 0, 74);

//		segments[A] = new Polygon();
//		segments[A].addPoint((int) (20 * xScale), (int) (8 * yScale));
//		segments[A].addPoint((int) (90 * xScale), (int) (8 * yScale));
//		segments[A].addPoint((int) (98 * xScale), (int) (15 * yScale));
//		segments[A].addPoint((int) (90 * xScale), (int) (22 * yScale));
//		segments[A].addPoint((int) (20 * xScale), (int) (22 * yScale));
//		segments[A].addPoint((int) (12 * xScale), (int) (15 * yScale));
//
//		segments[B] = new Polygon();
//		segments[B].addPoint(x + 91, y + 23);
//		segments[B].addPoint(x + 98, y + 18);
//		segments[B].addPoint(x + 105, y + 23);
//		segments[B].addPoint(x + 105, y + 81);
//		segments[B].addPoint(x + 98, y + 89);
//		segments[B].addPoint(x + 91, y + 81);
//
//		segments[C] = new Polygon();
//		segments[C].addPoint(x + 91, y + 97);
//		segments[C].addPoint(x + 98, y + 89);
//		segments[C].addPoint(x + 105, y + 97);
//		segments[C].addPoint(x + 105, y + 154);
//		segments[C].addPoint(x + 98, y + 159);
//		segments[C].addPoint(x + 91, y + 154);
//
//		segments[D] = new Polygon();
//		segments[D].addPoint(x + 20, y + 155);
//		segments[D].addPoint(x + 90, y + 155);
//		segments[D].addPoint(x + 98, y + 162);
//		segments[D].addPoint(x + 90, y + 169);
//		segments[D].addPoint(x + 20, y + 169);
//		segments[D].addPoint(x + 12, y + 162);
//
//		segments[E] = new Polygon();
//		segments[E].addPoint(x + 5, y + 97);
//		segments[E].addPoint(x + 12, y + 89);
//		segments[E].addPoint(x + 19, y + 97);
//		segments[E].addPoint(x + 19, y + 154);
//		segments[E].addPoint(x + 12, y + 159);
//		segments[E].addPoint(x + 5, y + 154);
//
//		segments[F] = new Polygon();
//		segments[F].addPoint(x + 5, y + 23);
//		segments[F].addPoint(x + 12, y + 18);
//		segments[F].addPoint(x + 19, y + 23);
//		segments[F].addPoint(x + 19, y + 81);
//		segments[F].addPoint(x + 12, y + 89);
//		segments[F].addPoint(x + 5, y + 81);
//
//		segments[G] = new Polygon();
//		segments[G].addPoint(x + 20, y + 82);
//		segments[G].addPoint(x + 90, y + 82);
//		segments[G].addPoint(x + 95, y + 89);
//		segments[G].addPoint(x + 90, y + 96);
//		segments[G].addPoint(x + 20, y + 96);
//		segments[G].addPoint(x + 15, y + 89);
	}

	private void setSegmentState(Graphics graphics, Polygon segment, int state) {
		if (state == OFF) {
			graphics.setColor(off);
		} else {
			graphics.setColor(on);
		}

		graphics.fillPolygon(segment);
		graphics.drawPolygon(segment);
	}
}