################ ALL THE FRAMEWORK PROPERTIES  ################



# Browser Name for driver instance
# 1. firefox 	: Firefox Browser
# 2. chrome		: Chrome Browser
# 3. ie			: Internet Explorer
# Note : chrome and ie need driverPath set with driver executables
org.seltest.driver.Browser

# Driver Executable Path
# Path Relative to ${user.home}
org.seltest.driver.DriverPath

# Enable Log For All the WebDriver Events
# IMP *** Should only be Set to false for testing affects wait functionality
org.seltest.driver.EventFiring= true

# Base URL for the Application
org.seltest.app.BaseUrl

#Browser Opens In FullScreen Mode
org.seltest.app.FullScreen

# ******** Parallel Execution is Dependent on this property *************
#Number Of Browser Instance 
#	1. suite 		: All <suite> Will be executed on same Browser Instance
#	2. tests		: All <test> Will be executed in same Browser Instance

# Capture Screen Shot 
#	Note By Default will try to capture screen shot for exception 
#	1. all 			: Captures screen shot  Steps , Assertions , Result
#	2. result		: Captures screen shot Assertion , Result
#	3. assertion	: Captures screen shot for Assertion 


# Framework Wait Type 
#	1. explicit : Wait for a WebElement and has a max timeout out 
#	2. implicit : Wait for a particular time Not Dependent on WebElement
# Note : explicit wait is 33% faster (Avg)

#Implicit Wait After Click on WebElement 
#	Time in Seconds 
# **IMP** : Increasing this value changes the execution time drastically . 
# Note if value below 2 will be taken as 2 sec as test cases fail 

# Explicit Wait max timeout 
#	Max Time in Seconds 
