package com.example.lab7_1.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.DB.DAO.UserDao
import com.example.DB.UserDatabase
import com.example.DB.entity.User
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {
    val context = LocalContext.current
    val db = remember { UserDatabase.getDatabase(context) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var users by remember { mutableStateOf<List<User>>(emptyList()) }

    val dao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios") },
                actions = {
                    IconButton(onClick = {
                        if (firstName.isNotBlank() && lastName.isNotBlank()) {
                            val user = User(firstName = firstName, lastName = lastName)
                            coroutineScope.launch {
                                dao.insert(user)
                                firstName = ""
                                lastName = ""
                            }
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar Usuario")
                    }
                    Box {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                users = dao.getAll()
                                isDropdownExpanded = true
                            }
                        }) {
                            Icon(Icons.Default.List, contentDescription = "Listar Usuarios")
                        }
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false }
                        ) {
                            if (users.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No hay usuarios") },
                                    onClick = { }
                                )
                            } else {
                                users.forEach { user ->
                                    DropdownMenuItem(
                                        text = { Text("${user.firstName} ${user.lastName}") },
                                        onClick = { }
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name: ") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name:") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        dao.deleteLastUser()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar Último Usuario", fontSize = 16.sp)
            }
        }
    }
}