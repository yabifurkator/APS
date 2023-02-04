package application.components;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import utils.Action;
import utils.ActionType;

import java.util.Random;

@Getter
public class Administrator {
  private final Coach[] coaches;
  private final Buffer buffer;
  private final Server server;
  private final Random generator;
  private final double maxRand;

  public Administrator(@NotNull final Buffer buffer,
                       final Server server,
                       @NotNull final Coach[] devices) {
    this.buffer = buffer;
    this.coaches = devices;
    this.generator = new Random();
    this.maxRand = generator.nextDouble();
    this.server = server;
  }

  public void setOrderInDevice(int numDevice, SigningUp request) {
    coaches[numDevice].setOrder(request);
  }

  public Action sendOrderToDevice(final double currentTime) {
    int freeCoachIndex = findFreeDeviceIndex();
    Coach currentCoach = coaches[freeCoachIndex];
    if (currentCoach.isFree() && !buffer.isEmpty()) {
      final SigningUp order = buffer.getRequest();
      setOrderInDevice(currentCoach.getCoachId(), order);
      coaches[freeCoachIndex].setOrderStartTime(currentTime);
      return new Action(ActionType.Completed,
        currentTime + coaches[freeCoachIndex].getReleaseTime(),
        currentCoach.getCoachId());
    }
    if (!currentCoach.isFree()){
      return new Action(ActionType.Administrator_error,
               0,
              -1);
    }
    if (buffer.isEmpty()){
      return new Action(ActionType.Buffer_error,
              0,
              -1);
    }
    return null;
  }

  public Action requestOrderToServer(final double currentTime) {
    int freeCoachIndex = findFreeDeviceIndex();
    Coach currentCoach = coaches[freeCoachIndex];
    if (currentCoach.isFree() && !buffer.isEmpty()) {
      final SigningUp order = server.getRequest(); // в сервер
      setOrderInDevice(currentCoach.getCoachId(), order);
      coaches[freeCoachIndex].setOrderStartTime(currentTime);
      return new Action(ActionType.Completed,
              currentTime + coaches[freeCoachIndex].getReleaseTime(),
              currentCoach.getCoachId());
    }
    if (!currentCoach.isFree()){
      return new Action(ActionType.Administrator_error,
              0,
              -1);
    }
    if (buffer.isEmpty()){
      return new Action(ActionType.Buffer_error,
              0,
              -1);
    }
    return null;
  }

  private int findFreeDeviceIndex() {
    int ind = 0;
    while (!coaches[ind].isFree() && ind < coaches.length - 1) {
      ind++;
    }
    return ind;
  }
}
