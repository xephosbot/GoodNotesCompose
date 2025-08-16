# Good Notes Compose

Good Notes Compose is an Android application developed using Kotlin and Jetpack Compose. It features a clean architecture to ensure scalability and maintainability.

![Frame 9](https://github.com/xephosbot/GoodNotesCompose/assets/75476237/3adca9ed-25a1-4e2d-a6cb-b777a5134c7c)

Preview the application in action: [YouTube Video](https://youtube.com/shorts/Tz0WPUggpVE)

## Features

- **Note Display and Organization**: View notes categorized into folders for efficient organization.
- **Note Editor**: Edit notes with capabilities to modify the title, content, and color.
- **Shared Element Transitions**: Smooth transitions between UI components for an enhanced user experience.

## Architecture

The project adheres to clean architecture principles, dividing the codebase into distinct modules:

- **app**: Contains the user interface and application logic.
- **core**: Includes core utilities and extensions used across the application.
- **core-ui**: Contains design system components and style.
- **data**: Manages data sources, repositories, and data models.
- **domain**: Defines business logic and use cases.

## Custom Components

- **Custom Top App Bar**: [View Implementation](https://github.com/xephosbot/GoodNotesCompose/blob/master/core/ui/src/main/java/com/xbot/ui/component/AppBar.kt)
- **Selectable List Items**: State management for selecting items within a list. [View Implementation](https://github.com/xephosbot/GoodNotesCompose/blob/master/core/ui/src/main/java/com/xbot/ui/component/SelectableItemsState.kt)
- **Drag and Drop Functionality**: Implemented drag-and-drop features for interactive folders order management. [View Implementation](https://github.com/xephosbot/GoodNotesCompose/blob/master/core/ui/src/main/java/com/xbot/ui/component/DragDropState.kt)

## Prerequisites

- **Android Studio 2025.1.2** or newer.
- **JDK 21** or newer.
- **Gradle 8.13** or newer.

## License

This project is distributed under the MIT license. See [LICENSE.txt](LICENSE.txt) for details.
