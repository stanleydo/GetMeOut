### The Get Me Out app is a new Android messaging app that is geared towards safely messaging for help via a discrete SOS button. The application will send out a discrete SMS message when the user is faced with a troubling social interaction with another individual. The SMS message will create an opening for the user to leave the situation. ###

This application was developed as part of a Software Engineering project at CSULA.
Members include: Christopher Ortega, Andrew Vasquez, and Saul Rugama.

<img src="demo/Demo.gif" width="33%" height="33%">

#### Known Bugs ####
- Importing contacts only works for devices running API 25
- Duplicate entries are allowed
- Contacts can be imported with parenthesis in the phone number, but contacts can not be added with parenthesis.
- Location (as a google maps link) sometimes does not send
- Possible memory leaks due to improper usage of cursor and/or coroutines.
- Notification problems when app does not have Contact/Location/SMS permissions.

#### Considerations ####
- Fake Calls
- Reduced Location links (Using bit.ly or something)
- Navbar
- Delete All
