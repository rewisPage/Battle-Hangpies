# Battle Hangpies: An Integrated Gaming and Marketplace System

**Battle Hangpies** is a Java-based mini-capstone project developed as an integrated text-console business management system combined with a graphical user interface (GUI) word-guessing battle game. The system is designed to showcase core competencies in Object-Oriented Programming (OOP) concepts, fundamental Data Structures and Algorithms (DSA), and flat text-file data persistence.

![Image1](https://github.com/user-attachments/assets/e4a8cca7-2d4f-4eba-8d5e-406f6d98fb82)

![Image2](https://github.com/user-attachments/assets/28db2a6b-330e-445c-a056-a025b87c07dc)

![Image4](https://github.com/user-attachments/assets/ed2f25e2-d747-4514-882d-2d22e05396b5)

![Image5](https://github.com/user-attachments/assets/bba96871-05f0-41fc-8fbe-d91295f72e33)

---

## 🎮 System Overview & Ecosystem
The core ecosystem centers on a fictional race of custom pet avatars called **Hangpies** (a blend of "Hangman" word-mechanics and companion "pets").

### ⚔️ Game Mechanics (AWT GUI Window)
* **PvE Word-Guessing Combat:** Players enter turn-based combat against automated Mob or Boss opponents.
* **Dynamic Damage Rules:** Submitting a correct letter guess inflicts health bar damage on the opponent. Conversely, every incorrect letter guess inflicts health damage on the player's own active Hangpie avatar.
* **Combat Conditions:** A match safely concludes when either the player's or the opponent's health bar drops to zero.
* **Level Progression Engine:** Defeating a Mob battle rewards the player with gold and increases their immediate game progress level. Defeating a major Boss battle rewards the player with premium gold yields, levels up the underlying Hangpie pet, advances the global World Level, and resets local progress levels back to 1.
* **Access Control:** Players must acquire and own at least one companion Hangpie from the marketplace before they are permitted to launch a battle game.

### 💰 Niche Business & Currency Model
* **Closed Economy Token:** The marketplace exclusively transacts using a single system currency called **Gold**.
* **Code Card Redemption:** In accordance with its niche business design, the system restricts direct wallet recharges. Users instead simulate buying physical or third-party code cards from distributed stores and must manually input those code numbers into the app to redeem gold balances.

---

## 🚀 System Features

### 👤 User Module
* **Interactive Dashboard:** Features a multi-option navigational pathway guiding players to the Marketplace, Inventory, Wallet, Purchase Logs, Profile customizer, and active gameplay windows.
* **Marketplace:** Provides terminal functions to browse available Hangpies, check descriptions, complete item acquisitions via Gold, and generate checkout receipts.
* **Inventory Tracker:** Allows players to evaluate owned pets, personalize companion names, or sell them back to the marketplace for a dynamic price determined by the asset's level.
* **Wallet & Purchase Ledger:** Tracks active currency counts, registers voucher card activations, and reviews complete redemption and purchase logs.
* **Profile Engine:** Enables users to access, review, and update personal details including full name, physical address, and contact links.
* **Activity Log:** Maintained inside the console to log runtime session operations dynamically.

### ⚙️ Admin Module
* **Platform Analytics:** Displays high-level administrative metrics including total registered user accounts, active marketplace items, and total cumulative gold value redeemed across the system.
* **Product Management:** Complete CRUD capabilities allowing managers to safely introduce, read, alter, or remove listed Hangpie models with designated names, descriptions, and market values.
* **User Management:** Oversees profile states, granting permissions to search, update, or remove customer accounts.
* **Code Management:** Automatically outputs randomized voucher cards organized by targeted gold weights, allowing full creation and deletion tasks.

---

## 🛠️ Technical Architecture

### 🧩 Object-Oriented Programming (OOP) Realization
* **Inheritance & Abstraction:** A foundational `Character` abstract parent class defines standardized states (name, max health, current health, level). This structure is cleanly inherited by specific concrete subclasses like `Hangpie` and system `Enemy` objects to maximize code reusability.
* **Encapsulation:** All core fields across data entities (such as `User`, `Hangpie`, and code records) are isolated via `private` access modifiers and managed strictly through normalized `public` getter and setter methods.
* **Polymorphism:** Method overriding is utilized across entities (such as custom `toString()` data string formatting and distinct damage mechanics) to enable versatile runtime behavior.

### 📊 Data Structures & Algorithms (DSA) Implementation
* **Dynamic Collections (ArrayList):** Integrated directly into user instances to handle the player's mutable, dynamic companion lists (Inventory management).
* **Key-Value Mapping (HashMap):** Used heavily inside data managers (such as the `UserManager`) to store system profiles using unique keys (`String username`) paired with complete object models.
* **Optimized Search Algorithm:** Rather than using basic sequential loop structures with high processing costs ($O(n)$ time complexity), the login verification phase utilizes a constant-time ($O(1)$) hash-map lookup algorithm to authenticate user records efficiently.

### 💾 Data Storage & Error Resistance
* **Text File Persistence:** Employs flat text files as a light database engine, using safe string splitting patterns (Pipe-delimited `|` boundaries) to accurately write and parse profiles, wallet balances, progress levels, and item parameters across system restarts.
* **Fault-Tolerant Exception Blocks:** File read/write processing operations are enclosed within secure `try-with-resources` structures to isolate `IOException` and `NumberFormatException` failures, preventing application crashes.

---

## 🗺️ Development Roadmap: Step-by-Step Guide

This project was built iteratively to ensure OOP principles and DSA requirements were thoroughly integrated. Here is the step-by-step roadmap used to develop the system:

### Phase 1: Project Setup & Core OOP Models
* **Objective:** Establish the foundational data models and implement Encapsulation and Inheritance.
* **Execution:**
  * Created the abstract parent `Character` class to hold shared states (health, level, name).
  * Created the `Hangpie` subclass (extending `Character`) to add marketplace properties (price, ID, description).
  * Created the `User` class to encapsulate authentication credentials, personal details, gold balance, and an `ArrayList` representing their inventory.

### Phase 2: Data Persistence & Managers
* **Objective:** Implement flat-file database storage and fulfill DSA searching requirements.
* **Execution:**
  * Designed pipe-delimited (`|`) `.txt` files for users, products, and codes.
  * Built the `UserManager` utilizing a `HashMap<String, User>` to store active profiles.
  * Implemented an $O(1)$ search algorithm for the authentication/login system.
  * Added `try-with-resources` blocks to safely parse external text files and catch `IOExceptions`.

### Phase 3: Admin Console Features
* **Objective:** Build the management terminal and implement CRUD operations.
* **Execution:**
  * Developed the core `while`-loop based console menu.
  * Built the Admin Dashboard to route navigation.
  * Implemented logic for administrators to append new data to the `products.txt` and `codes.txt` files.
  * Created an automated random-string generator algorithm for code card creation.

### Phase 4: User Console Features
* **Objective:** Bring the niche business model to life via the interactive user terminal.
* **Execution:**
  * Built the User Dashboard navigation system.
  * Implemented the Wallet logic, allowing users to search the active code database, redeem a match, add to their gold balance, and delete the used code from the system.
  * Built the Marketplace and Inventory logic, handling the transactional exchange of gold for `Hangpie` objects and updating the specific user's save file.

### Phase 5: The Game Integration (Java AWT)
* **Objective:** Transition from the console into a graphical interface for the core battle loop.
* **Execution:**
  * Designed the Java AWT frames, panels, and layout managers.
  * Built the `GameEngine` class to handle the Hangman-style string-matching mechanics.
  * Connected GUI buttons (letter inputs) to the internal logic, triggering health bar updates via the `takeDamage()` method inherited from Phase 1.
  * Established the PvE progression loop (Mob vs. Boss tracking, auto-leveling the Hangpie, and issuing gold rewards back to the console model).

### Phase 6: Polish & Final Requirements Check
* **Objective:** Ensure application stability and finalize formatting.
* **Execution:**
  * Implemented input validation (e.g., catching `InputMismatchException` when a user types a letter instead of a menu number).
  * Integrated a localized Activity Log to track user runtime actions.
  * Conducted final playthrough tests to ensure seamless handoffs between the Java Console context and the Java AWT GUI context.

---

## 📂 Project Structure

The project code is organized within an Eclipse IDE structure as follows:

```text
Battle-Hangpies/
│
├── src/
│   └── com/
│       └── battlehangpies/
│           ├── main/          # Core driver files and initialization code
│           ├── models/        # Encapsulated entities (Character, User, Hangpie)
│           ├── managers/      # File database parsers, authenticators, and state managers
│           └── gui/           # Java AWT classes governing the battle frames and graphics
│
├── users.txt                  # Local text file storing account profiles
├── products.txt               # Local text file tracking listed marketplace items
├── codes.txt                  # Local text file listing generated balance vouchers
└── README.md                  # System documentation manual
```

## ⚙️ Execution and Setup

### 🛠️ Prerequisites
* **Java Development Kit (JDK):** Version 8 or newer is required to successfully compile and run the Java AWT graphical components.
* **IDE:** Eclipse IDE is highly recommended as the project structure is optimized for it, but any Java-compatible environment (IntelliJ IDEA, NetBeans, or CLI) will work.

### 📥 Installation & Setup Guide

**Step 1: Clone the Repository**
Open your terminal or command prompt and execute the following command:

```bash
git clone [https://github.com/rewisPage/Battle-Hangpies.git](https://github.com/rewisPage/Battle-Hangpies.git)
```

**Step 2: Import the Project into Eclipse**
1. Launch Eclipse IDE.
2. Navigate to **File** > **Import...**
3. Select **General** > **Existing Projects into Workspace** and click **Next**.
4. Click **Browse...** next to "Select root directory" and locate the downloaded `Battle-Hangpies` folder.
5. Ensure the project is checked in the "Projects" list area and click **Finish**.

**Step 3: Verify Database Files (Crucial Step)**
1. In the Eclipse Package Explorer, look at the root directory of the `Battle-Hangpies` project.
2. Ensure that the three flat text files (`users.txt`, `products.txt`, `codes.txt`) are located at this root level (at the exact same level as the `src` folder, **not** inside it). 
*Note: If `users.txt` is missing, the system's `UserManager` is designed to automatically generate a default file upon the first launch to prevent crash errors.*

**Step 4: Launch the System**
1. In the Eclipse Package Explorer, expand the `src` folder.
2. Navigate to the `com.battlehangpies.main` package.
3. Locate the primary driver class (e.g., `Main.java`).
4. Right-click the file, select **Run As** > **Java Application**.
5. Interact with the application via the Eclipse Console window at the bottom of your screen.

### 🔑 Default System Credentials
Upon launching, you can log into the system using the pre-configured administrator account:
* **Role:** Admin
* **Username:** `admin`
* **Password:** `admin123`

*(To test the standard user perspective, simply use the "Sign Up" option in the console menu upon startup to register a new player profile).*
