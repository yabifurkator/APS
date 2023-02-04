package GUI.frames;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableChangeColorBuffer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

        if(column==1 || column == 2) c.setHorizontalAlignment(CENTER);

        if(value != null && column==2) {
            c.setBackground(Color.PINK);
        }

        if(value == "*" && column==0) {
            c.setBackground(Color.ORANGE);
            c.setHorizontalAlignment(CENTER);
        }
        else c.setBackground(new JLabel().getBackground());
        return c;
    }
}
