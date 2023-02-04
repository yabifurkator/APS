package GUI.frames;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class BufferTableModel extends AbstractTableModel {
    private final int columnCount = 3;
    private ArrayList<String []> dataArrayList; // данные таблицы


    public BufferTableModel() {
        dataArrayList = new ArrayList<String[]>();
        for (int i = 0; i < dataArrayList.size(); i++) {
            dataArrayList.add(new String[getColumnCount()]); //проинициализировали табл
        }
    }

    @Override
    public int getRowCount() {
        return dataArrayList.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> "Указатель";
            case 1 -> "Индекс";
            case 2 -> "Заявка";
            default -> "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] rows = dataArrayList.get(rowIndex);
        return rows[columnIndex];
    }

    public void setData(int rowIndex, int columnIndex, String str) {
        String[] rows = dataArrayList.get(rowIndex);
        rows[columnIndex] = str;
        dataArrayList.set(rowIndex, rows);
    }

    public void addData(String[] row) { // добавление строки с данными в табл
        String[] rowTable = new String[getColumnCount()];
        rowTable = row;
        dataArrayList.add(rowTable);
    }
}
