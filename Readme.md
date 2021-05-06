Capstone project for Android Certification
Features:
- Firebase integration uses online database + login using google account.
- Full sync with server side ( you can add items while being logged, uninstall app, install again and login - stuff will be synced using firebase)
- Google places api to search  for the places.
- Async image loading using places api and login.
- Tablet/Phone specific layout.

In order to build you have to pass places api key as a gradle build param

** ./gradlew installDebug -Pplaces_key=AIza________ **