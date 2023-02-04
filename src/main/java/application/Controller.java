package application;

import application.components.*;
import lombok.Getter;
import findOptimalStatistic.statistic.StatisticController;
import utils.Action;
import utils.ActionType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Controller {
  public static StatisticController statistics;
  private final int requiredOrdersCount = StatisticController.countOfRequiredRequests;
  private static double currentTime;
  private final Buffer buffer;
  private final WebSite terminal;
  private final Administrator manager;
  private final Sportsman[] customers;
  private final Coach[] workers;
  private ArrayList<Action> actions;
  public ArrayList<String> listComponents = new ArrayList<String>();
  private final Server server;

  public static double currentTime() {
    return currentTime;
  }

  private void initAction() {
    actions = new ArrayList<>();
    for (int i = 0; i < statistics.getClientsCount(); i++) {
      actions.add(new Action(ActionType.Generated, customers[i].getTimeForGeneratedOrder(), i));
    }
    if (actions.size() > 0) {
      actions.sort(Action::compare);
    }
  }

  public Controller() {
    currentTime = 0;
    statistics = StatisticController.getInstance();
    buffer = new Buffer(statistics.getBufferSize());
    workers = new Coach[statistics.getDevicesCount()];
    for (int i = 0; i < statistics.getDevicesCount(); i++) {
      workers[i] = new Coach(i, statistics);
    }
    customers = new Sportsman[statistics.getClientsCount()];
    for (int i = 0; i < statistics.getClientsCount(); i++) {
      customers[i] = new Sportsman(i);
    }
    server = new Server(buffer);
    manager = new Administrator(buffer, server, workers);
    terminal = new WebSite(buffer, customers, statistics, server);
    initAction();
  }

  public void auto() {
    while (!actions.isEmpty()) {
      doStep();
    }
  }

  public Action doStep() {
    if (actions.isEmpty())
      return null;
    final Action currentEvent = actions.remove(0);
    final ActionType currentType = currentEvent.actionType;
    final int currentId = currentEvent.id;
    currentTime = currentEvent.actionTime;
    if (currentType == ActionType.Generated) {
      listComponents.add("Sportsman");
      listComponents.add("WebSite");
      listComponents.add("Server");
      if (statistics.getTotalRequestsCount() < requiredOrdersCount) {
        List<Action> newEvents = terminal.sendOrderToServer(currentId, currentTime);
        if (!actions.isEmpty() || currentId == 0) {
          actions.addAll(newEvents);
          actions.sort(Action::compare);
        }
      }
    } else if (currentType == ActionType.Unbuffered) {
      listComponents.add("Administrator");
      listComponents.add("Server to buffer");

      final Action newEvent = manager.requestOrderToServer(currentTime);
      if (newEvent.actionType == ActionType.Completed) {
        actions.add(newEvent);
        actions.sort(Action::compare);
        listComponents.add("Buffer");
        listComponents.add("Server to administrator");
        listComponents.add("Administrator to coach");
      } else if (newEvent.actionType == ActionType.Buffer_error){
        listComponents.add("Error buffer");
      } else if (newEvent.actionType == ActionType.Administrator_error){
        listComponents.add("Buffer");
        listComponents.add("Server to administrator");
        listComponents.add("Error administrator");
      }

    } else if (currentType == ActionType.Completed) {
      listComponents.add("Coach");
      workers[currentId].release(currentTime);
      actions.add(new Action(ActionType.Unbuffered, currentTime));
      actions.sort(Action::compare);
    }
    return currentEvent;
  }

  public StatisticController getStatistics() {
    return statistics;
  }
}

