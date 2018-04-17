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
		} else if (command.equals("userLogin")) {
			controller = new LoginController();
			//Thread t = new Thread((LoginController)controller);
			//t.start();
		} else if (command.equals("creaetHouseHandle")) {
			controller = new RegisterHouseController();
			//Thread t = new Thread((RegisterHouseController)controller);
			//t.start();
		} else if (command.equals("joinHouseHandle")) {
			controller = new JoinHouserController();
			//Thread t = new Thread((JoinHouserController)controller);
			//t.start();
		}
		else if(command.equals("dummyHandle"))
		{
			controller = (Controller) new DummyController();
		}
		
		
		System.out.println("inside handler "+controller+"controller created");
		return controller;
	}


	
}