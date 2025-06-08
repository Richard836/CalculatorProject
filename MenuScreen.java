import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MenuScreen implements ActionListener {
	
	public static int PlayerLevel = 1;
	public static int PlayerGold = 0;
	public static int PlayerHealth = 20;
	
	JFrame frame;
	JTextField cheatTextField = new JTextField();
	String[] Cheats = {"immortal", "MakeITRAIN", "SwitchItUp"};
	
	JButton[] menuButtons = new JButton[3];
	JButton[] difficultyButtons = new JButton[4];
	JButton[] settingButtons = new JButton[4];
	JButton[] cheatsButtons = new JButton[3];
	
	JPanel Menupanel = new JPanel();
	JLabel label = new JLabel();
	ImageIcon background = new ImageIcon("CastleBackground.jpg");
	JLabel BGlabel = new JLabel(background);
	
	Font myFont;
	
	Color blue = new Color(51, 204, 255);
	static Clip GlobalClip;
	static File audioFile = new File("StartGameMusic.wav");
	
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static int width =  (int) (screenSize.getWidth());    //retrieves max width of screen based off dimensional object
	static int height = (int) (screenSize.getHeight());   //retrieves max height of screen based off dimensional object
	public int musicCondition = 0;
	
	
	public MenuScreen() {
		frame = new JFrame("Menu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setLayout(null);
		
		ChooseScreen();
		
		frame.setVisible(true);
	}
	
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		try { 
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			audioClip.start();
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);
			GlobalClip = audioClip;
			
			MenuScreen menu = new MenuScreen();
			
		}
		catch(FileNotFoundException ex) {
			System.out.println("file not found");
		}
	}
	
	
	
	//WHEN BUTTONS ARE CLICKED THIS METHOD HANDLES THEM
	public void actionPerformed(ActionEvent e) {
		
		
//-----------------------------------------MAIN MENU-----------------------------------------------------------			
		if(e.getSource() == menuButtons[0]) {    //PLAY GAME
			Menupanel.removeAll();
			DifficultyScreen();
		}
		else if(e.getSource() == menuButtons[1]) {  //SETTINGS
			Menupanel.removeAll();
			Settings();
		}
		else if(e.getSource() == menuButtons[2]) {   //EXIT GAME
			System.exit(0);
		}
//-----------------------------------------MAIN MENU-----------------------------------------------------------		

		
//-----------------------------------------DIFFICULTY MENU-----------------------------------------------------------	
		else if(e.getSource() == difficultyButtons[0]) {  // DIFFICULTY EASY
			this.PlayerGold -= 50;
			this.PlayerLevel = 3;
			TheGame call = new TheGame();
			call.Background();
		}
		else if(e.getSource() == difficultyButtons[1]) {  //DIFFICULTY MEDIUM
			this.PlayerGold -= 25;
			this.PlayerLevel = 2;
			TheGame call = new TheGame();
			call.Background();
		}
		else if(e.getSource() == difficultyButtons[2]) {  //DIFFICULTY HARD
			this.PlayerLevel = 1;
			TheGame call = new TheGame();
			call.Background();
		}
		else if(e.getSource() == difficultyButtons[3]) {  //RETURN TO MENU FROM PLAY BUTTON
			Menupanel.removeAll();
			ChooseScreen();
		}
//-----------------------------------------DIFFICULTY MENU-----------------------------------------------------------	
		
		
//-----------------------------------------SETTINGS MENU-----------------------------------------------------------			
		else if(e.getSource() == settingButtons[0] && musicCondition == 1) {  //TURN ON GAME MUSIC
			try {
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				AudioFormat format = audioStream.getFormat();
			
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				this.GlobalClip = (Clip) AudioSystem.getLine(info);
				this.GlobalClip.open(audioStream);
				this.GlobalClip.start();
				this.GlobalClip.loop(Clip.LOOP_CONTINUOUSLY);
				musicCondition = 0;
			}
			catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == settingButtons[1] && musicCondition == 0) {  //TURN OFF GAME MUSIC
			this.GlobalClip.close(); 
			musicCondition = 1;
		}	
		else if(e.getSource() == settingButtons[2]) {  //GO TO CHEATS MENU
			Menupanel.removeAll();
			frame.remove(BGlabel);
			frame.getContentPane().setBackground(blue);
			Cheats();
		}	
		else if(e.getSource() == settingButtons[3]) {  //RETURN TO MENU FROM SETTINGS
			Menupanel.removeAll();
			ChooseScreen();
		}
//-----------------------------------------SETTINGS MENU-----------------------------------------------------------	
		

//-----------------------------------------CHEATS MENU-----------------------------------------------------------
		else if(e.getSource() == cheatsButtons[0]) {
			String value = cheatTextField.getText();

			if(value.contains(Cheats[0])) {
				cheatTextField.setText("Cheat Activated: 'I will never die'");
				this.PlayerHealth = 1000000;
			}
			else if(value.contains(Cheats[1])) {
				cheatTextField.setText("Cheat Activated: 'I'm RICH!!!'");
				this.PlayerGold = 1000000;
			}
			else if(value.contains(Cheats[2])) {
				cheatTextField.setText("Cheat Activated: 'Vibe Switch'");
			}
			else if(value.contains("reset")) {
				cheatTextField.setText("All activated cheats have been reversed!");
				this.PlayerHealth = 20;
				this.PlayerGold = 0;
			}
		}
		else if(e.getSource() == cheatsButtons[1]) {  //CLEAR SCREEN ON CHEAT MENU
			cheatTextField.setText("");
		}
		else if(e.getSource() == cheatsButtons[2]) {  //RETURNS FROM CHEAT MENU
			Menupanel.removeAll();
			frame.remove(cheatTextField);
			frame.add(BGlabel);
			Settings();
		}
//-----------------------------------------CHEATS MENU-----------------------------------------------------------	
	}
	
	public void ChooseScreen() {
		myFont = new Font("Times New Roman",Font.BOLD,50);
		label.setText("One's Own Path");
		label.setBounds((width - 350)/2, (height - 1400)/2, 800, 600);
		label.setFont(myFont);
		label.setForeground(Color.RED);
		
		Menupanel.setBounds((width - 300)/2, (height - 600)/2, 300, 900);
		Menupanel.setLayout(new GridLayout(4,1,10,100)); //creates a layout for the menu screen
		Menupanel.setOpaque(false);
		
		menuButtons[0] = new JButton("Play Game");
		menuButtons[1] = new JButton("Settings");
		menuButtons[2] = new JButton("Exit Game");
		
		myFont = new Font("Calibri",Font.BOLD,30);
		for(int i = 0; i < menuButtons.length; i++) {
			menuButtons[i].addActionListener(this);
			menuButtons[i].setFont(myFont);
			menuButtons[i].setFocusable(false);
		}
		
		Menupanel.add(menuButtons[0]);
		Menupanel.add(menuButtons[1]);
		Menupanel.add(menuButtons[2]);

		frame.add(label);
		frame.add(Menupanel);
		BGlabel.setBounds(0, 0, width, height);
		frame.add(BGlabel);
		frame.repaint();
	}
	
	
	//SELECT DIFFICULTY OF GAME
	public void DifficultyScreen() {
		myFont = new Font("Calibri",Font.BOLD,50);
		label.setText("Select Difficulty");
		label.setBounds((width - 300)/2, (height - 1400)/2, 800, 600);
		label.setFont(myFont);
		label.setForeground(Color.RED);
		
		
		Menupanel.setBounds((width - 300)/2, (height - 700)/2, 300, 700);
		Menupanel.setLayout(new GridLayout(4,1,10,50)); //creates a layout for the menu screen
		
		difficultyButtons[0] = new JButton("Easy");
		difficultyButtons[1] = new JButton("Medium");
		difficultyButtons[2] = new JButton("Hard");
		difficultyButtons[3] = new JButton("Return to menu");
				
		myFont = new Font("Calibri",Font.BOLD,30);
		
		for(int i = 0; i < difficultyButtons.length; i++) {
			difficultyButtons[i].addActionListener(this);
			difficultyButtons[i].setFont(myFont);
			difficultyButtons[i].setFocusable(false);
		}
		
		Menupanel.add(difficultyButtons[0]);
		Menupanel.add(difficultyButtons[1]);
		Menupanel.add(difficultyButtons[2]);
		Menupanel.add(difficultyButtons[3]);
		
		frame.repaint();
	}
	
	
	//SETTINGS MENU
	public void Settings() {
		myFont = new Font("Calibri",Font.BOLD,50);
		label.setText("Choose a setting");
		label.setBounds((width - 320)/2, (height - 1400)/2, 800, 600);
		label.setFont(myFont);
		label.setForeground(Color.RED);
		
		Menupanel.setBounds((width - 300)/2, (height - 700)/2, 300, 700);
		Menupanel.setLayout(new GridLayout(4,1,10,50)); //creates a layout for the menu screen
		
		settingButtons[0] = new JButton("Turn on Game Music");
		settingButtons[1] = new JButton("Turn off Game Music");
		settingButtons[2] = new JButton("Cheat Codes");
		settingButtons[3] = new JButton("Return");
				
		myFont = new Font("Calibri",Font.BOLD,30);
		
		for(int i = 0; i < settingButtons.length; i++) {
			settingButtons[i].addActionListener(this);
			settingButtons[i].setFont(myFont);
			settingButtons[i].setFocusable(false);
		}
		
		Menupanel.add(settingButtons[0]);
		Menupanel.add(settingButtons[1]);
		Menupanel.add(settingButtons[2]);
		Menupanel.add(settingButtons[3]);
		
		frame.repaint();
	}
	
	
	//CHEATS MENU
	public void Cheats() {
		myFont = new Font("Calibri",Font.BOLD,50);
		label.setText("Cheat Activation");
		label.setBounds((width - 350)/2, (height - 1400)/2, 800, 600);
		label.setFont(myFont);
		label.setForeground(Color.RED);
		
		cheatTextField.setBounds((width - 800)/2, (height - 850), 850, 70);   //cheat text field parameters
		cheatTextField.setFont(myFont);
		
		Menupanel.setBounds((width - 300)/2, (height - 300)/2, 300, 800);
		Menupanel.setLayout(new GridLayout(4,1,10,100)); //creates a layout for the menu screen
		
		cheatsButtons[0] = new JButton("Enter Cheat");
		cheatsButtons[1] = new JButton("Clr Entry");
		cheatsButtons[2] = new JButton("Return");
		
		myFont = new Font("Calibri",Font.BOLD,30);
		
		for(int i = 0; i < cheatsButtons.length; i++) {
			cheatsButtons[i].addActionListener(this);
			cheatsButtons[i].setFont(myFont);
			cheatsButtons[i].setFocusable(false);
		}
		
		Menupanel.add(cheatsButtons[0]);
		Menupanel.add(cheatsButtons[1]);
		Menupanel.add(cheatsButtons[2]);
		frame.add(cheatTextField);
		
		frame.repaint();
	}
	
//----------------------------------------------------------------------------BEYOND THIS IS THE GAME ITSELF-----------------------------------------------------------------------------------------------------------------------

}
