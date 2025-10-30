# 📱 Messaging App

Real-time messaging application for Android built as part of the Mobile Application Design & Development portfolio assignment. It showcases chats, calls, stories/status, profile management, and a polished UI using Android best practices. ✨

---

## 🚀 Features

- 🗨️ Chats: one-to-one conversations, message bubbles (sent/received), unread badge
- 🧷 Attachments: documents, images, camera capture, and quick add options
- 😀 Emoji support: emoji picker and reactions (icons present in resources)
- 🔍 Search: quickly find chats and messages
- ☎️ Calls: call log, call details, in-call screen (mute/speaker/end)
- 🎥 Video call: dedicated UI for video calling
- 📸 Camera: in-app capture for quick sharing
- 📝 Status/Stories: create and view status updates
- 🧑 Profile: view/edit profile and user info
- ⚙️ Settings: app preferences and account options
- 🔐 Auth: login and sign-up screens
- 📰 Posts: simple social-style post list and create flow

> Note: This project template includes full UI screens and resources. Back-end/real-time infra can be added as needed.

---

## 🧱 Tech Stack

- 🟦 Android (Native)
- ☕ Java for app source code (`app/src/main/java`)
- 🧩 Gradle Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`)
- 🎨 Material Design components and custom drawables

---

## 📂 Project Structure

```
Messaging_app/
├─ app/
│  ├─ src/
│  │  ├─ main/
│  │  │  ├─ AndroidManifest.xml
│  │  │  ├─ java/com/example/...               # Activities, adapters, models (Java)
│  │  │  └─ res/
│  │  │     ├─ layout/                         # activity_*.xml, item_*.xml
│  │  │     ├─ drawable/                       # icons, shapes, backgrounds
│  │  │     ├─ menu/                           # toolbar and overflow menus
│  │  │     ├─ values/, values-night/          # themes, colors, strings
│  │  │     └─ mipmap-*/                       # app launcher icons
│  │  ├─ androidTest/java/...                  # UI/instrumented tests
│  │  └─ test/java/...                         # Unit tests
│  ├─ build.gradle.kts                         # Module Gradle config (Kotlin DSL)
│  └─ proguard-rules.pro
├─ build.gradle.kts                            # Project Gradle config (Kotlin DSL)
├─ gradle/                                     # Gradle wrapper + versions catalog
├─ gradle.properties
├─ gradlew / gradlew.bat                        # Wrapper scripts
├─ local.properties                             # Android SDK path (generated)
└─ settings.gradle.kts
```

---

## 🛠️ Prerequisites

- 🧰 Android Studio (latest stable, e.g., Iguana or newer)
- ☕ JDK 17 (recommended for latest Android Gradle Plugin)
- 📦 Android SDK + required build tools (handled by Android Studio)

---

## ▶️ Getting Started

1. Clone or copy the project into your workspace
2. Open the root folder in Android Studio
3. Wait for Gradle sync to complete
4. Select a device/emulator and press Run ▶️

Alternatively (command line):

```bash
./gradlew assembleDebug          # macOS/Linux
gradlew.bat assembleDebug        # Windows
```

Install the generated APK from `app/build/outputs/apk/debug/` if needed.

---

## 🔧 Build & Run

- Debug build: `assembleDebug` or run from Android Studio
- Release build: `assembleRelease` (configure signing first)
- Instrumented tests: `connectedDebugAndroidTest`
- Unit tests: `testDebugUnitTest`

```bash
./gradlew clean assembleDebug
./gradlew connectedDebugAndroidTest
```

---

## 🔑 App Permissions

Depending on enabled features, the app may request:

- 🌐 INTERNET
- 🎤 RECORD_AUDIO
- 📷 CAMERA
- 🗂️ READ/WRITE EXTERNAL STORAGE (or scoped storage alternatives)
- 📞 CALL_PHONE / READ_CALL_LOG (for call features, if implemented)

Check `AndroidManifest.xml` for the exact set used.

---

## 🧪 Testing

- Unit tests live under `app/src/test/java`
- Instrumented UI tests under `app/src/androidTest/java`

Run from Android Studio test gutter icons or via Gradle tasks above.

---

## 🖼️ Screenshots

Add screenshots to a `docs/screenshots/` folder and reference them here:

| Chats | Conversation | Calls |
|---|---|---|
| <img src="docs/screenshots/chats.png" width="240"/> | <img src="docs/screenshots/conversation.png" width="240"/> | <img src="docs/screenshots/calls.png" width="240"/> |

| Status | Camera | Video Call |
|---|---|---|
| <img src="docs/screenshots/status.png" width="240"/> | <img src="docs/screenshots/camera.png" width="240"/> | <img src="docs/screenshots/video_call.png" width="240"/> |

---

## 🧭 Roadmap

- ✅ Core UI screens and navigation
- 🔄 Link to real-time backend (e.g., Firebase or custom API)
- 🔔 Push notifications (FCM)
- 🛡️ End-to-end encryption (design or integration)
- 🌗 Dark mode refinements
- 🌍 Localization (strings and RTL support)

---

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repo
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m "Add amazing feature"`
4. Push branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

---

## 📄 License

This project is currently unlicensed and intended for academic/portfolio use. If you plan to publish or open-source it, consider adding a license (e.g., MIT/Apache-2.0) and update this section. 🔒

---

## 🙏 Acknowledgements

- Android Developers documentation
- Material Design guidelines
- Open-source icons and resources used in `res/drawable*`

---

## ❓ FAQ

- "Why Java and Kotlin DSL together?" → The app sources are Java, while Gradle uses Kotlin DSL for modern, type-safe build configuration.
- "Why are some features placeholders?" → The UI is complete; connect your preferred backend for full functionality.

Happy building! 🚀
