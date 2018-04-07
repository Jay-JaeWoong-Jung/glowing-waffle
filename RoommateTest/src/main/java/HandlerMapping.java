public class HandlerMapping {
    private static HandlerMapping hm = new HandlerMapping();

    private HandlerMapping() {
    }

    public static HandlerMapping getInstance() {
        return hm;
    }

    public Controller createController(String command) {
        Controller controller = null;
        switch(command){
            case "signup":
                //controller = new SignUpController();
                break;
            case "login":
                //controller = new LogInController();
                break;
            case "homepage":
                controller = new HomePageController();
                break;
        }

        System.out.println("inside handler "+controller+"controller created");
        return controller;
    }



}
