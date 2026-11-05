export const emptyAction = {
  name: "",
  accountId: sessionStorage.getItem("currentUserId"),
  actionType: "",
  runOnTime: "00:00",
  runOnDay: "",
  notificationText: "",
  conditions: [],
  sendTo: "",
  deleted: false,
};

export const metricOptions = [
  { label: "Impressions", value: "impressions" },
  { label: "Views", value: "views" },
  { label: "Clicks", value: "clicks" },
  { label: "CTR", value: "ctr" },
  { label: "Likes", value: "likes" },
  { label: "Comments", value: "comments" },
  { label: "Shares", value: "shares" },
  { label: "Engagement Rate", value: "engagementRate" },
];

export const emptyMetricConst = {
  name: "",
  accountId: sessionStorage.getItem("currentUserId"),
  metric: "",
  threshold: "",
  time_frame_hours: "",
};
