package eu.tavoda.swing;

import javax.swing.*;
import java.awt.*;

public class SegmentDisplay extends JPanel {
	private static final Color SEGMENT_ON = Color.GREEN.brighter().brighter().brighter().brighter().brighter();
	private static final Color SEGMENT_OFF = Color.DARK_GRAY;
	private static final Color BACKGROUND = Color.BLACK;
	private static final int DEFAULT_BORDER = 3;
	private static final int DEFAULT_THICK  = 5;
	private static final int DEFAULT_SPACE  = 2;

	SevenSegment[] segments;

	public SegmentDisplay(int segments, int width, int height) {
		this(segments, width, height, DEFAULT_BORDER, DEFAULT_THICK, DEFAULT_SPACE, BACKGROUND, SEGMENT_ON, SEGMENT_OFF);
	}

	public SegmentDisplay(int segments, int width, int height, int border, int thick, int space) {
		this(segments, width, height, border, thick, space, BACKGROUND, SEGMENT_ON, SEGMENT_OFF);
	}

	public SegmentDisplay(int segments, int width, int height, int border, int thick, int space, Color background, Color on, Color off) {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.segments = new SevenSegment[segments];
		for (int i = 0; i < segments; i++) {
			this.segments[i] = new SevenSegment(width, height, border, thick, space, background, on, off);
			add(this.segments[i]);
		}
	}

	public void setValue(int value) {
		String strValue = Integer.toString(value);
		int offset = strValue.length() - segments.length;
		for (int i = 0; i < segments.length; i++) {
			int strOffset = offset + i;
			char ch = strOffset >= 0 ? strValue.charAt(strOffset) : '0';
			segments[i].setNumber(ch - '0');
		}
	}
}
