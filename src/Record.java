import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 월간 기록 확인 캘린더
 * 
 * @author YoungEun
 */

public class Record extends JFrame implements ActionListener
{
	String [] days = {"일","월","화","수","목","금","토"};		// 요일에 해당하는 문자열 배열 선언 		
	int year,month,day,todays,memoDay=0;	
	Font f;
	Color bc,fc;
	Calendar today;
	Calendar cal;
	JButton btnBefore,btnAfter;
	JButton[] calBtn = new JButton[49];				// 일 수를 49일로 설정 							
	JLabel thing;
	JLabel time;
	JLabel Explain;
	JPanel panWest; 								// 달력의 일을 나타내는 부분								
	JPanel panSouth;								// 달력의 아래쪽 영역				
	JPanel panNorth; 								// 달력의 위쪽 영역		
	JTextField txtMonth,txtYear;
	JTextField txtTime;

	BorderLayout bLayout= new BorderLayout();    	// BorderLayout 객체 생성			

	int target; 									// 목표치											

	FileReader fr = null;							// 파일 오픈할 때 예외처리를 해야하므로 run에서 파일 입력 스트림 생성				
	BufferedReader br = null;						// 파일 오픈할 때 예외처리를 해야하므로 run에서 버퍼 파일 입력 스트림 생성, 입력 효율 향상		

	String line;
	String[] str;
	int fileYear;
	int date;
	int setMonth;
	int c;
	public static double Date[][] = new double [12][31];    	// 일	
	public static int Target[][] = new int [12][31];			// 등급

	/**
	 * 기록 초기화(설명추가)
	 */
	public Record(){											
		for(int i =0; i<12; i++){
			for(int j =0; j<31; j++){
				Date[i][j] = 0;
			}
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		// 닫기 버튼을 눌렀을 때 해당 창만 종료
	}

	/**
	 * 파일입력
	 * 등급 나누기
	 * 쉼표로 분리하여 한줄씩 읽어옴
	 * @throws IOException
	 */
	public void run(){
		today = Calendar.getInstance(); 						// 현재 시스템의 시간 정보를 얻는 Calendar 클래스 객체를 생성,디폴트의 타임 존 및 로케일을 사용해 달력을 가져옴							
		cal = new GregorianCalendar();							// GregorianCalendar 객체 생성		
		year = today.get(Calendar.YEAR);						// 년도 정보		
		month = today.get(Calendar.MONTH)+1;					// 월 정보, 1월의 값이 0이므로 +1

		try{
			fr = new FileReader("log\\Info.txt");				// 기록이 저장된 파일 가져오기, 파일 입력 스트림 생성				
			br = new BufferedReader(fr);						// 버퍼 파일 입력 스트림 생성, 입력 효율 향상

			while((line = br.readLine()) != null){  			// 한 줄씩 읽어옴			
				str = line.split(",");   						// 쉼표로 분리	

				fileYear = Integer.parseInt(str[0]); 
				setMonth = Integer.parseInt(str[1]); 			// 월을 int형으로 변환하여 저장
				date = Integer.parseInt(str[2]);   				// 일을 int형으로 변환하여 저장		 
				Date[setMonth-1][date-1] = Double.parseDouble(str[3]); // 배열은 0부터 시작, 퍼센트를 double형으로 변환하여 저장

				//읽어들인 연도가 현재 연도와 같다면 등급나누기 실행
				if(fileYear == cal.get(Calendar.YEAR)){
					for(int i =0; i<12; i++){
						for(int j =0; j<31; j++){

							if(Date[i][j]<26 && Date[i][j]>0)					
								Target[i][j] = 1;						
							else if(Date[i][j]<51 && Date[i][j]>=26)	
								Target[i][j] = 2;						
							else if(Date[i][j]<76 && Date[i][j]>=51)	
								Target[i][j] = 3;						
							else if(Date[i][j]>=76)						
								Target[i][j] = 4;							
							else if(Date[i][j]==0)							
								Target[i][j] = 0;						
						}
					}
				}
			}
		}
		catch (java.io.FileNotFoundException e) { 	//Info.txt가 없을 경우
			try {
				FileWriter fw = null;
				BufferedWriter bw = null;
				File dsFile = new File("log\\Info.txt");		
				if(!dsFile.exists()){			//log폴더가 없을 경우				
					File dsDir = new File("log"); 				
					dsDir.mkdirs();
					dsDir = null;
				}

				dsFile = null;				
				fw = new FileWriter("log\\Info.txt"); 
				bw = new BufferedWriter(fw);

				bw.write(String.format("%d,%d,%d,0.00", today.get(Calendar.YEAR),
						today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH)));
				bw.flush();
				if(bw != null)
					bw.close();
				if(fw != null)
					fw.close();
			}
			catch (IOException te) {				
				te.printStackTrace();
				System.exit(1);
			}
			return;
		}		
		catch (IOException e) {
			System.out.println("파일입력실패");
			System.err.println(e);
			System.exit(1);
		}




		panNorth = new JPanel();								// JPanel 객체를 생성하여 panNorth에 저장	
		panNorth.add(btnBefore = new JButton("Before"));		// "Before" 버튼을 panNorth에 삽입		  
		panNorth.add(txtYear = new JTextField(year+"년"));		// 년도를 나타내는 텍스트필드를 panNorth에 삽입		
		panNorth.add(txtMonth = new JTextField( month+"월",3));	// 월을 나타내는 텍스트필드를 panNorth에 삽입	
		txtYear.setEnabled(false); 								// 바꿀수 없게 고정	
		txtMonth.setEnabled(false);								// 바꿀수 없게 고정	
		panNorth.add(btnAfter = new JButton("After"));			// "After" 버튼을 panNorth에 삽입		
		f=new Font("Sherif",Font.BOLD,18); 						// 폰트 설정		
		txtYear.setFont(f);										// txtYear 텍스트필드에 폰트 적용	
		txtMonth.setFont(f);       								// txtMonth 텍스트필드에 폰트 적용		
		add(panNorth,"North"); 									// panNorth을 위에 삽입	

		panSouth = new JPanel();								// JPanel 객체를 생성하여 panSouth에 저장	
		panSouth.add(Explain = new JLabel("빨강 : 1 ~ 25%, 주황 : 26 ~ 50%, 파랑 : 51 ~ 75%, 초록 : 76 ~ 100%")); 	 // 문자열 레이블 컴포넌트 생성
		f=new Font("Sherif",Font.BOLD,12); 						// 폰트 설정	
		Explain.setFont(f);										// Explain 레이블에 폰트 적용	
		add(panSouth,"South");									// panSouth를 아래에 삽입		

		// 달력에 날에 해당하는 부분
		panWest = new JPanel(new GridLayout(7,7));				// 7X7 분할로 컴포넌트 배치	
		f=new Font("Sherif",Font.BOLD,12);						// 폰트 설정	
		gridInit();
		calSet();
		hideInit();
		add(panWest,"Center"); 									// panWest를 가운데에 삽입

		btnBefore.addActionListener(this);
		btnAfter.addActionListener(this);       
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Calendar");									// 창이름은 "Calendar"
		setBounds(300,300,445,350);								// (300,300) 가로 445, 세로 350 크기로 위치 시킴	
		setLocationRelativeTo(null);							// 닫기 버튼을 눌렀을 때 해당 창만 종료
		setVisible(true);										// 창을 보이게 함
	}

	/**
	 * 달력 설정
	 */
	public void calSet(){
		cal.set(Calendar.YEAR,year);							// 년도 지정
		cal.set(Calendar.MONTH,(month-1));						// 월 지정
		cal.set(Calendar.DATE,1);								// 일 지정
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);			// 요일 가져옴
		int j=0;
		int hopping=0;

		// 일요일, 토요일 버튼 숫자 색깔 설정
		calBtn[0].setForeground(new Color(255,0,0));			// 일요일은 빨간색	
		calBtn[6].setForeground(new Color(0,0,255));			// 토요일은 파란색	
		for(int i=cal.getFirstDayOfWeek(); i<dayOfWeek; i++){  j++;  } 	// 첫 주인 일요일부터 그 달의 시작 요일까지 j++

		// 일요일부터 그달의 첫시작 요일까지 빈칸으로 셋팅하기 위해 
		hopping=j;
		for(int kk=0; kk<hopping; kk++){
			calBtn[kk+7].setText(""); 								
		}

		// 월의 최저값과 최고값을 이용하여 for문 돌리기
		for(int i=cal.getMinimum(Calendar.DAY_OF_MONTH); i<=cal.getMaximum(Calendar.DAY_OF_MONTH); i++){    
			cal.set(Calendar.DATE,i);							// i의 값을 일로 설정							
			// 달이 일치하지않으면 break
			if(cal.get(Calendar.MONTH) !=month-1){ 								
				break;
			}

			todays=i;

			if(memoDay==1){
				calBtn[i+6+hopping].setForeground(new Color(0,255,0));  	// 초록색               
			}

			else{
				calBtn[i+6+hopping].setForeground(new Color(0,0,0));		// 색깔 x
			}
			//
			// 요일을 찍은 다음부터 계산해야 하니 요일을 찍은 버튼의 갯수를 더하고
			// 인덱스가 0부터 시작이니 -1을 해준 값으로 연산을 해주고
			// 버튼의 색깔을 변경해준다.
			//
			calBtn[i+6+hopping].setText((i)+""); 							// ""을 붙이는 이유는 i를 문자로 바꾸기 위해서			

			for(j=0; j<12; j++){
				if(month == j+1){

					// 등급에 따라 색깔 나누기
					switch(Target[j][i-1]){
					case 1 :
						calBtn[i+6+hopping].setBackground(Color.red);		// 목표치 1일 때는 빨간색
						break;

					case 2 :
						calBtn[i+6+hopping].setBackground(Color.orange);	// 목표치 2일 때는 오렌지색
						break;

					case 3 :
						calBtn[i+6+hopping].setBackground(Color.blue);		// 목표치 3일 때는 파란색
						break;

					case 4 :
						calBtn[i+6+hopping].setBackground(Color.green);		// 목표치 4일 때는 초록색
						break;

					default :
						break;
					}
				}
			}
		}//for
	}//end Calset()

	public void actionPerformed(ActionEvent ae){         
		if(ae.getSource() == btnBefore){
			this.panWest.removeAll();
			calInput(-1);
			gridInit();
			panelInit();               
			calSet();
			hideInit();
			this.txtYear.setText(year+"년");
			this.txtMonth.setText(month+"월");                   
		}                   
		else if(ae.getSource() == btnAfter){
			this.panWest.removeAll();
			calInput(1);
			gridInit();
			panelInit();
			calSet();
			hideInit();
			this.txtYear.setText(year+"년");
			this.txtMonth.setText(month+"월");                                       
		}

		// 날짜를 눌렀을 때 버튼의 벨류 즉 1,2,3.... 문자를 정수형으로 변화하여 클릭한 날짜를 바꿔준다.
		else if(Integer.parseInt(ae.getActionCommand()) >= 1 && Integer.parseInt(ae.getActionCommand()) <=31){			
			day = Integer.parseInt(ae.getActionCommand());

			System.out.println(+year+"-"+month+"-"+day);			
			calSet();
		}      
	}//end actionperformed()

	/**
	 * 일이 찍히지 않은 버튼 비활성화
	 */
	public void hideInit(){
		for(int i = 0 ; i < calBtn.length;i++){
			if((calBtn[i].getText()).equals(""))
				calBtn[i].setEnabled(false); 
		}//end for
	}//end hideInit()

	public void gridInit(){

		// jPanel3에 버튼 붙이기
		for(int i = 0 ; i < days.length;i++) 
		{
			panWest.add(calBtn[i] = new JButton(days[i]));
			calBtn[i].setContentAreaFilled(false); 				// 버튼의 배경색을 칠할지 말지 결정, 월,화,수,목,금에는 배경색 지정 x
			calBtn[i].setBorderPainted(false);
		}	
		for(int i = days.length ; i < 49;i++){                
			panWest.add(calBtn[i] = new JButton(""));                   
			calBtn[i].addActionListener(this);
		}              
	}//end gridInit()

	/**
	 * 팬 레이아웃 설정
	 */
	public void panelInit(){									// 팬 레이아웃 설정
		GridLayout gridLayout1 = new GridLayout(7,7);			// 7x7 GridLayout 배치관리
		panWest.setLayout(gridLayout1);   						// panWest 컴포넌트 배치
	}//end panelInit()

	/**
	 * 월, 년도 옮기기
	 */
	public void calInput(int gap){	
		month+=(gap);											// month = month + gap	
		if (month<=0){
			month = 12;
			//year  =year- 1; 									// 년도를 옮긴는 것이지만 배열이 커지므로 삭제		
		}else if (month>=13){
			month = 1;
			//year =year+ 1;
		}		
	}//end calInput()	
}//end class
