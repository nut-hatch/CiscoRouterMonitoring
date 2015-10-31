
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class ButtonColumn extends AbstractCellEditor implements
		TableCellRenderer, TableCellEditor, ActionListener {
	/**
	 * generated serialVersionUID.
	 */
	private static final long serialVersionUID = 1464704726225913774L;

	/**
	 * Die Tabelle.
	 */
	private JTable table;

	/**
	 * Die Action des Buttons.
	 */
	private Action action;

	/**
	 * JButton, Anzeige.
	 */
	private JButton renderButton;

	/**
	 * JButton, Action.
	 */
	private JButton editButton;

	/**
	 * editorValue.
	 */
	private Object editorValue;

	/**
	 * Erzeugt eine Buttoncolumn für eine angegebene Spalte.
	 * 
	 * @param table
	 *            die Tabelle, der ein Button hinzugefügt werden soll.
	 * @param action
	 *            die Action, die bei Klick ausgeführt werden soll.
	 * @param column
	 *            der Index der Spalte.
	 */
	public ButtonColumn(JTable table, Action action, int column) {
		if (column >= 0) {
			this.table = table;
			this.action = action;

			renderButton = new JButton();
			editButton = new JButton();
			editButton.setFocusPainted(false);
			editButton.addActionListener(this);

			TableColumnModel columnModel = table.getColumnModel();
			columnModel.getColumn(column).setCellRenderer(this);
			columnModel.getColumn(column).setCellEditor(this);
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (value == null)
			editButton.setText("");
		else
			editButton.setText(value.toString());
		this.editorValue = value;
		return editButton;
	}

	@Override
	public Object getCellEditorValue() {
		return editorValue;
	}

	/**
	 * CellRenderer Interface.
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value == null)
			renderButton.setText("");
		else
			renderButton.setText(value.toString());
		return renderButton;
	}

	/**
	 * ActionListener Interface.
	 */
	public void actionPerformed(ActionEvent e) {
		int row = table.convertRowIndexToModel(table.getEditingRow());

		ActionEvent event = new ActionEvent(table,
				ActionEvent.ACTION_PERFORMED, "" + row);
		action.actionPerformed(event);
		fireEditingStopped();
	}
}
