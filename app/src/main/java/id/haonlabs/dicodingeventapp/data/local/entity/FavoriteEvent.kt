package id.haonlabs.dicodingeventapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "mediaCover")
    var mediaCover: String? = null,
    @ColumnInfo(name = "imageLogo")
    var imageLogo: String? = null,
    @ColumnInfo(name = "summary")
    var summary: String = "",
    @ColumnInfo(name = "link")
    var link: String = "",
) : Parcelable
