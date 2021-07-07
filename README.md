<p align="center">
  <a href="https://github.com/Team-Fourth-Dimension/FFCSeZ">
    <img align="left" src="images/icon.png" alt="App Icon" width="250" height="250">
  </a>
  
  <h1 align="center">FFCSeZ</h3>
  <h3 align="center">An app to ease your FFCS</h3>
  
  <p align="center">
  <a href="https://github.com/Team-Fourth-Dimension/FFCSeZ"><strong>Explore the docs »</strong></a>
    <br>
    <br>
  ·
  <a href="https://github.com/Team-Fourth-Dimension/FFCSeZ/issues">Report a bug</a>
  ·
  <a href="https://github.com/Team-Fourth-Dimension/FFCSeZ/issues">Request a feature</a>
  ·
  </p>
</p>

<br>

<p align="center">

[![Contributors](https://img.shields.io/github/contributors/github_username/repo.svg?style=for-the-badge)](https://github.com/Team-Fourth-Dimension/FFCSeZ/graphs/contributors)
[![Forks](https://img.shields.io/github/forks/github_username/repo.svg?style=for-the-badge)](https://github.com/Team-Fourth-Dimension/FFCSeZ/network/members)
[![Stargazers](https://img.shields.io/github/stars/github_username/repo.svg?style=for-the-badge)](https://github.com/Team-Fourth-Dimension/FFCSeZ/stargazers)
[![Issues](https://img.shields.io/github/issues/github_username/repo.svg?style=for-the-badge)](https://github.com/Team-Fourth-Dimension/FFCSeZ/issues)
[![MIT License](https://img.shields.io/github/license/github_username/repo.svg?style=for-the-badge)](https://github.com/Team-Fourth-Dimension/FFCSeZ/blob/master/LICENSE)

</p>

<details open="open">
  <summary><h3 style="display: inline-block">Table of Contents</h3></summary>
  <ol>
    <li><a href="#-about-the-project">About The Project</a>
      <ul>
        <li><a href="#-features">Features</a></li>
        <li><a href="#-tech-stack">Tech Stack</a></li>
      </ul>
    </li>
    <li>
      <a href="#-getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#-roadmap">Roadmap</a></li>
    <li><a href="#-contributing">Contributing</a></li>
    <li><a href="#-license">License</a></li>
    <li><a href="#-reach-out">Reach out</a></li>
  </ol>
</details>


## <img src="https://openclipart.org/download/307315/1538154643.svg" width="32" height="32"> About the project
FFCSeZ is an app to facilitate the smooth and hassle-free preparation of timetables during FFCS and keeping a track of daily classes and related to-dos, with a host a of new and student-centric features. The app can be used by any student studying in VIT right from the first years to the final years.

## <img src="https://noveltypharma.eu/wp-content/uploads/2020/10/icon_novel_ingredients.png" width="32" height="32"> Features
- Updated with the latest faculty information for FFCS.
- Minimalistic and smooth UI for creating timetables on mobile.
- Easy add/drop courses with credit management.
- Filters to help you narrow down to your preferred slots.
- Support for multiple mock time tables.
- Time-table sharing.
- To-do listings for all classes with timely reminders, if set.
- Sleep alarms to remind a few moments before a class starts based on the current timetable.

## <img src="https://techstackapps.com/media/2019/11/TechStackApps-logo-icon.png" width="32" height="32"> Tech Stack
<img src="https://2.bp.blogspot.com/-tzm1twY_ENM/XlCRuI0ZkRI/AAAAAAAAOso/BmNOUANXWxwc5vwslNw3WpjrDlgs9PuwQCLcBGAsYHQ/s1600/pasted%2Bimage%2B0.png" width="40" height="40" alt="Android Studio"> <img src="https://symbols-electrical.getvecta.com/stencil_261/26_mongodb-realm.8095f50267.png" width="36" height="36" alt="Realm"> <img src="https://img.icons8.com/color/452/mongodb.png" width="40" height="40" alt="MongoDB Atlas"> <img src="https://i.pinimg.com/originals/a5/58/b4/a558b426cb8973523f37bbed94cf0f09.png" width="40" height="40" alt="Figma"> <img src="https://img.icons8.com/color/452/firebase.png" width="40" height="40" alt="Firebase">

## <img src="https://cdn.iconscout.com/icon/free/png-512/laptop-user-1-1179329.png" width="32" height="32"> Getting Started
To get a local copy up and running follow these simple steps.
### Prerequisites
In order to get a copy of the project you will require you to have Android Studio (v4.0 and above) installed. If you don't have it, you can download Android Studio from the [official website](https://developer.android.com/studio).
### Installation
Open Android Studio, select `Import from Version Control` and paste the following link to clone the repository:
``` 
https://github.com/Team-Fourth-Dimension/FFCSeZ.git 
```
Run a gradle build if there is some error in syncing the dependencies. Generate an APK by going to `Build > Build APK` and install it on your Android device or run the app from within Android Studio either by using an emulator or by USB debugging.

## <img src="https://image.flaticon.com/icons/png/128/712/712058.png" width="32" height="32"> Roadmap
The main features of the app are broken down into a technical task timeline.

- <strong><em> Task 1: Project Setup </em></strong>
  - [x] Add the required dependencies for library support.
  - [ ] Create a Login UI for GoogleAuth.
  - [x] Setup database services.

- <strong><em> Task2: Setting up the FFCS interface and database </em></strong>
  - [x] Build the UI for the FFCS selection screen.
  - [x] Adding query methods from the database for the selection. 
  - [x] Add chips and set up a filter system.
  - [x] Build the UI for the slot display screen.
  - [x] Build UI for Full TimeTable Screen. 

- <strong><em> Task 3: Setting the Timetables </em></strong>
  - [x] Build the UI for the timetable display.
  - [x] Setup app navigation.
  - [x] Code for the database storage of timetables.
  - [x] Wrap up the code of Task 2 and the code of Task 3.
  - [x] Complete debug till Task 3. 

- <strong><em> Task 4: The Navigation Pane </em></strong>
  - [x] Setup the functionality to add and display the existing timetables of the user.
  - [ ] Adding the import timetable feature.
  - [x] Add notifications to remind when new FFCS data has arrived
  - [x] UI cleanup.

- <strong><em> Task 5: Notes (To-Dos) and Reminders </em></strong>
  - [ ] Building the UI for the to-dos activity for each class.
  - [ ] Setting up a broadcast receiver for the notifications.
  - [ ] Code for Task 5.

- <strong><em> Task 6: Alarms </em></strong>
  - [ ] UI for the alarms setup.
  - [ ] Implementation of alarms using a content provider.
  - [ ] Driver code.
  - [ ] Complete debug till Task 6.

- <strong><em> Task 7: Cleanup </em></strong>
  - [ ] Cleanup or improvise the UI/UX.
  - [ ] Refactor code and code optimizations.
  - [ ] Add guide at first-time startup.

- <strong><em> Task 8: Widgets and Additional features </em></strong>
  - [ ] Implement a dark theme and its toggle.
  - [ ] Build a widget of the current timetable.
  - [ ] Build a widget of the to-dos of the current timetable.

## <img src="https://hpe-developer-portal.s3.amazonaws.com/uploads/media/2020/3/git-icon-1788c-1590702885345.png" width=32 height=32> Contributing
Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project. [(Refer the get started instructions)](#-getting-started)
2. Create your Feature Branch. (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes. (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch. (`git push origin feature/AmazingFeature`)
5. Open a Pull Request.

## <img src="https://petpat.lv/wp-content/uploads/2018/12/license-icon-27934542-2.png" width=32 height=32> License
Distributed under the **MIT License**. See [`LICENSE`](https://github.com/Team-Fourth-Dimension/FFCSeZ/blob/master/LICENSE) for more information.

## <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Google_Contacts_icon.svg/1024px-Google_Contacts_icon.svg.png" width=32 height=32> Reach out
##### Arjun Sivaraman via [Email](mailto:arjun140702@gmail.com) · [Github](https://github.com/1407arjun) · [Linkedin](https://linkedin.com/in/arjun-sivaraman-1407)
##### Prasoon Soni via [Email](mailto:prasoonsoni03@gmail.com) · [Github](https://github.com/prasoonsoni) · [Linkedin](https://www.linkedin.com/in/prasoonsoni)

<br><h4 align="center"> With :heart: by <a href="https://ietvit.tech/" target="_blank">Team Fourth Dimension, IET-VIT</a></h4>


