package com.task.engine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TaskLogDto {

	private String fechaString;
	private String log;
	private Date fecha;
	
	public TaskLogDto(String fecha, String log) {
		this.fechaString = fecha;
		this.log = log;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date = formatter.parse(this.fechaString);
			this.fecha = date;
		} catch (Exception e) {
			this.fecha = null;
		}
	}

	
	public String getHoraMinuto() {
		String fecha;
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(this.fecha);
			fecha = "" + TaskEngine.fill(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),2,"0");
			fecha = fecha + ":" + TaskEngine.fill(String.valueOf(calendar.get(Calendar.MINUTE)),2,"0");
		return fecha;
	}
	
	
	
	
	
	
	
	
	
	
	public String getFechaString() {
		return fechaString;
	}

	public void setFechaString(String fechaString) {
		this.fechaString = fechaString;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
}
