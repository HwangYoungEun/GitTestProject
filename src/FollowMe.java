import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * 안구이동근 운동 : 움직이는 점 트레이닝(설명추가)
 * 
 * @author Sol
 * @see HowToUse, Save
 * @param static int speedSet - 공 스피드 디폴트 값(랜덤)
 * @param static int sizeSet - 공 크기 디폴트 값(중)
 * @param boolean flag - 종료 플래그
 * @param boolean play - 반복 플래그
 * @param JButton btn - 설정창을 불러오기 위한 버튼
 * @param Color color - 백그라운드 색깔 지정(연두색)
 * @param Dimention res - 전체화면 사이즈를 가져오기 위한 변수
 * @param int speed1[] - 창크기에 따른 공 스피드(하, 중, 상)
 * @param int speed2[] - 창크기에 따른 랜덤 공 스피드
 * @param int size[] - 컴퓨터 창 크기 따른 상대적인 공 크기(중, 하)
 */
public class FollowMe extends JFrame implements Runnable, WindowListener{
	public static int speedSet = 3;
	public static int sizeSet = 0;
	Setting setting;					
	Container contentPane;
	boolean flag;
	boolean play;
	JButton btn; 						
	HowToUse htu1;						

	//백그라운드 색깔 설정
	Color color = new Color(206,247,110);		
	
	//전체화면 사이즈 가져오기
	Dimension res = Toolkit.getDefaultToolkit().getScreenSize(); 

	String [] speedText = {"하", "중", "상", "랜덤"};
	String [] sizeText = {"중", "소"};

	int speed1[]= {res.width/350, res.width/250, res.width/150};	
	int speed2[] = {res.width/350, res.width/250, res.width/200, res.width/50};				
	int size[] = {res.width/15, res.width/20};													

	//창이 포커스를 가지고 있다면 실행
	public void windowActivated(WindowEvent e) {
		play = true;
	}
	public void windowClosed(WindowEvent e) {}
	//창을 닫았을 때 실행
	public void windowClosing(WindowEvent e) {
		finish();								
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	//창에 포커스가 가지않는다면 실행
	public void windowIconified(WindowEvent e) {
		play = false;
	}
	public void windowOpened(WindowEvent e) {}

	/**
	 * FollowMe 생성자
	 * 
	 * 배치관리자를 삭제하고 공이 움직이는 패널과 설정 버튼 추가
	 * 모니터 크기만큼 창크기를 설정하고 설정 버튼을 눌렀을 경우 설정창이 화면 중앙에 나오도록 설정
	 */
	FollowMe() {
		super("FolloFollow Me");
		contentPane = getContentPane();
		
		//배치관리자를 삭제하고 백그라운드 색상을 설정한다.
		contentPane.setLayout(null); 	
		contentPane.setBackground(color);			
		
		//공이 움직이는 패널 생성하고 백그라운드 색상과 범위를 설정한다.
		DrawCircle panel = new DrawCircle();					
		panel.setSize(res.width-100,res.height); 					
		panel.setBackground(color);						
		
						
		
		this.addWindowListener(this);

		flag = false;
		play = true;

		//버튼을 생성하고 사이즈와 위치를 설정한다.
		btn = new JButton("메뉴"); 									
		btn.setSize(50,50);									
		btn.setLocation(res.width-70,30);							
		

		//버튼 마우스 리스너 추가
		btn.addActionListener(new ActionListener() {				
			public void actionPerformed(ActionEvent e){
				//설정 창 보이기
				setting.setVisible(true);							
			}
		});

		//컨텐트 펜에 패널과 버튼 부착
		contentPane.add(panel); 
		contentPane.add(btn);
		
		//설정창을 생성하고 화면 중앙에 나오도록 설정한다.
		setting = new Setting(this, "설정");							
		setting.setLocationRelativeTo(null);						

		//모니터 크기만큼 창 크기 설정
		setSize(res.width, res.height);								
		setVisible(true);

		//운동에 대한 설명서
		htu1 = new HowToUse(1);										

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * 운동을 종료하는 메소드
	 * 
	 * flag가 true가 되면 창이 꺼진다.
	 */
	void finish(){
		flag = true;
	}

	/**
	 * 운동이 계속 돌아가는 메소드
	 * 
	 * 5초가 지나면 설명문을 사라지게 하고 10초가 지나면 설정창이 사라지게 하며 60초동안 운동을 시작한다
	 * 운동을 끝까지 했다면 FMC을 카운트하고 카운트가 설정한 목표를 넘어가지 않도록 설정한다.
	 * 마지막으로 기록을 저장하고 창을 닫는다.
	 */
	public void run(){
		int k = -10;
		while(play){
			try{
				Thread.sleep(1000);   

				if(flag == true){
					dispose();				
					return;
				}
			}catch(InterruptedException e){
				System.err.println(e);
				return;
			}

			k++;

			if(k == -5)
				htu1.finish();			

			if(k == 0)
				setting.setVisible(false);

			if(k == 60){				
				MainFrame.FMC++;
				if(MainFrame.FMC > MainFrame.FM) MainFrame.FMC = MainFrame.FM;		
				Save.SaveNow();			

				dispose();				
				return;
			}
		}
	}

	/**
	 * 공이 움직이는 패널
	 * 
	 * 공이 랜덤으로 패널 안을 이동한다.
	 * @author Sol
	 * @param xPos - 공이 처음 시작하는 x 위치
	 * @param yPos - 공이 처음 시작하는 y 위치
	 * @param speedX - x좌표로의 스피드(초기값 10)
	 * @param speedY - y좌표로의 스피드(초기값 10)
	 * @param speedx - x좌표의 다음 스피드 저장
	 * @param speedy - y좌표의 다음 스피드 저장
	 * @param icon - 공이미지 설정
	 * @param img - 아이콘의 이미지를 저장, 이 변수로 공의 이미지 설정
	 */
	public class DrawCircle extends JPanel {						
		private int xPos=200; 			
		private int yPos=100; 				

		int speedX; 					
		int speedY; 					
		int speedx;						
		int speedy;							

		ImageIcon icon = new ImageIcon("image\\heart.png");	
		Image img = icon.getImage();

		public DrawCircle() {
			speedX = speedY = 10;
			
			try {
				new Thread(){
					public void run(){
						while(true){
							Random rd = new Random();

							/*
							 * Random.nextInt(int n) : 0과 (n-1)사이의 int형의 난수를 발생한다.
							 * dir은 0~1의 난수를 만들어서 스피드를 음수로 할 지 양수로 할 지 결정
							 * rnd는 랜덤 스피드를 골랐을 경우 스피드를 바꾸기 위해 설정
							 */
							int x_dir = rd.nextInt(2);  		
							int y_dir = rd.nextInt(2);

							int x_rnd = rd.nextInt(4);				
							int y_rnd = rd.nextInt(4);

							//하, 중, 상을 골랐을 경우
							if(speedSet <3){							
								speedx= speed1[speedSet];
								speedy= speed1[speedSet];
							}
							
							//랜덤을 골랐을 경우
							if(speedSet ==3){							
								speedx = speed2[x_rnd];
								speedy = speed2[y_rnd];
							}

							//좌표 이동
							xPos = xPos + speedX; 					
							yPos = yPos + speedY; 					

							//패널 창의 끝에 닿으면 그 반대로 움직이도록 설정, 이때 dir이 0이면 다른 축의 방향은 그대로
							if(xPos + size[sizeSet] >=DrawCircle.this.getWidth() ){
								speedX = -speedx;
								speedY = (y_dir == 0 ? speedy : -speedy);	
							}

							if( yPos+ size[sizeSet] >=DrawCircle.this.getHeight()){
								speedY = -speedy;
								speedX = (x_dir == 0 ? speedx : -speedx);	
							}
							if(xPos < 0){
								speedX = speedx;
								speedY = (y_dir == 0 ? speedy : -speedy);
							}

							if(yPos < 0){
								speedY = speedy;
								speedX = (x_dir == 0 ? speedx : -speedx);
							}

							//공이 움직이기 때문에 다시 그린다.
							repaint(); 										
							
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 공에 대한 이미지와 위치를 설정하는 메소드
		 */
		public void paintComponent(Graphics g) {									
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;

			//공에 대한 설정을 오른쪽 위에 그리기
			g.drawString("공 크기 : "+sizeText[sizeSet], res.width-170, 50);				
			g.drawString("공 속도 : "+speedText[speedSet], res.width-170, 70);

			//x, y 위치에 size만큼의 이미지 그리기
			g2d.drawImage(img, xPos, yPos, size[sizeSet], size[sizeSet], this);		
		}
	}
}

/**
 * 공 설정창 클래스
 * 
 * 설명추가
 * @author Sol
 * @param speedTmp - 설정된 값을 적용하기 전의 값을 보관하는 임시 공 스피드 (초기값 : 랜덤)
 * @param sizeTmp - 설정된 값을 적용하기 전의 값을 보관하는 임시 공 크기 (초기값 : 중)
 */
class Setting extends JDialog {
	Container c;

	int speedTmp = 3;				
	int sizeTmp = 0;			

	JLabel sizeLabel = new JLabel("공 크기");
	JLabel speedLabel = new JLabel("공 속도");
	
	//공 스피드 라디오 버튼 생성
	JRadioButton [] speedBTN = new JRadioButton[4];	
	//공 크기 라디오 버튼 생성
	JRadioButton [] sizeBTN = new JRadioButton[2];			

	String [] speedText = {"하", "중", "상", "랜덤"};
	String [] sizeText = {"중", "소"};

	public Setting(JFrame frame, String title) {
		super(frame, title);

		c = getContentPane();
		c.setLayout(null);

		//공 속도 선택 그룹에 라디오 버튼을 넣고 리스너를 단다. 설정된 값(초기조건:랜덤)이 선택된 상태로 나오게 함
		ButtonGroup gspeed = new ButtonGroup();			
		for(int i=0; i<speedBTN.length; i++){
			speedBTN[i] = new JRadioButton(speedText[i]);		
			gspeed.add(speedBTN[i]);						
			speedBTN[i].addItemListener(new SpListener()); 
		}
		speedBTN[FollowMe.speedSet].setSelected(true); 					

		//공 크기 선택 그룹에 라디오 버튼을 넣고 리스너를 단다. 설정된 값(초기조건:중)이 선택된 상태로 나오게 함
		ButtonGroup gsize = new ButtonGroup();			
		for(int i=0; i<sizeBTN.length; i++){
			sizeBTN[i] = new JRadioButton(sizeText[i]);		
			gsize.add(sizeBTN[i]);							
			sizeBTN[i].addItemListener(new SzListener());	
		}
		sizeBTN[FollowMe.sizeSet].setSelected(true); 					

		//확인 버튼
		JButton ok = new JButton("확인");
		ok.setBackground(new Color(93,93,93));
		ok.setForeground(Color.white);

		//버튼 UI 설정
		ok.setUI(new StyledButtonUI());
		
		//확인 버튼을 누르면 선택된 공 스피드, 사이즈로 설정하고 설정창을 사라지게 한다.
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FollowMe.speedSet = speedTmp;			
				FollowMe.sizeSet = sizeTmp;			
				setVisible(false);				
			}
		});

		//취소 버튼
		JButton cancel = new JButton("취소");
		cancel.setBackground(new Color(93,93,93));
		cancel.setForeground(Color.white);
		
		//버튼 UI 설정
		cancel.setUI(new StyledButtonUI());
		
		//취소 버튼을 누르면 그냥 설정창을 사라지게 한다.
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);				
			}
		});

		//공 크기 텍스트와 라디오 버튼 위치지정
		sizeLabel.setLocation(135,20);
		sizeLabel.setSize(200,30);
		sizeBTN[0].setLocation(55,50);
		sizeBTN[0].setSize(75,30);
		sizeBTN[1].setLocation(185,50);
		sizeBTN[1].setSize(75,30);

		//공 속도 텍스트와 라디오 버튼 위치지정
		speedLabel.setLocation(135,120);
		speedLabel.setSize(200,30);
		for(int i=0; i<speedBTN.length; i++){
			speedBTN[i].setLocation(20+70*i,150);
			speedBTN[i].setSize(60,30);
		}

		//확인 취소 버튼 위치 지정
		ok.setLocation(55,220);
		ok.setSize(75,30);
		cancel.setLocation(185,220);
		cancel.setSize(75,30);

		//버튼을 창에 부착한다.
		c.add(sizeLabel);
		c.add(speedLabel);
		c.add(ok);
		c.add(cancel);
		for(int i=0; i<sizeBTN.length; i++){
			c.add(sizeBTN[i]);
		}
		for(int i=0; i<speedBTN.length; i++){
			c.add(speedBTN[i]);
		}

		setSize(350,300);
		setVisible(true);
		
		//창 크기를 못 늘리게 고정
		setResizable(false);		
	}

	/**
	 * 공 스피드 버튼 리스너
	 * @author Sol
	 * 선택된 버튼에 따라 임시 스피드 변수에 스피드 값 저장
	 */
	class SpListener implements ItemListener {						
		public void itemStateChanged(ItemEvent e){
			if(e.getStateChange() == ItemEvent.DESELECTED)
				return; 

			if(speedBTN[0].isSelected())
				speedTmp = 0;
			else if(speedBTN[1].isSelected())
				speedTmp = 1;
			else if(speedBTN[2].isSelected())
				speedTmp = 2;
			else
				speedTmp = 3;
		}
	}

	/**
	 * 공 사이즈 버튼 리스너
	 * @author Sol
	 * 선택된 버튼에 따라 임시 사이즈 변수에 사이즈 값 저장
	 */
	class SzListener implements ItemListener {						
		public void itemStateChanged(ItemEvent e){
			if(e.getStateChange() == ItemEvent.DESELECTED)
				return; 

			if(sizeBTN[0].isSelected())
				sizeTmp = 0;
			else
				sizeTmp = 1;
		}
	}
}

/**
 * 버튼 UI 설정 클래스
 * @author Sol
 *
 */
class StyledButtonUI extends BasicButtonUI {					
	@Override
	public void installUI (JComponent c) {
		super.installUI(c);
		AbstractButton button = (AbstractButton) c;
		button.setOpaque(false);
		button.setBorder(new EmptyBorder(5, 15, 5, 15));
	}

	@Override
	public void paint (Graphics g, JComponent c) {
		AbstractButton b = (AbstractButton) c;
		paintBackground(g, b, b.getModel().isPressed() ? 2 : 0);
		super.paint(g, c);
	}

	private void paintBackground (Graphics g, JComponent c, int yOffset) {
		Dimension size = c.getSize();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(c.getBackground().darker());
		g.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
		g.setColor(c.getBackground());
		g.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
	}
}