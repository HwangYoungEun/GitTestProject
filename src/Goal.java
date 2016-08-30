import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 운동들의 목표 횟수 설정창
 * 
 * @author Sol
 * @param int yet[] - 설정된 값을 적용하기 전의 값을 보관하는 변수
 */
public class Goal extends JFrame implements Runnable{
	Container contentPane;

	//Fifteen Dots 목표 횟수 버튼
	JRadioButton[] FDSet= new JRadioButton[2];
	//Follow Me 목표 횟수 버튼
	JRadioButton[] FMSet = new JRadioButton[2];			

	int yet [] = new int[2];

	Goal(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * 배치관리자를 삭제하고 버튼 위치를 지정한다. 설정창은 창 가운데에 나오게 하며 창크기를 못 늘리도록 고정시킨다. 
	 * 목표횟수에 따라 라디오 버튼이 선택된 상태로 나오게 한다.
	 * 
	 */
	public void run(){
		setTitle("Setting");

		contentPane = getContentPane();
		contentPane.setLayout(null); 					

		//Fifteen Dots, FollowMe 버튼 그룹 생성
		ButtonGroup GFD = new ButtonGroup();			
		ButtonGroup GFM = new ButtonGroup();			

		//Fifteen Dots 버튼  생성하고 리스너 달기
		FDSet[0]=new JRadioButton("9회");				
		FDSet[1]=new JRadioButton("15회");	
		FDSet[0].addItemListener(new SetFDListener());	
		FDSet[1].addItemListener(new SetFDListener());

		//그룹에 버튼 추가
		GFD.add(FDSet[0]);
		GFD.add(FDSet[1]);

		//Fifteen Dots 목표횟수가 9회이면 첫번째 라디오 버튼이 선택된 상태로 15회면 두번째 라디오 버튼이 선택된 상태로 나오게 함
		if(MainFrame.FD == 9)
			FDSet[0].setSelected(true); 			
		else
			FDSet[1].setSelected(true);		

		//Follow Me 버튼  생성하고 리스너 달기
		FMSet[0]=new JRadioButton("9회");				
		FMSet[1]=new JRadioButton("15회");
		FMSet[0].addItemListener(new SetFMListener());	
		FMSet[1].addItemListener(new SetFMListener());
		
		//그룹에 버튼 추가
		GFM.add(FMSet[0]);
		GFM.add(FMSet[1]);

		//Follow Me 목표횟수가 9회이면 첫번째 라디오 버튼이 선택된 상태로 15회면 두번째 라디오 버튼이 선택된 상태로 나오게 함
		if(MainFrame.FM ==9)
			FMSet[0].setSelected(true);					
		else
			FMSet[1].setSelected(true);					

		//확인 버튼을 눌렀을 때 목표 횟수를 변경하고 Today.txt에 설정된 목표횟수를 다시 기록한다
		JButton ok = new JButton("확인");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.FM = yet[0];				
				MainFrame.FD = yet[1];				

				Save.SaveNow();						
				
				dispose();							
			}
		});

		//취소 버튼을 눌렀을 때 아무런 변화없이 설정창만 닫는다.
		JButton cancel = new JButton("취소");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();							
			}
		});

		//항목 설정
		JLabel Eye1 = new JLabel("Follow Me");
		JLabel Eye2 = new JLabel("Fifteen Dots");

		//버튼과 항목 사이즈, 위치 설정
		Eye1.setSize(70,20);
		Eye2.setSize(70,20);
		ok.setSize(75,30);
		cancel.setSize(75,30);

		FDSet[0].setSize(60,20);
		FMSet[0].setSize(70,20);
		FDSet[1].setSize(60,20);
		FMSet[1].setSize(70,20);

		Eye1.setLocation(120,80);
		Eye2.setLocation(310,80);
		ok.setLocation(140,200);
		cancel.setLocation(250,200);

		FMSet[0].setLocation(85,110);
		FMSet[1].setLocation(155,110);

		FDSet[0].setLocation(285,110);
		FDSet[1].setLocation(345,110);

		contentPane.add(Eye1);
		contentPane.add(Eye2);
		contentPane.add(ok);
		contentPane.add(cancel);

		contentPane.add(FMSet[0]);
		contentPane.add(FDSet[0]);
		contentPane.add(FMSet[1]);
		contentPane.add(FDSet[1]);

		setSize(500, 300);
		setLocationRelativeTo(null);
		setVisible(true);
		//창 크기를 못 늘리게 고정
		setResizable(false);				
	}

	//횟수 버튼 리스너, 확인을 누르기 전 임시로 설정된 변수에 값을 저장한다.
	class SetFMListener implements ItemListener {
		public void itemStateChanged(ItemEvent e){	
			if(e.getStateChange() == ItemEvent.DESELECTED)
				return; 
			
			if(FMSet[0].isSelected())				
				yet[0] = 9;
			else 
				yet[0] = 15;
		}
	}

	//횟수 버튼 리스너, 확인을 누르기 전 임시로 설정된 변수에 값을 저장한다.
	class SetFDListener implements ItemListener {
		public void itemStateChanged(ItemEvent e){	
			if(e.getStateChange() == ItemEvent.DESELECTED)
				return;
			
			if(FDSet[0].isSelected())				
				yet[1] = 9;
			else 
				yet[1] = 15;
		}
	}
}
