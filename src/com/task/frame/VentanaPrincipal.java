package com.task.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.task.engine.TaskEngine;
import com.task.engine.TaskLogDto;

public class VentanaPrincipal extends JFrame implements MouseListener, KeyListener {
	
	JButton btnSaveLog;
	JButton btnContinueActivity;
	JButton btnCerrar;
	
	JButton btnBreak;
	
	
	JTextArea txtLogTask;
	JTextArea txtHistorial;
	JScrollPane scrollLOG;
	JScrollPane scrollHIS;
	JcTrayIcon jct = new JcTrayIcon( this );
	boolean swcontrol = false;
	boolean swshit = false;
	
	public VentanaPrincipal() {
		Dimension dim = new Dimension(300,300);
		this.setPreferredSize(dim);
		this.setSize(dim);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initializeControls();
		loadControls();
		loadEvents();
		
		this.setUndecorated(true);
		this.setLocationRelativeTo(null);
		
		invokeRemoteAction();
		jct.iniciarProceso();
		this.setVisible(true);
	}
	
	private void initializeControls() {
		btnSaveLog = new JButton("Salvar");
		btnBreak = new JButton("Break");
		
		btnContinueActivity = new JButton("Continuo");
		btnCerrar = new JButton("Cerrar");
		txtLogTask = new JTextArea(15,5);
		txtHistorial = new JTextArea(15,5);
		txtLogTask.setLineWrap(true);
		txtHistorial.setLineWrap(true);
		Dimension dim = new Dimension(200,100);
		txtLogTask.setSize(dim);
		txtLogTask.setPreferredSize(dim);
		txtLogTask.setText("");
		
		txtHistorial.setSize(dim);
		txtHistorial.setPreferredSize(dim);
		txtHistorial.setText("");
		
		scrollHIS = new JScrollPane(txtLogTask);
        
        scrollLOG = new JScrollPane(txtHistorial);
        Dimension dimHis = new Dimension(100,50);
        Dimension dimLog = new Dimension(100,50);
        scrollHIS.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollLOG.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollHIS.setSize(dimHis);
        scrollLOG.setSize(dimLog);
        scrollHIS.setPreferredSize(dimHis);
        scrollLOG.setPreferredSize(dimLog);
        
        txtHistorial.setEditable(false);
        txtHistorial.setEnabled(false);
	}
	private void loadControls() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints cons =null;
		cons = new GridBagConstraints();
		cons.fill= GridBagConstraints.BOTH;
		cons.weightx=1.0;
		cons.weighty=1.0;
		cons.gridwidth=2;
		cons.gridx=0;
		cons.gridy=1;
		
		
		
		this.getContentPane().add(scrollHIS,cons);
		
		
		
		
		cons = new GridBagConstraints();
		cons.fill= GridBagConstraints.BOTH;
		cons.weightx=1.0;
		cons.weighty=0.3;
		cons.gridwidth=2;
		cons.gridx=0;
		cons.gridy=2;
		this.getContentPane().add(scrollLOG,cons);
		
		
		cons = new GridBagConstraints();
		cons.fill= GridBagConstraints.HORIZONTAL;
		cons.gridx=0;
		cons.gridy=3;		
		cons.weightx=1.0;
		this.getContentPane().add(btnSaveLog,cons);
		
		cons = new GridBagConstraints();
		cons.fill= GridBagConstraints.HORIZONTAL;
		cons.gridx=1;
		cons.gridy=3;		
		cons.weightx=1.0;
		this.getContentPane().add(btnContinueActivity,cons);
		
		
		
		cons = new GridBagConstraints();
		cons.fill= GridBagConstraints.HORIZONTAL;
		cons.gridx=0;
		cons.gridy=4;	
		cons.gridwidth=2;
		cons.weightx=1.0;
		this.getContentPane().add(btnBreak,cons);

		//cons = new GridBagConstraints();
		//cons.anchor = GridBagConstraints.EAST;
		//cons.gridx=1;
		//cons.gridy=0;
		//this.getContentPane().add(btnCerrar,cons);
		
		
		((JComponent)this.getContentPane()).setBorder((Border) new LineBorder(Color.BLACK));
		
		
	}
	private void loadEvents() {
		this.addMouseListener(this);
		this.addKeyListener(this);
		txtHistorial.addKeyListener(this);
		txtLogTask.addKeyListener(this);
		btnSaveLog.addMouseListener(this);
		btnBreak.addMouseListener(this);
		btnContinueActivity.addMouseListener(this);
		btnCerrar.addMouseListener(this);

	}

	public void invokeRemoteAction() {
		txtLogTask.setText("");
		TaskLogDto lastLog = TaskEngine.getLastLog();
		if(lastLog.getFecha() == null || "".equals(lastLog.getLog())) {
			btnContinueActivity.setEnabled(false);
			btnBreak.setEnabled(false);
		} else {
			btnContinueActivity.setEnabled(true);
			btnBreak.setEnabled(true);
			txtHistorial.setText(lastLog.getLog());
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource() == btnSaveLog) {
			saveTask();
			
		} else if(e.getSource() == btnContinueActivity) {
			continueTask();
		} else if(e.getSource() == btnBreak) {
			breakInitialize();
		}
	}
	private void breakInitialize() {
		this.setVisible(false);
		TaskEngine.saveLog(TaskEngine.BREAK);
	}
	private void continueTask() {
		this.setVisible(false);
		TaskLogDto lastLog = TaskEngine.getLastLog();
		TaskEngine.saveLog(lastLog.getLog());
	}

	private void waitLunch() {
		
	}
	private void saveTask() {
		if(txtLogTask.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Debe digitar una tarea ");
		} else {
			this.setVisible(false);
			TaskEngine.saveLog(txtLogTask.getText());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			swcontrol = true;
		} else {
			if (swcontrol && e.getKeyCode() == KeyEvent.VK_ENTER) {
				saveTask();
				swcontrol = false;
			}
		}		
		
		if (e.getKeyCode() == KeyEvent.VK_F5) {
				continueTask();
				swshit = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F9) {
			breakInitialize();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
