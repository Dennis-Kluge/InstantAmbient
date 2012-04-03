package instantambient.ambientclient;

import android.app.Application;

public class App extends Application{
	
	private static App instance;
	
	public static App getInstance(){
		if(instance == null) {
			new App();
		}
		
		return instance;
	}
	
	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
	}
	
}
