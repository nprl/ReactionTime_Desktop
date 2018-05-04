
// Version 3 New things in V3: 
//in order to create jar do: jar -cvm0f manifest.txt NameOfJar.jar *.class rightHand.PNG leftHand.PNG logo.jpg 
// random inbetween repeated?


//All the stuff we need to import to run this program
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.Random;
import java.lang.System;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.scene.text.Font;
import javafx.animation.SequentialTransition;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.animation.Timeline;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.util.concurrent.TimeUnit;
import javafx.animation.PathTransition;
import javafx.animation.FillTransition;
import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCombination;
import javafx.stage.StageStyle;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.KeyCode;
import javafx.stage.WindowEvent;
import javafx.scene.image.WritableImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Cursor;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.LinkedList;

//single class. everything in here 

public class ReactionTime extends Application{
	
	//variable we will use in various methods throughout the program
	private String seq = "423142124323";
	private String predetermined = "";
	public static String newline = System.getProperty("line.separator");
	private Rectangle r1, r2, r3, r4,rpressed; //represents each of the rectangles 1-4 and the rectangle thats pressed
	private int count = 0; // keeps a count of how many times we have gone through the cycle method
	private int trials; // total number of trials aka how many times we want them to press the key
	private String correct; // says which key is the correct key for rectangle that's pressed
	private long[] onset; //array of the onsets of the time when the rectangle changes color
	private long[] offset; //arrry of the offsets of the time when the user presses the key
	private long[] reactionTimes; // array of the reaction times. where at each index -- starting with 0 and ending at trials - 1
	//represents the amount of time (in milliseconds the participant took to react)
	//the value of the reactiontime will be negative if an incorrect key was pressed
	private int viableTrials; //trails minus the wrong keys pressed
	private int[] bad;
	// Scanner scan;
	private String text; //not even sure if I use
	private String id; //test or train
	private int countdown;
	private StackPane stack;
	private int runs;
	private long avgReaction; //average reactiontime of the participant across all of the trials
	private long[] avgSeqReaction;
	private long[] interpress;
	private long randFirstAvg;
	private long randLastAvg; 
	private long trialsAvg;
	private double percentage; //percentage of trials in which the participant pressed the correct key
	private double[] avgSeqAccuracy;
	private double randFirstAcc;
	private double randLastAcc;
	private double trialsAcc;
	private StackPane primaryStack;
	private Group rmid;
	private ImageView hand;
	private String test = "";
	private String namer = "";
	private String visiter = "";
	private String stager = "";

	private ArrayList<String>[] pressed; //need to instantiate after set string is called
	private String oldkey = "a";
	private int looper = 0;  //since the key pressed thing loops, we need this variable to see
	//how many times it loops.
	private int individualCounter = 0; // seperate counter for which row to place the data in
	// in the text file. used to edit the array pressed
	private boolean incrementNext = false; //since we need to interpret next, this var
	//helps us keep track of if we are gonna increment the next keypress or no 
	private Rectangle2D primaryScreenBounds; //screen size of either attached screen //if there is one
	//or current screen
	public boolean bool;

	//for awareness:
	private LinkedList<Rectangle[]> animationSeqs = new LinkedList<>(); //will need to put in each sequence (array of rectangles)
	private LinkedList<String> answers = new LinkedList<>(); //will need to set answers
	private LinkedList<String> userResponse = new LinkedList<>(); //will set when user presses
	
	public static void main(String[] args) {
		launch(args);
	}
	

	// this corresponds to the main method in normal java programs
	// this prepares everything for the program and allows the game
	// to begin only after the start button is pressed
	public void start(Stage stage) {
		
		//added stuff: to test
		if(Screen.getScreens().size() == 1) {
			primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		} else {
			primaryScreenBounds = Screen.getScreens().get(1).getVisualBounds();
		}
		
		
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());

		//set the dimesions to the screen size:
		stage.setWidth(primaryScreenBounds.getWidth());
		stage.setHeight(primaryScreenBounds.getHeight());
		stage.setFullScreen(true);
		stage.sizeToScene();
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
		boolean play = dialog();
		if (play) { //true type
			stage.show();
			startButtonScreen(stage);
			// stage.show();

		} else {
			System.out.println("went to AWARE");
			awareScreen(stage);
			//aware
		}
		
	}
	public void awareScreen(Stage stage) {
		stack = new StackPane();
		Button start = new Button("AWARE");
		start.setFont(new Font(100.0));
		start.setTextFill(Color.CYAN);
		start.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		start.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());

		HBox box = new HBox(25); //horizontal box in which the rectangles are placed. seperated by 25 pixels
		box.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		// create the rectangles
		Rectangle rect1 = new Rectangle(100.0, 100.0);
		Rectangle rect2 = new Rectangle(100.0, 100.0);
		Rectangle rect3 = new Rectangle(100.0, 100.0);
		Rectangle rect4 = new Rectangle(100.0, 100.0);
		
		// make them white
		rect1.setFill(Color.WHITE);
		rect2.setFill(Color.WHITE);
		rect3.setFill(Color.WHITE);
		rect4.setFill(Color.WHITE);

		box.getChildren().addAll(rect1,rect2, rect3,rect4); //add the rectangles to the horizontal box
		box.setAlignment(Pos.CENTER); //centers the box on the window
		
		Label recognize = new Label("Do you recognize this pattern?");
		recognize.setFont(new Font("Arial", 30));
		recognize.setWrapText(true);
		recognize.setPrefHeight(1000.0);
		recognize.setAlignment(Pos.TOP_CENTER); //why does top left put in top center? idk
		recognize.setTextFill(Color.WHITE);

		stack.getChildren().addAll(box, recognize);
		initialize(rect1, rect2, rect3, rect4);

		//add hereeeee
		// FillTransition clear2 = new FillTransition(Duration.millis(10000000), rect2, Color.WHITE, Color.WHITE);
		// clear2.play();
		//FillTransition clear2 = new FillTransition(Duration.millis(1), rect2, Color.BLACK, Color.WHITE);
		Rectangle[] curAnimation = animationSeqs.peek();
		animate(curAnimation[0], curAnimation[1], curAnimation[2]);
		
		
		Label pressText = new Label("Press Y for yes, N for no, and R to repeat");
		pressText.setFont(new Font("Arial", 30));
		pressText.setWrapText(true);
		pressText.setPrefHeight(1000.0);
		pressText.setAlignment(Pos.BOTTOM_CENTER); //why does top left put in top center? idk
		pressText.setTextFill(Color.WHITE);
		stack.getChildren().add(pressText);

		Scene s = new Scene(stack, 1000, 1000);
		
		//check for key press
		s.setOnKeyPressed(new EventHandler<KeyEvent>() { // when key pressed
			
			public void handle(KeyEvent k){

				String key = k.getText();
				KeyCode code = k.getCode();
				if (code == KeyCode.Y || code == KeyCode.N) {
					
					userResponse.add(key);
					//move to next animation sequence
					animationSeqs.pop();
						
					if (!animationSeqs.isEmpty()){
						Rectangle[] curAnimation = animationSeqs.peek();
						animate(curAnimation[0], curAnimation[1], curAnimation[2]);
					} else {
						//close
						awareText();
						stage.close();
					}
					
					
				} else if(code == KeyCode.R) {
					// if(newCurAnimation == NULL) {
					Rectangle[] curAnimation = animationSeqs.peek();
					animate(curAnimation[0], curAnimation[1], curAnimation[2]);
					// } else {
					// 	animate(newCurAnimation[0], newCurAnimation[1], newCurAnimation[2]);
					// }
					
					//repeat current seq
				}

			}
		});

		stage.show();
		stage.setScene(s);
		// stage.showAndWait();
	}

	public void initialize(Rectangle rect1, Rectangle rect2, Rectangle rect3, Rectangle rect4) {
		//seq: 423142124323 where 1 = v

		
		Rectangle[] arr1 = {rect3, rect1, rect2}; //rand:312
		Rectangle[] arr2 = {rect2, rect3, rect1}; //4[231]42124323
		Rectangle[] arr3 = {rect1, rect2, rect3}; //rand:123
		Rectangle[] arr4 = {rect1, rect4, rect2}; //423[142]124323
		Rectangle[] arr5 = {rect4, rect2, rect1}; //4231[421]24323
		Rectangle[] arr6 = {rect1, rect4, rect4}; //rand:144
		Rectangle[] arr7 = {rect4, rect3, rect2}; //42314212[432]3
		Rectangle[] arr8 = {rect2, rect2, rect3}; //rand:223
		Rectangle[] arr9 = {rect1, rect2, rect1}; //rand:121
		Rectangle[] arr10 = {rect3, rect2, rect3}; //423142124[323]
		animationSeqs.add(arr1);
		animationSeqs.add(arr2);
		animationSeqs.add(arr3);
		animationSeqs.add(arr4);
		animationSeqs.add(arr5);
		animationSeqs.add(arr6);
		animationSeqs.add(arr7);
		animationSeqs.add(arr8);
		animationSeqs.add(arr9);
		animationSeqs.add(arr10);

		answers.add("n");
		answers.add("y");
		answers.add("n");
		answers.add("y");
		answers.add("y");
		answers.add("n"); // arr6
		answers.add("y");
		answers.add("n");
		answers.add("n");
		answers.add("y");

	}
	public void animate(Rectangle rect1, Rectangle rect2, Rectangle rect3) {


		// fill transition will work when from value is 1.0
		//make rect 1 blink
		FillTransition w1 = new FillTransition(Duration.millis(500), rect1, Color.WHITE, Color.WHITE);
		FadeTransition b1 = new FadeTransition( new Duration(1.0), rect1);
		b1.setToValue(1.0);
		b1.setFromValue(1.0);
		b1.setCycleCount(1);// Color.WHITE, Color.CYAN);
		FillTransition blue1 = new FillTransition(Duration.millis(500), rect1, Color.WHITE, Color.CYAN);
		FillTransition white1 = new FillTransition(Duration.millis(500), rect1, Color.CYAN, Color.WHITE);
		
		//rect 2 flash
		FillTransition w2 = new FillTransition(Duration.millis(500), rect2, Color.WHITE, Color.WHITE);
		FadeTransition b2 = new FadeTransition( new Duration(1.0), rect2);
		b2.setToValue(1.0);
		b2.setFromValue(1.0);
		b2.setCycleCount(1);// Color.WHITE, Color.CYAN);
		FillTransition blue2 = new FillTransition(Duration.millis(500), rect2, Color.WHITE, Color.CYAN);
		FillTransition white2 = new FillTransition(Duration.millis(500), rect2, Color.CYAN, Color.WHITE);
		

		//rect 3 flash
		FillTransition w3 = new FillTransition(Duration.millis(500), rect3, Color.WHITE, Color.WHITE);
		FadeTransition b3 = new FadeTransition( new Duration(1.0), rect3);
		b3.setToValue(1.0);
		b3.setFromValue(1.0);
		b3.setCycleCount(1);// Color.WHITE, Color.CYAN);
		FillTransition blue3 = new FillTransition(Duration.millis(500), rect3, Color.WHITE, Color.CYAN);
		FillTransition white3 = new FillTransition(Duration.millis(500), rect3, Color.CYAN, Color.WHITE);
		
		//old
		// FillTransition b3 = new FillTransition(Duration.millis(400), rect3, Color.WHITE, Color.CYAN);
		// FadeTransition ft3 = new FadeTransition(new Duration(400.0), rect3);
		// ft3.setFromValue(0);
		// ft3.setToValue(1.0);
		// ft3.setCycleCount(1);
		// FillTransition w3 = new FillTransition(Duration.millis(400), rect3, Color.CYAN, Color.WHITE);
		
		
		
		SequentialTransition sequence = new SequentialTransition();
		sequence.getChildren().addAll(w1, b1, blue1, white1, w2,b2,blue2,white2, w3,b3,blue3,white3);
		sequence.play();
	}

	public void startButtonScreen (Stage stage) {
		pressed = new ArrayList[trials];
		for(int x = 0; x < pressed.length; x++){
	        pressed[x] = new ArrayList<String>();
	    }

		stack = new StackPane();
		Button start = new Button("Start");
		start.setFont(new Font(100.0));
		start.setTextFill(Color.CYAN);
		start.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		start.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
		
		ImageView logo = new ImageView(new Image(this.getClass().getResourceAsStream("logo.jpg"))); 
		logo.setFitHeight(100);
		logo.setPreserveRatio(true);
		stack.setAlignment(logo, Pos.TOP_LEFT);
		hand.setFitHeight(300);
		hand.setPreserveRatio(true);
		stack.setAlignment(hand, Pos.BOTTOM_CENTER);
		
		stack.getChildren().addAll(start, logo, hand);
		Scene s = new Scene(stack, 1000, 1000);

		

		s.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent code) {
				if(code.getCode().equals(KeyCode.ENTER)) {
					main(stage);
				}
			}
		});
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent w) {
				close(stage);
				System.exit(0);

			}
		});

		start.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				main(stage); // calls "main" method upon clicking start button
			}
		});

		stage.setScene(s);
		stage.show();
		
	}

	// this is the main method that sets the background, rectangles, and adds them all to the window
	public void main(Stage s2) {
		primaryStack = new StackPane();
		// BorderPane border = new BorderPane();

		

		// border.setBottom(logo);
		HBox box = new HBox(25); //horizontal box in which the rectangles are placed. seperated by 25 pixels
		box.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		// create the rectangles
		r1 = new Rectangle(100.0, 100.0);
		r2 = new Rectangle(100.0, 100.0);
		Text plus = new Text("+");
		plus.setFill(Color.WHITE);
		plus.setFont(new Font(20));
		rmid = new Group(plus);
		r3 = new Rectangle(100.0, 100.0);
		r4 = new Rectangle(100.0, 100.0);
		
		// make them white
		r1.setFill(Color.WHITE);
		r2.setFill(Color.WHITE);
		r3.setFill(Color.WHITE);
		r4.setFill(Color.WHITE);

		box.getChildren().addAll(r1,r2, r3,r4); //add the rectangles to the horizontal box
		box.setAlignment(Pos.CENTER); //centers the box on the window
		primaryStack.getChildren().addAll(box, rmid);
		
		Scene scene = new Scene(primaryStack, Color.BLACK); //creates a 500px X 500px window with a black background
		scene.setCursor(Cursor.NONE);
		s2.setScene(scene); 
		s2.show(); //necessary to see javafx


		s2.setFullScreen(true);
		
		
		Text three = new Text("3");
		three.setFill(Color.WHITE);
		three.setFont(new Font(50));
		Group g3 = new Group(three);
		g3.setTranslateY(-300);
		primaryStack.getChildren().add(g3);

		try {
		    Thread.sleep(2000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		primaryStack.getChildren().remove(g3);

		// Text bthree = new Text("3");
		// bthree.setFill(Color.BLACK);
		// bthree.setFont(new Font(60));
		// Group b3 = new Group(bthree);
		// b3.setTranslateY(-300);
		// primaryStack.getChildren().add(b3);
		// g3.getParent().getChildren().remove(g3);

		cycle(scene, s2); // recursive method that allows for the looping of the rectangles blinking see cycle method

		

	}

	/**
	 *This method sets the color of the passed in rectangle to CYAN
	 *and saves the onset time. 
	 */
	public void blink(Rectangle r) {
		r.setFill(Color.CYAN);
		onset[count] = System.currentTimeMillis();
	}

	/**
	 *This method changes the passed in rectangle to blue
	 *also sets a fade method for 400ms so the next rectangle does
	 *not come up until 400 milliseconds afterwards
	 */
	public void white(Rectangle r){
		r.setFill(Color.WHITE);
		FadeTransition ft = new FadeTransition(new Duration(400.0), rpressed);
		ft.setFromValue(0);
		ft.setToValue(1.0);
		ft.setCycleCount(1);
		ft.setAutoReverse(true);
		ft.play();
	}

	/**
	 *This method chooses a random rectangle from the 4 rectangles
	 */
	public Rectangle random() {
		Random rand = new Random();
		int num = rand.nextInt(4) + 1;
		return num2Rect(num);

	}

	// returns a random integer from 1 to 4
	public int randomInt() {
		Random rand = new Random();
		int num = rand.nextInt(4) + 1;
		return num;

	}

	// turns a passed in number to a rectangle
	//also assigns a correct key depending on the rectangle
	//turns therectangle cyan with the blink method
	//returns the rectangle that it randomly picked
	public Rectangle num2Rect(int num) {
		if (num == 1) {
			blink(r1);
			correct = "v";
			return r1;
		} else if (num == 2) {
			blink(r2);
			correct = "b";
			return r2;
		} else if (num == 3) {
			blink(r3);
			correct = "n";
			return r3;
		} else if (num == 4) {
			blink(r4);
			correct = "m";
			return r4;
		} else {
			return random();
		}
	}

	//reads a string at an index and returns the coresponding rectangle
	//that needs to light up later.
	public Rectangle read(String order, int x) {
		int num = Character.getNumericValue(order.charAt(x));
		return num2Rect(num);
	}
	
	/**
	 *This is a significant recursive method
	 *This determines wheather or not to put another rectangle
	 *this listens for the keypress and checks for the correct key
 	 *if the wrong key is pressed it will decrease the number of viabletrials
	 *once all of the trials finish it will call the calculations and write
	 *methods
	 *also creates the pop up at the end
	 */
	public void cycle(Scene scene, Stage s2) {
		

		r1.setFill(Color.WHITE);
		r2.setFill(Color.WHITE);
		r3.setFill(Color.WHITE);
		r4.setFill(Color.WHITE);

		rpressed = read(predetermined, count);
		
		// for a random rectangle. 
		// 	rpressed = random(); 

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent k) {
				String key = k.getText();
				
				if(looper > 5) { //considering it a longpress
					pressed[individualCounter].add(key + "*"); //indicating long press
				} else { //reg press
					pressed[individualCounter].add(key);
				}
				
				looper = 0;
			}
		});
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() { // when key pressed
			
			public void handle(KeyEvent k){
				String key = k.getText();
				if(incrementNext == true) {
					individualCounter = individualCounter + 1;
					incrementNext = false;
				}

				if (key.equals(correct)) { // if user entered correct key
					white(rpressed);
					offset[count] = System.currentTimeMillis();
					reactionTimes[count] = offset[count] - onset[count]; 
					if (count > 0) {
						interpress[count] = offset[count] - offset[count - 1];
					} else {
						primaryStack.getChildren().remove(rmid);
					}

					if (count < trials) { // checks if going to continue another cycle
						
						if (count < trials - 1){ 
							count = count + 1;
							cycle(scene, s2); //continues
						} else if (count < trials) { //aka does count == trials - 1
							close(s2);	//exits

						}
					}
					incrementNext = true; //increment the individualCounter next time a key is pressed
				} else if (k.getCode().equals(KeyCode.BACK_SPACE)) { // if user presses backspace
					close(s2);
				} else {// if user entered incorrect key
					viableTrials = viableTrials - 1;
					bad[count] = bad[count] - 1;
				}
				looper = looper + 1;
				oldkey = key;
			
			}
		});
	}


	// This method createst the first dialog box
	public boolean dialog() {
		// text input dialog with multiple
		//Creating a GridPane container
		Stage s = new Stage();
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		//Defining the Name text field
		final TextField name = new TextField();
		name.setPromptText("Enter Participant ID.");
		name.setPrefWidth(250);
		name.setPrefColumnCount(10);
		name.getText();
		GridPane.setConstraints(name, 0, 0);
		grid.getChildren().add(name);
		//Defining the test text field
		final TextField testText = new TextField();
		testText.setPromptText("TESTR, TESTL, TRAINR, or TRAINL.");
		GridPane.setConstraints(testText, 0, 1);
		grid.getChildren().add(testText);
		//Defining the Last Name text field
		final TextField lastName = new TextField();
		lastName.setPromptText("Enter Visit Number.");
		GridPane.setConstraints(lastName, 0, 2);
		grid.getChildren().add(lastName);
		//Defining the Comment text field
		final TextField comment = new TextField();
		comment.setPrefColumnCount(15);
		comment.setPromptText("Time/Stage. (ie. Post0)");
		GridPane.setConstraints(comment, 0, 3);
		grid.getChildren().add(comment);

		//adding the message
		Text message = new Text("Welcome, press submit to continue");
		GridPane.setConstraints(message, 0, 4);
		grid.getChildren().add(message);

		//Defining the Submit button
		Button submit = new Button("Submit");
		GridPane.setConstraints(submit, 1, 0);
		grid.getChildren().add(submit);

		//Defining the Clear button
		Button clear = new Button("Clear");
		GridPane.setConstraints(clear, 1, 1);
		grid.getChildren().add(clear);
		s.setScene(new Scene(grid));

		
		//process
		//Adding a Label
		// final Label label = new Label();
		// GridPane.setConstraints(label, 0, 3);
		// GridPane.setColumnSpan(label, 2);
		// grid.getChildren().add(label);

		//Setting an action for the Submit button
		submit.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		    public void handle(ActionEvent e) {
		        namer = name.getText();
		        test = testText.getText();
		        visiter = lastName.getText();
		        stager = comment.getText();
		        boolean worked = setString(test);
		        // System.out.println(test);
		        // System.out.println(worked);

		        if (worked) {
		        	s.close();
					bool = true;

		        } else { //set string returned false
		        	if (test.contains("AWARE")) {
		        		bool = false;
		        		s.close();
		        	} else {
			        	clear.fire();
			        	message.setText("Please enter a viable TEST condition");
			        	//TODO: add a message at the bottom maybe
		        	}
		        }
		     }
		 });
		 
		//Setting an action for the Clear button
		clear.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		    public void handle(ActionEvent e) {
		        name.clear();
		        testText.clear();
		        lastName.clear();
		        comment.clear();
		    }
		});
		s.showAndWait();
		return bool;
	}

	// this method turns the blank predetermined string to a string that is already set depending on
	// weather this is a test session or training session
	//
	public boolean setString(String test) { //creates the sequence
		if (test.contains("TESTR") || test.contains("TESTL")) { //if user enters test
			if (test.contains("L")){
				hand = new ImageView(new Image(this.getClass().getResourceAsStream("leftHand.PNG")));
		
			} else if (test.contains("R")) {
				hand = new ImageView(new Image(this.getClass().getResourceAsStream("rightHand.PNG")));
			}

			if (test.contains("1")) {
				seq = invert(seq);
			}
			
			predetermined = predetermined + "24132413412432431243124132432412432134231241324321";
			
			runs = 15;
			for (int x = 0; x < runs; x++) {
				predetermined = predetermined + seq; // seq repeated 15 times
			}
			
			predetermined = predetermined + "12341243214213214231234231231423142341231231412432";
			
			trials =  predetermined.length();
			
			viableTrials = predetermined.length();
			onset = new long[trials];
			offset = new long[trials];
			interpress = new long[trials];
			reactionTimes = new long[trials];
			bad = new int[trials];
			return true;
		} else if (test.contains("TRAINR") || test.contains("TRAINL")) { // if user enters training
			hand = new ImageView(new Image(this.getClass().getResourceAsStream("rightHand.PNG")));
			if (test.contains("L")){
				hand = new ImageView(new Image(this.getClass().getResourceAsStream("leftHand.PNG")));
			} else if (test.contains("R")) {
				hand = new ImageView(new Image(this.getClass().getResourceAsStream("rightHand.PNG")));
			}

			if (test.contains("1")) {
				seq = invert(seq);
			}
			predetermined = predetermined + "24321321421341234312314324323143213243124142314321";
			runs = 25;
			for (int x = 0; x < runs; x++){
				predetermined = predetermined + seq; //seq repeated 25 times
			}
			predetermined = predetermined + "12314312342341243241321324324324312341324321341342";
			trials =  predetermined.length();
			viableTrials = predetermined.length();
			onset = new long[trials];
			offset = new long[trials];
			interpress = new long[trials];
			reactionTimes = new long[trials];
			bad = new int[trials];
			return true;
		} else { // if user enters something that isn't test or training
			return false;
			
		}
	}

	//Calculates the accuracy and avg reaction times of overall session
	public void calculations() {
		double total = 0;
		int badTotal = 0;
		for (int a: bad) {
			badTotal = badTotal + a;
		}
		for(long l: reactionTimes) {
			total = total + l;
		}
		avgReaction = (Math.round(total/((double)count)));

		percentage = (double)(Math.round((100.0*((double)count + (double)badTotal))/((double)count))/100.0);
		// might want to implement
		// so just in case
		// if (percentage < 0) {
		// 	percentage = 0.0;
		// }			
	}

	// attempts to analyze the data in relation to the particular sequence
	// gives the avg reaction time and accurace of the beginning random sequence,
	// the repeated sequences, and the random sequence at the end
	public void analyze() {
		// count = count - 1;
		double end = 0.0;
		if (count > 50) {
			end = 50.0;
		} else {
			end = (double)(count);
		}
		
		long randFirstTotal = 0L;
		int randFirstBadTotal = 0;
		
		for (int x = 0; x < end; x++) {
			randFirstTotal = randFirstTotal + reactionTimes[x];
			randFirstBadTotal = randFirstBadTotal + bad[x];
		}
		randFirstAvg = Math.round(randFirstTotal/(double)end);
		randFirstAcc = Math.round(100.0*(end + (double)(randFirstBadTotal))/(end))/100.0;
		
		// System.out.println("RAND FIRST AVG" + randFirstAvg);
		// System.out.println("RAND FIRST ACCURACY" + randFirstAcc);

		long avgTotal = 0l;
		int avgBad = 0;
		avgSeqReaction = new long[runs];
		avgSeqAccuracy = new double[runs];
		for (int x = 0; x < runs; x++){
			long temptotal = 0L;
			int tempbad = 0;
			if (count > 50+(seq.length()*(x+1))) { //if you went through this seq
				end = seq.length();
				for (int y = 0; y < seq.length(); y++) {
					temptotal = temptotal + reactionTimes[50+(seq.length()*x)+y];
					avgTotal = avgTotal + reactionTimes[50+(seq.length()*x)+y];
					tempbad = tempbad + bad[50+(seq.length()*x)+y];
					avgBad = avgBad + bad[50+(seq.length()*x)+y];
				}
			} else if (((count - 50 - (seq.length() * x)) % seq.length()) >= 0) { //if you stopped mid sequence
				end = ((count - 50 - (seq.length() * x)) % seq.length());
				for (int y = 0; y < end; y++) {
					temptotal = temptotal + reactionTimes[50+(seq.length()*x)+y];
					avgTotal = avgTotal + reactionTimes[50+(seq.length()*x)+y];
					tempbad = tempbad + bad[50+(seq.length()*x)+y];
					avgBad = avgBad + bad[50+(seq.length()*x)+y];
				}
			} else { //if you never got to this sequence
				end = 1.0;
				temptotal = 0L;
				tempbad = -1;
			}
			avgSeqReaction[x] = Math.round(temptotal/(double)end);
			avgSeqAccuracy[x] = Math.round(100.0*(end + (double)(tempbad))/ (double)end)/100.0;
			// System.out.println("avgSeqReaction" + "[" + x+ "]: " +avgSeqReaction[x]);
			// System.out.println("avgSegAccuracy" +"[ "+x+"]: " + avgSeqAccuracy[x] );
		}
		if (count > 50 + (seq.length()*runs)) { // if stop after all sequences
			trialsAvg = Math.round(avgTotal/(double)(seq.length()*runs));
			trialsAcc = Math.round(100.0*(count - 100.0 + (double)avgBad)/((double)(seq.length()*runs)))/100.0;
		} else if (count > 50) { // if stop in middle of seq
			trialsAvg = Math.round((avgTotal/(double)(count - 50.0)));
			trialsAcc = Math.round(100.0*(count -50.0 + (double)avgBad)/((double)count - 50.0))/100.0;
		} else { // if didn't get to sequence
			trialsAvg = 0l;
			trialsAcc = 0.0;
		}

		long randLastTotal = 0L;
		int randLastBadTotal = 0; 
		if (count == predetermined.length() - 1) { // if break after end of 50
			end = 50.0;
			// count = count + 1;
		} else if (count > (predetermined.length() - 51)) { // if break inbetween 50
			end = 50.0 - (predetermined.length()-count);
		} else { // if didn't reach last fiffty
			end = 1;
			randLastBadTotal = -1;
		}
		for (int x = count; x > predetermined.length() - 51; x--) {
			randLastTotal = randLastTotal + reactionTimes[x];
			randLastBadTotal = randLastBadTotal + bad[x];

		}
		
		
		randLastAvg = Math.round(randLastTotal/(double)end);
		randLastAcc = Math.round(100.0*(end + (double)(randLastBadTotal))/(end))/100.0;
		// System.out.println("RAND LAST AVG" + randLastAvg);
		// System.out.println("RAND LAST ACCURACY" + randLastAcc);
		if (count == predetermined.length() - 1) {
			count = count + 1;
		}
	}

	// writes to a text file called ReactionTimes.txt
	// and in individual file based on the id (namer)
	public  void writer() {
		writeToFile("ReactionTimes.txt");
		writeToFile(namer);
		indivTrials(namer);
	}

	//records data from the groups
	public void writeToFile(String fileName) {
		try {
			FileWriter file = new FileWriter(new File(fileName), true);

			if (fileName.equals(namer)) {
				file.write("ID:\tvisit\ttime\tCond\tBlock\tAvgRxnT\tAccuracy" + newline);
				file.write(namer+ "\t"+ visiter + "\t" + stager + "\t" +test+ "\tRandBeg\t" +randFirstAvg + "\t" + randFirstAcc + newline);
				for (int x = 0; x < runs; x++) {
					file.write(namer+ "\t"+ visiter + "\t" + stager +  "\t" +test+ "\tRep"+(x+1)+"\t" +avgSeqReaction[x] + "\t" + avgSeqAccuracy[x] + newline);
				}
				file.write(namer+ "\t"+ visiter + "\t" + stager + "\t" +test + "\tRepAvg\t"+trialsAvg + "\t" +trialsAcc + newline);
				file.write(namer+ "\t"+ visiter + "\t" + stager + "\t" +test+ "\tRandEnd\t" +randLastAvg + "\t" + randLastAcc + newline);
				file.write(namer+ "\t"+ visiter + "\t" + stager + "\t" + test+ "\tOverall\t" + avgReaction + "\t" + percentage + newline);
				file.write("End at " +(count) + newline + newline);
			} else {
				file.write("ID: \tCond\tBlock\tAvgRxnT\tAccuracy" + newline);
				file.write(namer+"_" + visiter + "_" + stager + "\t" +test+ "\tRandBeg\t" +randFirstAvg + "\t" + randFirstAcc + newline);
				for (int x = 0; x < runs; x++) {
					file.write(namer+"_" + visiter + "_" + stager + "\t" +test+ "\tRep"+(x+1)+"\t" +avgSeqReaction[x] + "\t" + avgSeqAccuracy[x] + newline);
				}
				file.write(namer+"_" + visiter + "_" + stager + "\t" +test + "\tRepAvg\t"+trialsAvg + "\t" +trialsAcc + newline);
				file.write(namer+"_" + visiter + "_" + stager + "\t" +test+ "\tRandEnd\t" +randLastAvg + "\t" + randLastAcc + newline);
				file.write(namer +"_" + visiter + "_" + stager + "\t" + test+ "\tOverall\t" + avgReaction + "\t" + percentage + newline);
				file.write("End at " +(count) + newline + newline);
			}
			
			file.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	//records individual keypresses
	public void indivTrials(String fileName) {
		try {
			FileWriter file = new FileWriter(new File(fileName), true);
			file.write("trial\tRxnTime\tExpectd\tActual\tCorrect?" + newline);
			for (int x = 0; x < trials; x++) {
				Character num = predetermined.charAt(x);
				String let;
				if (num == '1') {
					let = "v";
				} else if (num == '2') {
					let = "b";
				} else if (num == '3') {
					let = "n";
				} else if (num == '4') {
					let = "m";
				} else {
					let = "?";
				}

				String actual = pressed[x].toString();
				// for (int y = 0; y < pressed[count].size(); y++) {
				// 	actual = actual + pressed[count].remove(0) + ", ";
				// }
				file.write(x + "\t" + reactionTimes[x] + "\t"+ let + "\t" + actual + "\t"+ bad[x] + newline);

			}
			file.write(newline);
			file.write(newline);
			file.write(newline);
			file.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void awareText() {
		try {
			FileWriter file = new FileWriter(new File(namer), true);
			file.write("Seq#\tCorrect\tUser" + "\t" + "good?"+ newline);
			int animationCount = 0;
			int total = answers.size();
			int numRight = 0;
			while (!answers.isEmpty()) {
				String curans = answers.pop();	
				String resp = userResponse.pop();
				String good;

				if (curans.equals(resp)) {
					good = "yes";
					numRight = numRight + 1;
				} else {
					good = "no";
				}
				file.write(animationCount + "\t" + curans + "\t" + resp + "\t" + good + newline);
				animationCount = animationCount + 1;
			}
			file.write("total correct: " + numRight + newline);
			file.write("total: " + (double)(100*numRight/total) + "%" + newline);
			file.write(newline);
			file.close();


		} catch (Exception e) {
			System.out.println(e);
		}
	}
	// inverts the string that is passed
	public String invert(String p) {
		String q = "";
		for (int x = p.length()-1; x > -1; x--) {
			q = q + p.charAt(x);
		}
		return q;
	}

	// a method that should be called whenever someone wants to exit the program
	public void close(Stage s2) {
		calculations();
		analyze();
		writer();
		s2.close();
	}
}


