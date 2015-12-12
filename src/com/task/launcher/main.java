package com.task.launcher;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import com.task.engine.Util;
import com.task.frame.VentanaPrincipal;

public class main {

	public static void main(String[] args) {
		
		try {
			new VentanaPrincipal();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
