package com.example.notices.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DrawerViewModel():ViewModel(){
    private val _selectedItem = MutableStateFlow("All")
    val selectedItem :StateFlow<String> = _selectedItem

    fun selectedItem(item:String){
        _selectedItem.value=item
    }
}