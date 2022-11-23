// TODO: Change window size to dynamic variables
// TODO: Add functionality to click all none-flagged tiles when the face button is pressed
// TODO: Add functionality to reveal all none-flagged tiles when a number tile is middle clicked

package Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.PlainDocument;

import Components.Clock;
import Components.Images;
import Components.CustomButton;
import Util.IntFilter;
import Util.Mode;
import Util.Timer;
import Util.Tools;

public class MainMenu extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private Thread clockMechanism;
	
	private static final int LATENCY = 100;
	private static final String TITLE = "Minesweeper";
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final Font modeLabelFont = new Font("Tahoma", Font.BOLD, 13);
	public static final int SCREEN_WIDTH = (int) screenSize.getWidth();
	public static final int SCREEN_HEIGHT = (int) screenSize.getHeight();
	public static final int COMPONENT_MARGIN = 10;
	
	public static Mode EASY = new Mode()
	.setMapHeight(9)
	.setMapWidth(9)
	.setMineAmount(10)
	.setName("Easy");
	
	public static Mode MEDIUM = new Mode()
	.setMapHeight(16)
	.setMapWidth(16)
	.setMineAmount(40)
	.setName("Medium");
	
	public static Mode HARD = new Mode()
	.setMapHeight(16)
	.setMapWidth(30)
	.setMineAmount(99)
	.setName("Hard");
	
	// public static Mode CUSTOM = new Mode()
	// .setMapHeight(20)
	// .setMapWidth(20)
	// .setMineAmount(10)
	// .setName("Custom");

	public static Mode CUSTOM = new Mode()
	.setMapHeight(50)
	.setMapWidth(50)
	.setMineAmount(684)
	.setName("Custom");
	
	public Mode[] Modes = {EASY, MEDIUM, HARD, CUSTOM};
	public static Mode GAMEMODE = EASY;
	
	private JPanel contentPane;
	private JPanel mainPanel;
	private JPanel topPanel;
	
	private JFormattedTextField ftFieldM;
	private JFormattedTextField ftFieldW;
	private JFormattedTextField ftFieldH;
	
	private JLabel smileFaceLbl;
	private JLabel lblM;
	private JLabel lblW;
	private JLabel lblH;
	
	private JButton btnStart;
	
	private JComboBox<String> comboBox;
	
	private Clock timeCounter;
	private Clock mineCounter;
	private Timer mines;
	private Timer timer;
	private GameView gameView;
	
	/**
	* Launches the application
	* @throws InterruptedException
	*/
	public static void main(String[] args) throws InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu menu = new MainMenu();
					menu.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	/**
	* Constructor
	*/
	public MainMenu() {
		setupFrame();
		setupModeSelect();
		setupClocks();
		setupTextFields();
		setupModeLabels();
		setupGameButton();
		
		Thread game = new Thread() {
			public void run() {
				while (true) {
					mineCounter.setClock(mines.getTime());
					Tools.Wait(LATENCY);
				}
			}
		};
		game.start();
		
		contentPane.add(mainPanel);
	}
	
	/**
	* Game window setup
	*/
	private void setupFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 736, 614);
		setLocation(SCREEN_WIDTH / 2 - getWidth() / 2, SCREEN_HEIGHT / 2 - getHeight() / 2);
		setTitle(TITLE);
		setIconImage(new ImageIcon(Images.GAME_ICON).getImage());
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		topPanel = new JPanel();
		topPanel.setBackground(SystemColor.inactiveCaption);
		topPanel.setBorder(
		new MatteBorder(2, 4, 2, 4, (Color) new Color(153, 180, 209)));
		topPanel.setBounds(0, 0, 720, 45);
		contentPane.add(topPanel);
		topPanel.setLayout(null);
		
		int smileFaceSize = (int) (topPanel.getHeight() - COMPONENT_MARGIN * 1);
		smileFaceLbl = new JLabel("");
		smileFaceLbl.setBounds(
		topPanel.getWidth()  / 2 - smileFaceSize / 2,
		topPanel.getHeight() / 2 - smileFaceSize / 2,
		smileFaceSize,
		smileFaceSize
		);
		
		Image image = new ImageIcon(Images.SMILE_HAPPY).getImage();
		Image newimg = image.getScaledInstance(smileFaceLbl.getWidth(), smileFaceLbl.getHeight(), java.awt.Image.SCALE_SMOOTH);
		smileFaceLbl.setIcon(new ImageIcon(newimg));
		topPanel.add(smileFaceLbl);
	}
	
	/**
	* Difficulty selection dropdown
	*/
	private void setupModeSelect() {
		int comboBoxWidth = 80;
		int comboBoxHeight = topPanel.getHeight() - COMPONENT_MARGIN * 2;
		
		comboBox = new JComboBox<String>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBoxHandleAction();
			}
		});
		
		comboBox.setModel((ComboBoxModel<String>) new DefaultComboBoxModel<String>(new String[] {
			"Easy", "Medium", "Hard", "Custom"
		}));
		comboBox.setBounds(COMPONENT_MARGIN, COMPONENT_MARGIN, comboBoxWidth, comboBoxHeight);
		topPanel.add(comboBox);
	}
	
	/**
	* Difficulty selection changer
	*/
	private void comboBoxHandleAction() {
		String selected = (String) comboBox.getSelectedItem();
		GAMEMODE = Mode.getModeByName(selected, Modes);
		textFieldToggles(selected == CUSTOM.getName());
		displayModeInfo();
	}
	
	/**
	* Timer and Mine-Counter Clock setup
	*/
	private void setupClocks() {
		int clockW = 60;
		int clockH = topPanel.getHeight() - COMPONENT_MARGIN;
		
		mines = new Timer();
		mines.kill();
		mineCounter = new Clock(mines, clockW, clockH, smileFaceLbl.getX() - clockW - COMPONENT_MARGIN,
		COMPONENT_MARGIN / 2);
		topPanel.add(mineCounter);
		
		timer = new Timer();
		timeCounter = new Clock(timer, clockW, clockH, smileFaceLbl.getX() + smileFaceLbl.getWidth() + COMPONENT_MARGIN,
		COMPONENT_MARGIN / 2);
		clockMechanism = new Thread(timeCounter);
		clockMechanism.start();
		topPanel.add(timeCounter);
	}
	
	/**
	* Draw text fields
	*/
	private void setupTextFields() {
		ftFieldM = new JFormattedTextField();
		PlainDocument doc = (PlainDocument) ftFieldM.getDocument();
		IntFilter filter = new IntFilter();
		doc.setDocumentFilter(filter);
		ftFieldM.setBounds(119, 10, 28, 24);
		topPanel.add(ftFieldM);
		
		ftFieldW = new JFormattedTextField();
		doc = (PlainDocument) ftFieldW.getDocument();
		doc.setDocumentFilter(filter);
		ftFieldW.setBounds(176, 10, 28, 24);
		topPanel.add(ftFieldW);
		
		ftFieldH = new JFormattedTextField();
		doc = (PlainDocument) ftFieldH.getDocument();
		doc.setDocumentFilter(filter);
		ftFieldH.setBounds(233, 10, 28, 24);
		topPanel.add(ftFieldH);
		
		textFieldToggles(false);
		displayModeInfo();
	}
	
	/**
	* Sets visability of the the text fields
	*/
	private void textFieldToggles(boolean state) {
		ftFieldM.setEditable(state);
		ftFieldW.setEditable(state);
		ftFieldH.setEditable(state);
	}
	
	/**
	* Display difficulty information, size of board and number of mines 
	*/
	private void displayModeInfo() {
		ftFieldM.setText(String.valueOf(GAMEMODE.getMinesAmount()));
		ftFieldW.setText(String.valueOf(GAMEMODE.getMapWidth()));
		ftFieldH.setText(String.valueOf(GAMEMODE.getMapHeight()));
		mineCounter.setClock(GAMEMODE.getMinesAmount());
	}
	
	/**
	* Draw the lables for the board and number of mines
	*/
	private void setupModeLabels() {
		lblM = new JLabel("M:");
		lblM.setFont(modeLabelFont);
		lblM.setHorizontalAlignment(SwingConstants.CENTER);
		lblM.setBounds(100, 11, 20, 23);
		topPanel.add(lblM);
		
		lblW = new JLabel("W:");
		lblW.setFont(modeLabelFont);
		lblW.setHorizontalAlignment(SwingConstants.CENTER);
		lblW.setBounds(157, 11, 20, 23);
		topPanel.add(lblW);
		
		lblH = new JLabel("H:");
		lblH.setHorizontalAlignment(SwingConstants.CENTER);
		lblH.setFont(modeLabelFont);
		lblH.setBounds(214, 11, 20, 23);
		topPanel.add(lblH);
	}
	
	/**
	* Draw and initialize the Start button
	*/
	private void setupGameButton() {
		btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startAction();
			}
		});
		btnStart.setFont(new Font("Consolas", Font.BOLD, 13));
		btnStart.setUI(new CustomButton());
		
		int w = topPanel.getWidth() - COMPONENT_MARGIN * 2 - (timeCounter.getX() + timeCounter.getWidth());
		int h = 35;
		int x = timeCounter.getX() + timeCounter.getWidth() + COMPONENT_MARGIN;
		int y = 5;
		
		btnStart.setBounds(x, y, w, h);
		topPanel.add(btnStart);
		
		mainPanel = new JPanel();
		mainPanel.setBorder(new MatteBorder(2, 4, 6, 4, (Color) new Color(153, 180, 209)));
		mainPanel.setBackground(SystemColor.inactiveCaption);
		mainPanel.setBounds(0, 42, 720, 533);
		mainPanel.setLayout(null);
	}
	
	/**
	* Start button action handler
	*/
	private void startAction() {
		if (gameView != null) {
			mainPanel.remove(gameView);
			mainPanel.repaint();
		}
		if (GAMEMODE.getName() == CUSTOM.getName()) {
			boolean flag = true;
			flag &= GAMEMODE.setMapHeight(Integer.parseInt(ftFieldH.getText())) != null;
			flag &= GAMEMODE.setMapWidth(Integer.parseInt(ftFieldW.getText())) != null;
			flag &= GAMEMODE.setMineAmount(Integer.parseInt(ftFieldM.getText())) != null;
			
			if (!flag) {
				JOptionPane.showMessageDialog(mainPanel, "No value can be 0", "Invalid Value/s",
				JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if (GAMEMODE.getMapWidth() > 50) {
				GAMEMODE.setMapWidth(50);
				flag = false;
			}
			if (GAMEMODE.getMapHeight() > 50) {
				GAMEMODE.setMapHeight(50);
				flag = false;
			}
			
			if (!flag) {
				JOptionPane.showMessageDialog(mainPanel,
				"Width and Height must be less than 50.", "Invalid Value/s",
				JOptionPane.WARNING_MESSAGE);
			}
			
			if (GAMEMODE.getMinesAmount() >= GAMEMODE.getMapHeight() * GAMEMODE.getMapWidth()) {
				GAMEMODE.setMineAmount(GAMEMODE.getMapHeight() * GAMEMODE.getMapWidth() - 1);
				JOptionPane.showMessageDialog(mainPanel, "Too many mines.", "Invalid Value", JOptionPane.WARNING_MESSAGE);
			}
			displayModeInfo();
		}
		
		gameView = new GameView(mainPanel, smileFaceLbl, GAMEMODE, mines, timer);
		mines.set(GAMEMODE.getMinesAmount());
		timer.setup(0, 1000, 999);
		timer.on();
		
		Image image = new ImageIcon(Images.SMILE_HAPPY).getImage();
		Image newimg = image.getScaledInstance(smileFaceLbl.getWidth(), smileFaceLbl.getHeight(), java.awt.Image.SCALE_SMOOTH);
		smileFaceLbl.setIcon(new ImageIcon(newimg));
		
		mainPanel.add(gameView);
		mainPanel.repaint();
	}
}