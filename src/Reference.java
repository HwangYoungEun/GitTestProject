import java.awt.*;
import javax.swing.*;

/**
 * 개발자 및 학교 & 학과 소개
 * 
 * @author YoungEun
 */

public class Reference extends JFrame{
	Container c;												// 컨테이너 변수 선언								
	
	public Reference(){
		setTitle("소개");											// 창 이름은 "소개"		
		c = getContentPane();									// 프레임에 연결된 컨텐트팬을 알아냄						
		MyPanel panel = new MyPanel();							// MyPanel 객체 생성						
		c.add(panel, BorderLayout.CENTER);						// panel을 BorderLayout의 중앙 위로 배치					

		setSize(1084, 1125);									// 창 크기 설정							
		setLocationRelativeTo(null);							// 화면의 가운데에 창을 띄움					
		setVisible(true);  										// 창을 보이게 함									
		setResizable(false);									// 창 크기를 못 늘리게 고정									
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		// 닫기 버튼을 눌렀을 때 해당 창만 종료	
	}
	
	// 이미지 가져옴
	class MyPanel extends JPanel{
		ImageIcon icon = new ImageIcon("image\\Ref.jpg");		// ImageIcon 객체 생성	
		Image img = icon.getImage();							// 이미지 가져옴		

		/**
		 * 이미지 그리기
		 */
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(img,0,0, getWidth(), getHeight(),this);	// 이미지를 (0,0) 좌표에서 시작해 해당 이미지의 너비와 높이만큼 출력
		}
	}
}
