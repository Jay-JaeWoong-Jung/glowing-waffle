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
		if(command.equals("registerMember")){
			controller = new RegisterMemberController();
		} else if (command.equals("login")) {
			controller = new LoginController();
		}
		
		System.out.println("inside handler "+controller+"controller created");
		return controller;
	}


	
}