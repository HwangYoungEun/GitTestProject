import javax.swing.*;
import java.awt.*;

/**
 * 운동에 대한 설명문
 * 
 * 선택된 운동 종류에 해당하는 설명 이미지를 가져옴
 * @author YoungEun
 */

public class HowToUse extends JFrame{
	Container c;														// 컨테이너 변수 선언								
	ImageIcon icon;														// 이미지 아이콘 변수 선언
	
	/**
	 * 운동 선택
	 * 
	 * @param ex - 운동 종류 (1: FollowME, 2: FifteenDots, 3: Brightness)
	 */
	public HowToUse(int ex){											// 전달인자 'ex'는 운동 종류 (1: FollowME, 2: FifteenDots, 3: Brightness)						
			
		// 운동종류에 따라 설명문 바꾸기
		switch(ex){
		case 1:															// FollowMe 실행 시
			icon = new ImageIcon("image\\use1.jpg");					// 이미지는 'use1'		
			break;				
		case 2:															// FifteenDots 실행 시		
			icon = new ImageIcon("image\\use2.jpg");					// 이미지는 'use2'		
			break;
		case 3:															// Brightness 실행 시	
			icon = new ImageIcon("image\\use3.jpg");					// 이미지는 'use3'	
			break;
		default :
			break;
		}
		
		setTitle("How To Use");											// 창이름은 "How To Use"					
		c = getContentPane();											// 프레임에 연결된 컨텐트팬을 알아냄
		MyPanel panel = new MyPanel();									// MyPaenl 객체 생성
		c.add(panel, BorderLayout.CENTER);								// panel을 BorderLayout의 중앙 위로 배치	
		
		setSize(1043, 471);												// 설명문 사이즈 조절			
		setLocationRelativeTo(null);									// 화면의 가운데에 창을 띄움
		setVisible(true);												// 창을 보이게 함
		setResizable(false);			 								// 창 크기를 못 늘리게 고정
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);				// 닫기 버튼을 눌렀을 때 해당 창만 종료
	}

	public boolean finish(){				
		dispose();
		return true;
	}
	
	// 이미지 가져옴
	class MyPanel extends JPanel{
		Image img = icon.getImage();									

		/**
		 * 이미지 그리기
		 */
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(img,0,0, getWidth(), getHeight(),this);			// 이미지를 (0,0) 좌표에서 시작해 해당 이미지의 너비와 높이만큼 출력
		}
	}
}
