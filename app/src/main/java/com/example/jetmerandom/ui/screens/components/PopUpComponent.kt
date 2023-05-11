import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.example.jetmerandom.R
import com.example.jetmerandom.ui.SearchViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HeadOptions(
    viewModel: SearchViewModel
){

    val state = viewModel.uiState.collectAsState().value

    var popupControl by remember { mutableStateOf(false) }

    var nAdults by remember { mutableStateOf(2) }

    var nChilds by remember { mutableStateOf(0) }

    val popupProperties = PopupProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.SecureOff,
        clippingEnabled = true,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = Color.Blue),

        ) {
        TextButton(
            onClick = { popupControl = true },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (state.isDirect){
                IconWithText(
                    stringResourceId = R.string.direct_flight,
                    iconResourceId = R.drawable.baseline_airplane_ticket_24)
            } else {
                IconWithText(
                    stringResourceId = R.string.indirect_flight,
                    iconResourceId = R.drawable.baseline_airplane_ticket_24)
            }
            Spacer(modifier = Modifier.width(70.dp))
            IconWithText(
                value = state.qAdults.toString(),
                iconResourceId = R.drawable.baseline_face_24)

            Spacer(modifier = Modifier.width(10.dp))
            IconWithText(
                value = state.qChilds.toString(),
                iconResourceId = R.drawable.baseline_child_care_24)
        }
    }

    if (!state.checkPassengers){
        Text(
            text = "The total nº of passenger cant be 0",
            color = Color.Red,
            fontSize = 12.sp
        )
    }

    if (popupControl) {
        Popup (
            onDismissRequest = {popupControl = false},
            alignment = Alignment.BottomStart,
            properties = popupProperties,
        ){
            Box(
                modifier = Modifier
                    .width(345.dp)
                    .height(280.dp)
                    .background(Color.LightGray, RoundedCornerShape(16.dp)),
            ){
                IconButton(
                    onClick = { popupControl = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_cancel_24),
                        contentDescription = null,
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(30.dp))
                    TextButton(onClick = { viewModel.setIsDirect(true) }) {
                        Text(
                            text = stringResource(id = R.string.direct_flight),
                            fontSize = 16.sp
                        )
                    }
                    TextButton(onClick = { viewModel.setIsDirect(false) }) {
                        Text(
                            text = stringResource(id = R.string.indirect_flight),
                            fontSize = 16.sp
                        )
                    }
                    Divider(color = Color.Gray, thickness = 1.dp)
                    PopUpRow(
                        viewModel = viewModel,
                        iconResourceId = R.drawable.baseline_face_24,
                        ageGroup = "Adult")
                    PopUpRow(
                        viewModel = viewModel,
                        iconResourceId = R.drawable.baseline_child_care_24,
                        ageGroup = "Child")
                }
            }
        }
    }
}

@Composable
fun PopUpRow(
    viewModel: SearchViewModel,
    @DrawableRes iconResourceId: Int,
    ageGroup: String
){

    val state = viewModel.uiState.collectAsState().value


    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        IconButton(
            onClick = {
                if (ageGroup == "Adult" && state.qAdults > 0){
                    viewModel.setPassenger(state.qAdults.dec(), ageGroup)
                } else if (ageGroup == "Child" && state.qChilds > 0) {
                    viewModel.setPassenger(state.qChilds.dec(), ageGroup)
                }
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_remove_24),
                contentDescription = null
            )
        }
        Spacer(Modifier.width(20.dp))
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = null
        )

        if (ageGroup == "Adult"){
            Text(
                text = state.qAdults.toString(),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        } else {
            Text(
                text = state.qChilds.toString(),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 5.dp)
            )
        }
        Spacer(Modifier.width(20.dp))
        IconButton(
            onClick = {
                if (ageGroup == "Adult" && state.qAdults <= 10){
                    viewModel.setPassenger(state.qAdults.inc(), ageGroup)
                } else if (ageGroup == "Child" && state.qChilds <= 10) {
                    viewModel.setPassenger(state.qChilds.inc(), ageGroup)
                }
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = null
            )
        }
    }
}

@Composable
fun IconWithText(
    @StringRes stringResourceId: Int,
    @DrawableRes iconResourceId: Int,
){
    Icon(
        painter = painterResource(id = iconResourceId),
        contentDescription = null
    )
    Text(
        text = stringResource(id = stringResourceId),
    )
}
@Composable
fun IconWithText(
    value: String,
    @DrawableRes iconResourceId: Int,
){
    Icon(
        painter = painterResource(id = iconResourceId),
        contentDescription = null
    )
    Text(
        text = value,
    )
}