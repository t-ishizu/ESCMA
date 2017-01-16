package jp.ac.osaka.u.ist.t_ishizu.Viewer;

public class MyPoint {
	private int x,y,height,width;

	public MyPoint(int a,int b,int h,int w){
		x=a;
		y=b;
		height=h;
		width=w;
	}

	public int getX(){return this.x;}
	public int getY(){return this.y;}
	public int getHeight(){return this.height;}
	public int getWidth(){return this.width;}
}
