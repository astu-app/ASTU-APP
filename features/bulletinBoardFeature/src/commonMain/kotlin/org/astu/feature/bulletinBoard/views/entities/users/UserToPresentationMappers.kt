package org.astu.feature.bulletinBoard.views.entities.users

import org.astu.feature.bulletinBoard.models.entities.audience.MemberWithRights
import org.astu.feature.bulletinBoard.models.entities.audience.User
import org.astu.feature.bulletinBoard.views.entities.userGroups.details.UserSummaryWithUserRights
import kotlin.jvm.JvmName

object UserToPresentationMappers {
    @JvmName("UserCollectionToPresentations")
    fun Collection<User>.toPresentations(): List<UserSummary> =
        this.map { it.toPresentation() }

    @JvmName("UserToPresentation")
    fun User.toPresentation(): UserSummary =
        UserSummary(this.id, this.firstName, this.secondName, this.patronymic)

    @JvmName("MemberWithRightsToPresentation")
    fun MemberWithRights.toPresentation(): UserSummaryWithUserRights =
        UserSummaryWithUserRights(
            user = this.user.toPresentation(),
            canViewAnnouncements = this.rights.canViewAnnouncements,
            canCreateAnnouncements = this.rights.canCreateAnnouncements,
            canCreateSurveys = this.rights.canCreateSurveys,
            canRuleUserGroupHierarchy = this.rights.canRuleUserGroupHierarchy,
            canViewUserGroupDetails = this.rights.canViewUserGroupDetails,
            canCreateUserGroups = this.rights.canCreateUserGroups,
            canEditUserGroups = this.rights.canEditUserGroups,
            canEditMembers = this.rights.canEditMembers,
            canEditMemberRights = this.rights.canEditMemberRights,
            canEditUserGroupAdmin = this.rights.canEditUserGroupAdmin,
            canDeleteUserGroup = this.rights.canDeleteUserGroup,
        )
}