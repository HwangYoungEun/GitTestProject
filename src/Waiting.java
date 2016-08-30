import java.util.Calendar;

/**
 * MainFrame 비활성화 시 1분마다 시간을 체크해서 일정시간(50분)이 되면 자동으로 운동 띄움.
 * 
 * @author Sol
 * @see Brightness, Dots, FollowMe
 * @param Canlendar cal - 현재 시간 갱신을 위한 변수
 * @param boolean flag - 종료 플래그
 */
public class Waiting implements Runnable{
	private Calendar cal;
	static boolean flag;

	Waiting(){
		cal = Calendar.getInstance();
		flag = false;
	}

	public static void finish(){
		flag = true;
	}

	public void run(){
		System.out.println("Waiting 실행 중");

		while(true){
			cal = Calendar.getInstance();		//현재 시간 갱신

			if(cal.get(Calendar.MINUTE) == 50 ){
				switch(cal.get(Calendar.HOUR_OF_DAY)){					

				case 0:
					Thread followMe1 = new Thread(new FollowMe());
					followMe1.start();
					break;

				case 1:
					Thread dots1 = new Thread(new Dots());
					dots1.start();
					break;

				case 2:
					Thread brightness1 = new Thread(new Brightness());
					brightness1.start();
					break;

				case 8:
					Thread followMe2 = new Thread(new FollowMe());
					followMe2.start();
					break;

				case 11:
					Thread dots2 = new Thread(new Dots());
					dots2.start();
					break;

				case 13:
					Thread brightness2 = new Thread(new Brightness());
					brightness2.start();
					break;

				case 15:
					Thread followMe3 = new Thread(new FollowMe());
					followMe3.start();
					break;

				case 17:
					Thread dots3 = new Thread(new Dots());
					dots3.start();
					break;

				case 19:
					Thread brightness3 = new Thread(new Brightness());
					brightness3.start();
					break;

				case 21:
					Thread followMe4 = new Thread(new FollowMe());
					followMe4.start();
					break;

				case 22:
					Thread dots4 = new Thread(new Dots());
					dots4.start();
					break;

				case 23:
					Thread brightness4 = new Thread(new Brightness());
					brightness4.start();
					break;
				}
			}

			try{
				Thread.sleep(60000);
				if(flag == true){
					System.out.println("Waiting 종료");
					return;
				}
			}catch(InterruptedException e){
				System.err.println(e);
				return;
			}
		}
	}
}
