package org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent

class AdminDropdownMenuContent(
    onInfoClick: () -> Unit,
    onStopSurveyClick: () -> Unit,
    onDeleteClick: () -> Unit,
) : PostedAnnouncementDropdownMenuContentBase(onInfoClick, onDeleteClick, onStopSurveyClick)