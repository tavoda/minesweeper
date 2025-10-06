package eu.tavoda;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CustomGameDialog extends JFrame {
	public static final String H_SIZE = "hSize";
	public static final String V_SIZE = "vSize";
	public static final String MINES = "mines";
	public static final String RANDOM = "random";
	public static final String ID = "ID";
	Map<String, JTextComponent> fields = new HashMap<>();

	public CustomGameDialog(JFrame parent) {
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setTitle("Custom game");
		JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
		createInput(panel, H_SIZE, "Horizontal size", 40L, true);
		createInput(panel, V_SIZE, "Vertical size", 20L, true);
		createInput(panel, MINES, "Mines", 120L, true);
		createInput(panel, RANDOM, "Random", Math.abs((new Random()).nextLong()), true);
		createInput(panel, ID, "ID", 0L, false);
		encodeId();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		Dimension d = panel.getPreferredSize();
		d.width = 600;
		panel.setMaximumSize(d);

		add(panel, BorderLayout.CENTER);
		add(Box.createVerticalGlue(), BorderLayout.NORTH);

		JPanel buttons = new JPanel();
		JButton start = new JButton("New game");
		start.addActionListener(e -> {
			System.out.println(H_SIZE + ": " + fields.get(H_SIZE).getText());
			System.out.println(V_SIZE + ": " + fields.get(V_SIZE).getText());
			System.out.println(MINES + ": " + fields.get(MINES).getText());
			System.out.println(RANDOM + ": " + fields.get(RANDOM).getText());
			setVisible(false);
		});
		buttons.add(start);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> this.setVisible(false));
		buttons.add(cancel);
		add(buttons);
		pack();
		setLocationByPlatform(true);
	}

	private void encodeId() {
		Long hSizeVal = getFieldValue(H_SIZE);
		Long vSizeVal = getFieldValue(V_SIZE);
		Long mines = getFieldValue(MINES);
		Long random = getFieldValue(RANDOM);
		String unique = hSizeVal + "," + vSizeVal + "," + mines + "," + random;
		String id = Base64.getEncoder().encodeToString(unique.getBytes(StandardCharsets.ISO_8859_1));
		fields.get("ID").setText(id);
	}

	private boolean decodeId(String idBase) {
		String id = new String(Base64.getDecoder().decode(idBase), StandardCharsets.ISO_8859_1);
		String[] idValues = id.split(",");
		boolean result = false;

		if (idValues.length == 4) {
			setFieldValue(H_SIZE, idValues[0]);
			setFieldValue(V_SIZE, idValues[1]);
			setFieldValue(MINES, idValues[2]);
			setFieldValue(RANDOM, idValues[3]);
			result = true;
		}
		return result;
	}

	private void setFieldValue(String fieldName, String idValue) {
		long newVal = Long.parseLong(idValue);
		fields.get(fieldName).setText(Long.toString(newVal));
	}

	private Long getFieldValue(String fieldName) {
		return Long.parseLong(fields.get(fieldName).getText());
	}

	private void createInput(JPanel p, String name, String label, Long value, boolean numberOnly) {
		JLabel l = new JLabel(label);
		l.setHorizontalAlignment(JLabel.RIGHT);
		p.add(l);
		JTextField textField = new JTextField();
		textField.setMaximumSize(new Dimension(400, 100));
		textField.setText(Long.toString(value));

		if (numberOnly) {
			textField.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					encodeId();
				}
			});

			DocumentValueFilter.installFilter(textField, (oldVal, newVal) -> {
				try {
					Long.parseLong(newVal);
					return true;
				} catch (NumberFormatException e) {
					// Ignore
				}
				return false;
			});
		} else {
			DocumentValueFilter.installFilter(textField, (oldVal, newVal) -> {
				decodeId(newVal);
				return true;
			});
		}
		fields.put(name, textField);
		p.add(textField);
	}
}
