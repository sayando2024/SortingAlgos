package birthday;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
    static TableView tableView;
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
        xAxis.setTickLabelFont(new Font(30));
        xAxis.setTickLength(20.0);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Score");
        yAxis.setTickLabelFont(new Font(15));
        yAxis.setTickLength(20.0);

        //Creating the Bar chart
        StackedBarChart<String, Number> barChart = new StackedBarChart<>(xAxis, yAxis);
        barChart.setPrefWidth(10000.0);
        barChart.setPrefHeight(50.0);

        series = new XYChart.Series[numRounds];
        for (int i = 0; i < numRounds; i++) {
            series[i] = createSeries(playerNames, "Round " + (i+1));
        }

//        series1 = createSeries(playerNames, "Round 2");
//        series2 = createSeries(playerNames, "Round 3");
        //Prepare XYChart.Series objects by setting data
//        String[] columnNames = {"Player Name", "Score"};
        //Creating a Group object
//        StackPane root1 = new StackPane();
//        tableView = new TableView();
//        for (int i = 0; i < playerNames.length; i++) {
//            TableColumn tc = new TableColumn(playerNames[i]);
//            tableView.getColumns().addAll(tc);
//        }
//        root1.getChildren().add(tableView);
        //Setting the data to bar chart
        barChart.getData().addAll(series);
//        barChart.getData().addAll(series1);
//        barChart.getData().addAll(series2);
        Task<Void> showScoresTask = showRoundScores(series);

        BorderPane root = new BorderPane(barChart);

        HBox hbox = new HBox(0);
        hbox.getChildren().addAll(root);

        //Creating a scene object
        Scene scene = new Scene(hbox, 1000, 2000);

        //Setting title to the Stage
        stage.setTitle("1st Annual Cup Pong Competition Scoreboard");

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
    private static TestVoice tv;
    public static void main(String args[]) throws EngineException, AudioException, EngineStateError, IllegalArgumentException, InterruptedException {
        try {
            System.setProperty(VOICES_KEY, VOICE_VALUE);
            Central.registerEngineCentral(CENTRAL_DATA);
//            sy = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
//            sy.allocate();
            tv = new TestVoice("kevin16");
//            sy.resume();
            String[] questionJokes = {"What did one wall say to the other wall? I will meet you at the corner!",
                    "I always wanted to be a doctor, but I didn't have the patients.",
                    "I'm on a seafood diet: When I see food, I eat it.",
                    "I tried to do my homework but my pencil broke, so it was pointless.",
                    "I ate a clock yesterday, it was very time-consuming.",
                    "I've just written a song about tortillas; actually, it’s more of a w-rap.",
                    "What do you call a sleeping bull? A bull-dozer.",
                    "What did the buffalo say when his kid went to college? Bison.",
                    "Where did the music teacher leave her keys? In the piano!"};
            textToSpeech("Do you want a regular or custom game");
            if (sc.nextLine().equals("regular")) {
                maxDraw = 100;
                maxBonus = 50;
                numRounds = 2;
                numPlayers = 5;
                playerNames = new String[]{"Alex", "Bob", "Tim", "Elizabeth", "Jane"};
            }
            else {
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
                    textToSpeech("Hello " + playerNames[i]);
                    textToSpeech("Here's a joke for you!");
                    Thread.sleep(500);
                    textToSpeech(questionJokes[i]);
                    Thread.sleep(500);
                }
            }

            textToSpeech("Welcome all to the 1st annual Cup Pong competition!");
            textToSpeech("Best of luck to everyone! Let's play hard");

            points = new int[numRounds][numPlayers];

            launch(args);
//            sy.waitEngineState(Synthesizer.QUEUE_EMPTY);
//            sy.deallocate();
            tv.exit();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void textToSpeech(String data){
        try {
            System.out.println(data);
//            sy.speakPlainText(data, null);
            tv.mluv(data);

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
                        int bonus = (int) (Math.random() * (maxBonus + 1));
                        points[i][j] = pointsEarned + bonus;

                        textToSpeech("Your score is " + pointsEarned + " points!");
                        textToSpeech("Your bonus from the lucky draw is...");
                        Thread.sleep(1500);
                        textToSpeech(bonus + " points!");
                        textToSpeech("Your total for this round is " + points[i][j] + "!");

                        data.get(j).setYValue((int) data.get(j).getYValue() + points[i][j]);
                        int currentPlayerScore = 0;
                        for (int k = 0; k <= i; k++) {
                            currentPlayerScore += points[k][j];
                        }

                        textToSpeech("Moving on");
                        Thread.sleep(200);


                    }
//                    ObservableList<int[]> curData = FXCollections.observableArrayList();
//                    curData.addAll(Arrays.asList(points));
//
//                    tableView.setItems(curData);
                }
                Thread.sleep(2000);
                textToSpeech("We are ready to announce the results");
                int pos = numPlayers;
                int winner = 0;
                while (pos > 0) {
                    winner = 0;
                    int winningTotal = 20000;
                    for (int i = 0; i < numPlayers; i++) {
                        int sum = 0;
                        for (int j = 0; j < numRounds; j++) {
                            sum += points[j][i];
                        }
                        if (sum < winningTotal) {
                            winningTotal = sum;
                            winner = i;
                        }
                    }
                    if (pos == 1) {
                        textToSpeech("We are proud to announce the winner is " + playerNames[winner] +  " with a total of " + winningTotal+ " points");
                    }
                    else if (pos == 2) {
                        textToSpeech("Congratulations to the runner-up " + playerNames[winner] +  ", with a total of " + winningTotal+ " points");
                    }
                    else {
                        textToSpeech(playerNames[winner] + " gets rank " + pos + " with a total of " + winningTotal+ " points");
                    }
                    Thread.sleep(2000);
                    for (int k = 0; k < numRounds; k++) {
                        points[k][winner] = 20000;
                    }
                    pos = pos-1;
                }

                textToSpeech("Game has finished! Thanks to everyone for playing our 1st annual Cup Pong competition");

                return null;
            }
        };
    }
}

