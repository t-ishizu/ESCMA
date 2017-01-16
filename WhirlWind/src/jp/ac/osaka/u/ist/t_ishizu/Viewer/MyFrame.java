package jp.ac.osaka.u.ist.t_ishizu.Viewer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class MyFrame extends JFrame implements ActionListener{

	private Container contentPane;

	public File seedFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyFrame frame = new MyFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MyFrame() {
		setTitle("ESCMA Viewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = getContentPane();
		contentPane.add(createInitialPanel());
	}

	public JPanel createInitialPanel(){
		JPanel panel = new JPanel();
		panel.add(createButton("既に検出されているコードクローンの集約量の推定","1"));
		return panel;
	}

	public JButton createButton(String title,String command){
		JButton button = new JButton(title);
		button.setActionCommand(command);
		button.addActionListener(this);
		return button;
	}

	public JButton createButton(String title,String command,MyPoint point){
		JButton button = new JButton(title);
		button.setActionCommand(command);
		button.setBounds(point.getX(),point.getY(),point.getWidth(),point.getHeight());
		button.addActionListener(this);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int command = Integer.parseInt(e.getActionCommand());
		getContentPane().removeAll();
		JPanel panel = new JPanel();
		switch(command){
		case 0:
			panel=createInitialPanel();
			break;
		case 1:
			panel=createSeedFileSelectionPanel();
			break;
		case 2:
			break;
		default:
			panel=createInitialPanel();
			break;
		}
		contentPane.add(panel);
		setVisible(true);
	}

	public JPanel createSeedFileSelectionPanel(){
		JPanel panel = new JPanel();
		JTextField textbox = new JTextField();
		textbox.setBorder(new TitledBorder("Clone File Path:"));
		textbox.setPreferredSize(new Dimension(400, 40));
		panel.add(textbox);
		JButton nextButton = createButton("次へ","2");
		nextButton.setEnabled(false);
		panel.add(new JButton(new DirectryDialog(textbox,nextButton)));
		panel.add(nextButton);
		panel.add(createButton("戻る","0"));
		return panel;
	}

	private class DirectryDialog extends AbstractAction{
		private JTextField textbox;
		private JButton next;
		private DirectryDialog(JTextField textbox,JButton next){
			super("フォルダ選択");
			this.textbox = textbox;
			this.next = next;
		}

		@Override
		public void actionPerformed(ActionEvent e){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int answer = chooser.showDialog(null,"選択");
			if(answer == JFileChooser.APPROVE_OPTION){
				 seedFile = chooser.getSelectedFile();
			     textbox.setText(seedFile.getPath());
			     next.setEnabled(true);
			}
		}
	}
}
