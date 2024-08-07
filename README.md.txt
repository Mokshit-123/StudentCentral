# CampusConnect

CampusConnect is an intuitive app designed for students to seamlessly access important academic information, including notices, attendance records, and personal profile details. The app aims to enhance the student experience by providing a centralized platform for accessing and managing their academic data.

## Features

### Login
- Secure login using the ID provided by your institute.
- Personalized access to your academic information.

### Notices
- View all notices provided by the institute.
- Stay updated with the latest announcements and updates.
- Access related PDFs directly from the app.

### Attendance
- Check your current attendance status.
- View attendance records for all subjects.
- Keep track of your attendance percentage to ensure compliance.

### Profile
- Access your profile information stored in official documents.
- View and manage your personal data as recorded by the institute.

## Getting Started

### Prerequisites
- Android device running version 5.0 (Lollipop) or higher.
- Institute-provided ID for login.
- Backend server to upload and manage notices, attendance, and profile data.

### Installation
1. **Set Up the Backend:**
   - Deploy a backend server to manage and upload notices, attendance, and profile data. You can use any backend technology (e.g., Node.js, Django, Flask).
   - Ensure the backend API endpoints match those expected by the app.

2. **Configure the App:**
   - Clone the repository to your local machine.
     ```bash
     git clone https://github.com/Mokshit-123/CampusConnect.git
     ```
   - Open the project in Android Studio.
   - Update the URLs in the network directory of the app to point to your backend server.
     - Locate the `ApiService` interface and replace the base URL with your backend server URL.
     ```kotlin
     // Example:
     const val BASE_URL = "https://your-backend-server.com/api/"
     ```
   - Build and run the app on your Android device.

### Usage
1. **Open the CampusConnect App:**
   - Launch the app on your Android device.
   - Log in using your institute-provided ID.
   - Navigate through the app to access notices, attendance records, and profile information.

2. **Manage Data via Backend:**
   - Use the backend server to upload and manage notices, attendance, and profile data.
   - Ensure the data is in the correct format as expected by the app.

## Contributing
We welcome contributions to enhance the app. To contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add new feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Open a pull request.


## Contact
For support or inquiries, contact:
- Developer: Mokshit
- Email: gargm0068@gmail.com
- GitHub: [Mokshit-123](https://github.com/Mokshit-123)

---

Thank you for using CampusConnect. We hope it makes your academic life easier and more organized!
