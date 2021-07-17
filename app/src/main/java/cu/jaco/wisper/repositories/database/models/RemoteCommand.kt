package cu.jaco.wisper.repositories.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "remote_command")
data class RemoteCommand(
    @PrimaryKey
    @ColumnInfo(name = "phone")
    val id: String,
    @ColumnInfo(name = "alias")
    val alias: String,
    @ColumnInfo(name = "coordinates_sms")
    @SerializedName("coordinates_sms")
    val coordinatesSms: String,
    @ColumnInfo(name = "wipe_sms")
    @SerializedName("wipe_sms")
    val wipeSms: String
)