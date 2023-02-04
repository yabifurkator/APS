package GUI.frames;

import application.components.Buffer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableChangeColorBufferBatch extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

        if (value.toString().contains("batch") && column == 2) {
            c.setText(value.toString().substring(0, value.toString().length()-5));
            c.setBackground(Color.LIGHT_GRAY);
        }
        else c.setBackground(new JLabel().getBackground());
        return c;
    }
}
