package jp.ac.osaka.u.ist.t_ishizu.Viewer;

import java.io.ByteArrayOutputStream;

import javax.swing.JTextArea;

public class RedirectConsole {
	private static void redirectConsole(JTextArea textarea){
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(){
			@Override
			public synchronized void flush() throws IOException{
				textarea.setText(toString());
			}
		}
	}
}
