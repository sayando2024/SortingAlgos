package birthday;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javax.speech.*;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

import static javafx.application.Application.launch;

public class Table extends Application{
    static int numRounds;
    static int numPlayers;
    static int maxDraw;
    static int maxBonus;
    static XYChart.Series<String, Number>[] series;
    static XYChart.Series<String, Number> series1;
    static XYChart.Series<String, Number> series2;
    static String[] playerNames;
    static int[][] points;
    Player player = new Player("");

    static Scanner sc = new Scanner(System.in);

    private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    public void start(Stage stage) {
        //Defining the axes
        CategoryAxis xAxis = new CategoryAxis();

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Score");

        //Creating the Bar chart
        StackedBarChart<String, Number> barChart = new StackedBarChart<>(xAxis, yAxis);
        series = new XYChart.Series[numRounds];
        for (int i = 0; i < numRounds; i++) {
            series[i] = createSeries(playerNames, "Round " + (i+1));
        }

//        series1 = createSeries(playerNames, "Round 2");
//        series2 = createSeries(playerNames, "Round 3");
        //Prepare XYChart.Series objects by setting data


        //Setting the data to bar chart
        barChart.getData().addAll(series);
//        barChart.getData().addAll(series1);
//        barChart.getData().addAll(series2);
        Task<Void> showScoresTask = showRoundScores(series);
        //Creating a Group object
        BorderPane root = new BorderPane(barChart);

        HBox hbox = new HBox(0);
        hbox.getChildren().addAll(root);

        //Creating a scene object
        Scene scene = new Scene(hbox, 1000, 800);

        //Setting title to the Stage
        stage.setTitle("Leaderboard");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();

        try {
            exec.submit(showScoresTask);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Program executed successfully!");
        }
    }

    private static final String VOICES_KEY="freetts.voices";
    private static final String VOICE_VALUE="com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory";
    private static final String CENTRAL_DATA="com.sun.speech.freetts.jsapi.FreeTTSEngineCentral";
    private static Synthesizer sy;
    public static void main(String args[]) throws EngineException, AudioException, EngineStateError, IllegalArgumentException, InterruptedException {
        try {
            System.setProperty(VOICES_KEY, VOICE_VALUE);
            Central.registerEngineCentral(CENTRAL_DATA);
            sy = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            sy.allocate();
            sy.resume();

            textToSpeech("What is the greatest amount of points for the game?");
            maxDraw = sc.nextInt();
            textToSpeech("What can the greatest bonus be given?");
            maxBonus = sc.nextInt();
            textToSpeech("How many rounds of the game will be played?");
            numRounds = sc.nextInt();
            textToSpeech("How many players are participating?");
            numPlayers = sc.nextInt();
            playerNames = new String[numPlayers];

            for (int i = 0; i < numPlayers; i++) {
                textToSpeech("What is Player " + (i+1) + "'s name?");
                playerNames[i] = sc.next();
                textToSpeech("Welcome " + playerNames[i] + " to the 1st annual Cup Pong contest!");
            }
            textToSpeech("Best of luck to all players! Let's play hard");

            points = new int[numRounds][numPlayers];



//            playerTotalScores = new int[numPlayers];
//            for (int i = 0; i < numPlayers; i++) {
//                int sum = 0;
//                for (int j = 0; j < numRounds; j++) {
//                    sum += points[j][i];
//                }
//                playerTotalScores[i] = sum;
//            }
            launch(args);
            sy.waitEngineState(Synthesizer.QUEUE_EMPTY);
            sy.deallocate();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void textToSpeech(String data){
        try {
            System.out.println(data);
            sy.speakPlainText(data, null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static XYChart.Series<String,Number> createSeries(String[] s, String name) {
        XYChart.Series<String, Number> seriesL = new XYChart.Series<>();
        seriesL.setName(name);
        for (int i = 0; i < numPlayers; i++) {
            seriesL.getData().add(new XYChart.Data<>(s[i], 0));
        }

        return seriesL;
    }

    private Task<Void> showRoundScores(XYChart.Series<String, Number>[] seriesCur) {
        return new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                for (int i = 0; i < numRounds; i++) {
                    ObservableList<XYChart.Data<String, Number>> data = seriesCur[i].getData();
                    for (int j = 0; j < numPlayers; j++) {
                        textToSpeech("Hello " + playerNames[j] + ", what score did you earn in round " + (i + 1) + "?");
                        int pointsEarned = sc.nextInt();
                        if (pointsEarned > maxDraw || pointsEarned < 0) {
                            textToSpeech("Impossible, entered an invalid number, you lose your turn!");
                            pointsEarned = 0;
                        }
                        int bonus = player.getBonus();
                        points[i][j] = pointsEarned + bonus;

                        textToSpeech("You have earned an extra " + bonus + " points from a random lucky draw!");
                        textToSpeech("Your score for this round is " + points[i][j] + "!");
                        data.get(j).setYValue((int) data.get(j).getYValue() + points[i][j]);
                        int currentPlayerScore = 0;
                        for (int k = 0; k <= i; k++) {
                            currentPlayerScore += points[k][j];
                        }
                        //data.get(j).getNode().setLayoutX((double) data.get(j).getYValue() + points[i][j]);
//                        for (int x = 0; x < numRounds; x++) {
//                            //ObservableList<XYChart.Data<String, Number>> data1 = data;
//                            data.get(j).setXValue(playerNames[j] + " - " + currentPlayerScore);
//                        }

                        Thread.sleep(200);


                    }
                }
                int winner = 0;
                int winningTotal = 0;
                for (int i = 0; i < numPlayers; i++) {
                    int sum = 0;
                    for (int j = 0; j < numRounds; j++) {
                        sum += points[j][i];
                    }
                    if (sum > winningTotal) {
                        winningTotal = sum;
                        winner = i;
                    }
                }
                textToSpeech("Game has finished! Thanks to everyone for playing our 1st annual Cup Pong competition");
                textToSpeech("We are proud to announce the winner is " + playerNames[winner] + " with a total of " + winningTotal+ " points");
                return null;
            }
        };
    }
}

