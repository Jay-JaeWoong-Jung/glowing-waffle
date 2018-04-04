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
		if(command.equals("singup")){
		//	controller = new SingUpController();
		} else if (command.equals("signin")) {
		//	controller = new SingInController();
		}
		
		System.out.println("inside handler "+controller+"controller created");
		return controller;
	}


	
}