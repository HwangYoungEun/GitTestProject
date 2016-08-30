import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * 오늘의 목표달성치
 * 
 * @author YoungEun
 * percent1 = FollowMe 달성률, percent2 = Fifteen Dots 달성률
 */
class Graph extends JFrame{
	Container c;
	double percent1 = 100 * (double)MainFrame.FMC/MainFrame.FM;				// Follow Me 퍼센트 
	double percent2 = 100 * (double)MainFrame.FDC/MainFrame.FD;				// Fifteen Dots 퍼센트

	/**
	 * 그래프의 위치와 크기 설정
	 */
	Graph(){	 															// percent1 = FollowMe 달성률, percent2 = Fifteen Dots 달성률
		setTitle("오늘 목표달성률(%)");											// 창 이름 설정
		c = getContentPane();												// 프레임에 연결된 컨텐트팬을 알아냄
		c.setLayout(null);													// 배치관리자 삭제

		JLabel FM = new JLabel("Follow Me");								// Follow Me를 나타내는 레이블 객체 생성
		JLabel CD = new JLabel("Fifteen Dots");								// Fifteen Dots를 나타내는 레이블 객체 생성

		CircleGraph FollowMe = new CircleGraph(250, 250, percent1, 1);		// Follow Me 그래프 객체 생성
		CircleGraph Card = new CircleGraph(250, 250, percent2, 2);			// Fifteen Dots 그래프 객체 생성

		FollowMe.setLocation(40,50);										// Follow Me 그래프 위치 설정
		FollowMe.setSize(260,260);											// Follow Me 그래프 크기 설정
		Card.setLocation(340,50);											// Fifteen Dots 그래프 위치 설정
		Card.setSize(260,260);												// Fifteen Dots 그래프 크기 설정
		FM.setLocation(135,310);											// Follow Me 레이블 위치 설정
		FM.setSize(70,40);													// Follow Me 레이블 크기 설정
		CD.setLocation(435,310);											// Fifteen Dots 레이블 위치 설정
		CD.setSize(70,40);													// Fifteen Dots 레이블 크기 설정

		c.add(FollowMe);													// Follow Me 그래프 컨텐트팬에 부착
		c.add(Card);														// Fifteen Dots 그래프 컨텐트팬에 부착
		c.add(FM);															// Follow Me 레이블 컨텐트팬에 부착
		c.add(CD);															// Fifteen Dots 레이블 컨텐트팬에 부착
		setSize(640,460);													// 창 크기 설정
		setLocationRelativeTo(null);										// 화면의 가운데에 창을 띄움
		setVisible(true);													// 창을 보이게 함
		setResizable(false);												// 창 크기를 못 늘리게 고정
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);					// 닫기 버튼을 눌렀을 때 해당 창만 종료
	}

	public class CircleGraph extends JPanel{
		int width, height;													// 원 가로 세로 크기
		double value;														// %값
		float r;															// 반지름 길이
		double angle;														// 각	
		Color lineColor;													// 테두리 컬러
		Color arcColor;														// 호 컬러

		Ellipse2D.Float outCircle;											// 외곽 테두리
		Ellipse2D.Float outFillCircle;										// 내부 원 영역

		Arc2D.Float inArc;													// 호 영역

		int stringX, stringY;												// 값 출력 위치

		/**
		 * 원 크기 디폴트 값 설정
		 * @param value - % 값
		 */
		public CircleGraph(double value){									// 원 크기 디폴트 값 설정
			this(100,100,value, 1);
		}
		
		/**
		 * 운동별 그래프 설정
		 * @param width - 원 가로
		 * @param height - 원 세로
		 * @param value - % 값
		 * @param ex - 운동종류 (1 = FollowMe, 2 = Fifteen Dots)
		 */
		public CircleGraph(int width, int height, double value, int ex){ 	
																			
			this.width = width;
			this.height = height;
			this.r = (width > height)? width/2f: height/2f;
			this.lineColor = Color.black;									// 테두리 색 설정
																			// ex = 운동종류 (1 = FollowMe, 2 = Fifteen Dots)
			if(ex==1){														// Follow Me인 경우
				this.arcColor = new Color(255, 0, 0, 50); 					// 그래프 색 설정, 알파 = 색 명암 조절
			}
			else{															// Fifteen Dots인 경우
				this.arcColor = new Color(0, 0, 255, 50); 					// 그래프 색 설정, 알파 = 색 명암 조절
			}

			this.outCircle = new Ellipse2D.Float(1,1,2*r,2*r);				// Ellipse2D(x, y, 직경, 직경), 그래프 테두리 객체 생성
			this.outFillCircle = new Ellipse2D.Float((int)outCircle.getX()+1,
					(int)outCircle.getY()+1,(int)outCircle.getWidth()-1,
					// 그래프 채우기 객체 생성, outcircle보다 직경이 작아야지 외곽선이 보임
					(int)outCircle.getHeight()-1);	
			// 화면에 보이게 함
			setValue(value);												
		}

		/**
		 * 퍼센트 값 설정(0이하여도 0으로 되게 설정, 100넘어도 100으로 되게 설정)
		 * @param value - % 값
		 */
		public void setValue(double value){
			// 퍼센트 값 설정 (100넘어도 100으로 되게 설정)
			if(value > 100) value=100;										
			// 퍼센트 값 설정 (0이하여도 0으로 되게 설정)
			if(value < 0) value = 0;										

			this.value = value;

			this.angle = 3.6 * value;										// 1%당 3.6도 <-(360/100)
			inArc = new Arc2D.Float(2,2,2*r-1,2*r-1,0,(int)angle,2);		// Arc2D(x, y, 직경, 직경)
			// 컴포넌트 다시 그리기
			repaint();													
		}

		public double getValue(){
			return this.value;
		}

		/**
		 * 컴포넌트 그리기
		 */
		public void paintComponent(Graphics g){	
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D)g;
			
			// 안티앨리어싱 활성화 (계단 현상 제거하기. 즉 원이 사각형모양으로 삐뚤삐뚤 삐져나오는거 제거)
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
			g2.setStroke(new BasicStroke(1));								// 선두께 1로 설정
			g2.draw(outCircle);												// 외각원 그리기
			g.setColor(arcColor);											// 그래프 색 적용		
			g2.fill(inArc);													// 호 채우기

			FontMetrics fm = g.getFontMetrics();
			int offX = fm.stringWidth(value+"%") /3;
			
			// 삼각함수를 통하여 해당되는 각도의 x좌표 구함
			stringX = (int)(Math.cos(Math.toRadians(angle/2d))*(r/2)) + (int)r - offX;		
			// 삼각함수를 통하여 해당되는 각도의 y좌표 구함
			stringY = (int)(Math.sin(Math.toRadians(angle/2d))*(r/2)) * -1 + (int)r + 5;			
			// 외각원 그리기
			g.setColor(Color.black);										
			// 퍼센트값(문자열) 출력
			g2.drawString(String.format("%.2f", value)+"%",stringX,stringY);
		}
		
		/**
		 * 컴포넌트의 폭과 크기 정하기
		 */
		public Dimension getPreferredSize(){
			return new Dimension(width+4,height+4);
		}
		
		// 컴포넌트 크기 일정
		public Dimension getMinimumSize(){								
			return getPreferredSize();										
		}
		
		// 컴포넌트 크기 일정
		public Dimension getMaximumSize(){							
			return getPreferredSize();										
		}
	}
}
