# Task Plan: Add Back Button, Worker Profile in Map View, and Location Toggle

## Information Gathered

### Current Application Structure:
1. **UserDashboard.kt** - Main dashboard with service selection, provider list, and provider details. Already has conditional back button based on `DashboardState`.
2. **UserLogin.kt** - Map view screen with category dropdown, map, and worker list. **Currently has NO back button**.
3. **SettingsScreen.kt** - Settings screen with back button (already implemented).
4. **WorkerProfileScreen.kt** - Worker profile details with back button (already implemented).
5. **MainActivity.kt** - Navigation routes.
6. **OsmMapView.kt** - OpenStreetMap component.

### Issues to Fix:
1. **UserLogin.kt** (Map View) - Missing back button at top left corner.
2. **Map View** - When clicking on worker in the list, it already navigates to WorkerProfileScreen which shows the same profile info.
3. **Location toggle** - Need to add an option to enable/disable location when switching to map view.

## Plan

### Step 1: Add Back Button to UserLogin.kt (Map View Screen)
- Add ArrowBack icon button in the header section
- Navigate back to UserDashboard when clicked

### Step 2: Add Location Toggle Feature in UserLogin.kt
- Add a toggle button/switch in the header to enable/disable location
- When location is OFF: Show default location (Bangalore) without requesting permissions
- When location is ON: Request location permissions and use actual user location

### Step 3: Verify Worker Profile Navigation in Map View
- The click on worker already navigates to WorkerProfileScreen which shows the same profile info
- No changes needed here as it's already implemented

## Files to Edit

1. **app/src/main/java/com/example/gigmarket/ui/screens/UserLogin.kt**
   - Add back button in header
   - Add location toggle feature
   - Update UI accordingly

## Followup Steps
- Test the app to verify:
  1. Back button works in map view
  2. Location toggle works properly
  3. Worker profile shows correctly when clicking on worker in map view

