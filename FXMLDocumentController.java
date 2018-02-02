/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pong.game;


import static java.lang.System.exit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 *
 * @author Brend
 */
public class FXMLDocumentController implements Initializable {
    
    //.5 for easy, 4 for medium, 8 for hard.
    //easy is defaulty.
    double mDifficultySelection = 0.5;
   
    
    //if in the pong game, make ball move
    //if switched from true to false, reset the game
    //if swithced from false to true, ball will start moving, along with shapes. 
    boolean mGamePlaying = false;
    
    
    
    //game panes boundaries, so racket, nor ball, will go out of bounds.    
    double mGameYBoundary = -150;
    double mGameYBoundary2 = 240; 
    double mGameXBoundary = 0;
    
    //if this is true, keys will be used for input, otherwise mouse will be used
    boolean mKeyInput = true;
            
    
    
    @FXML
    private ChoiceBox mInputMethod;
        
    @FXML
    private Shape mTopBoundary;
    
    @FXML
    private Text mLeftScore;   
    private int mLeftScoreNum=0;
    String mLeftScoreString;
    
    @FXML
    private Text mRightScore;        
    private int mRightScoreNum=0;
    String mRightScoreString;
    
    @FXML
    private Button mDifficultyButton;
    
    @FXML
    private Button mStartButton;
    
    @FXML
    private Button mMainMenuButton;
    
    @FXML
    private Button mExitButton;
    
    @FXML 
    private AnchorPane mMainMenu;
    
    @FXML
    private Label mMainMenuLabel;
    
    @FXML
    private GridPane mMainMenuGridPane;
    
    @FXML
    private GridPane mDifficultyGridPane;
    
    @FXML
    private GridPane mGamePane; 
    
    @FXML
    private Button mEasyButton;
    
    @FXML
    private Button mMediumButton;
    
    @FXML
    private Button mHardButton;
    
    @FXML
    private Shape mPongBall;
    
    @FXML
    private Shape mRacket1;
    
    @FXML
    private Shape mRacket2;
    
    @FXML
    private Button mPauseResumeButton;
    
    
    //racket1 original location to reset it. Can use for racket 2 as well.
    double mOriginalYPosition = 0;
    
    //original x and y position for the ball
    double mBallOriginalX = 0;
    double mBallOriginalY = 0;
  
    //to time animation of ball
    //java.util.Timer mTimer = new java.util.Timer();
    
    //pong balls movements. will start going towards top right every game
    double mXVelocity = 2;
    double mYVelocity =  3;
    
    //for every 10 frames, mXVelocity and mYVelocity increases by .1
    int mFramesCounter = 0;     
    
    
    //will handle the starting and stopping of moving the ball
    AnimationTimer mAnimationTimer;
    
    //moves up or down x amount of pixels per frame depending on difficulty
    private void moveRacket2(){
    
        //if paused, do nothing
        if (mPaused == true)
            return;
        
        //if ball is above racket, move racket up (while still inbounds)
        if (mRacket2.getTranslateY() > mPongBall.getTranslateY())
            if (mRacket2.getTranslateY()-75-mDifficultySelection > -211)
                mRacket2.setTranslateY(mRacket2.getTranslateY()-mDifficultySelection);
        //if ball below racket, move racket down (while still inbounds)
        if (mRacket2.getTranslateY() < mPongBall.getTranslateY())
            if (mRacket2.getTranslateY()+mDifficultySelection+75 < 290)
                mRacket2.setTranslateY(mRacket2.getTranslateY()+mDifficultySelection);
                
    }
        
    
    
    
    @FXML
    private void moveBall(){
        
        //if game paused, do nothing
        if (mPaused == true)
            return;
        
        //if next move would be out of bounds (X-Axis), reset ball and add 1 to winners score
        if (mPongBall.getTranslateX()-mXVelocity < -410){
         
            mPongBall.setTranslateY(mBallOriginalY);
            mPongBall.setTranslateX(mBallOriginalX);
                        
            //adding 1 to the right sides score
            mRightScoreNum++;
            mRightScoreString = Integer.toString(mRightScoreNum);
            mRightScore.setText(mRightScoreString);
            
            mXVelocity = 2;
            mYVelocity = 3;
            
        }        
        if (mPongBall.getTranslateX()-mXVelocity > 370){
            
            mPongBall.setTranslateY(mBallOriginalY);
            mPongBall.setTranslateX(mBallOriginalX);
            
            //adding 1 to the left sides score
            mLeftScoreNum++;
            mLeftScoreString = Integer.toString(mLeftScoreNum);
            mLeftScore.setText(mLeftScoreString);
           
            mXVelocity = 2;
            mYVelocity = 3;
        }
        
        //if ball touches a racket, reverse its velocities.
        //will compare the radius of the ball compared to its position, and then
        //compare that to the x and y positions of both rackets, and the height of them
        //along with their x positions. (x positions will always be the same for both rackets)
        //but y is always changing.
        //    mXVelocity *=-1;
        
        //checking if ball is inbounds in the Y-Axis
        if (mPongBall.getTranslateY()- mYVelocity > 290 )
            mYVelocity *=-1;
        if (mPongBall.getTranslateY()- mYVelocity < -211)
            mYVelocity*=-1;
        
        
        //move racket2 according to ball position, and difficulty.
        moveRacket2();
                        
        
        //if ball is in same x and y positions as rackets, reverse its x direction
        if (mPongBall.getTranslateX() - mXVelocity <= -350)
            if (mPongBall.getTranslateY()- mYVelocity > mRacket1.getTranslateY()-75 && mPongBall.getTranslateY() < mRacket1.getTranslateY()+75)
                mXVelocity *=-1;            
        if (mPongBall.getTranslateX()- mXVelocity >= 321)
            if (mPongBall.getTranslateY()- mYVelocity > mRacket2.getTranslateY()-75 && mPongBall.getTranslateY() < mRacket2.getTranslateY()+75)
                mXVelocity*=-1;
        
        //if pong ball hits bottom or top of rectangle, reverse its y direction
        //if (mPongBall.getTranslateX()-mXVelocity <= -350) //racket1
        //    if (mPongBall.getTranslateY() )
        
        
        //moving ball to its next position.
        mPongBall.setTranslateX(mPongBall.getTranslateX()- mXVelocity);
        mPongBall.setTranslateY(mPongBall.getTranslateY() - mYVelocity);
        
        //increase balls speed every 10 frames
        mFramesCounter++;        
        if (mFramesCounter%9==0)
        {
            if (mXVelocity>0){
                mXVelocity+=.15;
                mYVelocity+=.15;
            }
            else {
                mXVelocity-=.15;
                mYVelocity-=.15;
            }
        }
        
        
    }
    
    
    //will check if game is paused or playing
    private boolean mPaused = false;
    
    @FXML
    private void pauseresumebutton(ActionEvent e) throws InterruptedException {
        
        //if game was playing, pause it
        if (mPaused == false){
            
            mPaused = true;        
            mAnimationTimer.stop();            
        }
        
        else if (mPaused == true)
        {
            mPaused = false;
            mAnimationTimer.start();
        }
    }
                   
    
    @FXML
    private void trymovingrect2(MouseEvent e) {
        if (mPaused == true)
            return;
        
        
        //HAVENT CHECKED IF IT IS INBOUNDS YET!
        if (mKeyInput == false)
        {
            //if (e.getY() > 290 || e.getY() < -211)
              //  return;
            
            //move racket to where mouse is
            mRacket1.setTranslateY(e.getY()-300);            
            
            
        }
    }
    
    

    
    @FXML
    private void trymovingrect(Event e){
        
        //if game is paused, dont move rectangle
        //if key isnt being used for input, dont move rectangle
        if (mPaused == true)            
            return;
        
        if (mKeyInput == true) {
                    
            //prints what key was pressed
            System.out.println(e.toString());
            
            if (e.toString().contains("UP")){                                                  
                
                if (mRacket1.getTranslateY()-40 > mGameYBoundary )
                    mRacket1.setTranslateY(mRacket1.getTranslateY()-40);  
            
            }
            
            else if (e.toString().contains("DOWN"))
                if (mRacket1.getTranslateY()+40 < mGameYBoundary2)
                    mRacket1.setTranslateY(mRacket1.getTranslateY()+40);
                    
        }

        }
        
        
    @FXML
    private void exitbutton(ActionEvent event) {
        exit(0);
    }
    
       
    
    @FXML
    private void startbutton(ActionEvent event) throws InterruptedException {
        
        mMainMenuGridPane.setVisible(false);
        mDifficultyGridPane.setVisible(false);
        mGamePane.setVisible(true);
        
        mGamePlaying = true;
        mAnimationTimer.start();                               
        }         

    
    @FXML
    private void difficultybutton(ActionEvent event){
        mMainMenuGridPane.setVisible(false);
        mDifficultyGridPane.setVisible(true);
        mGamePane.setVisible(false);
    }
    
    @FXML
    private void easybutton(ActionEvent event){
        mDifficultySelection = .5;
        mDifficultyGridPane.setVisible(false);
        mMainMenuGridPane.setVisible(true);
        mGamePane.setVisible(false);
    }
    
    @FXML
    private void mediumbutton(ActionEvent event){
        mDifficultySelection = 4.5763;
        mDifficultyGridPane.setVisible(false);
        mMainMenuGridPane.setVisible(true);
        mGamePane.setVisible(false);
    }
    
    @FXML
    private void hardbutton(ActionEvent event){
        mDifficultySelection = 7;
        mDifficultyGridPane.setVisible(false);
        mMainMenuGridPane.setVisible(true);
        mGamePane.setVisible(false);
    }
    
    @FXML
    private void mainmenubutton(ActionEvent event) {
        
        //if was playing and hits main menu, reset ball, and rackets
        //will reset score in later update as well.
        if (mGamePlaying == true)
        {
            mGamePlaying = false;   
            //resets objects to original positions.
            mRacket1.setTranslateY(mOriginalYPosition);            
            mRacket2.setTranslateY(mOriginalYPosition);
            mPongBall.setTranslateX(mBallOriginalX);
            mPongBall.setTranslateY(mBallOriginalY);
            
            //stops balls animation timer.
            mAnimationTimer.stop();
            
        }
        
        //going back to main menu
        mDifficultyGridPane.setVisible(false);
        mMainMenuGridPane.setVisible(true);
        mGamePane.setVisible(false);
        
        //resetting frames, and balls speed
        mFramesCounter =0;        
        mXVelocity = 2;
        mYVelocity = 3;
                
    }
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        mDifficultyGridPane.setVisible(false);
        mMainMenuGridPane.setVisible(true);
        mGamePane.setVisible(false);
        
        
        //racket1 original location to reset it. Can use for racket 2 as well.
        mOriginalYPosition = mRacket1.getTranslateY();        
        
        
        //original x and y position for the ball
        mBallOriginalX = mPongBall.getTranslateX();
        mBallOriginalY = mPongBall.getTranslateY();
        
        
        //getting boundary for ball and rackets       
        mGameXBoundary = mGamePane.getTranslateX(); 
        
        
        //creating a timer to move the ball, but not starting timer yet
        mAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveBall();
            }

        };
        mAnimationTimer.stop(); 
        
        
        
        
        //adding choices to user to use either mouse or key to move racket
        mInputMethod.setItems((ObservableList)(Object)observableArrayList("Key", "Mouse"));
        mInputMethod.setValue("Key");  
        
        //adding change listener for choice box
        mInputMethod.getSelectionModel().selectedIndexProperty().addListener( new ChangeListener<Number>(){
            
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {                
                
                // if new value is 1, then key input is false since mouse is location 1
                mKeyInput = !(newValue.intValue() == 1); 
                
            }
        });
        
    }    
    
}
