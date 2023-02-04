package application.components;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import findOptimalStatistic.statistic.StatisticController;

import java.util.Random;

@Getter
@Setter
public class Coach {
  private int coachId;
  private SigningUp order;
  private double orderStartTime;
  private final StatisticController statistics;
  private Integer completedCount;

  public Coach(final int deviceId,
               @NotNull final StatisticController statistics) {
    this.coachId = deviceId;
    this.orderStartTime = 0;
    this.order = null;
    this.statistics = statistics;
    this.completedCount = 0;
  }

  public double getReleaseTime() {
    Random rand = new Random();
    double r = StatisticController.minimum + rand.nextDouble() *
               (StatisticController.maximum - StatisticController.minimum);
    statistics.getClientsStats().get(order.sportsmanId()).addTimeInWork(r);
    return r;
  }

  public boolean isFree() {
    return (order == null);
  }

  public void release(final double currentTime) {
    if (order != null) {
      statistics.taskCompleted(order.sportsmanId(),
        currentTime - orderStartTime,
        currentTime - orderStartTime);
      order = null;
      statistics.getDeviceTime()[coachId] += currentTime - orderStartTime;
      StatisticController.Total += currentTime - orderStartTime;
      orderStartTime = currentTime;
      completedCount++;
      StatisticController.countsOfCompleted[coachId] = completedCount;
    }
  }
}
