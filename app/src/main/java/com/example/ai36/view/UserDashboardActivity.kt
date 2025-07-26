package com.example.ai36.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.ai36.R
import com.example.ai36.model.CartItemModel
import com.example.ai36.model.WishlistItemModel
import com.example.ai36.repository.CartRepositoryImpl
import com.example.ai36.repository.ProductRepositoryImpl
import com.example.ai36.repository.UserRepositoryImpl
import com.example.ai36.repository.WishlistRepositoryImpl
import com.example.ai36.viewmodel.*

val PrimaryColor = Color(0xFF684C2F)
val BackgroundColor = Color(0xFFF0EAE1)

class UserDashboardActivity : ComponentActivity() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var wishlistViewModel: WishlistViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cartViewModel = ViewModelProvider(this, CartViewModelFactory(CartRepositoryImpl()))[CartViewModel::class.java]
        wishlistViewModel = ViewModelProvider(this, WishlistViewModelFactory(WishlistRepositoryImpl))[WishlistViewModel::class.java]
        userViewModel = ViewModelProvider(this, UserViewModelFactory(UserRepositoryImpl()))[UserViewModel::class.java]

        setContent {
            MaterialTheme(colorScheme = lightColorScheme(
                primary = PrimaryColor,
                background = BackgroundColor
            )) {
                UserDashboardBody(
                    cartViewModel = cartViewModel,
                    wishlistViewModel = wishlistViewModel,
                    userViewModel = userViewModel
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        userViewModel.getCurrentUser()?.uid?.let { userViewModel.getUserById(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardBody(
    cartViewModel: CartViewModel,
    wishlistViewModel: WishlistViewModel,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current

    val productViewModel = remember { ProductViewModel(ProductRepositoryImpl()) }
    val products by productViewModel.filteredProducts.observeAsState(initial = emptyList())
    val loading by productViewModel.loading.observeAsState(initial = false)
    var searchQuery by remember { mutableStateOf("") }

    val user by userViewModel.user.observeAsState()
    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(userViewModel.getCurrentUser()?.uid) {
        userViewModel.getCurrentUser()?.uid?.let { userViewModel.getUserById(it) }
        productViewModel.getAllProducts()
    }
    LaunchedEffect(searchQuery) { productViewModel.filterProducts(searchQuery) }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Yala Carves") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        val intent = Intent(context, EditProfileActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Edit Profile", tint = Color.White)
                    }
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Address Book") },
                                onClick = {
                                    menuExpanded = false
                                    context.startActivity(Intent(context, AddressActivity::class.java))
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    menuExpanded = false
                                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = PrimaryColor) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Home", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        context.startActivity(Intent(context, CartActivity::class.java))
                    },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.White) },
                    label = { Text("Cart", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        context.startActivity(Intent(context, WishlistActivity::class.java))
                    },
                    icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Wishlist", tint = Color.White) },
                    label = { Text("Wishlist", color = Color.White) }
                )
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                val imageModifier = Modifier
                    .size(48.dp)
                    .background(Color.LightGray, CircleShape)
                val imageUrl = user?.image
                if (!imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = "Profile Picture",
                        modifier = imageModifier,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.profilepicplaceholder),
                        error = painterResource(id = R.drawable.profilepicplaceholder)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Profile",
                        modifier = imageModifier.padding(8.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Welcome, ${user?.firstName ?: "User"}!",
                    style = MaterialTheme.typography.titleLarge,
                    color = PrimaryColor
                )
            }

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search products...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )

            when {
                loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryColor)
                }
                products.isEmpty() -> Text("No products found.", Modifier.padding(16.dp), color = PrimaryColor)
                else -> LazyColumn(Modifier.padding(8.dp)) {
                    items(products.size) { index ->
                        val product = products[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = PrimaryColor)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = product?.productName ?: "No Name",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Rs. ${product?.productPrice ?: 0.0}",
                                    color = Color.White
                                )
                                Text(
                                    text = product?.productDescription ?: "",
                                    color = Color.White
                                )
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    OutlinedButton(onClick = {
                                        val cartItem = CartItemModel(
                                            id = "",
                                            productName = product?.productName ?: "",
                                            productPrice = product?.productPrice ?: 0.0,
                                            image = product?.image ?: "",
                                            quantity = 1
                                        )
                                        cartViewModel.addToCart(cartItem)
                                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                                    }) { Text("Add to Cart") }

                                    OutlinedButton(onClick = {
                                        val wishlistItem = WishlistItemModel(
                                            productName = product?.productName ?: "",
                                            productPrice = product?.productPrice ?: 0.0,
                                            image = product?.image ?: ""
                                        )
                                        wishlistViewModel.addToWishlist(wishlistItem)
                                        Toast.makeText(context, "Added to wishlist", Toast.LENGTH_SHORT).show()
                                    }) { Text(" Wishlist") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}