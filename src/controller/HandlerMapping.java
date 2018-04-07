package controller;


public class HandlerMapping {
	private static HandlerMapping hm = new HandlerMapping();

	private HandlerMapping() {
	}

	public static HandlerMapping getInstance() {
		return hm;
	}
	
	public Controller createController(String command) {
		Controller controller = null;
		if(command.equals("registerUser")){
			controller = new RegisterUserController();
		} else if (command.equals("login")) {
			controller = new LoginController();
		} else if (command.equals("creaetHouseHandle")) {
			controller = new createHouseHandle();
		} else if (command.equals("getHouseHandle")) {
			controller = new getHouserHandle();
		}
		
		System.out.println("inside handler "+controller+"controller created");
		return controller;
	}


	
}