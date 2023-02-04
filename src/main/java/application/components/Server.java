package application.components;

import lombok.Data;
import utils.Action;
import utils.ActionType;

import java.util.List;

import static application.Controller.statistics;

@Data
public class Server {
    private SigningUp signingUp;
    private Buffer buffer;

    public Server(Buffer buffer) {
        this.buffer = buffer;
    }

    public List<Action> sendOrderToBuffer(final int currentId, final double currentTime, Sportsman[] sportsmen) {
        buffer.add(sportsmen[currentId].sendSigningUp(currentTime));
        List<Action> events = List.of(
                new Action(ActionType.Generated, currentTime + sportsmen[currentId].getTimeForGeneratedOrder(), currentId),
                new Action(ActionType.Unbuffered, currentTime));
        statistics.orderGenerated(currentId);
        return events;
    }

    public SigningUp getRequest(){
        return buffer.getRequest();
    }
}
