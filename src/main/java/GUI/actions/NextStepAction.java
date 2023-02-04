package GUI.actions;

import application.Controller;
import org.jetbrains.annotations.NotNull;
import findOptimalStatistic.statistic.StatisticController;
import utils.Action;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NextStepAction extends AbstractAction {
  private final JFrame stepModeFrame;
  private final Controller controller;
  public static Action event = null;
  static double time = 0;

  public NextStepAction(@NotNull final JFrame stepModeFrame, @NotNull final Controller controller) {
    this.stepModeFrame = stepModeFrame;
    this.controller = controller;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    event = controller.doStep();
    if (controller.getStatistics().getCompletedRequestsCount() == StatisticController.countOfRequiredRequests) {
      GetResultAction getResultAction = new GetResultAction(stepModeFrame, controller);
      getResultAction.actionPerformed(e);
      return;
    }
    time = Controller.currentTime();
    Graphics g = stepModeFrame.getContentPane().getGraphics();
    stepModeFrame.getContentPane().print(g);
  }
}
