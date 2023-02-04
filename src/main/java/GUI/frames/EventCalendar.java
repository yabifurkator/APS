package GUI.frames;

import GUI.actions.GetResultAction;
import application.Controller;
import application.components.SigningUp;
import findOptimalStatistic.statistic.ClientStatistic;
import utils.Action;
import utils.ActionType;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class EventCalendar extends CustomFrame {
    private JScrollPane scrollPane;
    private JScrollPane scrollPaneBuffer;
    private JScrollPane scrollPaneDevice;
    private final Controller controller;
    public static Action event = null;
    private Integer oldCanceledTask;
    private Integer oldCompletedRequestCount;

    public EventCalendar(Controller controller) {
        this.controller = controller;
        oldCanceledTask = 0;
        oldCompletedRequestCount = 0;
    }

    public void createTable() {
        EventTableModel etm = new EventTableModel();
        JTable eventTable = new JTable(etm);
        scrollPane = new JScrollPane(eventTable);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        scrollPane.setBounds(0, 0, 1000, 400);
        currentFrame = createFrame("Event calendar");
        currentFrame.setLayout(null);
        currentFrame.add(scrollPane);
        currentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        BufferTableModel btm = new BufferTableModel();
        JTable bufferTable = new JTable(btm);
        TableColumn tColumn = bufferTable.getColumnModel().getColumn(0);
        tColumn.setCellRenderer(new TableChangeColorBuffer());
        bufferTable.setDefaultRenderer(Object.class, new TableChangeColorBufferBatch());

        JLabel label1 = new JLabel("Буфер");
        label1.setBounds(10, 400, 100, 30);
        currentFrame.add(label1);
        scrollPaneBuffer = new JScrollPane(bufferTable);
        scrollPaneBuffer.setPreferredSize(new Dimension(200, 200));
        scrollPaneBuffer.setBounds(0, 430, 1000, 385);
        currentFrame.add(scrollPaneBuffer);

        JLabel label2 = new JLabel("Приборы");
        label2.setBounds(1010, 400, 100, 30);
        currentFrame.add(label2);
        DeviceTableModel dtm = new DeviceTableModel();
        JTable deviceTable = new JTable(dtm);
        deviceTable.setDefaultRenderer(Object.class, new TableChangeColorDevice());
        scrollPaneDevice = new JScrollPane(deviceTable);
        scrollPaneDevice.setPreferredSize(new Dimension(200, 200));
        scrollPaneDevice.setBounds(1000, 430, 500, 385);
        currentFrame.add(scrollPaneDevice);

        for (int i = 0; i < controller.getBuffer().getSize(); i++) {
            String[] str = {"", String.valueOf(i), ""};
            btm.addData(str);
        }

        for (int i = 0; i < controller.getWorkers().length; i++) {
            String[] str = {String.valueOf(i), "" };
            dtm.addData(str);
        }

        JButton button = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.listComponents.isEmpty()) {
                    event = controller.doStep();
                }
                if (event.getActionType() == ActionType.Generated) {
                    ArrayList<ClientStatistic> clientsStats = controller.getStatistics().getClientsStats();
                    Integer canselTasks = 0;
                    for (ClientStatistic cl : clientsStats) {
                        canselTasks += cl.getCanceledTasksCount();
                    }
                    if (controller.listComponents.get(0) == "Sportsman") {
                        String[] str = {controller.listComponents.get(0) + " - " + event.id,
                                event.getActionTime().toString(),
                                event.getActionType().toString(),
                                String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                                oldCanceledTask.toString(),
                                String.valueOf(oldCompletedRequestCount)};
                        etm.addData(str);
                        controller.listComponents.remove(0);
                        if(canselTasks != oldCanceledTask) {
                            oldCanceledTask = canselTasks;
                            oldCompletedRequestCount = controller.getStatistics().getCompletedRequestsCount();
                        }
                    } else if (controller.listComponents.get(0) == "Server") {
                        String[] str = {"Server",
                                event.getActionTime().toString(),
                                "Send to buffer",
                                String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                                oldCanceledTask.toString(),
                                String.valueOf(oldCompletedRequestCount)};
                        etm.addData(str);
                        controller.listComponents.remove(0);

                        for (int i = 0; i < controller.getBuffer().getSize(); i++) {
                            SigningUp signingUp = controller.getBuffer().getSigningUps()[i];
                            btm.setData(i, 0,"");
                            if (signingUp == null) {
                                btm.setData(i, 2, "");
                            } else {
                                if (controller.getBuffer().getBatch()[i] != null) {
                                    btm.setData(i, 2, signingUp.orderId() + " batch");
                                } else {
                                    btm.setData(i, 2, signingUp.orderId());
                                }
                            }
                        }
                        btm.setData(controller.getBuffer().getIndex(), 0, "*");
                        if (controller.getBuffer().getIndex() == 0) {
                            btm.setData(controller.getBuffer().getSize() - 1, 0, "");
                        } else {
                            btm.setData(controller.getBuffer().getIndex() - 1, 0, "");
                        }
                        currentFrame.repaint();
                    } else if (controller.listComponents.get(0) == "WebSite"){
                        String[] str = {controller.listComponents.get(0),
                                event.getActionTime().toString(),
                                "Send to server",
                                String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                                canselTasks.toString(),
                                String.valueOf(controller.getStatistics().getCompletedRequestsCount())};
                        etm.addData(str);
                        controller.listComponents.remove(0);
                        currentFrame.repaint();
                    }
                }

                if (event.getActionType() == ActionType.Unbuffered) {
                    ArrayList<ClientStatistic> clientsStats = controller.getStatistics().getClientsStats();
                    Integer canselTasks = 0;
                    for (ClientStatistic cl : clientsStats) {
                        canselTasks += cl.getCanceledTasksCount();
                    }
                    if (controller.listComponents.get(0) == "Administrator") {
                        String[] str = {controller.listComponents.get(0),
                                event.getActionTime().toString(),
                                "Request to server",
                                String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                                canselTasks.toString(),
                                String.valueOf(controller.getStatistics().getCompletedRequestsCount())};
                        etm.addData(str);
                        controller.listComponents.remove(0);
                    } else if (controller.listComponents.get(0) == "Administrator to coach") {
                        String[] str = {"Administrator",
                                event.getActionTime().toString(),
                                "Send to coach",
                                String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                                canselTasks.toString(),
                                String.valueOf(controller.getStatistics().getCompletedRequestsCount())};
                        etm.addData(str);
                        controller.listComponents.remove(0);

                        for (int i = 0; i < controller.getWorkers().length; i++) {
                            SigningUp signingUp = controller.getWorkers()[i].getOrder();
                            if (signingUp == null) {
                                dtm.setData(i, 1, "");
                            } else {
                                dtm.setData(i, 1, signingUp.orderId());
                            }
                        }
                        currentFrame.repaint();
                    } else if (controller.listComponents.get(0) == "Buffer") {
                        String[] str = {controller.listComponents.get(0),
                                event.getActionTime().toString(),
                                "Send to server",
                                String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                                canselTasks.toString(),
                                String.valueOf(controller.getStatistics().getCompletedRequestsCount())};
                        etm.addData(str);
                        controller.listComponents.remove(0);

                        for (int i = 0; i < controller.getBuffer().getSize(); i++) {
                            SigningUp signingUp = controller.getBuffer().getSigningUps()[i];
                            if (signingUp == null) {
                                btm.setData(i, 2, "");
                            } else if (controller.getBuffer().getBatch()[i] != null) {
                                btm.setData(i, 2, signingUp.orderId() + " batch");
                            } else {
                                btm.setData(i, 2, signingUp.orderId());
                            }
                        }
                        currentFrame.repaint();
                    } else if (controller.listComponents.get(0) == "Error buffer") {
                        String[] str = {"Buffer",
                                event.getActionTime().toString(),
                                "Buffer is empty",
                                String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                                canselTasks.toString(),
                                String.valueOf(controller.getStatistics().getCompletedRequestsCount())};
                        etm.addData(str);
                        controller.listComponents.remove(0);
                    } else if (controller.listComponents.get(0) == "Error administrator") {
                        String[] str = {"Administrator",
                                event.getActionTime().toString(),
                                "All coaches are busy",
                                String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                                canselTasks.toString(),
                                String.valueOf(controller.getStatistics().getCompletedRequestsCount())};
                        etm.addData(str);
                        controller.listComponents.remove(0);
                    } else if (controller.listComponents.get(0) == "Server to buffer") {
                        String[] str = {"Server",
                                event.getActionTime().toString(),
                                "Request to buffer",
                                String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                                canselTasks.toString(),
                                String.valueOf(controller.getStatistics().getCompletedRequestsCount())};
                        etm.addData(str);
                        controller.listComponents.remove(0);
                    } else if (controller.listComponents.get(0) == "Server to administrator") {
                        String[] str = {"Server",
                                event.getActionTime().toString(),
                                "Send to administrator",
                                String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                                canselTasks.toString(),
                                String.valueOf(controller.getStatistics().getCompletedRequestsCount())};
                        etm.addData(str);
                        controller.listComponents.remove(0);
                    }
                }

                if (event.getActionType() == ActionType.Completed) {
                    ArrayList<ClientStatistic> clientsStats = controller.getStatistics().getClientsStats();
                    Integer canselTasks = 0;
                    for (ClientStatistic cl : clientsStats) {
                        canselTasks += cl.getCanceledTasksCount();
                    }
                    String[] str = {controller.listComponents.get(0) + " - " + event.id,
                            event.getActionTime().toString(),
                            event.getActionType().toString(),
                            String.valueOf((controller.getStatistics().getCompletedRequestsCount() - canselTasks)),
                            canselTasks.toString(),
                            String.valueOf(controller.getStatistics().getCompletedRequestsCount())};
                    etm.addData(str);
                    controller.listComponents.remove(0);

                    for (int i = 0; i < controller.getWorkers().length; i++) {
                        SigningUp signingUp = controller.getWorkers()[i].getOrder();
                        if (signingUp == null) {
                            dtm.setData(i, 1, "");
                        } else {
                            dtm.setData(i, 1, signingUp.orderId());
                        }
                    }
                    currentFrame.repaint();
                }

                JTable newEventTable = new JTable(etm);
                JScrollPane newEventTableScrollPane = new JScrollPane(newEventTable);
                newEventTableScrollPane.setPreferredSize(new Dimension(400, 1000));
                newEventTableScrollPane.setBounds(0, 0, 1000, 400);
                currentFrame.remove(scrollPane);
                currentFrame.add(newEventTableScrollPane);
                scrollPane = newEventTableScrollPane;

            }
        });
        button.setText("Далее");
        currentFrame.add(button).setBounds(1100, 25, 115, 30);

        JButton buttonResults = new JButton(new GetResultAction(currentFrame, controller));
        buttonResults.setText("Результаты");
        currentFrame.add(buttonResults).setBounds(1100, 75, 115, 30);
    }
}
