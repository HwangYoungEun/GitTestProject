import java.io.*;

/**
 *  홍채 운동 : 명암 트레이닝 (설명추가)
 *  @author Sol
 */
public class Brightness implements Runnable{
	HowToUse htu3;

	/**
	 * 모니터의 밝기를 조절하는 메소드
	 * @param brightness : 0~100 사이의 명도 조절
	 * @throws IOException
	 */
	public static void setBrightness(int brightness)  throws IOException 
	{
		//Creates a powerShell command that will set the brightness to the requested value (0-100), after the requested delay (in milliseconds) has passed. 
		String s = String.format("$brightness = %d;", brightness)
				+ "$delay = 0;"
				+ "$myMonitor = Get-WmiObject -Namespace root\\wmi -Class WmiMonitorBrightnessMethods;"
				+ "$myMonitor.wmisetbrightness($delay, $brightness)";
		String command = "powershell.exe  " + s;

		// Executing the command
		Process powerShellProcess = Runtime.getRuntime().exec(command);
		powerShellProcess.getOutputStream().close();

		//Report any error messages
		String line;

		BufferedReader stderr = new BufferedReader(new InputStreamReader(
				powerShellProcess.getErrorStream()));
		line = stderr.readLine();

		if (line != null)
		{
			System.err.println("Standard Error:");

			do
			{
				System.err.println(line);
			} while ((line = stderr.readLine()) != null);

		}
		stderr.close();
	}

	/**
	 * 5초 정도 최저 조명을 유지하고 3.5초정도 최대 조명을 유지한다.
	 * 이 운동은 열번 동안 최저조명과 최대 조명을 번갈아 실행한다.
	 * 운동이 끝나면 80정도의 조명으로 되돌려주고 설명서를 닫으며 종료한다.
	 */
	public void run(){
		/*운동에 대한 설명서*/
		htu3 = new HowToUse(3);
		int k = -1;

		for(k = 0; k < 10; k++){ 
			try{
				try{
					setBrightness(0);				
					Thread.sleep(5000);				
					setBrightness(100);				
					Thread.sleep(3500);				
				}catch(IOException e){
					System.err.println(e);
					return;
				}
			}catch(InterruptedException e){
				System.err.println(e);
				return;
			}
		}
		try{
			setBrightness(80);   
			htu3.finish();		
			return;
		}catch(IOException e){
			System.err.println(e);
			return;
		}
	}
}