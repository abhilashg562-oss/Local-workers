# Task Plan: User Dashboard Service Selection Feature

## Information Gathered:
1. **Project Structure**: Gig Market app using Jetpack Compose with neon/dark theme
2. **Backend**: Node.js with MongoDB - Provider model already has:
   - `skills` array for worker categories
   - `location` field with GeoJSON for 5km proximity search
   - Existing endpoint: `GET /api/providers/nearby?lat=X&lng=Y&radius=5000&category=Z`
3. **Current State**: UserDashboard.kt is a simple welcome screen with no functionality

## Plan:

### 1. Create Network Layer for Providers
- **ProviderModels.kt**: Data classes for Provider, NearbyProvidersResponse
- **ProviderApi.kt**: Retrofit interface with `/api/providers/nearby` endpoint
- **RetrofitClient.kt**: Add ProviderApi to the retrofit instance

### 2. Create Repository & ViewModel
- **ProviderRepository.kt**: Handle API calls to fetch nearby providers
- **ProviderViewModel.kt**: Manage state for selected service and provider list

### 3. Create UI Components
- **ServiceCategories.kt**: Object with list of available services (Electrician, Plumber, Carpenter, Driver, AC Repair, Refrigerator Repair, Washing Machine Repair, RO Water Purifier Service, House Shifting, Cleaners)
- **ServiceCategoryCard.kt**: Reusable neon-styled card for each service
- **ProviderCard.kt**: Reusable card to display worker details

### 4. Redesign UserDashboard.kt
- Add service category selection grid with neon theme
- Add location permission handling (get user's current location)
- Fetch and display nearby workers when a category is selected
- Show worker details (name, rating, hourly rate, skills)

### 5. Update MainActivity.kt
- Pass user location data to UserDashboard if needed

## Dependent Files to be Created:
- network/ProviderModels.kt
- network/ProviderApi.kt
- repository/ProviderRepository.kt
- viewmodel/ProviderViewModel.kt
- ui/screens/ServiceCategories.kt
- ui/components/ServiceCategoryCard.kt
- ui/components/ProviderCard.kt

## Dependent Files to be Edited:
- network/RetrofitClient.kt
- ui/screens/UserDashboard.kt

## Followup Steps:
- Test the app to ensure location permission is handled properly
- Verify API calls work with the backend
- Test with mock provider data in database

