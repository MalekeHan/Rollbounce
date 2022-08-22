package A2;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Random;

/**
 * 
 * @author Maleke Hanson
 * @date March 31, 2022
 * @version 3.31
 */
public class RollBounce extends JPanel implements ActionListener {
	private final int gravity=10;
	private final int friction=2;
	static ListNode<Ball> list;
	private final int minspeed=5;
	private final int maxspeed=35;
	private final int timerDelay=75;
	private final int balls=7;
	private final static int window_height = 480;
	private final static int window_width = 640;
	private final int ball_radius = 20;
	
	/** Ball Class used to hold the attributes that each ball needs in order to move across the JWinder in the correct manner 
	 * @param <T>
	 */
	private class Ball<T> {
		private int x;
		private int y;
		private int xVel;
		private int yVel;
		
	}
	
	/**
	 * Import ofListNode class that was previously created for Lab6 -- This class allows for us to create a linked List of any given Object type
	 * @param <Ball> We give the Object type as Ball for this project as we are creating a LinkedList of Ball objects
	 */
	public static class ListNode<Ball> {

        private Node head; 
        private int size; 
    
        public ListNode() { 
            head = null;
            size = 0; 
        }
        /**
         * Private node class to allow us to build our linked list
         * each node object is given a Ball object as data and its next attribute as another node
         */
        private class Node { 
            private Ball data; 
            private Node next; 
            
            /**
             * 
             * @param data Takes in a ball object as a paramter for each node constructed
             */
            public Node(Ball data) { 
                this.data = data; 
                next = null;
            }
        }

        /**
         * Method to add Ball Object nodes to a linked List together 
         * @param item Takes in an item of the type Ball 
         * @return boolean true -- will return if we successfully add the ball object to the list 
         */
        public boolean add(Ball item) { 
            if (item == null) { 
                throw new NullPointerException("Must input a real element");    
            }
            Node addedNode = new Node(item); 
            if (head == null) { 
                head = addedNode; 
            }
            else { 
            	Node current = head; 
                while (current.next != null) { 
                	current = current.next; 
                }
                current.next = addedNode; 
            }
            size++;
            return true;
        }
        /**
         * Method to retrieve a specific node from the list at a given index 
         * @param position input the desired position of the node the user wants to retrieve from the list 
         * @return the node/Ball object from the given position -- allows us access all of the Ball object's attributes 
         */
        public Ball get(int position) {
            if (position < 0 || position >= size) { 
                throw new IndexOutOfBoundsException("Position input is invalid"); 
            }
            if (head.next == null && position > 0) { 
                System.out.println("Invalid Position");
                throw new IndexOutOfBoundsException("Position input is invalid"); 
            }
            if(head == null) {
            	System.out.println("Empty List");
            	return null;
            }
            
            else { 
            	Node curr = head; 
                for (int i = 0; i < position; i++) { 
                    curr = curr.next; 
                }
                return curr.data; 
            }
        }
    }
	

    protected Timer tm;

    /**
     * Method to create the Timer object
     * This method also instatiates the list varaible to hold our linked List of ball objects
     * 
     */
    public RollBounce () {

        tm = new Timer(timerDelay, this); 
        
        Random rand = new Random();
        //List of balls
        list = new ListNode<Ball>();
        for(int i = 0; i < balls; i++) { //For loop -- Used to create a new ball object, add the object to our list, give the ball a random starting position, and apply an X and Y velocity to the ball
        	Ball ball = new Ball();
        	list.add(ball);
        	list.get(i).x = rand.nextInt((window_height - ball_radius) - 0) + 0;
    		list.get(i).y = rand.nextInt((window_height - ball_radius) - 0) + 0;
    		list.get(i).xVel = rand.nextInt(maxspeed-minspeed) + minspeed;
    		list.get(i).yVel = gravity;
    		
      
        }
    }

    //Method to paint the bouncing balls in the JWindow
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Probably best you leave this as is.

       //An array of Colors named colors -- given the size of the amount of balls such that we have a unique color for each one
       Color[]colors = new Color[balls];
       colors[0] = Color.BLUE;
       colors[1] = Color.RED;
       colors[2] = Color.GREEN;
       colors[3] = Color.YELLOW;
       colors[4] = Color.BLACK;
       colors[5] = Color.MAGENTA;
       colors[6] = Color.PINK;
    	  
       //For loop used to paint each ball within the list a different color
        for(int i = 0; i < balls; i++) {
        	g.setColor(colors[i]);
        	g.fillOval(list.get(i).x, list.get(i).y, ball_radius, ball_radius);
        }


        // Recommend you leave the next line as is
        tm.start();
    }
    /**
     * Function to apply friction to each ball once they hit one of the barries of the window
     * @param ball Takes in a Ball object to allow us to manipulate the velocties and slow them down 
     */
    public void doFriction(Ball ball) {
    	ball.yVel -= friction;
    	if(ball.xVel > 0) {
    		ball.xVel -= friction;
    		System.out.println("Doin friction 1");
    		System.out.println("Current X Vel:" + ball.xVel);
    	}
    	else if(ball.xVel < 0) {
    		ball.xVel -= friction;
    		System.out.println("Doin friction 2");
    		System.out.println("Current X Vel:" + ball.xVel);
    	}
    }
    /**
     * Method to contiusly track each balls' position and use conditionals to determine whether a ball should change directions, move left/right based on it's current position of the screen and against the Window's walls
     * @param ball Takes in a ball object such that we can change it's position continuously though each frame 
     */
    public void getNewPosition(Ball ball) {
    	ball.yVel += gravity;
		//ball.xVel += gravity;
		
    	if(ball.y >= (window_height - ball_radius) && ball.xVel > 0) { //If the ball's current y position is greater than the window height and the X still has velocity, then we are going to reverse the Y velocity and apply friction to the ball as it has hit the border of the window
    		ball.yVel = -ball.yVel;
    		doFriction(ball);
    	}
    	
    	if((ball.x == 0 && ball.xVel > 0)|| (ball.x >= (window_height - ball_radius) && ball.xVel > 0)) { //If the ball's current position is at the edge of teh window and it still has velocity, then we are going to reverse the velocity to make the ball look as if it is bouncing and apply friction to the ball
    		ball.xVel = -ball.xVel;
    		doFriction(ball);
    	}
    	int currentXPostion = ball.x + ball.xVel; //We calculate where the ball's X position needs to be in the current frame by adding the current position that it is at + the velocity, this gives the illusion the ball is gliding through the window through each frame
    	if(currentXPostion < 0) {
    		currentXPostion = 0; //If the position adds to more than 0, then we will just automatically place the ball back at 0
    	}
    	else if(currentXPostion > window_height) { //If the position adds to more than the window height, then we will just automatically place the ball back at the edge
    		currentXPostion = window_height;
    	}
    	int currentYPostion = ball.y + ball.yVel; //We calculate where the ball's Y position needs to be in the current frame by adding the current position that it is at + the velocity, this gives the illusion the ball is gliding through the window through each frame
    	if(currentYPostion < 0) {
    		currentYPostion = 0;
    	} 
    	else if(currentYPostion > window_height){
    		currentYPostion = window_height;
    	}
    	ball.y = currentYPostion; //We will set the balls's Y to the currentY it should be at after the calculations
    	ball.x =  currentXPostion; //We will set the balls's X to the currentY it should be at after the calculations
   
    }
    /**
     * Method to continuously repaint the 7 balls across the screen over each frame, giving the illusion that the balls are just simply gliding on the screen
     */
    public void actionPerformed(ActionEvent actionEvent) {
        
    	
    	//Uses a for loop -- Loop through the list of balls
    	for (int i = 0; i < balls ; i++) {
    		System.out.println("Current I " + i + "Current X: " + list.get(i).x + "Current X VEl:" + list.get(i).xVel);
    		getNewPosition(list.get(i));
    	}

        	repaint();
    	}

    public static void main(String[] args) {
        RollBounce rb = new RollBounce();

        JFrame jf = new JFrame();
        jf.setTitle("Roll Bounce");
        jf.setSize(window_height, window_height); 
        jf.add(rb);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
