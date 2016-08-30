import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;
import java.util.Calendar;


import javax.swing.JOptionPane;

/**
 * 메인창에서 닫기 버튼을 눌렀을 시에 나타나는 트레이
 * 
 * @author Sol
 * @see Waiting
 * @param SystemTray tray - 트레이 생성
 * @param PopupMenu popup - 팝업 메뉴 생성
 * @param MenuItem Open, Close - 열기 닫기 메뉴
 * @param CheckboxMenuItem Wait - 자동시작 여부를 위한 체크박스
 *
 */
public class Tray implements ActionListener, ItemListener{
	private SystemTray tray;
	private PopupMenu popup;
	private MenuItem open, close;
	private CheckboxMenuItem wait;
	private TrayIcon icon;

	public Tray(){
		try {
			setup();
		} catch(AWTException awte){
			System.out.println("Error TRAY");
			System.out.println(awte.toString());
		}
	}

	/**
	 * 트레이 설정
	 */
	public void setup() throws AWTException{
		//현재 os에서 사용가능하다면 트레이 설정 
		if(SystemTray.isSupported()){
			//메뉴 달기 
			popup = new PopupMenu();
			open = new MenuItem("Open");				
			close = new MenuItem("Exit");			
			wait = new CheckboxMenuItem("Auto Start", true);	

			//액션 리스너 달기
			open.addActionListener(this);
			close.addActionListener(this);
			wait.addItemListener(this);

			//팝업에 메뉴 달기
			popup.add(open);
			popup.add(wait);
			popup.add(close);


			//트레이아이콘 설정
			Image image = Toolkit.getDefaultToolkit().getImage("image\\logo.png");
			icon = new TrayIcon(image, "Oculus", popup);
			icon.setImageAutoSize(true);

			//트레이 아이콘 근처로 팝업창 뜨게하기
			icon.displayMessage("Oculus", "트레이로 이동합니다.", MessageType.INFO);

			//더블클릭했을 시에 메인 창이 뜨도록 함.(의문점 : MousePressed를 이용했을 때는 실행이 안됨, 더블클릭시에 실행됨)
			icon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//트레이를 열었을때 트레이 아이콘 삭제하고 메인창 restart()
					tray.remove(icon);
					restart();
				}
			});
			tray = SystemTray.getSystemTray();
			tray.add(icon);
		}
	}


	/**
	 * Open, Close 메뉴를 눌렀을 때 발생하는 이벤트 설정
	 * Open이면 트레이 아이콘을 삭제하고 메인창 restart()
	 * Close면 메시지창을 뜨게 함 
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == open){
			restart();	
			tray.remove(icon);						
		} else if(ae.getSource() == close){
			showMessage("종료", "종료하시겠습니까?");		
		}
	}

	/**
	 * 자동시작 아이템이 체크 됐을 때 기능 설정
	 * 체크가 되어있다면 Waiting 실행, 체크가 풀리면 Waiting 종료
	 */
	public void itemStateChanged(ItemEvent e){
		if(e.getStateChange() == ItemEvent.SELECTED){
			Thread Waiting = new Thread(new Waiting());
			Waiting.start();
		}	else if(e.getStateChange() == ItemEvent.DESELECTED){			
			Waiting.finish();
		}
	}

	/**
	 * exit 버튼을 눌렀을 때 나타나는 창 설정
	 * @param title : 창의 제목 설정
	 * @param message : 나타날 메세지 유형 설정
	 * 
	 * 사용자가 "예"를 눌렀을 경우 Oculus 종료
	 * "아니오"나 선택없이 창을 닫을 경우 그대로
	 */
	private void showMessage(String title, String message){
		int result = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);

		if(result == JOptionPane.CLOSED_OPTION) {				
		}

		else if(result == JOptionPane.YES_OPTION) {
			System.out.println("Oculus 종료");
			System.exit(0);
		}

		else {
		}
	}

	/**
	 * 메인창을 다시 시작하게 하는 메소드
	 */
	public void restart(){
		MainFrame mFrame = new MainFrame();

		Calendar now = Calendar.getInstance();
		Save.SaveDay();

		Thread dateJLabel = new Thread(new DateJLabel(now, mFrame.getDate()));
		dateJLabel.start();

		mFrame.runRecord();
		mFrame.runGraph();
		mFrame.runGoal();
		mFrame.runExercise1();
		mFrame.runExercise2();
		mFrame.runExercise3();
		mFrame.runReference();
	}
}
