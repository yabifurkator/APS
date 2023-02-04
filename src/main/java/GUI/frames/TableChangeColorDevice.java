package GUI.frames;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableChangeColorDevice extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

        if(column==0) c.setHorizontalAlignment(CENTER);

        if(value == "" && column==1) {
            c.setBackground(Color.green);
            c.setHorizontalAlignment(CENTER);
            c.setText("Свободно");
        }
        else c.setBackground(new JLabel().getBackground());
        return c;
    }
}
