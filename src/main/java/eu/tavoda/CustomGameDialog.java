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
	private static final int INSET_VAL = 2;
	private static final Insets insets = new Insets(INSET_VAL, INSET_VAL, INSET_VAL, INSET_VAL);

	public static final String H_SIZE = "hSize";
	public static final String V_SIZE = "vSize";
	public static final String MINES = "mines";
	public static final String RANDOM = "random";
	public static final String ID = "ID";
	GridBagConstraints labelConstraints = new GridBagConstraints();
	GridBagConstraints fieldConstraints = new GridBagConstraints();
	Map<String, JTextComponent> fields = new HashMap<>();
	Minesweeper minesweeper;

	public CustomGameDialog(Minesweeper parent) {
		fieldConstraints.fill = GridBagConstraints.BOTH;
		fieldConstraints.weightx = 2;
		fieldConstraints.gridwidth = GridBagConstraints.REMAINDER;
		fieldConstraints.insets = insets;
		labelConstraints.fill = GridBagConstraints.BOTH;
		labelConstraints.insets = insets;
		minesweeper = parent;

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setTitle("Custom game");
		GridBagLayout gbl = new GridBagLayout();
		JPanel panel = new JPanel(gbl);
		createInput(panel, H_SIZE, "Horizontal size", 40L, true);
		createInput(panel, V_SIZE, "Vertical size", 20L, true);
		createInput(panel, MINES, "Mines", 120L, true);
		createInput(panel, RANDOM, "Random", Math.abs((new Random()).nextLong()), true);
		JButton newRandomBtn = new JButton("New random");
		newRandomBtn.addActionListener(e -> {
			fields.get(RANDOM).setText(Long.toString(Math.abs((new Random()).nextLong())));
			encodeId();
		});
		gbl.setConstraints(newRandomBtn, fieldConstraints);
		panel.add(newRandomBtn);
		createInput(panel, ID, "ID", 0L, false);
		encodeId();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		Dimension d = panel.getPreferredSize();
		d.width = 600;
		panel.setMaximumSize(d);

		add(panel, BorderLayout.CENTER);
		add(Box.createVerticalGlue(), BorderLayout.NORTH);

		JPanel buttons = getButtonsPanel();
		add(buttons);
		pack();
		setLocationByPlatform(true);
	}

	private JPanel getButtonsPanel() {
		JPanel buttons = new JPanel();
		JButton start = new JButton("New game");
		start.addActionListener(e -> {
			Long rows = getFieldValue(V_SIZE);
			Long cols = getFieldValue(H_SIZE);
			Long mines = getFieldValue(MINES);
			Long random = getFieldValue(RANDOM);
			minesweeper.newGame(rows.intValue(), cols.intValue(), mines.intValue(), random);
			setVisible(false);
		});
		buttons.add(start);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> this.setVisible(false));
		buttons.add(cancel);
		return buttons;
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
		String base = idBase.trim();
		String id;
		try {
			id = new String(Base64.getDecoder().decode(base), StandardCharsets.ISO_8859_1);
		} catch(IllegalArgumentException e) {
			try {
				id = new String(Base64.getDecoder().decode(base + "="), StandardCharsets.ISO_8859_1);
			} catch(IllegalArgumentException e2) {
				id = new String(Base64.getDecoder().decode(base + "=="), StandardCharsets.ISO_8859_1);
			}
		}
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
		GridBagLayout gbl = (GridBagLayout) p.getLayout();
		JLabel l = new JLabel(label + ":");
		l.setHorizontalAlignment(JLabel.RIGHT);
		l.setHorizontalTextPosition(JLabel.RIGHT);
		gbl.setConstraints(l, labelConstraints);
		p.add(l);
		JTextField textField = new JTextField();
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
					if (!newVal.isBlank()) {
						Long.parseLong(newVal);
					}
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
		gbl.setConstraints(textField, fieldConstraints);
		p.add(textField);
	}
}
