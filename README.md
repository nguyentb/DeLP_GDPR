# DeLP_GDPR
- Paper Title: A Defeasible Logic-based Modelling Solution for GDPR Compliance
- Authors: Azam Naila, Michala AnnaLito, Ansari Shuja*, Nguyen Truong
- Affiliation: School of Computing Science, University of Glasgow, Glasgow, United Kingdom
	* James Watt School of Engineering, University of Glasgow, Glasgow, United Kingdom

- All the source code for the platform demonstration in this paper can be found in the following link (Github):
https://github.com/nguyentb/DeLP_GDPR

Detail of the source code: All the source code consists of the following parts:

1. Defeasible Logic Programming: DeLP_GDPR/src/DeLP_GDPR/delp/

	- Demonstration for the Telehelp Service can be found in the sub-folder: ./examples
	- The reasoning mechanism can be found in the sub-folder: ./reasoner
	- Other classes supporting the Defeasible Logic Programing can be found in the sub-folders:
		+ Parser: ./parse
		+ Semantics: ./semantics
		+ Syntax: ./syntax
	
2. Defeasible Logic Programming with Argumentation is developed based on many other packages about logics and reasoning:
These packages are inherited from TweetyProject. Some of them can be found under the folders: DeLP_GDPR/src/DeLP_GDPR/
	- DUNG
	- Graphs
	- Logics
	- Math and Maths

4. Defeasible Knowledge bases are developed for different scenarios, including the Telehealth service use-case.
These knowledge bases can be found under the folder: DeLP_GDPR/src/resources
	- The sub-folder ./NonConsent_testcases are scenarios for the system performance evaluation

5. System Performance Evaluation Results can be found in the file: DeLP_GDPR/results.txt
This results file includes information about execution time for different types of queries (YES/NO/UNDECIDED) in different knowledge bases (found in DeLP_GDPR/src/resources)

If you need more information, please contact me (Nguyen Truong) at ngbitr@gmail.com or Nguyen.Truong@glasgow.ac.uk
