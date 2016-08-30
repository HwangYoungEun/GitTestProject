import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 모양체 운동 : 15점 트레이닝(설명추가)
 * 
 * @author YoungEun
 */

public class Dots extends JFrame implements Runnable, WindowListener{
	int row = 0;
	String line;
	String[] str;
	boolean flag;
	
	// 버튼이 나타나는 순서
	int[][] order = {											
			{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4},
			{1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4},
			{2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4},
			{0, 0, 2}, {0, 1, 3}, {0, 2, 4},
			{1, 2, 4}, {1, 1, 3}, {1, 0, 2},
			{2, 0, 2}, {2, 1, 3}, {2, 2, 4},
			{0, 2}, {0, 1, 3}, {0, 0, 4},
			{1, 2}, {1, 1, 3}, {1, 0, 4},
			{2, 2}, {2, 1, 3}, {2, 0, 4},
	};

	public JButton btn[][] = new JButton[3][5];							// 버튼 객체 생성		

	ImageIcon icon;		
	Container contentPane;

	Color color = new Color(206,247,110);								// 색 설정		

	// 참고자료 : http://b-jay.tistory.com/123
	Dimension res = Toolkit.getDefaultToolkit().getScreenSize(); 		// 전체화면 사이즈 가져오기	

	// 설명서 설정	
	HowToUse htu2;								

	public void windowActivated(WindowEvent e) {}						// 윈도우가 활성화 될 때 호출
	public void windowClosed(WindowEvent e) {}							// 윈도우가 완전히 닫혀질 때 호출
	// 창을 닫았을 때 실행
	public void windowClosing(WindowEvent e) {
		finish();														// 창 닫으면 카운트되지않고 종료		
	}
	public void windowDeactivated(WindowEvent e) {}						// 윈도우가 비활성화될 때 호출
	public void windowDeiconified(WindowEvent e) {}						// 윈도우가 아이콘에서 이전 크기로 될 때 호출
	public void windowIconified(WindowEvent e) {}						// 윈도우가 아이콘화(최소화)될 때 호출
	public void windowOpened(WindowEvent e) {}							// 윈도우가 열릴 때 호출


	public void setOn(int i, int[][] j){
		for(int a =1 ; a <j[row].length; a++)
			btn[j[i][0]][j[i][a]].setVisible(true);
	}

	public void setOff(){
		for(int a = 0; a<btn.length; a++)
			for(int b = 0; b < btn[a].length; b++)
				btn[a][b].setVisible(false);
	}

	Dots() {
		super("Fifteen Dots");

		// 캐릭터 이미지
		icon = new ImageIcon("image\\heart.png");				

		contentPane = getContentPane();									
		contentPane.setLayout(new GridLayout(3, 5, 5, 5)); 				// 배치관리자 삭제			
		contentPane.setBackground(color);								// 배경 샐깔 적용	
		this.addWindowListener(this);

		flag = false;

		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 5; j++){
				btn[i][j] = new JButton(icon);
				btn[i][j].setSize(res.width/15, res.width/15);			// 모니터 해상도에 따른 버튼 사이즈 조절

				// 버튼 정렬
				btn[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				btn[i][j].setVerticalAlignment(SwingConstants.CENTER);

				// 버튼 디자인 정리
				btn[i][j].setBorderPainted(false); 						// 버튼 경계선 제거					
				btn[i][j].setFocusPainted(false);						// 포커스(선택했던 버튼 표시) 제거
				btn[i][j].setContentAreaFilled(false);					// 버튼영역 배경 제거

				contentPane.add(btn[i][j]);								// 컨텐트팬에 버튼 부착			
			}
		}

		// 모니터 크기만큼 창 크기 설정
		setSize(res.width, res.height);									// 모니터 크기만큼 창 크기 설정				
		setVisible(true);												// 창을 보이게 함

		htu2 = new HowToUse(2);											// 운동에 대한 설명서					

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);				// 닫기 버튼을 눌렀을 때 해당 창만 종료			
	}

	void finish(){
		flag = true;
	}

	public void run(){
		int k = -4;

		while(true){
			try{				
				//Thread.sleep(1000);   									// 디버깅용
				Thread.sleep(2000);   										// 2초씩 넘어감							

				if(flag == true){
					// 설명서 닫기
					htu2.finish();								
					// 프레임 종료
					dispose();											
					return;
				}

			}catch(InterruptedException e){
				System.err.println(e);
				return;
			}

			k++;

			// 2초가 지난후 설명이 사라지게 하기
			if(k == -1)
				htu2.finish();										

			if(k > 0){
				// 버튼 모두 끄기	
				setOff();																						
				setOn(row, order);

				row++;
			}

			if(row == 33){
				MainFrame.FDC++;
				if(MainFrame.FDC > MainFrame.FD) MainFrame.FDC = MainFrame.FD;
				// 기록 저장
				Save.SaveNow();									

				// 프레임 종료
				dispose();											
				return;
			}
		}
	}
}
