package com.example.gigmarket.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.gigmarket.viewmodel.WorkerWithDistance
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/**
 * A display-only OpenStreetMap view showing user location and nearby worker markers.
 * Scroll and touch gestures are disabled to avoid conflicts with the parent scroll view.
 */
@Composable
fun OsmMapView(
    userLat: Double,
    userLng: Double,
    workers: List<WorkerWithDistance>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            // Configure OSMDroid before first use
            Configuration.getInstance().apply {
                load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
                userAgentValue = ctx.packageName
            }
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                // Disable multi-touch zoom - map is display-only inside a scroll view
                setMultiTouchControls(false)
                controller.setZoom(13.5)
            }
        },
        update = { mapView ->
            try {
                mapView.controller.setCenter(GeoPoint(userLat, userLng))
                mapView.overlays.clear()

                // User's location marker
                val userMarker = Marker(mapView).apply {
                    position = GeoPoint(userLat, userLng)
                    title = "📍 You are here"
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                mapView.overlays.add(userMarker)

                // Worker markers – one per filtered worker
                workers.forEach { w ->
                    // coordinates stored as [longitude, latitude]
                    val workerLat = w.provider.location.coordinates[1]
                    val workerLng = w.provider.location.coordinates[0]
                    val marker = Marker(mapView).apply {
                        position = GeoPoint(workerLat, workerLng)
                        title = w.provider.name
                        snippet = "${w.provider.category} • ${String.format("%.1f", w.distanceKm)} km away"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    mapView.overlays.add(marker)
                }

                mapView.invalidate()
            } catch (e: Exception) {
                // Silently ignore map update errors (e.g., context not ready)
            }
        },
        modifier = modifier
    )
}
