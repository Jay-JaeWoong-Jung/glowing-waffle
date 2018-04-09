package controller;

public class ModelAndView {
	private String path;
	private boolean isRedirect=false;
	public ModelAndView(String path, boolean isRedirect) {
		super();
		this.path = path;
		this.isRedirect = isRedirect;
	}
	public ModelAndView(String path) {
		super();
		this.path = path;
	}
	public ModelAndView() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getPath() {
		return path;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	
		
}
