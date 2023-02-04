package application.components;

import org.jetbrains.annotations.NotNull;
import findOptimalStatistic.statistic.StatisticController;
import utils.Action;
import utils.ActionType;

import java.util.List;

public class WebSite {
  @NotNull
  final private Buffer buffer;
  @NotNull
  Sportsman[] sportsmen;
  @NotNull
  private final StatisticController statistics;

  private Server server;

  public WebSite(@NotNull final Buffer buffer,
                 @NotNull final Sportsman[] customers,
                 @NotNull final StatisticController statistics,
                 @NotNull final Server server) {
    this.buffer = buffer;
    this.sportsmen = customers;
    this.statistics = statistics;
    this.server = server;
  }

  public List<Action> sendOrderToBuffer(final int currentId, final double currentTime) {
    buffer.add(sportsmen[currentId].sendSigningUp(currentTime));
    List<Action> events = List.of(
      new Action(ActionType.Generated, currentTime + sportsmen[currentId].getTimeForGeneratedOrder(), currentId),
      new Action(ActionType.Unbuffered, currentTime));
    statistics.orderGenerated(currentId);
    return events;
  }

  public List<Action> sendOrderToServer(final int currentId, final double currentTime) {
    return server.sendOrderToBuffer(currentId, currentTime, this.sportsmen);
  }
}
