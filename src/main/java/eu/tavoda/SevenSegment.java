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
	private Color off;
	private Color on;
	private int width;
	private int height;
	private int border;
	private int thick;
	private int space;
	private final Polygon[] segments = new Polygon[SEGMENT_COUNT];
	private int[] number;

	public SevenSegment(int width, int height, int border, int thick, int space, Color background, Color on, Color off) {
		this.width = width;
		this.height = height;
		this.border = border;
		this.thick = thick;
		this.space = space;
		this.on = on;
		this.off = off;
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setOpaque(true);
		setBackground(background);
		createSegments();
		number = zero;
	}

	public void setNumber(int n) {
		if (n > 0 && n < 10) {
			number = SEGMENT_CONFIG[n];
		} else {
			number = zero;
		}
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < SEGMENT_COUNT; i++) {
			setSegmentState(g, segments[i], number[i]);
		}
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

	private void createSegments() {
		int thickH = thick / 2;
		int p = border + thickH;
		int sw = width - 2 * p;
		int sh = height / 2 - p;
		segments[A] = createHorizontalPolygon(p, p, sw, thickH, space);
		segments[B] = createVerticalPolygon(width - p, p, sh, thickH, space);
		segments[C] = createVerticalPolygon(width - p, height / 2, sh, thickH, space);
		segments[D] = createHorizontalPolygon(p, height - p, sw, thickH, space);
		segments[E] = createVerticalPolygon(p, height / 2, sh, thickH, space);
		segments[F] = createVerticalPolygon(p, p, sh, thickH, space);
		segments[G] = createHorizontalPolygon(p, height / 2, sw, thickH, space);
	}

	private Polygon createHorizontalPolygon(int startX, int startY, int length, int thickH, int space) {
		int xs = startX + space;
		int xe = startX + length - space;
		Polygon segment = new Polygon();
		segment.addPoint(xs, startY);
		segment.addPoint(xs + thickH, startY - thickH);
		segment.addPoint(xe - thickH, startY - thickH);
		segment.addPoint(xe, startY);
		segment.addPoint(xe - thickH, startY + thickH);
		segment.addPoint(xs + thickH, startY + thickH);

		return segment;
	}

	private Polygon createVerticalPolygon(int startX, int startY, int length, int thickH, int space) {
		int ys = startY + space;
		int ye = startY + length - space;
		Polygon segment = new Polygon();
		segment.addPoint(startX, ys);
		segment.addPoint(startX + thickH, ys + thickH);
		segment.addPoint(startX + thickH, ye - thickH);
		segment.addPoint(startX, ye);
		segment.addPoint(startX - thickH, ye - thickH);
		segment.addPoint(startX - thickH, ys + thickH);

		return segment;
	}
}