import GUI.UI;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws InterruptedException, IOException {
    UI ui = new UI();
    ui.execute("2", "5", "0.1", "0.2", "10");
  }
}
