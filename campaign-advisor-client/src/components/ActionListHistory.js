import React, { useRef } from "react";
import { Button } from "primereact/button";
import { Toast } from "primereact/toast";
import Table from "./Table";
import { useFetchWithAuth } from "../utils/useFetchWithAuth";
import { useAuth } from "./AuthContext";

const ActionListHistory = ({ actions, metrics, fetchActions }) => {
  const { currentUserId, currentUserRoles } = useAuth();
  const fetchWithAuth = useFetchWithAuth();
  const toast = useRef(null);

  const hasPermission = (requiredRole) =>
    currentUserRoles.includes(requiredRole);

  const enableAction = async (actionId) => {
    try {
      const response = await fetchWithAuth(
        `http://localhost:9097/actions/restore/${actionId}`,
        "PUT"
      );

      const textReturned = await response.text(); // assuming the server sends some data back
      if (response.ok) {
        await fetchActions();
        toast.current.show({
          severity: "success",
          summary: "Enable Action",
          detail: textReturned,
        });
      } else {
        throw new Error(textReturned);
      }
    } catch (error) {
      toast.current.show({
        severity: "error",
        summary: "Enable Action Failed",
        detail: error.message,
      });
    }
  };

  const conditionBodyTemplate = (conditions) => {
    return (
      <span>
        {conditions.map((conditionGroup, index) => (
          <React.Fragment key={index}>
            {index > 0 && " OR "}
            {conditionGroup
              .map((metricId) => {
                const metric = metrics.find((m) => m.id === metricId);
                return metric ? metric.name : "";
              })
              .join(" && ")}
          </React.Fragment>
        ))}
      </span>
    );
  };

  const actionBodyTemplate = (id) => (
    <React.Fragment>
      <Button
        icon="pi pi-play-circle"
        className="p-button-rounded p-button-success mr-2"
        disabled={!hasPermission("ROLE_UPDATE_ACTION")}
        tooltip={
          hasPermission("ROLE_UPDATE_ACTION")
            ? "Revive"
            : "You don't have permission to disable"
        }
        tooltipOptions={{ showOnDisabled: true, position: "top" }}
        onClick={() => enableAction(id)}
      />
    </React.Fragment>
  );

  const columns = [
    {
      field: "id",
      header: "ID",
      body: (rowData) => rowData.id,
      sortable: true,
    },
    {
      field: "name",
      header: "Name",
      body: (rowData) => rowData.name,
      sortable: true,
    },
    {
      field: "createDate",
      header: "Created at",
      body: (rowData) => rowData.createDate,
      sortable: true,
    },
    {
      field: "condition",
      header: "Condition",
      body: (rowData) => conditionBodyTemplate(rowData.conditions),
    },
    {
      field: "actionType",
      header: "Type",
      body: (rowData) => rowData.actionType,
      sortable: true,
    },
    {
      field: "runOnTime",
      header: "Run at",
      body: (rowData) => rowData.runOnTime,
      sortable: true,
    },
    {
      field: "runOnDay",
      header: "Run on day",
      body: (rowData) => rowData.runOnDay,
      sortable: true,
    },
    {
      field: "notificationText",
      header: "Notification text",
      body: (rowData) => rowData.notificationText,
      sortable: true,
    },
    {
      field: "to",
      header: "Send to",
      body: (rowData) => rowData.sendTo,
      sortable: true,
    },
    {
      field: "actions",
      header: "Actions",
      body: (rowData) => actionBodyTemplate(rowData.id),
    },
  ];

  return (
    <div className="m-1">
      <Toast ref={toast} />
      <Table
        tableName={"Actions History"}
        data={actions}
        columns={columns}
        paginator
        rows={5}
      />
    </div>
  );
};

export default ActionListHistory;
