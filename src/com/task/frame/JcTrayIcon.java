package com.task.frame;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.task.engine.TaskEngine;
import com.task.engine.Util;

/**
 * @web http://www.jc-mouse.net
 * @author Mouse
 */
public class JcTrayIcon {

	private VentanaPrincipal miframe;
	private PopupMenu popup = new PopupMenu();
//	private Image image = new ImageIcon(getClass().getResource("/com/task/sources/clock.png")).getImage();
	private Image image = new ImageIcon(getClass().getResource("/com/task/sources/testing.png")).getImage();
	private final TrayIcon trayIcon = new TrayIcon(image, "Usetime", popup);
	private Timer timer;
	private boolean band;
	private boolean breakTime = false;

	public JcTrayIcon(VentanaPrincipal frame) {
		
		this.miframe = frame;
		// comprueba si SystemTray es soportado en el sistema
		if (SystemTray.isSupported()) {
			// obtiene instancia SystemTray
			SystemTray systemtray = SystemTray.getSystemTray();
			// acciones del raton sobre el icono en la barra de tareas
			MouseListener mouseListener = new MouseListener() {

				public void mouseClicked(MouseEvent evt) {
					// Si se presiono el boton izquierdo y la aplicacion esta
					// minimizada
					if (evt.getButton() == MouseEvent.BUTTON1
							&& miframe.getExtendedState() == JFrame.ICONIFIED)
						MensajeTrayIcon(
								"'Lamento boliviano' \n Y yo estoy aqui, Borracho y loco, \n Y mi corazon idiota, Siempre brillara",
								MessageType.WARNING);
				}

				public void mouseEntered(MouseEvent evt) {
				}

				public void mouseExited(MouseEvent evt) {
				}

				public void mousePressed(MouseEvent evt) {
				}

				public void mouseReleased(MouseEvent evt) {
				}
			};

			// ACCIONES DEL MENU POPUP
			// Salir de aplicacion
			ActionListener exitListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			};
			// Restaurar aplicacion
			ActionListener RestaurarListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					miframe.invokeRemoteAction();
					miframe.setVisible(true);
					miframe.setLocationRelativeTo(null);
					miframe.setAlwaysOnTop(true);
				}
			};
			// Se crean los Items del menu PopUp y se añaden

			ActionListener DesplegarLog = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String text = TaskEngine.getLogDay();
					JTextArea txt = new JTextArea(30, 30);
					txt.setText(text);
					txt.setSize(new Dimension(300, 300));
					txt.setPreferredSize(new Dimension(300, 300));
					txt.setWrapStyleWord(true);
					JOptionPane.showMessageDialog(null, txt);
				}
			};

			MenuItem SalirItem = new MenuItem("Salir");
			SalirItem.addActionListener(exitListener);
			popup.add(SalirItem);

			MenuItem ItemRestaurar = new MenuItem("Agregar tarea");
			ItemRestaurar.addActionListener(RestaurarListener);
			popup.add(ItemRestaurar);

			MenuItem logDelDia = new MenuItem("LOG del día");
			logDelDia.addActionListener(DesplegarLog);
			popup.add(logDelDia);

			trayIcon.setImageAutoSize(true);
			trayIcon.addMouseListener(mouseListener);

			// Añade el TrayIcon al SystemTray
			try {
				systemtray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("Error:" + e.getMessage());
			}
		} else {
			System.err.println("Error: SystemTray no es soportado");
		}

		// Cuando se minimiza JFrame, se oculta para que no aparesca en la barra
		// de tareas
		miframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowIconified(WindowEvent e) {
				miframe.setVisible(false);// Se oculta JFrame
				// Se inicia una tarea cuando se minimiza
				band = false;
				if (timer == null) {
					timer = new Timer();
					timer.schedule(new MyTimerTask(), 0, 1000 * 60);// Se
																	// ejecuta
																	// cada 10
																	// segundos
				}
			}
		});

	}

	public void setBreakTime() {
		breakTime = true;
	}

	// Muestra una burbuja con la accion que se realiza
	public void MensajeTrayIcon(String texto, MessageType tipo) {
		trayIcon.displayMessage("Mi Aplicación dice:", texto, tipo);
	}

	public void iniciarProceso() {
		miframe.setVisible(false);
		band = false;
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new MyTimerTask(), 0, 1000 * 58);// Se ejecuta cada
															// 58 segundos
		}
	}

	// clase interna que manejara una accion en segundo plano
	class MyTimerTask extends TimerTask {
		public void run() {
			taskAlert();
		}

		// Una accion a realiza cuando la aplicacion a sido minimizada
		public void taskAlert() {
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(new Date());

			int minute = cal.get(GregorianCalendar.MINUTE);
			int hour = cal.get(GregorianCalendar.HOUR_OF_DAY);
			
			boolean p = hour <= 12;
			boolean q = hour >=14;
			if (minute % 15 == 0) {
				if( (p && !q) || (!p && q)) {	
					miframe.invokeRemoteAction();
					miframe.setVisible(true);
					miframe.setLocationRelativeTo(null);
					miframe.setAlwaysOnTop(true);
				}
			}
		}

	}

}