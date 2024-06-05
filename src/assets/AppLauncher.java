package assets;

import javax.swing.SwingUtilities;

public class AppLauncher {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override

			public void run() {
				// show weather app gui here
				new WeatherAppGui().setVisible(true);
//				System.out.println(WeatherApp.getLocationData("Tokyo"));
				
//				System.out.println(WeatherApp.getCurrentTime());
				
			}
			
		}); // swingutilities
		

	} // end main

}
