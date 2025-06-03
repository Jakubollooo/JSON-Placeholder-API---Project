📱 JSONPLACEHOLDER API – ANDROID APP
A MODERN ANDROID APPLICATION BUILT IN KOTLIN WITH JETPACK COMPOSE, FOLLOWING MVVM ARCHITECTURE AND THE REPOSITORY PATTERN. IT INTERACTS WITH THE JSONPLACEHOLDER API TO DISPLAY POSTS, USERS, TODOS, AND MORE.

✨ Key Features
✅ Main Screen (Posts)
Scrollable list of posts with author names.

Tap a post → View Post Details.

Tap a user name → View User Details.

❤️ Like posts — liked posts appear at the top.

📊 Like counter (liked / total posts).

![likeowanienowe](https://github.com/user-attachments/assets/b61125ee-ec22-4841-a5b4-6e4dd51ab058)


✅ Likes persist between sessions (DataStore).

✅ Post Details Screen
Full post content.

Displays author's ID.

Back navigation.

✅ User Details Screen

![osoba](https://github.com/user-attachments/assets/b1374e1f-9981-416d-b83a-56a878b91ff7)

Full user profile:

Name, username, email, phone, website.

Address (city, street, zip).

Company name & catchphrase.

List of todos with check status.

📍 User markers on Google Map (based on address geo).

✅ Profile Screen (NEW!)

![profilnowePOP](https://github.com/user-attachments/assets/3bac2386-6bfc-4cd7-93b7-c85ff6f71ddf)


👤 Create personal profile (first & last name).

📷 Add/change profile picture.

📍 Display your location on Google Map.

🧹 Option to clear/reset profile.

💾 All profile data saved locally (Preferences DataStore).

✅ Dark Mode Support

![ciemnymotywnowe](https://github.com/user-attachments/assets/1894a140-5e03-4d87-a4c8-d1c6e2d696f6)

🌓 Toggle dark/light theme manually.

🌙 Theme preference saved automatically (DataStore).

✅ Robust UX
Loading spinners during data fetch.

![bladnowe](https://github.com/user-attachments/assets/ab1b68be-da19-4e1e-b0c9-f4df67a4e19d)

User-friendly error messages and retry buttons on failure.

🧱 Tech Stack
Kotlin (100%)

Jetpack Compose – declarative UI framework

Retrofit – REST API client

Kotlin Coroutines + Flow – async data handling

Jetpack Navigation – for screen transitions

Google Maps Compose – interactive maps

MVVM Architecture – clean UI/viewmodel separation

Repository Pattern – abstracted data layer

Preferences DataStore – for local persistence
