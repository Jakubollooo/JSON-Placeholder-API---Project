# JSON-Placeholder-API---Project
📱 JSONPlaceholder API - Android App
This is an Android mobile application built with Kotlin and Jetpack Compose, following the MVVM architecture.
It interacts with the JSONPlaceholder API to display posts, users, and their tasks (todos).

✨ Key Features:

✅ Main Screen: Scrollable list of posts combined with user information.

- Displays post titles and author names.

- Tapping on a post navigates to Post Details.

- Tapping on a user name navigates to User Details.

![1](https://github.com/user-attachments/assets/06ac85f7-df1c-42a4-b8b3-941d2fc98462)


✅ Post Details Screen:

- Shows full post title and content.

- Displays post author's ID.

- Easy navigation back to the main screen.

![2](https://github.com/user-attachments/assets/92e00fcb-06e5-4a00-be7d-3328aaca58a6)


✅ User Details Screen:

- Full user information (name, username, email, phone, website, address, company).

- List of user's tasks (todos) with completion status.

- Scrollable tasks list with clear visual indicators.

![3](https://github.com/user-attachments/assets/53828942-3926-4c23-b275-0494e5d33e9c)


✅ Loading & Error States:

- Loading indicators while fetching data.

- User-friendly error messages with retry options on network failure.

![4](https://github.com/user-attachments/assets/e0be0d43-49c7-483d-a7b1-75475a640dd9)


🛠️ Tech Stack:
Kotlin (100%)

- Jetpack Compose (UI framework)

- Retrofit (network communication)

- Kotlin Coroutines + Flow (async data handling)

- Jetpack Compose Navigation (navigation between screens)

- MVVM Architecture (separation of concerns, reactive UI)

- Repository Pattern (clean data layer abstraction)
