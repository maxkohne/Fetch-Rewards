# Fetch Rewards Mobile Coding Challenge

## Overview
This project implements the Fetch Rewards Mobile Coding Challenge, which involves fetching and displaying a list of items from a given API. The app retrieves data, filters out invalid entries, and presents a grouped and sorted list in a user-friendly UI. The requirements are outlined in https://fetch-hiring.s3.amazonaws.com/mobile.html.

This is a multi-module project using Gradle convention plugins that share logic between modules. More can be learned from:
- https://docs.gradle.org/current/samples/sample_convention_plugins.html
- https://developer.squareup.com/blog/herding-elephants/
- https://developer.android.com/topic/modularization

**NOTE:**
*This project is overengineered on purpose (multi-modules, Room, etc) just to showcase my expertise in these areas. In a project this small, these technologies are very much overkill.*

## Features

#### Required
- Fetch data from the provided API.
- Filter out items with blank or null names.
- Group items by `listId`.
- Sort items first by `listId` and then by `name`.
- Display the processed list in a clean UI.

#### Extra
- Stores data in Room database
- Search by `name` in list
- Pull to refresh
- Robust error handling with Snackbars based upon different error scenarios (http errors, storage error, etc.)

## Tech Stack
- **Kotlin** for development.
- **Jetpack Compose** for UI.
- **Hilt** for Dependency Injection.
- **Retrofit** for API calls.
- **Room** for local caching.
- **Navigation** for screen routing.
- **Multi-Module** for separation of concerns
- **Coroutines & Flow** for asynchronous operations.
- **JUnit & Mockito** for unit testing.

## Screenshots

#### List
<img src="https://github.com/user-attachments/assets/37e0f3d9-ff76-46b9-9a95-fe2ea2978c1c" width="250">

<img src="https://github.com/user-attachments/assets/f74cba50-2265-4386-951d-f2e116e838bc" width="250">

#### Details

<img src="https://github.com/user-attachments/assets/ccbbc291-0786-4bed-92d2-545099a10c75" width="250">

#### Some Errors
<img src="https://github.com/user-attachments/assets/d9d0b017-bfea-4eb4-bc0d-8fe8c38a017c" width="250">

<img src="https://github.com/user-attachments/assets/4b55a2eb-cab2-4fb9-872a-b9f31bb0a233" width="250">


## Testing
Many of the classes are united tested including ViewModels, Repositories, and others.

To run unit tests, execute:
```sh
./gradlew testDebugUnitTest
```

## Future Improvements
- Implement pagination for large datasets.
- Improve UI/UX with animations and Material Design elements.
