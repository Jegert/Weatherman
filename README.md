# Weatherman
Web app that compares the predictions of different weather sites using 4 different weather API-s.  
Front end is made with Angular and back end is made with Spring boot.  
This web application was made as an test task for CGI summer internship.

###Any assumptions you made that were not clearly written in the task description  
At first it was a bit confusing, where would you need to use back end for the application, because all the API-s can be called through the front end. After doing the task it was clear that all the API-s differ so much and it is so much cleaner to do the dirty work of making all the API-s look the same in the back end.
###How to deploy/run your solution (Someone who knows nothing about the solution should be able to deploy it using your instructions)  
To run the Spring Boot server, run the **WeathermanSpringApplication.java** file found in **weatherman-spring/src/main/java/com/example/weathermanspring/**  
For the Angular front end using IntelliJ Webstorm run the Angular CLI Server while in the folder **weatherman-angular**  
###How you solved the tasks, and how much time did different parts take
I finished all the tasks, as Angular was not very familiar to me, it took me about 2 days.  

The front end design and most of the code was finished on the first day and I finished the backend and made it work with the front end on the second day.
###How you decided what to implement and what to skip  
In the weatherman form I decided not to use the date range field, because all the API-s differ so much and some of them did not support historical or long term prediction weather information, it shows only the current time temperature.  

Using OpenStreetMap you can click to choose a location or type the coordinates in the fields. When you type the coordinates in it automatically shows the location you typed out.  

You can choose between 4 Weather API-s to compare against, AccuWeather checkbox is disabled, because it acts as an "true weather" information in this application.

After seeing the comparison you can save the forecast and it will show it every time you open the application and updates on start of every hour.  

You can sort the weather table rows from ascending to descending.
###What were the biggest challenges and how did you overcome them  
The biggest challenge for me was how to make all the different API-s into the same object, so it could be used easily in the front end. I made an helper class in Spring Boot that all the API calls into the same WeatherForecast Class.  
Second challenge for me was how to use the openstreetmap and make the location change with a click, I looked at other code examples, how it is done and modified it for my use.
###If there was a problem you did not have time to overcome, describe how you think it could be solved with more time  
I could not overcome the issue with the daterange field, many of the weather API-s do not support historical weather information and only show this week's forecast.  
It could have been solved using different API-s and making a minimum and maximum range for the field.  

###What did you learn from implementing the project  
I learned how to use Angular, as I have never used it before.  
How to tie in front end and back end using Axios.  
How to do scheduled tasks in Spring Boot.  
How to use OpenStreetMaps.