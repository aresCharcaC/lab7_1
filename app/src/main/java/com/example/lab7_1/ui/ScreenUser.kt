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
    var dataUser by remember { mutableStateOf("") }

    val dao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                val user = User(firstName = firstName, lastName = lastName)
                coroutineScope.launch {
                    dao.insert(user)
                    firstName = ""
                    lastName = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Usuario", fontSize = 16.sp)
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    dataUser = getUsers(dao)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Listar Usuarios", fontSize = 16.sp)
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    dao.deleteLastUser()
                    dataUser = getUsers(dao)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar Último Usuario", fontSize = 16.sp)
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = dataUser,
            fontSize = 20.sp
        )
    }
}

suspend fun getUsers(dao: UserDao): String {
    var rpta = ""
    val users = dao.getAll()
    users.forEach { user ->
        rpta += "${user.firstName} - ${user.lastName}\n"
    }
    return rpta
}