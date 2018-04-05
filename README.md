# roommate manamgent application


## Contributor Install Instruction

1.  Create your own dynamic web project on Eclipse and name it as you wish
    1.  Create a temporary folder anywhere outside of the eclipse web project folder.
    2.  Copy the following eclipse project metadata files and paste them inside the temporary folder:
	*  .classpath
	*  .project
	*  .settings
	*  /build
    3. Delete all the files in your eclipse web project, so the project folder is empty.
2.  Open the terminal, change the directory to your dynamic web project, and run the following commands:
    ```	
    git init
    git remote add origin git@github.com:Jay-JaeWoong-Jung/roommate-mgmt.git
    git pull origin master
    ```
3.  double check your eclipse project is now in sync with the remote repository.
4.  Then copy over all the eclipse metadata files back to your eclipse project. (*the remote repository contains .gitignore files that ignore all the metadata, so git won't track them*)
4.  Make sure your project build path has all of the following:
	* Appache Tomcat v9.0 (runtime library)
	* EAR Library
	* JRE System Library
	* Web app Library

	
## Notes

*we are intentionally ignoring project meta/config files for cross-OS compatibility of the repository; refer to .gitignore*
## frequent issues
* if push doesn't work, usually it means your local repo isn't up-to-date. I.E. someone else already made changes on remote repo that you don't have in your local git. You need to pull origin <branchname> before you can push your code. Remember to work inside your pair branch, __not on master__

markdown syntax can be found below
https://confluence.atlassian.com/bitbucketserver/markdown-syntax-guide-776639995.html



