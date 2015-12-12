package com.task.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TaskEngine {
	public static String PATH_BASE = "taskLog";
	public static String TOKEN_SPLIT = "#=#";
	public static String BREAK = "¡¡¡BREAK!!!";
	public static String VERSION = "1.2";
	
	public static void saveLog(String log) {
		preparing();
		BufferedWriter out = null;   
		try {   
		    out = new BufferedWriter(new FileWriter(PATH_BASE+getFormatDate(false)+VERSION+".log", true));   
		    out.write("\n"+getFormatDate(true).trim() + TOKEN_SPLIT + log.trim()+TOKEN_SPLIT);   
		} catch (IOException e) {   
		    // error processing code   
		} finally {   
		    if (out != null) {   
		        try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}   
		    }   
		} 
	}
	
	private static  void preparing() {
		
		File archivo = new File(PATH_BASE+getFormatDate(false)+VERSION+".log");
		if(!archivo.exists()) {
			try {
				archivo.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static String getTaskLogDay() {
		List<String> logs = new ArrayList<String>();
		preparing();
		String linea = "";
		String lastLog = "";
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			archivo = new File(PATH_BASE + getFormatDate(false) + VERSION +".log");
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			while ((linea = br.readLine()) != null) {
				System.out.println(linea);
				if (linea.indexOf(TOKEN_SPLIT) >= 0) {
					if(!"".equals(lastLog)) {
						logs.add(lastLog);
					}
					lastLog = linea.split(TOKEN_SPLIT)[0] + "\n" + linea.split(TOKEN_SPLIT)[1];
				} else {
					lastLog = lastLog + "\n" + linea;
				}

			}
			logs.add(lastLog);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		int cant = logs.size();
		String actual = null;
		List<String> depurados = new ArrayList<String>();
		depurados.add(logs.get(1));
		
		for(int i=0;i<cant-1; i++){
			String cont1 = logs.get(i).split(TOKEN_SPLIT)[1];
			String cont2 = logs.get(i+1).split(TOKEN_SPLIT)[1];
			if(cont1.trim().toUpperCase().equals(cont2.trim().toUpperCase())) {
				if(!cont1.equals(actual)) {
					depurados.add(cont1);
					actual = cont1;
				}
			} else {
				
			}
			
		}
		
		
		
		StringBuilder cons = new StringBuilder();
		String c = "";
		for(String item : depurados) {
			c = item + "\n" + c;
		}
		
		return c;
		

	}
	
	public static String getLogDay() {
		
		preparing();
		TaskLogDto last = new TaskLogDto("", "");
		String content = getFileContent();
		String[] elements = content.split(TOKEN_SPLIT);
		List<TaskLogDto> logs = new ArrayList<TaskLogDto>();
		List<TaskLogDto> toDelete = new ArrayList<TaskLogDto>();
		if(elements.length > 1) {
			for(int i = 0; i<elements.length; i=i+2) {
				logs.add(new TaskLogDto(elements[i],elements[i+1]));
			}
			int cant = logs.size();
			
//			while(logs.get(cant-1).getLog().equals(BREAK)) {
//				cant--;
//			}
			last = logs.get(cant-1);
		}
		
		for(int i=0;i<logs.size()-1;i++) {
			if(logs.get(i).getLog().trim().toUpperCase().equals(logs.get(i+1).getLog().trim().toUpperCase())) {
				toDelete.add(logs.get(i+1));
			}
		}
		
		for(TaskLogDto item : toDelete) {
			logs.remove(item);
		}
		
		StringBuilder cons = new StringBuilder();
		
		for(int i=0;i<logs.size()-1;i++) {
			cons.append(logs.get(i).getHoraMinuto() + " - " + logs.get(i+1).getHoraMinuto() + "  " + logs.get(i).getLog() +"\n" );
		}
		String fecha;
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date());
			fecha = "" + TaskEngine.fill(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),2,"0");
			fecha = fecha + ":" + TaskEngine.fill(String.valueOf(calendar.get(Calendar.MINUTE)),2,"0");
		cons.append(logs.get(logs.size()-1).getHoraMinuto() + " - " + fecha + "  " + logs.get(logs.size()-1).getLog() +"\n" );
		
		
		return cons.toString();
	}
	public static TaskLogDto getLastLog() {
		preparing();
		TaskLogDto last = new TaskLogDto("", "");
		String content = getFileContent();
		String[] elements = content.split(TOKEN_SPLIT);
		List<TaskLogDto> logs = new ArrayList<TaskLogDto>();
		if(elements.length > 1) {
			for(int i = 0; i<elements.length; i=i+2) {
				logs.add(new TaskLogDto(elements[i],elements[i+1]));
			}
			int cant = logs.size();
			
			while(logs.get(cant-1).getLog().equals(BREAK)) {
				cant--;
			}
			last = logs.get(cant-1);
		}
		
		return last;
	}
	
	private static String getFileContent() {
		
		StringBuilder cons = new StringBuilder();
		String linea = "";
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			archivo = new File(PATH_BASE+getFormatDate(false)+VERSION+".log");
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			
			while ((linea = br.readLine()) != null) {
				cons.append(linea);
				
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return cons.toString();
	}
	public static String getLastLog__() {
		preparing();
		String linea = "";
		String lastLog="";
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			archivo = new File(PATH_BASE+getFormatDate(false)+VERSION+".log");
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			
			while ((linea = br.readLine()) != null) {
				System.out.println(linea);
				if(linea.indexOf(TOKEN_SPLIT)>=0) {
					lastLog = linea.split(TOKEN_SPLIT)[1];
				} else {
					lastLog= lastLog+"\n"+linea;
				}
				
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		
		return lastLog;
	}
	public static String fill(String text,int pad,String caracter) {
		StringBuilder cons = new StringBuilder();
		int longitud = pad - text.length();
		for(int i=0;i < longitud;i++) {
			cons.append(caracter);
		}
		cons.append(text);
		return cons.toString();
		
	}
	
	public static String getFormatDate(boolean full) {
		String fecha;
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date());
		fecha = String.valueOf(calendar.get(Calendar.YEAR));
		fecha = fecha + "-" + fill(String.valueOf(calendar.get(Calendar.MONTH)),2,"0");
		fecha = fecha + "-" + fill(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),2,"0");
		if(full) {
			fecha = fecha + " " + fill(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),2,"0");
			fecha = fecha + ":" + fill(String.valueOf(calendar.get(Calendar.MINUTE)),2,"0");
		}
		return fecha;
	}
}
