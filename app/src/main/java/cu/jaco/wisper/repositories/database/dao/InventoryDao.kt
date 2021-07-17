package cu.jaco.wisper.repositories.database.dao

import androidx.room.Dao
import cu.jaco.wisper.repositories.database.models.RemoteCommand
import cu.jaco.wisper.repositories.database.utils.BaseDao

@Dao
interface RemoteCommandDao : BaseDao<RemoteCommand> {

}