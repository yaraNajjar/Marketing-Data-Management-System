import React, { useState, useEffect, useRef } from "react";
import ActionListHistory from "./ActionListHistory";
import MetricListHistory from "./MetricListHistory";
import { useFetchWithAuth } from "../utils/useFetchWithAuth";
import { useAuth } from "./AuthContext";

const HistoryTab = () => {
  const { currentUserId } = useAuth();
  const fetchWithAuth = useFetchWithAuth();
  const [metrics, setMetrics] = useState([]);
  const [actions, setActions] = useState([]);
  const toast = useRef(null);

  // Assuming user_id is provided via currentUserId context
  const fetchMetrics = async () => {
    try {
      const response = await fetchWithAuth(
        `http://localhost:9096/metrics/history/${currentUserId}`
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
        `http://localhost:9097/actions/history/${currentUserId}`
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
  }, [currentUserId]);
  return (
    <>
      <ActionListHistory
        actions={actions}
        setActions={setActions}
        fetchActions={fetchActions}
        metrics={metrics}
      />
      <hr />
      <MetricListHistory
        metrics={metrics}
        setMetrics={setMetrics}
        fetchMetrics={fetchMetrics}
      />
    </>
  );
};

export default HistoryTab;
