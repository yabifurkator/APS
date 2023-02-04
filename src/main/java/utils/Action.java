package utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Action {
  public ActionType actionType;
  public Double actionTime;
  public int id;

  public Action(ActionType type, double time, int id) {
    this.actionType = type;
    this.actionTime = time;
    this.id = id;
  }

  public Action(ActionType type, double time) {
    this.actionType = type;
    this.actionTime = time;
    this.id = -1;
  }

  public static int compare(Action l, Action r) {
    if (l.actionTime < r.actionTime) {
      return -1;
    } else if (l.actionTime > r.actionTime) {
      return 1;
    } else if (l.actionType.ordinal() > r.actionType.ordinal()) {
      return -1;
    }
    return 0;
  }
}
