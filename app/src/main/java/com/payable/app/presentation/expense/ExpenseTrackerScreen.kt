package com.payable.app.presentation.expense

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTrackerScreen(
    navController: NavHostController,
    viewModel: ExpenseViewModel = hiltViewModel(),
) {
    val expenses by viewModel.expenses.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("Food", "Rent", "Utilities", "Salary", "Transportation", "Entertainment", "Other")
    val balance = totalIncome - totalExpenses

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Tracker") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            // Balance Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Income: $${String.format("%,.0f", totalIncome).replace(',', ' ')} - Expenses: $${String.format("%,.0f", totalExpenses).replace(',', ' ')}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "Balance: $${String.format("%,.0f", balance).replace(',', ' ')}",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (balance >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Monthly Summary Chart
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = "Monthly Summary",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Simple Bar Chart
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        val maxValue = maxOf(totalIncome, totalExpenses).takeIf { it > 0.0 } ?: 1000.0

                        // Income Bar
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = "$${String.format("%,.0f", totalIncome).replace(',', ' ')}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF4CAF50),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height((totalIncome / maxValue * 80).dp),
                            ) {
                                drawRect(
                                    color = Color(0xFF4CAF50),
                                    size = size
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Income",
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }

                        // Expenses Bar
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = "$${String.format("%,.0f", totalExpenses).replace(',', ' ')}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFF44336),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height((totalExpenses / maxValue * 80).dp),
                            ) {
                                drawRect(
                                    color = Color(0xFFF44336),
                                    size = size
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Expenses",
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Is Income?")
                        Switch(
                            checked = isIncome,
                            onCheckedChange = { isIncome = it }
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.addExpense(amount, selectedCategory, isIncome)
                            if (errorMessage == null) {
                                amount = ""
                                selectedCategory = ""
                                isIncome = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Add")
                    }

                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color(0xFFF44336),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expenses List
            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(vertical = 8.dp),
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(expenses) { expense ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically(),
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = expense.category,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                                    )
                                    Text(
                                        text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                            .format(Date(expense.date)),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                    )
                                }
                                Text(
                                    text = "${if (expense.isIncome) "+" else "-"}$${String.format("%,.0f", expense.amount).replace(',', ' ')}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (expense.isIncome) Color(0xFF4CAF50) else Color(0xFFF44336),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
