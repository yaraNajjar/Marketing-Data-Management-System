import React, { useState, useRef } from "react";
import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { Toast } from "primereact/toast";
import Table from "./Table";
import { LuFileScan } from "react-icons/lu";
import { InputText } from "primereact/inputtext";
import { Dropdown } from "primereact/dropdown";
import { classNames } from "primereact/utils";
import { emptyAction } from "../shared/globalConst";
import ConditionBuilder from "./ConditionBuilder";
import { useAuth } from "./AuthContext";
import { useFetchWithAuth } from "../utils/useFetchWithAuth";

const ActionList = ({ actions, setActions, metrics, fetchActions }) => {
  const fetchWithAuth = useFetchWithAuth();
  const [dialogVisible, setDialogVisible] = useState(false);
  const [currentAction, setCurrentAction] = useState(null);
  const [deleteDialogVisible, setDeleteDialogVisible] = useState(false);
  const [disableDialogVisible, setDisableDialogVisible] = useState(false);

  const toast = useRef(null);
  const { currentUserId, isAdmin, currentUserRoles } = useAuth();

  const hasPermission = (requiredRole) =>
    currentUserRoles.includes(requiredRole);

  const hours = Array.from({ length: 24 }, (_, i) => ({
    label: `${i.toString().padStart(2, "0")}`,
    value: `${i.toString().padStart(2, "0")}`,
  }));

  const minutes = [
    { label: "00", value: "00" },
    { label: "30", value: "30" },
  ];
  const days = [
    { label: "EveryDay", value: 0 },
    { label: "Sunday", value: 1 },
    { label: "Monday", value: 2 },
    { label: "Tuesday", value: 3 },
    { label: "Wednesday", value: 4 },
    { label: "Thursday", value: 5 },
    { label: "Friday", value: 6 },
    { label: "Saturday", value: 7 },
  ];

  const actionTypes = [
    { label: "SMS", value: "SMS" },
    { label: "Whatsapp", value: "WHATSAPP" },
    { label: "Email", value: "EMAIL" },
  ];

  const validateToField = () => {
    if ([0, 1].includes(currentAction?.actionType)) {
      return /^\+\d{10,15}$/.test(currentAction.sendTo);
    } else if (currentAction?.actionType === 3) {
      return /\S+@\S+\.\S+/.test(currentAction?.sendTo);
    }
    return true;
  };

  const onInputChange = (e, name) => {
    const val = (e.target && e.target.value) || "";
    setCurrentAction({ ...currentAction, [name]: val });
  };

  const onDropdownChange = (e, name) => {
    setCurrentAction({ ...currentAction, [name]: e.value });
  };

  const triggerManualScan = async () => {
    try {
      const response = await fetchWithAuth(
        `http://localhost:9091/loader/trigger`
      );

      const resultText = await response.text();

      if (response.ok) {
        toast.current.show({
          severity: "info",
          summary: "Manual Scan",
          detail: resultText,
        });
      } else {
        throw new Error(resultText);
      }
    } catch (error) {
      toast.current.show({
        severity: "error",
        summary: "Error Running Manual Scan",
        detail: error.message,
      });
    }
  };

  const disableDialogFooter = (
    <div>
      <Button
        label="Yes"
        icon="pi pi-check"
        className="p-button-text"
        onClick={() => {
          disableAction();
          setDisableDialogVisible(false);
        }}
      />
      <Button
        label="No"
        icon="pi pi-times"
        className="p-button-text"
        onClick={() => setDisableDialogVisible(false)}
      />
    </div>
  );

  const disableAction = async () => {
    try {
      const response = await fetchWithAuth(
        `http://localhost:9097/actions/disable/${currentAction.id}`,
        "PUT"
      );

      const resultText = await response.text();

      if (response.ok) {
        await fetchActions();
        toast.current.show({
          severity: "info",
          summary: "Action Disabled",
          detail: resultText,
        });
      } else {
        throw new Error(resultText);
      }
    } catch (error) {
      toast.current.show({
        severity: "error",
        summary: "Error Disabling Action",
        detail: error.message,
      });
    }
  };

  const enableAction = async (actionId) => {
    try {
      const response = await fetchWithAuth(
        `http://localhost:9097/actions/enable/${actionId}`,
        "PUT"
      );
      const resultText = await response.text();
      if (response.ok) {
        await fetchActions(); // Reload data to reflect changes
        toast.current.show({
          severity: "success",
          summary: "Action Enabled",
          detail: resultText,
        });
      } else {
        throw new Error(resultText);
      }
    } catch (error) {
      toast.current.show({
        severity: "error",
        summary: "Error Enabling Action",
        detail: error.message,
      });
    }
  };

  const openNewActionDialog = () => {
    setCurrentAction(emptyAction);
    setDialogVisible(true);
  };

  const openEditDialog = (action) => {
    setCurrentAction(action);
    setDialogVisible(true);
  };

  const handleDeleteAction = (action) => {
    setCurrentAction(action);
    setDeleteDialogVisible(true);
  };

  const actionBodyTemplate = (rowData) => {
    return (
      <React.Fragment>
        <Button
          icon="pi pi-pencil"
          className="p-button-rounded p-button-secondary mr-2"
          tooltipOptions={{ showOnDisabled: true, position: "top" }}
          onClick={() => openEditDialog(rowData)}
          disabled={!hasPermission("ROLE_UPDATE_ACTION")}
          tooltip={
            hasPermission("ROLE_UPDATE_ACTION")
              ? "Edit"
              : "You don't have permission to edit"
          }
        />
        {rowData.enabled ? (
          <Button
            icon="pi pi-pause-circle"
            className="p-button-rounded p-button-warning mr-2"
            tooltipOptions={{ showOnDisabled: true, position: "top" }}
            onClick={() => {
              setCurrentAction(rowData);
              setDisableDialogVisible(true);
            }}
            disabled={!hasPermission("ROLE_UPDATE_ACTION")}
            tooltip={
              hasPermission("ROLE_UPDATE_ACTION")
                ? "Disable"
                : "You don't have permission to disable"
            }
          />
        ) : (
          <Button
            icon="pi pi-play-circle"
            className="p-button-rounded p-button-success mr-2"
            tooltipOptions={{ showOnDisabled: true, position: "top" }}
            onClick={() => enableAction(rowData.id)}
            disabled={!hasPermission("ROLE_UPDATE_ACTION")}
            tooltip={
              hasPermission("ROLE_UPDATE_ACTION")
                ? "Enable"
                : "You don't have permission to enable"
            }
          />
        )}
        <Button
          icon="pi pi-trash"
          className="p-button-rounded p-button-danger"
          tooltipOptions={{ showOnDisabled: true, position: "top" }}
          onClick={() => handleDeleteAction(rowData)}
          disabled={!hasPermission("ROLE_DELETE_ACTION")}
          tooltip={
            hasPermission("ROLE_DELETE_ACTION")
              ? "Delete"
              : "You don't have permission to delete"
          }
        />
      </React.Fragment>
    );
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
                return metric ? metric.name : "Unknown Metric";
              })
              .join(" && ")}
          </React.Fragment>
        ))}
      </span>
    );
  };

  const deleteAction = async () => {
    if (!currentAction?.id) {
      toast.current.show({
        severity: "error",
        summary: "Deletion Error",
        detail: "No action selected for deletion.",
      });
      return;
    }

    try {
      const response = await fetchWithAuth(
        `http://localhost:9097/actions/${currentAction.id}`,
        "DELETE"
      );

      const resultText = await response.text();

      if (response.ok) {
        setActions((prevActions) =>
          prevActions.filter((action) => action.id !== currentAction.id)
        );
        toast.current.show({
          severity: "info",
          summary: "Action Deleted",
          detail: resultText,
        });
      } else {
        throw new Error(resultText);
      }
    } catch (error) {
      toast.current.show({
        severity: "error",
        summary: "Deletion Failed",
        detail: error.message,
      });
    }

    // Hide dialog and clear current action
    setDeleteDialogVisible(false);
    setCurrentAction(emptyAction); // Reset or clear the current action
  };

  const deleteDialogFooter = (
    <div>
      <Button
        label="Yes"
        icon="pi pi-check"
        className="p-button-text"
        onClick={deleteAction}
      />
      <Button
        label="No"
        icon="pi pi-times"
        className="p-button-text"
        onClick={() => setDeleteDialogVisible(false)}
      />
    </div>
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
    ...(isAdmin
      ? [
          {
            field: "accountId",
            header: "Created By",
            body: (rowData) => rowData.accountId,
            sortable: true,
          },
        ]
      : []),
    { field: "actions", header: "Actions", body: actionBodyTemplate },
  ];

  const hideDialog = () => {
    setDialogVisible(false); // Function to hide the dialog
    setCurrentAction(emptyAction);
  };

  const saveAction = async () => {
    if (
      !currentAction?.name ||
      !currentAction?.notificationText ||
      !validateToField()
    ) {
      toast.current.show({
        severity: "error",
        summary: "Validation Error",
        detail: "Please check your inputs and try again.",
      });
      return;
    }

    currentAction.accountId = currentUserId;

    const url = currentAction.id
      ? `http://localhost:9097/actions/${currentAction.id}`
      : `http://localhost:9097/actions/create`;
    const method = currentAction.id ? "PUT" : "POST";

    try {
      const response = await fetchWithAuth(url, method, currentAction);

      const resultText = await response.text(); // Get the response as plain text

      if (response.ok) {
        // After successful API response, re-fetch actions
        await fetchActions();
        toast.current.show({
          severity: method === "POST" ? "success" : "success",
          summary: method === "POST" ? "Action Added" : "Action Updated",
          detail: resultText,
        });
      } else {
        throw new Error(`Failed to save action: ${resultText}`);
      }
    } catch (error) {
      toast.current.show({
        severity: "error",
        summary: "Error",
        detail: error.message,
      });
    }

    hideDialog();
  };

  const dialogFooter = (
    <div>
      <Button
        label="Save"
        icon="pi pi-check"
        className="p-button-text"
        onClick={saveAction}
        disabled={
          !hasPermission(
            currentAction?.id ? "ROLE_UPDATE_ACTION" : "ROLE_CREATE_ACTION"
          )
        }
        tooltip={
          hasPermission(
            currentAction?.id ? "ROLE_UPDATE_ACTION" : "ROLE_CREATE_ACTION"
          )
            ? "Save"
            : "You don't have permission to save this action"
        }
        tooltipOptions={{ showOnDisabled: true, position: "top" }}
      />
      <Button
        label="Cancel"
        icon="pi pi-times"
        className="p-button-text"
        onClick={hideDialog}
      />
    </div>
  );

  const actionButtons = (
    <>
      <Button
        label="Trigger Manual Scan"
        icon={<LuFileScan />}
        onClick={triggerManualScan}
        className="p-button-sm mr-2"
        severity="info"
        disabled={!hasPermission("ROLE_TRIGGER")}
        tooltip={
          hasPermission("ROLE_TRIGGER")
            ? "Trigger Manual Scan"
            : "You don't have permission to trigger manual scan"
        }
        tooltipOptions={{ showOnDisabled: true, position: "bottom" }}
      />
      <Button
        label="Add New Action"
        icon="pi pi-plus"
        onClick={openNewActionDialog}
        className="p-button-sm"
        disabled={!hasPermission("ROLE_CREATE_ACTION") || metrics.length === 0}
        tooltip={
          metrics.length === 0
            ? "You can't create a new action before creating metrics"
            : hasPermission("ROLE_CREATE_ACTION")
            ? "Add New Action"
            : "You don't have permission to add new action"
        }
        tooltipOptions={{ showOnDisabled: true, position: "bottom" }}
      />
    </>
  );

  const handleConditionChange = (newConditions) => {
    setCurrentAction((prevAction) => ({
      ...prevAction,
      conditions: newConditions,
    }));
  };

  const addNewActionDialog = (
    <Dialog
      visible={dialogVisible}
      onHide={hideDialog}
      footer={dialogFooter}
      style={{ width: "35%" }}
      header="New Action Details"
    >
      <div className="p-fluid">
        <div className="p-field m-1">
          <label htmlFor="action-name">Action Name</label>
          <InputText
            id="action-name"
            value={currentAction?.name}
            onChange={(e) =>
              setCurrentAction({ ...currentAction, name: e.target.value })
            }
          />
        </div>
        <br />
        <div className="p-field m-1">
          <label>Run on Time</label>
          <div className="formgrid grid">
            <div className="field col w-1">
              <Dropdown
                value={currentAction?.runOnTime.split(":")[0]}
                options={hours}
                onChange={(e) =>
                  setCurrentAction({
                    ...currentAction,
                    runOnTime: `${e.value}:${
                      currentAction?.runOnTime.split(":")[1].split(" ")[0]
                    }`,
                  })
                }
                placeholder="Hour"
                className="p-mr-2 p-dropdown-sm"
              />
            </div>
            <div className="field col w-1">
              <Dropdown
                value={currentAction?.runOnTime.split(":")[1].split(" ")[0]}
                options={minutes}
                onChange={(e) =>
                  setCurrentAction({
                    ...currentAction,
                    runOnTime: `${currentAction?.runOnTime.split(":")[0]}:${
                      e.value
                    }`,
                  })
                }
                placeholder="Minute"
                className="p-dropdown-sm"
              />
            </div>
            <div className="field col w-1">
              <Dropdown
                value={currentAction?.runOnTime.split(" ")[1]}
                options={[
                  { label: "am", value: "am" },
                  { label: "pm", value: "pm" },
                ]}
                onChange={(e) =>
                  setCurrentAction({
                    ...currentAction,
                    runOnTime: `${currentAction.runOnTime.split(" ")[0]} ${
                      e.value
                    }`,
                  })
                }
                placeholder="am/pm"
                className="p-mr-2 p-dropdown-sm"
              />
            </div>
          </div>
        </div>
        <br />
        <div className="p-field m-1">
          <label htmlFor="run-on-day">Run on Day</label>
          <Dropdown
            id="run-on-day"
            value={currentAction?.runOnDay}
            options={days}
            onChange={(e) =>
              setCurrentAction({ ...currentAction, runOnDay: e.value })
            }
            placeholder="Select Day"
          />
        </div>
        <br />
        <div className="p-field m-1">
          <label htmlFor="action-type">Action Type</label>
          <Dropdown
            id="action-type"
            value={currentAction?.actionType}
            options={actionTypes}
            onChange={(e) => onDropdownChange(e, "actionType")}
            placeholder="Select Type"
          />
        </div>
        <br />
        <div className="p-field m-1">
          <ConditionBuilder
            metrics={metrics}
            initialConditions={currentAction?.conditions}
            onChange={handleConditionChange}
          />
        </div>
        <br />
        <div className="p-field m-1">
          <label htmlFor="to">To</label>
          <InputText
            id="to"
            value={currentAction?.sendTo}
            onChange={(e) => onInputChange(e, "sendTo")}
            className={classNames({ "p-invalid": !validateToField() })}
          />
        </div>
        <br />
        <div className="p-field m-1">
          <label htmlFor="notification-text">Notification Text</label>
          <InputText
            id="notification-text"
            value={currentAction?.notificationText}
            onChange={(e) => onInputChange(e, "notificationText")}
            required
          />
        </div>
      </div>
    </Dialog>
  );

  const deleteDialog = (
    <Dialog
      visible={deleteDialogVisible}
      onHide={() => setDeleteDialogVisible(false)}
      footer={deleteDialogFooter}
      header="Confirm Delete"
    >
      Are you sure you want to delete this action?
    </Dialog>
  );
  const disableDialog = (
    <Dialog
      visible={disableDialogVisible}
      onHide={() => setDisableDialogVisible(false)}
      footer={disableDialogFooter}
      header="Confirm Disable"
    >
      Are you sure you want to disable this action?
    </Dialog>
  );

  return (
    <div className="m-1">
      <Toast ref={toast} />
      {addNewActionDialog}
      {disableDialog}
      {deleteDialog}

      <Table
        tableName={"Actions"}
        actionButtons={actionButtons}
        data={actions}
        columns={columns}
        paginator
        rows={5}
      />
    </div>
  );
};

export default ActionList;
