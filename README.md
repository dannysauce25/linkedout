# Linked Out

## Team Information
### Team Members
- Pauline Nguyen
  - GitHub: https://github.com/papql 
  - Email: pauline.nguyen@mail.utoronto.ca
  - Message for Your Future Self: kinda want molly tea
    
- Lucy Liu
  - GitHub: https://github.com/NOVAx1216-coder
  - Email: lucymeichen.liu@mail.utoronto.ca
  - Message for Your Futures Self: Locked in and Linked Out!
    
- Joon Park
  - GitHub: https://github.com/kdaster
  - Email: jooner.park@mail.utoronto.ca
  - Message for Your Futures Self:
    
- Serena Ganbaatar
  - GitHub: https://github.com/serena-gb
  - Email: saruul.ganbaatar@mail.utoronto.ca
  - Message for Your Futures Self: This too shall pass.
    
- Issac Wan
  - GitHub: https://github.com/isaacwan-dev
  - Email: isaac.wan@mail.utoronto.ca
  - Message for Your Futures Self: We can do this
    
- Daniel Park
  - GitHub: https://github.com/dannysauce25
  - Email: daniell.park@mail.utoronto.ca
  - Message for Your Futures Self: we got dis shi
---
## Project Description
Turn everyday life into an adventure with Linked Out, a gamified productivity app that encourages users to explore new interests, step outside their comfort zones, and take on exciting side quests! From earning XP and leveling up to capturing memories and tracking accomplishments, Linked Out makes trying new things feel like a game.

### Domain
Side Quest App focused on Productivity & Entertainment

[Project description]
---

## User Stories
### We will remove this later but here is a template for how you can write your user stories: As a [kind of user] I want to [accomplish a goal] so that [I receive some benefit] -Pauline
### User Story 1: As a user, I want to login and set up my personal info (username, profile picture) and check out my current level so that I can visually personalize my account page.
### User Story 2: As a user, I want to accept side quests and track them on a dashboard, marking each one completed or abandoned and earning XP when I finish, so that I can see my progress and feel accomplished as my history grows.
### User Story 3: As a user, I want to document my completed side quests through journal entries and photos (either taken with my camera or uploaded from my gallery) so that I can look back on my experiences, relive fun memories, and celebrate my achievements.
### User Story 4: <Map>
### User Story 5: <Recommendations Quests> As a user, I want to receive a list of recommended side quests so that I can choose side quests I am interested in.
### User Story 6: <Defaults/Adding Quests> As a user, I want to be able to log a side quest I completed as an entry in the app for other people to see, as well as browse a collection of default side quests to complete.
---
## Use Cases
### Use Case 1: LogIn, SetUsername, SetProfilePicture, CheckLevel
### Use Case 2: <Progress>
Lead: Daniel
2.1 Accept Quest

Main flow:
User clicks "Do this quest" on a quest (from the quest catalogue, recommendations, or a map pin).
System asks "Would you like to do this side quest?" and the user confirms.
System records the quest as PENDING for this user, with the current time as the start time.
System shows "Quest accepted!" and the quest appears under Pending on the dashboard.

Alternative flows:
The quest is already pending for this user: system shows "You're already doing this quest."
The user has already completed this quest: system shows "You've already completed this quest."
The quest doesn't exist: system shows an error.
The user cancels at step 2: nothing happens.

2.2 Complete Quest

Main flow:
User clicks "Complete" on a pending quest in the dashboard.
System checks that at least 10 minutes have passed since the quest was accepted and that the user has completed fewer than 5 quests today.
System marks the quest COMPLETED and adds its XP to the user.
System shows "+50 XP!" (and "Level up!" if the user's level went up).
System asks "Would you like to journal this?", which opens the journal entry (Use Case 3.x).

Alternative flows:
Less than 10 minutes since accepting: system shows "Too soon! Wait N more minutes before completing."
5 quests already completed today: system shows "You've hit today's limit of 5 quests. Come back tomorrow!"
The quest is not pending (already completed or abandoned): system shows an error.
The quest has been deleted: system shows "This quest no longer exists."

In every alternative flow, nothing is saved and no XP is given.

2.3 Abandon Quest

Main flow:
User clicks "Abandon" on a pending quest.
System asks "Are you sure? You won't get XP for this quest." and the user confirms.
System marks the quest ABANDONED and moves it to the Abandoned list.

Alternative flows:
The quest is not pending: system shows an error.
The user cancels at step 2: nothing happens.

2.4 View Dashboard 

Main flow:
User opens the Dashboard tab (it also refreshes after any of Use Cases 2.1 to 2.3).
System shows the user's quests in three lists, Pending, Completed, and Abandoned, newest first. Each row shows the title, theme, XP, and date.
System shows a summary: number of quests completed and total XP earned from quests.

Alternative flows:
The user has no quests: system shows "No quests yet!" with a button to the recommendations.
### Use Case 3: Create a Journal Entry
Lead: Pauline Nguyen <br>
Primary actor: App user <br>
Goal: Save a journal entry containing a written reflection and optional photos, associated with a selected quest.
Preconditions: <br>
The user is logged in. <br>
The user has a quest available (quest has status of completed, in progress or cancelled) that they can journal about. <br>
Trigger: The user selects “Add Journal Entry” for a quest. <br>
Main success scenario: <br>
1. The system displays a journal entry form showing the selected quest.
2. The user enters a written reflection.
3. The user optionally imports photos.
4. The system displays the selected photos alongside the reflection.
5. The user selects “Save".
6. The system validates the entry.
7. The system saves the entry with its associated user and quest.
8. The system displays the saved entry and confirms that it was saved successfully.

Alternative flows and exceptions:
1. Empty reflection: The system displays a validation message and allows the user to enter text.
2. Camera unavailable or permission denied: The user can import an existing photo or continue without one.
3. Save fails: The system displays an error, retains the entered content, and allows the user to retry.
4. User cancels: The system asks whether to discard unsaved changes. If confirmed, it exits without creating an entry.

Success postcondition: The journal entry is saved and available for the user to revisit. <br>
Failure postcondition:No new journal entry is saved. <br>

Features that can be added later: <br>
1. Editing and deleting existing entries.
2. Searching or filtering entries.

### Use Case 4: <Map>
### Use Case 5: <Recommendations Quests>
### Use Case 6: <Defaults/Adding Quests>
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Pauline |**Create a Text Journal Entry**<br><br>**Lead:** Pauline Nguyen<br>**Primary actor:** App user<br><br>**Goal:** Create and save a text journal entry linked to a quest.<br><br>**Preconditions:**<br>1. The user is logged in.<br>2. The user has a quest available to journal about.<br>3. The user is on the journal page for that quest.<br><br>**Trigger:** The user clicks a text block on the journal page for the quest.<br><br>**Main success scenario:**<br>1. The system displays the selected quest and a text field for the reflection.<br>2. The user enters a reflection.<br>3. The user selects “Save.”<br>4. The system checks that the reflection is not blank.<br>5. The system saves the entry with its associated user and quest.<br>6. The system displays the saved entry.<br><br>**Alternative flows and exceptions:**<br>1. Blank reflection: The system displays “Please enter a reflection” and keeps the form open.<br>2. Save fails: The system displays an error and retains the reflection so the user can retry.<br>3. User cancels: The system exits without saving the entry.<br><br>**Success postcondition:** The entry is stored persistently and can be retrieved after restarting the application.<br><br>**Failure postcondition:** No new entry is saved. |
| Lucy | Use Case 1: LogIn, SetUsername |
| Joon | Use Case |
| Serena | Use Case |
| Issac | Use Case 6: Create Side Quests |
| Daniel |**Use Case 2.1: Accept Quest**<br><br>**Lead:** Daniel Park<br>**Primary actor:** App user<br><br>**Goal:** Start a quest so it appears as pending on the dashboard.<br><br>**Preconditions:**<br>1. The user is logged in.<br>2. The quest exists.<br>3. The user is viewing the quest in the quest catalogue, recommendations, or map.<br><br>**Trigger:** The user clicks “Do this quest.”<br><br>**Main success scenario:**<br>1. The system displays “Would you like to do this side quest?”<br>2. The user confirms.<br>3. The system checks that the quest is not already pending or completed for this user.<br>4. The system saves the quest as PENDING with the current time as its start time.<br>5. The system displays “Quest accepted!” and the quest appears under Pending on the dashboard.<br><br>**Alternative flows and exceptions:**<br>1. Already pending: The system displays “You're already doing this quest.”<br>2. Already completed: The system displays “You've already completed this quest.”<br>3. Quest no longer exists: The system displays an error.<br>4. User cancels: The system exits without saving.<br><br>**Success postcondition:** The quest is stored as pending with its start time and can be retrieved after restarting the application.<br><br>**Failure postcondition:** Nothing is saved.<br><br><br>**Use Case 2.2: Complete Quest**<br><br>**Lead:** Daniel Park<br>**Primary actor:** App user<br><br>**Goal:** Mark a pending quest as completed and earn its XP.<br><br>**Preconditions:**<br>1. The user is logged in.<br>2. The user has accepted the quest and it is still pending.<br>3. The user is on the dashboard.<br><br>**Trigger:** The user clicks “Complete” on a pending quest.<br><br>**Main success scenario:**<br>1. The system checks that the quest is pending.<br>2. The system checks that the user has completed fewer than 5 quests today.<br>3. The system checks that at least 10 minutes have passed since the quest was accepted.<br>4. The system marks the quest COMPLETED with the current time.<br>5. The system adds the quest's XP to the user.<br>6. The system saves the quest and the user.<br>7. The system displays “+50 XP!” and “Level up!” if the user's level increased.<br>8. The system asks “Would you like to journal this?” and opens the journal entry if the user agrees.<br><br>**Alternative flows and exceptions:**<br>1. Too soon: The system displays “Too soon! Wait N more minutes before completing.”<br>2. Daily limit reached: The system displays “You've hit today's limit of 5 quests. Come back tomorrow!”<br>3. Quest not pending: The system displays “This quest is already completed.” (or abandoned).<br>4. Quest deleted: The system displays “This quest no longer exists.”<br>5. User declines to journal: The system returns to the dashboard. The completion is still saved.<br><br>**Success postcondition:** The quest is stored as completed with its completion time, the user's XP is increased, and both can be retrieved after restarting the application.<br><br>**Failure postcondition:** The quest stays pending and no XP is given.<br><br><br>**Use Case 2.3: Abandon Quest**<br><br>**Lead:** Daniel Park<br>**Primary actor:** App user<br><br>**Goal:** Give up on a pending quest.<br><br>**Preconditions:**<br>1. The user is logged in.<br>2. The quest is pending for this user.<br>3. The user is on the dashboard.<br><br>**Trigger:** The user clicks “Abandon” on a pending quest.<br><br>**Main success scenario:**<br>1. The system displays “Are you sure? You won't get XP for this quest.”<br>2. The user confirms.<br>3. The system checks that the quest is pending.<br>4. The system marks the quest ABANDONED with the current time and saves it.<br>5. The system moves the quest to the Abandoned list.<br><br>**Alternative flows and exceptions:**<br>1. Quest not pending: The system displays an error.<br>2. User cancels: The system exits without saving.<br><br>**Success postcondition:** The quest is stored as abandoned and no XP is given.<br><br>**Failure postcondition:** The quest's status is unchanged.<br><br><br>**Use Case 2.4: View Dashboard**<br><br>**Lead:** Daniel Park<br>**Primary actor:** App user<br><br>**Goal:** See all of the user's quests grouped by status.<br><br>**Preconditions:**<br>1. The user is logged in.<br><br>**Trigger:** The user opens the Dashboard tab, or Use Case 2.1, 2.2, or 2.3 finishes.<br><br>**Main success scenario:**<br>1. The system loads the user's quests.<br>2. The system groups them into Pending, Completed, and Abandoned, newest first.<br>3. Each row shows the quest's title, theme, XP, and date.<br>4. The system displays a summary of quests completed and total XP earned from quests.<br>5. Pending rows show “Complete” and “Abandon” buttons.<br><br>**Alternative flows and exceptions:**<br>1. No quests: The system displays “No quests yet!” with a button to the recommendations.<br>2. A quest was deleted: The system skips that row.<br>3. Loading fails: The system displays an error and shows empty lists.<br><br>**Success postcondition:** The dashboard shows the user's saved quest history.<br><br>**Failure postcondition:** An error is shown and no data is changed.<br><br><br>**Later (not MVP):** Use Case 2.5: Star / Unstar Theme |
---
## Entities
### Entity 1: <Quest Entity>
UserQuest (Daniel): one user's progress on one quest.
Instance variables: username: String, questId: String, status: QuestStatus, startedAt: LocalDateTime, completedAt: LocalDateTime (null unless completed), abandonedAt: LocalDateTime (null unless abandoned)
Constant: MIN_MINUTES_BEFORE_COMPLETE = 10
Methods: getters, isPending(), meetsMinimumTime(now), minutesUntilCompletable(now), complete(now), abandon(now). complete and abandon throw IllegalStateException if the quest isn't pending.

QuestStatus (Daniel): enum PENDING, COMPLETED, ABANDONED. Status only moves PENDING → COMPLETED or PENDING → ABANDONED.

Full design for this part (classes per layer, data flow, teammate touchpoints): docs/DASHBOARD.md
### Entity 2: <Profile/Person Entity> User
### Entity 3
---
## API Usage
**API Name:**
**Documentation:** [Link]
**How it is used in the program:**

[Complete Later, Map API & Camera API]
---
## Design Diagrams
### UML Class Diagram 

### Sequence Diagrams


---
## How to Run <Complete Later After Implementation>
[Instructions for running the program.]
---
## Notes
Why We Chose The Name "Linked Out":

