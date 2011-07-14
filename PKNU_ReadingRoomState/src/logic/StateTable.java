package logic;

import javax.swing.table.AbstractTableModel;

import data.State;

public class StateTable extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private ParseState parseState;
	
	public StateTable() {
		parseState = new ParseState();
		parseState.parse();
	}
	
	public void refreshStateTable() {
		parseState.parse();
	}
	
	public String getDate() {
		return parseState.getDate();
	}
	
	public String getStateTableByString() {
		String stateTableByString = "³¯Â¥ : " + parseState.getDate();
		
		for (State state : parseState.getStateList()) {
			stateTableByString += ("\n" + state.getStateByString());
		}
			
		return stateTableByString;
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return parseState.getStateList().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		State target = parseState.getStateList().get(rowIndex);
		Object result = null;
		
		switch (columnIndex) {
		case 0:
			result = target.getName();
			break;
		case 1:
			result = target.getTotal();
			break;
		case 2:
			result = target.getUse();
			break;
		case 3:
			result = target.getRemain();
			break;
		case 4:
			result = String.format("%,.2f", target.getRate()) + " %";
			break;
		}
		
		return result;
	}
}
