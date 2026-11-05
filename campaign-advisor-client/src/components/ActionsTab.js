import React, { useState, useEffect, useRef } from "react";
import ActionList from "./ActionList";
import MetricList from "./MetricList";
import { useFetchWithAuth } from "../utils/useFetchWithAuth";
import { useAuth } from "./AuthContext";

const ActionsTab = () => {
  const { currentUserId } = useAuth();
  const fetchWithAuth = useFetchWithAuth();
  const [metrics, setMetrics] = useState([]);
  const [actions, setActions] = useState([]);
  const toast = useRef(null);

  // Assuming user_id is provided via currentUserId context
  const fetchMetrics = async () => {
    const response = await fetchWithAuth(
      `http://localhost:9096/metrics/active/${currentUserId}`
    );
    if (response.ok) {
      const fetchedMetrics = await response.json();
      setMetrics(fetchedMetrics);
    } else {
      toast.current.show({
        severity: "error",
        summary: "Fetch Failed",
        detail: `Failed to fetch metrics.`,
      });
    }
  };

  const fetchActions = async () => {
    try {
      const response = await fetchWithAuth(
        `http://localhost:9097/actions/active/${currentUserId}`
      );
      if (response.ok) {
        const data = await response.json();
        setActions(data);
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

export default ActionsTab;
