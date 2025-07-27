package com.example.ai36.view
//
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ai36.R

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            LoginBody()

        }
    }
}

@Composable
fun LoginBody() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    var rememberMe by remember {
        mutableStateOf(false)
    }



    val context = LocalContext.current

    val activity = context as? Activity

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostScope = remember { SnackbarHostState() }

    var showDialog by remember { mutableStateOf(false) }




    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostScope) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Button(onClick = { showDialog = true }) {
                Text("Show AlertDialog")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    }, // dismiss when clicked outside
                    confirmButton = {
                        Button(onClick = {
                            // Confirm action
                            showDialog = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            // Cancel action
                            showDialog = false
                        }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text(text = "Confirm") },
                    text = { Text("Welcome!!.") }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(R.drawable.moto),
                contentDescription = null,
                modifier = Modifier
                    .height(250.dp)
                    .width(250.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            //email
            OutlinedTextField(
                value = email,
                onValueChange = { input ->
                    email = input
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(12.dp),
                prefix = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text("abc@gmail.com")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Gray.copy(0.2f),
                    unfocusedContainerColor = Color.Gray.copy(0.2f),

                    )
            )


            Spacer(modifier = Modifier.height(20.dp))
            //password
            OutlinedTextField(
                value = password,
                onValueChange = { input ->
                    password = input
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(12.dp),
                prefix = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                suffix = {
                    Icon(
                        painter = painterResource(
                            if (passwordVisibility) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24
                        ),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            passwordVisibility = !passwordVisibility
                        }
                    )
                },
                //            minLines = 4,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                placeholder = {
                    Text("*******")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                //            minLines = 5,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Gray.copy(0.2f),
                    unfocusedContainerColor = Color.Gray.copy(0.2f),

                    )
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = {
                            rememberMe = it
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Green,
                            checkmarkColor = Color.White
                        )
                    )
                    Text("Remember me")
                }

                Text("Forget Password?", modifier = Modifier.clickable {

                })
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                ),
                onClick = {
//                    if (email == "ram@gmail.com"
//                        && password == "password"
//                    ) {
                    val intent = Intent(context, DashboardActivity::class.java)
                    //first parameter - key
                    //second parameter - value
                    intent.putExtra("email",email)
                    intent.putExtra("password",password)

                    context.startActivity(intent)
                    activity?.finish()

                    Toast.makeText(
                        context,
                        "Login Success",
                        Toast.LENGTH_SHORT
                    )
                        .show()
//                    } else {
//                        coroutineScope.launch {
//                            snackBarHostScope.showSnackbar(
//                                "Invalid login",
//                                actionLabel = "Retry"
//                            )
//                        }
//                    }
                }) {
                Text("Login")
            }

            Text(
                "Don't have an account? Signup",
                modifier = Modifier.clickable {
                    val intent = Intent(context, RegistrationActivity::class.java)
                    context.startActivity(intent)

                    activity?.finish()
                })
        }
    }
}

@Preview
@Composable
fun PreviewLogin() {
    LoginBody()
}