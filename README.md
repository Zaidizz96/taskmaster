# TaskMaster Android App

TaskMaster is an Android application designed to help you manage your tasks and stay organized. This app allows you to add new tasks, view all tasks, and provides a user-friendly interface for tracking your to-do list.

## Daily Change Log

- **Day 1 (October 16, 2023):**
  - Created the main page with a heading, a "My Tasks" image, and buttons for navigating to the "Add Tasks" and "All Tasks" pages.
  - Implemented the "Add a Task" page, allowing users to input task details (title and body) and display a "submitted!" label when the submit button is clicked.
  - Added the "All Tasks" page, which features a back button and provides the structure for future functionality.

- **Day 2 (October 18, 2023):**
  - Modify the home page to have three hardcoded button tasks.
  - Implemented a "Task Detail" page to display detailed information about a selected task.
  - Developed a "Settings" page for users to enter and save their usernames.
  - Enhanced the home page to display the user's tasks using their username from the "Settings" page.

- **Day 3 (October 23, 2023):**
  - Added the RecyclerView functionality to the homepage.
  - Created a Task class with title, body, and state properties.
  - Populated the RecyclerView with at least three hardcoded Task instances.
  - Enabled tapping on tasks in the RecyclerView to launch the detail page with the correct Task title displayed.

- **Day 4 (October 25, 2023):**
  - Set up Room database for local storage of tasks.
  - Modified the "Add a Task" form to save task data in the local database.
  - Refactored the homepage to display tasks from the local database.
  - Updated the detail page to show task description and status.
  
- **Day 5 (November 6, 2023):**
  - Added Espresso UI testing to enhance the app's quality and reliability.
  
- **Day 6 (November 8, 2023):**
  - Integrated AWS Amplify for cloud data storage to create a scalable backend for the app.
  - Used the Amplify CLI to create a Task resource in DynamoDB that replicates our existing Task schema.
  - Updated all references to the Task data to access data in DynamoDB using AWS Amplify.


## Screenshots

![Homepage](screenshots/Home_page_lab29.png)
![UserSettingPage](screenshots/User-Setting-page.png)
![TaskDetailPage](screenshots/Task_details_page_lab29.png)
![AddTaskPage](screenshots/Add_task_page_lab29.png)


## Setup

To get started with the TaskMaster Android app:

1. Create a new directory and repo named "taskmaster."

2. Use Android Studio to set up a new app following the instructions discussed in class.

3. Clone this repository and open the project in Android Studio.

4. Run the app on an Android emulator or a physical device to see the main page with navigation buttons.

## Features

## AWS Amplify Integration

We have integrated AWS Amplify to add cloud functionality to our TaskMaster app. Here's what we did on Day 6:

### Homepage

- We refactored the homepage's RecyclerView to display all Task entities from DynamoDB. Now, users can view their tasks from the cloud in the app.

### Task Detail Page:
- Display task title, description, and status.

### Settings Page:
- Allow users to enter and save their usernames.

### Add a Task

- We modified the "Add Task" form to save the data entered as a Task to DynamoDB. When users add a new task, it's now stored in the cloud.

### All Tasks

The "All Tasks" page currently features a back button and will be enhanced to display tasks from the local database, allowing users to view all their tasks at a glance.


## Espresso Testing
1. **Assert Important UI Elements**:
  - UI elements that are crucial for the app's functionality are verified to be displayed on the page, ensuring a seamless user experience.

2. **Task Detail Display Test**:
  - A test has been added to tap on a task, and the resulting activity is tested to ensure that it displays the name of the selected task correctly.

3. **Username Edit Test**:
  - The user's ability to edit their username has been tested, and it is verified that the updated username is correctly displayed on the homepage.

## Resources

- [Android Buttons Documentation](https://developer.android.com/guide/topics/ui/controls/button)
- [Android UI Events Documentation](https://developer.android.com/guide/topics/ui/ui-events)
- [RecyclerView Documentation](https://developer.android.com/guide/topics/ui/layout/recyclerview)

Start building your TaskMaster app and have fun developing your project!

## Author

Zaid Izziddine
