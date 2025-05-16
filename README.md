# JSON-Placeholder-API---Project
📱 JSONPlaceholder API - Android App
This is an Android mobile application built with Kotlin and Jetpack Compose, following the MVVM architecture.
It interacts with the JSONPlaceholder API to display posts, users, and their tasks (todos).

✨ Key Features:

✅ Main Screen: Scrollable list of posts combined with user information.

- Displays post titles and author names.

- Tapping on a post navigates to Post Details.

- Tapping on a user name navigates to User Details.

![1](https://github.com/user-attachments/assets/7abe31d4-10de-40c1-bf40-dad4b785d30e)


✅ Post Details Screen:

- Shows full post title and content.

- Displays post author's ID.

- Easy navigation back to the main screen.

  ![2](https://github.com/user-attachments/assets/d61b236a-c5e7-4961-97b6-87e438e81247)


✅ User Details Screen:

- Full user information (name, username, email, phone, website, address, company).

- List of user's tasks (todos) with completion status.

- Scrollable tasks list with clear visual indicators.

  ![3](https://github.com/user-attachments/assets/9b7dfc4d-7ebc-4ee6-89a7-01a32f1011f2)


✅ Loading & Error States:

- Loading indicators while fetching data.

- User-friendly error messages with retry options on network failure.

  ![4](https://github.com/user-attachments/assets/61d653a2-4215-4827-a001-65339f744bb7)


🛠️ Tech Stack:
Kotlin (100%)

- Jetpack Compose (UI framework)

- Retrofit (network communication)

- Kotlin Coroutines + Flow (async data handling)

- Jetpack Compose Navigation (navigation between screens)

- MVVM Architecture (separation of concerns, reactive UI)

- Repository Pattern (clean data layer abstraction)
