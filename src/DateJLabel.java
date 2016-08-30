import java.util.*;
import java.awt.*;
import java.io.*;

import javax.swing.*;

/**
 * 현재 날짜와 시각을 표시
 * 
 * @author YoungEun
 * @param private JLabel date - 시간을 나타낼 Label
 * @param private Calendar cal - 달력 받아올 변수
 * @param String dayOfWeek - 요일을 나타내는 변수
 */

public class DateJLabel implements Runnable{
	private JLabel date;													
	private Calendar cal;										
	String dayOfWeek;											

	public static FileReader fr;								// 파일 입력 스트림 생성	
	public static BufferedReader br;							// 버퍼 파일 입력 스트림 생성, 입력 효율 향상
	public static FileWriter fw;								// 파일 출력 스트림 생성
	public static BufferedWriter bw;							// 버퍼 파일 출력 스트림 생성, 출력 효율 향상
	public static boolean flag = true;

	/**
	 *  창을 닫았을 시에 쓰레드 종료 
	 */
	public static void stop(){							
		flag = false;
	}

	/**
	 * 폰트 설정
	 * 
	 * @param now - 달력 받아올 변수
	 * @param date - 시간을 나타낼 Label
	 */
	DateJLabel(Calendar now, JLabel date){					
		this.cal = now;
		this.date = date;
		// 폰트 설정
		date.setFont(new Font("Arial", Font.BOLD, 20));			
		this.dayOfWeek = null;
		flag = true;
	}

	/**
	 * 	시간과 요일 설정
	 * 
	 *  시간, 분, 초를 십의 단위와 일의 단위로 나눠서 저장
	 *  요일은 스위치문 사용하여 설정
	 *  1분 지날때마다 시간 갱신
	 *  1시간이 지나면 시간 올리기
	 *  하루가 지나면 다시 요일명 설정하고 카운트 초기화
	 */
	public void run(){

		// 시간, 분, 초를 십의 단위와 일의 단위로 나눠서 저장
		int h = cal.get(Calendar.HOUR_OF_DAY), h10 = h/10, h1 = h%10;		
		int m = cal.get(Calendar.MINUTE), m10 = m/10, m1 = m%10;
		int s = cal.get(Calendar.SECOND), s10 = s/10, s1 = s%10;

		// 요일은 숫자로 표시되기때문에 요일명으로 설정하기 위해 스위치문 사용
		switch(cal.get(Calendar.DAY_OF_WEEK)){					
		case Calendar.SUNDAY : 
			dayOfWeek = "Sun."; 
			break;
		case Calendar.MONDAY : 
			dayOfWeek = "Mon."; 
			break;
		case Calendar.TUESDAY : 
			dayOfWeek = "Tues."; 
			break;
		case Calendar.WEDNESDAY : 
			dayOfWeek = "Wed."; 
			break;
		case Calendar.THURSDAY : 
			dayOfWeek = "Thur."; 
			break;
		case Calendar.FRIDAY: 
			dayOfWeek = "Fri."; 
			break;
		case Calendar.SATURDAY : 
			dayOfWeek = "Sat."; 
			break;
		}

		do{
			date.setText("" + cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." 
					+ cal.get(Calendar.DAY_OF_MONTH) + " (" + dayOfWeek + ") "
					+ h10 + h1 + ":" + m10 + m1 + ":" + s10 + s1);
			s++;							

			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				System.err.println(e);
				return;
			}

			// 1분 지날때마다 시간 갱신
			if(s == 60){										
				cal = Calendar.getInstance();
				h = cal.get(Calendar.HOUR_OF_DAY); h10 = h/10; h1 = h%10;
				m = cal.get(Calendar.MINUTE); m10 = m/10; m1 = m%10;
				s = cal.get(Calendar.SECOND); s10 = s/10; s1 = s%10;
			}
			// 1시간이 지나면 시간 올리기
			if(m == 60){											
				h++;
				m = 0;
			}

			h10 = h/10; h1 = h%10;
			m10 = m/10; m1 = m%10;
			s10 = s/10; s1 = s%10;

			// 하루가 지나면 다시 요일명 설정하고 카운트 초기화
			if(h == 24){								
				switch(cal.get(Calendar.DAY_OF_WEEK)) {			
				case Calendar.SUNDAY : dayOfWeek = "Sun."; break;
				case Calendar.MONDAY : dayOfWeek = "Mon."; break;
				case Calendar.TUESDAY : dayOfWeek = "Tues."; break;
				case Calendar.WEDNESDAY : dayOfWeek = "Wed."; break;
				case Calendar.THURSDAY : dayOfWeek = "Thur."; break;
				case Calendar.FRIDAY: dayOfWeek = "Fri."; break;
				case Calendar.SATURDAY : dayOfWeek = "Sat."; break;
				}

				// 카운트 초기화
				MainFrame.FMC = 0;								
				MainFrame.FDC = 0;
			}
		}while(flag);	
	}
}
