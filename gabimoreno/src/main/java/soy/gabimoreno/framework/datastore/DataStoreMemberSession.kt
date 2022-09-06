package soy.gabimoreno.framework.datastore

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.session.MemberSession
import javax.inject.Inject

class DataStoreMemberSession @Inject constructor(
    private val context: Context
) : MemberSession {

    override suspend fun isActive(): Boolean {
        return context.isMemberActive().first()
    }

    override suspend fun setActive(isActive: Boolean) {
        context.setMemberActive(isActive)
    }
}
