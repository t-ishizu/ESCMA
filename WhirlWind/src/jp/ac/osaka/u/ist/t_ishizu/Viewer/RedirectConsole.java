package jp.ac.osaka.u.ist.t_ishizu.Viewer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class RedirectConsole extends OutputStream{
	private JTextArea area;
	private ByteArrayOutputStream buf;
	public RedirectConsole(JTextArea textarea){
		area = textarea;
		buf = new ByteArrayOutputStream();
	}

	@Override
	public void write(int b) throws IOException{
		buf.write(b);
	}

	@Override
	public void flush() throws IOException{
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				area.append(buf.toString());
				buf.reset();
			}
		});
	}

//	public static void redirectConsole(JTextArea textarea,JButton resetButton){
//		final ByteArrayOutputStream bytes = new ByteArrayOutputStream(){
//			@Override
//			public synchronized void flush() throws IOException{
//				textarea.setText(toString());
//			}
//		};
//
//		resetButton.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e){
//				synchronized(bytes){
//					bytes.reset();
//				}
//			}
//		});
//
//		PrintStream out = new PrintStream(bytes, true);
//
//		System.setErr(out);
//		System.setOut(out);
//
//	}
}
