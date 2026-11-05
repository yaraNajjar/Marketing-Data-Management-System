export const rolesFormatHeader = (header) => {
  const roleMap = {
    ROLE_ADMIN: "Admin",
    ROLE_CREATE_ACTION: "Create Action",
    ROLE_UPDATE_ACTION: "Update Action",
    ROLE_DELETE_ACTION: "Delete Action",
    ROLE_CREATE_METRIC: "Create Metric",
    ROLE_UPDATE_METRIC: "Update Metric",
    ROLE_DELETE_METRIC: "Delete Metric",
    ROLE_TRIGGER: "Trigger Manual Scan",
    ROLE_USER: "User",
  };

  // Check if header exists in the map
  return (
    roleMap[header] ||
    header
      .split("_")
      .slice(1)
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(" ")
  );
};
