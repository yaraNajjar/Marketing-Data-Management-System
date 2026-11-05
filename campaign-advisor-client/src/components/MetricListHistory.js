import React, { useRef } from "react";
import { Button } from "primereact/button";
import { Toast } from "primereact/toast";
import Table from "./Table";
import { useAuth } from "./AuthContext";
import { useFetchWithAuth } from "../utils/useFetchWithAuth";

const MetricsListHistory = ({ metrics, fetchMetrics }) => {
  const fetchWithAuth = useFetchWithAuth();
  const { currentUserRoles } = useAuth();
  const toast = useRef(null);

  const hasPermission = (requiredRole) =>
    currentUserRoles.includes(requiredRole);

  const enableMetric = async (metricId) => {
    try {
      const response = await fetchWithAuth(
        `http://localhost:9096/metrics/revive/${metricId}`,
          "PUT"
      );
      const resultText = await response.text();

      if (response.ok) {
        await fetchMetrics();
        toast.current.show({
          severity: "success",
          summary: "Enable Metric",
          detail: resultText,
        });
      } else {
        throw new Error("Failed to enable the metric");
      }
    } catch (error) {
      toast.current.show({
        severity: "error",
        summary: "Enable Metric Failed",
        detail: error.message || "Error enabling metric",
      });
    }
  };

  const actionBodyTemplate = (rowData) => (
    <React.Fragment>
      <Button
        icon="pi pi-play-circle"
        className="p-button-rounded p-button-success mr-2"
        disabled={!hasPermission("ROLE_UPDATE_METRIC")}
        tooltip={
          hasPermission("ROLE_UPDATE_METRIC")
            ? "Enable"
            : "You don't have permission to enable metrics"
        }
        tooltipOptions={{ showOnDisabled: true, position: "top" }}
        onClick={() => enableMetric(rowData.id)}
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

  return (
    <div className="m-1">
      <Toast ref={toast} />
      <div className="flex justify-content-end mr-4"></div>
      <Table
        tableName={"Metrics History"}
        data={metrics}
        columns={columns}
        paginator
        rows={5}
      />
    </div>
  );
};

export default MetricsListHistory;
