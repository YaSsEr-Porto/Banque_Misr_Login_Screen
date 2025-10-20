package com.example.banquemisrlogin

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.banquemisrlogin.ui.theme.BanqueMisrLoginTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        //Default Language
        super.attachBaseContext(LocaleHelper.setLocale(newBase, "en"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var lang by rememberSaveable { mutableStateOf("en") }
            val context = remember(lang) {
                LocaleHelper.setLocale(this, lang)
            }
            CompositionLocalProvider(
                LocalLayoutDirection provides if (lang == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
            ) {
                BanqueMisrLoginTheme {
                    Scaffold(modifier = Modifier) { innerPadding ->
                        LoginDesign(
                            currentLang = lang,
                            onToggleLang = { lang = if (lang == "en") "ar" else "en" },
                            context = context,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

//Update app configuration with a new Locale
object LocaleHelper {
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale.forLanguageTag(language)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)

    }
}

@Composable
fun LoginDesign(
    currentLang: String,
    onToggleLang: () -> Unit,
    context: Context,
    modifier: Modifier = Modifier
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.bm_icon),
                contentDescription = stringResource(R.string.banque_misr_logo),
                modifier = Modifier.height(80.dp)
            )
            Text(
                text = if (currentLang == "ar") context.getString(R.string.language)
                else context.getString(R.string.language),
                color = Color(0xFFAB2F3F),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onToggleLang() }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = context.getString(R.string.username),
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = context.getString(R.string.password),
            isPassword = true,
            passwordVisible = passwordVisible,
            onVisibilityToggle = { passwordVisible = !passwordVisible })

        Text(
            text = context.getString(R.string.forgot_username_password),
            fontSize = 12.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {},
            shape = RoundedCornerShape(20),
            enabled = username.isNotBlank() && password.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBF3547), disabledContainerColor = Color(0xFFEFD0D4)
            ),
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
        ) {
            Text(context.getString(R.string.login), color = Color.White)
        }


        Text(
            buildAnnotatedString {
                append(context.getString(R.string.need_help))

                withLink(
                    LinkAnnotation.Url(
                        url = "https://www.banquemisr.ae/contact-us/",
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC95564),
                                textDecoration = TextDecoration.Underline
                            )
                        ),
                    )
                ) {
                    append(context.getString(R.string.contact_us))
                }
            },
            modifier = Modifier.padding(top = 32.dp),
        )

        Spacer(modifier = Modifier.height(42.dp))

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        Spacer(modifier = Modifier.height(42.dp))

        BanqueFeatures(context)
    }
}

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityToggle: (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword && onVisibilityToggle != null) {
            {
                IconButton(onClick = onVisibilityToggle) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(R.string.toggle_password_visibility)
                    )
                }
            }
        } else null,
        modifier = Modifier.fillMaxWidth())
}

@Composable
fun BanqueFeatures(context: Context) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FeaturesItem(
            icon = R.drawable.our_products,
            label = context.getString(R.string.our_products)
        )
        FeaturesItem(
            icon = R.drawable.exchange_rate,
            label = context.getString(R.string.exchange_rate)
        )
        FeaturesItem(
            icon = R.drawable.security_tips,
            label = context.getString(R.string.security_tips)
        )
        FeaturesItem(
            icon = R.drawable.nearest_branch_or_atm,
            label = context.getString(R.string.nearest_branch_or_atm)
        )
    }
}

@Composable
fun FeaturesItem(icon: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = Color.Unspecified,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    BanqueMisrLoginTheme {
        LoginDesign(currentLang = "en", onToggleLang = {}, context = LocalContext.current)
    }
}