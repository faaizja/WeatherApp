package assets;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.json.simple.JSONObject;

public class WeatherAppGui extends JFrame {

	private JSONObject weatherData;

	public WeatherAppGui() { // constructor

		super("Weather App!");

		setDefaultCloseOperation(EXIT_ON_CLOSE); // END PROGRAM ONCE CLOSED

		setSize(450, 650); // gui size

		setLocationRelativeTo(null); // center in screen

		setLayout(null); // manually position components in grid

		setResizable(false); // prevent resizing of gui

		addGuiComponents();

	}

	private void addGuiComponents() {
		// search field in app

		JTextField searchTextField = new JTextField();

		searchTextField.setBounds(15, 15, 351, 45); // set location and size of search box

		searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24)); // set font style and size
		add(searchTextField);
		
		//weather image
		JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
		weatherConditionImage.setBounds(0,125,450,217);
		add(weatherConditionImage); // add image
		
		// temp text
		JLabel temperatureText = new JLabel("10 C");
		temperatureText.setBounds(0,350,450,54); // set size and location
		temperatureText.setFont(new Font("Dialog",Font.BOLD,48)); // create font
		temperatureText.setHorizontalAlignment(SwingConstants.CENTER); // allign text
		add(temperatureText); // create
		
		
		// create weather condition description
		JLabel weatherConditionDesc = new JLabel("Cloudy");
		weatherConditionDesc.setBounds(0,405,450,36); // set location and size
		weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN,32));
		weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
		add(weatherConditionDesc); // create
		
		// humidity image
		JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
		humidityImage.setBounds(15,500,74,66); // set location and size
		add(humidityImage); // create
		
		//humidity text
		JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
		humidityText.setBounds(90,500,85,55); // set size and location
		humidityText.setFont(new Font("Dialog",Font.BOLD,16)); // create font
		add(humidityText);
		
		
		//windspeed image
		JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
		windspeedImage.setBounds(220,500,74,66); // set size and location
		add(windspeedImage); // create 
		
		// windspeed text
		JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
		windspeedText.setBounds(310,500,85,55); // set size and location
		add(windspeedText); // create
		
		// search button
				JButton searchButton = new JButton(loadImage("src/assets/search.png"));
				
				// if hovering on this button change to hand cursor
				searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				searchButton.setBounds(375,13,47,45);
				
				searchButton.addActionListener(new ActionListener() {
					@Override 
					public void actionPerformed(ActionEvent e) {
						// get location that user enters to retrieve weather
						String userInput = searchTextField.getText();
						
						// remove whitespace
						if (userInput.replaceAll("\\s", "").length() <= 0) {
							return;
						} // if
						
						// retrieve weather data
						weatherData = WeatherApp.getWeatherData(userInput);
						
						// update gui
						
						// update image
						String weatherCondition = (String) weatherData.get("weather_condition");
						
						switch(weatherCondition) {
						case "Clear": 
							weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
							break;
						case "Cloudy": 
							weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
							break;
						case "Rain": 
							weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
							break;
						case "Snow": 
							weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
							break;
						
						} // end switch
						
						// update temp text
						double temperature = (double) weatherData.get("temperature");
						temperatureText.setText(temperature + " C");
						
						// update weather condition text
						long humidity = (long) weatherData.get("humidity");
						humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");
						
						// update windspeed text
						double windspeed = (double) weatherData.get("windspeed");
						windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");
						
					} // actionPerformed
					
				}); // addactionlistener
				
				add(searchButton); // add button
		
	} // end addguicomponents

	// create images in our gui
	private ImageIcon loadImage(String resourcePath) {
		try {
			BufferedImage image = ImageIO.read(new File(resourcePath)); // read image file path

			return new ImageIcon(image); // return image icon to be rendered

		} catch (IOException e) {
			e.printStackTrace();
		}

		// throw exception if nothing found
		System.out.println("Could not find resource!");
		return null;

	} // end image icon method

} // end class
