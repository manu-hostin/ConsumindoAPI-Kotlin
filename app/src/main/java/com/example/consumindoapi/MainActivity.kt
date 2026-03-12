package com.example.consumindoapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

import com.example.consumindoapi.ui.theme.ConsumindoAPITheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import org.json.JSONObject
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConsumindoAPITheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ProdutoAPI()
                }
            }
        }
    }
}

data class Produto(
    val titulo: String,
    val preco: Double,
    val imagem: String
)

@Composable
fun ProdutoAPI() {

    val url = "https://dummyjson.com/products"

    var listaProdutos by remember {
        mutableStateOf(listOf<Produto>()) // listaOf é uma lista imutável, então não daria para add Produto
    }

    // roda apenas uma vez
    LaunchedEffect(Unit) {

        Thread {

            // Faz a requisição HTTP para a API
            val resposta = URL(url).readText()
            // Converte a resposta (String) para um objeto JSON
            val json = JSONObject(resposta)
            // Pega o array "products" que existe dentro do JSON
            val produtosJSON = json.getJSONArray("products")

            val temp = mutableListOf<Produto>() // Criamos uma lista temporaria

            for (i in 0 until produtosJSON.length()) {

                // Pega cada produto individual
                val produto = produtosJSON.getJSONObject(i)

                // Extrai os dados do produto
                val titulo = produto.getString("title")
                val preco = produto.getDouble("price")
                val imagem = produto.getString("thumbnail")

                temp.add(Produto(titulo, preco, imagem))
            }

            listaProdutos = temp // Adicionamos a lista temporaria na lista imutável

        }.start()

    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text (
            text = "Loja Cosmetic's",
            fontWeight = FontWeight.Bold,
            fontSize = 50.sp,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFFE91E63),
            modifier = Modifier.padding(top = 18.dp)
            )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Para cada item da lista, crie um card
            items(listaProdutos) { prod ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE4E9)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AsyncImage(
                            model = prod.imagem,
                            contentDescription = prod.titulo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                        )

                        Text(
                            text = prod.titulo,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(text = "Preço: $${prod.preco}")

                    }

                }
            }

        }
    }
}