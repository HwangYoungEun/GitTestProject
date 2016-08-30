import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * 메인창
 * 
 * 설명추가
 * @author Sol
 * @param public static int year - 연도 지정
 * @param public static int month - 월 지정
 * @param public static int day - 일 지정
 * @param public static int FM - Follow Me 목표 횟수
 * @param public static int FMC - Follow Me 카운트
 * @param public static int FD - Fifteen Dots 목표 횟수
 * @param public static int FDC - Fifteen Dots 카운트
 * 
 * @param private JLabel date - 현재 날짜, 시각
 * @param private JButton record - 월간/주간 목표 그래프
 * @param private JButton graph - 오늘 목표달성치(%)
 * @param private JButton goal - 목표 설정
 * @param private JButton exercise1 - 운동 1 : Follow Me
 * @param private JButton exercise2 - 운동 2 : 15 dots
 * @param private JButton exercise3 - 운동 3 : Brightness
 * @param private JButton referenc - 개발자 및 앱 소개
 *
 */
class MainFrame extends JFrame implements WindowListener {
	public static int year = -1;		
	public static int month = -1;
	public static int day = -1;
	public static int FM = -1; 
	public static int FMC = -1; 
	public static int FD = -1; 
	public static int FDC = -1; 

	private JLabel date;
	private JButton record; 
	private JButton graph; 
	private JButton goal;
	private JButton exercise1;
	private JButton exercise2; 
	private JButton exercise3; 
	private JButton reference; 

	private Calendar cal;

	//창이 포커스를 받을 때마다 기록을 불러오고 하루가 바꼈다면 기록 저장
	public void windowActivated(WindowEvent e) {
		Save.Load();										
		Save.SaveDay();			
	}
	
	public void windowClosed(WindowEvent e) {
	}

	//창을 닫으면 트레이를 시작하고 시계 쓰레드를 종료한다. 트레이로 돌려보냈을 시에 지정시간(50분)이 되면 운동 중 하나가 자동으로 실행하게 함
	public void windowClosing(WindowEvent e) {
		new Tray(); 
		DateJLabel.stop(); 
		Thread Waiting = new Thread(new Waiting()); 
		Waiting.start();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	//창을 내렸을 경우 지정시간(50분)이 되면 운동 중 하나가 자동으로 실행하게 함
	public void windowIconified(WindowEvent e) {
		Thread Waiting = new Thread(new Waiting()); 
		Waiting.start();
	}

	//창을 올리면 Waiting 종료
	public void windowDeiconified(WindowEvent e) {
		Waiting.finish(); 
	}

	//트레이에서 돌아오면 Waiting 종료
	public void windowOpened(WindowEvent e) {
		Waiting.finish(); 
	}

	//창을 가운데에 띄우고 시계, 기록, 운동들, 레퍼런스 버튼 달기
	MainFrame() {
		setTitle("Oclulus");
		setSize(750, 1000);
		setLocationRelativeTo(null);
		setVisible(true);

		this.addWindowListener(this);
		
		//달력초기화
		cal = Calendar.getInstance(); 
		
		Save.Load();			

		//시계 생성
		date = new JLabel("현재 날짜시각", null, SwingConstants.CENTER);

		//기록 버튼 생성과 버튼 색상 설정
		record = new JButton("월간 기록 확인");
		record.setFont(new Font("Gothic", Font.BOLD, 20));
		record.setBackground(new Color(93, 93, 93));
		record.setForeground(Color.white);
		record.setUI(new StyledButtonUI()); 
		
		//그래프 버튼 생성과 버튼 색상 설정
		graph = new JButton("오늘 목표달성율(%)");
		graph.setFont(new Font("Gothic", Font.BOLD, 20));
		graph.setBackground(new Color(93, 93, 93));
		graph.setForeground(Color.white);
		graph.setUI(new StyledButtonUI()); 
		
		//설정 버튼 생성과 버튼 색상 설정
		goal = new JButton("운동 횟수 설정");
		goal.setFont(new Font("Gothic", Font.BOLD, 20));
		goal.setBackground(new Color(93, 93, 93));
		goal.setForeground(Color.white);
		goal.setUI(new StyledButtonUI());
		
		//Follow Me 버튼 생성과 버튼 색상 설정
		exercise1 = new JButton("Follow Me");
		exercise1.setFont(new Font("Arial", Font.BOLD, 20));
		exercise1.setBackground(new Color(93, 93, 93));
		exercise1.setForeground(Color.white);
		exercise1.setUI(new StyledButtonUI());
		
		//Fifteen Dots 버튼 생성과 버튼 색상 설정
		exercise2 = new JButton("Fifteen Dots");
		exercise2.setFont(new Font("Gothic", Font.BOLD, 20));
		exercise2.setBackground(new Color(93, 93, 93));
		exercise2.setForeground(Color.white);
		exercise2.setUI(new StyledButtonUI()); 
		
		//Brightness 버튼 생성과 버튼 색상 설정
		exercise3 = new JButton("홍채 운동");
		exercise3.setFont(new Font("Gothic", Font.BOLD, 20));
		exercise3.setBackground(new Color(93, 93, 93));
		exercise3.setForeground(Color.white);
		exercise3.setUI(new StyledButtonUI()); 
		
		//소개 버튼 생성과 버튼 색상 설정
		reference = new JButton("가천대학교 의용생체공학과 소개");
		reference.setFont(new Font("Gothic", Font.BOLD, 20));
		reference.setBackground(new Color(93, 93, 93));
		reference.setForeground(Color.white);
		reference.setUI(new StyledButtonUI()); 
		
		Container mContainer = getContentPane();
		mContainer.setLayout(new GridLayout(4, 2, 30, 30)); 
		
		//버튼 추가
		mContainer.add(date);
		mContainer.add(record);
		mContainer.add(graph);
		mContainer.add(goal);
		mContainer.add(exercise1);
		mContainer.add(exercise2);
		mContainer.add(exercise3);
		mContainer.add(reference);
	}

	//Record 버튼 : 리스너 & 스레드
	public void runRecord() {
		record.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Record record = new Record();
				record.run();
			}
		});
	}

	public void runGraph() {
		graph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph graph = new Graph();
			}
		});
	}

	//Goal 버튼 : 리스너 & 스레드
	public void runGoal() {
		goal.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Thread goal = new Thread(new Goal());
				goal.start();
			}
		});
	}

	//Follow Me 버튼 : 리스너 & 스레드
	public void runExercise1() {
		exercise1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread followMe = new Thread(new FollowMe());
				followMe.start();
			}
		});
	}

	//Dots 버튼 : 리스너 & 스레드
	public void runExercise2() {
		exercise2.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Thread dots = new Thread(new Dots());
				dots.start();
			}
		});
	}

	//Brightness 버튼 : 리스너 & 스레드
	public void runExercise3() {
		exercise3.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Thread brightness = new Thread(new Brightness());
				brightness.start();
			}
		});
	}

	public void runReference() {
		reference.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reference reference = new Reference();
			}
		});
	}

	public void setDate(JLabel date) {
		this.date = date;
	}

	public void setRecord() {
		return;
	}

	public void setGraph() {
		return;
	}

	public void setGoal() {
		return;
	}

	public void setExercise1() {
		return;
	}

	public void setExercise2() {
		return;
	}

	public void setExercise3() {
		return;
	}

	public void setReference() {
		return;
	}

	public JLabel getDate() {
		return date;
	}

	public JButton getRecord() {
		return record;
	}

	public JButton getGraph() {
		return graph;
	}

	public JButton getGoal() {
		return goal;
	}

	public JButton getExercise1() {
		return exercise1;
	}

	public JButton getExercise2() {
		return exercise2;
	}

	public JButton getExercise3() {
		return exercise3;
	}

	public JButton getReference() {
		return reference;
	}
}

/*시계, 운동 쓰레드 시작*/
public class Main {
	public static void main(String[] args) {
		MainFrame mFrame = new MainFrame();

		Calendar now = Calendar.getInstance(); 

		Thread dateJLabel = new Thread(new DateJLabel(now, mFrame.getDate())); 
		
		dateJLabel.start();

		//쓰레드 생성
		mFrame.runRecord();
		mFrame.runGraph();
		mFrame.runGoal();
		mFrame.runExercise1();
		mFrame.runExercise2();
		mFrame.runExercise3();
		mFrame.runReference();
	}


	/**
	 * 버튼 UI 설정 클래스
	 * @author Sol
	 *
	 */
	class StyledButtonUI extends BasicButtonUI {

		@Override
		public void installUI(JComponent c) {
			super.installUI(c);
			AbstractButton button = (AbstractButton) c;
			button.setOpaque(false);
			button.setBorder(new EmptyBorder(5, 15, 5, 15));
		}

		@Override
		public void paint(Graphics g, JComponent c) {
			AbstractButton b = (AbstractButton) c;
			paintBackground(g, b, b.getModel().isPressed() ? 2 : 0);
			super.paint(g, c);
		}

		private void paintBackground(Graphics g, JComponent c, int yOffset) {
			Dimension size = c.getSize();
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(c.getBackground().darker());
			g.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
			g.setColor(c.getBackground());
			g.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
		}
	}
}
