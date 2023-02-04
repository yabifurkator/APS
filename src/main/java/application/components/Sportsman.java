package application.components;

import lombok.Getter;

import static findOptimalStatistic.statistic.StatisticController.lambda;

@Getter
public class Sportsman {
  final private Integer customerId;
  private final String name;
  private final String email;
  private Integer requestNumber;

  public Sportsman(final int customerId) {
    this.customerId = customerId;
    this.name = "Name_customer" + customerId;
    this.email = "Email_customer" + customerId + "@yyy";
    requestNumber = 0;
  }

  public SigningUp sendSigningUp(final double currentTime) {
    requestNumber++;
    return new SigningUp(customerId, String.valueOf(customerId) + '-' + requestNumber, currentTime);
  }

  public double getTimeForGeneratedOrder() {
    return (-1.0 / lambda) * Math.log(Math.random());
  }
}
