package application.components;

import org.jetbrains.annotations.NotNull;

public record SigningUp(int sportsmanId,
                        @NotNull String orderId,
                        double startTime) {
}
