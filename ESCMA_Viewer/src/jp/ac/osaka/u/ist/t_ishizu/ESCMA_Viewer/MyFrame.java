package jp.ac.osaka.u.ist.t_ishizu.ESCMA_Viewer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans.CCM_OutputTrans;
import jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.ESCMA_COBOL;

import org.apache.commons.lang3.StringUtils;

public class MyFrame extends JFrame implements ActionListener{
	private Container contentPane;
	public File file;
	public ArrayList<File> cobolFileList;
	public JTextArea mainArea;
	public String CCFXPath;
	public String OutputFilePath;
	public SpinnerNumberModel model_Size =  new SpinnerNumberModel(10,10,200,10);
	public SpinnerNumberModel model_RNR = new SpinnerNumberModel(0.0,0.0,1.0,0.1);
	public int RNR = 0;
	public int tokenLength = 50;
	public DefaultListModel<String> listModel1;
	public DefaultListModel<String> listModel2;
	public String html1;
	public String html2;
	public ArrayList<String> fileIdList;
	public ArrayList<String> CCMfileIdList;
	public HashMap<Integer,ArrayList<jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans.CodeClone>> CCMfileToCloneMap;
	public ArrayList<jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.CloneSet> cloneSetList;
	public ArrayList<jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans.CloneSet> CCMcloneSetList;
	public HashMap<Integer,ArrayList<jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.CodeClone>>fileToCloneMap;
	public HashMap<Integer,CodeSnipets> snipetsMap;
	public File clonePairFile;
	public File FunctionsA;
	String[] colorPattern = new String[]{"#67D5B5","#EE7785","#C89EC4","#84B1ED"};
	public MyFrame(){
		setTitle("ESCMA Viewer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setBounds(100, 100, 1000, 850);
	    contentPane = getContentPane();
	    contentPane.add(getDefPanel());
	    setVisible(true);

	}

	public JButton createButton(String title,String command){
		JButton button = new JButton(title);
		button.setActionCommand(command);
		button.addActionListener(this);
		return button;
	}

	public JPanel getDefPanel(){
		JPanel panel = new JPanel();
		panel.add(createButton("新たなコードクローンの集約","1"));
		panel.add(createButton("ESCMA集約結果の表示","2"));
		panel.add(createButton("CCM集約結果の表示","8"));
	    return panel;

	}


	public JPanel getSelectDirectryPanel(){
		JPanel panel = new JPanel();
		JTextField textbox = new JTextField();
		textbox.setBorder(new TitledBorder("フォルダパス"));
		textbox.setPreferredSize(new Dimension(400, 40));
		panel.add(textbox);
		JButton nextButton = createButton("次へ","3");
		nextButton.setEnabled(false);
		panel.add(new JButton(new DirectryDialog(textbox,nextButton)));
		panel.add(nextButton);
		panel.add(createButton("戻る","0"));
		return panel;
	}

	public JPanel getSelectFilePanel(){
		JPanel panel = new JPanel();
		JTextField textbox = new JTextField();
		textbox.setBorder(new TitledBorder("ファイルパス"));
		textbox.setPreferredSize(new Dimension(400, 40));
		panel.add(textbox);
		JButton nextButton = createButton("次へ","4");
		nextButton.setEnabled(false);
		panel.add(new JButton(new FileDialog(textbox,nextButton)));
		panel.add(nextButton);
		panel.add(createButton("戻る","0"));
		return panel;
	}

	public JPanel getSelectTwoFilePanel(){
		JPanel panel = new JPanel();
		JTextField textbox = new JTextField();
		textbox.setBorder(new TitledBorder("clonepairファイルパス"));
		textbox.setPreferredSize(new Dimension(400, 40));
		panel.add(textbox);
		JButton nextButton = createButton("次へ","9");
		nextButton.setEnabled(false);
		panel.add(new JButton(new ClonePairFileDialog(textbox,nextButton)));
		panel.add(nextButton);
		panel.add(createButton("戻る","0"));
		return panel;
	}

	public JPanel getSelectThreeFilePanel(){
		JPanel panel = new JPanel();
		JTextField textbox = new JTextField();
		textbox.setBorder(new TitledBorder("Function-aファイルパス"));
		textbox.setPreferredSize(new Dimension(400, 40));
		panel.add(textbox);
		JButton nextButton = createButton("次へ","10");
		nextButton.setEnabled(false);
		panel.add(new JButton(new FunctionsAFileDialog(textbox,nextButton)));
		panel.add(nextButton);
		panel.add(createButton("戻る","0"));
		return panel;
	}

	public JPanel getFileListPanel(){
		//選択されたフォルダ下に特定の拡張子を持つファイルが1つ以上存在するのか確認する．
		cobolFileList = new ArrayList<File>();
		File[] files = file.listFiles();
		for(int i=0;i<files.length;i++){
			String suffix = getSuffix(files[i].getName());
			if(suffix.equals("cob")||suffix.equals("cbl")){
				cobolFileList.add(files[i]);
			}
		}

		JPanel panel = new JPanel();
		JTextArea textArea = new JTextArea(file.getPath());
		//textArea.setBorder(new TitledBorder("選択されたフォルダパス"));
		//textArea.setPreferredSize(new Dimension(400, 40));
		//textArea.setEditable(false);
		//panel.add(textArea);
		//panel.setLayout(new FlowLayout());
		JButton nextButton = createButton("次へ","5");
		/*
		if(cobolFileList.size()>0){
			JList<String> list = new JList<String>(toArr(cobolFileList));
			panel.add(list);
			panel.add(nextButton);
		}else{
			JTextArea noFileText = new JTextArea("選択されたフォルダにはファイルがありませんでした．");
			noFileText.setEditable(false);
			noFileText.setBorder(new TitledBorder("ファイルリスト"));
			noFileText.setPreferredSize(new Dimension(400, 40));
			panel.add(noFileText);
			nextButton.setEnabled(false);
			panel.add(nextButton);
		}
		*/
		panel.add(nextButton);
		panel.add(createButton("戻る","1"));
		return panel;
	}

	public JPanel getOptionPanel(){
		JPanel panel = new JPanel();
		//SpinnerNumberModel model_Size = new SpinnerNumberModel(10,10,200,10);
		JSpinner spinner_Size = new JSpinner(model_Size);
		spinner_Size.setPreferredSize(new Dimension(120,40));
		panel.add(spinner_Size);
		//SpinnerNumberModel model_RNR = new SpinnerNumberModel(0.0,0.0,1.0,0.1);
		JSpinner spinner_RNR = new JSpinner(model_RNR);
		spinner_RNR.setPreferredSize(new Dimension(100,40));
		panel.add(spinner_RNR);

		JTextField textbox = new JTextField();
		textbox.setBorder(new TitledBorder("CCFinderXのファイルパス"));
		textbox.setPreferredSize(new Dimension(400, 40));
		panel.add(textbox);
		JButton nextButton = createButton("検出開始","6");
		nextButton.setEnabled(false);
		panel.add(new JButton(new CCFinderDialog(textbox,nextButton)));
		panel.add(nextButton);
		panel.add(createButton("戻る","3"));
		return panel;
	}

	public JPanel getViewerPanel(){
		/*コードクローンの情報の初期化*/
		fileIdList = ESCMA_COBOL.createFileIdList(file.getPath());
		cloneSetList = ESCMA_COBOL.createCloneSetList(file.getPath());
		fileToCloneMap = ESCMA_COBOL.createFileToCloneMap(cloneSetList);

//		String[] nums1 = new String[cloneSetList.size()];
//		for(int i=0;i<cloneSetList.size();i++){
//			nums1[i]=String.valueOf(i+1);
//		}
		/*リストボックスの初期化*/
		listModel1 = new DefaultListModel<String>();
		for(int i=0;i<cloneSetList.size();i++){
			listModel1.addElement(String.valueOf(i+1));
		}
		JList<String> list1 = new JList<String>(listModel1);
		JScrollPane scrollPanel_list1 = new JScrollPane();
		JViewport view1 = scrollPanel_list1.getViewport();
		view1.setView(list1);

//		String[] nums2 = new String[cloneSetList.get(0).getCloneList().size()];
//		for(int i=0;i<cloneSetList.get(0).getCloneList().size();i++){
//			nums2[i]=String.valueOf(i+1);
//		}
		listModel2 = new DefaultListModel<String>();
		for(int i=0;i<cloneSetList.get(0).getCloneList().size();i++){
			jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.CodeClone c = cloneSetList.get(0).getCloneList().get(i);
			String str2 = (i+1)+" 0."+c.getFileId()+" "+c.getLS()+","+c.getCS()+","+c.getTS()+" "+c.getLE()+","+c.getCE()+","+c.getTE();
			listModel2.addElement(str2);
		}
		JList<String> list2 = new JList<String>(listModel2);
		JScrollPane scrollPanel_list2 = new JScrollPane();
		scrollPanel_list2.getViewport().setView(list2);


		JPanel mainPanel = new JPanel();

		html1 = "<html>"+"<body><h1>Kitty on your lap</h1>"+"</html>";
		JEditorPane viewPanel1 = new JEditorPane("text/html",html1);
		JScrollPane scrollPanel_viewer1 = new JScrollPane(viewPanel1);
		//JScrollBar vBar1 = scrollPanel_viewer1.getVerticalScrollBar();
		//JViewport viewport1 = scrollPanel_viewer1.getViewport();
		//viewport1.setView(viewPanel1);
		scrollPanel_viewer1.setPreferredSize(new Dimension(400,600));

		//JTextArea area1 = new JTextArea();
		//JScrollPane scrollPanel1 = new JScrollPane(area1);
		//viewPanel1.add(scrollPanel1);

		html2 = "<html>"+"<body><h1>Kitty on your lap</h1>"+"</html>";
		JEditorPane viewPanel2 = new JEditorPane("text/html",html2);
		JScrollPane scrollPanel_viewer2 = new JScrollPane();
		JViewport viewport2 = scrollPanel_viewer2.getViewport();
		viewport2.setView(viewPanel2);
		scrollPanel_viewer2.setPreferredSize(new Dimension(400,600));
		//JTextArea area2 = new JTextArea();
		//JScrollPane scrollPanel2 = new JScrollPane(area2);
		//viewPanel1.add(scrollPanel2);

		JPanel selectPanel = new JPanel();
		selectPanel.add(scrollPanel_list1);
		CloneSetSelected css = new CloneSetSelected(list1,list2,viewPanel1);
		selectPanel.add(new JButton(css));
		selectPanel.add(scrollPanel_list2);
		CodeCloneSelected ccs = new CodeCloneSelected(list2,viewPanel1,viewPanel2,scrollPanel_viewer1);
		css.setSelected(ccs);
		selectPanel.add(new JButton(ccs));
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.add(createButton("戻る","2"));


		mainPanel.add(scrollPanel_viewer1,BorderLayout.WEST);
		mainPanel.add(scrollPanel_viewer2,BorderLayout.CENTER);
		mainPanel.add(selectPanel,BorderLayout.EAST);
		mainPanel.add(ButtonPanel,BorderLayout.SOUTH);
		return mainPanel;
	}

	public JPanel getCCMViewerPanel(){
		/*コードクローンの情報の初期化*/
		CCMfileIdList = CCM_OutputTrans.createFileIdList(clonePairFile.getPath());
		CCMfileToCloneMap = CCM_OutputTrans.createFileToCloneMap(clonePairFile.getPath());
		CCMcloneSetList = CCM_OutputTrans.createCloneSetList(CCMfileToCloneMap);
		String[] prepHierarchy = CCM_OutputTrans.getPrepHierarchy(clonePairFile.getPath());
		CCM_OutputTrans.findCloneLocation(CCMfileIdList, prepHierarchy, CCMfileToCloneMap);
		snipetsMap = createSnipetsMap();
		/*リストボックスの初期化*/
		listModel1 = new DefaultListModel<String>();
		for(int i=0;i<CCMcloneSetList.size();i++){
			listModel1.addElement(String.valueOf(i+1));
		}
		JList<String> list1 = new JList<String>(listModel1);
		JScrollPane scrollPanel_list1 = new JScrollPane();
		JViewport view1 = scrollPanel_list1.getViewport();
		view1.setView(list1);

//		String[] nums2 = new String[cloneSetList.get(0).getCloneList().size()];
//		for(int i=0;i<cloneSetList.get(0).getCloneList().size();i++){
//			nums2[i]=String.valueOf(i+1);
//		}
		listModel2 = new DefaultListModel<String>();
		for(int i=0;i<CCMcloneSetList.get(0).getCloneList().size();i++){
			jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans.CodeClone c = CCMcloneSetList.get(0).getCloneList().get(i);
			String str2 = (i+1)+"  "+c.getCloneSetId()+"  0."+c.getFileId()+" "+c.getLS()+","+c.getCS()+","+c.getTS()+"  "+c.getLE()+","+c.getCE()+","+c.getTE();
			listModel2.addElement(str2);
		}
		JList<String> list2 = new JList<String>(listModel2);
		JScrollPane scrollPanel_list2 = new JScrollPane();
		scrollPanel_list2.getViewport().setView(list2);


		JPanel mainPanel = new JPanel();

		html1 = "<html>"+"<body><h1>Kitty on your lap</h1>"+"</html>";
		JEditorPane viewPanel1 = new JEditorPane("text/html",html1);
		JScrollPane scrollPanel_viewer1 = new JScrollPane(viewPanel1);
		//JScrollBar vBar1 = scrollPanel_viewer1.getVerticalScrollBar();
		//JViewport viewport1 = scrollPanel_viewer1.getViewport();
		//viewport1.setView(viewPanel1);
		scrollPanel_viewer1.setPreferredSize(new Dimension(400,600));

		//JTextArea area1 = new JTextArea();
		//JScrollPane scrollPanel1 = new JScrollPane(area1);
		//viewPanel1.add(scrollPanel1);

		html2 = "<html>"+"<body><h1>Kitty on your lap</h1>"+"</html>";
		JEditorPane viewPanel2 = new JEditorPane("text/html",html2);
		JScrollPane scrollPanel_viewer2 = new JScrollPane();
		JViewport viewport2 = scrollPanel_viewer2.getViewport();
		viewport2.setView(viewPanel2);
		scrollPanel_viewer2.setPreferredSize(new Dimension(400,600));
		//JTextArea area2 = new JTextArea();
		//JScrollPane scrollPanel2 = new JScrollPane(area2);
		//viewPanel1.add(scrollPanel2);

		JPanel selectPanel = new JPanel();
		selectPanel.add(scrollPanel_list1);
		CCMCloneSetSelected css = new CCMCloneSetSelected(list1,list2,viewPanel1);
		selectPanel.add(new JButton(css));
		selectPanel.add(scrollPanel_list2);
		CCMCodeCloneSelected ccs = new CCMCodeCloneSelected(list2,viewPanel1,viewPanel2,scrollPanel_viewer1);
		css.setSelected(ccs);
		selectPanel.add(new JButton(ccs));
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.add(createButton("戻る","2"));


		mainPanel.add(scrollPanel_viewer1,BorderLayout.WEST);
		mainPanel.add(scrollPanel_viewer2,BorderLayout.CENTER);
		mainPanel.add(selectPanel,BorderLayout.EAST);
		mainPanel.add(ButtonPanel,BorderLayout.SOUTH);
		return mainPanel;
	}

	public HashMap<Integer,CodeSnipets> createSnipetsMap(){
		HashMap<Integer,CodeSnipets> snipetsMap = new HashMap<Integer,CodeSnipets>();
		BufferedReader br = getBufferedReader(FunctionsA.toString());
		try {
			String str = br.readLine();
			while(str!=null){
				CodeSnipets snipet = new CodeSnipets();
				snipet.snipets = new ArrayList<ArrayList<Integer>>();
				if(!StringUtils.isBlank(str)){
					String[] str_split = str.split("[\\s]+",0);
					snipet.CID = Integer.parseInt(str_split[2]);
					str = br.readLine();
					str_split = str.split("[\\s]+",0);
					snipet.chunks = Integer.parseInt(str_split[2]);
					str = br.readLine();
					str_split = str.split("[\\s]+",0);
					snipet.size = Integer.parseInt(str_split[2]);
					for(int i=0;i<snipet.size;i++){
						str = br.readLine();
						String str1 = str.substring(1,str.length()-1);
						str_split = str1.split("[,\\s\t]+",0);
						ArrayList<Integer> idList = new ArrayList<Integer>();
						for(String id : str_split){
							idList.add(Integer.parseInt(id));
						}
						snipet.snipets.add(idList);
					}
					snipetsMap.put(snipet.CID,snipet);
				}
				str = br.readLine();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return snipetsMap;
	}

	public String getSuffix(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(point + 1);
	    }
	    return fileName;
	}

	public String[] toArr(ArrayList<File> list){
        // List<Integer> -> int[]
        int l = list.size();
        String[] arr = new String[l];
        for(int i=0;i<list.size();i++){
        	arr[i] = list.get(i).getPath();
        }
        return arr;
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
				 file = chooser.getSelectedFile();//ここで選択されたファイルを取得しています。
			     textbox.setText(file.getPath());
			     next.setEnabled(true);
			}
		}
	}

	private class FileDialog extends AbstractAction{
		private JTextField textbox;
		JButton next;
		private FileDialog(JTextField textbox,JButton next){
			super("ファイル選択");
			this.textbox = textbox;
			this.next = next;
		}

		@Override
		public void actionPerformed(ActionEvent e){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int answer = chooser.showDialog(null,"選択");
			if(answer == JFileChooser.APPROVE_OPTION){
				 file = chooser.getSelectedFile();//ここで選択されたファイルを取得しています。
			     textbox.setText(file.getPath());
			     next.setEnabled(true);
			}
		}
	}

	private class ClonePairFileDialog extends AbstractAction{
		private JTextField textbox;
		JButton next;
		private ClonePairFileDialog(JTextField textbox,JButton next){
			super("ファイル選択");
			this.textbox = textbox;
			this.next = next;
		}

		@Override
		public void actionPerformed(ActionEvent e){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int answer = chooser.showDialog(null,"選択");
			if(answer == JFileChooser.APPROVE_OPTION){
				 clonePairFile = chooser.getSelectedFile();//ここで選択されたファイルを取得しています。
			     textbox.setText(clonePairFile.getPath());
			     next.setEnabled(true);
			}
		}
	}

	private class FunctionsAFileDialog extends AbstractAction{
		private JTextField textbox;
		JButton next;
		private FunctionsAFileDialog(JTextField textbox,JButton next){
			super("ファイル選択");
			this.textbox = textbox;
			this.next = next;
		}

		@Override
		public void actionPerformed(ActionEvent e){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int answer = chooser.showDialog(null,"選択");
			if(answer == JFileChooser.APPROVE_OPTION){
				 FunctionsA = chooser.getSelectedFile();//ここで選択されたファイルを取得しています。
			     textbox.setText(FunctionsA.getPath());
			     next.setEnabled(true);
			}
		}
	}

	private class CCFinderDialog extends AbstractAction{
		private JTextField textbox;
		JButton next;
		private CCFinderDialog(JTextField textbox,JButton next){
			super("ファイル選択");
			this.textbox = textbox;
			this.next = next;
		}

		@Override
		public void actionPerformed(ActionEvent e){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int answer = chooser.showDialog(null,"選択");
			if(answer == JFileChooser.APPROVE_OPTION){
				 //CCFXPath = chooser.getSelectedFile();//ここで選択されたファイルを取得しています。
			     textbox.setText(chooser.getSelectedFile().getPath());
			     next.setEnabled(true);
			     CCFXPath = chooser.getSelectedFile().getPath();
			}
		}
	}

	private class CloneSetSelected extends AbstractAction{
		private JList<String> SetJList;
		private JList<String> cloneJList;
		private JEditorPane editPane;
		private CodeCloneSelected obj;
		private CloneSetSelected(JList<String> setList,JList<String> cloneList,JEditorPane edit){
			super("選択");
			this.SetJList = setList;
			this.cloneJList = cloneList;
			this.editPane = edit;
		}


		@Override
		public void actionPerformed(ActionEvent e){
			int index = SetJList.getSelectedIndex();
			if(index>-1){
				jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.CloneSet cs = cloneSetList.get(index);
				String[] nums = new String[cs.getCloneList().size()];
				for(int i=0;i<nums.length;i++){
					nums[i]=String.valueOf(i+1);
				}
				listModel2.clear();
				for(int i=0;i<nums.length;i++){
					jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.CodeClone c = cs.getCloneList().get(i);
					String str2 = (i+1)+" 0."+c.getFileId()+" "+c.getLS()+","+c.getCS()+","+c.getTS()+" "+c.getLE()+","+c.getCE()+","+c.getTE();
					listModel2.addElement(str2);
				}
				obj.setId(cs.getCloneSetId());

			}
		}



		public void setSelected(CodeCloneSelected ccs){
			obj = ccs;
		}
	}

	private class CCMCloneSetSelected extends AbstractAction{
		private JList<String> SetJList;
		private JList<String> cloneJList;
		private JEditorPane editPane;
		private CCMCodeCloneSelected obj;
		private CCMCloneSetSelected(JList<String> setList,JList<String> cloneList,JEditorPane edit){
			super("選択");
			this.SetJList = setList;
			this.cloneJList = cloneList;
			this.editPane = edit;
		}


		@Override
		public void actionPerformed(ActionEvent e){
			int index = SetJList.getSelectedIndex();
			if(index>-1){
				jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans.CloneSet cs = CCMcloneSetList.get(index);
				String[] nums = new String[cs.getCloneList().size()];
				for(int i=0;i<nums.length;i++){
					nums[i]=String.valueOf(i+1);
				}
				listModel2.clear();
				for(int i=0;i<nums.length;i++){
					jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans.CodeClone c = cs.getCloneList().get(i);
					String str2 = (i+1)+"  "+c.getCloneSetId()+"  0."+c.getFileId()+" "+c.getLS()+","+c.getCS()+","+c.getTS()+"  "+c.getLE()+","+c.getCE()+","+c.getTE();
					listModel2.addElement(str2);
				}
				obj.setId(cs.getCloneSetId());

			}
		}
		public void setSelected(CCMCodeCloneSelected ccs){
			obj = ccs;
		}
	}

	private class CodeCloneSelected extends AbstractAction{
		private JList<String> cloneJList;
		private JEditorPane editPane1;
		private JEditorPane editPane2;
		private int cloneSetId=0;
		private JScrollPane scrollPane1;
		private CodeCloneSelected(JList<String> setList,JEditorPane edit1,JEditorPane edit2,JScrollPane scrollPane1){
			super("選択");
			this.cloneJList = setList;
			this.editPane1 = edit1;
			this.scrollPane1 = scrollPane1;
			this.editPane2 = edit2;
		}
		public void setId(int id){
			cloneSetId = id;
		}

		@Override
		public void actionPerformed(ActionEvent e){
			int cloneId = cloneJList.getSelectedIndex();
			if(cloneId>-1&&cloneSetId>-1){
				ArrayList<jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.CodeClone> cloneList = fileToCloneMap.get(cloneSetList.get(cloneSetId).getCloneList().get(cloneId).getFileId());
				jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.CodeClone c_selected = cloneSetList.get(cloneSetId).getCloneList().get(cloneId);
				BufferedReader br = getBufferedReader(fileIdList.get(cloneSetList.get(cloneSetId).getCloneList().get(cloneId).getFileId()));
				int line_selected= 0;
				boolean firstline = true;
				boolean syuuyaku = false;
				int length1=0;
				int length2=0;
				html1 = new String("<html><body>");
				html2 = new String("<html><body>");
				try{
					 //<pre style = \"background:#CCFFCC;\">
					 String str = br.readLine();
					 int line1 = 1;
					 int line2 = 1;
					 //int currentId = -1;
					 int currentLine = -1;
					 //int sum_length = 0;
					 while(str != null){
						 boolean find_clone = false;
						 boolean find_selected = false;
						 boolean find_brother = false;
						 for(jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.CodeClone c:cloneList){
							 if(c.getLS()<=line1&&c.getLE()>=line1){
								 find_clone = true;
								 if(c.getLS() != currentLine){
									// System.out.println("find");
									 syuuyaku = true;
									 //currentId = c.getParent().getCloneSetId();
									 currentLine = c.getLS();
								 }else{
									 syuuyaku = false;
								 }
								 if(c.getParent().getCloneSetId()==c_selected.getParent().getCloneSetId()){
									 //System.out.println(c.getParent().getCloneSetId());
									 find_brother = true;
									 if(c.getTS()==c_selected.getTS()&&c.getTE()==c_selected.getTE()){
										find_selected = true;
										if(line_selected==0){
											line_selected = line1;

										}
									 }
								 }
							 }
						 }
						 if(find_clone){
							 if(find_brother){
								 if(find_selected){
									 html1 = html1+"<p style = background-color:#00B4FF;>"+String.valueOf(line1)+str+"</p>";
									 if(firstline){
										 editPane1.setText(html1);
										length1 = editPane1.getDocument().getLength();
										html2 = html2+"<p style = background-color:#00B4FF;>"+String.valueOf(line2++)+"PERFORMorCALL::"+str+"</p>";
										editPane2.setText(html2);
										length2 = editPane2.getDocument().getLength();
										firstline = false;
									 }
								 }else{
									 html1 = html1+"<p style = background-color:'yellow';>"+String.valueOf(line1)+str+"</p>";
									 if(syuuyaku){
										 html2 = html2+"<p style = background-color:'yellow';>"+String.valueOf(line2++)+"PERFORMorCALL::"+str+"</p>";
									 }
								 }
							 }else{
								 html1 = html1+"<p style = background-color:#EEEEEE;>"+String.valueOf(line1)+str+"</p>";
								 if(syuuyaku){
									 html2 = html2+"<p style = background-color:#EEEEEE;>"+String.valueOf(line2++)+"PERFORMorCALL::"+str+"</p>";
								 }
							 }
						 }else{
							 html1 = html1+"<p>"+String.valueOf(line1)+str+"</p>";
							 html2 = html2+"<p>"+String.valueOf(line2++)+str+"</p>";
						 }
						 //sum_length += str.length()-4;
//						 if(c.getLS()>line||c.getLE()<line){
//							 html1 = html1+"<p><pre>"+str+"</pre></p>";
//						 }else{
//							 html1 = html1+"<p><span><pre style = \"background-color:#CCFFCC;\">"+str+"</pre></span></p>";
//						 }
						 str = br.readLine();
						 line1++;
					 }
				 }catch(IOException ioe){
					 System.out.println(ioe.getMessage());
						System.exit(0);
				 }
				 html1 = html1+"</body></html>";

				 //html1 = "<html>"+"<body><h1>"+fileIdList.get(c.getFileId())+"</h1>"+"</html>";
				 editPane1.setText(html1);
				 editPane2.setText(html2);
//				 try{
//					  Thread.sleep(3000);
//					}catch (InterruptedException e1){
//					}
				 editPane1.setCaretPosition(0);
				 editPane1.setCaretPosition(length1);
				 editPane2.setCaretPosition(0);
				 editPane2.setCaretPosition(length2);
				 //scrollPane1.getVerticalScrollBar().setValue(100);
				 //System.out.println(scrollPane1.getVerticalScrollBar().getValue());
			}
		}

		public void setList(JList<String> list){
			this.cloneJList = list;
		}
	}

	private class CCMCodeCloneSelected extends AbstractAction{
		private JList<String> cloneJList;
		private JEditorPane editPane1;
		private JEditorPane editPane2;
		private int cloneSetId=0;
		private JScrollPane scrollPane1;
		private CCMCodeCloneSelected(JList<String> setList,JEditorPane edit1,JEditorPane edit2,JScrollPane scrollPane1){
			super("選択");
			this.cloneJList = setList;
			this.editPane1 = edit1;
			this.scrollPane1 = scrollPane1;
			this.editPane2 = edit2;
		}
		public void setId(int id){
			cloneSetId = id;
		}

		@Override
		public void actionPerformed(ActionEvent e){
			int cloneId = cloneJList.getSelectedIndex();
			if(cloneId>-1&&cloneSetId>-1){
				ArrayList<jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans.CodeClone> cloneList = CCMfileToCloneMap.get(CCMcloneSetList.get(cloneSetId).getCloneList().get(cloneId).getFileId());
				jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans.CodeClone c_selected = CCMcloneSetList.get(cloneSetId).getCloneList().get(cloneId);
				BufferedReader br = getBufferedReader(CCMfileIdList.get(CCMcloneSetList.get(cloneSetId).getCloneList().get(cloneId).getFileId()-1));
				CodeSnipets snipet = snipetsMap.get(c_selected.getCloneSetId());
				int line_selected= 0;
				boolean firstline = true;
				boolean syuuyaku = false;
				int length1=0;
				int length2=0;
				ArrayList<Integer> currentSnipets = null;
				int cloneSetId = -1;
				int lineId = -1;
				html1 = new String("<html><body>");
				html2 = new String("<html><body>");
				try{
					 //<pre style = \"background:#CCFFCC;\">
					 String str = br.readLine();
					 int line1 = 1;
					 int line2 = 1;
					 int currentLine = -1;
					 int color = -1;
					 //int sum_length = 0;
					 while(str != null){
						 boolean first_find =false;
						 boolean find_clone = false;
						 boolean find_selected = false;
						 boolean find_brother = false;
						 for(jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans.CodeClone c:cloneList){
							 if(!first_find&&c.getLS()<=line1&&c.getLE()>=line1&&snipetsMap.containsKey(c.getCloneSetId())){
								 first_find = true;
								 if(currentSnipets==null){
									 syuuyaku = true;
									 color++;
									 System.out.println("color change0");
									 System.out.println(line1-c.getLS());
									 currentSnipets = snipetsMap.get(c.getCloneSetId()).snipets.get(line1-c.getLS());
									 cloneSetId = c.getCloneSetId();
									 lineId = 0;
								 }else{
									if(cloneSetId != c.getCloneSetId()){
										if(compareSnipets(currentSnipets,snipetsMap.get(c.getCloneSetId()).snipets.get(line1-c.getLS()))){
											syuuyaku = false;
										}else{
											syuuyaku = true;
											System.out.println("color change1");
											color++;
										}
										currentSnipets = snipetsMap.get(c.getCloneSetId()).snipets.get(line1-c.getLS());
										cloneSetId = c.getCloneSetId();
										lineId = line1-c.getLS();
									}else{
										//System.out.println(line1-c.getLS());
										if(snipetsMap.get(c.getCloneSetId()).size>line1-c.getLS()){
											if(compareSnipets(currentSnipets,snipetsMap.get(c.getCloneSetId()).snipets.get(line1-c.getLS()))){
												syuuyaku = false;
											}else{
												syuuyaku = true;
												System.out.println("color change2");
												currentSnipets = snipetsMap.get(c.getCloneSetId()).snipets.get(line1-c.getLS());
												cloneSetId = c.getCloneSetId();
												lineId = line1-c.getLS();
												color++;
											}
										}
									}
								 }
								 find_clone = true;
								 if(c.getParent().getCloneSetId()==c_selected.getParent().getCloneSetId()){
									 //System.out.println(c.getParent().getCloneSetId());
									 find_brother = true;
									 if(c.getTS()==c_selected.getTS()&&c.getTE()==c_selected.getTE()){
										find_selected = true;
										if(line_selected==0){
											line_selected = line1;

										}
									 }
								 }
							 }
						 }
						 if(find_clone){
							 if(currentSnipets!=null){
								 System.out.println(line1+":"+currentSnipets);
							 }
							 if(find_brother){
								 if(find_selected){
									 html1 = html1+"<p style = background-color:"+colorPattern[color%4]+";>"+String.valueOf(line1)+str+"</p>";
									 if(firstline){
										 editPane1.setText(html1);
										length1 = editPane1.getDocument().getLength();
										html2 = html2+"<p style = background-color:"+colorPattern[color%4]+";>"+String.valueOf(line2++)+"PERFORMorCALL::"+str+"</p>";
										editPane2.setText(html2);
										length2 = editPane2.getDocument().getLength();
										firstline = false;
									 }
								 }else{
									 html1 = html1+"<p style = background-color:"+colorPattern[color%4]+";>"+String.valueOf(line1)+str+"</p>";
									 if(syuuyaku){
										 html2 = html2+"<p style = background-color:"+colorPattern[color%4]+";>"+String.valueOf(line2++)+"PERFORMorCALL::"+str+"</p>";
									 }
								 }
							 }else{
								 html1 = html1+"<p style = background-color:"+colorPattern[color%4]+";>"+String.valueOf(line1)+str+"</p>";
								 if(syuuyaku){
									 html2 = html2+"<p style = background-color:"+colorPattern[color%4]+";>"+String.valueOf(line2++)+"PERFORMorCALL::"+str+"</p>";
								 }
							 }
						 }else{
							 html1 = html1+"<p>"+String.valueOf(line1)+str+"</p>";
							 html2 = html2+"<p>"+String.valueOf(line2++)+str+"</p>";
						 }
						 //sum_length += str.length()-4;
//						 if(c.getLS()>line||c.getLE()<line){
//							 html1 = html1+"<p><pre>"+str+"</pre></p>";
//						 }else{
//							 html1 = html1+"<p><span><pre style = \"background-color:#CCFFCC;\">"+str+"</pre></span></p>";
//						 }
						 str = br.readLine();
						 line1++;
					 }
				 }catch(IOException ioe){
					 System.out.println(ioe.getMessage());
						System.exit(0);
				 }
				 html1 = html1+"</body></html>";

				 //html1 = "<html>"+"<body><h1>"+fileIdList.get(c.getFileId())+"</h1>"+"</html>";
				 editPane1.setText(html1);
				 editPane2.setText(html2);
//				 try{
//					  Thread.sleep(3000);
//					}catch (InterruptedException e1){
//					}
				 editPane1.setCaretPosition(0);
				 editPane1.setCaretPosition(length1);
				 editPane2.setCaretPosition(0);
				 editPane2.setCaretPosition(length2);
				 //scrollPane1.getVerticalScrollBar().setValue(100);
				 //System.out.println(scrollPane1.getVerticalScrollBar().getValue());
			}
		}

		public boolean compareSnipets(ArrayList<Integer> snipet1,ArrayList<Integer> snipet2){
			if(snipet1.size()!=snipet2.size()){
				System.out.println("snipet1:"+snipet1+"\tsnipet2:"+snipet2);
				return false;
			}else{
				for(int index =0; index < snipet1.size();index++){
					if(snipet1.get(index).compareTo(snipet2.get(index))!=0){
						System.out.println("snipet1:"+snipet1.get(index)+"\tsnipet2:"+snipet2.get(index));
						return false;
					}
				}
			}
			return true;
		}

		public void setList(JList<String> list){
			this.cloneJList = list;
		}
	}

	public static void main(String[] args){
		new MyFrame();
	}


	public void actionPerformed(ActionEvent e){
		int command = Integer.parseInt(e.getActionCommand());
		JPanel panel = new JPanel();
		switch(command){
		case 0:
			panel = getDefPanel(); // next 1 or 2
			break;
		case 1:
			panel = getSelectDirectryPanel(); // next 3
			break;
		case 2:
			panel = getSelectFilePanel(); //next 4
			break;
		case 3:
			panel = getFileListPanel(); //next 5
			break;
		case 4: // next
			panel = getViewerPanel();
			break;
		case 5:
			panel = getOptionPanel(); //next 6
			break;
		case 6:
			mainArea = new JTextArea();
			panel.add(mainArea);
			//panel = getExecutionPanel(); //next 7
			break;
		case 7:
			break;
		case 8:
			panel = getSelectTwoFilePanel(); //next 9
			break;
		case 9:
			panel = getSelectThreeFilePanel(); //next 10
			break;
		case 10:
			panel = getCCMViewerPanel();
			break;
		default:
			break;
		}

		getContentPane().removeAll();
		getContentPane().add(panel);
		setVisible(true);
		if(command==6){
			operateDetector();
			panel.add(createButton("戻る","0"));
		}

	}

	public void operateDetector(){
		mainArea.append("コードクローンの検出を開始しています．\n");
		mainArea.append("ソースコードを抽出しています．\n");
		ForcusSourceCode.setDirPath(file.getPath());
		for(File fileName:cobolFileList){
			ForcusSourceCode.forcus(fileName.getName());
		}
		mainArea.append("CCFinderXを実行しています．\n");
		executeCCFX();
		mainArea.append("CONVERTERを実行しています．\n");
		convertCCFXtoCCF();
		ESCMA_COBOL.main(new String[]{"c.txt"});
	}

	public void executeCCFX(){
		String APath = new File(".").getAbsolutePath() +"\\a.ccfxd";
		String XPath = CCFXPath + "\\ccfx";
		String PPath = CCFXPath + "\\picosel";
		/**CCFinderXを実行　検出するトークン長を指定する．　その後，picoselコマンドを用いてRNRでフィルタリングする．(LNRを求める手間を省くため)**/
		executeCommand(new String[]{XPath,"d","cobol","-b",String.valueOf(model_Size.getValue()),"-dn",file.getPath()+"\\ESCMAver2"});
		executeCommand(new String[]{XPath,"m",APath,"-c","-o","RNR.tsv"});
		executeCommand(new String[]{PPath,"-o","filtered.txt","from","RNR.tsv","select","CID","where","RNR",".ge.",String.valueOf(model_RNR.getValue())});
		executeCommand(new String[]{XPath,"s",APath,"-o","filtered.ccfxd","-ci","filtered.txt"});
		/**filtered.ccfxdをコンバートする．**/
		executeCommand(new String[]{XPath,"p","filtered.ccfxd","-o","b.txt"});

	}

	public void executeCommand(String[] command){
		for(String c:command){
			System.out.print(c +" ");
		}
		System.out.println();
			ProcessBuilder pb = new ProcessBuilder(command);
			Process proc;
			try {
				proc = pb.start();
				pb.redirectErrorStream(true);
				BufferedReader brstd = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				brstd.close();
				proc.waitFor();
			} catch (IOException | InterruptedException e) {
				System.out.println("Process Stops");
				System.exit(0);
			}
	}

	public PrintWriter getPrintWriter(String file){
		File output = new File(file);
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(output,false)));
		}catch(IOException e2){
			e2.printStackTrace();
		}
		return null;
	}

	public void convertCCFXtoCCF(){
		ArrayList<String> fileIdList = createFileIdList("b.txt");
		HashMap<Integer,ArrayList<CodeClone>> fileToCloneMap = createFileToCloneMap("b.txt");
		ArrayList<CloneSet> cloneSetList = createCloneSetList(fileToCloneMap);
		String[] prepHierarchy = getPrepHierarchy("b.txt");
		findCloneLocation(fileIdList, prepHierarchy, fileToCloneMap);
		convert(fileIdList,cloneSetList);
	}

	public void convert(ArrayList<String> fileList,ArrayList<CloneSet> cloneSetList){
		PrintWriter pw = getPrintWriter(new File("c.txt"));
		pw.println("#begin{file description}");
			for(int i=0;i<fileList.size();i++){
				pw.println("0."+i+"\t0\t0\t"+fileList.get(i));
			}
		pw.println("#end{file description}");
		pw.println("#begin{clone}");
		for(CloneSet cs : cloneSetList){
			pw.println("#begin{set}");
			for(CodeClone c:cs.getCloneList()){
				/*fileId LS,CS,TS LE,CE,TE, LNR*/
				pw.println("0."+(c.getFileId()-1)+"\t"+c.getLS()+","+c.getCS()+","+c.getTS()
						+"\t"+c.getLE()+","+c.getCE()+","+c.getTE()+"\t0");
			}
			pw.println("#end{set}");
		}
		pw.println("#end{clone}");
		pw.close();
	}

	public  ArrayList<String> createFileIdList(String pass){
		BufferedReader br = getBufferedReader(pass);
		ArrayList<String> fileIdList = new ArrayList<String>();
		try{
			String str = br.readLine();
			boolean canRead = false;
			while(str!=null){
				if(str.equals("source_files {")){
					canRead = true;
				}else if(str.equals("}")){
					canRead = false;
				}else if(canRead){
					String[] str_split = str.split("[\\s]+",0);
					fileIdList.add(str_split[1]);
				}
				str=br.readLine();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return fileIdList;
	}

	public ArrayList<CloneSet> createCloneSetList(HashMap<Integer,ArrayList<CodeClone>>map){
		ArrayList<CloneSet> cloneSetList = new ArrayList<CloneSet>();
		int cloneSetId=0;
		for(ArrayList<CodeClone> list:map.values()){
			for(CodeClone c : list){
				if(c.getParent().getCloneSetId()==-1){
					cloneSetList.add(c.getParent().setCloneSetId(cloneSetId++));
				}
			}
		}
		return cloneSetList;
	}

	public HashMap<Integer,ArrayList<CodeClone>> createFileToCloneMap(String pass){
		BufferedReader br = getBufferedReader(pass);
		HashMap<Integer,ArrayList<CodeClone>> fileToCloneMap = new HashMap<Integer,ArrayList<CodeClone>>();
		try{
			String str = br.readLine();
			boolean canRead = false;
			while(str!=null){
				if(str.equals("clone_pairs {")){
					canRead = true;
				}else if(str.equals("}")){
					canRead = false;
				}else if(canRead){
					String[] str_split = str.split("[.,\\-\t]+",0);
					CodeClone c1 = new CodeClone()
					.setFileId(Integer.parseInt(str_split[1]))
					.setToken(Integer.parseInt(str_split[2]),Integer.parseInt(str_split[3]));
					CodeClone c2 = new CodeClone()
					.setFileId(Integer.parseInt(str_split[4]))
					.setToken(Integer.parseInt(str_split[5]), Integer.parseInt(str_split[6]));
					int index1 = -1;
					if(fileToCloneMap.containsKey(c1.getFileId())){
						index1=getCodeCloneIndex(fileToCloneMap.get(c1.getFileId()),c1);
					}else{
						fileToCloneMap.put(c1.getFileId(), new ArrayList<CodeClone>());
					}
					int index2 = -1;
					if(fileToCloneMap.containsKey(c2.getFileId())){
						index2=getCodeCloneIndex(fileToCloneMap.get(c2.getFileId()),c2);
					}else{
						fileToCloneMap.put(c2.getFileId(), new ArrayList<CodeClone>());
					}
					if(index1==-1&&index2==-1){
						fileToCloneMap.get(c1.getFileId()).add(c1);
						fileToCloneMap.get(c2.getFileId()).add(c2);
						CloneSet cs = new CloneSet();
						c1.setParent(cs);
						c2.setParent(cs);
						cs.getCloneList().add(c1);
						cs.getCloneList().add(c2);
					}else if(index1!=-1&&index2==-1){
						fileToCloneMap.get(c2.getFileId()).add(c2);
						CloneSet cs = fileToCloneMap.get(c1.getFileId()).get(index1).getParent();
						c2.setParent(cs);
						cs.getCloneList().add(c2);
					}else if(index1==-1&&index2!=-1){
						fileToCloneMap.get(c1.getFileId()).add(c1);
						CloneSet cs = fileToCloneMap.get(c2.getFileId()).get(index2).getParent();
						c1.setParent(cs);
						cs.getCloneList().add(c1);
					}
				}
				str=br.readLine();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return fileToCloneMap;
	}

	public int getCodeCloneIndex(ArrayList<CodeClone>list,CodeClone clone){
		for(int i=0;i<list.size();i++){
			CodeClone c = list.get(i);
			if(c.getTS()==clone.getTS()&&c.getTE()==clone.getTE()){
				return i;
			}
		}
		return -1;
	}

	public String[] getPrepHierarchy(String pass){
		BufferedReader br = getBufferedReader(pass);
		String[] prepHierarchy = new String[2];
		try{
			String str = br.readLine();
			while(str!=null){
				String[] str_split = str.split("[\\s]+",0);
				if(str_split[0].equals("option:")){
					if(str_split[1].equals("-preprocessed_file_postfix")){
						prepHierarchy[0] = str_split[2];
					}else if(str_split[1].equals("-n")){
						prepHierarchy[1] = str_split[2];
						if(!prepHierarchy[0].isEmpty())
						return prepHierarchy;
					}
				}
				str = br.readLine();
			}

		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return prepHierarchy;
	}

	public void findCloneLocation(ArrayList<String> fileIdList,String[] prepHierarchy,HashMap<Integer,ArrayList<CodeClone>> map){
		for(int fileId:map.keySet()){
			StringBuffer sb = new StringBuffer();
			sb.append(prepHierarchy[1]);
			sb.append("\\.ccfxprepdir");
			sb.append(fileIdList.get(fileId-1).substring(prepHierarchy[1].length(), fileIdList.get(fileId-1).length()));
			sb.append(prepHierarchy[0]);
			String prepLocation =  sb.toString();
			BufferedReader br = getBufferedReader(prepLocation);
			try {
				String str = br.readLine();
				ArrayList<Integer[]> locList = new ArrayList<Integer[]>();
				while(str!=null){
					String[] str_split = str.split("[.\\s]+",0);
					Integer[] loc = new Integer[]{
							Integer.parseInt(str_split[0],16),Integer.parseInt(str_split[1],16)};
					locList.add(loc);
					str = br.readLine();
				}
				for(CodeClone c:map.get(fileId)){
					c.setLine(locList.get(c.getTS())[0],locList.get(c.getTE())[0])
					.setColumn(locList.get(c.getTS())[1], locList.get(c.getTE())[1]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public BufferedReader getBufferedReader(String file){
		File input = new File(file);
		if(!input.exists()){
			System.out.println("there are no file. : " + file);
			System.exit(0);
		}
		try {
			return new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PrintWriter getPrintWriter(File output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(output,false)));
		}catch(IOException e2){
			e2.printStackTrace();
		}
		return null;
	}
}
