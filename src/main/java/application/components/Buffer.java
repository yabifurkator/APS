package application.components;

import application.Controller;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

@Getter
public class Buffer {
  private int countSigningUps;
  private final SigningUp[] signingUps;
  private int index;
  private final int size;
  private Integer[] batch; // рабочий массив

  public Buffer(final int size) {
    this.size = size;
    this.signingUps = new SigningUp[this.size];
    this.batch = new Integer[this.size];
    this.countSigningUps = 0;
    this.index = 0;
  }

  public boolean isEmpty() {
    return this.countSigningUps == 0;
  }

  public boolean isFull() {
    return this.size == this.countSigningUps;
  }

  public void add(@NotNull final SigningUp signingUp) {
    if (!isFull()) {
      while (this.signingUps[this.index] != null) {
        this.index = (this.index + 1) % this.size;
      }
      this.countSigningUps++;
    } else {
      this.batch[this.index] = null;
      SigningUp oldRequest = signingUps[index];
      Controller.statistics.taskCanceled(oldRequest.sportsmanId(), signingUp.startTime() - oldRequest.startTime());
    }
    this.signingUps[this.index] = signingUp;
    this.index = (this.index + 1) % this.size;
  }

  public SigningUp getRequest() {
    if (isEmpty()) {
      return null;
    }
    if (!Arrays.asList(batch).parallelStream().allMatch(Objects::isNull)) {
      double minStartTime = Double.MAX_VALUE;
      int indexSigningUp = 0;
      for (int i = 0; i < this.size; i++) {
        if (batch[i] != null && minStartTime > signingUps[i].startTime()) {
          minStartTime = signingUps[i].startTime();
          indexSigningUp = i;
          //batch[i] = null;
        }
      }
      batch[indexSigningUp] = null;
      SigningUp s = signingUps[indexSigningUp];
      signingUps[indexSigningUp] = null;
      this.countSigningUps--;
      return s;
    } else {
      int minVisitorId = Integer.MAX_VALUE;
      for (int i = 0; i < this.size; i++) { // минимальный visitor
        if (this.signingUps[i] != null && this.signingUps[i].sportsmanId() < minVisitorId) {
          minVisitorId = this.signingUps[i].sportsmanId();
        }
      }
      for (int i = 0; i < this.size; i++) { // заполнение рабочего массива
        if (this.signingUps[i] != null && this.signingUps[i].sportsmanId() == minVisitorId) {
          this.batch[i] = this.signingUps[i].sportsmanId();
        }
      }
      if (!Arrays.asList(batch).parallelStream().allMatch(Objects::isNull)) {
        double minStartTime = Double.MAX_VALUE;
        int indexSigningUp = 0;
        for (int i = 0; i < this.size; i++) {
          if (batch[i] != null && minStartTime > signingUps[i].startTime()) {
            indexSigningUp = i;
            minStartTime = signingUps[i].startTime();
          }
        }
        batch[indexSigningUp] = null;
        SigningUp s = signingUps[indexSigningUp];
        signingUps[indexSigningUp] = null;
        this.countSigningUps--;
        return s;
      }
      return null;
    }
  }
}
