package GUI.frames;

import application.Controller;
import findOptimalStatistic.Results;
import org.jetbrains.annotations.NotNull;
import findOptimalStatistic.statistic.ClientStatistic;
import findOptimalStatistic.statistic.StatisticController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class ResultFrame extends GUI.frames.CustomFrame {
  StatisticController statistics;

  public void setStatistics(@NotNull final StatisticController statistics) {
    this.statistics = statistics;
  }

  private double getMaxDeviceTime(Double[] doubles) {
    Double d = 0.0;
    for (Double aDouble : doubles) {
      if (aDouble > d) {
        d = aDouble;
      }
    }
    return d;
  }

  public void start() throws InterruptedException, IOException {
    currentFrame = createFrame("Auto mode");
    String[] columnNames = {"Кол-во заявок", "Вероятность отказа", "Ср. время заявок в системе",
      "Ср. время в буфере", "Ср. время обработки", "Дисперсия времени в буфере", "Дисперсия времени обработки", "Использование устройств"};

    String[][] data = new String[Controller.statistics.getClientsCount()][8];
    int i = 0;
    for (ClientStatistic s : statistics.getClientsStats()) {
      data[i][0] = String.valueOf(s.getGeneratedTasksCount());
      data[i][1] = String.valueOf((double) s.getCanceledTasksCount() / s.getGeneratedTasksCount());
      data[i][2] = String.valueOf(s.getTotalTime() / (s.getGeneratedTasksCount()));
      data[i][3] = String.valueOf(s.getTotalBufferedTime() / s.getGeneratedTasksCount());
      data[i][4] = String.valueOf(s.getTimeInWork() / (s.getGeneratedTasksCount()));
      data[i][5] = String.valueOf(s.getBufferDispersion());
      data[i][6] = String.valueOf(s.getDeviceDispersion());
      if (i < statistics.getDevicesCount()) {
        data[i][7] = String.valueOf(statistics.getDeviceTime()[i] / Controller.currentTime());
      }
      i++;
    }

    JTable table = new JTable(data, columnNames);
    JScrollPane scroll = new JScrollPane(table);
    table.setPreferredScrollableViewportSize(new Dimension(700, 200));
    currentFrame.getContentPane().add(scroll);
    currentFrame.setSize(1000, 500);
    currentFrame.revalidate();
    if (Results.minWork > 0.9 && Results.maxFail < 0.1) {
      int dev = StatisticController.countOfDevices;
      int siz = StatisticController.sizeOfBuffer;
      double mi = StatisticController.minimum;
      double ma = StatisticController.maximum;
      System.out.print(dev + " " + siz + " " + mi + " " + ma + "   ");
      System.out.println(dev * 10000 / (0.2 + mi) + 3000 * siz);
      System.out.println(Results.minWork);
      System.out.println(Results.maxFail);
    }
  }
}
