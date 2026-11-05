import React, { useState, useEffect, useRef } from "react";
import Table from "./Table";
import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Dropdown } from "primereact/dropdown";
import { InputNumber } from "primereact/inputnumber";
import { useAuth } from "./AuthContext";
import { Toast } from "primereact/toast";
import { metricOptions, emptyMetricConst } from "../shared/globalConst";
import { useFetchWithAuth } from "../utils/useFetchWithAuth";

const MetricList = ({ metrics, setMetrics, fetchMetrics }) => {
  const fetchWithAuth = useFetchWithAuth();
  const { currentUserId, currentUserRoles } = useAuth();
  const [dialogVisible, setDialogVisible] = useState(false);
  const [currentMetric, setCurrentMetric] = useState(null);
  const [deleteDialogVisible, setDeleteDialogVisible] = useState(false);
  const toast = useRef(null);

  const hasPermission = (requiredRole) =>
    currentUserRoles.includes(requiredRole);

  useEffect(() => {
    fetchMetrics();
  }, []);

  const openNewMetricDialog = () => {
    setCurrentMetric(emptyMetricConst);
    setDialogVisible(true);
  };

  const openEditDialog = (metric) => {
    setCurrentMetric(metric);
    setDialogVisible(true);
  };

  const hideDialog = () => {
    setDialogVisible(false);
    setCurrentMetric(emptyMetricConst);
  };

  const saveMetric = async () => {
    currentMetric.accountId = currentUserId;

    const url = currentMetric.id
      ? `http://localhost:9096/metrics/${currentMetric.id}`
      : "http://localhost:9096/metrics/create";
    const method = currentMetric.id ? "PUT" : "POST";

    try {
      const response = await fetchWithAuth(url, method, currentMetric);
      const resultText = await response.text();

      if (response.ok) {
        await fetchMetrics();
        toast.current.show({
          severity: "success",
          summary: currentMetric.id ? "Metric Updated" : "Metric Added",
          detail: resultText,
        });
      } else {
        throw new Error(resultText);
      }
    } catch (error) {
      toast.current.show({
        severity: "error",
        summary: "Error",
        detail: `Failed to save metric: ${error.message}`,
      });
    }

    hideDialog(); // Hide dialog after save or on error
  };

  const handleDeleteMetric = (metric) => {
    setCurrentMetric(metric);
    setDeleteDialogVisible(true);
  };

  const deleteMetric = async () => {
    if (!currentMetric?.id) {
      toast.current.show({
        severity: "error",
        summary: "Deletion Error",
        detail: "No metric selected for deletion.",
      });
      return;
    }

    try {
      const response = await fetchWithAuth(
        `http://localhost:9096/metrics/${currentMetric.id}`,
        "DELETE"
      );
      const resultText = await response.text();

      if (response.ok) {
        setMetrics((metrics) =>
          metrics.filter((m) => m.id !== currentMetric.id)
        );
        toast.current.show({
          severity: "info",
          summary: "Metric Deleted",
          detail: "The metric has been successfully deleted.",
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

    // Hide dialog and clear current metric
    setDeleteDialogVisible(false);
    setCurrentMetric(null); // Reset or clear the current metric
  };

  const dialogFooter = (
    <div>
      <Button
        label="Save"
        icon="pi pi-check"
        className="p-button-text"
        onClick={saveMetric}
        disabled={
          !hasPermission(
            currentMetric?.id ? "ROLE_UPDATE_METRIC" : "ROLE_CREATE_METRIC"
          )
        }
        tooltip={
          hasPermission(
            currentMetric?.id ? "ROLE_UPDATE_METRIC" : "ROLE_CREATE_METRIC"
          )
            ? "Save"
            : "You don't have permission to save this metric"
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

  const deleteDialogFooter = (
    <div>
      <Button
        label="Yes"
        icon="pi pi-check"
        className="p-button-text"
        onClick={deleteMetric}
      />
      <Button
        label="No"
        icon="pi pi-times"
        className="p-button-text"
        onClick={() => setDeleteDialogVisible(false)}
      />
    </div>
  );

  const actionBodyTemplate = (rowData) => (
    <React.Fragment>
      <Button
        icon="pi pi-pencil"
        className="p-button-rounded p-button-secondary mr-2"
        tooltipOptions={{ position: "top", showOnDisabled: true }}
        onClick={() => openEditDialog(rowData)}
        disabled={!hasPermission("ROLE_UPDATE_METRIC")}
        tooltip={
          hasPermission("ROLE_UPDATE_METRIC")
            ? "Edit"
            : "You don't have permission to edit this metric"
        }
      />
      <Button
        icon="pi pi-trash"
        className="p-button-rounded p-button-danger"
        tooltipOptions={{ position: "top", showOnDisabled: true }}
        onClick={() => handleDeleteMetric(rowData)}
        disabled={!hasPermission("ROLE_DELETE_METRIC")}
        tooltip={
          hasPermission("ROLE_DELETE_METRIC")
            ? "Delete"
            : "You don't have permission to delete this metric"
        }
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
      header: "Create Date",
      body: (rowData) => rowData.createDate,
      sortable: true,
    },
    {
      field: "metric",
      header: "Metric",
      body: (rowData) => rowData.metric,
      sortable: true,
    },
    {
      field: "threshold",
      header: "Threshold",
      body: (rowData) => rowData.threshold,
      sortable: true,
    },
    {
      field: "timeFrameHours",
      header: "Time Frame (hours)",
      body: (rowData) => rowData.timeFrameHours,
      sortable: true,
    },
    { field: "actions", header: "Actions", body: actionBodyTemplate },
  ];

  const actionButtons = (
    <Button
      label="Add New Metric"
      icon="pi pi-plus"
      className="p-button-sm"
      onClick={openNewMetricDialog}
      disabled={!hasPermission("ROLE_CREATE_METRIC")}
      tooltip={
        hasPermission("ROLE_CREATE_METRIC")
          ? "Add New Metric"
          : "You don't have permission to add a new metric"
      }
      tooltipOptions={{ position: "top", showOnDisabled: true }}
    />
  );

  const timeFrameOptions = Array.from({ length: 24 }, (_, i) => ({
    label: `${i + 1} hour${i > 0 ? "s" : ""}`, // Adding plural 's' conditionally
    value: i + 1,
  }));

  const MetricDetailsForm = (
    <Dialog
      visible={dialogVisible}
      onHide={hideDialog}
      footer={dialogFooter}
      style={{ width: "35%" }}
      header="Metric Details"
    >
      <div className="p-fluid">
        <div className="p-field">
          <label htmlFor="metric-name">Name</label>
          <InputText
            id="metric-name"
            value={currentMetric?.name}
            onChange={(e) =>
              setCurrentMetric({ ...currentMetric, name: e.target.value })
            }
          />
        </div>
        <br />
        <div className="p-field">
          <label htmlFor="metric-type">Metric</label>
          <Dropdown
            id="metric-type"
            value={currentMetric?.metric}
            options={metricOptions}
            onChange={(e) =>
              setCurrentMetric({ ...currentMetric, metric: e.value })
            }
            placeholder="Select a Metric"
          />
        </div>
        <br />
        <div className="p-field">
          <label htmlFor="metric-threshold">Threshold (positive integer)</label>
          <InputNumber
            id="metric-threshold"
            value={currentMetric?.threshold}
            onValueChange={(e) =>
              setCurrentMetric({ ...currentMetric, threshold: e.value })
            }
            min={0}
            mode="decimal"
            useGrouping={false}
          />
        </div>
        <br />
        <div className="p-field">
          <label htmlFor="time-frame-hours">Time Frame (hours)</label>
          <Dropdown
            id="time-frame-hours"
            value={currentMetric?.timeFrameHours}
            onChange={(e) =>
              setCurrentMetric({ ...currentMetric, timeFrameHours: e.value })
            }
            options={timeFrameOptions}
            placeholder="Select Time Frame"
          />
        </div>
      </div>
    </Dialog>
  );

  return (
    <div className="m-1">
      <Toast ref={toast} />

      {MetricDetailsForm}

      <Dialog
        visible={deleteDialogVisible}
        onHide={() => setDeleteDialogVisible(false)}
        footer={deleteDialogFooter}
        header="Confirm Delete"
      >
        Are you sure you want to delete this metric?
      </Dialog>
      <Table
        tableName={"Metrics"}
        actionButtons={actionButtons}
        data={metrics}
        columns={columns}
        paginator
        rows={5}
      />
    </div>
  );
};

export default MetricList;
