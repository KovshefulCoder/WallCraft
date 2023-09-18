# WallCraft 🖼️

WallCraft is a comprehensive wallpaper application that offers users a wide range of images, neatly organized into categories.

This application lets users sift through different image categories, select a preferred image, and conveniently set it as wallpaper on their lock screen, desktop, or both.

<details>
<summary> <h2> Key features </h2> </summary>
  
* Extensive collection of categorized wallpapers to choose from.
* Capability to set selected images as wallpaper for lock screen, desktop, or both.
* Download desired images directly to your device.
* Add your favorite images to a dedicated 'favorites' section for easy access.
* Dedicated screens to view and navigate through downloaded and favorited photos.
* Settings screen equipped with theme picker functionality.
* Adjustable settings to determine the number of collections and photos displayed in each collection.
* User-friendly interface for a seamless wallpaper selection and customization experience.

</details>

<details>
<summary> <h2> Improvement options </h2> </summary>

* Use DataStore instead of Shares Preferences because there is integration with Coroutines and more flexibility, but Shared Pref was chosen to meet the requirement of the task for a minimum of third-party libraries.
* Use Shares Preferences instead of Room to store links to images by Downloaded and Favorite keys as "url1,url2,...", but this is not flexible and any addition will require a lot of changes and most likely a complete rewrite of all logic. 
* Use Kotlin Result API instead of Custom Exceptions
* Use Snack bar instead of Toast
* Move some repeated compose code to shared composable functions, for example CircularProgressIndicator

</details>

<details>
<summary> <h2> Photo </h2> </summary>

</details>

<details>
<summary> <h2> Video demonstration </h2> </summary>

</details>

## Tech stack
* Kotlin
* Jetpack Compose
* Clean Architecture
* MVVM
* Compose navigation
* Hilt
* Retrofit
* Room
