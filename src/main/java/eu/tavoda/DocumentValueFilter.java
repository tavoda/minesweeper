package eu.tavoda;

import javax.swing.*;
import javax.swing.text.*;
import java.util.function.BiFunction;

public class DocumentValueFilter extends DocumentFilter {
	private BiFunction<String, String, Boolean> valueTester;

	public static void installFilter(JTextField textField, BiFunction<String, String, Boolean> valueTester) {
		((PlainDocument) textField.getDocument()).setDocumentFilter(new DocumentValueFilter(valueTester));
	}

	public DocumentValueFilter(BiFunction<String, String, Boolean> valueTester) {
		this.valueTester = valueTester;
	}

	protected boolean testValue(String oldValue, String newValue) {
		return valueTester.apply(oldValue, newValue);
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		Document doc = fb.getDocument();
		String oldValue = doc.getText(0, doc.getLength());
		StringBuilder sb = new StringBuilder(oldValue);
		sb.insert(offset, string);
		if (testValue(oldValue, sb.toString())) {
			super.insertString(fb, offset, string, attr);
		}
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		Document doc = fb.getDocument();
		String oldValue = doc.getText(0, doc.getLength());
		StringBuilder sb = new StringBuilder(oldValue);
		sb.replace(offset, offset + length, text);
		if (testValue(oldValue, sb.toString())) {
			super.replace(fb, offset, length, text, attrs);
		}
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		Document doc = fb.getDocument();
		String oldValue = doc.getText(0, doc.getLength());
		StringBuilder sb = new StringBuilder(oldValue);
		sb.delete(offset, offset + length);
		if (testValue(oldValue, sb.toString())) {
			super.remove(fb, offset, length);
		}
	}
}