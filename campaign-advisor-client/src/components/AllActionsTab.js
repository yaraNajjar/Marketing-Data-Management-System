import React, { useState, useEffect, useRef } from "react";
import ActionList from "./ActionList";
import MetricList from "./MetricList";
import { Toast } from "primereact/toast";
import { useFetchWithAuth } from "../utils/useFetchWithAuth";

const AllActionsTab = () => {
  const fetchWithAuth = useFetchWithAuth();
  const [metrics, setMetrics] = useState([]);
  const [actions, setActions] = useState([]);
  const toast = useRef(null);

  // Assuming user_id is provided via currentUserId context
  const fetchMetrics = async () => {
    try {
      const response = await fetchWithAuth(
        `http://localhost:9096/metrics/active/all`
      );
      if (response.ok) {
        const data = await response.json();
        setMetrics(data || []);
      } else {
        throw new Error("Failed to fetch metrics");
      }
    } catch (error) {
      toast.current.show({
        severity: "error",
        summary: "Error fetching metrics",
        detail: error.message,
      });
    }
  };

  const fetchActions = async () => {
    try {
      const response = await fetchWithAuth(
        `http://localhost:9097/actions/active/all`
      );
      if (response.ok) {
        const data = await response.json();
        setActions(data || []);
      } else {
        throw new Error("Failed to fetch actions");
      }
    } catch (error) {
      toast.current.show({
        severity: "error",
        summary: "Error fetching actions",
        detail: error.message,
      });
    }
  };
  useEffect(() => {
    fetchMetrics();
    fetchActions();
  }, []);

  return (
    <>
      <Toast ref={toast} />
      <ActionList
        actions={actions}
        setActions={setActions}
        metrics={metrics}
        fetchActions={fetchActions}
      />
      <hr />
      <MetricList
        metrics={metrics}
        setMetrics={setMetrics}
        fetchMetrics={fetchMetrics}
      />
    </>
  );
};

export default AllActionsTab;
