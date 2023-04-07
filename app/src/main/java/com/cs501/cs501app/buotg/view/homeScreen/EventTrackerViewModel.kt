package com.cs501.cs501app.buotg.view.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs501.cs501app.buotg.database.repositories.EventRepository
import com.cs501.cs501app.buotg.database.entities.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class EventTrackerViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private var emptyEvent = Event(event_id = UUID.randomUUID(), event_name = "Empty Event", latitude = 1234, longitude = 9876, start_time = Date(), end_time = Date(),
        repeat_mode = 0, priority = 1, desc = "Empty Event description")
    private val _currentEventStream = MutableStateFlow(emptyEvent)
    val currentEventStream: StateFlow<Event> = _currentEventStream
    val eventListStream: Flow<List<Event>> = eventRepository.eventStream
    fun newEmptyEvent() {
        emptyEvent = Event(event_id = UUID.randomUUID(), event_name = "Empty Event", latitude = 1234, longitude = 9876, start_time = Date(), end_time = Date(),
            repeat_mode = 0, priority = 1, desc = "Empty Event description")
    }
    fun resetCurrentEvent() {
        newEmptyEvent()
        _currentEventStream.update { emptyEvent }
    }
    fun updateCurrentEvent(event: Event) {
        _currentEventStream.update { event }
    }

    fun saveEvent() = viewModelScope.launch {
        if (_currentEventStream.value.event_id == emptyEvent.event_id) {
            newEmptyEvent()
            eventRepository.addEvent(_currentEventStream.value)
        } else {
            eventRepository.updateEvent(_currentEventStream.value)
        }
    }

    fun deleteEvent(event: Event) = viewModelScope.launch {
        eventRepository.deleteEvent(event)
    }
}