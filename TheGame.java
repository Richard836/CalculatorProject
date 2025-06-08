import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.swing.*;
import javax.sound.sampled.*;

public class TheGame implements KeyListener {
	
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) screenSize.getWidth();	//retrieves max width of screen based off dimensional object
	int height = (int) screenSize.getHeight();	//retrieves max height of screen based off dimensional object
	
	static Color green = new Color(0, 255, 0);
	static Color sandRoad = new Color(255, 204, 153);
	static Color sandRocks = new Color(153, 102, 0);
	
	
	ImageIcon RightCharacter = new ImageIcon("CharacterWalkRight.png");   //character animation
	ImageIcon LeftCharacter = new ImageIcon("CharacterWalkLeft.png");    //character animation
	ImageIcon grassPatch = new ImageIcon("grassPatch.png");
	ImageIcon ItemShop = new ImageIcon("House.png");
	ImageIcon InsideItemShop = new ImageIcon("InsideStore.png");
	ImageIcon PriceSign = new ImageIcon("PriceSign.png");
	ImageIcon GoldCoin = new ImageIcon("GoldCoin.png");
	
	
	JLabel CharacterLabel = new JLabel(RightCharacter);   //character itself (initialize to right animation)
	JLabel grassPatchLabels[] = new JLabel[10];
	JLabel ItemShopLabel = new JLabel(ItemShop);
	JLabel InsideItemShopLabel = new JLabel(InsideItemShop);
	JLabel PriceSignLabel[] = new JLabel[3];
	JLabel PriceSignText[] = new JLabel[3];
	JLabel GoldCoinLabel = new JLabel(GoldCoin);
	
	
	Font myFont = new Font("Times New Roman",Font.BOLD,30);
	JFrame frame = new JFrame("frame");
	
	
	JPanel grass = new JPanel(), Verticalroad = new JPanel(), Horizontalroad = new JPanel(), characterPanel = new JPanel();
	JPanel Verticalrocks[] = new JPanel[15];
	JPanel Horizontalrocks[] = new JPanel[25];
	
	
	int VerticalPosition = (height/2 - 200);
	int HorizontalPosition = (width/2 - 200);
	int GlobalStepsTaken = 0;
	int StepsForShop = 0;
	int StepsNeeded = 1000;
	int PlayerLevel = MenuScreen.PlayerLevel;
	int PlayerGold = MenuScreen.PlayerGold + ((PlayerLevel-1) * 25);
	int PlayerHealth = MenuScreen.PlayerHealth * PlayerLevel;
	int ShopChanceIncrease = 1;
	int ShopPriceScaling = 300;
	int ItemsBought = 0;
	
	
	JLabel PlayerHealthLabel = new JLabel("Health = " + Integer.toString(PlayerHealth));
	JLabel PlayerGoldLabel = new JLabel("Gold = " + Integer.toString(PlayerGold));
	JLabel PlayerLevelLabel = new JLabel("Level = " + Integer.toString(PlayerLevel));
	JLabel StepsToItemShopLabel = new JLabel("Steps Until Shop  : " + Integer.toString(StepsNeeded - StepsForShop));
	JLabel StepsLabelText = new JLabel("Total Steps Taken: " + Integer.toString(GlobalStepsTaken));
	JLabel StepsToGoldInfo = new JLabel("(Every 20 steps = 1 gold)");
	JLabel x = new JLabel("X = " + Integer.toString(HorizontalPosition)), y = new JLabel("Y = " + Integer.toString(VerticalPosition));
	
	
	String[][] StoreItems = { {"Small Health Potion", "30", "60"}, {"Medium Health Potion", "70", "150"}, {"Large Health Potion", "150", "350"} };  //NAME, EFFECT, COST
	String[][] PlayerItems = new String[this.ItemsBought][2];
	
	
	boolean ShopExists = false;
	boolean ShopEntered = false;
	boolean PickedUpCoin = false;
	
	
	static File CoinSound = new File("MarioCoinSound.wav");
	
	
	
	
	public void Background() {
		Random rand = new Random();
		
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setVisible(true);
		
		frame.add(x); frame.add(y);
		x.setBounds(625, -50, 600, 200); y.setBounds(755, -50, 600, 200);
		x.setFont(myFont); y.setFont(myFont);
		
		
		frame.add(PlayerHealthLabel);  //Player health label
		PlayerHealthLabel.setBounds(25, -50, 600, 200);
		PlayerHealthLabel.setFont(myFont);
		
		frame.add(PlayerGoldLabel);  //Player gold label
		PlayerGoldLabel.setBounds(25, 0, 600, 200);
		PlayerGoldLabel.setFont(myFont);
		
		frame.add(PlayerLevelLabel);  //Player level label
		PlayerLevelLabel.setBounds(25, 50, 600, 200);
		PlayerLevelLabel.setFont(myFont);
		
		frame.add(StepsLabelText);  //global steps label
		StepsLabelText.setBounds(1545, -50, 600, 200);
		StepsLabelText.setFont(myFont);
		
		frame.add(StepsToItemShopLabel);  //steps to item shop label
		StepsToItemShopLabel.setBounds(1545, 5, 600, 200);
		StepsToItemShopLabel.setFont(myFont);
		
		frame.add(StepsToGoldInfo);  //steps to item shop label
		StepsToGoldInfo.setBounds(1545, 60, 600, 200);
		StepsToGoldInfo.setFont(myFont);
		
		frame.add(ItemShopLabel);   //add the house label from now
		
		CharacterLabel.setBounds(HorizontalPosition, VerticalPosition, 150, 150);
		frame.add(CharacterLabel);
		frame.add(GoldCoinLabel);
		
		for(int i = 0; i < grassPatchLabels.length; i++) {   //add the grass patch pictures
			grassPatchLabels[i] = new JLabel(grassPatch);       //add the grassPatch png to the label
			frame.add(grassPatchLabels[i]);
		}
		
		for(int i = 0; i < Verticalrocks.length; i++) {  //add vertical rocks
			Verticalrocks[i] = new JPanel();
			Verticalrocks[i].setBackground(sandRocks);
			frame.add(Verticalrocks[i]);
		}
		
		for(int i = 0; i < Horizontalrocks.length; i++) {  //add horizontal rocks
			Horizontalrocks[i] = new JPanel();
			Horizontalrocks[i].setBackground(sandRocks);
			frame.add(Horizontalrocks[i]);
		}
		
		RandomizeMap(ShopChanceIncrease);  //When this is called randomizes rocks, grass patches, house(s), etc.
		
		frame.add(Verticalroad);
		frame.add(Horizontalroad);
		frame.add(grass); 
		
		Verticalroad.setBackground(sandRoad);
		Horizontalroad.setBackground(sandRoad);
		grass.setBackground(green);    
		
		Verticalroad.setBounds(width/2 - 200, 0, 400, height); 
		Horizontalroad.setBounds(0, height/2, width, 200); 
		grass.setBounds(0, 0, width, height);    
		
		frame.addKeyListener(this);
		frame.repaint();
	}
	
	

	
	
	public void RandomizeMap(int ShopChanceIncrease) {
		Random rand = new Random();
		
///////////////////////////////////ITEM SHOP RANDOMIZED BELOW///////////////////////////////////////////////
		int houseSpawn = rand.nextInt(ShopChanceIncrease) + 1;
		
		if(houseSpawn == 1 ||  (StepsNeeded - StepsForShop) <= 0) {
			ShopExists = true;
			StepsForShop = 0;
			ItemShopLabel.setBounds(0, -225, 1000, 1000);
			frame.add(ItemShopLabel);
			frame.repaint();	
		}
		else {
			ShopExists = false;
			frame.remove(ItemShopLabel);
			frame.repaint();
		}
		
		
		int CoinSpawnX;
		int CoinSpawnY;
		int CoinSpawnChance = rand.nextInt(1) + 1;
		
		if(CoinSpawnChance == 1  &&  houseSpawn == 1) {
			PickedUpCoin = false;
			CoinSpawnX = rand.nextInt(1500) + 150;
			CoinSpawnY = rand.nextInt(250) + 550; 
			GoldCoinLabel.setBounds(CoinSpawnX, CoinSpawnY, 100, 100);
			frame.add(GoldCoinLabel);
		}
		else if(CoinSpawnChance == 1  &&  houseSpawn != 1) {
			PickedUpCoin = false;
			CoinSpawnX = rand.nextInt(700) + 150;
			CoinSpawnY = rand.nextInt(250) + 100; 
			GoldCoinLabel.setBounds(CoinSpawnX, CoinSpawnY, 100, 100);
			frame.add(GoldCoinLabel);
		}
		
		
		frame.repaint();
///////////////////////////////////GRASS PATCH RANDOMIZED BELOW////////////////////////////////////////////				
		int grassx;
		int grassy;
		
		for(int i = 0; i < grassPatchLabels.length; i++) {   //add the grass patch pictures
			grassx = rand.nextInt(1401);	//grass position x
			grassy = rand.nextInt(751);	//grass position y
			grassPatchLabels[i].setBounds(grassx, grassy, 400, 400);
		}
///////////////////////////////////ROCKS RANDOMIZED BELOW////////////////////////////////////////////		
		int rocky = 50;
		int rockx = 0;
			
		for(int i = 0; i < Verticalrocks.length; i++) {  //set vertical rocks position	
			rockx = rand.nextInt(351) + 775;
			Verticalrocks[i].setBounds(rockx, rocky, 15, 15);
			rocky += 75;
		}
			
		rockx = 50;
			
		for(int i = 0; i < Horizontalrocks.length; i++) {  //set vertical rocks position	
			rocky = rand.nextInt(171) + 550;
			Horizontalrocks[i].setBounds(rockx, rocky, 15, 15);
			rockx += 75;
		}
		frame.repaint();
	}
	


	
	
	
	public void CheckCharacterOutOfBounds() {
		
		if(HorizontalPosition < -200) {          //CHARACTER GOES FAR LEFT THEN PLACE FAR RIGHT
			ShopChanceIncrease = 5;
			HorizontalPosition = width - 250;
			RandomizeMap(ShopChanceIncrease);
			CharacterLabel.setBounds(HorizontalPosition, VerticalPosition, 150, 150);
		}
		else if(HorizontalPosition > width - 250) {          //CHARACTER GOES FAR RIGHT THEN PLACE FAR LEFT
			ShopChanceIncrease = 5;
			HorizontalPosition = -200;
			RandomizeMap(ShopChanceIncrease);
			CharacterLabel.setBounds(HorizontalPosition, VerticalPosition, 150, 150);
		}
		else if(VerticalPosition < -150) {          //CHARACTER GOES FAR UP THEN PLACE FAR DOWN
			ShopChanceIncrease = 10;
			VerticalPosition = height - 200;
			RandomizeMap(ShopChanceIncrease);
			CharacterLabel.setBounds(HorizontalPosition, VerticalPosition, 150, 150);
		}
		else if(VerticalPosition > height - 200) {          //CHARACTER GOES FAR DOWN THEN PLACE FAR UP
			ShopChanceIncrease = 10;
			VerticalPosition = -150;
			RandomizeMap(ShopChanceIncrease);
			CharacterLabel.setBounds(HorizontalPosition, VerticalPosition, 150, 150);
		}
		frame.repaint();
	}
	
	
	
	
	public void ItemShop() {    //Item shop method runs
		
		if(ShopEntered == true) {
			frame.add(InsideItemShopLabel);
			InsideItemShopLabel.setBounds(0, 0, width, height);
		
			int XSignSpace = 0;
			for(int i = 0; i < PriceSignLabel.length; i++) {
				PriceSignLabel[i] = new JLabel(PriceSign);
				frame.add(PriceSignLabel[i]);
				PriceSignLabel[i].setBounds(-360 + XSignSpace, 120, 1300, 800);
				XSignSpace += 420;
			}
		}
		else {
			frame.remove(InsideItemShopLabel);
			for(int i = 0; i < PriceSignLabel.length; i++) {
				frame.remove(PriceSignLabel[i]);
			}
		}
		frame.repaint();
	}
	
	
	
	
	
///////////////////////////////////////////MAIN METHOD//////////////////////////////////////////////////////

	public static void main(String[] args) {   
		TheGame call = new TheGame();
		call.Background();
	}

///////////////////////////////////////////MAIN METHOD//////////////////////////////////////////////////////	
	
	
	
	
	public void PlayCoinSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(CoinSound);
		AudioFormat format = audioStream.getFormat();
		
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		Clip audioClip = (Clip) AudioSystem.getLine(info);
		audioClip.open(audioStream);
		audioClip.start();
	}

	
	
	
	
//------------------------------------------------------------CHARACTER KEY CONTROLS---------------------------------------------------------------------------------
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if((key == 87  ||  key == 65  ||  key == 83  ||  key == 68)  &&  ShopEntered == false) {
			GlobalStepsTaken += 1;
			StepsForShop += 1;
			
			if(key == 87) {            //GO UP
				frame.remove(Verticalroad);
				frame.remove(Horizontalroad);
				frame.remove(grass);
				
				for(int i = 0; i < grassPatchLabels.length; i++) {   
					frame.remove(grassPatchLabels[i]);
				}

				for(int i = 0; i < Horizontalrocks.length; i++) {  
					frame.remove(Horizontalrocks[i]);
				}
			
				for(int i = 0; i < Verticalrocks.length; i++) {
					frame.remove(Verticalrocks[i]);
				}
			
				frame.remove(CharacterLabel);         //last thing removed
				frame.add(CharacterLabel);            //first thing added
			
				for(int i = 0; i < grassPatchLabels.length; i++) {   
					frame.add(grassPatchLabels[i]);
				}

				for(int i = 0; i < Horizontalrocks.length; i++) {  
					frame.add(Horizontalrocks[i]);
				}
			
				for(int i = 0; i < Verticalrocks.length; i++) {
					frame.add(Verticalrocks[i]);
				}
			
				frame.add(Verticalroad);
				frame.add(Horizontalroad);
				frame.add(grass);
			
			
				VerticalPosition -= 15;
				CharacterLabel.setBounds(HorizontalPosition, VerticalPosition, 150, 150);
				frame.repaint();
				CheckCharacterOutOfBounds();
			}
		
			else if(key == 65) {      //GO LEFT
				frame.remove(Verticalroad);
				frame.remove(Horizontalroad);
				frame.remove(grass);
			
				for(int i = 0; i < grassPatchLabels.length; i++) {   
					frame.remove(grassPatchLabels[i]);
				}

				for(int i = 0; i < Horizontalrocks.length; i++) {  
					frame.remove(Horizontalrocks[i]);
				}
			
				for(int i = 0; i < Verticalrocks.length; i++) {
					frame.remove(Verticalrocks[i]);
				}
			
				frame.remove(CharacterLabel);         //last thing removed
				CharacterLabel = new JLabel(LeftCharacter);
				frame.add(CharacterLabel);            //first thing added
			
				for(int i = 0; i < grassPatchLabels.length; i++) {   
					frame.add(grassPatchLabels[i]);
				}

				for(int i = 0; i < Horizontalrocks.length; i++) {  
					frame.add(Horizontalrocks[i]);
				}
			
				for(int i = 0; i < Verticalrocks.length; i++) {
					frame.add(Verticalrocks[i]);
				}
			
				frame.add(Verticalroad);
				frame.add(Horizontalroad);
				frame.add(grass);
			
				HorizontalPosition -= 15;
				CharacterLabel.setBounds(HorizontalPosition, VerticalPosition, 150, 150);
				frame.repaint();
				CheckCharacterOutOfBounds();  //Check for character out of bounds
			
			}
			
			else if(key == 83) {       //GO DOWN
				frame.remove(Verticalroad);
				frame.remove(Horizontalroad);
				frame.remove(grass);
			
				for(int i = 0; i < grassPatchLabels.length; i++) {   
					frame.remove(grassPatchLabels[i]);
				}

				for(int i = 0; i < Horizontalrocks.length; i++) {  
					frame.remove(Horizontalrocks[i]);
				}
			
				for(int i = 0; i < Verticalrocks.length; i++) {
					frame.remove(Verticalrocks[i]);
				}
			
				frame.remove(CharacterLabel);         //last thing removed
				frame.add(CharacterLabel);            //first thing added
			
				for(int i = 0; i < grassPatchLabels.length; i++) {   
					frame.add(grassPatchLabels[i]);
				}

				for(int i = 0; i < Horizontalrocks.length; i++) {  
					frame.add(Horizontalrocks[i]);
				}
			
				for(int i = 0; i < Verticalrocks.length; i++) {
					frame.add(Verticalrocks[i]);
				}
			
				frame.add(Verticalroad);
				frame.add(Horizontalroad);
				frame.add(grass);
			
				VerticalPosition += 15;
				CharacterLabel.setBounds(HorizontalPosition, VerticalPosition, 150, 150);
				frame.repaint();
				CheckCharacterOutOfBounds();
			}
			
			else if(key == 68) {	   //GO RIGHT
				frame.remove(Verticalroad);
				frame.remove(Horizontalroad);
				frame.remove(grass);
				
				for(int i = 0; i < grassPatchLabels.length; i++) {   
					frame.remove(grassPatchLabels[i]);
				}

				for(int i = 0; i < Horizontalrocks.length; i++) {  
					frame.remove(Horizontalrocks[i]);
				}
			
				for(int i = 0; i < Verticalrocks.length; i++) {
					frame.remove(Verticalrocks[i]);
				}
			
				frame.remove(CharacterLabel);         //last thing removed
				CharacterLabel = new JLabel(RightCharacter);
				frame.add(CharacterLabel);            //first thing added
			
				for(int i = 0; i < grassPatchLabels.length; i++) {   
					frame.add(grassPatchLabels[i]);
				}

				for(int i = 0; i < Horizontalrocks.length; i++) {  
					frame.add(Horizontalrocks[i]);
				}
			
				for(int i = 0; i < Verticalrocks.length; i++) {
					frame.add(Verticalrocks[i]);
				}
			
				frame.add(Verticalroad);
				frame.add(Horizontalroad);
				frame.add(grass);
			
				HorizontalPosition += 15;
				CharacterLabel.setBounds(HorizontalPosition, VerticalPosition, 150, 150);
				frame.repaint();
				CheckCharacterOutOfBounds();
			}
			
			StepsLabelText.setText("Total Steps Taken: " + Integer.toString(GlobalStepsTaken));
		
			if(StepsNeeded - StepsForShop > -1) {
				StepsToItemShopLabel.setText("Steps Until Shop  : " + Integer.toString(StepsNeeded - StepsForShop));
			}
		
			if(GlobalStepsTaken%20 == 0) {
				PlayerGold += 1;
				PlayerGoldLabel.setText(("Gold = " + Integer.toString(PlayerGold)));
			}
			x.setText("X = " + Integer.toString(HorizontalPosition));
			y.setText("Y = " + Integer.toString(VerticalPosition));
			
			
			if(HorizontalPosition >= GoldCoinLabel.getX() - 80  &&  HorizontalPosition <= GoldCoinLabel.getX() + 80  &&  VerticalPosition <= GoldCoinLabel.getY() + 80  &&  VerticalPosition >= GoldCoinLabel.getY() - 80  &&  PickedUpCoin == false) {
				try {
					PlayCoinSound();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
				this.PlayerGold += 100;
				frame.remove(GoldCoinLabel);
				PickedUpCoin = true;
				frame.repaint();
			}
		
			
			if(HorizontalPosition >= 85  &&  HorizontalPosition <= 490  &&  VerticalPosition <= 355  &&  VerticalPosition >= 80 && ShopExists == true) {
				VerticalPosition = (height/2 - 200);
				HorizontalPosition = (width/2 - 200);
				ShopEntered = true;
			}
		}
		
		
///////////////////////////////////////////////////////////BELOW IS FOR SHOP CONDITION IS TRUE////////////////////////////////////////////////////////////////////////////////////////////		
		
		if(ShopEntered == true) {
			frame.remove(ItemShopLabel);
			frame.remove(Verticalroad);
			frame.remove(Horizontalroad);
			frame.remove(grass);
			frame.remove(GoldCoinLabel);
			
			for(int i = 0; i < grassPatchLabels.length; i++) {   
				frame.remove(grassPatchLabels[i]);
			}

			for(int i = 0; i < Horizontalrocks.length; i++) {  
				frame.remove(Horizontalrocks[i]);
			}
		
			for(int i = 0; i < Verticalrocks.length; i++) {
				frame.remove(Verticalrocks[i]);
			}
		
			
			frame.remove(x); frame.remove(y);
			frame.remove(CharacterLabel);         
			frame.remove(PlayerHealthLabel);
			frame.remove(PlayerGoldLabel); 
			frame.remove(PlayerLevelLabel); 
			frame.remove(StepsLabelText); 
			frame.remove(StepsToItemShopLabel);
			frame.remove(StepsToGoldInfo); 
			ItemShop();
			
			if(key == 27) {
				frame.remove(InsideItemShopLabel);
				
				CharacterLabel = new JLabel(RightCharacter);
				frame.add(CharacterLabel);            //first thing added
			
				for(int i = 0; i < grassPatchLabels.length; i++) {   
					frame.add(grassPatchLabels[i]);
				}

				for(int i = 0; i < Horizontalrocks.length; i++) {  
					frame.add(Horizontalrocks[i]);
				}
			
				for(int i = 0; i < Verticalrocks.length; i++) {
					frame.add(Verticalrocks[i]);
				}
			
				frame.add(Verticalroad);
				frame.add(Horizontalroad);
				frame.add(grass);
				
				CharacterLabel.setBounds(HorizontalPosition, VerticalPosition, 150, 150);
				ShopEntered = false;
				frame.repaint();
			}
		}
		frame.repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
//------------------------------------------------------------CHARACTER KEY CONTROLS---------------------------------------------------------------------------------
}