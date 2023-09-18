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
<summary> <h2> Photo </h2> </summary>

<img src="https://github.com/KovshefulCoder/WallCraft/assets/84292117/ed9bbf11-7c92-4e6c-8047-535ab82b71c1" width="200">
<img src="https://github.com/KovshefulCoder/WallCraft/assets/84292117/f4b23fd3-c253-4bdf-ba32-e63e44d2800b" width="200">
<img src="https://github.com/KovshefulCoder/WallCraft/assets/84292117/527cf299-3d60-4097-b438-383ca245f292" width="200">
<img src="https://github.com/KovshefulCoder/WallCraft/assets/84292117/9ee487b5-66ce-43c9-8eee-20a734a38430" width="200">
<img src="https://github.com/KovshefulCoder/WallCraft/assets/84292117/0954d6e2-8329-40c9-ac40-e7523ffae18d" width="200">
<img src="https://github.com/KovshefulCoder/WallCraft/assets/84292117/91710968-03de-4414-8e74-ed59825b8c0a" width="200">
<img src="https://github.com/KovshefulCoder/WallCraft/assets/84292117/e49a723f-a20d-4663-bc09-d7a140560f93" width="400">
<img src="https://github.com/KovshefulCoder/WallCraft/assets/84292117/2644fe26-3034-48f0-8b39-95fcc600ea55" width="400">

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


<details>
<summary> <h2> Improvement options </h2> </summary>

* Use DataStore instead of Shares Preferences because there is integration with Coroutines and more flexibility, but Shared Pref was chosen to meet the requirement of the task for a minimum of third-party libraries.
* Use Shares Preferences instead of Room to store links to images by Downloaded and Favorite keys as "url1,url2,...", but this is not flexible and any addition will require a lot of changes and most likely a complete rewrite of all logic. 
* Use Kotlin Result API instead of Custom Exceptions
* Use Snack bar instead of Toast
* Move some repeated compose code to shared composable functions, for example CircularProgressIndicator

</details>
