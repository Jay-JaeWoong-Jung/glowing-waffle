# roommate manamgent application


## Contributor Install Instruction

1.  Create your own dynamic web project from your IDE; project name is irrelevant.
2.  Then open terminal and change directory to the __inside__ of your dynamic web project
3.  Make sure your project build path has all of the following:
	* Appache Tomcat v9.0 (runtime library)
	* EAR Library
	* JRE System Library
	* Web app Library
4.  On Terminal, type the following commands:
	```	
	git init
	touch .gitignore
	```
5. Then open up the project folder and open .gitignore file using a text editor and copy and paste the following
	```
	.classpath
	.project
	.settings
	/build
	```
6. Go back to Terminal, and type the following commands:
	```
	git add .
	git commit -m "[yourname]'s eclipse project created"
	git remote add origin git@github.com:Jay-JaeWoong-Jung/roommate-mgmt.git
	git pull origin master
	```
	
## Notes

*we are intentionally ignoring project meta/config files for cross-OS compatibility of the repository; refer to .gitignore*
## frequent issues
* if push doesn't work, usually it means your local repo isn't up-to-date. I.E. someone else already made changes on remote repo that you don't have in your local git. You need to pull origin <branchname> before you can push your code. Remember to work inside your pair branch, __not on master__

markdown syntax can be found below
https://confluence.atlassian.com/bitbucketserver/markdown-syntax-guide-776639995.html



