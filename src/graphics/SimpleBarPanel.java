package graphics;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class SimpleBarPanel extends Application {

    private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

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
        
        barChart.setBarGap(0);

       // Task<Void> sortingAlgoTask = createBubleSortingTask(series);
        Task<Void> sortingAlgoTask = createInsertSortingTask(series);
        
        exec.submit(sortingAlgoTask);
        
        //Creating a Group object
        BorderPane root = new BorderPane(barChart);
        
       
        //Creating a scene object
        Scene scene = new Scene(root, 1000, 800);

        //Setting title to the Stage
        stage.setTitle("Bar Chart");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    private Task<Void> createBubleSortingTask(Series<String, Number> series) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                ObservableList<Data<String, Number>> data = series.getData();
                for (int i = data.size() - 1; i >= 0; i--) {
                    for (int j = 0 ; j < i; j++) {

                        Data<String, Number> first = data.get(j);
                        Data<String, Number> second = data.get(j + 1);

                       Thread.sleep(50);

                       first.getNode().setStyle("-fx-background-color: green ;");
                       second.getNode().setStyle("-fx-background-color: green ;");
                       
                        if (first.getYValue().doubleValue() > second.getYValue().doubleValue()) {
                            Number temp = first.getYValue().doubleValue();
                            first.setYValue(second.getYValue().doubleValue());
                            second.setYValue(temp);
                        }
                        Thread.sleep(500);
                        
                        first.getNode().setStyle("-fx-background-color: red ;");
                        second.getNode().setStyle("-fx-background-color: red ;");

                    }
                }
                return null;
            }
        };
    }
    
    private Task<Void> createInsertSortingTask(Series<String, Number> series) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                ObservableList<Data<String, Number>> data = series.getData();
                
                int n = data.size();  
                for (int j = 1; j < n; j++) {  
                    Double key = data.get(j).getYValue().doubleValue(); 
                    int i = j-1; 
                    
                    Data<String, Number> first = data.get(j);
                    Data<String, Number> second = data.get(j - 1);

                   Thread.sleep(50);

                   first.getNode().setStyle("-fx-background-color: green ;");
                   //second.getNode().setStyle("-fx-background-color: green ;");
                   
                    while ( (i > -1) && (data.get(i).getYValue().doubleValue() > key ) ) { 
                    	Data<String, Number> currentFirst = data.get(i);
                    	currentFirst.getNode().setStyle("-fx-background-color: green ;");
                    	data.get(i+1).setYValue(data.get(i).getYValue());  
                        i--;  
                    }  
                    
                    data.get(i+1).setYValue(key);
                    
                    Thread.sleep(500);
                    
                    first.getNode().setStyle("-fx-background-color: red ;");
                   // second.getNode().setStyle("-fx-background-color: red ;");
                }  
                
                return null;
            }
        };
    }
    
    public static void createSeries() {
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Index");
        for (int i = 0; i < 50; i++) {
//            xAxis.setCategories(FXCollections.<String>
//                    observableArrayList(Arrays.asList("Order")));
            Number randomNumber = (Math.random() * 100);
            series1.getData().add(new XYChart.Data<>("" + i, randomNumber));
        }

        series = series1;
    }

    
    public static void main(String args[]) throws InterruptedException {
        launch(args);
       // NewThread t = new NewThread();
       // t.start();
       // System.out.println("I want to execute something here");
       // launch(args);
    }

}