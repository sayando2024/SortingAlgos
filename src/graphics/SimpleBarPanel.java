package graphics;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class SimpleBarPanel extends Application {

//    private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
//        Thread t = new Thread(runnable);
//        t.setDaemon(true);
//        return t;
//    });

    static XYChart.Series<String, Number> series;
    static BarChart<String, Number> bc;
    Button sort = new Button("Sort");

    public static void updateChart() {
        createSeries();
        bc.getData().removeAll();
        bc.getData().addAll(series);
    }

    //@Override
    public void start(Stage stage) {
        //Defining the axes
        CategoryAxis xAxis = new CategoryAxis();


        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("value");

        //Creating the Bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        bc = barChart;

        //Prepare XYChart.Series objects by setting data
        createSeries();

        //Setting the data to bar chart
        barChart.getData().addAll(series);

        //Creating a Group object
        Group root = new Group(barChart);

        //Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        //Setting title to the Stage
        stage.setTitle("Bar Chart");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }
    public static void main(String args[]) throws InterruptedException {
        //launch(args);
        NewThread t = new NewThread();
        t.start();
        System.out.println("I want to execute something here");
        launch(args);
    }

    public static void createSeries() {
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Index");
        for (int i = 0; i < 10; i++) {
//            xAxis.setCategories(FXCollections.<String>
//                    observableArrayList(Arrays.asList("Order")));
            Number randomNumber = (Math.random() * 10);
            series1.getData().add(new XYChart.Data<>("" + i, randomNumber));
        }

        series = series1;
    }

    public static class NewThread extends Thread {
        public void run() {
            long startTime = System.currentTimeMillis();
            int i = 0;
            while (true) {
                System.out.println(this.getName() + ": New Thread is running..." + i++);
                try {
                    //Wait for one sec so it doesn't print too fast
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}