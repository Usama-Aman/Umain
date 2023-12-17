# Assignment

Present the restaurants fetched from the API in a list.
When pressing a restaurant card opens up a detail view and show if the restaurant is opened or closed.
Fetch filters (id's can be found in restaurants response) and present them in a horizontal list, do not extract assets from figma,
fetch them from the provided URL. Pressing a filter item which should alter the restaurant list so it only shows restaurants with
that tag. Multiple filters can be selected at the same time.

*Language - Kotlin, XML
Architecture - MVVM  
Dependency Injection - dagger Hilt
minSdk = 24  
targetSdk = 34  
Gradle Version - 8.0  
Dependencies used - Androidx Activity, Fragment, Lifecycle ,DI (Dagger Hilt), Coil, Retrofit       
App Name is Usama_Aman*

## Explanation

- I used the clean architecture MVVM, Repository, Dagger Hilt for dependency injection and some basic components
- Retrofit client is used to hit the network call, added interceptors as well
- Restaurants are fetched in a list, after fetching restaurants I extracted the filters from each restaurants in a hashmap to
  avoid duplication and then fetched the data for each filter
- The hashmap is used to get the data of the filter based on id (key), though it can be done using list but i preferred hashmap 
  to be distinctive with the filter ids
- You can select multiple filters and the restaurant list will be altered accordingly
- Coil image loading library is used cox it works on coroutines threads replying on its built in cache mechanism, though we
  can write out own as well
- Diff Util is added in the recycler adapter to update the list efficiently
- Connection Live data is added to observe network change, it can also be observed used broadcast added the broadcaster logic as
  well
- On open app, if there is not internet and then you connects to internet, it will call the api automatically and load the data
- Added some extension functions
- I tried to match the UI as close as possible, didn't have the physical device , was testing it on emulator. Though it seems
  perfect. I took care of almost every detail
- Added three fonts, Helvetica, Poppins, Inter
- Strings are extracted in the strings folder
- Text styles are added in the theme file

##### Thanks And Regards

Looking forward to see you soon.   
Thanks for reviewing my code.  
Have a great day.  
Happy Coding!!!

