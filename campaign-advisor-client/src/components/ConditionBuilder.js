import React, { useState, useEffect } from "react";
import { Button } from "primereact/button";
import { Dropdown } from "primereact/dropdown";
import { Tag } from "primereact/tag";

const ConditionBuilder = ({ metrics, initialConditions, onChange }) => {
  const [groups, setGroups] = useState(initialConditions || []);

  useEffect(() => {
    if (JSON.stringify(groups) !== JSON.stringify(initialConditions)) {
      onChange(groups);
    }
  }, [groups, onChange, initialConditions]);

  const addGroup = () => {
    setGroups((prevGroups) => [...prevGroups, []]);
  };

  const addMetricToGroup = (groupIndex, metricId) => {
    setGroups((prevGroups) =>
      prevGroups.map((group, index) => {
        if (index === groupIndex) {
          // Prevent adding the same metric twice
          return group.includes(metricId) ? group : [...group, metricId];
        }
        return group;
      })
    );
  };

  const removeMetricFromGroup = (groupIndex, metricIndex) => {
    setGroups((prevGroups) =>
      prevGroups.map((group, index) => {
        if (index === groupIndex) {
          return group.filter((_, idx) => idx !== metricIndex);
        }
        return group;
      })
    );
  };

  const removeGroup = (groupIndex) => {
    setGroups((prevGroups) =>
      prevGroups.filter((_, index) => index !== groupIndex)
    );
  };

  return (
    <div className="">
      <hr />
      <div className=" flex justify-items-center gap-2">
        <label htmlFor="action-type" className="pt-2">
          Condition
        </label>
        <Button
          label="Add Group"
          onClick={addGroup}
          rounded
          className="mb-3 w-auto"
          icon="pi pi-plus"
        />
      </div>
      {groups.map((group, groupIndex) => (
        <div key={groupIndex} className="p-col-12 p-shadow-2 p-p-3 p-mb-4">
          <div className="grid align-items-center mb-3 gap-3">
            <h4 className="col">
              {groupIndex >= 1 ? "OR " : ""}Group {groupIndex + 1}
            </h4>
            <Button
              size="small"
              label="Remove Group"
              onClick={() => removeGroup(groupIndex)}
              className="col"
              severity="danger"
              rounded
              outlined
              icon="pi pi-trash"
            />
            <hr />
          </div>
          <div className="ml-3">
            {group.map((metricId, metricIndex) => (
              <Tag key={metricIndex} severity="info" className="mr-2 mb-1">
                <div className="flex align-items-center gap-2">
                  <div key={metricIndex}>
                    <Button
                      icon="pi pi-minus"
                      rounded
                      severity="secondary"
                      className="ml-2"
                      onClick={() =>
                        removeMetricFromGroup(groupIndex, metricIndex)
                      }
                    />
                  </div>
                  <span className="text-base text-sm">
                    {metrics.find((m) => m.id === metricId).name}
                  </span>
                </div>
              </Tag>
            ))}
          </div>
          <Dropdown
            value=""
            options={metrics}
            onChange={(e) => addMetricToGroup(groupIndex, e.value)}
            placeholder="Select Metric"
            optionLabel="name"
            optionValue="id"
            className="p-mt-2"
          />
        </div>
      ))}
      <hr />
    </div>
  );
};

export default ConditionBuilder;
