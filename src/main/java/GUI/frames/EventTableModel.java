package GUI.frames;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class EventTableModel extends AbstractTableModel {
    private final int columnCount = 6;
    private ArrayList<String []> dataArrayList; // данные таблицы


    public EventTableModel() {
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
            case 0 -> "Компонент";
            case 1 -> "Время";
            case 2 -> "Действие";
            case 3 -> "Успешно обработанные заявки";
            case 4 -> "Отказы";
            case 5 -> "Всего";
            default -> "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] rows = dataArrayList.get(rowIndex);
        return rows[columnIndex];
    }

    public void addData(String[] row) { // добавление строки с данными в табл
        String[] rowTable = new String[getColumnCount()];
        rowTable = row;
        dataArrayList.add(rowTable);
    }
}
