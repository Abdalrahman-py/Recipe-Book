# Copilot Prompts Strategy

This document defines how Copilot should generate code for this project.

Model recommendation:
Use strongest reasoning model available for architectural slices.
Use faster model for small UI or bug fixes.

---

# General Rules for Code Generation

- Follow architecture.md strictly.
- Do not introduce Navigation Component.
- Do not introduce additional Activities.
- Follow MVVM separation.
- Do not put Firebase logic inside Fragments.
- All Firebase calls must go through Repository.
- Use LiveData for UI updates.
- Use ViewBinding in Fragments.
- Use Material 3 components.

---

# Vertical Slice Development Strategy

Each slice must:
- Compile successfully.
- Be fully functional.
- Not break existing features.

Commit after each slice.

---

# Slice Order

1. Activity and Fragment skeleton only.
2. Authentication logic.
3. User Firestore profile integration.
4. Recipe create and home list.
5. Categories TabLayout filtering.
6. Search logic integrated with tabs.
7. Recipe details formatting.
8. Edit and delete with owner enforcement.
9. Profile screen with user recipes.
10. Stability and validation sweep.

---

# Firebase Rules

- Use serverTimestamp() for createdAt.
- Store ingredients and steps as List<String>.
- Use creatorId to determine ownership.
- Never trust UI only for ownership checks.

---

# Fragment Navigation Pattern

Use:

getParentFragmentManager()
.beginTransaction()
.replace()
.addToBackStack()
.commit()

No Navigation Component allowed.

---

# Error Handling

Every Firebase operation must:
- Handle success
- Handle failure
- Show Toast

No silent failures allowed.

---

# Important

Do not refactor architecture unless explicitly instructed.
Do not rename Firestore fields.
Do not change model structure.
Do not move away from two-activity structure.

---

End of prompts specification.
