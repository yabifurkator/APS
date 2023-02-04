package GUI.actions;

import GUI.frames.EventCalendar;
import application.Controller;
import org.jetbrains.annotations.NotNull;
import findOptimalStatistic.statistic.StatisticController;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class SetDataAction extends AbstractAction {
  @NotNull
  private ArrayList<JTextField> startDataFields;
  @NotNull
  private final JFrame startFrame;

  public SetDataAction(@NotNull final JFrame startFrame, @NotNull final ArrayList<JTextField> array) {
    this.startFrame = startFrame;
    this.startDataFields = array;
  }

  @Override
  public void actionPerformed(@NotNull final ActionEvent e) {
    StatisticController.countOfDevices = Integer.parseInt(startDataFields.get(0).getText());
    StatisticController.countOfClients = Integer.parseInt(startDataFields.get(1).getText());
    StatisticController.countOfRequiredRequests = Integer.parseInt(startDataFields.get(2).getText());
    StatisticController.sizeOfBuffer = Integer.parseInt(startDataFields.get(3).getText());
    StatisticController.minimum = Double.parseDouble(startDataFields.get(4).getText());
    StatisticController.maximum = Double.parseDouble(startDataFields.get(5).getText());
    StatisticController.lambda = Double.parseDouble(startDataFields.get(6).getText());
    startFrame.setVisible(false);
    Controller controller = new Controller();
    EventCalendar eventCalendar = new EventCalendar(controller);
    eventCalendar.createTable();
  }
}
