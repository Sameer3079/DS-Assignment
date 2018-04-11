import java.io.Serializable;

public class Reading implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8777199189672505760L;
	/**
	 * 
	 */
	private boolean alert = false;
	private String sensorId;
	private double temperature;
	private double batteryLevel;
	private int smokeLevel;
	private int co2Level;
	public String getSensorId() {
		return sensorId;
	}
	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getBatteryLevel() {
		return batteryLevel;
	}
	public void setBatteryLevel(double batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
	public int getSmokeLevel() {
		return smokeLevel;
	}
	public void setSmokeLevel(int smokeLevel) {
		this.smokeLevel = smokeLevel;
	}
	public int getCo2Level() {
		return co2Level;
	}
	public void setCo2Level(int co2Level) {
		this.co2Level = co2Level;
	}
	
	public boolean isAlert() {
		return alert;
	}
	public void PrintReading() {
		System.out.println("Sensor: "+getSensorId()+" Temp.: "+getTemperature()+" Battery: "+getBatteryLevel()+" Smoke: "+getSmokeLevel()+" CO2: "+getCo2Level());
	}
	
	public void PrintAlertReading() {
		System.out.println("ALERT from Sensor: "+getSensorId()+" Temp.: "+getTemperature()+" Battery: "+getBatteryLevel()+" Smoke: "+getSmokeLevel()+" CO2: "+getCo2Level());
	}
	
	public boolean activateAlert() {
		if (temperature > 50 || smokeLevel > 7 ) {
			alert = true;
			return true;
		}
		return false;
	}
	
	
}
